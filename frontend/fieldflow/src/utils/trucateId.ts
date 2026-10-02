

export function truncateId(id: string, chars = 8): string {
    if (id.length <= chars) {
        return id;
    }
    return `${id.slice(0, chars)}...${id.slice(-chars)}`;


}