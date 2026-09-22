import type { Client } from "@/types/client";

type Props = {
    client: Client;
    selected: boolean;
    onClick: () => void;
};

export function ClientListItem({
    client,
    selected,
    onClick,
}: Props) {
    const installationCount = client.sites.reduce(
        (total, site) => total + site.installations.length,
        0
    );


    return (
        <button
            type="button"
            onClick={onClick}
            className={`
        w-full rounded-lg border p-4 text-left transition-colors cursor-pointer
        ${selected
                    ? "border-blue-500 bg-[#151d27]"
                    : "border-[#293545] bg-[#121922] hover:border-[#3b4b60]"
                }
      `}
        >
            <h3 className="truncate font-semibold text-white">
                {client.name}
            </h3>

            <div className="mt-3 flex gap-5 text-sm text-[#6387b6]">
                <span>
                    {client.sites.length}{" "}
                    {client.sites.length === 1 ? "sede" : "sedes"}
                </span>

                <span>
                    {installationCount}{" "}
                    {installationCount === 1
                        ? "instalación"
                        : "instalaciones"}
                </span>
            </div>
        </button>
    );
}