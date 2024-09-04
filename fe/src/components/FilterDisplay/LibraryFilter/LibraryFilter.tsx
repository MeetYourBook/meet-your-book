import { useLibraryFilter } from "@/hooks/useLibraryFilter";
import { DownOutlined, UpOutlined } from "@ant-design/icons";
import * as S from "@/styles/LibraryFilterStyle";
import LibraryList from "../LibraryList/LibraryList";
import LoadingFallBack from "@/components/LoadingFallBack/LoadingFallBack";
import { LibrariesType } from "@/types/Libraries";

const LibraryFilter = () => {
    const {
        isOpen,
        searchValue,
        librariesFilter,
        handleSelectLibrary,
        toggleFilter,
        handleSearch,
        isLoading,
        getDisplayLibraries,
        observerRef
    } = useLibraryFilter();
    console.log("배포테스트 1")
    return (
        <S.Container>
            <S.Header onClick={toggleFilter}>
                <S.Title>도서관 필터</S.Title>
                {isOpen ? <UpOutlined /> : <DownOutlined />}
            </S.Header>
            <S.ListWrap $isOpen={isOpen}>
                <S.Input
                    value={searchValue}
                    onChange={handleSearch}
                    placeholder="도서관 검색..."
                />
                {isLoading && <LoadingFallBack/>}
                <LibraryList
                    libraries={getDisplayLibraries as LibrariesType[]}
                    librariesFilter={librariesFilter}
                    handleSelectLibrary={handleSelectLibrary}
                    />
                <div ref={observerRef} style={{ height: "10px" }} />
            </S.ListWrap>
        </S.Container>
    );
};

export default LibraryFilter;
