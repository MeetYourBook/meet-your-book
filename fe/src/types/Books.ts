export interface LibraryResponse {
    id: string;
    LibraryName: string;
    BookLibraryUrl: string;
}
export interface BookContent {
    id: string;
    title: string;
    author: string;
    provider: string;
    publisher: string;
    imageUrl: string;
    libraryResponses: LibraryResponse[];
}