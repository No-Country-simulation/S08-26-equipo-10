import { useState } from "react";
import { Modal } from "@/components/common/modal";
import { Button } from "@/components/common/Button";

type Props = {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (name: string) => void;
};

export function CreateClientModal({
    isOpen,
    onClose,
    onSubmit,
}: Props) {
    const [name, setName] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const trimmedName = name.trim();

        if (!trimmedName) return;

        onSubmit(trimmedName);
        setName("");
        onClose();
    };

    return (
        <Modal
            isOpen={isOpen}
            onClose={onClose}
            title="Nuevo cliente"
        >
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <label
                        htmlFor="client-name"
                        className="mb-2 block text-sm font-medium text-slate-300"
                    >
                        Nombre del cliente
                    </label>

                    <input
                        id="client-name"
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        placeholder="Ej. Metalúrgica del Norte S.A."
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
                    <Button variant="ghost" onClick={onClose}>
                        cancelar
                    </Button>

                    <Button type="submit" variant="primary" disabled={!name.trim()}>
                        Crear cliente
                    </Button>
                </div>
            </form>
        </Modal>
    );
}