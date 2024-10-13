import { Outlet } from "react-router-dom";
import Logo from "../Navigation/Logo/Logo";
import SearchInput from "../Navigation/SearchInput/SearchInput";
import MobileSearchFilterPanel from "../Navigation/MobileSearchFilterPanel/MobileSearchFilterPanel";
import * as S from "@/styles/NavigationStyle";
import ThemeSwitcher from "../Navigation/ThemeSwitcher/ThemeSwitcher";
import FilterStatusBar from "../Navigation/FilterStatusBar/FilterStatusBar";
import ErrorBoundary from "../ErrorBoundary/ErrorBoundary";
import ErrorFallBack from "../ErrorFallBack/ErrorFallBack";

const DefaultLayout = () => {
    return (
        <>
            <ErrorBoundary
                fallback={ErrorFallBack}
                onReset={() => window.location.reload()}
            >
                <S.NavContainer>
                    <S.NavWrap>
                        <Logo />
                        <S.SearchWrap>
                            <SearchInput />
                        </S.SearchWrap>
                        <MobileSearchFilterPanel />
                        <S.ThemeSwitcherWrap>
                            <ThemeSwitcher />
                        </S.ThemeSwitcherWrap>
                    </S.NavWrap>
                </S.NavContainer>
                <FilterStatusBar />
                <Outlet />
            </ErrorBoundary>
        </>
    );
};

export default DefaultLayout;
