import styled, { keyframes } from "styled-components";
import { CloseOutlined } from "@ant-design/icons";

const modalShow = keyframes`
    from {
        opacity: 0;
    }
    to {
        opacity: 1;
    }
`;
const PopupOverlay = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(249, 250, 251, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    animation: ${modalShow} 0.2s ease-in-out forwards;
`;

const PopupCard = styled.div`
    background-color: #ffffff;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    padding: 16px;
    max-width: 42rem;
    min-width: 300px;
`;

const CloseBtn = styled(CloseOutlined)`
    display: block;
    text-align: right;
`;

const BookInfoWrap = styled.div`
    width: 100%;
    display: flex;
    gap: 1.5rem;
    align-items: flex-end;
`;

const Img = styled.img`
    width: 150px;
    height: 250px;
    object-fit: contain;
`;

const InfoWrap = styled.div`
    width: 100%;
    height: 100%;
    margin-bottom: 20px;
`;
const Title = styled.h2`
    font-weight: bold;
    font-size: 24px;
`;

const MetaInfo = styled.p`
    margin-top: 5px;
    font-size: 14px;
    color: gray;
`;

const Description = styled.p`
    font-size: 14px;
    padding-bottom: 1rem;
    border-bottom: 1px solid #ccc;
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
