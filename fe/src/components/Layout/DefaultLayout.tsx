import { Outlet } from "react-router-dom";
import Logo from "../Navigation/Logo/Logo";
import SearchInput from "../Navigation/SearchInput/SearchInput";
import MobileSearchFilterPanel from "../Navigation/MobileSearchFilterPanel/MobileSearchFilterPanel";
import * as S from "@/styles/NavigationStyle";

const DefaultLayout = () => {
    return (
        <>
            <S.NavContainer>
                <S.NavWrap>
                    <Logo />
                    <S.SearchWrap>
                        <SearchInput />
                    </S.SearchWrap>
                    <MobileSearchFilterPanel />
                </S.NavWrap>
            </S.NavContainer>
            <Outlet />
        </>
    );
};

export default DefaultLayout;
