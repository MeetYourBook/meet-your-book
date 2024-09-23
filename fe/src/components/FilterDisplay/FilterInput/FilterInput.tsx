import React, { Dispatch, SetStateAction, useEffect, useState } from "react";
import styled from "styled-components";
import { DEBOUNCE_TIME, FIRST_PAGE } from "@/constants";

interface FilterInputProps {
    setDebounceValue: Dispatch<SetStateAction<string>>;
    setLibraryPage: Dispatch<SetStateAction<number>>
}
const FilterInput = ({ setDebounceValue,setLibraryPage }: FilterInputProps) => {
    const [searchValue, setSearchValue] = useState("");

    const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) =>
        setSearchValue(e.target.value);

    useEffect(() => {
        const timer = setTimeout(() => {
            setDebounceValue(searchValue)
            setLibraryPage(FIRST_PAGE)
        }, DEBOUNCE_TIME);
        
        return () => clearTimeout(timer);
    }, [searchValue, setDebounceValue, setLibraryPage]);

    return (
        <Input
            value={searchValue}
            onChange={handleSearch}
            placeholder="도서관 검색..."
        />
    );
};

export default FilterInput;

const Input = styled.input`
    width: 90%;
    height: 2rem;
    border: ${({ theme }) => theme.border};
    border-radius: 8px;
    margin: 0px auto 1rem;
    padding: 0 0.6rem;
    outline: none;
    background-color: ${({ theme }) => theme.input};
    color: ${({ theme }) => theme.text};
`;
