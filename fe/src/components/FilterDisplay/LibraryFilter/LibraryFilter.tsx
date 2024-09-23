import { useLibraryFilter } from "@/hooks/useLibraryFilter";
import { DownOutlined, UpOutlined } from "@ant-design/icons";
import * as S from "@/styles/LibraryFilterStyle";
import LibraryList from "../LibraryList/LibraryList";
import LoadingFallBack from "@/components/LoadingFallBack/LoadingFallBack";
import FilterInput from "../FilterInput/FilterInput";
const LibraryFilter = () => {
    const {
        isOpen,
        setDebounceValue,
        toggleFilter,
        setLibraryPage,
        librariesItem,
        isLoading,
        librariesFilter,
        handleSelectLibrary,
        observerRef,
        totalPages,
        libraryPage,
    } = useLibraryFilter();

    const hasNoContent = !isLoading && librariesItem.length === 0
    const isLastPage = totalPages <= libraryPage;

    return (
        <S.Container>
            <S.Header onClick={toggleFilter}>
                <S.Title>도서관 필터</S.Title>
                {isOpen ? <UpOutlined /> : <DownOutlined />}
            </S.Header>
            <S.ListWrap $isOpen={isOpen}>
                <FilterInput
                    setDebounceValue={setDebounceValue}
                    setLibraryPage={setLibraryPage}
                />
                {hasNoContent ? (
                    <S.Message>검색 결과가 없습니다.</S.Message>
                ) : (
                    <>
                        <LibraryList
                            libraries={librariesItem}
                            librariesFilter={librariesFilter}
                            handleSelectLibrary={handleSelectLibrary}
                        />
                        {isLoading && <LoadingFallBack />}
                        {!isLoading && (
                            isLastPage 
                                ? <S.Message>마지막 페이지입니다.</S.Message>
                                : <S.Message ref={observerRef} style={{ height: "10px" }} />
                        )}
                    </>
                )}
            </S.ListWrap>
        </S.Container>
    );
};

export default LibraryFilter;
