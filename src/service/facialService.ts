import {createInstance} from './api';

export const validarAutenticacao = async () => {
  const Instance = await createInstance();
  try {
    const {data} = await Instance.post(`app-credentials/validate`, {
      key: 'CXMSZE5SKV-MFQFPR9LWL-2BZQXNADG3',
      secret: 'WDYMCM6-CARXG4X-G769ZNJ',
    });

    console.log('data', data);

    return data;
  } catch (error) {
    console.error('Erro na autenticação:', error);
    throw error;
  }
};

export const gerarToken = async () => {
  const Instance = await createInstance();
  try {
    const {data} = await Instance.post(`app-credentials/token`, {
      key: 'CXMSZE5SKV-MFQFPR9LWL-2BZQXNADG3',
      secret: 'WDYMCM6-CARXG4X-G769ZNJ',
    });
    console.log('data', data);
    return data;
  } catch (error) {
    console.error('Erro na autenticação:', error);
    throw error;
  }
};

export const numeroDeFaces = async (token: string) => {
  const Instance = await createInstance();
  try {
    const {data} = await Instance.get(`services/face/count`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });
    console.log('data', data);
    return data;
  } catch (error) {
    console.error('Erro na autenticação:', error);
    throw error;
  }
};

export const listagemDeFaces = async (token: string) => {
  const Instance = await createInstance();
  try {
    const {data} = await Instance.get(`services/face/list`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });
    console.log('data', data);
    return data;
  } catch (error) {
    console.error('Erro na autenticação:', error);
    throw error;
  }
};

export const cadastrarFace = async (token: string, image: string) => {
  const Instance = await createInstance();
  try {
    const {data} = await Instance.post(
      `services/face/enroll/{ext_id}`,
      {
        Images: [
          {
            imageType: 'frontal',
            imageData: image,
          },
        ],
      },
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      },
    );
    console.log('data', data);
    return data;
  } catch (error) {
    console.error('Erro na autenticação:', error);
    throw error;
  }
};

export const verificarFace = async (token: string, image: string) => {
  const Instance = await createInstance();
  try {
    const {data} = await Instance.post(
      `services/face/{ext_id}/verify`,
      {
        Images: [
          {
            imageType: 'frontal',
            imageData: image,
          },
        ],
      },
      {
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      },
    );
    console.log('data', data);
    return data;
  } catch (error) {
    console.error('Erro na autenticação:', error);
    throw error;
  }
};
