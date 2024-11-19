<template>
  <div class="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center">
    <div class="bg-white p-6 rounded-lg shadow-lg w-96">
      <h2 class="text-xl font-bold mb-4">
        {{ isEditMode ? 'Edit Category' : 'Add New Category' }}
      </h2>
      <form>
        <!-- Name Field -->
        <input
          type="text"
          v-model="category.name"
          placeholder="Category Name"
          class="w-full border p-2 rounded mb-4"
        />
        <span v-if="category.name.trim() === ''" class="text-red-500 text-sm">
          Category name is required.
        </span>

        <!-- Actions -->
        <div>
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
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCategoryManager } from '@/stores/categoryStore.js'
import { useToastStore } from '@/stores/toastStore'
import { fetchPost, fetchPut, fetchGet } from '@/utils/fetchUtil.js'

const router = useRouter()
const categoryManager = useCategoryManager()
const toastStore = useToastStore()

const category = ref({
  id: null,
  name: '',
})

const isEditMode = ref(false)
const isFormValid = computed(() => category.value.name.trim() !== '')

const closePopup = () => {
  router.push({ name: 'categories' })
}

onMounted(() => {
  const route = router.currentRoute.value

  if (route.name === 'editCategory') {
    isEditMode.value = true
    fetchCategoryDetails(route.params.id)
  }
})

function handleSubmit(event) {
  event.preventDefault()

  if (isEditMode.value) {
    const categoryId = router.currentRoute.value.params.id
    fetchPut(`/categories/${categoryId}`, { name: category.value.name })
      .then((response) => {
        categoryManager.editCategory(categoryId, response)
        toastStore.addToast({ message: 'Updating category successfully', type: 'success' })
        closePopup()
      })
      .catch(() => {
        toastStore.addToast({ message: 'Error updating category', type: 'error' })
        closePopup()
      })
  } else {
    fetchPost('/categories', { name: category.value.name })
      .then((response) => {
        categoryManager.addCategory(response)
        toastStore.addToast({ message: 'Creating category successfully', type: 'success' })
        closePopup()
      })
      .catch(() => {
        toastStore.addToast({ message: 'Error creating category', type: 'error' })
        closePopup()
      })
  }
}

const fetchCategoryDetails = async (id) => {
  try {
    const categoryData = await fetchGet(`/categories/${id}`)
    category.value = categoryData
  } catch (error) {
    toastStore.addToast({
      message: 'Error fetching category details: Category not found or an error occurred.',
      type: 'error',
    })
    closePopup()
  }
}
</script>

<style scoped></style>
