


export function formatCurrentDateTime(): string {
    const now = new Date();

    const weekday = new Intl.DateTimeFormat("es-CO", {
        weekday: "short",
    }).format(now);

    const date = new Intl.DateTimeFormat("es-CO", {
        day: "2-digit",
        month: "short",
        year: "numeric",
    }).format(now);

    const time = new Intl.DateTimeFormat("es-CO", {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
    }).format(now);

    return `${weekday.charAt(0).toUpperCase()}${weekday.slice(1)} ${date} · ${time}`;
}
