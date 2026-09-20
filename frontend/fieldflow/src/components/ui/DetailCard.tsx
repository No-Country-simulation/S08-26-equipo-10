interface DetailCardProps {
    title: string;
    children: React.ReactNode;
}

export function DetailCard({
    title,
    children,
}: DetailCardProps) {
    return (
        <div className="rounded-lg border border-slate-700 bg-slate-900/70 p-5">
            <h2 className="mb-4 border-b border-slate-700 pb-3 text-base font-semibold text-white">
                {title}
            </h2>

            {children}
        </div>
    );
}