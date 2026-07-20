const STATUS_COLORS = {
  APPLIED: 'bg-gray-200 text-gray-800',
  OA: 'bg-yellow-100 text-yellow-800',
  INTERVIEW: 'bg-blue-100 text-blue-800',
  OFFER: 'bg-green-100 text-green-800',
  REJECTED: 'bg-red-100 text-red-800',
}

export default function ApplicationList({ applications, onEdit, onDelete }) {
  if (applications.length === 0) {
    return <p className="text-gray-500 text-center py-8">No applications yet — add your first one above.</p>
  }

  return (
    <div className="space-y-2">
      {applications.map((app) => (
        <div key={app.id} className="bg-white p-4 rounded-lg shadow flex items-center justify-between">
          <div>
            <p className="font-semibold text-gray-800">{app.company} — {app.role}</p>
            <p className="text-sm text-gray-500">
              {app.appliedDate ? `Applied ${app.appliedDate}` : 'No date set'}
              {app.notes ? ` · ${app.notes}` : ''}
            </p>
          </div>
          <div className="flex items-center gap-2">
            <span className={`text-xs font-medium px-2 py-1 rounded ${STATUS_COLORS[app.status]}`}>
              {app.status}
            </span>
            <button onClick={() => onEdit(app)} className="text-sm text-blue-600 hover:underline">Edit</button>
            <button onClick={() => onDelete(app.id)} className="text-sm text-red-600 hover:underline">Delete</button>
          </div>
        </div>
      ))}
    </div>
  )
}
