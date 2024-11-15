<template>
  <div class="fixed top-4 right-4 space-y-2">
    <div
      v-for="toast in toastStore.messages"
      :key="toast.id"
      :class="[
        'p-4 rounded shadow-lg transition duration-300 ease-in-out',
        getToastClass(toast.type),
      ]"
    >
      <div class="flex items-center justify-between">
        <span>{{ toast.message }}</span>
        <button @click="dismissToast(toast.id)" class="ml-4 text-white hover:opacity-80">✕</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useToastStore } from '@/stores/toastStore'

const toastStore = useToastStore()

const dismissToast = (id) => {
  toastStore.removeToast(id)
}

const getToastClass = (type) => {
  return (
    {
      success: 'bg-green-500 text-white',
      error: 'bg-red-500 text-white',
      warning: 'bg-yellow-500 text-white',
      info: 'bg-blue-500 text-white',
    }[type] || 'bg-gray-500 text-white'
  )
}
</script>

<style scoped></style>
