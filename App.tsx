import React, {useState, useEffect} from 'react';
import {
  View,
  Text,
  Button,
  Image,
  StyleSheet,
  ScrollView,
  Alert,
  NativeModules,
} from 'react-native';

const App = () => {
  const {FingerPrintUareu} = NativeModules;

  const [encodedImage, setEncodedImage] = useState(null);
  const [wsqBase64, setWsqBase64] = useState(null);
  const [usbDevices, setUsbDevices] = useState('');

  const startFingerprintScan = async () => {
    try {
      const result = await FingerPrintUareu.startScan();
      setEncodedImage(result.encoded);
    } catch (error) {
      Alert.alert('Erro', error.message || 'Falha ao iniciar o scan');
    }
  };

  const getConnectedUsbDevices = async () => {
    try {
      const devices = await FingerPrintUareu.getConnectedUsbDevices();
      setUsbDevices(devices);
    } catch (error) {
      Alert.alert('Erro', error.message || 'Falha ao obter dispositivos USB');
    }
  };

  useEffect(() => {
    getConnectedUsbDevices();
  }, []);

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>Captura de Digital</Text>
      <Button title="Iniciar Scan" onPress={startFingerprintScan} />
      <Button title="pegar dispositivos" onPress={getConnectedUsbDevices} />

      {encodedImage && (
        <View style={styles.resultContainer}>
          <Text style={styles.subtitle}>Imagem Capturada (Base64):</Text>
          <Image
            source={{uri: `data:image/png;base64,${encodedImage}`}}
            style={styles.image}
            resizeMode="contain"
          />
        </View>
      )}

      {wsqBase64 && (
        <View style={styles.resultContainer}>
          <Text style={styles.subtitle}>Codificação WSQ (Base64):</Text>
          <Text style={styles.base64Text}>{wsqBase64}</Text>
        </View>
      )}

      {usbDevices && (
        <View style={styles.resultContainer}>
          <Text style={styles.subtitle}>Dispositivos USB Conectados:</Text>
          <Text style={styles.base64Text}>{usbDevices}</Text>
        </View>
      )}
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    alignItems: 'center',
    padding: 20,
    backgroundColor: '#f4f4f4',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  resultContainer: {
    marginTop: 20,
    width: '100%',
    alignItems: 'center',
  },
  subtitle: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 10,
  },
  image: {
    width: 200,
    height: 200,
    borderWidth: 1,
    borderColor: '#ccc',
  },
  base64Text: {
    fontSize: 12,
    color: '#555',
    marginTop: 10,
    textAlign: 'center',
    paddingHorizontal: 10,
  },
});

export default App;
