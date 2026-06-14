import { defineStore } from 'pinia'
import { ref } from 'vue'
import { Client } from '@stomp/stompjs'

export const useAiStore = defineStore('ai', () => {
  const connected = ref(false)
  const sessionId = ref(null)
  const messages = ref([])
  const loading = ref(false)
  const client = new Client()
  let suggestionSub = null

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
      reconnectDelay: 3000,
      onConnect: () => {
        connected.value = true
        // Start session first, response comes to /topic/session
        client.subscribe('/topic/session', msg => {
          const data = JSON.parse(msg.body)
          sessionId.value = data.id
          // Now subscribe to this session's suggestion channel
          if (suggestionSub) suggestionSub.unsubscribe()
          suggestionSub = client.subscribe(`/topic/suggestion/${data.id}`, msg => {
            loading.value = false
            addMessage('assistant', JSON.parse(msg.body).text)
          })
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
        if (suggestionSub) { suggestionSub.unsubscribe(); suggestionSub = null }
      },
      onStompError: frame => {
        console.error('STOMP error:', frame.headers['message'])
      }
    })
    client.activate()
  }

  function ask(text) {
    if (!client.connected || !sessionId.value) {
      addMessage('system', '诊断助理未连接，请先开始接诊')
      return
    }
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
    if (suggestionSub) { suggestionSub.unsubscribe(); suggestionSub = null }
    client.deactivate()
    connected.value = false
    sessionId.value = null
    messages.value = []
    loading.value = false
  }

  return { connected, sessionId, messages, loading, connect, ask, disconnect }
})
