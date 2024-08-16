import { create } from "zustand";
import { BookContent } from "@/types/Books";
interface StoreState {
    booksItem: BookContent[]
    searchText: string;
    page: number;
    size: number;
    selectedValue: string;
    librariesFilter: string[];
    setBooksItem: (books: BookContent[]) => void;
    setSearchText: (searchText: string) => void;
    setPage: (pageNum: number) => void;
    setSize: (size: number) => void;
    setSelectedValue: (value: string) => void;
    setLibrariesFilter: (libraries: string[]) => void;
}

const useQueryStore = create<StoreState>((set) => ({
    booksItem: [],
    searchText: "",
    page: 0,
    size: 20,
    selectedValue: "all",
    librariesFilter: [],
    setBooksItem: (books) => set({booksItem: books}),
    setSearchText: (searchText) => set({ searchText }),
    setPage: (pageNum) => set({page: pageNum}),
    setSize: (size) => set({ size }),
    setSelectedValue: (value) => set({ selectedValue: value }),
    setLibrariesFilter: (libraries) => set({ librariesFilter: libraries }),
}));

export default useQueryStore;
