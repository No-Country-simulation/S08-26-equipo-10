import { MapPin } from "lucide-react";
import { TechnicianStatusBadge } from "./TechnicianStatusBadge";


interface TechnicianCardProps {
    initials: string;
    name: string;
    code: string;
    specialty: string;
    phone: string;
    email: string;
    status: "En campo" | "Disponible" | "Ocupado";
    location: string;

    orderStatus?: "En ejecución" | "En camino" | "Asignada";
    orderId?: string;
    clientName?: string;

    certifications?: string[];

    activeOrders: number;
    completedOrders: number;

    onViewOrder?: () => void;
    onViewAgenda?: () => void;
}

export function TechnicianCard({
    initials,
    name,
    code,
    specialty,
    phone,
    email,
    status,
    location,
    orderStatus,
    orderId,
    clientName,
    certifications = [],
    activeOrders,
    completedOrders,
    onViewOrder,
    onViewAgenda,
}: TechnicianCardProps) {
    return (
        <article className="overflow-hidden rounded-lg border border-slate-700 bg-slate-900">
            {/* Información del técnico */}
            <div className="p-4">
                <div className="flex items-start justify-between gap-4">
                    <div className="flex min-w-0 items-start gap-4">
                        {/* Avatar */}
                        <div className="flex h-14 w-14 shrink-0 items-center justify-center rounded-full bg-blue-900 text-xl font-semibold text-blue-400">
                            {initials}
                        </div>

                        {/* Datos */}
                        <div className="min-w-0">
                            <div className="flex flex-wrap items-center gap-2">
                                <h3 className="text-lg font-semibold text-white">
                                    {name}
                                </h3>

                                <span className="font-mono text-sm text-slate-500">
                                    {code}
                                </span>
                            </div>

                            <p className="mt-1 text-slate-400">
                                {specialty}
                            </p>

                            <div className="mt-3 flex flex-wrap items-center gap-3">
                                <TechnicianStatusBadge status={status} />

                                <span className="flex items-center gap-2 text-sm text-slate-500">
                                    <MapPin className="h-4 w-4 text-pink-400" />
                                    {location}
                                </span>
                            </div>
                        </div>
                    </div>

                    {/* Contacto */}
                    <div className="hidden text-right md:block">
                        <p className="text-sm text-slate-300">
                            {phone}
                        </p>

                        <p className="mt-1 text-sm text-slate-500">
                            {email}
                        </p>
                    </div>
                </div>
            </div>

            {/* OT activa */}
            {orderId && clientName && (
                <div className="border-y border-slate-700 bg-slate-800/60 px-4 py-4">
                    <div className="flex items-center justify-between gap-4">
                        <div className="min-w-0">
                            <div className="flex items-center gap-3">
                                <span className="h-2 w-2 rounded-full bg-purple-400" />

                                <span className="text-sm text-slate-400">
                                    {orderStatus}
                                </span>
                            </div>

                            <p className="mt-1 font-mono text-base font-semibold text-white">
                                {orderId}
                                <span className="mx-2 text-slate-500">•</span>
                                {clientName}
                            </p>
                        </div>

                        <button
                            type="button"
                            onClick={onViewOrder}
                            className="shrink-0 text-sm font-medium text-cyan-400 transition-colors hover:text-cyan-300"
                        >
                            Ver OT →
                        </button>
                    </div>
                </div>
            )}

            {/* Certificaciones */}
            {certifications.length > 0 && (
                <div className="border-b border-slate-700 px-4 py-4">
                    <p className="mb-3 text-sm text-blue-400">
                        Certificaciones
                    </p>

                    <div className="flex flex-wrap gap-2">
                        {certifications.map((certification) => (
                            <span
                                key={certification}
                                className="rounded-md border border-slate-700 bg-slate-800 px-3 py-1 text-sm text-slate-400"
                            >
                                {certification}
                            </span>
                        ))}
                    </div>
                </div>
            )}

            {/* Resumen */}
            <div className="flex items-center justify-between px-4 py-4">
                <div className="flex gap-6 text-sm">
                    <span className="text-slate-400">
                        <strong className="font-normal text-slate-300">
                            {activeOrders}
                        </strong>{" "}
                        OTs activas
                    </span>

                    <span className="text-slate-400">
                        <strong className="font-normal text-slate-300">
                            {completedOrders}
                        </strong>{" "}
                        finalizadas
                    </span>
                </div>

                <button
                    type="button"
                    onClick={onViewAgenda}
                    className="text-sm font-medium text-cyan-400 transition-colors hover:text-cyan-300"
                >
                    Ver agenda
                </button>
            </div>
        </article>
    );
}