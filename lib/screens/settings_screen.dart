import 'package:flutter/material.dart';

import '../services/secure_storage_service.dart';

class SettingsScreen extends StatefulWidget {
  const SettingsScreen({super.key});

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  final _apiKeyController = TextEditingController();
  final _modelController = TextEditingController();
  bool _obscureApiKey = true;
  bool _loading = true;
  bool _saving = false;

  @override
  void initState() {
    super.initState();
    _load();
  }

  Future<void> _load() async {
    final storage = SecureStorageService.instance;
    final apiKey = await storage.getApiKey();
    final model = await storage.getModel();
    setState(() {
      _apiKeyController.text = apiKey ?? '';
      _modelController.text = model;
      _loading = false;
    });
  }

  Future<void> _save() async {
    setState(() => _saving = true);
    final storage = SecureStorageService.instance;
    await storage.setApiKey(_apiKeyController.text.trim());
    final model = _modelController.text.trim();
    await storage.setModel(
      model.isEmpty ? SecureStorageService.defaultModel : model,
    );
    setState(() => _saving = false);
    if (!mounted) return;
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('Settings saved')),
    );
    Navigator.of(context).pop();
  }

  Future<void> _clearApiKey() async {
    await SecureStorageService.instance.clearApiKey();
    setState(() => _apiKeyController.clear());
  }

  @override
  void dispose() {
    _apiKeyController.dispose();
    _modelController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Settings')),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : ListView(
              padding: const EdgeInsets.all(16),
              children: [
                const Text(
                  'This app calls the Anthropic API directly from your '
                  'device to write the cover letter. You need your own '
                  'API key from console.anthropic.com. The key is stored '
                  'only on this device (Android Keystore) and is never '
                  'sent anywhere except to Anthropic.',
                  style: TextStyle(color: Colors.black54),
                ),
                const SizedBox(height: 20),
                TextField(
                  controller: _apiKeyController,
                  obscureText: _obscureApiKey,
                  decoration: InputDecoration(
                    labelText: 'Anthropic API key',
                    border: const OutlineInputBorder(),
                    suffixIcon: IconButton(
                      icon: Icon(
                        _obscureApiKey
                            ? Icons.visibility_outlined
                            : Icons.visibility_off_outlined,
                      ),
                      onPressed: () =>
                          setState(() => _obscureApiKey = !_obscureApiKey),
                    ),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: _modelController,
                  decoration: const InputDecoration(
                    labelText: 'Model',
                    border: OutlineInputBorder(),
                    helperText: 'Default: claude-sonnet-4-6',
                  ),
                ),
                const SizedBox(height: 24),
                FilledButton(
                  onPressed: _saving ? null : _save,
                  child: _saving
                      ? const SizedBox(
                          height: 18,
                          width: 18,
                          child: CircularProgressIndicator(strokeWidth: 2),
                        )
                      : const Text('Save'),
                ),
                const SizedBox(height: 8),
                OutlinedButton(
                  onPressed: _clearApiKey,
                  child: const Text('Clear saved API key'),
                ),
              ],
            ),
    );
  }
}
