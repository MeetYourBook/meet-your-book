import styled from "styled-components";
import { StarOutlined, StarFilled } from "@ant-design/icons";
import { LibrariesType } from "@/types/Libraries";
import { useEffect, useState } from "react";
import { getStorage, hasFavoriteItem } from "@/utils";
interface FavoritesBtnProps {
    item: LibrariesType;
    storageName: "libraries";
}

const FavoritesBtn = ({ item, storageName }: FavoritesBtnProps) => {
    
    const [isFavorite, setIsFavorite] = useState(false);

    const handleFavorite = () => {
        const favoritesStorage = getStorage(storageName);
        const newFavoritesStorage = hasFavoriteItem(item.id, favoritesStorage)
            ? favoritesStorage.filter((curFav: LibrariesType) => curFav.id !== item.id)
            : [...favoritesStorage, item];
        localStorage.setItem(storageName, JSON.stringify(newFavoritesStorage));
        setIsFavorite(!isFavorite);
    };

    useEffect(() => {
        const favoritesStorage = getStorage(storageName);
        setIsFavorite(hasFavoriteItem(item.id, favoritesStorage));
    }, [item.id, storageName]);

    return (
        <div onClick={handleFavorite}>
            {isFavorite ? <FavoriteBtn /> : <UnFavoriteBtn />}
        </div>
    );
};

export default FavoritesBtn;

const UnFavoriteBtn = styled(StarOutlined)`
    color: #fadb14;
`;
const FavoriteBtn = styled(StarFilled)`
    color: #fadb14;
`;
