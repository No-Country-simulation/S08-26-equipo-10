import { useState } from "react";
import { ClientDetail } from "@/features/clients/components/ClientDetail";
import { ClientList } from "@/features/clients/components/ClientList";
import { clients } from "@/features/clients/services/clients.mock";
import { CreateClientModal } from "@/features/clients/components/CreateClientModal";
import { CreateSiteModal } from "@/features/clients/components/createSiteModal";

export default function ClientesPage() {
    const [selectedClientId, setSelectedClientId] = useState<string>(
        clients[0]?.id ?? ""
    );

    const selectedClient = clients.find(
        (client) => client.id === selectedClientId
    );

    const [isCreateClientOpen, setIsCreateClientOpen] = useState(false);
    const [isCreateSiteOpen, setIsCreateSiteOpen] = useState(false);

    const handleCreateClient = (name: string) => {
        console.log("Creating client:", name);
    }

    const handleCreateSite = (name: string, address: string) => {
        console.log("Creating site:", name, address);
    }

    return (
        <div className="h-full min-h-0 p-6">
            <header className="mb-6 flex items-start justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-semibold text-white">
                        Clientes
                    </h1>

                    <p className="mt-1 text-sm text-[#78a0d2]">
                        {clients.length} clientes registrados
                    </p>
                </div>

                <button
                    onClick={() => setIsCreateClientOpen(true)}
                    type="button"
                    className="
            rounded-lg bg-blue-600
            px-5 py-2.5
            text-sm font-semibold text-white
            hover:bg-blue-500 cursor-pointer
          "
                >
                    + Nuevo Cliente
                </button>
            </header>

            <div
                className="
          grid min-h-0
          grid-cols-1 gap-6
          lg:grid-cols-[minmax(300px,0.65fr)_minmax(0,1.35fr)]
        "
            >
                <ClientList
                    clients={clients}
                    selectedClientId={selectedClientId}
                    onSelectClient={setSelectedClientId}
                />

                <div className="min-w-0">
                    {selectedClient ? (
                        <ClientDetail client={selectedClient} onAddSite={() => setIsCreateSiteOpen(true)} />
                    ) : (
                        <div className="flex min-h-80 items-center justify-center rounded-lg border border-[#293545] bg-[#121922] text-[#6387b6]">
                            Selecciona un cliente
                        </div>
                    )}
                </div>
            </div>

            <CreateClientModal
                isOpen={isCreateClientOpen}
                onClose={() => setIsCreateClientOpen(false)}
                onSubmit={handleCreateClient}
            />

            <CreateSiteModal
                isOpen={isCreateSiteOpen}
                onClose={() => setIsCreateSiteOpen(false)}
                onSubmit={handleCreateSite}
            />
        </div>
    );
}