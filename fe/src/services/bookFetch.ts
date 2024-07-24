const API_URL = import.meta.env.VITE_APP_API_URL;

export const getData = async () => {
    try {
        const response = await fetch(API_URL + "books");
        if (!response.ok) {
            throw new Error(`요청이 잘 못 되었습니다. ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        throw error;
    }
};
