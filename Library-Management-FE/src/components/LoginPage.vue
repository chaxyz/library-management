<script setup>
import router from '@/router/index.js'
import { ref, computed, onMounted } from 'vue'
import { useToastStore } from '@/stores/toastStore'
import ToastPopup from './ToastPopup.vue'
import { fetchApi, fetchGet, fetchDelete, fetchPut, fetchPost } from '../utils/fetchUtil.js'
const username = ref('')
const password = ref('')
const toastStore = useToastStore()
const isButtonDisabled = computed(() => username.value.length < 8 || password.value.length < 8)

const handleLogin = async () => {
  try {
    const user = await fetchPost('/login', {
      username: username.value,
      password: password.value,
    })
    if (user) {
      localStorage.setItem('token', JSON.stringify(user.access_token))
      localStorage.setItem('refresh_token', JSON.stringify(user.refresh_token))
      router.push({
        path: '/library',
        state: { toastMessage: 'Login successful!', toastType: 'success' },
      })
    }
  } catch (error) {
    let message = 'Login failed. Please try again.'
    if (error.message.includes('401')) {
      message = 'Invalid credentials. Please check your username or password.'
    } else if (error.message.includes('500')) {
      message = 'Server error. Please try again later.'
    }
    toastStore.addToast({ message, type: 'error' })
  }
}

onMounted(async () => {
  try {
    const user = await fetchPost('/login', {
      username: 'test1234',
      password: 'test1234',
    })
    console.log(user)
    fetch('http://localhost:8080/books', {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => response.json())
      .then((data) => console.log(data))
      .catch((error) => console.error('Error:', error))

    const books = await fetchGet('/books')

    console.log(books)
  } catch (error) {
    let message = 'failed to fetch'
    if (error.message.includes('401')) {
      message = 'Invalid credentials. Please check your username or password.'
    } else if (error.message.includes('500')) {
      message = 'Server error. Please try again later.'
    }
    toastStore.addToast({ message, type: 'error' })
  }
})
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <ToastPopup />

    <div class="w-full max-w-md p-8 space-y-4 bg-white rounded-lg shadow-md">
      <h2 class="text-2xl font-bold text-center text-gray-800">Login</h2>

      <form @submit.prevent="handleLogin">
        <div>
          <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
          <input
            type="text"
            id="username"
            v-model="username"
            class="w-full px-4 py-2 mt-1 border rounded-md focus:ring-indigo-500 focus:border-indigo-500"
            placeholder="Enter your username "
            required
          />
        </div>

        <div>
          <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
          <input
            type="password"
            id="password"
            v-model="password"
            class="w-full px-4 py-2 mt-1 border rounded-md focus:ring-indigo-500 focus:border-indigo-500"
            placeholder="Enter your password"
            required
          />
        </div>

        <button
          type="submit"
          :disabled="isButtonDisabled"
          :class="{
            'w-full px-4 py-2 mt-6 font-semibold text-white bg-indigo-600 rounded-md hover:bg-indigo-700 focus:outline-none':
              !isButtonDisabled,
            'w-full px-4 py-2 mt-6 font-semibold text-white bg-gray-400 rounded-md cursor-not-allowed':
              isButtonDisabled,
          }"
        >
          Log In
        </button>
      </form>
    </div>
  </div>
</template>
