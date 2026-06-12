<template>
  <section class="panel">
    <div class="panel-header">
      <span class="ai-dot" :class="{ inactive: !consulting }"></span>
      <h2>AI 助理医生</h2>
      <span>{{ consulting ? '实时伴诊中' : '待机' }}</span>
    </div>

    <div class="ai-content">
      <div class="ai-card warning">
        <h4>⚠️ 鉴别诊断提示</h4>
        <p>AI 正在分析问诊内容，将实时推送鉴别诊断和用药合规建议。</p>
      </div>
      <div class="ai-card">
        <h4>🔍 诊断建议</h4>
        <p>接诊开始后，AI 将自动提取病历关键信息并推送可能的诊断方向。</p>
      </div>
      <div class="ai-card info">
        <h4>💡 用药合规提醒</h4>
        <p>开具处方时将自动校验药物相互作用及禁忌症。</p>
      </div>
      <div class="ai-card">
        <h4>📊 辅助检查推荐</h4>
        <p>根据主诉和体征，AI 将推荐必要的辅助检查项目。</p>
      </div>
    </div>

    <div class="panel-footer">
      <div class="mic-indicator">
        🎤 AI 正在监听问诊对话，实时推送诊疗建议
      </div>
    </div>
  </section>
</template>

<script setup>
defineProps({ consulting: Boolean })
</script>

<style scoped>
.panel {
  width: 360px; flex-shrink: 0; background: var(--bg-card);
  border-left: 1px solid var(--border); display: flex; flex-direction: column;
  box-shadow: var(--shadow-sm);
}
.panel-header {
  padding: 16px 20px; border-bottom: 1px solid var(--border);
  display: flex; align-items: center; gap: 10px;
}
.ai-dot {
  width: 10px; height: 10px; border-radius: 50%;
  background: var(--accent); animation: pulse 2s infinite;
}
.ai-dot.inactive { background: var(--text-muted); animation: none; }
.panel-header h2 { font-size: 14px; font-weight: 600; }
.panel-header span { font-size: 11px; color: var(--text-muted); }
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }
.ai-content { flex: 1; overflow-y: auto; padding: 16px; }
.ai-card {
  background: var(--bg-panel); border-radius: var(--radius-md);
  padding: 14px; margin-bottom: 10px; border-left: 3px solid var(--accent);
}
.ai-card h4 { font-size: 12px; font-weight: 600; margin-bottom: 6px; color: var(--accent-dark); }
.ai-card p { font-size: 13px; line-height: 1.6; color: var(--text-secondary); }
.ai-card.warning { border-left-color: var(--warning); }
.ai-card.warning h4 { color: var(--warning); }
.ai-card.info { border-left-color: var(--info); }
.ai-card.info h4 { color: var(--info); }
.panel-footer { padding: 12px 16px; border-top: 1px solid var(--border); }
.mic-indicator { font-size: 12px; color: var(--text-muted); }
</style>
