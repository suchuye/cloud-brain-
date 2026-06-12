<template>
  <div class="ws">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="logo">云</div>
      <button class="s-btn active" title="门诊">🏥</button>
      <button class="s-btn" title="病历">📋</button>
      <button class="s-btn" title="检查">🔬</button>
      <div class="spacer"></div>
      <button class="s-btn" title="退出" @click="logout">⏻</button>
    </aside>

    <!-- Queue -->
    <QueuePanel
      :queue="queue"
      :activeId="activePatient?.id"
      @select="selectPatient"
      @call-next="doCallNext"
    />

    <!-- Workspace -->
    <section class="center">
      <!-- Top bar -->
      <div class="top-bar">
        <div class="dr-info">
          <div class="avatar">{{ user.name?.charAt(0) }}</div>
          <div>
            <div class="dr-name">{{ user.name }} 主任医师</div>
            <div class="dr-dept">心内科 ｜ 今日已接诊 {{ todayCount }} 人</div>
          </div>
        </div>
        <div class="spacer"></div>
        <div class="consult-banner" v-if="activePatient">
          <span class="live-dot"></span>
          当前接诊：<strong>{{ activePatient.patientName }}</strong>
        </div>
      </div>

      <!-- EMR -->
      <EmrEditor :patient="activePatient" @save="doSaveEmr" />

      <!-- Order -->
      <OrderBar @order="openOrderModal" @end="doEndConsultation" />

      <!-- Search -->
      <div class="card" style="margin-top:12px">
        <h3>🔍 病历搜索</h3>
        <div style="display:flex;gap:8px">
          <input v-model="searchKeyword" placeholder="输入关键词搜索病历（如：头痛、胸痛）" style="flex:1;padding:10px 12px;border:1px solid var(--border);border-radius:var(--radius-sm);font-size:13px" @keyup.enter="doSearch" />
          <button class="btn-primary" @click="doSearch" style="padding:10px 20px">搜索</button>
        </div>
        <div v-if="searchResults.length" style="margin-top:12px">
          <div v-for="r in searchResults" :key="r.id" class="result-item">
            <strong>{{ r.chiefComplaint }}</strong>
            <span style="color:var(--text-muted);font-size:12px;margin-left:8px">{{ r.presentIllness?.substring(0,50) }}...</span>
          </div>
        </div>
      </div>
    </section>

    <!-- AI Panel -->
    <AiPanel />

    <!-- Order Modal -->
    <div class="modal-mask" v-if="orderType" @click.self="orderType=''">
      <div class="modal">
        <h3>{{ {IMAGING:'开具检查',LAB:'开具检验',PRESCRIPTION:'开具处方'}[orderType] }}</h3>
        <div class="form-g" v-if="orderType==='IMAGING'">
          <label>检查项目</label>
          <select v-model="orderDetail"><option>头部 CT 平扫</option><option>胸部 CT 平扫</option><option>冠脉 CTA</option></select>
        </div>
        <div class="form-g" v-if="orderType==='LAB'">
          <label>检验项目</label>
          <select v-model="orderDetail"><option>血常规 + CRP</option><option>高敏肌钙蛋白 I</option><option>心肌酶谱</option></select>
        </div>
        <div class="form-g" v-if="orderType==='PRESCRIPTION'">
          <label>处方药品</label>
          <input v-model="orderDetail" placeholder="药品名称、剂量、用法" />
        </div>
        <div class="modal-actions">
          <button class="btn-ghost" @click="orderType=''">取消</button>
          <button class="btn-primary" @click="doSubmitOrder">确认开具</button>
        </div>
      </div>
    </div>

    <!-- Toast -->
    <div class="toast" :class="{ show: toast.show }">{{ toast.msg }}</div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getQueue, callNext, completeQueue, saveEmr, submitOrder, searchEmr } from '../api'
import QueuePanel from '../components/QueuePanel.vue'
import EmrEditor from '../components/EmrEditor.vue'
import OrderBar from '../components/OrderBar.vue'
import AiPanel from '../components/AiPanel.vue'

const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{"name":"医生"}')
const queue = ref([])
const activePatient = ref(null)
const searchKeyword = ref('')
const searchResults = ref([])
const orderType = ref('')
const orderDetail = ref('')
const todayCount = ref(12)
const toast = reactive({ msg: '', show: false })
let timer

function showToast(msg) { toast.msg = msg; toast.show = true; setTimeout(() => toast.show = false, 3000) }

