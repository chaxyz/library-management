<template>
  <div class="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center">
    <div class="bg-white p-6 rounded-lg shadow-lg w-96">
      <h2 class="text-xl font-bold mb-4">
        {{ isDetailMode ? 'Book Details' : isEditMode ? 'Edit Book' : 'Add New Book' }}
      </h2>
      <form>
        <!-- Name Field -->
        <input
          type="text"
          v-model="book.name"
          placeholder="Book Name"
          class="w-full border p-2 rounded mb-4"
          :disabled="isDetailMode"
        />
        <span v-if="book.name.trim() === ''" class="text-red-500 text-sm">
          Book name is required.
        </span>

        <!-- Category -->
        <div>
          <label>Category:</label>
          <select
            v-model="book.category.id"
            class="w-full border p-2 rounded mb-4"
            :disabled="isDetailMode"
          >
            <option value="" disabled selected>Select a category</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
          <span v-if="book.category.id === null" class="text-red-500 text-sm">
            Category is required.
          </span>
        </div>

        <!-- Status -->
        <div>
          <label>Status:</label>
          <label>
            <input type="radio" v-model="book.status" value="returned" :disabled="isDetailMode" />
            Returned
          </label>
          <label>
            <input type="radio" v-model="book.status" value="borrowed" :disabled="isDetailMode" />
            Borrowed
          </label>
          <span v-if="book.status === ''" class="text-red-500 text-sm"> Status is required. </span>
        </div>

        <!-- File Upload -->

        <div v-if="!isDetailMode">
          <label>Upload File:</label>
          <input type="file" class="w-full border p-2 rounded mb-4" ref="fileInput" />
        </div>

        <!-- Actions -->
        <div v-if="!isDetailMode">
          <button
            class="bg-blue-500 text-white px-4 py-2 rounded mr-2"
            type="submit"
            :disabled="!isFormValid"
            @click="handleSubmit"
          >
            {{ isEditMode ? 'Save Changes' : 'Submit' }}
          </button>

          <button @click.prevent="closePopup" class="bg-gray-500 text-white px-4 py-2 rounded">
            Cancel
          </button>
        </div>
        <button
          v-if="isDetailMode"
          @click.prevent="closePopup"
          class="bg-gray-500 text-white px-4 py-2 rounded"
        >
          Close
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCategoryManager } from '@/stores/categoryStore.js'
import { useToastStore } from '@/stores/toastStore'
import {
  fetchFileUpload,
  fetchApi,
  fetchGet,
  fetchDelete,
  fetchPut,
  fetchPost,
} from '@/utils/fetchUtil.js'
import { useBookManager } from '@/stores/bookStore.js'
const router = useRouter()
const bookManager = useBookManager()
const categoryManager = useCategoryManager()
const toastStore = useToastStore()
const fileInput = ref(null)
const book = ref({
  id: null,
  name: '',
  category: { id: null, name: '' },
  status: '',
})

const categories = computed(() => categoryManager.getCategories())
const isDetailMode = ref(false)
const isEditMode = ref(false)
const isAddMode = computed(() => !isEditMode.value && !isDetailMode.value)
const isFormValid = computed(() => {
  return (
    book.value.name.trim() !== '' && book.value.category.id !== null && book.value.status !== ''
  )
})

const closePopup = () => {
  router.push({ name: 'library' })
}

onMounted(() => {
  const route = router.currentRoute.value

  if (route.name === 'bookDetails') {
    isDetailMode.value = true
    fetchBookDetails(route.params.id)
  } else if (route.name === 'editBook') {
    isEditMode.value = true
    fetchBookDetails(route.params.id)
  }
})

function handleSubmit(event) {
  event.preventDefault()
  const file = fileInput.value?.files[0]
  const bookData = {
    name: book.value.name,
    categoryId: book.value.category.id,
    status: book.value.status,
  }

  if (isAddMode.value) {
    fetchFileUpload('/books', file, bookData)
      .then((response) => {
        bookManager.addBook(response)
        toastStore.addToast({ message: 'Creating book successfully', type: 'success' })
        closePopup()
      })
      .catch((error) => {
        toastStore.addToast({ message: 'Error creating book', type: 'error' })
      })
  } else if (isEditMode.value) {
    const bookId = router.currentRoute.value.params.id
    fetchFileUpload(`/books/${bookId}`, file, bookData, { method: 'PUT' })
      .then((response) => {
        bookManager.editBook(bookId, response)
        toastStore.addToast({ message: 'Updating book successfully', type: 'success' })
        closePopup()
      })
      .catch((error) => {
        toastStore.addToast({ message: 'Error updating book', type: 'error' })
      })
  }
}

const fetchBookDetails = async (id) => {
  try {
    const bookData = await fetchGet(`/books/${id}`)
    book.value = bookData
  } catch (error) {
    let message = 'Error fetching book details: '
    if (error.message == 404) {
      message += 'Book not found'
    } else {
      message += 'Some thing went wrong'
    }
    toastStore.addToast({ message, type: 'error' })
    closePopup()
  }
}
</script>
