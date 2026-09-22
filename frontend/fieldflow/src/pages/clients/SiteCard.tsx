import { ChevronRight, MapPin } from "lucide-react";
import type { Site } from "@/types/client";

type Props = {
    site: Site;
};

export function SiteCard({ site }: Props) {
    return (
        <div className="border-t border-[#293545] p-5">
            <div>
                <h3 className="font-semibold text-white">
                    {site.name}
                </h3>

                <p className="mt-2 flex items-center gap-1.5 text-sm text-[#78a0d2]">
                    <MapPin size={14} />
                    {site.address}
                </p>
            </div>

            <div className="mt-5">
                <div className="mb-2 flex items-center justify-between">
                    <p className="text-sm font-medium text-[#8eafd8]">
                        Instalaciones
                    </p>

                    <span className="text-xs text-[#6387b6]">
                        {site.installations.length}
                    </span>
                </div>

                <div className="space-y-2">
                    {site.installations.map((installation) => (
                        <button
                            key={installation.id}
                            type="button"
                            className="
                flex w-full items-center justify-between
                rounded-md border border-[#293545]
                bg-[#18222e] px-3 py-2.5
                text-left text-sm text-[#d7e3f4]
                transition-colors
                hover:border-[#3b4f68]
                hover:bg-[#1d2a39]
              "
                        >
                            <span>{installation.name}</span>

                            <ChevronRight
                                size={16}
                                className="text-[#6387b6]"
                            />
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
}