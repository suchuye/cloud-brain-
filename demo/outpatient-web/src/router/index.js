import { createRouter, createWebHistory } from 'vue-router'
import Workstation from '../views/Workstation.vue'
import Login from '../views/Login.vue'

const routes = [
  { path: '/', name: 'login', component: Login },
  { path: '/workstation', name: 'workstation', component: Workstation }
]

export default createRouter({ history: createWebHistory(), routes })
