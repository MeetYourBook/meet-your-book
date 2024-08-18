import * as S from "@/styles/SearchInputStyle";
import { KeyboardEvent, useRef } from "react";
import useQueryStore from "@/stores/queryStore";
import { SearchOutlined } from "@ant-design/icons";
import { FIRST_PAGE } from "@/constants";

const SearchInput = () => {
    const inputRef = useRef<HTMLInputElement>(null);
    const { setSearchText, setPage } = useQueryStore();

    const handleSearchClick = () => {
        if (inputRef.current) {
            setPage(FIRST_PAGE)
            setSearchText(inputRef.current.value);
        }
    };

    const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") handleSearchClick();
    };

    return (
        <>
            <S.InputField
                ref={inputRef}
                onKeyDown={handleKeyDown}
                placeholder="Search For Book..."
            />
            <S.SearchField onClick={handleSearchClick}>
                <SearchOutlined />
            </S.SearchField>
        </>
    );
};

export default SearchInput;
