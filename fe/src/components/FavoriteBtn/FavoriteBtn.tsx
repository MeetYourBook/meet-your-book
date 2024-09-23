import styled from "styled-components";
import { StarOutlined, StarFilled } from "@ant-design/icons";
import { Libraries } from "@/types/Libraries";
import { BookContent } from "@/types/Books";
import { useEffect, useState } from "react";
import { getStorage, hasFavoriteItem } from "@/utils";
interface FavoritesBtnProps {
    item: Libraries | BookContent;
    storageName: "libraries" | "books";
}

const FavoriteBtn = ({ item, storageName }: FavoritesBtnProps) => {

    const [isFavorite, setIsFavorite] = useState(false);

    const handleFavorite = (e: React.MouseEvent) => {
        e.stopPropagation();
        const favoritesStorage = getStorage(storageName);
        const newFavoritesStorage = hasFavoriteItem(item.id, favoritesStorage)
            ? favoritesStorage.filter((curFav: Libraries) => curFav.id !== item.id)
            : [...favoritesStorage, item];
        localStorage.setItem(storageName, JSON.stringify(newFavoritesStorage));
        setIsFavorite(!isFavorite);
    };

    useEffect(() => {
        const favoritesStorage = getStorage(storageName);
        setIsFavorite(hasFavoriteItem(item.id, favoritesStorage));
    }, [item.id, storageName]);

    return (
        <button onClick={handleFavorite}>
            {isFavorite ? <FavoriteButton /> : <UnFavoriteButton />}
        </button>
    );
};

export default FavoriteBtn;

const UnFavoriteButton = styled(StarOutlined)`
    color: #fadb14;
`;
const FavoriteButton = styled(StarFilled)`
    color: #fadb14;
`;
