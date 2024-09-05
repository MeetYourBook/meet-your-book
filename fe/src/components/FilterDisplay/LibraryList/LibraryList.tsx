import FavoritesBtn from "@/components/FavoritesBtn/FavoritesBtn";
import * as S from "@/styles/LibraryListStyle";
import { LibrariesType } from "@/types/Libraries";

interface LibraryListProps {
    libraries: LibrariesType[];
    librariesFilter: string[];
    handleSelectLibrary: (id: string) => void;
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
                    checked={librariesFilter.includes(library.id)}
                    onChange={() => handleSelectLibrary(library.id)}
                />
                <S.Label htmlFor={`${library.id}-${index}`}>
                    {library.name}
                </S.Label>
                <S.FavoriteBtnWrap>
                    <FavoritesBtn item={library} storageName="libraries"/>
                </S.FavoriteBtnWrap>
            </S.ListItem>
        ))}
    </>
);

export default LibraryList;
