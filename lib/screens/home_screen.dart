import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';

import '../services/cover_letter_service.dart';
import '../services/file_text_service.dart';
import '../services/secure_storage_service.dart';
import 'result_screen.dart';
import 'settings_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final _jobDescriptionController = TextEditingController();

  String? _resumeFileName;
  String? _resumeText;
  bool _isParsingResume = false;
  bool _isParsingJobDescription = false;
  bool _isGenerating = false;
  String? _errorMessage;

  bool get _canGenerate =>
      _resumeText != null &&
      _resumeText!.trim().isNotEmpty &&
      _jobDescriptionController.text.trim().isNotEmpty &&
      !_isGenerating;

  Future<void> _pickResume() async {
    setState(() => _errorMessage = null);
    final result = await FilePicker.platform.pickFiles(
      type: FileType.custom,
      allowedExtensions: ['pdf', 'docx'],
    );
    final file = result?.files.single;
    if (file?.path == null) return;

    setState(() {
      _isParsingResume = true;
      _resumeFileName = file!.name;
    });
    try {
      final text = await FileTextService.extractText(file!.path!);
      setState(() => _resumeText = text);
    } catch (e) {
      setState(() {
        _resumeFileName = null;
        _resumeText = null;
        _errorMessage = 'Could not read resume: $e';
      });
    } finally {
      setState(() => _isParsingResume = false);
    }
  }

  void _removeResume() {
    setState(() {
      _resumeFileName = null;
      _resumeText = null;
    });
  }

  Future<void> _pickJobDescriptionFile() async {
    setState(() => _errorMessage = null);
    final result = await FilePicker.platform.pickFiles(
      type: FileType.custom,
      allowedExtensions: ['pdf', 'docx', 'txt'],
    );
    final file = result?.files.single;
    if (file?.path == null) return;

    setState(() => _isParsingJobDescription = true);
    try {
      final text = await FileTextService.extractText(file!.path!);
      setState(() {
        _jobDescriptionController.text = text;
      });
    } catch (e) {
      setState(() => _errorMessage = 'Could not read job description: $e');
    } finally {
      setState(() => _isParsingJobDescription = false);
    }
  }

  Future<void> _generate() async {
    final apiKey = await SecureStorageService.instance.getApiKey();
    if (apiKey == null || apiKey.trim().isEmpty) {
      setState(() {
        _errorMessage = 'Add your Anthropic API key in Settings first.';
      });
      return;
    }

    setState(() {
      _isGenerating = true;
      _errorMessage = null;
    });

    try {
      final model = await SecureStorageService.instance.getModel();
      final coverLetter = await CoverLetterService.generate(
        apiKey: apiKey,
        model: model,
        resumeText: _resumeText!,
        jobDescription: _jobDescriptionController.text.trim(),
      );
      if (!mounted) return;
      Navigator.of(context).push(
        MaterialPageRoute(
          builder: (_) => ResultScreen(coverLetter: coverLetter),
        ),
      );
    } catch (e) {
      setState(() => _errorMessage = e.toString());
    } finally {
      if (mounted) setState(() => _isGenerating = false);
    }
  }

  @override
  void dispose() {
    _jobDescriptionController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Cover Letter Generator'),
        actions: [
          IconButton(
            icon: const Icon(Icons.settings_outlined),
            onPressed: () => Navigator.of(context).push(
              MaterialPageRoute(builder: (_) => const SettingsScreen()),
            ),
          ),
        ],
      ),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _SectionCard(
            title: '1. Your resume',
            child: _resumeFileName == null
                ? OutlinedButton.icon(
                    onPressed: _isParsingResume ? null : _pickResume,
                    icon: const Icon(Icons.upload_file_outlined),
                    label: const Text('Upload resume (PDF or DOCX)'),
                  )
                : Row(
                    children: [
                      const Icon(Icons.description_outlined),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          _resumeFileName!,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ),
                      IconButton(
                        icon: const Icon(Icons.close),
                        onPressed: _removeResume,
                      ),
                    ],
                  ),
            footer: _isParsingResume
                ? const Padding(
                    padding: EdgeInsets.only(top: 12),
                    child: LinearProgressIndicator(),
                  )
                : null,
          ),
          const SizedBox(height: 16),
          _SectionCard(
            title: '2. Job description',
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                TextField(
                  controller: _jobDescriptionController,
                  maxLines: 8,
                  minLines: 5,
                  decoration: const InputDecoration(
                    border: OutlineInputBorder(),
                    hintText: 'Paste the job description here...',
                  ),
                  onChanged: (_) => setState(() {}),
                ),
                const SizedBox(height: 8),
                OutlinedButton.icon(
                  onPressed:
                      _isParsingJobDescription ? null : _pickJobDescriptionFile,
                  icon: const Icon(Icons.attach_file_outlined),
                  label: const Text('Or upload a file (PDF, DOCX, TXT)'),
                ),
              ],
            ),
            footer: _isParsingJobDescription
                ? const Padding(
                    padding: EdgeInsets.only(top: 12),
                    child: LinearProgressIndicator(),
                  )
                : null,
          ),
          if (_errorMessage != null) ...[
            const SizedBox(height: 16),
            Container(
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.errorContainer,
                borderRadius: BorderRadius.circular(8),
              ),
              child: Text(
                _errorMessage!,
                style: TextStyle(
                  color: Theme.of(context).colorScheme.onErrorContainer,
                ),
              ),
            ),
          ],
          const SizedBox(height: 24),
          FilledButton.icon(
            onPressed: _canGenerate ? _generate : null,
            icon: _isGenerating
                ? const SizedBox(
                    height: 18,
                    width: 18,
                    child: CircularProgressIndicator(strokeWidth: 2),
                  )
                : const Icon(Icons.auto_awesome_outlined),
            label: Text(_isGenerating ? 'Generating...' : 'Generate cover letter'),
          ),
        ],
      ),
    );
  }
}

class _SectionCard extends StatelessWidget {
  const _SectionCard({required this.title, required this.child, this.footer});

  final String title;
  final Widget child;
  final Widget? footer;

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Text(title, style: Theme.of(context).textTheme.titleMedium),
            const SizedBox(height: 12),
            child,
            if (footer != null) footer!,
          ],
        ),
      ),
    );
  }
}
