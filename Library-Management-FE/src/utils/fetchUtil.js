async function fetchApi(url, options = {}) {
  const baseUrl = import.meta.env.VITE_BASE_URL
  const defaultOptions = {
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  }

  const mergedOptions = { ...defaultOptions, ...options }

  const tryRequest = async () => {
    const response = await fetch(`${baseUrl}${url}`, mergedOptions)

    if (response.status === 204) {
      return null
    }

    if (response.ok) {
      return await response?.json()
    }

    if (response.status === 401) {
      const refreshed = await refreshAccessToken(`${baseUrl}/token`)
      if (refreshed) {
        // Retry the original request with the new token
        mergedOptions.headers.Authorization = `Bearer ${localStorage.getItem('token')}`
        const retryResponse = await fetch(`${baseUrl}${url}`, mergedOptions)
        if (retryResponse.ok) {
          return await retryResponse.json()
        }
      }
      redirectToLogin()
    }

    throw new Error(response.status)
  }

  return tryRequest()
}

async function refreshAccessToken(refreshTokenUrl) {
  try {
    const refreshToken = localStorage.getItem('refresh_token')
    if (!refreshToken) throw new Error('No refresh token available')

    const response = await fetch(refreshTokenUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${refreshToken}`,
      },
      credentials: 'include',
    })

    if (!response.ok) {
      throw new Error('Failed to refresh access token')
    }

    const data = await response.json()
    localStorage.setItem('token', data.access_token)
    return true
  } catch (error) {
    console.error('Refresh token error:', error)
    return false
  }
}

async function fetchFileUpload(url, file = null, obj = {}, options = {}) {
  const baseUrl = import.meta.env.VITE_BASE_URL
  const defaultOptions = {
    headers: {
      Authorization: `Bearer ${localStorage.getItem('token')}`,
    },
  }

  const token = localStorage.getItem('token')
  if (!token) {
    redirectToLogin()
    throw new Error('No authorization token')
  }

  const formData = new FormData()
  if (file) {
    formData.append('file', file)
  }

  if (obj && Object.keys(obj).length > 0) {
    const jsonString = JSON.stringify(obj)
    const blob = new Blob([jsonString], { type: 'application/json' })
    formData.append('bookDetails', blob)
  }

  const mergedOptions = {
    ...defaultOptions,
    method: 'POST',
    ...options,
    body: formData,
  }

  const tryRequest = async () => {
    const response = await fetch(`${baseUrl}${url}`, mergedOptions)

    if (response.ok) {
      return await response.json()
    }

    if (response.status === 401) {
      const refreshed = await refreshAccessToken(`${baseUrl}/token`)
      if (refreshed) {
        mergedOptions.headers.Authorization = `Bearer ${localStorage.getItem('token')}`
        const retryResponse = await fetch(`${baseUrl}${url}`, mergedOptions)
        if (retryResponse.ok) {
          return await retryResponse.json()
        }
      }
      redirectToLogin()
    }

    throw new Error(response.status)
  }

  return tryRequest()
}

function redirectToLogin() {
  localStorage.clear()
  window.location.href = '/login'
}

async function fetchGet(url, options = {}) {
  return fetchApi(url, { ...options, method: 'GET' })
}

async function fetchPost(url, body, options = {}) {
  return fetchApi(url, { ...options, method: 'POST', body: JSON.stringify(body) })
}

async function fetchPut(url, body, options = {}) {
  return fetchApi(url, { ...options, method: 'PUT', body: JSON.stringify(body) })
}

async function fetchDelete(url, options = {}) {
  return fetchApi(url, { ...options, method: 'DELETE' })
}

export { fetchApi, fetchGet, fetchDelete, fetchPut, fetchPost, fetchFileUpload }
