export type EquipmentStatus =
    | "Operativo"
    | "Requiere mantenimiento"
    | "Fuera de servicio";

export interface EquipmentClient {
    id: string;
    name: string;
}

export interface EquipmentSite {
    id: string;
    name: string;
    address: string;
}

export interface EquipmentInstallation {
    id: string;
    name: string;
}

export interface Equipment {
    id: string;
    identifier: string;
    name: string;
    currentStatus: EquipmentStatus;
    client: EquipmentClient;
    site: EquipmentSite;
    installation: EquipmentInstallation;
}