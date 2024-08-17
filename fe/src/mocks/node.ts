import { setupWorker } from "msw/browser";
import { booksHandlers } from "./handlers/booksHandler";
import { libraryHandlers } from "./handlers/libraryHandler";

export const worker = setupWorker(...booksHandlers, ...libraryHandlers);