
export type Technician = {
    id: string;
    nombre: string;
    codigo: string;
    telefono: string;
    email: string;
}

export type TechnicianWorkOrder = Omit<Technician, "codigo" | "telefono" | "email">;

