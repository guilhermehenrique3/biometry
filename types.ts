declare module 'react-native-fingerprint-uareu-v1' {
  interface FingerPrintUareu {
    startScan(): Promise<{encoded: string}>;
    stopScan(): Promise<void>;
    getConnectedUsbDevices(): Promise<any>;
  }

  const FingerPrintUareu: FingerPrintUareu;
  export default FingerPrintUareu;
}
