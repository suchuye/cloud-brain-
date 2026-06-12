<template>
  <div class="order-bar card">
    <h3>📋 下达医嘱</h3>
    <div class="order-row">
      <button class="o-btn" @click="$emit('order','IMAGING')">
        <span class="o-icon">🔬</span>
        <span class="o-label">开具检查</span>
        <span class="o-desc">CT / MRI / X线</span>
      </button>
      <button class="o-btn" @click="$emit('order','LAB')">
        <span class="o-icon">🧪</span>
        <span class="o-label">开具检验</span>
        <span class="o-desc">血常规 / 生化</span>
      </button>
      <button class="o-btn" @click="$emit('order','PRESCRIPTION')">
        <span class="o-icon">💊</span>
        <span class="o-label">开具处方</span>
        <span class="o-desc">西药 / 中成药</span>
      </button>
      <button class="o-btn" @click="$emit('end')">
        <span class="o-icon">✅</span>
        <span class="o-label">结束就诊</span>
        <span class="o-desc">归档·下发处方</span>
      </button>
    </div>
  </div>
</template>

<script setup>
defineEmits(['order', 'end'])
</script>

<style scoped>
.card {
  background: var(--bg-card); border-radius: var(--radius-lg);
  padding: 20px; box-shadow: var(--shadow-sm);
}
.card h3 { font-size: 13px; font-weight: 600; color: var(--text-secondary); margin-bottom: 12px; text-transform: uppercase; }
.order-row { display: flex; gap: 12px; }
.o-btn {
  flex: 1; padding: 16px 12px; border-radius: var(--radius-md);
  border: 1.5px solid var(--border); background: var(--bg-card);
  cursor: pointer; text-align: center; transition: 0.2s; font-family: inherit;
}
.o-btn:hover { border-color: var(--accent); background: var(--accent-light); }
.o-icon { font-size: 24px; display: block; margin-bottom: 6px; }
.o-label { font-size: 13px; font-weight: 600; display: block; }
.o-desc { font-size: 11px; color: var(--text-muted); margin-top: 2px; display: block; }
</style>
