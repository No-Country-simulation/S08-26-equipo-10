

import type { ReactNode } from "react";
import { Loading } from "./loading";
import { ErrorMessage } from "./ErrorMessage";
import { EmptyState } from "./EmptyState";

export interface Column<T> {
    header: string;
    accessor: keyof T;
    render?: (value: unknown, row: T) => ReactNode;
}

export function createColumn<T>() {
    return function <K extends keyof T>(
        column: {
            header: string;
            accessor: K;
            render?: (value: T[K], row: T) => ReactNode;
        }
    ): Column<T> {
        return column as Column<T>;
    };
}

interface TableProps<T> {
    columns: Column<T>[];
    data: T[];
    isLoading?: boolean;
    isError?: boolean;
    errorMessage?: string;
    emptyMessage?: string;
}

export function Table<T>({
    columns,
    data,
    isLoading = false,
    isError = false,
    errorMessage = "Ocurrió un error al cargar los datos",
    emptyMessage = "No hay registros para mostrar",
}: TableProps<T>) {
    return (
        <div className="overflow-x-auto rounded-lg border border-slate-700">
            {isLoading ? (
                <Loading />
            ) : isError ? (
                <ErrorMessage message={errorMessage} />
            ) : data.length === 0 ? (
                <EmptyState title="Vacio" description={emptyMessage} />
            ) : (
                <table className="w-full text-left text-sm">
                    <thead className="border-b border-slate-700 bg-slate-800">
                        <tr>
                            {columns.map((column) => (
                                <th
                                    key={String(column.accessor)}
                                    className="px-4 py-3 font-medium text-slate-400"
                                >
                                    {column.header}
                                </th>
                            ))}
                        </tr>
                    </thead>

                    <tbody>
                        {data.map((row, index) => (
                            <tr
                                key={index}
                                className="border-b border-slate-800 last:border-0"
                            >
                                {columns.map((column) => (
                                    <td
                                        key={String(column.accessor)}
                                        className="px-4 py-3 text-slate-300"
                                    >
                                        {column.render
                                            ? column.render(
                                                row[column.accessor],
                                                row
                                            )
                                            : String(row[column.accessor])}
                                    </td>
                                ))}
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}