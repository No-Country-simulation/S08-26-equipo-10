import { useState } from "react";
import TecnicoCard, {
    type Tecnico,
} from "@/components/ui/TechnicianCard";
import TecnicoForm from "@/components/ui/TechnicianForm";
import { Modal } from "@/components/common/modal";

const tecnicosIniciales: Tecnico[] = [
    {
        id: "1",
        nombre: "Carlos Mendoza Ruiz",
        codigo: "TEC-001",
        especialidad: "HVAC & Refrigeración",
        telefono: "+51 921 111 222",
        email: "c.mendoza@fieldflow.pe",
        zona: "Lima Norte",
        estado: "En campo",
        certificaciones: [
            "ASHRAE",
            "F-Gas",
            "Seguridad en alturas",
        ],
        otsActivas: 1,
        otsFinalizadas: 0,
        ordenActual: {
            codigo: "OT-2025-0041",
            cliente: "Metalúrgica del Norte S.A.",
        },
    },
    {
        id: "2",
        nombre: "Laura Quispe Cárdenas",
        codigo: "TEC-002",
        especialidad: "Electricidad Industrial",
        telefono: "+51 921 222 333",
        email: "l.quispe@fieldflow.pe",
        zona: "Lima Centro",
        estado: "Disponible",
        certificaciones: [
            "RETIE",
            "IE-2020",
            "PLC Siemens",
        ],
        otsActivas: 0,
        otsFinalizadas: 0,
    },
    {
        id: "3",
        nombre: "Diego Flores Mamani",
        codigo: "TEC-003",
        especialidad: "Mecánica Industrial",
        telefono: "+51 921 333 444",
        email: "d.flores@fieldflow.pe",
        zona: "Callao",
        estado: "En campo",
        certificaciones: [
            "Soldadura",
            "Mantenimiento industrial",
        ],
        otsActivas: 2,
        otsFinalizadas: 4,
        ordenActual: {
            codigo: "OT-2025-0042",
            cliente: "Industrias del Pacífico",
        },
    },
    {
        id: "4",
        nombre: "Ana Torres Valdivia",
        codigo: "TEC-004",
        especialidad: "Refrigeración Comercial",
        telefono: "+51 921 444 555",
        email: "a.torres@fieldflow.pe",
        zona: "Lima Sur",
        estado: "Disponible",
        certificaciones: [
            "F-Gas",
            "ASHRAE",
        ],
        otsActivas: 0,
        otsFinalizadas: 3,
    },
    {
        id: "5",
        nombre: "Luis Ramírez Soto",
        codigo: "TEC-005",
        especialidad: "Electricidad Industrial",
        telefono: "+51 921 555 666",
        email: "l.ramirez@fieldflow.pe",
        zona: "Lima Este",
        estado: "Disponible",
        certificaciones: [
            "RETIE",
            "PLC Siemens",
        ],
        otsActivas: 0,
        otsFinalizadas: 7,
    },
    {
        id: "6",
        nombre: "Pedro Castillo Vega",
        codigo: "TEC-006",
        especialidad: "Mecánica Industrial",
        telefono: "+51 921 666 777",
        email: "p.castillo@fieldflow.pe",
        zona: "Lima Centro",
        estado: "Descanso",
        certificaciones: [
            "Soldadura",
        ],
        otsActivas: 0,
        otsFinalizadas: 5,
    },
];

function TecnicosPage() {
    const [tecnicos, setTecnicos] =
        useState<Tecnico[]>(tecnicosIniciales);

    const [modalOpen, setModalOpen] = useState(false);

    const disponibles = tecnicos.filter(
        (tecnico) => tecnico.estado === "Disponible"
    ).length;

    const enCampo = tecnicos.filter(
        (tecnico) => tecnico.estado === "En campo"
    ).length;

    const descanso = tecnicos.filter(
        (tecnico) => tecnico.estado === "Descanso"
    ).length;

    const noDisponibles = tecnicos.filter(
        (tecnico) => tecnico.estado === "No disponible"
    ).length;

    const handleCrearTecnico = (
        nuevoTecnico: Omit<Tecnico, "id" | "codigo" | "otsActivas" | "otsFinalizadas">
    ) => {
        const tecnico: Tecnico = {
            ...nuevoTecnico,
            id: crypto.randomUUID(),
            codigo: `TEC-${String(tecnicos.length + 1).padStart(3, "0")}`,
            otsActivas: 0,
            otsFinalizadas: 0,
        };

        setTecnicos((prev) => [...prev, tecnico]);
        setModalOpen(false);
    };

    return (
        <div className="min-h-full bg-[#0b1015] p-6 text-white">
            {/* Header */}
            <div className="mb-8 flex items-start justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-semibold">
                        Técnicos
                    </h1>

                    <p className="mt-1 text-base text-slate-400">
                        {tecnicos.length} técnicos registrados
                    </p>
                </div>

                <button
                    onClick={() => setModalOpen(true)}
                    className="rounded-xl bg-blue-600 px-5 py-3 font-semibold text-white transition hover:bg-blue-500"
                >
                    + Nuevo Técnico
                </button>
            </div>

            {/* Estadísticas */}
            <div className="mb-6 grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
                <StatCard
                    title="DISPONIBLES"
                    value={disponibles}
                    valueClass="text-emerald-400"
                />

                <StatCard
                    title="EN CAMPO"
                    value={enCampo}
                    valueClass="text-blue-400"
                />

                <StatCard
                    title="DESCANSO"
                    value={descanso}
                    valueClass="text-yellow-400"
                />

                <StatCard
                    title="NO DISPONIBLES"
                    value={noDisponibles}
                    valueClass="text-red-400"
                />
            </div>

            {/* Técnicos */}
            <div className="grid grid-cols-1 gap-5 xl:grid-cols-2">
                {tecnicos.map((tecnico) => (
                    <TecnicoCard
                        key={tecnico.id}
                        tecnico={tecnico}
                    />
                ))}
            </div>

            {/* Modal */}
            <Modal
                isOpen={modalOpen}
                onClose={() => setModalOpen(false)}
                title="Nuevo Técnico"
            >
                <TecnicoForm
                    onSubmit={handleCrearTecnico}
                    onCancel={() => setModalOpen(false)}
                />
            </Modal>
        </div>
    );
}

interface StatCardProps {
    title: string;
    value: number;
    valueClass: string;
}

function StatCard({
    title,
    value,
    valueClass,
}: StatCardProps) {
    return (
        <div className="rounded-xl border border-slate-700 bg-[#131a22] px-5 py-4">
            <p className="text-sm tracking-wide text-blue-300">
                {title}
            </p>

            <p className={`mt-2 text-3xl font-semibold ${valueClass}`}>
                {value}
            </p>
        </div>
    );
}

export default TecnicosPage;