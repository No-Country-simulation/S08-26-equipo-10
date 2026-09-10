import type { LucideIcon } from "lucide-react";
import type { ButtonHTMLAttributes } from "react";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: "primary" | "secondary" | "danger" | "ghost";
    icon?: LucideIcon;
    iconPosition?: "left" | "right";
}

export function Button({
    variant = "primary",
    className = "",
    children,
    icon: Icon,
    iconPosition = "left",
    ...props
}: ButtonProps) {
    const variants = {
        primary:
            "bg-blue-600 text-white hover:bg-blue-700",
        secondary:
            "border border-slate-700 bg-slate-800 text-slate-200 hover:bg-slate-700",
        danger:
            "bg-red-500 text-white hover:bg-red-400",
        ghost:
            "text-slate-300 hover:bg-slate-800",
    };

    return (
        <button
            className={`inline-flex items-center justify-center gap-2 rounded-md px-4 py-2 text-sm font-medium transition ${variants[variant]} ${className} cursor-pointer`}
            {...props}
        >
            {Icon && iconPosition === "left" && <Icon className="h-4 w-4 shrink-0" />}
            {children}
            {Icon && iconPosition === "right" && <Icon className="h-4 w-4 shrink-0" />}
        </button>
    );
}