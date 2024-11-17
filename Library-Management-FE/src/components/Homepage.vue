<template>
  <div class="min-h-screen bg-gray-100">
    <header class="bg-indigo-600 text-white py-4 px-6 flex items-center justify-between">
      <div class="flex items-center space-x-6">
        <div class="hover:underline cursor-pointer">Home</div>

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
              <li>
                <a href="#" class="block px-4 py-2 hover:bg-indigo-600">Dashboard</a>
              </li>
              <li>
                <a href="#" class="block px-4 py-2 hover:bg-indigo-600">Settings</a>
              </li>
              <li>
                <a href="#" class="block px-4 py-2 hover:bg-indigo-600">Earnings</a>
              </li>
              <li>
                <a href="#" class="block px-4 py-2 hover:bg-indigo-600">Sign out</a>
              </li>
            </ul>
          </div>
        </div>

        <div class="hover:underline cursor-pointer">Profile</div>
      </div>

      <div class="flex items-center space-x-4">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Search books..."
          class="px-4 py-2 rounded-md text-gray-800"
        />
        <span>Welcome, {{ username }}</span>
      </div>
    </header>

    <!-- Content Section -->
    <div class="p-6 flex flex-wrap gap-4 justify-start">
      <div
        @click="handleClick"
        v-for="(book, index) in bookManager.getBooks()"
        :key="index"
        class="bg-white shadow-md rounded-lg flex flex-col items-center p-4 w-40"
      >
        <img :src="book.path" alt="Book Cover" class="h-32 w-full object-cover rounded-md" />
        <div class="mt-4 text-center">
          <p class="font-semibold text-gray-800">{{ book.title }}</p>
          <p class="text-sm text-gray-600 mt-2">{{ book.description }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { parseJwt } from '@/utils/jwtUtil'
import { fetchApi, fetchGet, fetchDelete, fetchPut, fetchPost } from '../utils/fetchUtil.js'
import { useToastStore } from '@/stores/toastStore.js'
import { useBookManager } from '@/stores/bookStore.js'

// const books = ref([
//   { title: 'Book One', description: 'An amazing book.', image: 'https://via.placeholder.com/150' },
//   {
//     title: 'Book Two',
//     description: 'A thrilling adventure.',
//     image: 'https://via.placeholder.com/150',
//   },
//   {
//     title: 'Book Three',
//     description: 'An insightful read.',
//     image: 'https://via.placeholder.com/150',
//   },
// ])

const searchQuery = ref('')
const username = ref('')
const showDropdown = ref(false)
const toastStore = useToastStore()
const bookManager = useBookManager()
let hideTimeout

const hideDropdown = () => {
  hideTimeout = setTimeout(() => {
    showDropdown.value = false
  }, 300)
}

const clearHideTimeout = () => {
  clearTimeout(hideTimeout)
}

// const filteredBooks = computed(() => {
//   if (!searchQuery.value.trim()) return books.value
//   return books.value.filter((book) =>
//     book.title.toLowerCase().includes(searchQuery.value.toLowerCase()),
//   )
// })

onMounted(async () => {
  const token = localStorage.getItem('token')
  if (token) {
    const decoded = parseJwt(token)
    username.value = decoded?.name || 'Unknown User'
  }
  try {
    const books = await fetchGet('/books')
    bookManager.setBook(books)
    console.log(bookManager.getBooks())
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

function handleClick(e) {
  console.log('test')
}
</script>

<style scoped></style>
