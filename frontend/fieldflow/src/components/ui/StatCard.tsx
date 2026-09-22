import type { LucideIcon } from "lucide-react";

interface StatCardProps {
    title: string;
    value: number | string;
    icon: LucideIcon;
    valueColor?: string;
    iconColor?: string;
    borderColor?: string;
    description?: string;
}

export function StatCard({
    title,
    value,
    icon: Icon,
    valueColor = "text-white",
    iconColor = "text-white",
    borderColor = "border-border",
    description,
}: StatCardProps) {
    return (
        <div
            className={`rounded-lg border ${borderColor} bg-card p-5`}
        >
            <div className="flex items-start justify-between">
                <span className="text-sm font-medium uppercase tracking-wide text-muted-foreground">
                    {title}
                </span>

                <Icon className={`h-5 w-5 ${iconColor}`} />
            </div>

            <p className={`mt-4 text-4xl font-semibold ${valueColor}`}>
                {value}
            </p>

            {description && (
                <p className="mt-2 text-sm text-muted-foreground">
                    {description}
                </p>
            )}
        </div>
    );
}