import DropDownBox from "./DropDownBox/DropDownBox";
import SearchInput from "./SearchInput/SearchInput";
import * as S from "@/styles/NavigationStyle";


const Navigation = () => {
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
            </S.NavWrap>
        </S.NavContainer>
    );
};

export default Navigation;
