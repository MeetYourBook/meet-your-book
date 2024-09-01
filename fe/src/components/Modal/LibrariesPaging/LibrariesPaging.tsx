import { LibrariesResponseType } from "@/types/LibrariesResponse";
import * as S from "@/styles/LibrariesPagingStyle";
import { useEffect, useState } from "react";
import { PAGINATION_FIRST_PAGE } from "@/constants";
import { Pagination } from "antd";
import { LIBRARIES_PER_PAGE } from "@/types/PageNation";
import useSearchFilter from "@/hooks/useFilterSearch";
import { getCurrentPageItems } from "@/utils";
interface LibrariesPagingProps {
    libraryResponses: LibrariesResponseType[];
}

const LibrariesPaging = ({ libraryResponses }: LibrariesPagingProps) => {
    const [currentPage, setCurrentPage] = useState(PAGINATION_FIRST_PAGE);
    const { searchValue, setSearchValue, filteredLibraries } = useSearchFilter({
        libraries: libraryResponses,
        keyName: 'LibraryName'
    });

    const currentPageLibraries = getCurrentPageItems(filteredLibraries, currentPage, LIBRARIES_PER_PAGE);

    const handlePageChange = (page: number) => setCurrentPage(page);

    useEffect(() => setCurrentPage(PAGINATION_FIRST_PAGE), [filteredLibraries]);
    
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
                    <S.LibraryItem key={idx} href={(curLibrary as LibrariesResponseType).BookLibraryUrl}>
                        {(curLibrary as LibrariesResponseType).LibraryName}
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
