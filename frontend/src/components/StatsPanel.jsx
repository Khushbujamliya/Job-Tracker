import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer, Legend } from 'recharts'

const COLORS = {
  APPLIED: '#9ca3af',
  OA: '#facc15',
  INTERVIEW: '#60a5fa',
  OFFER: '#4ade80',
  REJECTED: '#f87171',
}

export default function StatsPanel({ applications }) {
  const total = applications.length

  const counts = applications.reduce((acc, app) => {
    acc[app.status] = (acc[app.status] || 0) + 1
    return acc
  }, {})

  const data = Object.entries(counts).map(([status, count]) => ({ name: status, value: count }))

  const responseCount = applications.filter((a) => a.status !== 'APPLIED').length
  const responseRate = total > 0 ? Math.round((responseCount / total) * 100) : 0

  return (
    <div className="bg-white p-4 rounded-lg shadow">
      <div className="flex items-center justify-between mb-2">
        <h2 className="font-semibold text-gray-800">Stats</h2>
        <p className="text-sm text-gray-500">{total} total · {responseRate}% response rate</p>
      </div>

      {total > 0 ? (
        <ResponsiveContainer width="100%" height={220}>
          <PieChart>
            <Pie data={data} dataKey="value" nameKey="name" outerRadius={80} label>
              {data.map((entry) => (
                <Cell key={entry.name} fill={COLORS[entry.name]} />
              ))}
            </Pie>
            <Tooltip />
            <Legend />
          </PieChart>
        </ResponsiveContainer>
      ) : (
        <p className="text-gray-500 text-sm">Add applications to see stats.</p>
      )}
    </div>
  )
}
