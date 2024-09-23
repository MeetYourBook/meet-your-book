import { Libraries } from "@/types/Libraries";
import { BookContent } from "@/types/Books";
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

export const getStorage = (storageName: string) => {
    const storedData = localStorage.getItem(storageName);
    return JSON.parse(storedData ?? "[]");
};

export const hasFavoriteItem = (itemID: string | number, storage: Libraries[] | BookContent[]) => {
    return storage.some((fav: Libraries | BookContent) => fav.id === itemID)
}
