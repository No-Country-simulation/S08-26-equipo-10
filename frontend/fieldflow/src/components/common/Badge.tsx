interface BadgeProps {
    children: React.ReactNode;
    className?: string;
}

export function Badge({ children, className = "" }: BadgeProps) {
    return (
        <span
            className={`
        inline-flex
        items-center
        rounded-md
        border
        px-3
        py-1
        font-mono
        text-sm
        font-semibold
        ${className}
      `}
        >
            {children}
        </span>
    );
}