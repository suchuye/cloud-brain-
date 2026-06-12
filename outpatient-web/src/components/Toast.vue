<template>
  <div class="toast" :class="{ show: visible }">{{ message }}</div>
</template>

<script setup>
import { ref } from 'vue'

const message = ref('')
const visible = ref(false)
let timer

function show(msg) {
  message.value = msg
  visible.value = true
  clearTimeout(timer)
  timer = setTimeout(() => visible.value = false, 3000)
}

defineExpose({ show })
</script>

<style scoped>
.toast {
  position: fixed; top: 24px; right: 24px; padding: 12px 20px;
  background: var(--success); color: #fff; border-radius: var(--radius-md);
  font-size: 13px; font-weight: 600; z-index: 2000; pointer-events: none;
  opacity: 0; transform: translateY(-10px); transition: 0.3s;
}
.toast.show { opacity: 1; transform: translateY(0); }
</style>
