import { useNavigate } from "react-router-dom";
import * as S from "@/styles/NavigationStyle";

const Logo = () => {
    const navigate = useNavigate();

    const goToMain = () => navigate("/");

    return (
        <S.LogoWrap onClick={goToMain}>
            <S.LogoName>Meet Your Book</S.LogoName>
        </S.LogoWrap>
    );
};

export default Logo