import { defineStore } from 'pinia'
import { ref } from 'vue'
import { Client } from '@stomp/stompjs'

export const useAiStore = defineStore('ai', () => {
  const connected = ref(false)
  const sessionId = ref(null)
  const suggestions = ref([])
  const client = new Client()

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
          suggestions.value.unshift({
            text: data.text,
            time: new Date().toLocaleTimeString()
          })
          if (suggestions.value.length > 20) suggestions.value.pop()
        })
        // 启动会话
        client.publish({
          destination: '/app/start-session',
          body: JSON.stringify({ consultationId, doctorId })
        })
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

  function sendTranscript(text) {
    if (!client.connected || !sessionId.value) return
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
    suggestions.value = []
  }

  return { connected, sessionId, suggestions, connect, sendTranscript, disconnect }
})
