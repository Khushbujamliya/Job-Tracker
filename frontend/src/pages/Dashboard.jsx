import { useEffect, useState } from 'react'
import api, { fetchApplications, downloadCsv } from '../api/axios'
import { useAuth } from '../context/AuthContext'
import ApplicationForm from '../components/ApplicationForm'
import ApplicationList from '../components/ApplicationList'
import StatsPanel from '../components/StatsPanel'
import FilterBar from '../components/FilterBar'
import Pagination from '../components/Pagination'

export default function Dashboard() {
  const { user, logout } = useAuth()

  const [applications, setApplications] = useState([])
  const [totalPages, setTotalPages] = useState(0)
  const [page, setPage] = useState(0)

  const [status, setStatus] = useState('')
  const [company, setCompany] = useState('')

  const [editing, setEditing] = useState(null)
  const [showForm, setShowForm] = useState(false)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const timeout = setTimeout(() => {
      loadApplications()
    }, 300)
    return () => clearTimeout(timeout)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, status, company])

  async function loadApplications() {
    setLoading(true)
    try {
      const data = await fetchApplications({ status, company, page, size: 10 })
      setApplications(data.content)
      setTotalPages(data.totalPages)
    } catch (err) {
      console.error('Failed to load applications', err)
    } finally {
      setLoading(false)
    }
  }

  function handleStatusChange(value) {
    setStatus(value)
    setPage(0)
  }

  function handleCompanyChange(value) {
    setCompany(value)
    setPage(0)
  }

  async function handleAddOrUpdate(data) {
    if (editing) {
      await api.put(`/applications/${editing.id}`, data)
      setEditing(null)
    } else {
      await api.post('/applications', data)
    }
    setShowForm(false)
    loadApplications()
  }

  async function handleDelete(id) {
    await api.delete(`/applications/${id}`)
    loadApplications()
  }

  function handleEdit(app) {
    setEditing(app)
    setShowForm(true)
  }

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-3xl mx-auto space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-800">Job Application Tracker</h1>
            <p className="text-gray-500 text-sm">Welcome, {user?.name}</p>
          </div>
          <button onClick={logout} className="text-sm text-gray-600 hover:underline">Log out</button>
        </div>

        <StatsPanel applications={applications} />

        <FilterBar
          status={status}
          company={company}
          onStatusChange={handleStatusChange}
          onCompanyChange={handleCompanyChange}
          onExport={downloadCsv}
        />

        {showForm ? (
          <ApplicationForm
            existing={editing}
            onSubmit={handleAddOrUpdate}
            onCancel={() => { setShowForm(false); setEditing(null) }}
          />
        ) : (
          <button
            onClick={() => setShowForm(true)}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            + Add application
          </button>
        )}

        {loading ? (
          <p className="text-gray-500">Loading...</p>
        ) : (
          <>
            <ApplicationList applications={applications} onEdit={handleEdit} onDelete={handleDelete} />
            <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
          </>
        )}
      </div>
    </div>
  )
}