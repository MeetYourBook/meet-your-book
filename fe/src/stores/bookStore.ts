import { create } from "zustand";

interface StoreState {
    query: string;
    page: number;
    size: number;
    selectedValue: string;
    librariesFilter: string[];
    setQuery: (query: string) => void;
    setPage: (page: number) => void;
    setSize: (size: number) => void;
    setSelectedValue: (value: string) => void;
    setLibrariesFilter: (libraries: string[]) => void;
}

const useBookStore = create<StoreState>((set) => ({
    query: "",
    page: 1,
    size: 20,
    selectedValue: "",
    librariesFilter: [],
    setQuery: (query) => set({ query }),
    setPage: (page) => set({ page }),
    setSize: (size) => set({ size }),
    setSelectedValue: (value) => set({ selectedValue: value }),
    setLibrariesFilter: (libraries) => set({ librariesFilter: libraries }),
}));

export default useBookStore;
