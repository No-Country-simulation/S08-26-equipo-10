interface ErrorMessageProps {
    message?: string;
    onRetry?: () => void;
}

export function ErrorMessage({
    message = "Ocurrió un error inesperado.",
    onRetry,
}: ErrorMessageProps) {
    return (
        <div className="rounded-lg border border-red-500/30 bg-red-500/10 p-4">
            <p className="text-sm text-red-400">
                {message}
            </p>

            {onRetry && (
                <button
                    onClick={onRetry}
                    className="mt-3 text-sm font-medium text-red-300 hover:text-red-200"
                >
                    Intentar nuevamente
                </button>
            )}
        </div>
    );
}