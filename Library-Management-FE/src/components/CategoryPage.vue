<template>
  <div class="min-h-screen bg-gray-100">
    <header class="bg-indigo-600 text-white py-4 px-6 flex items-center justify-between">
      <div class="flex items-center space-x-6">
        <div class="hover:underline cursor-pointer" @click="handleHomeClick">Home</div>
      </div>

      <div class="flex items-center space-x-4">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search category..."
          class="px-4 py-2 rounded-md text-gray-800"
        />
        <div>
          <button @click="navigateToAddCategory" class="bg-green-500 text-white px-4 py-2 rounded">
            + Add Category
          </button>
        </div>
        <span
          >Welcome, {{ isLogined ? username[0].toUpperCase() + username.slice(1) : 'Guest' }}</span
        >
        <div
          v-if="isLogined"
          @click="handleSignOutClick"
          class="border border-white rounded-md p-2 hover:bg-indigo-700 cursor-pointer"
        >
          Sign out
        </div>
        <div
          v-if="!isLogined"
          @click="handleLoginClick"
          class="border border-white rounded-md p-2 hover:bg-indigo-700 cursor-pointer"
        >
          Login
        </div>
      </div>
    </header>
    <router-view />
    <div class="p-6 bg-white shadow rounded">
      <h2 class="text-lg font-semibold mb-4">Manage Categories</h2>
      <table class="min-w-full table-auto border-collapse border border-gray-200">
        <thead>
          <tr class="bg-gray-100">
            <th class="border border-gray-200 px-4 py-2 text-left">No.</th>
            <th class="border border-gray-200 px-4 py-2 text-left">Name</th>
            <th class="border border-gray-200 px-4 py-2 text-center">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="(category, index) in filteredCategory"
            :key="category.id"
            class="hover:bg-gray-50"
          >
            <td class="border border-gray-200 px-4 py-2">{{ index + 1 }}</td>
            <td class="border border-gray-200 px-4 py-2">{{ category.name }}</td>
            <td class="border border-gray-200 px-4 py-2 text-center">
              <button
                class="text-indigo-500 hover:underline mr-2"
                @click="editCategory(category.id)"
              >
                Edit
              </button>
              <button class="text-red-500 hover:underline" @click="deleteCategory(category.id)">
                Delete
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { parseJwt } from '@/utils/jwtUtil'
import { fetchGet, fetchDelete, fetchPut, fetchPost } from '../utils/fetchUtil.js'
import { useToastStore } from '@/stores/toastStore.js'
import { useBookManager } from '@/stores/bookStore.js'
import { useCategoryManager } from '@/stores/categoryStore.js'
import { logout } from '@/stores/userManager.js'
import { useRouter } from 'vue-router'

//store
const toastStore = useToastStore()
const bookManager = useBookManager()
const categoryManager = useCategoryManager()
//router
const router = useRouter()
//state
const searchQuery = ref('')
const username = ref('')
const categoryList = ref(categoryManager.getCategories())
let isLogined = computed(() => username.value && username.value.length > 0)

function navigateToAddCategory() {
  router.push({ name: 'addCategory' })
}

const filteredCategory = computed(() => {
  if (!searchQuery.value.trim()) return categoryList.value
  return categoryList.value.filter((category) =>
    category.name.toLowerCase().includes(searchQuery.value.toLowerCase()),
  )
})

onMounted(async () => {
  const token = localStorage.getItem('token')
  if (token) {
    const decoded = parseJwt(token)
    username.value = decoded?.name
  }
  try {
    const books = await fetchGet('/books')
    const category = await fetchGet('/categories')
    categoryManager.setCategory(category)
    bookManager.setBook(books)
  } catch (error) {
    let message = 'failed to fetch'
    if (error == 401) {
      message = 'Invalid credentials. Please check your username or password.'
    } else if (error == 500) {
      message = 'Server error. Please try again later.'
    }
    toastStore.addToast({ message, type: 'error' })
  }
})

function editCategory(id) {
  router.push({ name: 'editCategory', params: { id: id } })
}

function deleteCategory(id) {
  router.push({ name: 'deleteCategory', params: { id: id } })
}
async function handleHomeClick(e) {
  try {
    const books = await fetchGet('/books')
    bookManager.setBook(books)
  } catch (error) {
    let message = 'failed to fetch'
    if (error == 401) {
      message = 'Invalid credentials. Please check your username or password.'
    } else if (error == 500) {
      message = 'Server error. Please try again later.'
    }
    toastStore.addToast({ message, type: 'error' })
  }
  router.replace({ name: 'library' })
}

function handleLoginClick() {
  router.replace({ name: 'login' })
}

function handleSignOutClick(e) {
  logout()
  router.replace({ name: 'login' })
}
</script>

<style scoped></style>
