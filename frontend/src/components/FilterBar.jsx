const STATUS_OPTIONS = ['', 'APPLIED', 'OA', 'INTERVIEW', 'OFFER', 'REJECTED']

export default function FilterBar({ status, company, onStatusChange, onCompanyChange, onExport }) {
    return (
        <div className="bg-white p-4 rounded-lg shadow flex flex-wrap items-center gap-3">
            <input
                type="text"
                placeholder="Search by company..."
                value={company}
                onChange={(e) => onCompanyChange(e.target.value)}
                className="border rounded px-3 py-2 flex-1 min-w-[180px]"
            />

            <select
                value={status}
                onChange={(e) => onStatusChange(e.target.value)}
                className="border rounded px-3 py-2"
            >
                {STATUS_OPTIONS.map((s) => (
                    <option key={s} value={s}>{s === '' ? 'All statuses' : s}</option>
                ))}
            </select>

            <button
                onClick={onExport}
                className="ml-auto text-sm border rounded px-3 py-2 hover:bg-gray-50"
            >
                Export CSV
            </button>
        </div>
    )
}