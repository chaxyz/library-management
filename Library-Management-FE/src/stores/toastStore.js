import { defineStore } from 'pinia'

export const useToastStore = defineStore('toast', {
  state: () => ({
    messages: [],
  }),
  actions: {
    addToast({ message, type }) {
      const id = Date.now()
      this.messages.push({ id, message, type })

      setTimeout(() => {
        this.removeToast(id)
      }, 3000)
    },
    removeToast(id) {
      this.messages = this.messages.filter((toast) => toast.id !== id)
    },
    getToastClass(type) {
      return (
        {
          success: 'bg-green-500 text-white',
          error: 'bg-red-500 text-white',
        }[type] || 'bg-gray-500 text-white'
      )
    },
  },
})
