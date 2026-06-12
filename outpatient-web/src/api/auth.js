import client from './client'

export const login = (username, password) =>
  client.post('/auth/login', { username, password })

export const refreshToken = (token) =>
  client.post('/auth/refresh', null, { headers: { 'X-Refresh-Token': token } })

export const verifyCredential = (userId, password) =>
  client.post('/auth/verify-credential', { userId, password })
