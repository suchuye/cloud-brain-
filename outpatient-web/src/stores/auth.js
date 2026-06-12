import { defineStore } from 'pinia'
import { login as apiLogin } from '../api/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    token: localStorage.getItem('token') || null
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    doctorName: (state) => state.user?.name || '',
    doctorRole: (state) => state.user?.role || ''
  },

  actions: {
    async login(username, password) {
      const { data } = await apiLogin(username, password)
      this.token = data.accessToken
      this.user = { id: data.userId, name: data.name, role: data.role }
      localStorage.setItem('token', data.accessToken)
      localStorage.setItem('user', JSON.stringify(this.user))
    },

    logout() {
      this.token = null
      this.user = null
      localStorage.clear()
    }
  }
})
