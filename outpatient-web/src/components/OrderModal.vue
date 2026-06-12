<template>
  <Teleport to="body">
    <div class="modal-mask" v-if="visible" @click.self="$emit('close')">
      <div class="modal">
        <h3>{{ title }}</h3>
        <div class="form-group" v-if="orderType==='IMAGING'">
          <label>检查项目</label>
          <select v-model="detail">
            <option>头部 CT 平扫</option><option>胸部 CT 平扫</option>
            <option>冠脉 CTA</option><option>心脏超声</option>
          </select>
        </div>
        <div class="form-group" v-if="orderType==='LAB'">
          <label>检验项目</label>
          <select v-model="detail">
            <option>血常规 + CRP</option><option>高敏肌钙蛋白 I</option>
            <option>心肌酶谱</option><option>生化全项</option>
          </select>
        </div>
        <div class="form-group" v-if="orderType==='PRESCRIPTION'">
          <label>处方药品</label>
          <input v-model="detail" placeholder="药品名称、规格、剂量、用法" />
        </div>
        <div class="modal-actions">
          <button class="btn-ghost" @click="$emit('close')">取消</button>
          <button class="btn-primary" @click="$emit('submit', detail)">确认开具</button>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, computed } from 'vue'
const props = defineProps({ visible: Boolean, orderType: String })
defineEmits(['close', 'submit'])

const detail = ref('')
const title = computed(() =>
  ({ IMAGING:'开具检查', LAB:'开具检验', PRESCRIPTION:'开具处方' }[props.orderType] || ''))
</script>

<style scoped>
.modal-mask {
  position: fixed; inset: 0; background: rgba(11,25,41,0.5);
  display: flex; align-items: center; justify-content: center; z-index: 1000;
  backdrop-filter: blur(4px);
}
.modal {
  background: var(--bg-card); border-radius: var(--radius-lg);
  padding: 28px; width: 480px; box-shadow: var(--shadow-lg);
}
.modal h3 { font-size: 16px; font-weight: 600; margin-bottom: 20px; }
.form-group { margin-bottom: 16px; }
.form-group label {
  display: block; font-size: 12px; font-weight: 600;
  color: var(--text-secondary); margin-bottom: 6px;
}
.form-group input, .form-group select {
  width: 100%; padding: 10px 12px; border: 1px solid var(--border);
  border-radius: var(--radius-sm); font-size: 13px; outline: none;
  transition: var(--transition); font-family: var(--font-body);
}
.form-group input:focus, .form-group select:focus { border-color: var(--accent); }
.modal-actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 20px; }
.btn-primary {
  padding: 10px 24px; border: none; border-radius: var(--radius-pill);
  background: var(--accent); color: #fff; font-size: 14px; font-weight: 600;
  cursor: pointer; transition: var(--transition);
}
.btn-primary:hover { background: var(--accent-dark); }
.btn-ghost {
  padding: 10px 20px; border: 1px solid var(--border); border-radius: var(--radius-pill);
  background: var(--bg-card); color: var(--text-secondary); font-size: 13px;
  cursor: pointer; transition: var(--transition);
}
.btn-ghost:hover { background: var(--bg-panel); }
</style>
