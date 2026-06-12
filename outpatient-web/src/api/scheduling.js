import client from './client'

export const createSchedule = (data) =>
  client.post('/api/scheduling/schedules', data)

export const getSchedules = (doctorId, date) =>
  client.get(`/api/scheduling/schedules/${doctorId}`, { params: { date } })

export const addToQueue = (data) =>
  client.post('/api/scheduling/queue', data)

export const checkIn = (id) =>
  client.post(`/api/scheduling/queue/${id}/check-in`)

export const callNext = (doctorId) =>
  client.post('/api/scheduling/queue/call-next', null, { params: { doctorId } })

export const completeQueue = (id) =>
  client.post(`/api/scheduling/queue/${id}/complete`)

export const passPatient = (id) =>
  client.post(`/api/scheduling/queue/${id}/pass`)

export const cancelQueue = (id) =>
  client.post(`/api/scheduling/queue/${id}/cancel`)

export const getDoctorQueue = (doctorId) =>
  client.get(`/api/scheduling/queue/${doctorId}`)
