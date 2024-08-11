import { useState } from "react";
import useQueryStore from "@/stores/queryStore";
import * as S from "@/styles/DropDownStyle";

type ItemType = { value: string; label: string };

const initialItems = [
    { value: "all", label: "통합 검색" },
    { value: "title", label: "제목" },
    { value: "author", label: "저자" },
    { value: "publisher", label: "출판사" },
];

const DropDownBox = () => {
    const { setSelectedValue } = useQueryStore();
    const [items] = useState<ItemType[]>(initialItems);
    const [curSelect, setSelect] = useState<ItemType>(items[0]);
    const [isHover, setIsHover] = useState<boolean>(false);

    const handleClickItem = (item: ItemType) => {
        setSelect(item);
        setIsHover(false);
        setSelectedValue(item.value)
    };

    return (
        <S.DropdownContainer
            onMouseEnter={() => setIsHover(true)}
            onMouseLeave={() => setIsHover(false)}
        >
            <S.SelectedItemText>{curSelect.label}</S.SelectedItemText>
            {isHover && (
                <S.DropdownList>
                    {items.map((item) => (
                        <S.DropdownItem
                            key={item.value}
                            onClick={() => handleClickItem(item)}
                        >
                            {item.label}
                        </S.DropdownItem>
                    ))}
                </S.DropdownList>
            )}
        </S.DropdownContainer>
    );
};

export default DropDownBox;
