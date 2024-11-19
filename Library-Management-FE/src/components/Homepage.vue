<template>
  <div class="min-h-screen bg-gray-100">
    <header class="bg-indigo-600 text-white py-4 px-6 flex items-center justify-between">
      <div class="flex items-center space-x-6">
        <div class="hover:underline cursor-pointer" @click="handleHomeClick">Home</div>

        <div class="relative">
          <button
            @mouseenter="showDropdown = true"
            @mouseleave="hideDropdown"
            class="text-white bg-indigo-600 hover:bg-indigo-700 focus:ring-4 focus:outline-none focus:ring-indigo-300 font-medium rounded-lg text-sm px-5 py-2.5 inline-flex items-center"
            type="button"
          >
            Category
            <svg
              class="w-2.5 h-2.5 ms-3"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 10 6"
            >
              <path
                stroke="currentColor"
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="m1 1 4 4 4-4"
              />
            </svg>
          </button>

          <div
            v-show="showDropdown"
            @mouseenter="clearHideTimeout"
            @mouseleave="hideDropdown"
            class="absolute left-0 mt-2 z-10 bg-indigo-500 text-white divide-y divide-gray-100 rounded-lg shadow w-44"
          >
            <ul class="py-2 text-sm">
              <li v-for="(category, index) in categoryManager.getCategories()" :key="index">
                <div
                  @click="handleSelectCategory(category.id)"
                  class="block px-4 py-2 hover:bg-indigo-600"
                >
                  {{ category.name }}
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>

      <div class="flex items-center space-x-4">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search books..."
          class="px-4 py-2 rounded-md text-gray-800"
        />
        <div>
          <button @click="navigateToAddBook" class="bg-green-500 text-white px-4 py-2 rounded">
            + Add Book
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
    <div class="flex justify-end p-4 bg-gray-100">
      <button
        class="bg-indigo-500 text-white px-4 py-2 rounded shadow hover:bg-indigo-600"
        @click="navigateToCategory"
      >
        Manage Categories
      </button>
    </div>
    <div class="p-6 flex flex-wrap gap-4 justify-start">
      <div
        v-for="(book, index) in filteredBooks"
        :key="index"
        class="bg-white shadow-md rounded-lg flex flex-col items-center p-4 w-40 justify-between"
      >
        <img
          :src="book.path ? book.path : '/src/assets/bookicon.png'"
          alt="Book Cover"
          class="h-32 w-full object-cover rounded-md"
        />
        <div class="mt-4 text-center">
          <div class="font-semibold text-gray-800">{{ book.name }}</div>
        </div>
        <div class="mt-4 text-center flex items-center justify-center gap-4">
          <span
            @click="router.push({ name: 'bookDetails', params: { id: book.id } })"
            class="cursor-pointer text-blue-500 text-lg"
            title="View Details"
          >
            📖
          </span>
          <span
            @click="router.push({ name: 'editBook', params: { id: book.id } })"
            class="cursor-pointer text-green-500 text-lg"
            title="Edit Book"
          >
            ✏️
          </span>
          <span
            @click="router.push({ name: 'deleteBook', params: { id: book.id } })"
            class="cursor-pointer text-red-500 text-lg"
            title="Delete Book"
          >
            ❌
          </span>
        </div>
        <div
          class="text-sm text-gray-600 mt-2"
          :class="{
            'text-green-700': book.status === 'returned',
            'text-red-700': book.status !== 'returned',
          }"
        >
          {{
            book.status == 'returned'
              ? 'Available'
              : book.status[0].toUpperCase() + book.status.slice(1)
          }}
        </div>
      </div>
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
const showDropdown = ref(false)
const bookList = ref(bookManager.getBooks())
//condition
let hideTimeout

let isLogined = computed(() => username.value && username.value.length > 0)
function navigateToAddBook() {
  router.push({ name: 'addBook' })
}

const hideDropdown = () => {
  hideTimeout = setTimeout(() => {
    showDropdown.value = false
  }, 300)
}

const clearHideTimeout = () => {
  clearTimeout(hideTimeout)
}

const filteredBooks = computed(() => {
  if (!searchQuery.value.trim()) return bookList.value
  return bookList.value.filter((book) =>
    book.name.toLowerCase().includes(searchQuery.value.toLowerCase()),
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

async function handleSelectCategory(categoryId) {
  try {
    const books = await fetchGet(`/books?categoryId=${categoryId}`)
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

function navigateToCategory() {
  router.push({ name: 'categories' })
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
