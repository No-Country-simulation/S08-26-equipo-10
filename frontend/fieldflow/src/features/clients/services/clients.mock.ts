import type { Client } from "@/types/client";

export const clients: Client[] = [
    {
        id: "1",
        name: "Metalúrgica del Norte S.A.",
        sites: [
            {
                id: "site-1",
                name: "Planta Principal",
                address: "Av. Industrial 2450, Lima",
                installations: [
                    {
                        id: "installation-1",
                        name: "Sala de Compresores",
                    },
                    {
                        id: "installation-2",
                        name: "Área de Refrigeración",
                    },
                ],
            },
            {
                id: "site-2",
                name: "Almacén Norte",
                address: "Jr. Logística 890, Lima",
                installations: [
                    {
                        id: "installation-3",
                        name: "Zona de Carga",
                    },
                ],
            },
        ],
    },

    {
        id: "2",
        name: "Hipermercado Vidal S.A.C.",
        sites: [
            {
                id: "site-3",
                name: "Sucursal Centro",
                address: "Av. Principal 1200, Lima",
                installations: [
                    {
                        id: "installation-4",
                        name: "Sistema de Refrigeración",
                    },
                ],
            },
        ],
    },

    {
        id: "3",
        name: "Hospital San Rafael",
        sites: [
            {
                id: "site-4",
                name: "Hospital Principal",
                address: "Av. Salud 450, Lima",
                installations: [
                    {
                        id: "installation-5",
                        name: "Central de Gases Medicinales",
                    },
                    {
                        id: "installation-6",
                        name: "Sistema HVAC",
                    },
                ],
            },
        ],
    },

    {
        id: "4",
        name: "Centro Comercial Plaza Mayor",
        sites: [
            {
                id: "site-5",
                name: "Plaza Principal",
                address: "Av. Comercio 800, Lima",
                installations: [
                    {
                        id: "installation-7",
                        name: "Sistema de Climatización",
                    },
                ],
            },
        ],
    },

    {
        id: "5",
        name: "Empresa Portuaria del Pacífico",
        sites: [
            {
                id: "site-6",
                name: "Terminal Norte",
                address: "Zona Portuaria 120, Lima",
                installations: [
                    {
                        id: "installation-8",
                        name: "Sistema de Bombeo",
                    },
                ],
            },
        ],
    },
];