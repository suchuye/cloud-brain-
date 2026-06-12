<template>
  <aside class="sidebar">
    <div class="logo">云</div>
    <button class="nav-btn active" title="门诊工作台">🏥</button>
    <button class="nav-btn" title="电子病历">📋</button>
    <button class="nav-btn" title="检查检验">🔬</button>
    <button class="nav-btn" title="药房管理">💊</button>
    <div class="spacer"></div>
    <button class="nav-btn" title="退出" @click="$emit('logout')">⏻</button>
  </aside>
</template>

<script setup>
defineEmits(['logout'])
</script>

<style scoped>
.sidebar {
  width: 64px; background: var(--bg-sidebar); flex-shrink: 0;
  display: flex; flex-direction: column; align-items: center;
  padding: 16px 0; gap: 8px; z-index: 100;
}
.logo {
  width: 40px; height: 40px; line-height: 40px; text-align: center;
  background: linear-gradient(135deg, #1a9fad, #0f7a88);
  border-radius: 12px; color: #fff; font-size: 20px; font-weight: 700;
  margin-bottom: 16px; box-shadow: 0 4px 12px rgba(15,122,136,0.35);
}
.nav-btn {
  width: 40px; height: 40px; border-radius: 10px; border: none;
  background: transparent; color: rgba(255,255,255,0.45);
  cursor: pointer; font-size: 18px; transition: var(--transition); position: relative;
}
.nav-btn:hover, .nav-btn.active { background: rgba(255,255,255,0.08); color: #fff; }
.nav-btn.active::before {
  content: ''; position: absolute; left: 0; top: 8px; bottom: 8px;
  width: 3px; background: #1a9fad; border-radius: 0 3px 3px 0;
}
.spacer { flex: 1; }
</style>
