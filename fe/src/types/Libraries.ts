export interface LibrariesType {
    totalPages: number;
    totalElements: number;
    pageSize: number;
    pageNumber: number;
    content: Libraries[];
}

export interface Libraries {
    id: number;
    name: string;
}