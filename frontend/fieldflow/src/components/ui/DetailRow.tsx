interface DetailRowProps {
    label: string;
    value?: React.ReactNode;
}

export function DetailRow({
    label,
    value = "—",
}: DetailRowProps) {
    return (
        <div className="grid grid-cols-[150px_1fr] gap-4 py-1.5">
            <span className="text-slate-400">
                {label}
            </span>

            <span className="font-medium text-white">
                {value}
            </span>
        </div>
    );
}