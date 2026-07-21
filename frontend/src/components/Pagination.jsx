export default function Pagination({ page, totalPages, onPageChange }) {
    if (totalPages <= 1) return null

    return (
        <div className="flex items-center justify-center gap-3 py-2">
            <button
                onClick={() => onPageChange(page - 1)}
                disabled={page === 0}
                className="px-3 py-1 border rounded disabled:opacity-40 disabled:cursor-not-allowed"
            >
                Previous
            </button>
            <span className="text-sm text-gray-600">
                Page {page + 1} of {totalPages}
            </span>
            <button
                onClick={() => onPageChange(page + 1)}
                disabled={page >= totalPages - 1}
                className="px-3 py-1 border rounded disabled:opacity-40 disabled:cursor-not-allowed"
            >
                Next
            </button>
        </div>
    )
}