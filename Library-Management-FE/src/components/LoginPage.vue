<script setup>
import { useRouter } from 'vue-router'
import { ref, computed } from 'vue'
import { useToastStore } from '@/stores/toastStore'
import { login } from '@/stores/userManager'
const username = ref('')
const password = ref('')
const toastStore = useToastStore()
const isButtonDisabled = computed(() => username.value.length < 8 || password.value.length < 8)
const router = useRouter()

const handleLogin = async () => {
  const response = await login(
    {
      username: username.value,
      password: password.value,
    },
    router,
  )

  if (response && response.access_token && response.refresh_token) {
    localStorage.setItem('token', response.access_token)
    localStorage.setItem('refresh_token', response.refresh_token)
    router.push({ path: '/library' })
  } else {
    let message = 'Login failed, Try again later.'
    if (response == 401) {
      message = 'Invalid credentials. Please check your username or password.'
    } else if (response == 500) {
      message = 'Server error. Please try again later.'
    }
    toastStore.addToast({ message, type: 'error' })
  }
}

const continueAsGuest = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('refresh_token')
  router.push({ path: '/library' })
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
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
      <button
        @click="continueAsGuest"
        class="w-full px-4 py-2 mt-4 font-semibold text-white bg-green-600 rounded-md hover:bg-green-700 focus:outline-none"
      >
        Continue as Guest
      </button>
    </div>
  </div>
</template>
