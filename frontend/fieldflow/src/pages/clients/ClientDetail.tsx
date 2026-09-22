import type { Client } from "@/types/client";
import { SiteCard } from "@/pages/clients/SiteCard";

type Props = {
    client: Client;
    onAddSite: () => void;
};

export function ClientDetail({ client, onAddSite }: Props) {
    const installationCount = client.sites.reduce(
        (total, site) => total + site.installations.length,
        0
    );

    return (
        <div className="space-y-5">
            {/* Información del cliente */}
            <section className="rounded-lg border border-[#293545] bg-[#121922] p-6">
                <div className="flex items-start justify-between gap-4">
                    <div>
                        <h2 className="text-xl font-semibold text-white">
                            {client.name}
                        </h2>

                        <p className="mt-2 text-sm text-[#78a0d2]">
                            {client.sites.length}{" "}
                            {client.sites.length === 1 ? "sede" : "sedes"}
                            {" · "}
                            {installationCount}{" "}
                            {installationCount === 1
                                ? "instalación"
                                : "instalaciones"}
                        </p>
                    </div>

                    <button
                        type="button"
                        className="
              text-sm font-medium text-cyan-400
              hover:text-cyan-300 cursor-pointer
            "
                    >
                        Editar
                    </button>
                </div>
            </section>

            {/* Sedes */}
            <section className="overflow-hidden rounded-lg border border-[#293545] bg-[#121922]">
                <div className="flex items-center justify-between border-b border-[#293545] px-5 py-4">
                    <h2 className="font-semibold text-white">
                        Sedes
                    </h2>

                    <button
                        onClick={onAddSite}
                        type="button"
                        className="text-sm font-medium text-cyan-400 hover:text-cyan-300 cursor-pointer"
                    >
                        + Agregar sede
                    </button>
                </div>

                {client.sites.map((site) => (
                    <SiteCard
                        key={site.id}
                        site={site}
                    />
                ))}
            </section>
        </div>
    );
}