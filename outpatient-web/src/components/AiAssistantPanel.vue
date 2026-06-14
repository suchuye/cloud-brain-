<template>
  <section class="panel">
    <div class="panel-header">
      <span class="ai-dot" :class="{ inactive: !ai.connected }"></span>
      <h2>AI 助理医生</h2>
      <span>{{ ai.connected ? 'DeepSeek 实时伴诊' : '待机' }}</span>
    </div>

    <div class="ai-content">
      <!-- Real suggestions -->
      <div
        v-for="(s, i) in ai.suggestions" :key="i"
        class="ai-card"
        :class="cardStyle(s.text)"
      >
        <h4>{{ cardTitle(s.text) }}</h4>
        <p>{{ s.text }}</p>
        <div class="ai-time">{{ s.time }}</div>
      </div>

      <!-- Welcome -->
      <div v-if="!ai.suggestions.length" class="ai-placeholder">
        <div class="ai-card">
          <h4>🤖 DeepSeek 已就绪</h4>
          <p>接诊开始后，AI 将自动分析问诊内容，实时推送：</p>
          <ul style="margin-top:8px;font-size:12px;color:var(--text-secondary)">
            <li>鉴别诊断</li>
            <li>建议检查</li>
            <li>用药提醒</li>
          </ul>
        </div>
        <div class="ai-card warning">
          <h4>⚠️ 提示</h4>
          <p>所有 AI 建议仅供参考，最终诊断由医生确认。</p>
        </div>
      </div>
    </div>

    <div class="panel-footer">
      <div class="mic-indicator">
        {{ ai.connected ? '🟢 DeepSeek 在线，正在分析问诊内容' : '⚪ AI 待机中' }}
      </div>
    </div>
  </section>
</template>

<script setup>
import { useAiStore } from '../stores/ai'

const ai = useAiStore()

function cardStyle(text) {
  if (text.includes('⚠️') || text.includes('警惕')) return 'warning'
  if (text.includes('建议') || text.includes('检查') || text.includes('用药')) return 'info'
  return ''
}

function cardTitle(text) {
  if (text.includes('鉴别诊断') || text.includes('诊断')) return '🔍 鉴别诊断'
  if (text.includes('检查') || text.includes('建议检查')) return '📊 检查建议'
  if (text.includes('用药') || text.includes('药物')) return '💡 用药提醒'
  return '🤖 AI 建议'
}
</script>

<style scoped>
.panel {
  width: 300px; flex-shrink: 0; background: var(--bg-card);
  border-left: 1px solid var(--border); display: flex; flex-direction: column;
  box-shadow: var(--shadow-sm);
}
.panel-header {
  padding: 16px 20px; border-bottom: 1px solid var(--border);
  display: flex; align-items: center; gap: 10px;
}
.ai-dot {
  width: 10px; height: 10px; border-radius: 50%;
  background: var(--success); animation: pulse 2s infinite;
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
.ai-card p { font-size: 12px; line-height: 1.6; color: var(--text-secondary); white-space: pre-line; }
.ai-card.warning { border-left-color: var(--warning); }
.ai-card.warning h4 { color: var(--warning); }
.ai-card.info { border-left-color: var(--info); }
.ai-card.info h4 { color: var(--info); }
.ai-time { font-size: 10px; color: var(--text-muted); margin-top: 6px; text-align: right; }
.panel-footer { padding: 12px 16px; border-top: 1px solid var(--border); }
.mic-indicator { font-size: 12px; color: var(--text-muted); }
.ai-placeholder .ai-card ul { padding-left: 16px; }
</style>
