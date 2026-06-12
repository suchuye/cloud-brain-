<template>
  <div class="ai-panel">
    <div class="ai-hd">
      <span class="ai-dot"></span>
      <h2>AI 助理医生</h2>
      <span>实时伴诊中</span>
    </div>
    <div class="ai-body">
      <div class="ai-card warning">
        <h4>⚠️ 鉴别诊断提示</h4>
        <p>根据主诉和病史，高度警惕相关疾病的鉴别诊断，建议完善相关辅助检查。</p>
      </div>
      <div class="ai-card">
        <h4>🔍 诊断建议</h4>
        <p>AI 正在分析病历内容，实时推送可能的诊断方向。</p>
      </div>
      <div class="ai-card info">
        <h4>💡 用药合规提醒</h4>
        <p>开具处方时将自动校验药物相互作用及禁忌症。</p>
      </div>
    </div>
    <div class="ai-ft">
      <div class="ai-input">🎤 AI 正在监听问诊对话，实时推送诊疗建议</div>
    </div>
  </div>
</template>

<style scoped>
.ai-panel {
  width: 360px; background: var(--bg-card); display: flex; flex-direction: column;
  border-left: 1px solid var(--border); box-shadow: var(--shadow-sm);
}
.ai-hd {
  padding: 16px 20px; border-bottom: 1px solid var(--border);
  display: flex; align-items: center; gap: 10px;
}
.ai-dot { width: 10px; height: 10px; border-radius: 50%; background: var(--accent); animation: pulse 2s infinite; }
.ai-hd h2 { font-size: 14px; font-weight: 600; }
.ai-hd span { font-size: 11px; color: var(--text-muted); }
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }
.ai-body { flex: 1; overflow-y: auto; padding: 16px; }
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
.ai-ft { padding: 12px 16px; border-top: 1px solid var(--border); }
.ai-input { font-size: 12px; color: var(--text-muted); display: flex; gap: 8px; align-items: center; }
</style>
