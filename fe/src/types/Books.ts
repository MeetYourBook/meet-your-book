export interface LibraryResponse {
    id: string;
    libraryName: string;
    bookLibraryUrl: string;
}
export interface BookContent {
    id: string;
    title: string;
    author: string;
    publisher: string;
    imageUrl: string;
    publishDate: string;
    libraryResponses: LibraryResponse[];
}