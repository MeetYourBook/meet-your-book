import {http, HttpResponse} from "msw";
import libraryData from "../mockData/library.json"

export const libraryHandlers = [
    http.get("api/libraries", () => {
        return HttpResponse.json(libraryData.library)
    })
]