import { create } from "zustand";
import { BookContent } from "@/types/Books";
import { Libraries } from "@/types/Libraries";
interface StoreState {
    booksItem: BookContent[];
    searchText: string;
    inputValue: string;
    page: number;
    size: number;
    selectedValue: string;
    librariesFilter: Libraries[];
    setBooksItem: (books: BookContent[]) => void;
    setSearchText: (searchText: string) => void;
    setInputValue: (value: string) => void;
    setPage: (pageNum: number) => void;
    setSize: (size: number) => void;
    setSelectedValue: (value: string) => void;
    setLibrariesFilter: (libraries: Libraries[]) => void;
    resetFilter: () => void;
}

const useQueryStore = create<StoreState>((set) => ({
    booksItem: [],
    searchText: "",
    inputValue: "",
    page: 0,
    size: 20,
    selectedValue: "all",
    librariesFilter: [],
    setBooksItem: (books) => set({ booksItem: books }),
    setSearchText: (searchText) => set({ searchText }),
    setInputValue: (value) => set({ inputValue: value }),
    setPage: (pageNum) => set({ page: pageNum }),
    setSize: (size) => set({ size }),
    setSelectedValue: (value) => set({ selectedValue: value }),
    setLibrariesFilter: (libraries) => set({ librariesFilter: libraries }),
    resetFilter: () =>
        set({
            searchText: "",
            inputValue: "",
            page: 0,
            selectedValue: "all",
            librariesFilter: [],
        }),
}));

export default useQueryStore;
