import { BookContent } from "@/types/Books";
import * as S from "@/styles/BookInfoModalStyle";
import LibrariesPaging from "../LibrariesPaging/LibrariesPaging";
import { useRef } from "react";
import useOnClickOutside from "@/hooks/useOnClickOutside";
import { handleImageError } from "@/utils";
interface BookInfoModalProps {
    bookData: BookContent;
    handleModalClose: () => void;
}

const BookInfoModal = ({ bookData, handleModalClose }: BookInfoModalProps) => {
    const inSideRef = useRef(null)
    const { imageUrl, title, author, publisher, publishDate, libraryResponses } = bookData;
    
    useOnClickOutside(inSideRef, handleModalClose)
    
    return (
        <S.PopupOverlay>
            <S.PopupCard ref={inSideRef}>
                <S.CloseBtn onClick={handleModalClose} />
                <S.BookInfoWrap>
                    <S.Img src={`http://${imageUrl}`} alt={title} onError={handleImageError}/>
                    <S.InfoWrap>
                        <S.Title>{title}</S.Title>
                        <S.MetaInfo>저자: {author}</S.MetaInfo>
                        <S.MetaInfo>출판일: {publishDate}</S.MetaInfo>
                        <S.MetaInfo>출판사: {publisher}</S.MetaInfo>
                    </S.InfoWrap>
                </S.BookInfoWrap>
                <LibrariesPaging libraryResponses={libraryResponses}/>
            </S.PopupCard>
        </S.PopupOverlay>
    );
};

export default BookInfoModal;
