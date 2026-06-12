<template>
  <div class="queue-panel">
    <div class="panel-hd">
      <h2>候诊队列</h2>
      <span class="badge">{{ queue.length }} 人</span>
    </div>
    <div class="queue-list">
      <div
        v-for="p in queue" :key="p.id"
        class="q-item"
        :class="{ active: p.id === activeId }"
        @click="$emit('select', p)"
      >
        <div class="q-top">
          <span class="q-name">
            <span class="dot" :class="'dot-' + statusClass(p.status)"></span>
            {{ p.patientName }}
          </span>
          <span class="q-no">#{{ p.queueNumber }}</span>
        </div>
        <div class="q-meta">
          {{ p.source === 'ONLINE' ? '线上' : '线下' }} ·
          {{ statusText(p.status) }}
        </div>
      </div>
      <p v-if="!queue.length" class="empty">暂无候诊患者</p>
    </div>
    <div class="panel-ft">
      <button class="call-btn" @click="$emit('call-next')">📢 叫号</button>
    </div>
  </div>
</template>

<script setup>
defineProps({ queue: Array, activeId: String })
defineEmits(['select', 'call-next'])

function statusClass(s) {
  return { WAITING:'waiting', IN_QUEUE:'waiting', IN_CONSULTATION:'consulting', COMPLETED:'done', PASSED:'passed', CANCELLED:'cancelled' }[s] || 'waiting'
}
function statusText(s) {
  return { WAITING:'候诊中', IN_QUEUE:'候诊中', IN_CONSULTATION:'就诊中', COMPLETED:'已完成', PASSED:'已过号', CANCELLED:'已取消' }[s] || s
}
</script>

<style scoped>
.queue-panel {
  width: 320px; background: var(--bg-card); display: flex; flex-direction: column;
  border-right: 1px solid var(--border); box-shadow: var(--shadow-sm);
}
.panel-hd {
  padding: 18px 20px; border-bottom: 1px solid var(--border);
  display: flex; align-items: center; justify-content: space-between;
}
.panel-hd h2 { font-size: 15px; font-weight: 600; }
.badge {
  background: var(--accent-light); color: var(--accent-dark);
  font-size: 12px; padding: 3px 10px; border-radius: 12px; font-weight: 600;
}
.queue-list { flex: 1; overflow-y: auto; padding: 8px 12px; }
.q-item {
  padding: 14px; border-radius: var(--radius-md); margin-bottom: 6px;
  cursor: pointer; border: 1.5px solid transparent; transition: 0.2s;
}
.q-item:hover { background: var(--bg-panel); }
.q-item.active { background: var(--accent-light); border-color: var(--accent); }
.q-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px; }
.q-name { font-size: 14px; font-weight: 600; display: flex; align-items: center; gap: 6px; }
.q-no { font-size: 11px; color: var(--text-muted); background: var(--bg-panel); padding: 2px 8px; border-radius: 8px; }
.q-meta { font-size: 12px; color: var(--text-secondary); }
.dot { width: 7px; height: 7px; border-radius: 50%; display: inline-block; }
.dot-waiting { background: var(--text-muted); }
.dot-consulting { background: var(--accent); animation: pulse 2s infinite; }
.dot-passed { background: var(--warning); }
.dot-done { background: var(--success); }
.dot-cancelled { background: var(--danger); }
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }
.empty { padding: 40px 20px; text-align: center; color: var(--text-muted); font-size: 13px; }
.panel-ft { padding: 14px 16px; border-top: 1px solid var(--border); }
.call-btn {
  width: 100%; padding: 12px; border: none; border-radius: 22px;
  background: var(--accent); color: #fff; font-size: 14px; font-weight: 600;
  cursor: pointer; box-shadow: 0 2px 8px rgba(26,127,140,0.3); transition: 0.2s;
}
.call-btn:hover { background: var(--accent-dark); }
</style>
