<template>
  <div class="login-page">
    <div class="login-card">
      <div class="logo-area">
        <span class="logo-icon">云</span>
        <h1>智慧云脑诊疗平台</h1>
        <p>医生门诊工作台</p>
      </div>
      <form @submit.prevent="handleLogin">
        <div class="field">
          <label>用户名</label>
          <input v-model="username" type="text" placeholder="请输入用户名" autofocus />
        </div>
        <div class="field">
          <label>密码</label>
          <input v-model="password" type="password" placeholder="请输入密码" />
        </div>
        <button type="submit" :disabled="loading">
          {{ loading ? '登录中...' : '登 录' }}
        </button>
      </form>
      <p class="error" v-if="error">{{ error }}</p>
      <p class="hint">测试账号 doctor1 / test123</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const auth = useAuthStore()
const username = ref('doctor1')
const password = ref('test123')
const loading = ref(false)
const error = ref('')

async function handleLogin() {
  loading.value = true; error.value = ''
  try {
    await auth.login(username.value, password.value)
    router.push('/workstation')
  } catch {
    error.value = '登录失败，请检查用户名和密码'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  width: 100vw; height: 100vh; display: flex;
  align-items: center; justify-content: center;
  background: linear-gradient(135deg, var(--bg-deep) 0%, var(--bg-sidebar) 50%, #1a3a4a 100%);
}
.login-card {
  background: var(--bg-card); border-radius: var(--radius-lg);
  padding: 48px 40px; width: 400px; max-width: 90vw;
  box-shadow: 0 16px 48px rgba(0,0,0,0.3); text-align: center;
}
.logo-area { margin-bottom: 36px; }
.logo-icon {
  display: inline-flex; width: 56px; height: 56px; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #1a9fad, #0f7a88);
  border-radius: 16px; color: #fff; font-size: 28px; font-weight: 700;
  margin-bottom: 16px;
}
.logo-area h1 { font-size: 20px; font-weight: 700; color: var(--text-primary); }
.logo-area p { font-size: 13px; color: var(--text-muted); margin-top: 6px; }
.field { text-align: left; margin-bottom: 16px; }
.field label { display: block; font-size: 12px; font-weight: 600; color: var(--text-secondary); margin-bottom: 6px; }
.field input {
  width: 100%; padding: 11px 14px; border: 1.5px solid var(--border);
  border-radius: var(--radius-sm); font-size: 14px; outline: none;
  transition: var(--transition); font-family: var(--font-body);
}
.field input:focus { border-color: var(--accent); box-shadow: 0 0 0 3px var(--accent-light); }
button[type="submit"] {
  width: 100%; padding: 13px; border: none; background: var(--accent); color: #fff;
  border-radius: var(--radius-pill); font-size: 15px; font-weight: 600;
  cursor: pointer; transition: var(--transition);
  box-shadow: 0 4px 12px rgba(26,127,140,0.3); margin-top: 8px;
}
button[type="submit"]:hover { background: var(--accent-dark); }
button[type="submit"]:disabled { opacity: 0.6; cursor: not-allowed; }
.error { color: var(--danger); font-size: 13px; margin-top: 14px; }
.hint { color: var(--text-muted); font-size: 12px; margin-top: 18px; }
</style>
