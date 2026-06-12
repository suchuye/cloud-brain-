import client from './client'

export const submitImage = (data) =>
  client.post('/api/imaging/submit', data)

export const getTask = (id) =>
  client.get(`/api/imaging/tasks/${id}`)

export const getReport = (taskId) =>
  client.get(`/api/imaging/reports/${taskId}`)
