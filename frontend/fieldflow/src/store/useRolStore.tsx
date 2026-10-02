import { create } from "zustand";

export type UserRole = "ADMIN" | "TECHNICIAN";

interface RoleState {
    role: UserRole;
    setRole: (role: UserRole) => void;
    toggleRole: () => void;
}

export const useRoleStore = create<RoleState>((set) => ({
    role: "ADMIN",

    setRole: (role) => set({ role }),

    toggleRole: () =>
        set((state) => ({
            role: state.role === "ADMIN" ? "TECHNICIAN" : "ADMIN",
        })),
}));