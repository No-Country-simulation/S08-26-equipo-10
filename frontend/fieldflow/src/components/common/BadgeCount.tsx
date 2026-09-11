
function BadgeCount({ count }: { count: number }) {
    return (
        <span className="flex h-5 min-w-5 items-center justify-center rounded-full bg-blue-600 px-1.5 text-[10px] font-semibold text-white">
            {count}
        </span>
    )
}

export default BadgeCount