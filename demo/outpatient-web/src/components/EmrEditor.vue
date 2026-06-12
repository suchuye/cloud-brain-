<template>
  <div class="emr-editor">
    <div class="card-row">
      <div class="card" style="flex:1">
        <h3>📌 患者信息</h3>
        <div class="info-grid">
          <div><span class="lbl">姓名</span><span class="val">{{ patient?.patientName || '—' }}</span></div>
          <div><span class="lbl">性别/年龄</span><span class="val">—</span></div>
          <div><span class="lbl">就诊号</span><span class="val">{{ patient?.id || '—' }}</span></div>
          <div><span class="lbl">来源</span><span class="val">{{ patient?.source || '—' }}</span></div>
        </div>
      </div>
      <div class="card" style="flex:2">
        <h3>📝 电子病历</h3>
        <div class="emr-inputs">
          <input v-model="emr.chiefComplaint" placeholder="主诉" class="mini-input" />
          <input v-model="emr.diagnosis" placeholder="初步诊断" class="mini-input" />
        </div>
        <textarea
          v-model="emr.content"
          placeholder="现病史、体格检查、辅助检查..."
          class="emr-area"
        ></textarea>
        <div class="emr-actions">
          <button class="btn-ghost">暂存草稿</button>
          <button class="btn-primary" @click="save">✅ 保存病历</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const props = defineProps({ patient: Object })
const emit = defineEmits(['save'])

const emr = reactive({ chiefComplaint: '', diagnosis: '', content: '' })

function save() {
  const full = JSON.stringify({
    chiefComplaint: emr.chiefComplaint,
    diagnosis: emr.diagnosis,
    presentIllness: emr.content
  })
  emit('save', full)
}
</script>

<style scoped>
.card-row { display: flex; gap: 16px; }
.card {
  background: var(--bg-card); border-radius: var(--radius-lg);
  padding: 20px; box-shadow: var(--shadow-sm);
}
.card h3 { font-size: 13px; font-weight: 600; color: var(--text-secondary); margin-bottom: 12px; text-transform: uppercase; letter-spacing: 0.5px; }
.info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; font-size: 13px; }
.lbl { color: var(--text-muted); font-size: 11px; display: block; }
.val { color: var(--text-primary); font-weight: 500; }
.emr-inputs { display: flex; gap: 8px; margin-bottom: 10px; }
.mini-input {
  flex: 1; padding: 8px 10px; border: 1px solid var(--border);
  border-radius: var(--radius-sm); font-size: 13px; outline: none; transition: 0.2s;
}
.mini-input:focus { border-color: var(--accent); }
.emr-area {
  width: 100%; min-height: 120px; border: 1px solid var(--border);
  border-radius: var(--radius-sm); padding: 12px; font-size: 13px;
  line-height: 1.7; resize: vertical; outline: none; transition: 0.2s;
  font-family: inherit;
}
.emr-area:focus { border-color: var(--accent); box-shadow: 0 0 0 3px var(--accent-light); }
.emr-actions { display: flex; gap: 8px; justify-content: flex-end; margin-top: 12px; }
.btn-primary {
  padding: 10px 24px; border: none; border-radius: 20px;
  background: var(--accent); color: #fff; font-size: 14px; font-weight: 600;
  cursor: pointer; transition: 0.2s;
}
.btn-primary:hover { background: var(--accent-dark); }
.btn-ghost {
  padding: 10px 20px; border: 1px solid var(--border); border-radius: 20px;
  background: #fff; color: var(--text-secondary); font-size: 13px; cursor: pointer; transition: 0.2s;
}
.btn-ghost:hover { background: var(--bg-panel); }
</style>
