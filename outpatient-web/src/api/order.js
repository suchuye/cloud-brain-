import client from './client'

export const submitOrder = (idempotencyKey, consultationId, orderType, orderDetails) =>
  client.post('/api/orders', { idempotencyKey, consultationId, orderType, orderDetails })
