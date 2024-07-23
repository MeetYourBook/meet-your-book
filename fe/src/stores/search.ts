import { create } from "zustand";

interface SearchStore {
    search: string;
    setSearch: (text: string) => void;
}

const useSearchStore = create<SearchStore>((set) => ({
    search: "",
    setSearch: (text: string) => set({ search: text }),
}));

export default useSearchStore;
