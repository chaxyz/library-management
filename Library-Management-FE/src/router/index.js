import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/components/LoginPage.vue'
import Homepage from '@/components/Homepage.vue'
import { parseJwt } from '@/utils/jwtUtil'
const url = import.meta.env.VITE_BASE_URL

async function refreshAccessToken() {
  try {
    const refreshToken = localStorage.getItem('refresh_token')
    const res = await fetch(`${url}/token`, {
      method: 'POST',
      headers: {
        Authorization: `Bearer ${refreshToken}`,
        'content-type': 'application/json',
      },
    })

    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`)
    }

    const data = await res.json()

    localStorage.setItem('token', data.access_token)
    return data
  } catch (error) {
    console.log(`Error during token refresh: ${error}`)
    return null
  }
}

function deleteTokenFromLocalStorage() {
  localStorage.removeItem('token')
  localStorage.removeItem('refresh_token')
}

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
  },
]

const router = createRouter({
  history,
  routes,
  linkActiveClass: 'text-[#2ff6da]',
  linkExactActiveClass: 'hover:text-[#2ff6da] hover:text-[#2ff6da] p-2',
})

router.beforeEach(async (to, from, next) => {
  const publicRoutes = ['/login']
  const token = localStorage.getItem('token')
  const parsedToken = token ? parseJwt(token) : null

  const isTokenExpired = parsedToken && parsedToken.exp * 1000 < Date.now()

  if (publicRoutes.includes(to.path)) {
    return next()
  }

  if (token && !isTokenExpired) {
    return next()
  }

  if (isTokenExpired) {
    const refreshedToken = await refreshAccessToken()
    if (refreshedToken) {
      return next()
    } else {
      deleteTokenFromLocalStorage()
      return next('/login')
    }
  }

  deleteTokenFromLocalStorage()
  next('/login')
})

export default router
