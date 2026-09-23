export type Installation = {
    id: string;
    name: string;
};

export type Site = {
    id: string;
    name: string;
    address: string;
    installations: Installation[];
};

export type Client = {
    id: string;
    name: string;
    sites: Site[];
};