import 'dart:convert';

import 'package:http/http.dart' as http;

class CoverLetterGenerationException implements Exception {
  CoverLetterGenerationException(this.message);
  final String message;

  @override
  String toString() => message;
}

/// Calls the Anthropic Messages API directly from the device using the
/// user's own API key (entered in Settings, never sent anywhere else).
class CoverLetterService {
  CoverLetterService._();

  static const String _endpoint = 'https://api.anthropic.com/v1/messages';
  static const String _anthropicVersion = '2023-06-01';

  static Future<String> generate({
    required String apiKey,
    required String model,
    required String resumeText,
    required String jobDescription,
  }) async {
    const systemPrompt = '''
You are an expert career coach who writes concise, compelling, and truthful cover letters.
Write one complete cover letter (3-4 short paragraphs, under 350 words) tailored to the job
description below, using only facts that actually appear in the candidate's resume. Never
invent employers, job titles, dates, or skills that are not in the resume. Match the tone to
the seniority and industry implied by the job description. Output plain text only - no
markdown formatting, and no placeholder brackets unless information truly cannot be inferred.
''';

    final userPrompt = '''
RESUME:
"""
$resumeText
"""

JOB DESCRIPTION:
"""
$jobDescription
"""

Write the cover letter now.
''';

    http.Response response;
    try {
      response = await http.post(
        Uri.parse(_endpoint),
        headers: {
          'content-type': 'application/json',
          'x-api-key': apiKey,
          'anthropic-version': _anthropicVersion,
        },
        body: jsonEncode({
          'model': model,
          'max_tokens': 1200,
          'system': systemPrompt,
          'messages': [
            {'role': 'user', 'content': userPrompt},
          ],
        }),
      );
    } catch (e) {
      throw CoverLetterGenerationException(
        'Could not reach the Anthropic API. Check your internet connection. ($e)',
      );
    }

    if (response.statusCode != 200) {
      throw CoverLetterGenerationException(
        'Request failed (${response.statusCode}): ${_extractErrorMessage(response.body)}',
      );
    }

    final decoded = jsonDecode(response.body) as Map<String, dynamic>;
    final contentBlocks = decoded['content'] as List<dynamic>? ?? const [];
    final text = contentBlocks
        .whereType<Map<String, dynamic>>()
        .where((block) => block['type'] == 'text')
        .map((block) => block['text'] as String? ?? '')
        .join('\n')
        .trim();

    if (text.isEmpty) {
      throw CoverLetterGenerationException(
        'The model returned an empty response. Please try again.',
      );
    }
    return text;
  }

  static String _extractErrorMessage(String responseBody) {
    try {
      final decoded = jsonDecode(responseBody) as Map<String, dynamic>;
      final error = decoded['error'] as Map<String, dynamic>?;
      return error?['message'] as String? ?? responseBody;
    } catch (_) {
      return responseBody;
    }
  }
}
