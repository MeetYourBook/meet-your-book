import FavoriteBtn from "@/components/FavoriteBtn/FavoriteBtn";
import * as S from "@/styles/LibraryListStyle";
import { LibrariesType } from "@/types/Libraries";

interface LibraryListProps {
    libraries: LibrariesType[];
    librariesFilter: LibrariesType[];
    handleSelectLibrary: (library: LibrariesType) => void;
}

const LibraryList = ({
    libraries,
    librariesFilter,
    handleSelectLibrary,
}: LibraryListProps) => (
    <>
        {libraries.map((library, index) => (
            <S.ListItem key={`${library.id}-${index}`}>
                <S.Checkbox
                    type="checkbox"
                    id={`${library.id}-${index}`}
                    checked={librariesFilter.some(curLibrary => curLibrary.id === library.id)}
                    onChange={() => handleSelectLibrary(library)}
                />
                <S.Label htmlFor={`${library.id}-${index}`}>
                    {library.name}
                </S.Label>
                <S.FavoriteBtnWrap>
                    <FavoriteBtn item={library} storageName="libraries"/>
                </S.FavoriteBtnWrap>
            </S.ListItem>
        ))}
    </>
);

export default LibraryList;
