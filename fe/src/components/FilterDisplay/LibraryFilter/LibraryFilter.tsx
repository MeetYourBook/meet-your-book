import React, { useMemo } from "react";
import useDebounce from "@/hooks/useDebounce";
import { useLibraryFilter } from "@/hooks/useLibraryFilter";
import { LibrariesType } from "@/types/Libraries";
import { DownOutlined, UpOutlined } from "@ant-design/icons";
import { DEBOUNCE_TIME } from "@/constants";
import * as S from "@/styles/LibraryFilterStyle";
import useQueryData from "@/hooks/useQueryData";
interface LibraryListProps {
    libraries: LibrariesType[];
    librariesFilter: string[];
    handleSelectLibrary: (id: string) => void;
}

const LibraryList = React.memo(
    ({ libraries, librariesFilter, handleSelectLibrary }: LibraryListProps) => (
        <>
            {libraries.map((library, index) => (
                <S.ListItem key={library.id}>
                    <S.Checkbox
                        type="checkbox"
                        id={`library-${index}`}
                        checked={librariesFilter.includes(library.id)}
                        onChange={() => handleSelectLibrary(library.id)}
                    />
                    <S.Label htmlFor={`library-${index}`}>
                        {library.name}
                    </S.Label>
                </S.ListItem>
            ))}
        </>
    )
);

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

    if (isLoading) return <div>Loading...</div>; 
    // suspense 처리
    // errorBoundary 처리

    return (
        <S.Container>
            <S.Header onClick={toggleFilter}>
                <h1>도서관 필터</h1>
                {isOpen ? <UpOutlined /> : <DownOutlined />}
            </S.Header>
            <S.ListWrap $isOpen={isOpen}>
                <S.Input
                    value={search}
                    onChange={handleSearch}
                    placeholder="도서관 검색..."
                />
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
