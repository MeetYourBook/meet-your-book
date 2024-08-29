import { useMemo } from "react";
import useDebounce from "@/hooks/useDebounce";
import { useLibraryFilter } from "@/hooks/useLibraryFilter";
import { LibrariesType } from "@/types/Libraries";
import { DownOutlined, UpOutlined } from "@ant-design/icons";
import { DEBOUNCE_TIME } from "@/constants";
import * as S from "@/styles/LibraryFilterStyle";
import useQueryData from "@/hooks/useQueryData";
import LibraryList from "../LibraryList/LibraryList";
import LoadingFallBack from "@/components/LoadingFallBack/LoadingFallBack";

const LibraryFilter = () => {
    const {
        isOpen,
        search,
        librariesFilter,
        handleSelectLibrary,
        toggleFilter,
        handleSearch,
    } = useLibraryFilter();
    const {data = [], isLoading} = useQueryData("libraries")

    const debouncedValue = useDebounce(search, DEBOUNCE_TIME);
    
    const libraries = useMemo(() => {
        return debouncedValue
        ? data.filter((item: LibrariesType) => item.name.includes(debouncedValue))
        : data;
    }, [debouncedValue, data]);

    return (
        <S.Container>
            <S.Header onClick={toggleFilter}>
                <S.Title>도서관 필터</S.Title>
                {isOpen ? <UpOutlined /> : <DownOutlined />}
            </S.Header>
            <S.ListWrap $isOpen={isOpen}>
                <S.Input
                    value={search}
                    onChange={handleSearch}
                    placeholder="도서관 검색..."
                />
                {isLoading && <LoadingFallBack/>}
                <LibraryList
                    libraries={libraries}
                    librariesFilter={librariesFilter}
                    handleSelectLibrary={handleSelectLibrary}
                />
            </S.ListWrap>
        </S.Container>
    );
};

export default LibraryFilter;
