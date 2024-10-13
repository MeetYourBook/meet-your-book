import { http, HttpResponse } from "msw";
import libraryData from "../mockData/library.json";
import { DEV_API } from ".";
import { Libraries } from "@/types/Libraries";

interface Params {
    [Key: string]: string;
}

const filterLibraries = (libraries: Libraries[], params: Params) => {
    const { name } = params;
    
    return libraries.filter((curLibrary) =>
        curLibrary.name.toLowerCase().includes(name.toLowerCase())
    );
};

export const libraryHandlers = [
    http.get(DEV_API.LIBRARIES, ({ request }) => {
        const url = new URL(request.url);
        const params = Object.fromEntries(url.searchParams);
        
        const filteredLibraries = filterLibraries(libraryData.library, params);
        const page = parseInt(params.page);
        const size = parseInt(params.size);
        const startIdx = page * size;
        const endIndex = startIdx + size;
        
        const paginatedLibraries = filteredLibraries.slice(startIdx, endIndex);
        
        const response = {
            pageNumber: page,
            pageSize: size,
            totalElements: filteredLibraries.length,
            totalPages: Math.ceil(filteredLibraries.length / size),
            content: paginatedLibraries,
        }
        
        return HttpResponse.json(response);
    }),
];
