import { useNavigate } from "react-router-dom";
import DropDownBox from "./DropDownBox/DropDownBox";
import SearchInput from "./SearchInput/SearchInput";
import * as S from "@/styles/NavigationStyle";


const Navigation = () => {
    const navigate = useNavigate()

    const goToLogin = () => {
        navigate("/Login")
    }
    return (
        <S.NavContainer>
            <S.NavWrap>
                <S.LogoWrap>
                    <S.LogoAbbreviation>MYB</S.LogoAbbreviation>
                    <S.LogoFullName>MeetYourBook</S.LogoFullName>
                </S.LogoWrap>
                <S.InputWrap>
                    <DropDownBox/>
                    <SearchInput/>
                </S.InputWrap>
                <S.LoginBtn onClick={goToLogin}>로그인</S.LoginBtn>
            </S.NavWrap>
        </S.NavContainer>
    );
};

export default Navigation;
