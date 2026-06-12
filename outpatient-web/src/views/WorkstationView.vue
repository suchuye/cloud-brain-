<template>
  <div class="workstation">
    <!-- Sidebar Navigation -->
    <Sidebar @logout="handleLogout" />

    <!-- Left: Patient Queue -->
    <QueuePanel
      :patients="store.queue"
      :activeId="store.current?.id"
      :loading="store.loading"
      @select="store.selectPatient"
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
        @saved="handleEmrSaved"
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
import Sidebar from '../components/Sidebar.vue'
import QueuePanel from '../components/QueuePanel.vue'
import TopBar from '../components/TopBar.vue'
import PatientInfoCard from '../components/PatientInfoCard.vue'
import EmrEditor from '../components/EmrEditor.vue'
import OrderBar from '../components/OrderBar.vue'
import EmrSearchBar from '../components/EmrSearchBar.vue'
import AiAssistantPanel from '../components/AiAssistantPanel.vue'
import OrderModal from '../components/OrderModal.vue'
import Toast from '../components/Toast.vue'

const router = useRouter()
const auth = useAuthStore()
const store = useConsultationStore()
const toastRef = ref(null)
const showOrderModal = ref(false)
const orderType = ref('')
let pollTimer

function showToast(msg) { toastRef.value?.show(msg) }

async function handleCallNext() {
  const patient = await store.callNextPatient()
  if (patient) showToast(`已叫号 #${patient.queueNumber} ${patient.patientName}`)
  else showToast('当前无候诊患者')
}

async function handleEndConsult() {
  if (!store.current) return
  try {
    await store.endConsult(store.current.id)
    showToast('就诊结束 ✅')
  } catch { showToast('操作失败') }
}

function openOrderModal(type) {
  if (!store.current) return showToast('请先选择接诊患者')
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

function handleEmrSaved() { showToast('病历已保存（已同步索引至 ES）') }

function handleLogout() { auth.logout(); router.push('/') }

onMounted(() => {
  store.fetchQueue()
  pollTimer = setInterval(() => store.fetchQueue(), 5000)
})
onUnmounted(() => clearInterval(pollTimer))
</script>

<style scoped>
.workstation { display: flex; height: 100vh; }
.workspace {
  flex: 1; overflow-y: auto; padding: 20px 24px;
  display: flex; flex-direction: column; gap: 16px;
}
</style>
