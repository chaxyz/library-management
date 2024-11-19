<template>
  <header class="bg-indigo-600 text-white py-4 px-6 flex items-center justify-between">
    <div class="flex items-center space-x-6">
      <div class="hover:underline cursor-pointer" @click="navigateHome">Home</div>

      <div v-if="showCategory" class="relative">
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
            <li
              v-for="(category, index) in categories"
              :key="index"
              @click="handleSelectCategory(category.id)"
              class="block px-4 py-2 hover:bg-indigo-600 cursor-pointer"
            >
              {{ category.name }}
            </li>
          </ul>
        </div>
      </div>
    </div>

    <div class="flex items-center space-x-4">
      <input
        type="text"
        v-model="searchQuery"
        @input="emitSearch"
        placeholder="Search..."
        class="px-4 py-2 rounded-md text-gray-800"
      />
      <button
        v-if="showAddButton"
        @click="emitAdd"
        class="bg-green-500 text-white px-4 py-2 rounded"
      >
        {{ addButtonText }}
      </button>
      <span>Welcome, {{ isUserLogin ? username : 'Guest' }}</span>
      <button
        @click="handleLoginLogout"
        class="border border-white rounded-md p-2 hover:bg-indigo-700 cursor-pointer"
      >
        {{ isUserLogin ? 'Logout' : 'Login' }}
      </button>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  isUserLogin: { type: Boolean, required: true },
  username: { type: String, default: '' },
  addButtonText: { type: String, default: '+ Add Item' },
  showCategory: { type: Boolean, default: false },
  showAddButton: { type: Boolean, default: true },
  categories: { type: Array, default: () => [] },
})

// Emits
const emit = defineEmits(['search', 'add', 'login', 'logout', 'selectCategory'])

const router = useRouter()
const searchQuery = ref('')
const showDropdown = ref(false)
let hideTimeout

const navigateHome = () => router.push({ name: 'library' })
const emitSearch = () => emit('search', searchQuery.value)
const emitAdd = () => emit('add')
const handleLoginLogout = () => {
  if (props.isUserLogin) {
    emit('logout')
  } else {
    emit('login')
  }
}
const handleSelectCategory = (categoryId) => emit('selectCategory', categoryId)
const hideDropdown = () => {
  hideTimeout = setTimeout(() => {
    showDropdown.value = false
  }, 300)
}
const clearHideTimeout = () => clearTimeout(hideTimeout)
</script>
