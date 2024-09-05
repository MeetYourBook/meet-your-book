import {http, HttpResponse} from "msw";
import libraryData from "../mockData/library.json";
import { DEV_API } from ".";

export const libraryHandlers = [
    http.get(DEV_API.LIBRARIES, () => {
        return HttpResponse.json(libraryData.library)
    })
]