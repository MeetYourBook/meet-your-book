import { LibrariesType } from "@/types/Libraries";
import * as S from "@/styles/LibrariesPagingStyle";
import { useMemo, useState } from "react";
import useDebounce from "@/hooks/useDebounce";
import { DEBOUNCE_TIME } from "@/constants";
interface LibrariesPagingProps {
    libraryResponses: LibrariesType[];
}
const LibrariesPaging = ({ libraryResponses }: LibrariesPagingProps) => {
    const [searchValue, setSearchValue] = useState("");

    const debouncedValue = useDebounce(searchValue, DEBOUNCE_TIME);
    
    const filteredLibraries = useMemo(() => {
        return debouncedValue
        ? libraryResponses.filter((item: LibrariesType) => item.LibraryName.includes(debouncedValue))
        : libraryResponses;
    }, [debouncedValue, libraryResponses]);

    return (
        <S.Container>
            <p>소장 도서관: {libraryResponses.length}</p>
            <S.Input
                value={searchValue}
                placeholder="도서관 검색..."
                onChange={(e) => setSearchValue(e.target.value)}
            />
            <S.LibrariesWrap>
                {filteredLibraries.map((curLibrary, idx) => (
                    <S.LibraryItem key={idx}>
                        {curLibrary.LibraryName}
                    </S.LibraryItem>
                ))}
            </S.LibrariesWrap>
        </S.Container>
    );
};

export default LibrariesPaging;
