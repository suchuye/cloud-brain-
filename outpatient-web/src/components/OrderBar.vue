<template>
  <div class="card">
    <h3>📋 下达医嘱</h3>
    <div class="order-row">
      <button
        v-for="btn in buttons" :key="btn.type"
        class="order-btn" :disabled="disabled"
        @click="$emit('order', btn.type)"
      >
        <span class="o-icon">{{ btn.icon }}</span>
        <span class="o-label">{{ btn.label }}</span>
        <span class="o-desc">{{ btn.desc }}</span>
      </button>
      <button class="order-btn end-btn" :disabled="disabled" @click="$emit('end')">
        <span class="o-icon">✅</span>
        <span class="o-label">结束就诊</span>
        <span class="o-desc">归档·下发处方</span>
      </button>
    </div>
  </div>
</template>

<script setup>
defineProps({ disabled: Boolean })
defineEmits(['order', 'end'])

const buttons = [
  { type: 'IMAGING', icon: '🔬', label: '开具检查', desc: 'CT / MRI / X线' },
  { type: 'LAB', icon: '🧪', label: '开具检验', desc: '血常规 / 生化' },
  { type: 'PRESCRIPTION', icon: '💊', label: '开具处方', desc: '西药 / 中成药' }
]
</script>

<style scoped>
.card {
  background: var(--bg-card); border-radius: var(--radius-lg);
  padding: 20px; box-shadow: var(--shadow-sm);
}
.card h3 {
  font-size: 13px; font-weight: 600; color: var(--text-secondary);
  margin-bottom: 12px; text-transform: uppercase; letter-spacing: 0.5px;
}
.order-row { display: flex; gap: 12px; }
.order-btn {
  flex: 1; padding: 16px 12px; border-radius: var(--radius-md);
  border: 1.5px solid var(--border); background: var(--bg-card);
  cursor: pointer; text-align: center; transition: var(--transition);
  font-family: var(--font-body);
}
.order-btn:hover:not(:disabled) { border-color: var(--accent); background: var(--accent-light); }
.order-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.o-icon { font-size: 24px; display: block; margin-bottom: 6px; }
.o-label { font-size: 13px; font-weight: 600; display: block; }
.o-desc { font-size: 11px; color: var(--text-muted); display: block; margin-top: 2px; }
.end-btn { border-color: var(--accent); }
</style>
