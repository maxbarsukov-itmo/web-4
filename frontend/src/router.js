import { createRouter, createWebHistory } from 'vue-router'
import Main from '@/pages/Main.vue'
import NotFoundError from '@/pages/Error.vue'
import Registration from '@/pages/Registration.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'default-page',
      component: Registration,
      beforeEnter: (to, from, next) => {
        (localStorage.getItem("jwt") !== null)
          ? next({name: 'app-page'})
          : next({name: 'auth-page'});
      }
    },
    {
      path: '/auth',
      name: 'auth-page',
      component: Registration,
      beforeEnter: (to, from, next) => {
        (localStorage.getItem("jwt") !== null)
          ? next({name: 'app-page'})
          : next({name: 'auth-page'});
      }
    },
    {
      path: '/app',
      name: 'app-page',
      component: Main,
      beforeEnter: (to, from, next) => {
        if (localStorage.getItem("jwt")) next();
        else next({
          name: 'error-page-app',
        });
      }
    },
    {
      path: '/*',
      name: 'error-page',
      component: NotFoundError,
      props: {
        default: true,
        errorCode: "404",
        errorMessage: "Not Found"
      }
    },
    {
      path: '/error',
      name: 'error-page-app',
      component: NotFoundError,
      props: {
        default: true,
        errorCode: "401",
        errorMessage: "Unauthorized"
      }
    }
  ]
})

export default router
