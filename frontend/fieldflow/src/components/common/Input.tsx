import type { InputHTMLAttributes } from "react";

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
    label?: string;
    error?: string;
}

export function Input({
    label,
    error,
    className = "",
    ...props
}: InputProps) {
    return (
        <div className="flex flex-col gap-2">
            {label && (
                <label className="text-sm font-medium text-slate-300">
                    {label}
                </label>
            )}

            <input
                className={`rounded-md border border-slate-700 bg-slate-900 px-3 py-2 text-sm text-white outline-none placeholder:text-slate-500 focus:border-cyan-500 ${className}`}
                {...props}
            />

            {error && (
                <span className="text-sm text-red-400">
                    {error}
                </span>
            )}
        </div>
    );
}