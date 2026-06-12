import client from './client'

export const startConsultation = (consultationId, doctorId) =>
  client.post('/api/consultations/start', { consultationId, doctorId })

export const finishConsultation = (id) =>
  client.post(`/api/consultations/${id}/finish`)

export const saveEmr = (consultationId, content) =>
  client.post('/api/emr', { consultationId, content })

export const updateEmr = (id, content) =>
  client.put(`/api/emr/${id}`, { content })

export const searchEmr = (keyword) =>
  client.get('/api/emr/search', { params: { keyword } })

export const getPatientEmrs = (patientId) =>
  client.get(`/api/emr/patient/${patientId}`)
