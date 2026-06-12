<template>
  <div class="card">
    <h3>🔍 病历搜索</h3>
    <div class="search-row">
      <input
        v-model="keyword"
        placeholder="输入关键词搜索（如：头痛、胸痛、咳嗽）"
        @keyup.enter="search"
      />
      <button class="btn-primary" @click="search">搜索</button>
    </div>
    <div v-if="results.length" class="results">
      <div v-for="r in results" :key="r.id" class="result-item">
        <div class="result-head">
          <strong>{{ r.chiefComplaint || '（无主诉）' }}</strong>
          <span class="result-id">ID: {{ r.id?.substring(0,8) }}</span>
        </div>
        <p class="result-body">{{ r.presentIllness?.substring(0,60) || '—' }}</p>
      </div>
    </div>
    <p v-if="searched && !results.length" class="no-result">未找到匹配的病历记录</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { searchEmr } from '../api/outpatient'

const keyword = ref('')
const results = ref([])
const searched = ref(false)

async function search() {
  if (!keyword.value.trim()) return
  try {
    const { data } = await searchEmr(keyword.value)
    results.value = data
    searched.value = true
  } catch { results.value = [] }
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
.search-row { display: flex; gap: 8px; }
.search-row input {
  flex: 1; padding: 10px 14px; border: 1px solid var(--border);
  border-radius: var(--radius-sm); font-size: 13px; outline: none;
  transition: var(--transition); font-family: var(--font-body);
}
.search-row input:focus { border-color: var(--accent); }
.btn-primary {
  padding: 10px 20px; border: none; border-radius: var(--radius-pill);
  background: var(--accent); color: #fff; font-size: 14px; font-weight: 600;
  cursor: pointer; transition: var(--transition);
}
.btn-primary:hover { background: var(--accent-dark); }
.results { margin-top: 12px; }
.result-item { padding: 10px 0; border-bottom: 1px solid var(--border); }
.result-head { display: flex; justify-content: space-between; font-size: 13px; margin-bottom: 4px; }
.result-id { font-size: 11px; color: var(--text-muted); font-family: var(--font-mono); }
.result-body { font-size: 12px; color: var(--text-secondary); line-height: 1.5; }
.no-result { text-align: center; color: var(--text-muted); font-size: 13px; padding: 16px; }
</style>
