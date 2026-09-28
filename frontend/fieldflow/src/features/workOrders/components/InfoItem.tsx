interface InfoItemProps {
    label: string;
    value: string;
}

function InfoItem({ label, value }: InfoItemProps) {
    return (
        <div className="rounded-md border border-slate-800 bg-slate-950/60 p-3">
            <p className="text-xs uppercase tracking-wide text-slate-500">
                {label}
            </p>

            <p className="mt-1 text-sm font-medium text-white">
                {value}
            </p>
        </div>
    );
}

export default InfoItem;