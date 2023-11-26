import { createApp } from 'vue'
import App from './App.vue'
import Notifications from '@kyvg/vue3-notification'
import router from './router'

const app = createApp(App)
app.use(router)
app.use(Notifications)
app.mount('#app')
