import { useState } from "react";
import { Modal } from "@/components/common/modal";

type Props = {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (name: string, address: string) => void;
};

export function CreateSiteModal({
    isOpen,
    onClose,
    onSubmit,
}: Props) {
    const [name, setName] = useState("");
    const [address, setAddress] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const trimmedName = name.trim();
        const trimmedAddress = address.trim();

        if (!trimmedName || !trimmedAddress) return;

        onSubmit(trimmedName, trimmedAddress);

        setName("");
        setAddress("");
        onClose();
    };

    return (
        <Modal
            isOpen={isOpen}
            onClose={onClose}
            title="Nueva sede"
        >
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label
                        htmlFor="site-name"
                        className="mb-2 block text-sm font-medium text-slate-300"
                    >
                        Nombre de la sede
                    </label>

                    <input
                        id="site-name"
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Ej. Planta Principal"
                        className="
                            w-full rounded-md border border-slate-700
                            bg-slate-800 px-3 py-2.5
                            text-sm text-white
                            outline-none
                            placeholder:text-slate-500
                            focus:border-blue-500
                        "
                    />
                </div>

                <div>
                    <label
                        htmlFor="site-address"
                        className="mb-2 block text-sm font-medium text-slate-300"
                    >
                        Dirección
                    </label>

                    <input
                        id="site-address"
                        type="text"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                        placeholder="Ej. Av. Industrial 2450"
                        className="
                            w-full rounded-md border border-slate-700
                            bg-slate-800 px-3 py-2.5
                            text-sm text-white
                            outline-none
                            placeholder:text-slate-500
                            focus:border-blue-500
                        "
                    />
                </div>

                <div className="flex justify-end gap-3 pt-2">
                    <button
                        type="button"
                        onClick={onClose}
                        className="
                            rounded-md px-4 py-2
                            text-sm font-medium
                            text-slate-300
                            hover:bg-slate-800
                        "
                    >
                        Cancelar
                    </button>

                    <button
                        type="submit"
                        disabled={!name.trim() || !address.trim()}
                        className="
                            rounded-md bg-blue-600
                            px-4 py-2
                            text-sm font-medium text-white
                            hover:bg-blue-500
                            disabled:cursor-not-allowed
                            disabled:opacity-50
                        "
                    >
                        Crear sede
                    </button>
                </div>
            </form>
        </Modal>
    );
}