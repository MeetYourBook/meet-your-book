import * as S from "@/styles/SearchInputStyle";
import { useState, KeyboardEvent } from "react";
import { SearchOutlined } from "@ant-design/icons";

const SearchInput = () => {
    const [search, setSearch] = useState("");

    const handleSearchClick = () => {
        console.log(search)
        // zustant로 search query 생성하는 로직
    }

    const handleKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
        if (event.key === "Enter") handleSearchClick();
    };
    return (
        <>
            <S.InputField
                value={search}
                onChange={(e) => setSearch(e.target.value)}
                onKeyDown={handleKeyDown}
                placeholder="Search For Book..."
            />
            <S.SearchField onClick={handleSearchClick}>
                <SearchOutlined/>
            </S.SearchField>
        </>
    );
};

export default SearchInput;
