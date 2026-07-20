import { useState } from 'react'

const STATUS_OPTIONS = ['APPLIED', 'OA', 'INTERVIEW', 'OFFER', 'REJECTED']

// Used for both "add new" and "edit" — if `existing` is passed, the form pre-fills for editing.
export default function ApplicationForm({ existing, onSubmit, onCancel }) {
  const [company, setCompany] = useState(existing?.company || '')
  const [role, setRole] = useState(existing?.role || '')
  const [status, setStatus] = useState(existing?.status || 'APPLIED')
  const [appliedDate, setAppliedDate] = useState(existing?.appliedDate || '')
  const [notes, setNotes] = useState(existing?.notes || '')

  function handleSubmit(e) {
    e.preventDefault()
    onSubmit({ company, role, status, appliedDate: appliedDate || null, notes })
  }

  return (
    <form onSubmit={handleSubmit} className="bg-white p-4 rounded-lg shadow space-y-3">
      <div className="grid grid-cols-2 gap-3">
        <input
          type="text"
          placeholder="Company"
          value={company}
          onChange={(e) => setCompany(e.target.value)}
          className="border rounded px-3 py-2"
          required
        />
        <input
          type="text"
          placeholder="Role"
          value={role}
          onChange={(e) => setRole(e.target.value)}
          className="border rounded px-3 py-2"
          required
        />
        <select
          value={status}
          onChange={(e) => setStatus(e.target.value)}
          className="border rounded px-3 py-2"
        >
          {STATUS_OPTIONS.map((s) => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>
        <input
          type="date"
          value={appliedDate || ''}
          onChange={(e) => setAppliedDate(e.target.value)}
          className="border rounded px-3 py-2"
        />
      </div>

      <textarea
        placeholder="Notes (optional)"
        value={notes}
        onChange={(e) => setNotes(e.target.value)}
        className="w-full border rounded px-3 py-2"
        rows={2}
      />

      <div className="flex gap-2">
        <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
          {existing ? 'Save changes' : 'Add application'}
        </button>
        {onCancel && (
          <button type="button" onClick={onCancel} className="px-4 py-2 rounded border">
            Cancel
          </button>
        )}
      </div>
    </form>
  )
}
