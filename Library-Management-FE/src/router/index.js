import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/components/LoginPage.vue'
import Homepage from '@/components/Homepage.vue'
import BookPopup from '@/components/BookPopup.vue'
import ComfirmPopup from '@/components/ComfirmPopup.vue'
import CategoryPage from '@/components/CategoryPage.vue'
import CategoryPopup from '@/components/CategoryPopup.vue'
const history = createWebHistory(import.meta.env.BASE_URL)

const routes = [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/login',
    name: 'login',
    component: LoginPage,
  },
  {
    path: '/library',
    name: 'library',
    component: Homepage,
    children: [
      {
        path: 'add',
        name: 'addBook',
        component: BookPopup,
      },
      {
        path: ':id',
        name: 'bookDetails',
        component: BookPopup,
      },
      {
        path: ':id/edit',
        name: 'editBook',
        component: BookPopup,
      },
      {
        path: ':id/delete',
        name: 'deleteBook',
        component: ComfirmPopup,
      },
    ],
  },
  {
    path: '/categories',
    name: 'categories',
    component: CategoryPage,
    children: [
      {
        path: 'add',
        name: 'addCategory',
        component: CategoryPopup,
      },
      {
        path: ':id/edit',
        name: 'editCategory',
        component: CategoryPopup,
      },
      {
        path: ':id/delete',
        name: 'deleteCategory',
        component: ComfirmPopup,
      },
    ],
  },
]

const router = createRouter({
  history,
  routes,
  linkActiveClass: 'text-[#2ff6da]',
  linkExactActiveClass: 'hover:text-[#2ff6da] hover:text-[#2ff6da] p-2',
})

export default router
