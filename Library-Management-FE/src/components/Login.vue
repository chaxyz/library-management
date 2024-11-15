<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="w-full max-w-md p-8 space-y-4 bg-white rounded-lg shadow-md">
      <h2 class="text-2xl font-bold text-center text-gray-800">Login</h2>

      <form @submit.prevent="handleLogin">
        <div>
          <label for="username" class="block text-sm font-medium text-gray-700">Email</label>
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

<script setup>
import { ref, computed } from 'vue'
import {useToast} from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-sugar.css';
const $toast = useToast();

const username = ref('')
const password = ref('')

const isButtonDisabled = computed(() => username.value.length < 8 || password.value.length < 8)

const handleLogin = () => {
    const isLoginSuccessful = username.value === 'user@example.com' && password.value === 'password123';
   if(!isLoginSuccessful){
    let instance = $toast.error('Incorrect credentails' , {duration : 3000});
   }
}
</script>
