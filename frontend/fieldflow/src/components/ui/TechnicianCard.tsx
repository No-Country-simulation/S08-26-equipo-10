import { MapPin, Phone, Mail } from "lucide-react";

export type TecnicoStatus =
    | "Disponible"
    | "En campo"
    | "Descanso"
    | "No disponible";

export interface Tecnico {
    id: string;
    nombre: string;
    codigo: string;
    especialidad: string;
    telefono: string;
    email: string;
    zona: string;
    estado: TecnicoStatus;
    certificaciones: string[];
    otsActivas: number;
    otsFinalizadas: number;
    ordenActual?: {
        codigo: string;
        cliente: string;
    };
}

interface TecnicoCardProps {
    tecnico: Tecnico;
    onVerAgenda?: (tecnico: Tecnico) => void;
}

const statusStyles: Record<TecnicoStatus, string> = {
    Disponible:
        "border-emerald-500/40 bg-emerald-500/10 text-emerald-400",
    "En campo":
        "border-blue-500/40 bg-blue-500/10 text-blue-400",
    Descanso:
        "border-yellow-500/40 bg-yellow-500/10 text-yellow-400",
    "No disponible":
        "border-red-500/40 bg-red-500/10 text-red-400",
};

function getInitials(nombre: string) {
    return nombre
        .split(" ")
        .slice(0, 2)
        .map((word) => word[0])
        .join("")
        .toUpperCase();
}

function TecnicoCard({
    tecnico,
    onVerAgenda,
}: TecnicoCardProps) {
    return (
        <article className="overflow-hidden rounded-xl border border-slate-700 bg-[#131a22]">
            {/* Header */}
            <div className="flex items-start justify-between gap-4 p-6">
                <div className="flex min-w-0 gap-5">
                    {/* Avatar */}
                    <div className="flex h-14 w-14 shrink-0 items-center justify-center rounded-full bg-blue-900/70 text-lg font-semibold text-blue-400">
                        {getInitials(tecnico.nombre)}
                    </div>

                    {/* Información */}
                    <div className="min-w-0">
                        <div className="flex flex-wrap items-center gap-2">
                            <h3 className="text-lg font-semibold text-white">
                                {tecnico.nombre}
                            </h3>

                            <span className="font-mono text-sm text-slate-500">
                                {tecnico.codigo}
                            </span>
                        </div>

                        <p className="mt-1 text-base text-blue-400">
                            {tecnico.especialidad}
                        </p>

                        <div className="mt-3 flex flex-wrap items-center gap-3">
                            <span
                                className={`rounded border px-2.5 py-1 font-mono text-xs ${statusStyles[tecnico.estado]}`}
                            >
                                {tecnico.estado}
                            </span>

                            <span className="flex items-center gap-1 text-sm text-slate-500">
                                <MapPin size={14} />
                                {tecnico.zona}
                            </span>
                        </div>
                    </div>
                </div>

                {/* Contacto */}
                <div className="hidden text-right md:block">
                    <p className="flex items-center justify-end gap-2 text-sm text-slate-400">
                        <Phone size={14} />
                        {tecnico.telefono}
                    </p>

                    <p className="mt-1 flex items-center justify-end gap-2 text-sm text-slate-500">
                        <Mail size={14} />
                        {tecnico.email}
                    </p>
                </div>
            </div>

            {/* Orden actual */}
            {tecnico.ordenActual && (
                <div className="border-y border-slate-700 bg-[#182231] px-6 py-4">
                    <div className="mb-1 text-sm text-slate-400">
                        En ejecución
                    </div>

                    <div className="flex flex-wrap items-center justify-between gap-3">
                        <div className="flex items-center gap-3">
                            <span className="h-2 w-2 rounded-full bg-violet-400" />

                            <span className="font-mono font-semibold text-white">
                                {tecnico.ordenActual.codigo}
                            </span>

                            <span className="text-white">•</span>

                            <span className="font-mono font-semibold text-white">
                                {tecnico.ordenActual.cliente}
                            </span>
                        </div>

                        <button className="text-sm text-cyan-400 transition hover:text-cyan-300">
                            Ver OT →
                        </button>
                    </div>
                </div>
            )}

            {/* Certificaciones */}
            <div className="px-6 py-4">
                <p className="mb-2 text-sm text-slate-400">
                    Certificaciones
                </p>

                <div className="flex flex-wrap gap-2">
                    {tecnico.certificaciones.length > 0 ? (
                        tecnico.certificaciones.map((certificacion) => (
                            <span
                                key={certificacion}
                                className="rounded bg-slate-800 px-2.5 py-1 text-sm text-slate-400"
                            >
                                {certificacion}
                            </span>
                        ))
                    ) : (
                        <span className="text-sm text-slate-600">
                            Sin certificaciones
                        </span>
                    )}
                </div>
            </div>

            {/* Footer */}
            <div className="flex items-center justify-between border-t border-slate-700 px-6 py-4">
                <div className="flex gap-5 text-sm">
                    <span className="text-blue-400">
                        {tecnico.otsActivas} OTs activas
                    </span>

                    <span className="text-blue-400">
                        {tecnico.otsFinalizadas} finalizadas
                    </span>
                </div>

                <button
                    onClick={() => onVerAgenda?.(tecnico)}
                    className="text-sm text-cyan-400 transition hover:text-cyan-300"
                >
                    Ver agenda
                </button>
            </div>
        </article>
    );
}

export default TecnicoCard;