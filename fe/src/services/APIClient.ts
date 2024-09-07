import { fetchAPI } from "./fetch";

const headers = {'Content-Type': 'application/json'}

export class APIClient {
    #path: string;

    constructor(path: string) {
        this.#path = path
    }

    public get(query = "") {
        return fetchAPI(this.#path + query, {
            method: 'GET',
            headers: headers,
        })
    }

    public post<T>(query = "", body: T) {
        return fetchAPI(this.#path + query, {
            method: "POST",
            headers: headers,
            body: body ? JSON.stringify(body) : null
        })
    }

    public patch() {}

    public delete() {}
}