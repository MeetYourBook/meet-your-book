import { useNavigate } from "react-router-dom";
import SearchInput from "./SearchInput/SearchInput";
import * as S from "@/styles/NavigationStyle";
import { MobileSearchFilterPanel } from "./MobileSearchFilterPanel/MobileSearchFilterPanel";

export const Logo = () => {
    const navigate = useNavigate();

    const goToMain = () => {
        navigate("/");
    };
    return (
        <S.LogoWrap onClick={goToMain}>
            <S.LogoName>Meet Your Book</S.LogoName>
        </S.LogoWrap>
    );
};

const Navigation = () => {
    return (
        <S.NavContainer>
            <S.NavWrap>
                <Logo/>
                <S.SearchWrap>
                    <SearchInput />
                </S.SearchWrap>
                <MobileSearchFilterPanel />
            </S.NavWrap>
        </S.NavContainer>
    );
};

export default Navigation;
