<template>
  <section class="panel">
    <div class="panel-header">
      <h2>候诊队列</h2>
      <span class="badge">{{ patients.length }} 人</span>
    </div>

    <div class="queue-list">
      <div
        v-for="p in patients" :key="p.id"
        class="queue-item"
        :class="{ active: p.id === activeId }"
      >
        <div class="item-top">
          <span class="patient-name">
            <span class="dot" :class="'dot-' + statusColor(p.status)"></span>
            {{ p.patientName }}
          </span>
          <span class="queue-number">#{{ p.queueNumber }}</span>
        </div>
        <div class="item-meta">
          {{ p.source === 'ONLINE' ? '线上' : '线下' }} ·
          {{ {WAITING:'候诊中',IN_QUEUE:'候诊中',IN_CONSULTATION:'就诊中',COMPLETED:'已完成',PASSED:'已过号',CANCELLED:'已取消'}[p.status] || p.status }}
        </div>
      </div>
      <p v-if="!patients.length && !loading" class="empty">暂无候诊患者</p>
    </div>

    <div class="panel-footer">
      <button
        class="call-btn"
        :class="{ disabled: consulting }"
        :disabled="consulting"
        @click="$emit('call-next')"
      >
        {{ consulting ? '⏳ 就诊中...' : '📢 叫号' }}
      </button>
      <p class="call-hint" v-if="consulting">请先结束当前患者</p>
    </div>
  </section>
</template>

<script setup>
defineProps({ patients: Array, activeId: String, consulting: Boolean, loading: Boolean })
defineEmits(['call-next'])

function statusColor(s) {
  return { WAITING:'waiting', IN_QUEUE:'waiting', IN_CONSULTATION:'active', COMPLETED:'done', PASSED:'warn', CANCELLED:'error' }[s] || 'waiting'
}
</script>

<style scoped>
.panel {
  width: 320px; flex-shrink: 0; background: var(--bg-card);
  border-right: 1px solid var(--border); display: flex; flex-direction: column;
  box-shadow: var(--shadow-sm);
}
.panel-header {
  padding: 18px 20px; border-bottom: 1px solid var(--border);
  display: flex; justify-content: space-between; align-items: center;
}
.panel-header h2 { font-size: 15px; font-weight: 600; }
.badge {
  background: var(--accent-light); color: var(--accent-dark);
  font-size: 12px; padding: 3px 10px; border-radius: 12px; font-weight: 600;
}
.queue-list { flex: 1; overflow-y: auto; padding: 8px 12px; }
.queue-item {
  padding: 14px; border-radius: var(--radius-md); margin-bottom: 6px;
  border: 1.5px solid transparent; transition: var(--transition);
}
.queue-item.active { background: var(--accent-light); border-color: var(--accent); }
.item-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px; }
.patient-name { font-size: 14px; font-weight: 600; display: flex; align-items: center; gap: 6px; }
.queue-number { font-size: 11px; color: var(--text-muted); background: var(--bg-panel); padding: 2px 8px; border-radius: 8px; }
.item-meta { font-size: 12px; color: var(--text-secondary); }
.dot { width: 7px; height: 7px; border-radius: 50%; display: inline-block; }
.dot-waiting { background: var(--text-muted); }
.dot-active { background: var(--accent); animation: pulse 2s infinite; }
.dot-warn { background: var(--warning); }
.dot-done { background: var(--success); }
.dot-error { background: var(--danger); }
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }
.empty { padding: 40px; text-align: center; color: var(--text-muted); font-size: 13px; }
.panel-footer { padding: 14px 16px; border-top: 1px solid var(--border); }
.call-btn {
  width: 100%; padding: 12px; border: none; border-radius: var(--radius-pill);
  background: var(--accent); color: #fff; font-size: 14px; font-weight: 600;
  cursor: pointer; transition: var(--transition); box-shadow: 0 2px 8px rgba(26,127,140,0.3);
}
.call-btn:hover:not(:disabled) { background: var(--accent-dark); transform: translateY(-1px); }
.call-btn.disabled { background: var(--text-muted); box-shadow: none; cursor: not-allowed; }
.call-btn:disabled { cursor: not-allowed; }
.call-hint { font-size: 11px; color: var(--warning); text-align: center; margin-top: 6px; }
</style>
