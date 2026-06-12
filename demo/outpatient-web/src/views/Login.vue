<template>
  <div class="login-page">
    <div class="login-card">
      <div class="logo">
        <span class="logo-icon">云</span>
        <h1>智慧云脑诊疗平台</h1>
        <p>医生门诊工作台</p>
      </div>
      <form @submit.prevent="doLogin">
        <input v-model="username" placeholder="用户名" />
        <input v-model="password" type="password" placeholder="密码" />
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
import { login } from '../api'

const router = useRouter()
const username = ref('doctor1')
const password = ref('test123')
const loading = ref(false)
const error = ref('')

async function doLogin() {
  loading.value = true; error.value = ''
  try {
    const { data } = await login(username.value, password.value)
    localStorage.setItem('token', data.accessToken)
    localStorage.setItem('user', JSON.stringify(data))
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
  width: 100vw; height: 100vh;
  display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #0b1929 0%, #0f2440 50%, #1a3a4a 100%);
}
.login-card {
  background: #fff; border-radius: var(--radius-lg); padding: 48px 40px;
  width: 400px; box-shadow: 0 16px 48px rgba(0,0,0,0.3); text-align: center;
}
.logo { margin-bottom: 32px; }
.logo-icon {
  display: inline-block; width: 56px; height: 56px; line-height: 56px;
  background: linear-gradient(135deg, #1a9fad, #0f7a88);
  border-radius: 16px; color: #fff; font-size: 28px; font-weight: 700;
  margin-bottom: 16px;
}
.logo h1 { font-size: 20px; font-weight: 700; color: var(--text-primary); }
.logo p { font-size: 13px; color: var(--text-muted); margin-top: 4px; }
input {
  display: block; width: 100%; margin-bottom: 14px;
  padding: 12px 16px; border: 1.5px solid var(--border);
  border-radius: var(--radius-sm); font-size: 14px; outline: none;
  transition: border-color 0.2s;
}
input:focus { border-color: var(--accent); }
button {
  width: 100%; padding: 13px; border: none;
  background: var(--accent); color: #fff;
  border-radius: 24px; font-size: 15px; font-weight: 600;
  cursor: pointer; transition: background 0.2s;
  box-shadow: 0 4px 12px rgba(26,127,140,0.3);
}
button:hover { background: var(--accent-dark); }
button:disabled { opacity: 0.6; }
.error { color: var(--danger); font-size: 13px; margin-top: 12px; }
.hint { color: var(--text-muted); font-size: 12px; margin-top: 16px; }
</style>
