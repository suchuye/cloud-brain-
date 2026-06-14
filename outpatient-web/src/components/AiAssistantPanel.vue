<template>
  <section class="panel">
    <div class="panel-header">
      <span class="dot" :class="{ active: ai.connected }"></span>
      <h2>诊断助理</h2>
      <span>{{ ai.connected ? '在线' : '待机' }}</span>
    </div>

    <div class="chat-area">
      <!-- System message -->
      <div v-for="m in ai.messages" :key="m.id" class="msg-row" :class="m.role">
        <div v-if="m.role === 'system'" class="sys-msg">{{ m.text }}</div>
        <template v-else>
          <div class="avatar">{{ m.role === 'doctor' ? '👨‍⚕️' : '🩺' }}</div>
          <div class="bubble" :class="m.role">
            <div class="bubble-text">{{ m.text }}</div>
            <div class="bubble-time">{{ m.time }}</div>
          </div>
        </template>
      </div>

      <!-- Loading -->
      <div v-if="ai.loading" class="msg-row assistant">
        <div class="avatar">🩺</div>
        <div class="bubble assistant">
          <span class="typing">思考中...</span>
        </div>
      </div>

      <!-- Empty -->
      <div v-if="!ai.messages.length && !ai.connected" class="empty-state">
        <div class="empty-icon">🩺</div>
        <p>接诊开始后，诊断助理将自动就绪</p>
        <p class="sub">输入问诊信息，获取鉴别诊断、检查建议和用药提醒</p>
      </div>
    </div>

    <div class="input-area">
      <input
        ref="inputRef"
        v-model="input"
        placeholder="输入问诊内容，获取诊断建议..."
        :disabled="!ai.connected"
        @keyup.enter="send"
      />
      <button :disabled="!input.trim() || !ai.connected" @click="send">发送</button>
    </div>

    <div class="disclaimer">
      ⚠️ 诊断助理建议仅供参考，最终诊断由医生确认
    </div>
  </section>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { useAiStore } from '../stores/ai'

const ai = useAiStore()
const input = ref('')
const inputRef = ref(null)

function send() {
  if (!input.value.trim()) return
  ai.ask(input.value)
  input.value = ''
  nextTick(() => inputRef.value?.focus())
}
</script>

<style scoped>
.panel {
  width: 300px; flex-shrink: 0; background: var(--bg-card);
  border-left: 1px solid var(--border); display: flex; flex-direction: column;
  box-shadow: var(--shadow-sm);
}
.panel-header {
  padding: 14px 16px; border-bottom: 1px solid var(--border);
  display: flex; align-items: center; gap: 8px;
}
.dot { width: 8px; height: 8px; border-radius: 50%; background: var(--text-muted); }
.dot.active { background: var(--success); animation: pulse 2s infinite; }
.panel-header h2 { font-size: 14px; font-weight: 600; }
.panel-header span { font-size: 11px; color: var(--text-muted); margin-left: auto; }
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }

.chat-area { flex: 1; overflow-y: auto; padding: 12px; }
.msg-row { margin-bottom: 12px; display: flex; gap: 8px; }
.msg-row.doctor { flex-direction: row-reverse; }
.msg-row.assistant { flex-direction: row; }
.sys-msg {
  text-align: center; font-size: 11px; color: var(--text-muted);
  background: var(--bg-panel); padding: 6px 12px; border-radius: 10px;
  margin: 8px auto; display: inline-block; width: 100%;
}
.avatar { width: 28px; height: 28px; border-radius: 50%; font-size: 14px; line-height: 28px; text-align: center; flex-shrink: 0; }
.bubble {
  max-width: 85%; padding: 10px 12px; border-radius: 12px; font-size: 12px; line-height: 1.5;
}
.bubble.doctor {
  background: var(--accent-light); color: var(--text-primary);
  border-bottom-right-radius: 4px;
}
.bubble.assistant {
  background: var(--bg-panel); color: var(--text-secondary);
  border-bottom-left-radius: 4px;
}
.bubble-text { white-space: pre-line; }
.bubble-time { font-size: 10px; color: var(--text-muted); margin-top: 4px; text-align: right; }
.typing { color: var(--text-muted); font-size: 12px; }

.empty-state { text-align: center; padding: 48px 20px; }
.empty-icon { font-size: 40px; margin-bottom: 12px; }
.empty-state p { font-size: 13px; color: var(--text-secondary); }
.empty-state .sub { font-size: 11px; color: var(--text-muted); margin-top: 4px; }

.input-area { padding: 8px 12px; display: flex; gap: 6px; border-top: 1px solid var(--border); }
.input-area input {
  flex: 1; padding: 9px 12px; border: 1px solid var(--border);
  border-radius: 18px; font-size: 12px; outline: none; font-family: var(--font-body);
  transition: var(--transition);
}
.input-area input:focus { border-color: var(--accent); }
.input-area button {
  padding: 8px 14px; border: none; border-radius: 18px;
  background: var(--accent); color: #fff; font-size: 12px; font-weight: 600;
  cursor: pointer; transition: var(--transition); white-space: nowrap;
}
.input-area button:hover:not(:disabled) { background: var(--accent-dark); }
.input-area button:disabled { opacity: 0.4; cursor: not-allowed; }

.disclaimer {
  padding: 6px 12px; font-size: 10px; color: var(--text-muted);
  text-align: center; border-top: 1px solid var(--border);
}
</style>
