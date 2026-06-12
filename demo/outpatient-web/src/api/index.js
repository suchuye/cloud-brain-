import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: { 'Content-Type': 'application/json' }
})

// Auth
export const login = (username, password) =>
  api.post('/auth/login', { username, password })

// Outpatient
export const startConsultation = (consultationId, doctorId) =>
  api.post('/api/consultations/start', { consultationId, doctorId })

export const finishConsultation = (id) =>
  api.post(`/api/consultations/${id}/finish`)

export const saveEmr = (consultationId, content) =>
  api.post('/api/emr', { consultationId, content })

export const searchEmr = (keyword) =>
  api.get('/api/emr/search', { params: { keyword } })

// OrderRouting
export const submitOrder = (idempotencyKey, consultationId, orderType, orderDetails) =>
  api.post('/api/orders', { idempotencyKey, consultationId, orderType, orderDetails })

// Imaging
export const submitImage = (data) =>
  api.post('/api/imaging/submit', data)

export const getImageReport = (taskId) =>
  api.get(`/api/imaging/reports/${taskId}`)

// Scheduling
export const createSchedule = (data) =>
  api.post('/api/scheduling/schedules', data)

export const getQueue = (doctorId) =>
  api.get(`/api/scheduling/queue/${doctorId}`)

export const addToQueue = (data) =>
  api.post('/api/scheduling/queue', data)

export const callNext = (doctorId) =>
  api.post('/api/scheduling/queue/call-next', null, { params: { doctorId } })

export const checkIn = (id) =>
  api.post(`/api/scheduling/queue/${id}/check-in`)

export const completeQueue = (id) =>
  api.post(`/api/scheduling/queue/${id}/complete`)

export default api
