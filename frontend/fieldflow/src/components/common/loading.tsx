interface LoadingProps {
    text?: string;
}

export function Loading({
    text = "Cargando...",
}: LoadingProps) {
    return (
        <div className="flex flex-col items-center justify-center gap-3 py-10">
            <div className="h-8 w-8 animate-spin rounded-full border-2 border-slate-700 border-t-cyan-400" />

            <span className="text-sm text-slate-400">
                {text}
            </span>
        </div>
    );
}