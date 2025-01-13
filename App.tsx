import React, { useState } from 'react';
import { Button, View, Text } from 'react-native';
import { NativeModules } from 'react-native';

const { UsbModule } = NativeModules;

const App = () => {
  const [usbDevices, setUsbDevices] = useState('');
  const [scanResult, setScanResult] = useState('');

  const fetchUsbDevices = async () => {
    try {
      const devices = await UsbModule.getConnectedUsbDevices();
      setUsbDevices(devices);
    } catch (error) {
      if (error instanceof Error) {
        console.error(error.message);
      } else {
        console.error(error);
      }
    }
  };

  const startScan = async () => {
    try {
      const result = await UsbModule.startFingerprintScan();
      setScanResult(result);
    } catch (error) {
      console.error(error?.message);
    }
  };

  return (
    <View>
      <Button title="USB Devices" onPress={fetchUsbDevices} />
      <Text>{usbDevices}</Text>
      <Button title="Iniciar Scan" onPress={startScan} />
      <Text>{scanResult}</Text>
    </View>
  );
};

export default App;
