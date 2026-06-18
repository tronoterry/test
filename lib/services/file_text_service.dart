import 'dart:convert';
import 'dart:io';

import 'package:archive/archive.dart';
import 'package:qnox_pdf_text/qnox_pdf_text.dart';

class UnsupportedFileTypeException implements Exception {
  UnsupportedFileTypeException(this.extension);
  final String extension;

  @override
  String toString() => 'Unsupported file type ".$extension". '
      'Please use a PDF, DOCX, or TXT file.';
}

/// Extracts plain text from the resume / job-description files the user
/// picks, so the rest of the app only ever deals with plain strings.
class FileTextService {
  FileTextService._();

  static final QnoxPdfText _qnoxPdfText = QnoxPdfText();

  static Future<String> extractText(String filePath) async {
    final extension = filePath.toLowerCase().split('.').last;
    switch (extension) {
      case 'pdf':
        return _extractPdf(filePath);
      case 'docx':
        return _extractDocx(filePath);
      case 'txt':
        return File(filePath).readAsString();
      default:
        throw UnsupportedFileTypeException(extension);
    }
  }

  static Future<String> _extractPdf(String filePath) async {
    final text = await _qnoxPdfText.extractText(filePath);
    return text.trim();
  }

  static Future<String> _extractDocx(String filePath) async {
    final bytes = await File(filePath).readAsBytes();
    final archive = ZipDecoder().decodeBytes(bytes);
    final documentFile = archive.findFile('word/document.xml');
    if (documentFile == null) {
      throw const FormatException(
        'This does not look like a valid .docx file.',
      );
    }
    final xml = utf8.decode(documentFile.content);
    return _plainTextFromDocumentXml(xml);
  }

  /// Pulls visible text out of a Word `word/document.xml` payload without
  /// pulling in a full XML parser: paragraphs are delimited by `<w:p>`
  /// elements and visible runs live in `<w:t>` elements.
  static String _plainTextFromDocumentXml(String xml) {
    final paragraphPattern = RegExp(r'<w:p[ >].*?</w:p>', dotAll: true);
    final textPattern = RegExp(r'<w:t[^>]*>(.*?)</w:t>', dotAll: true);
    final tabPattern = RegExp(r'<w:tab\s*/?>');
    final breakPattern = RegExp(r'<w:br\s*/?>');

    final paragraphs = paragraphPattern.allMatches(xml).map((paragraphMatch) {
      final paragraphXml = paragraphMatch
          .group(0)!
          .replaceAll(tabPattern, '\t')
          .replaceAll(breakPattern, '\n');
      final runs = textPattern
          .allMatches(paragraphXml)
          .map((run) => _unescapeXmlEntities(run.group(1) ?? ''));
      return runs.join();
    });

    return paragraphs
        .where((paragraph) => paragraph.trim().isNotEmpty)
        .join('\n');
  }

  static String _unescapeXmlEntities(String input) {
    return input
        .replaceAll('&lt;', '<')
        .replaceAll('&gt;', '>')
        .replaceAll('&quot;', '"')
        .replaceAll('&apos;', "'")
        .replaceAll('&amp;', '&');
  }
}
