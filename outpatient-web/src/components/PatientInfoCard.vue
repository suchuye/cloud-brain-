<template>
  <div class="card" v-if="patient">
    <h3>📌 患者信息</h3>
    <div class="info-grid">
      <div><span class="lbl">姓名</span><span class="val">{{ patient.patientName }}</span></div>
      <div><span class="lbl">状态</span><span class="val">{{ patient.status }}</span></div>
      <div><span class="lbl">队列号</span><span class="val">#{{ patient.queueNumber }}</span></div>
      <div><span class="lbl">来源</span><span class="val">{{ patient.source === 'ONLINE' ? '线上预约' : '线下挂号' }}</span></div>
    </div>
  </div>
  <div class="card empty-card" v-else>
    <p>请从候诊队列中选择患者开始接诊</p>
  </div>
</template>

<script setup>
defineProps({ patient: Object })
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
.info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
.lbl { font-size: 11px; color: var(--text-muted); display: block; margin-bottom: 2px; }
.val { font-size: 13px; color: var(--text-primary); font-weight: 500; }
.empty-card { text-align: center; padding: 40px; }
.empty-card p { color: var(--text-muted); font-size: 13px; }
</style>
