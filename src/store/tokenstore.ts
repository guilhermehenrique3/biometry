import { create } from 'zustand';
import { persist } from 'zustand/middleware';

const useTokenStore = create(
  persist(
    (set) => ({
      token: '',
      setToken: (newToken:string) => set({ token: newToken }),
      clearToken: () => set({ token: '' })
    }),
    {
      name: 'token-storage', 
    }
  )
);

export default useTokenStore;
