const API_URL = import.meta.env.VITE_API_URL

export const fetchAPI = async (query: string, option: RequestInit) => {
    try{
        const response = await fetch(API_URL+ 11+ query, option)

        if(!response.ok) {
            throw new Error(`${response.status}`)
        }
        const data = await response.json();
        return data
    } catch (error) {
        if (error instanceof Error) {
            throw new Error(`Fetch API error: ${error.message}`);
        } else {
            throw new Error('An unknown error occurred');
        }
    }
}