async function fetchApi(url, options = {}) {
  const baseUrl = import.meta.env.VITE_BASE_URL
  const token = localStorage.getItem('token')
  const defaultOptions = {
    headers: {
      'Content-Type': 'application/json',
    },
  }

  const mergedOptions = { ...defaultOptions, ...options }

  const response = await fetch(`${baseUrl}${url}`, mergedOptions)

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`)
  }

  return await response.json()
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
export { fetchApi, fetchGet, fetchDelete, fetchPut, fetchPost }
