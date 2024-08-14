import { http, HttpResponse } from "msw";
import booksData from "../mockData/books.json";
import { BookContent } from "@/components/BooksDisplay/BooksDisplay";

interface Params {
    [Key: string]: string;
}

const DEFAULT_PAGE_SIZE = 20;

const searchFilter = (books: BookContent[], params: Params) => {
    if (params.title && params.author && params.publisher) {
        const search = params.title.toLowerCase();
        return books.filter(
            (book) =>
                book.title.toLowerCase().includes(search) ||
                book.author.toLowerCase().includes(search) ||
                book.publisher.toLowerCase().includes(search)
        );
    }

    if (params.title) {
        const search = params.title.toLowerCase();
        return books.filter((book) =>
            book.title.toLowerCase().includes(search)
        );
    }

    if (params.author) {
        const search = params.author.toLowerCase();
        return books.filter((book) =>
            book.author.toLowerCase().includes(search)
        );
    }

    if (params.publisher) {
        const search = params.publisher.toLowerCase();
        return books.filter((book) =>
            book.publisher.toLowerCase().includes(search)
        );
    }

    return books;
}

const filterBooks = (books: BookContent[], params: Params) => {
    let filteredBooks = searchFilter(books, params);

    if (params.libraries) {
        const libraryIds = params.libraries.split(',');
        filteredBooks = filteredBooks.filter(book =>
            book.libraryResponses.some(library => libraryIds.includes(library.id))
        );
    }

    return filteredBooks;
};

const paginateBooks = (books: BookContent[], page: number, size: number) => {
    const startIndex = page * size;
    const endIndex = startIndex + size;
    return books.slice(startIndex, endIndex);
};

export const booksHandlers = [
    http.get("api/books", ({ request }) => {
        const url = new URL(request.url);
        const params = Object.fromEntries(url.searchParams);
        const filteredBooks = filterBooks(booksData.content, params);
        
        const pageNum = parseInt(params.page) || 0;
        const sizeNum = parseInt(params.size) || DEFAULT_PAGE_SIZE;
        
        const paginatedBooks = paginateBooks(filteredBooks, pageNum, sizeNum);
        
        const response = {
            content: paginatedBooks,
            pageNumber: pageNum,
            pageSize: sizeNum,
            totalElements: filteredBooks.length,
            totalPages: Math.ceil(filteredBooks.length / sizeNum)
        };
        
        return HttpResponse.json(response);
    }),
];