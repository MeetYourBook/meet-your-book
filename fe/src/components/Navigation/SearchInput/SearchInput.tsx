import * as S from "@/styles/SearchInputStyle";
import { KeyboardEvent } from "react";
import useQueryStore from "@/stores/queryStore";
import { ERROR_MESSAGE, FIRST_PAGE } from "@/constants";
import DropDownBox from "../DropDownBox/DropDownBox";
import { message } from "antd";

const SearchInput = () => {
    const { inputValue, setInputValue, setSearchText, setPage } = useQueryStore();

    const handleSearchClick = () => {
        if (inputValue === "") {
            message.warning({
                content: ERROR_MESSAGE.EMPTY_INPUT,
                style: {
                    marginTop: "10vh",
                },
            });
            return;
        }
        setPage(FIRST_PAGE);
        setSearchText(inputValue);
    };

    const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") handleSearchClick();
    };

    return (
        <S.InputWrap>
            <DropDownBox />
            <S.InputField
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                onKeyDown={handleKeyDown}
                placeholder="Search For Book..."
            />
            <S.SearchField onClick={handleSearchClick} />
        </S.InputWrap>
    );
};

export default SearchInput;
