import type { ReactNode } from "react";
import { X } from "lucide-react";

interface ModalProps {
    isOpen: boolean;
    onClose: () => void;
    title: string;
    children: ReactNode;
}

export function Modal({
    isOpen,
    onClose,
    title,
    children,
}: ModalProps) {
    if (!isOpen) return null;

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-4">
            <div className="w-full max-w-lg rounded-lg border border-slate-700 bg-slate-900 shadow-xl">
                <div className="flex items-center justify-between border-b border-slate-700 p-4">
                    <h2 className="text-lg font-semibold text-white">
                        {title}
                    </h2>

                    <button
                        onClick={onClose}
                        className="text-slate-400 hover:text-white"
                        aria-label="Cerrar"
                    >
                        <X className="h-5 w-5" />
                    </button>
                </div>

                <div className="p-4">
                    {children}
                </div>
            </div>
        </div>
    );
}