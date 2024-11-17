import { reactive } from 'vue'
import { defineStore, acceptHMRUpdate } from 'pinia'
export const useBookManager = defineStore('bookManager', () => {
  const books = reactive([])
  const getBooks = function () {
    return books
  }
  const setBook = function (bookList = []) {
    books.length = 0
    if (bookList != null) {
      bookList.forEach((book) => {
        books.push(book)
      })
    }
  }
  const addBook = function (newBook) {
    books.push(newBook)
  }
  const findIndexById = function (id) {
    return books.findIndex((el) => el.id === id)
  }
  const editBook = function (id, newBook) {
    const index = findIndexById(id)
    books[index] = newBook
  }

  const deleteBook = function (id) {
    const index = tasks.findIndex((el) => {
      return el.id == id
    })
    books.splice(index, 1)
  }

  return {
    getBooks,
    setBook,
    addBook,
    editBook,
    deleteBook,
  }
})

if (import.meta.hot) {
  import.meta.hot.accept(acceptHMRUpdate(useBookManager, import.meta.hot))
}
