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

    public post() {}

    public patch() {}

    public delete() {}
}