import { useState } from "react";
import { ActiveType } from "@/types/Admin";
import { ADMIN_MENU_VALUE } from "@/constants";
import * as S from "@/styles/AdinPageStyle"
const Admin = () => {
    const [activeTap, setActiveTap] = useState<ActiveType>("books");
    const [isOpen, setOpen] = useState(false);
    return (
        <S.AdminContainer>
            <S.AdminNav>
                <S.NavInfoWrap>
                    <S.MenuIcon onClick={() => setOpen(!isOpen)}/>
                    <S.CurrentActive>{activeTap}</S.CurrentActive>
                </S.NavInfoWrap>
                <S.ReloadBtn />
            </S.AdminNav>
            <S.SideBarContainer $isOpen={isOpen}>
                {ADMIN_MENU_VALUE.map((curValue: ActiveType) => (
                    <li key={curValue} onClick={() => setActiveTap(curValue)}>
                        {curValue}
                    </li>
                ))}
            </S.SideBarContainer>
        </S.AdminContainer>
    );
};

export default Admin;


