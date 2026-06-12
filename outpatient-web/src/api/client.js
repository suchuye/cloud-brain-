import axios from 'axios'

const client = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
})

client.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

client.interceptors.response.use(
  r => r,
  err => {
    const msg = err.response?.data?.message || err.message
    console.error('[API]', msg)
    return Promise.reject(err)
  }
)

export default client
