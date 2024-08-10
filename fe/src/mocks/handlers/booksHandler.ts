import { http, HttpResponse } from "msw";
import booksData from "../mockData/books.json";

export const booksHandlers = [
    http.get("api/books", () => {
        return HttpResponse.json(booksData.books);
    }),
];
