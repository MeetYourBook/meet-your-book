import { LibrariesType } from "@/types/Libraries";
export const getCurrentPageItems = <T>(
    items: T[],
    currentPage: number,
    itemsPerPage: number
) => {
    const indexOfLast = currentPage * itemsPerPage;
    const indexOfFirst = indexOfLast - itemsPerPage;
    return items.slice(indexOfFirst, indexOfLast);
};

export const handleImageError = (
    e: React.SyntheticEvent<HTMLImageElement>,
    fallbackSrc: string = "/images/errorImg.png"
) => {
    e.currentTarget.src = fallbackSrc;
    e.currentTarget.style.objectFit = "cover";
};

export const getStorage = (storageName: "libraries") => {
    return JSON.parse(localStorage.getItem(storageName) || "[]");
}

export const hasFavoriteItem = (itemID: string, storage: LibrariesType[]) => {
    return storage.some((fav: LibrariesType) => fav.id === itemID)
}
