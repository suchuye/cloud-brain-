<template>
  <Teleport to="body">
    <div class="modal-mask" v-if="visible" @click.self="$emit('close')">
      <div class="modal">
        <h3>🔐 安全验证</h3>
        <p class="desc">{{ title }}</p>
        <div class="form-group">
          <label>请输入密码确认操作</label>
          <input
            ref="pwdInput"
            v-model="password"
            type="password"
            placeholder="输入登录密码"
            @keyup.enter="confirm"
          />
        </div>
        <p class="error" v-if="error">{{ error }}</p>
        <div class="modal-actions">
          <button class="btn-ghost" @click="$emit('close')">取消</button>
          <button class="btn-primary" @click="confirm" :disabled="!password">
            确认
          </button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import { verifyCredential } from '../api/auth'

const props = defineProps({ visible: Boolean, userId: String, title: String })
const emit = defineEmits(['close', 'verified'])

const password = ref('')
const error = ref('')
const pwdInput = ref(null)

watch(() => props.visible, async v => {
  if (v) { error.value = ''; password.value = ''; await nextTick(); pwdInput.value?.focus() }
})

async function confirm() {
  error.value = ''
  try {
    await verifyCredential(props.userId, password.value)
    emit('verified')
    emit('close')
  } catch {
    error.value = '密码错误，请重试'
    password.value = ''
    pwdInput.value?.focus()
  }
}
</script>

<style scoped>
.modal-mask {
  position: fixed; inset: 0; background: rgba(11,25,41,0.5);
  display: flex; align-items: center; justify-content: center; z-index: 1100;
  backdrop-filter: blur(4px);
}
.modal {
  background: var(--bg-card); border-radius: var(--radius-lg);
  padding: 28px; width: 400px; box-shadow: var(--shadow-lg);
}
.modal h3 { font-size: 16px; font-weight: 600; margin-bottom: 8px; }
.desc { font-size: 13px; color: var(--text-secondary); margin-bottom: 20px; }
.form-group { margin-bottom: 12px; }
.form-group label {
  display: block; font-size: 12px; font-weight: 600;
  color: var(--text-secondary); margin-bottom: 6px;
}
.form-group input {
  width: 100%; padding: 10px 12px; border: 1px solid var(--border);
  border-radius: var(--radius-sm); font-size: 14px; outline: none;
  transition: var(--transition); font-family: var(--font-body);
}
.form-group input:focus { border-color: var(--accent); }
.error { color: var(--danger); font-size: 12px; margin-bottom: 8px; }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; }
.btn-primary {
  padding: 10px 24px; border: none; border-radius: var(--radius-pill);
  background: var(--accent); color: #fff; font-size: 14px; font-weight: 600;
  cursor: pointer; transition: var(--transition);
}
.btn-primary:hover { background: var(--accent-dark); }
.btn-primary:disabled { opacity: 0.5; cursor: not-allowed; }
.btn-ghost {
  padding: 10px 20px; border: 1px solid var(--border); border-radius: var(--radius-pill);
  background: var(--bg-card); color: var(--text-secondary); font-size: 13px;
  cursor: pointer; transition: var(--transition);
}
.btn-ghost:hover { background: var(--bg-panel); }
</style>
