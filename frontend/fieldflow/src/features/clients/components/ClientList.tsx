import { Search } from "lucide-react";
import type { Client } from "@/features/clients/types/client";
import { ClientListItem } from "@/features/clients/components/ClientListItem";
import { useMemo, useState } from "react";

type Props = {
    clients: Client[];
    selectedClientId: string | null;
    onSelectClient: (id: string) => void;
};

export function ClientList({
    clients,
    selectedClientId,
    onSelectClient,
}: Props) {
    const [search, setSearch] = useState("");
    const filteredClients = useMemo(() => {
        const normalizedSearch = search.toLowerCase().trim();
        return clients.filter((client) =>
            client.name.toLowerCase().includes(normalizedSearch)
        );
    }, [clients, search]);


    return (
        <section className="flex min-h-0 flex-col">
            <div className="mb-4">
                <div className="relative">
                    <Search
                        size={18}
                        className="absolute left-3 top-1/2 -translate-y-1/2 text-[#6387b6]"
                    />

                    <input
                        type="text"
                        placeholder="Buscar cliente..."
                        className="
              w-full rounded-lg border border-[#293545]
              bg-[#121922] py-2.5 pl-10 pr-3
              text-sm text-white outline-none
              placeholder:text-[#526b8c]
              focus:border-blue-500
            "
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                    />
                </div>
            </div>

            <div className="space-y-2 overflow-y-auto pr-1">
                {filteredClients.map((client) => (
                    <ClientListItem
                        key={client.id}
                        client={client}
                        selected={client.id === selectedClientId}
                        onClick={() => onSelectClient(client.id)}
                    />
                ))}
            </div>
        </section>
    );
}