interface LoadingProps {
    text?: string;
    size?: "sm" | "md";
}

export function Loading({
    text = "Cargando...",
    size = "md",
}: LoadingProps) {
    const spinnerSize = size === "sm" ? "h-4 w-4" : "h-8 w-8";
    const padding = size === "sm" ? "py-3" : "py-10";

    return (
        <div
            className={`flex items-center justify-center gap-2 ${padding}`}
        >
            <div
                className={`${spinnerSize} animate-spin rounded-full border-2 border-slate-700 border-t-cyan-400`}
            />

            <span className="text-sm text-slate-400">
                {text}
            </span>
        </div>
    );
}