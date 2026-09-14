import { useForm } from "react-hook-form";
import type { Tecnico } from "@/components/ui/TechnicianCard";

type TecnicoFormData = Omit<
    Tecnico,
    "id" | "codigo" | "otsActivas" | "otsFinalizadas"
>;

interface TecnicoFormProps {
    onSubmit: (data: TecnicoFormData) => void;
    onCancel: () => void;
}

const especialidades = [
    "HVAC & Refrigeración",
    "Electricidad Industrial",
    "Mecánica Industrial",
    "Refrigeración Comercial",
];

const estados = [
    "Disponible",
    "En campo",
    "Descanso",
    "No disponible",
] as const;

function TecnicoForm({
    onSubmit,
    onCancel,
}: TecnicoFormProps) {
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<TecnicoFormData>({
        defaultValues: {
            nombre: "",
            especialidad: "",
            telefono: "",
            email: "",
            zona: "",
            estado: "Disponible",
            certificaciones: [],
        },
    });

    const submit = (data: TecnicoFormData) => {
        onSubmit(data);
    };

    return (
        <form
            onSubmit={handleSubmit(submit)}
            className="space-y-5"
        >
            {/* Nombre */}
            <div>
                <label className="mb-2 block text-sm font-medium text-slate-300">
                    Nombre completo
                </label>

                <input
                    {...register("nombre", {
                        required: "El nombre es obligatorio",
                        minLength: {
                            value: 3,
                            message:
                                "El nombre debe tener al menos 3 caracteres",
                        },
                    })}
                    placeholder="Ej. Carlos Mendoza Ruiz"
                    className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 text-sm text-white outline-none transition placeholder:text-slate-600 focus:border-blue-500"
                />

                {errors.nombre && (
                    <p className="mt-1 text-xs text-red-400">
                        {errors.nombre.message}
                    </p>
                )}
            </div>

            {/* Especialidad */}
            <div>
                <label className="mb-2 block text-sm font-medium text-slate-300">
                    Especialidad
                </label>

                <select
                    {...register("especialidad", {
                        required: "Selecciona una especialidad",
                    })}
                    className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 text-sm text-white outline-none focus:border-blue-500"
                >
                    <option value="">
                        Seleccionar especialidad
                    </option>

                    {especialidades.map((especialidad) => (
                        <option
                            key={especialidad}
                            value={especialidad}
                        >
                            {especialidad}
                        </option>
                    ))}
                </select>

                {errors.especialidad && (
                    <p className="mt-1 text-xs text-red-400">
                        {errors.especialidad.message}
                    </p>
                )}
            </div>

            {/* Teléfono + Email */}
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                <div>
                    <label className="mb-2 block text-sm font-medium text-slate-300">
                        Teléfono
                    </label>

                    <input
                        {...register("telefono", {
                            required: "El teléfono es obligatorio",
                        })}
                        placeholder="+57 300 000 0000"
                        className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 text-sm text-white outline-none placeholder:text-slate-600 focus:border-blue-500"
                    />

                    {errors.telefono && (
                        <p className="mt-1 text-xs text-red-400">
                            {errors.telefono.message}
                        </p>
                    )}
                </div>

                <div>
                    <label className="mb-2 block text-sm font-medium text-slate-300">
                        Correo electrónico
                    </label>

                    <input
                        type="email"
                        {...register("email", {
                            required: "El correo es obligatorio",
                            pattern: {
                                value:
                                    /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                                message:
                                    "Ingresa un correo válido",
                            },
                        })}
                        placeholder="tecnico@fieldflow.com"
                        className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 text-sm text-white outline-none placeholder:text-slate-600 focus:border-blue-500"
                    />

                    {errors.email && (
                        <p className="mt-1 text-xs text-red-400">
                            {errors.email.message}
                        </p>
                    )}
                </div>
            </div>

            {/* Zona + Estado */}
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                <div>
                    <label className="mb-2 block text-sm font-medium text-slate-300">
                        Zona
                    </label>

                    <input
                        {...register("zona", {
                            required: "La zona es obligatoria",
                        })}
                        placeholder="Ej. Valledupar Norte"
                        className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 text-sm text-white outline-none placeholder:text-slate-600 focus:border-blue-500"
                    />

                    {errors.zona && (
                        <p className="mt-1 text-xs text-red-400">
                            {errors.zona.message}
                        </p>
                    )}
                </div>

                <div>
                    <label className="mb-2 block text-sm font-medium text-slate-300">
                        Estado
                    </label>

                    <select
                        {...register("estado")}
                        className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 text-sm text-white outline-none focus:border-blue-500"
                    >
                        {estados.map((estado) => (
                            <option key={estado} value={estado}>
                                {estado}
                            </option>
                        ))}
                    </select>
                </div>
            </div>

            {/* Certificaciones */}
            <div>
                <label className="mb-2 block text-sm font-medium text-slate-300">
                    Certificaciones
                </label>

                <input
                    placeholder="Ej. RETIE, ASHRAE, PLC Siemens"
                    className="w-full rounded-lg border border-slate-700 bg-slate-900 px-4 py-3 text-sm text-white outline-none placeholder:text-slate-600 focus:border-blue-500"
                    onChange={(event) => {
                        const value = event.target.value
                            .split(",")
                            .map((item) => item.trim())
                            .filter(Boolean);

                        // Necesitamos registrar el valor manualmente.
                    }}
                />

                <p className="mt-1 text-xs text-slate-500">
                    Separa las certificaciones por coma.
                </p>
            </div>

            {/* Botones */}
            <div className="flex justify-end gap-3 border-t border-slate-700 pt-5">
                <button
                    type="button"
                    onClick={onCancel}
                    className="rounded-lg border border-slate-700 px-4 py-2.5 text-sm font-medium text-slate-300 transition hover:bg-slate-800"
                >
                    Cancelar
                </button>

                <button
                    type="submit"
                    className="rounded-lg bg-blue-600 px-5 py-2.5 text-sm font-semibold text-white transition hover:bg-blue-500"
                >
                    Crear técnico
                </button>
            </div>
        </form>
    );
}

export default TecnicoForm;