async function loadQueue() {
  try { const { data } = await getQueue('d-001'); queue.value = data } catch { /* NOP */ }
}
async function doCallNext() {
  try { const { data } = await callNext('d-001'); activePatient.value = data; todayCount.value++; showToast('已叫号 #' + data.queueNumber + ' ' + data.patientName) } catch { showToast('无候诊患者') }
}
function selectPatient(p) { activePatient.value = p }
async function doSaveEmr(content) {
  if (!activePatient.value) return showToast('请先选择患者')
  try { await saveEmr(activePatient.value.id, content); showToast('病历已保存 ✅') } catch { showToast('保存失败') }
}
async function doSubmitOrder() {
  if (!activePatient.value) return
  try {
    const key = activePatient.value.id + '_' + Date.now()
    await submitOrder(key, activePatient.value.id, orderType.value, JSON.stringify(orderDetail.value))
    orderType.value = ''; showToast('医嘱已开具 — 同步确认 accepted ✅')
  } catch { showToast('提交失败') }
}
async function doEndConsultation() {
  if (!activePatient.value) return
  try {
    await completeQueue(activePatient.value.id)
    showToast('就诊结束 ✅'); activePatient.value = null; loadQueue()
  } catch { showToast('操作失败') }
}
async function doSearch() {
  try { const { data } = await searchEmr(searchKeyword.value); searchResults.value = data } catch { searchResults.value = [] }
}
function logout() {
  localStorage.clear(); router.push('/')
}

onMounted(() => { loadQueue(); timer = setInterval(loadQueue, 5000) })
onUnmounted(() => clearInterval(timer))
</script>

<style scoped>
.ws { display: flex; height: 100vh; }
/* Sidebar */
.sidebar {
  width: 64px; background: var(--bg-sidebar); display: flex;
  flex-direction: column; align-items: center; padding: 16px 0; gap: 8px;
}
.logo {
  width: 40px; height: 40px; background: linear-gradient(135deg,#1a9fad,#0f7a88);
  border-radius: 12px; line-height: 40px; text-align: center; color: #fff;
  font-size: 20px; font-weight: 700; margin-bottom: 16px;
  box-shadow: 0 4px 12px rgba(15,122,136,0.35);
}
.s-btn {
  width: 40px; height: 40px; border-radius: 10px; border: none;
  background: transparent; color: rgba(255,255,255,0.45); cursor: pointer;
  font-size: 18px; transition: 0.2s;
}
.s-btn:hover, .s-btn.active { background: rgba(255,255,255,0.08); color: #fff; }
.spacer { flex: 1; }

/* Center */
.center { flex: 1; overflow-y: auto; padding: 20px 24px; display: flex; flex-direction: column; gap: 16px; }
.top-bar {
  display: flex; align-items: center; gap: 16px;
  padding: 12px 20px; background: var(--bg-card);
  border-radius: var(--radius-lg); box-shadow: var(--shadow-sm);
}
.avatar {
  width: 40px; height: 40px; border-radius: 50%; line-height: 40px; text-align: center;
  background: linear-gradient(135deg,#1a7f8c,#39a0b0); color: #fff; font-weight: 700;
}
.dr-name { font-size: 14px; font-weight: 600; }
.dr-dept { font-size: 12px; color: var(--text-muted); }
.spacer { flex: 1; }
.consult-banner {
  background: linear-gradient(135deg,#e0f4f5,#d0edf0);
  border-radius: var(--radius-md); padding: 10px 16px; font-size: 13px;
  display: flex; align-items: center; gap: 8px; color: var(--accent-dark);
}
.live-dot { width: 8px; height: 8px; border-radius: 50%; background: var(--accent); animation: pulse 1.5s infinite; }
@keyframes pulse { 0%,100%{opacity:1} 50%{opacity:0.4} }

.card { background: var(--bg-card); border-radius: var(--radius-lg); padding: 20px; box-shadow: var(--shadow-sm); }
.card h3 { font-size: 13px; font-weight: 600; color: var(--text-secondary); margin-bottom: 12px; text-transform: uppercase; }
.result-item { padding: 8px 0; border-bottom: 1px solid var(--border); font-size: 13px; }

/* Modal */
.modal-mask {
  position: fixed; inset: 0; background: rgba(11,25,41,0.5);
  display: flex; align-items: center; justify-content: center; z-index: 1000;
  backdrop-filter: blur(4px);
}
.modal {
  background: #fff; border-radius: var(--radius-lg); padding: 28px;
  width: 480px; box-shadow: var(--shadow-lg);
}
.modal h3 { font-size: 16px; margin-bottom: 20px; }
.form-g { margin-bottom: 16px; }
.form-g label { display: block; font-size: 12px; font-weight: 600; color: var(--text-secondary); margin-bottom: 6px; }
.form-g input, .form-g select {
  width: 100%; padding: 10px 12px; border: 1px solid var(--border);
  border-radius: var(--radius-sm); font-size: 13px; outline: none; font-family: inherit;
}
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px; }

.btn-primary {
  padding: 10px 24px; border: none; border-radius: 20px;
  background: var(--accent); color: #fff; font-size: 14px; font-weight: 600; cursor: pointer;
}
.btn-primary:hover { background: var(--accent-dark); }
.btn-ghost {
  padding: 10px 20px; border: 1px solid var(--border); border-radius: 20px;
  background: #fff; color: var(--text-secondary); font-size: 13px; cursor: pointer;
}

/* Toast */
.toast {
  position: fixed; top: 24px; right: 24px; padding: 12px 20px;
  background: var(--success); color: #fff; border-radius: var(--radius-md);
  font-size: 13px; font-weight: 600; z-index: 2000;
  opacity: 0; transform: translateY(-10px); transition: 0.3s; pointer-events: none;
}
.toast.show { opacity: 1; transform: translateY(0); }
</style>
