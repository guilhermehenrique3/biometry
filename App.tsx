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
      console.error(error.message);
    }
  };

  const startScan = async () => {
    try {
      const result = await UsbModule.startFingerprintScan();
      setScanResult(result);
    } catch (error) {
      console.error(error.message);
    }
  };

  return (
    <View>
      <Button title="List USB Devices" onPress={fetchUsbDevices} />
      <Text>{usbDevices}</Text>
      <Button title="Start Scan" onPress={startScan} />
      <Text>{scanResult}</Text>
    </View>
  );
};

export default App;
