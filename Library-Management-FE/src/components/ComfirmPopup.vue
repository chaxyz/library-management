<template>
  <div
    class="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50"
    @click.self="handleCancel"
  >
    <div class="bg-white rounded-lg p-6 shadow-lg">
      <h2 class="text-lg font-semibold mb-4">Are you sure you want to delete this book?</h2>
      <p class="text-gray-600 mb-6">This action cannot be undone.</p>
      <div class="flex justify-end space-x-4">
        <button
          @click="handleCancel"
          class="bg-gray-300 hover:bg-gray-400 text-gray-800 px-4 py-2 rounded"
        >
          Cancel
        </button>
        <button
          @click="handleConfirm"
          class="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded"
        >
          Confirm
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { fetchGet, fetchDelete, fetchPut, fetchPost } from '../utils/fetchUtil.js'
import { useBookManager } from '@/stores/bookStore'
import { useToastStore } from '@/stores/toastStore.js'
import { useCategoryManager } from '@/stores/categoryStore.js'
const router = useRouter()
const message = ref('')
const bookManager = useBookManager()
const toastStore = useToastStore()
const catagoryManager = useCategoryManager()
let path
onMounted(() => {
  const route = router.currentRoute.value
  if (route.name === 'deleteBook') {
    message.value = 'Are you sure you want to delete this book?'
    path = 'books'
  } else if (route.name === 'deleteCategory') {
    message.value = 'Are you sure you want to delete this catagory?'
    path = 'categories'
  }
})

async function handleConfirm() {
  const { id } = router.currentRoute.value.params

  try {
    const response = await fetchDelete(`/${path}/${id}`)
    if (path == 'books') {
      bookManager.deleteBook(id)
    } else if (path == 'categories') {
      catagoryManager.deleteCategory(id)
    }

    toastStore.addToast({ message: 'Deleting book successfully', type: 'success' })
    closePopup()
  } catch (error) {
    let message = 'failed to fetch'
    if (error.message.includes('401')) {
      message = 'Invalid credentials. Please check your username or password.'
    } else if (error.message.includes('500')) {
      message = 'Server error. Please try again later.'
    }
    toastStore.addToast({ message, type: 'error' })
    closePopup()
  }
}

function handleCancel() {
  closePopup()
}

function closePopup() {
  const route = router.currentRoute.value
  if (route.name === 'deleteBook') {
    router.push({ name: 'library' })
  } else if (route.name === 'deleteCategory') {
    router.push({ name: 'categories' })
  }
}
</script>
