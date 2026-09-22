import type { ReactNode } from "react";

interface EmptyStateProps {
    title: string;
    description?: string;
    action?: ReactNode;
}

export function EmptyState({
    title,
    description,
    action,
}: EmptyStateProps) {
    return (
        <div className="flex flex-col items-center justify-center rounded-lg border border-dashed border-slate-700 py-12 text-center">
            <h3 className="text-base font-semibold text-slate-300">
                {title}
            </h3>

            {description && (
                <p className="mt-2 max-w-md text-sm text-slate-500">
                    {description}
                </p>
            )}

            {action && (
                <div className="mt-4">
                    {action}
                </div>
            )}
        </div>
    );
}