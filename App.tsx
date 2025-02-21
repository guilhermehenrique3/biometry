import React, {useState} from 'react';
import {View, Text, TouchableOpacity, StyleSheet} from 'react-native';
import {NativeModules} from 'react-native';

const {FingerprintModule} = NativeModules;

const App = () => {
  const [status, setStatus] = useState<string>('Aguardando ação...');

  const handleInitialize = async () => {
    try {
      const response = await FingerprintModule.initialize();
      setStatus(response);
    } catch (error) {
      setStatus('Erro ao inicializar: ' + error.message);
    }
  };

  const handleCapture = async () => {
    try {
      const response = await FingerprintModule.captureFingerprint();
      setStatus(response);
    } catch (error) {
      setStatus('Erro ao capturar digital: ' + error.message);
    }
  };

  const handleClose = async () => {
    try {
      const response = await FingerprintModule.closeDevice();
      setStatus(response);
    } catch (error) {
      setStatus('Erro ao fechar dispositivo: ' + error.message);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Leitor Biométrico</Text>
      <Text style={styles.status}>{status}</Text>

      <TouchableOpacity style={styles.button} onPress={handleInitialize}>
        <Text style={styles.buttonText}>Inicializar SDK</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.button} onPress={handleCapture}>
        <Text style={styles.buttonText}>Capturar Digital</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.button} onPress={handleClose}>
        <Text style={styles.buttonText}>Fechar Dispositivo</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#f5f5f5',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  status: {
    fontSize: 16,
    marginBottom: 20,
    color: '#333',
    textAlign: 'center',
    paddingHorizontal: 20,
  },
  button: {
    backgroundColor: '#007bff',
    padding: 15,
    marginVertical: 10,
    borderRadius: 10,
    width: '80%',
    alignItems: 'center',
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default App;
