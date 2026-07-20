import { useEffect, useState } from 'react'
import api from '../api/axios'
import { useAuth } from '../context/AuthContext'
import ApplicationForm from '../components/ApplicationForm'
import ApplicationList from '../components/ApplicationList'
import StatsPanel from '../components/StatsPanel'

export default function Dashboard() {
  const { user, logout } = useAuth()
  const [applications, setApplications] = useState([])
  const [editing, setEditing] = useState(null) // holds the application being edited, or null
  const [showForm, setShowForm] = useState(false)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    fetchApplications()
  }, [])

  async function fetchApplications() {
    setLoading(true)
    try {
      const res = await api.get('/applications')
      setApplications(res.data)
    } catch (err) {
      console.error('Failed to load applications', err)
    } finally {
      setLoading(false)
    }
  }

  async function handleAddOrUpdate(data) {
    if (editing) {
      const res = await api.put(`/applications/${editing.id}`, data)
      setApplications((prev) => prev.map((a) => (a.id === editing.id ? res.data : a)))
      setEditing(null)
    } else {
      const res = await api.post('/applications', data)
      setApplications((prev) => [...prev, res.data])
    }
    setShowForm(false)
  }

  async function handleDelete(id) {
    await api.delete(`/applications/${id}`)
    setApplications((prev) => prev.filter((a) => a.id !== id))
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
          <ApplicationList applications={applications} onEdit={handleEdit} onDelete={handleDelete} />
        )}
      </div>
    </div>
  )
}
