import AsyncStorage from '@react-native-async-storage/async-storage';
import axios, {AxiosResponse, AxiosRequestConfig, AxiosInstance} from 'axios';

export const BASE_URL = 'https://api.techtrue.com.br/api/v1/';

const axiosInstance: AxiosInstance = axios.create({
  baseURL: 'BASE_URL',
});

const DEFAULT_HEADERS = {
  'Content-Type': 'application/json',
};

export const createInstance = async () => {
  const newInstance = axios.create({
    baseURL: BASE_URL,
    headers: {
      'Cache-Control': 'no-cache',
      ...DEFAULT_HEADERS,
      Authorization: `Bearer token`,
    },
  });

  newInstance.interceptors.request.use(
    function (config: any) {
      // Do something before request is sent
      return config;
    },
    function (error: any) {
      // Do something with request error
      return Promise.reject(error);
    },
  );

  return newInstance;
};
