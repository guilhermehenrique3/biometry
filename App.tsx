import React, {useEffect, useState} from 'react';
import {View, Text, TouchableOpacity, StyleSheet} from 'react-native';
import {NativeModules} from 'react-native';

import AppFacial from './AppFacial';
import AppFingerprint from './AppFingerprint';

const App = () => {
  return <AppFacial />;
};

export default App;
