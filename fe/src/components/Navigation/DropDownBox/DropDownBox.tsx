import { useState } from "react";
import useQueryStore from "@/stores/queryStore";
import * as S from "@/styles/DropDownStyle";
import { ItemType } from "@/types/DropDown";
import { DROP_DOWN_INITIAL_ITEMS, FIRST_PAGE } from "@/constants";

const DropDownBox = () => {
    const { setSelectedValue, setPage } = useQueryStore();
    const [items] = useState<ItemType[]>(DROP_DOWN_INITIAL_ITEMS);
    const [curSelect, setSelect] = useState<ItemType>(items[0]);
    const [isHover, setIsHover] = useState<boolean>(false);

    const handleClickItem = (item: ItemType) => {
        setPage(FIRST_PAGE)
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
