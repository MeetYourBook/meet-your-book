import { create } from "zustand";

interface StoreState {
    query: string;
    searchText: string;
    page: number;
    size: number;
    selectedValue: string;
    librariesFilter: string[];
    setQuery: (query: string) => void;
    setSearchText: (searchText: string) => void;
    setPage: (page: number) => void;
    setSize: (size: number) => void;
    setSelectedValue: (value: string) => void;
    setLibrariesFilter: (libraries: string[]) => void;
}

const useQueryStore = create<StoreState>((set) => ({
    query: "",
    searchText: "",
    page: 1,
    size: 20,
    selectedValue: "",
    librariesFilter: [],
    setQuery: (query) => set({ query }),
    setSearchText: (searchText) => set({ searchText }),
    setPage: (page) => set({ page }),
    setSize: (size) => set({ size }),
    setSelectedValue: (value) => set({ selectedValue: value }),
    setLibrariesFilter: (libraries) => set({ librariesFilter: libraries }),
}));

export default useQueryStore;
