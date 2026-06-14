<template>
  <div class="workstation">
    <!-- Sidebar Navigation -->
    <Sidebar @logout="handleLogout" />

    <!-- Left: Patient Queue -->
    <QueuePanel
      :patients="store.queue"
      :activeId="store.current?.id"
      :consulting="!!store.current"
      :loading="store.loading"
      @call-next="handleCallNext"
    />

    <!-- Center: Clinical Workspace -->
    <main class="workspace">
      <TopBar
        :doctor="auth.user"
        :today-count="store.todayCount"
        :consulting="store.current"
        @end-consult="handleEndConsult"
      />

      <PatientInfoCard :patient="store.current" />

      <EmrEditor
        :patient-id="store.current?.id"
        @saved="(content) => handleEmrSaved(content)"
      />

      <OrderBar
        :disabled="!store.current"
        @order="openOrderModal"
        @end="handleEndConsult"
      />

      <EmrSearchBar />
    </main>

    <!-- Right: AI Assistant -->
    <AiAssistantPanel :consulting="!!store.current" />

    <!-- Order Modal -->
    <OrderModal
      :visible="showOrderModal"
      :order-type="orderType"
      @close="showOrderModal = false"
      @submit="handleSubmitOrder"
    />

    <!-- Verify Modal -->
    <VerifyModal
      :visible="showVerify"
      :user-id="auth.user?.id"
      :title="verifyTitle"
      @close="showVerify = false"
      @verified="handleVerified"
    />

    <!-- Patient History -->
    <div class="card" v-if="patientHistory.length">
      <h3>📂 患者历史病历 ({{ patientHistory.length }} 条)</h3>
      <div v-for="h in patientHistory" :key="h.id" class="history-item">
        <div class="history-head">
          <span>📅 {{ h.id?.substring(0,8) }}</span>
          <span style="color:var(--text-muted);font-size:11px">{{ h.consultationId }}</span>
        </div>
        <p>{{ h.chiefComplaint || '（无主诉）' }}</p>
      </div>
    </div>

    <!-- Toast -->
    <Toast ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useConsultationStore } from '../stores/consultation'
import { submitOrder } from '../api/order'
import { getPatientEmrs } from '../api/outpatient'
import { useAiStore } from '../stores/ai'
import Sidebar from '../components/Sidebar.vue'
import QueuePanel from '../components/QueuePanel.vue'
import TopBar from '../components/TopBar.vue'
import PatientInfoCard from '../components/PatientInfoCard.vue'
import EmrEditor from '../components/EmrEditor.vue'
import OrderBar from '../components/OrderBar.vue'
import EmrSearchBar from '../components/EmrSearchBar.vue'
import AiAssistantPanel from '../components/AiAssistantPanel.vue'
import OrderModal from '../components/OrderModal.vue'
import VerifyModal from '../components/VerifyModal.vue'
import Toast from '../components/Toast.vue'

const router = useRouter()
const auth = useAuthStore()
const store = useConsultationStore()
const ai = useAiStore()
const toastRef = ref(null)
const showOrderModal = ref(false)
const orderType = ref('')
const showVerify = ref(false)
const verifyTitle = ref('')
const pendingAction = ref(null)
const patientHistory = ref([])
let pollTimer

function showToast(msg) { toastRef.value?.show(msg) }

async function handleCallNext() {
  if (store.current) {
    showToast('请先结束当前就诊')
    return
  }
  const patient = await store.callNextPatient()
  if (patient) {
    showToast(`已叫号 #${patient.queueNumber} ${patient.patientName}`)
    loadPatientHistory(patient.patientId)
    ai.connect(patient.id, auth.user?.id || 'd-001')
  } else {
    showToast('当前无候诊患者')
  }
}

async function loadPatientHistory(patientId) {
  try {
    const { data } = await getPatientEmrs(patientId)
    patientHistory.value = data
  } catch { patientHistory.value = [] }
}

function handleEndConsult() {
  if (!store.current) return
  verifyTitle.value = '结束就诊需二次确认'
  pendingAction.value = 'end'
  showVerify.value = true
}

async function handleVerified() {
  if (pendingAction.value === 'end') {
    try {
      await store.endConsult(store.current.id)
      ai.disconnect()
      patientHistory.value = []
      showToast('就诊结束 ✅')
    } catch { showToast('操作失败') }
  } else if (pendingAction.value === 'PRESCRIPTION') {
    orderType.value = 'PRESCRIPTION'
    showOrderModal.value = true
  }
  pendingAction.value = null
}

function openOrderModal(type) {
  if (!store.current) return showToast('请先选择接诊患者')
  if (type === 'PRESCRIPTION') {
    verifyTitle.value = '开具处方需二次确认'
    pendingAction.value = type
    showVerify.value = true
    return
  }
  orderType.value = type
  showOrderModal.value = true
}

async function handleSubmitOrder(detail) {
  const key = `${store.current.id}_${Date.now()}`
  try {
    await submitOrder(key, store.current.id, orderType.value, JSON.stringify(detail))
    showOrderModal.value = false
    showToast('医嘱已开具 — accepted ✅')
  } catch { showToast('医嘱提交失败') }
}

function handleEmrSaved(content) {
  showToast('病历已保存（已同步索引至 ES）')
  ai.sendTranscript(content)
}

function handleLogout() { auth.logout(); router.push('/') }

onMounted(() => {
  store.fetchQueue()
  pollTimer = setInterval(() => store.fetchQueue(), 5000)
})
onUnmounted(() => clearInterval(pollTimer))
</script>

<style scoped>
.workstation { display: flex; height: 100%; width: 100%; }
.workspace {
  flex: 1; min-width: 0; overflow-y: auto; padding: 20px 24px;
  display: flex; flex-direction: column; gap: 16px;
}
.card {
  background: var(--bg-card); border-radius: var(--radius-lg);
  padding: 20px; box-shadow: var(--shadow-sm);
}
.card h3 {
  font-size: 13px; font-weight: 600; color: var(--text-secondary);
  margin-bottom: 12px; text-transform: uppercase; letter-spacing: 0.5px;
}
.history-item {
  padding: 10px 0; border-bottom: 1px solid var(--border);
}
.history-item:last-child { border-bottom: none; }
.history-head {
  display: flex; justify-content: space-between; font-size: 12px;
  color: var(--text-secondary); margin-bottom: 4px;
}
.history-item p { font-size: 13px; color: var(--text-primary); }
</style>
