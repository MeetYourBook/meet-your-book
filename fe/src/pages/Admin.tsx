import { useState } from "react";
import { ActiveType } from "@/types/Admin";
import { ADMIN_MENU_VALUE } from "@/constants";
import * as S from "@/styles/AdinPageStyle";
import ItemManager from "@/components/Admin/ItmeManager/ItemManager";
import ThemeSwitcher from "@/components/Navigation/ThemeSwitcher/ThemeSwitcher";
const Admin = () => {
    const [activeTap, setActiveTap] = useState<ActiveType>("books");
    const [isOpen, setIsOpen] = useState(false);
    return (
        <S.AdminContainer>
            <S.AdminNav>
                <S.NavInfoWrap>
                    <S.MenuIcon onClick={() => setIsOpen(!isOpen)} />
                    <S.CurrentActive>{activeTap}</S.CurrentActive>
                </S.NavInfoWrap>
                <S.IconWrap>
                    <S.ReloadBtn />
                    <ThemeSwitcher />
                </S.IconWrap>
            </S.AdminNav>
            <S.SideBarContainer $isOpen={isOpen}>
                {ADMIN_MENU_VALUE.map((curValue: ActiveType) => (
                    <S.SideBarItem
                        key={curValue}
                        onClick={() => setActiveTap(curValue)}
                    >
                        {curValue}
                    </S.SideBarItem>
                ))}
            </S.SideBarContainer>
            <ItemManager />
        </S.AdminContainer>
    );
};

export default Admin;
