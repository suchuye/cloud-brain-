<template>
  <div class="card">
    <h3>📝 电子病历</h3>
    <div class="input-row">
      <input v-model="chiefComplaint" placeholder="主诉" />
      <input v-model="diagnosis" placeholder="初步诊断" />
    </div>
    <textarea
      v-model="content"
      placeholder="现病史、体格检查、辅助检查..."
      rows="5"
    ></textarea>
    <div class="actions">
      <button class="btn-ghost">暂存草稿</button>
      <button class="btn-primary" @click="save" :disabled="!patientId">
        ✅ 保存病历
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { saveEmr } from '../api/outpatient'

const props = defineProps({ patientId: String })
const emit = defineEmits(['saved'])
const chiefComplaint = ref('')
const diagnosis = ref('')
const content = ref('')

async function save() {
  if (!props.patientId) return
  const full = JSON.stringify({
    chiefComplaint: chiefComplaint.value,
    diagnosis: diagnosis.value,
    presentIllness: content.value
  })
  await saveEmr(props.patientId, full)
  emit('saved')
}
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
.input-row { display: flex; gap: 8px; margin-bottom: 10px; }
.input-row input {
  flex: 1; padding: 9px 12px; border: 1px solid var(--border);
  border-radius: var(--radius-sm); font-size: 13px; outline: none;
  transition: var(--transition); font-family: var(--font-body);
}
.input-row input:focus { border-color: var(--accent); }
textarea {
  width: 100%; border: 1px solid var(--border); border-radius: var(--radius-sm);
  padding: 12px; font-size: 13px; line-height: 1.7; resize: vertical;
  outline: none; transition: var(--transition); font-family: var(--font-body);
}
textarea:focus { border-color: var(--accent); box-shadow: 0 0 0 3px var(--accent-light); }
.actions { display: flex; gap: 8px; justify-content: flex-end; margin-top: 12px; }
.btn-primary {
  padding: 10px 24px; border: none; border-radius: var(--radius-pill);
  background: var(--accent); color: #fff; font-size: 14px; font-weight: 600;
  cursor: pointer; transition: var(--transition);
}
.btn-primary:hover { background: var(--accent-dark); }
.btn-primary:disabled { opacity: 0.5; cursor: not-allowed; }
.btn-ghost {
  padding: 10px 20px; border: 1px solid var(--border); border-radius: var(--radius-pill);
  background: var(--bg-card); color: var(--text-secondary); font-size: 13px;
  cursor: pointer; transition: var(--transition);
}
.btn-ghost:hover { background: var(--bg-panel); }
</style>
