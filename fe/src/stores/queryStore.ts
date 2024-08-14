import { create } from "zustand";

interface StoreState {
    searchText: string;
    page: number;
    size: number;
    selectedValue: string;
    librariesFilter: string[];
    setSearchText: (searchText: string) => void;
    setPage: (page: number) => void;
    setSize: (size: number) => void;
    setSelectedValue: (value: string) => void;
    setLibrariesFilter: (libraries: string[]) => void;
}

const useQueryStore = create<StoreState>((set) => ({
    searchText: "",
    page: 0,
    size: 20,
    selectedValue: "all",
    librariesFilter: [],
    setSearchText: (searchText) => set({ searchText }),
    setPage: (page) => set({ page }),
    setSize: (size) => set({ size }),
    setSelectedValue: (value) => set({ selectedValue: value }),
    setLibrariesFilter: (libraries) => set({ librariesFilter: libraries }),
}));

export default useQueryStore;
