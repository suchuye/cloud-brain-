import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'login',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/workstation',
    name: 'workstation',
    component: () => import('../views/WorkstationView.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth && !localStorage.getItem('token')) {
    next('/')
  } else {
    next()
  }
})

export default router
