import { useNavigate } from "react-router-dom";
import { ArrowLeftOutlined } from "@ant-design/icons";
import styled from "styled-components";

const BackButton = () => {
    const navigate = useNavigate();

    const handleGoBack = () => {
        navigate(-1);
    };

    return (
        <BackButtonWrapper onClick={handleGoBack}>
            <ArrowLeftOutlined />
        </BackButtonWrapper>
    );
};

export default BackButton;

const BackButtonWrapper = styled.div`
    cursor: pointer;
    display: flex;
    align-items: center;
`;
