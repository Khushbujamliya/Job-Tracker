import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api

export async function fetchApplications(filters = {}) {
  const params = {}
  if (filters.status) params.status = filters.status
  if (filters.company) params.company = filters.company
  params.page = filters.page ?? 0
  params.size = filters.size ?? 10

  const res = await api.get('/applications', { params })
  return res.data
}

export async function downloadCsv() {
  const res = await api.get('/applications/export', { responseType: 'blob' })
  const url = window.URL.createObjectURL(new Blob([res.data]))
  const link = document.createElement('a')
  link.href = url
  link.setAttribute('download', 'job-applications.csv')
  document.body.appendChild(link)
  link.click()
  link.remove()
  window.URL.revokeObjectURL(url)
}