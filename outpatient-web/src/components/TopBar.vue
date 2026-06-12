<template>
  <div class="top-bar">
    <div class="doctor-info">
      <div class="avatar">{{ doctor?.name?.charAt(0) }}</div>
      <div>
        <div class="name">{{ doctor?.name }} 主任医师</div>
        <div class="dept">心内科 ｜ 今日已接诊 {{ todayCount }} 人</div>
      </div>
    </div>
    <div class="spacer"></div>
    <div class="consulting-badge" v-if="consulting">
      <span class="live-dot"></span>
      当前接诊：<strong>{{ consulting.patientName }}</strong>
    </div>
  </div>
</template>

<script setup>
defineProps({ doctor: Object, todayCount: Number, consulting: Object })
</script>

<style scoped>
.top-bar {
  display: flex; align-items: center; gap: 16px;
  padding: 12px 20px; background: var(--bg-card);
  border-radius: var(--radius-lg); box-shadow: var(--shadow-sm);
}
.doctor-info { display: flex; align-items: center; gap: 10px; }
.avatar {
  width: 40px; height: 40px; border-radius: 50%; line-height: 40px; text-align: center;
  background: linear-gradient(135deg, #1a7f8c, #39a0b0); color: #fff; font-weight: 700;
}
.name { font-size: 14px; font-weight: 600; }
.dept { font-size: 12px; color: var(--text-muted); }
.spacer { flex: 1; }
.consulting-badge {
  background: linear-gradient(135deg, #e0f4f5, #d0edf0);
  border-radius: var(--radius-md); padding: 10px 16px; font-size: 13px;
  display: flex; align-items: center; gap: 8px; color: var(--accent-dark);
}
.live-dot {
  width: 8px; height: 8px; border-radius: 50%;
  background: var(--accent); animation: pulse 1.5s infinite;
}
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }
</style>
