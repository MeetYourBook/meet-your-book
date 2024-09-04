import styled, { keyframes } from "styled-components";
import { CloseOutlined } from "@ant-design/icons";

const fadeIn = keyframes`
    from { opacity: 0; transform: scale(0.95); }
    to { opacity: 1; transform: scale(1); }
`;

const PopupOverlay = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
`;

const PopupCard = styled.div`
    background-color: ${({ theme }) => theme.body};
    border-radius: 16px;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
    padding: 24px;
    max-width: 450px;
    width: 90%;
    animation: ${fadeIn} 0.3s ease-out forwards;

    @media (max-width: 600px) {
        width: 300px;
    }
`;

const CloseBtn = styled(CloseOutlined)`
    position: absolute;
    top: 16px;
    right: 16px;
    font-size: 20px;
    color: #666;
    cursor: pointer;
    transition: color 0.2s;

    &:hover {
        color: #000;
    }
`;

const BookInfoWrap = styled.div`
    display: flex;
    gap: 24px;
    margin-bottom: 24px;
    align-items: flex-end;
`;

const Img = styled.img`
    width: 150px;
    height: 250px;
    object-fit: cover;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
`;

const InfoWrap = styled.div`
    flex: 1;
`;

const Title = styled.h2`
    font-size: 24px;
    font-weight: 700;
    margin-bottom: 12px;
`;

const MetaInfo = styled.p`
    font-size: 14px;
    margin: 4px 0;
`;

const Description = styled.p`
    font-size: 16px;
    line-height: 1.6;
    margin-bottom: 24px;
    padding-bottom: 24px;
    border-bottom: 1px solid #eee;
`;
export {
    PopupOverlay,
    PopupCard,
    CloseBtn,
    BookInfoWrap,
    Img,
    InfoWrap,
    Title,
    MetaInfo,
    Description,
};
