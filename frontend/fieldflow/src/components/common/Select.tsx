import type { SelectHTMLAttributes } from "react";

interface SelectOption {
    label: string;
    value: string;
}

interface SelectProps
    extends SelectHTMLAttributes<HTMLSelectElement> {
    label?: string;
    options: SelectOption[];
}

export function Select({
    label,
    options,
    ...props
}: SelectProps) {
    return (
        <div className="flex flex-col gap-2">
            {label && (
                <label className="text-sm font-medium text-slate-300">
                    {label}
                </label>
            )}

            <select
                className="rounded-md border border-slate-700 bg-slate-900 px-3 py-2 text-sm text-white outline-none focus:border-cyan-500"
                {...props}
            >
                <option value="">Seleccionar...</option>

                {options.map((option) => (
                    <option key={option.value} value={option.value}>
                        {option.label}
                    </option>
                ))}
            </select>
        </div>
    );
}