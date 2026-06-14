import { defineStore } from 'pinia'
import { ref } from 'vue'
import { Client } from '@stomp/stompjs'

export const useAiStore = defineStore('ai', () => {
  const connected = ref(false)
  const sessionId = ref(null)
  const messages = ref([])
  const loading = ref(false)
  const client = new Client()

  function addMessage(role, text) {
    messages.value.push({
      role, text,
      time: new Date().toLocaleTimeString(),
      id: Date.now()
    })
  }

  function connect(consultationId, doctorId) {
    if (client.connected) return

    client.configure({
      brokerURL: 'ws://localhost:8084/ws/ai-assistant',
      onConnect: () => {
        connected.value = true
        client.subscribe('/topic/session', msg => {
          const data = JSON.parse(msg.body)
          sessionId.value = data.id
        })
        client.subscribe(`/topic/suggestion/${sessionId.value}`, msg => {
          const data = JSON.parse(msg.body)
          loading.value = false
          addMessage('assistant', data.text)
        })
        client.publish({
          destination: '/app/start-session',
          body: JSON.stringify({ consultationId, doctorId })
        })
        addMessage('system', '诊断助理已就绪，可输入问诊内容获取建议')
      },
      onDisconnect: () => {
        connected.value = false
        sessionId.value = null
      },
      onStompError: frame => {
        console.error('STOMP error:', frame.headers['message'])
      }
    })
    client.activate()
  }

  function ask(text) {
    if (!client.connected || !sessionId.value) return
    addMessage('doctor', text)
    loading.value = true
    client.publish({
      destination: '/app/transcript',
      body: JSON.stringify({ sessionId: sessionId.value, text })
    })
  }

  function disconnect() {
    if (client.connected && sessionId.value) {
      client.publish({
        destination: '/app/end-session',
        body: JSON.stringify({ sessionId: sessionId.value })
      })
    }
    client.deactivate()
    connected.value = false
    sessionId.value = null
    messages.value = []
    loading.value = false
  }

  return { connected, sessionId, messages, loading, connect, ask, disconnect }
})
