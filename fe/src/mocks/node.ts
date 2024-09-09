import { setupWorker } from "msw/browser";
import { booksHandlers } from "./handlers/booksHandler";
import { libraryHandlers } from "./handlers/libraryHandler";
import { loginHandlers } from "./handlers/loginHandler";

export const worker = setupWorker(
    ...booksHandlers,
    ...libraryHandlers,
    ...loginHandlers
);
