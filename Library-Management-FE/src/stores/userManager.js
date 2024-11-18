import { ref } from 'vue'
export const username = ref('')
export const userRole = ref('')

export function decodeJWT(token) {
  try {
    const [header, payload, signature] = token.split('.')
    const decodedHeader = JSON.parse(atob(header))
    const decodedPayload = JSON.parse(atob(payload))

    if (decodedHeader.typ !== 'JWT' || decodedHeader.alg !== 'HS256') {
      throw new Error('Invalid JWT header')
    }

    const requiredFields = ['role', 'name', 'oid', 'sub', 'iat', 'exp', 'iss']
    for (const field of requiredFields) {
      if (!decodedPayload.hasOwnProperty(field)) {
        throw new Error(`Missing required field in JWT payload: ${field}`)
      }
    }

    username.value = decodedPayload.name
    localStorage.setItem('username', decodedPayload.name)
    return {
      header: decodedHeader,
      payload: decodedPayload,
      signature: signature,
    }
  } catch (error) {
    throw new Error('Invalid JWT Token: ' + error.message)
  }
}

export async function login(userCredentials, router) {
  const response = await apiRequest(
    `${import.meta.env.VITE_BASE_URL}/login`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ ...userCredentials }),
      credentials: 'include',
    },
    router,
  )

  if (!response.ok) {
    return response.status
  }

  const data = await response.json()
  localStorage.setItem('token', data.access_token)
  localStorage.setItem('refresh_token', data.refresh_token)
  return data
}

export function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('refresh_token')
  localStorage.removeItem('username')
  username.value = ''
}

export async function refreshToken(router) {
  const refresh_token = localStorage.getItem('refresh_token')
  if (!refresh_token) {
    logout()
    router.replace({ name: 'login' })
    return null
  }

  try {
    const response = await fetch(`${import.meta.env.VITE_BASE_URL}/token`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${refresh_token}`,
      },
      credentials: 'include',
    })

    if (response.ok) {
      const data = await response.json()
      localStorage.setItem('token', data.access_token)
      return data.access_token //
    } else if (response.status === 401) {
      logout()
      router.replace({ name: 'login' })
      return null
    } else {
      alert('There was a problem refreshing the token. Please try again later.')
      return null
    }
  } catch (error) {
    alert('Error occurred while refreshing token.')
    return null
  }
}

export function useAuthGuard(router) {
  router.beforeEach(async (to, from, next) => {
    const requiresAuth = to.matched.some((record) => record.meta.requiresAuth)

    if (!requiresAuth) {
      return next()
    }

    const token = localStorage.getItem('token')
    if (!token) {
      logout()
      return next({ name: 'login' })
    }

    try {
      const decodedToken = decodeJWT(token)
      const currentTime = Math.floor(Date.now() / 1000)

      if (decodedToken.payload.exp < currentTime) {
        const newToken = await refreshToken(router)
        if (newToken) {
          localStorage.setItem('token', newToken)
          return next()
        } else {
          logout()
          return next({ name: 'login' })
        }
      }

      next()
    } catch (error) {
      logout()
      next({ name: 'login' })
    }
  })
}

async function apiRequest(url, options = {}, router) {
  let token = localStorage.getItem('token')

  if (token) {
    options.headers = {
      ...options.headers,
      Authorization: `Bearer ${token}`,
    }
  }

  const response = await fetch(url, options)

  if (response.status === 401) {
    const newToken = await refreshToken(router)

    if (newToken) {
      token = newToken
      options.headers.Authorization = `Bearer ${newToken}`
      return await fetch(url, options)
    } else {
      logout()
      router.replace({ name: 'login' })
    }
  }

  return response
}
