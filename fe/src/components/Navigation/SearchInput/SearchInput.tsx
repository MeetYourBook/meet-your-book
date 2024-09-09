import * as S from "@/styles/SearchInputStyle";
import { KeyboardEvent, useRef } from "react";
import useQueryStore from "@/stores/queryStore";
import { ERROR_MESSAGE, FIRST_PAGE } from "@/constants";
import DropDownBox from "../DropDownBox/DropDownBox";
import { message } from "antd";

const SearchInput = () => {
    const inputRef = useRef<HTMLInputElement>(null);
    const { setSearchText, setPage } = useQueryStore();

    const handleSearchClick = () => {
        const searchValue = inputRef.current?.value.trim() ?? "";
        if (searchValue === "") return message.warning(ERROR_MESSAGE.EMPTY_INPUT);
        setPage(FIRST_PAGE);
        setSearchText(searchValue);
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
