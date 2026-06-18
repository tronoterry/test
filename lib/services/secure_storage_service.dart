import 'package:flutter_secure_storage/flutter_secure_storage.dart';

/// Persists the Anthropic API key and model choice in the Android Keystore
/// backed secure storage so they never touch plain-text disk or backups.
class SecureStorageService {
  SecureStorageService._();
  static final SecureStorageService instance = SecureStorageService._();

  static const String defaultModel = 'claude-sonnet-4-6';

  final FlutterSecureStorage _storage = const FlutterSecureStorage();

  static const String _apiKeyKey = 'anthropic_api_key';
  static const String _modelKey = 'anthropic_model';

  Future<String?> getApiKey() => _storage.read(key: _apiKeyKey);

  Future<void> setApiKey(String value) =>
      _storage.write(key: _apiKeyKey, value: value);

  Future<void> clearApiKey() => _storage.delete(key: _apiKeyKey);

  Future<String> getModel() async {
    return await _storage.read(key: _modelKey) ?? defaultModel;
  }

  Future<void> setModel(String value) =>
      _storage.write(key: _modelKey, value: value);
}
