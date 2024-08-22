import { useNavigate } from "react-router-dom";
import SearchInput from "./SearchInput/SearchInput";
import * as S from "@/styles/NavigationStyle";

const Navigation = () => {
    const navigate = useNavigate();

    const goToMain = () => {
        navigate("/");
    };
    return (
        <S.NavContainer>
            <S.NavWrap>
                <S.LogoWrap onClick={goToMain}>
                    <S.LogoName>Meet Your Book</S.LogoName>
                </S.LogoWrap>
                <SearchInput/>
            </S.NavWrap>
        </S.NavContainer>
    );
};

export default Navigation;
