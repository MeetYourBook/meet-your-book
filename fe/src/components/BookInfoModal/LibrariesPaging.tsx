import { LibrariesResponseType } from "@/types/LibrariesResponse";
import * as S from "@/styles/LibrariesPagingStyle";
import { useMemo, useState } from "react";
import useDebounce from "@/hooks/useDebounce";
import { DEBOUNCE_TIME, PAGINATION_FIRST_PAGE } from "@/constants";
import { Pagination } from "antd";
import { LIBRARIES_PER_PAGE } from "@/types/PageNation";

interface LibrariesPagingProps {
    libraryResponses: LibrariesResponseType[];
}

const LibrariesPaging = ({ libraryResponses }: LibrariesPagingProps) => {
    const [searchValue, setSearchValue] = useState("");
    const [currentPage, setCurrentPage] = useState(PAGINATION_FIRST_PAGE);

    const debouncedValue = useDebounce(searchValue, DEBOUNCE_TIME);

    const filteredLibraries = useMemo(() => {
        return debouncedValue
            ? libraryResponses.filter((item: LibrariesResponseType) =>
                  item.LibraryName.includes(debouncedValue)
              )
            : libraryResponses;
    }, [debouncedValue, libraryResponses]);

    const indexOfLast = currentPage * LIBRARIES_PER_PAGE;
    const indexOfFirst = indexOfLast - LIBRARIES_PER_PAGE;
    const currentPageLibraries = filteredLibraries.slice(
        indexOfFirst,
        indexOfLast
    );

    const handlePageChange = (page: number) => setCurrentPage(page);
    return (
        <S.Container>
            <p>소장 도서관: {libraryResponses.length}</p>
            <S.Input
                value={searchValue}
                placeholder="도서관 검색..."
                onChange={(e) => setSearchValue(e.target.value)}
            />
            <S.LibrariesWrap>
                {currentPageLibraries.map((curLibrary, idx) => (
                    <S.LibraryItem key={idx} href={curLibrary.BookLibraryUrl}>
                        {curLibrary.LibraryName}
                    </S.LibraryItem>
                ))}
            </S.LibrariesWrap>
            <S.PagingWrap>
                <Pagination
                    current={currentPage}
                    total={filteredLibraries.length}
                    pageSize={LIBRARIES_PER_PAGE}
                    onChange={handlePageChange}
                    size="small"
                />
            </S.PagingWrap>
        </S.Container>
    );
};

export default LibrariesPaging;
