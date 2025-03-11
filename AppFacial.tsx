import React, {useState} from 'react';
import {StyleSheet, Text, TouchableOpacity, View, Image} from 'react-native';
import {
  cadastrarFace,
  gerarToken,
  listagemDeFaces,
  numeroDeFaces,
  validarAutenticacao,
  verificarFace,
} from './src/service/facialService';
import {IMAGE_EXAMPLE} from './image';

const AppFacial = () => {
  const [token, setUserToken] = useState('');
  const [photoBase64, setPhotoBase64] = useState(IMAGE_EXAMPLE);
  const [facesCount, setFacesCount] = useState(0);
  const [faceList, setFaceList] = useState(0);

  const retrieveToken = async () => {
    const response = await gerarToken();
    setUserToken(response.token);
  };

  const retrieveFacesCount = async () => {
    const response = await numeroDeFaces(token);
    setFacesCount(response.count);
  };

  const retrieveFacesList = async () => {
    const response = await listagemDeFaces(token);
    // setFaceList(response.result.total_entries);
  };

  const registerFaces = async () => {
    await cadastrarFace(token, photoBase64);
  };

  const verifyFace = async () => {
    await verificarFace(token, photoBase64);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Reconhecimento Facial</Text>
      <Text style={styles.title}>Token: {(!!token).toString()}</Text>

      <TouchableOpacity style={styles.button} onPress={validarAutenticacao}>
        <Text style={styles.buttonText}>Validar Autenticacao </Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.button} onPress={retrieveToken}>
        <Text style={styles.buttonText}>Gerar Token</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.button} onPress={retrieveFacesCount}>
        <Text style={styles.buttonText}>
          Numero de faces cadastradas: {facesCount}
        </Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.button} onPress={retrieveFacesList}>
        <Text style={styles.buttonText}>
          Listagem de faces cadastradas: {faceList}
        </Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.button} onPress={registerFaces}>
        <Text style={styles.buttonText}>Cadastrar Face</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.button} onPress={verifyFace}>
        <Text style={styles.buttonText}>Verificar Rosto</Text>
      </TouchableOpacity>

      {photoBase64 ? (
        <Image source={{uri: photoBase64}} style={styles.image} />
      ) : null}
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
  image: {
    width: 200,
    height: 200,
    marginTop: 20,
    borderRadius: 10,
  },
});

export default AppFacial;
