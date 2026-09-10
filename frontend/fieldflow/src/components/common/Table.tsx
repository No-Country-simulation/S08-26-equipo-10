import type { ReactNode } from "react";

interface Column<T> {
    header: string;
    accessor: keyof T;
    render?: (value: T[keyof T], row: T) => ReactNode;
}

interface TableProps<T> {
    columns: Column<T>[];
    data: T[];
}

export function Table<T>({
    columns,
    data,
}: TableProps<T>) {
    return (
        <div className="overflow-x-auto rounded-lg border border-slate-700">
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
                                        ? column.render(row[column.accessor], row)
                                        : String(row[column.accessor])}
                                </td>
                            ))}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}