import * as S from "@/styles/SearchInputStyle";
import { KeyboardEvent, useRef } from "react";
import useQueryStore from "@/stores/queryStore";
import { FIRST_PAGE } from "@/constants";
import DropDownBox from "../DropDownBox/DropDownBox";

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
        <S.InputWrap>
            <DropDownBox/>
            <S.InputField
                ref={inputRef}
                onKeyDown={handleKeyDown}
                placeholder="Search For Book..."
            />
            <S.SearchField onClick={handleSearchClick}/>
        </S.InputWrap>
    );
};

export default SearchInput;
