import { reactive } from 'vue'
import { defineStore, acceptHMRUpdate } from 'pinia'
export const useCategoryManager = defineStore('categoryManager', () => {
  const categories = reactive([])
  const getCategories = function () {
    return categories
  }
  const setCategory = function (categoriesList = []) {
    categories.length = 0
    if (categoriesList != null) {
      categoriesList.forEach((category) => {
        categories.push(category)
      })
    }
  }
  const addCategory = function (newCategory) {
    categories.push(newCategory)
  }
  const findIndexById = function (id) {
    return categories.findIndex((el) => el.id === id)
  }
  const editCategory = function (id, newCategory) {
    const index = findIndexById(id)
    categories[index] = newCategory
  }

  const deleteCategory = function (id) {
    const index = categories.findIndex((el) => {
      return el.id == id
    })
    categories.splice(index, 1)
  }

  return {
    getCategories,
    setCategory,
    addCategory,
    editCategory,
    deleteCategory,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useCategoryManager, import.meta.hot))
}
