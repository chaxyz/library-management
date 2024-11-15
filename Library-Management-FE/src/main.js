import './assets/main.css'

import { createApp } from 'vue'
import ToastPlugin from 'vue-toast-notification';
import { createPinia } from 'pinia'

import App from './App.vue'
// import router from './router'

const app = createApp(App)
app.use(ToastPlugin, {position: 'bottom-left'});

// app.use(createPinia())
// app.use(router)

app.mount('#app')
