import { defineStore } from 'pinia'
import { getDoctorQueue, callNext, completeQueue, checkIn, passPatient, cancelQueue } from '../api/scheduling'
import { startConsultation, finishConsultation } from '../api/outpatient'

export const useConsultationStore = defineStore('consultation', {
  state: () => ({
    queue: [],
    current: null,
    todayCount: 0,
    loading: false,
    error: null
  }),

  actions: {
    async fetchQueue(doctorId = 'd-001') {
      try {
        const { data } = await getDoctorQueue(doctorId)
        this.queue = data
      } catch (e) {
        this.error = '获取队列失败'
      }
    },

    async callNextPatient(doctorId = 'd-001') {
      try {
        this.current = (await callNext(doctorId)).data
        this.todayCount++
        return this.current
      } catch {
        this.error = '无候诊患者'
        return null
      }
    },

    async startConsult(consultationId, doctorId = 'd-001') {
      await startConsultation(consultationId, doctorId)
    },

    async endConsult(queueId) {
      await completeQueue(queueId)
      this.current = null
      await this.fetchQueue()
    },

    selectPatient(patient) {
      this.current = patient
    }
  }
})
