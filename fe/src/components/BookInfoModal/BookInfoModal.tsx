import { BookContent } from "@/types/Books";
import * as S from "@/styles/BookInfoModalStyle";
import LibrariesPaging from "./LibrariesPaging";
interface BookInfoModalProps {
    bookData: BookContent;
    handleModalClose: () => void;
}

const BookInfoModal = ({ bookData, handleModalClose }: BookInfoModalProps) => {
    const { imageUrl, title, author, provider, publisher, libraryResponses } = bookData;
    console.log(libraryResponses)
    return (
        <S.PopupOverlay>
            <S.PopupCard>
                <S.CloseBtn onClick={handleModalClose} />
                <S.BookInfoWrap>
                    <S.Img src={`http://${imageUrl}`} alt={title} />
                    <S.InfoWrap>
                        <S.Title>{title}</S.Title>
                        <S.MetaInfo>저자: {author}</S.MetaInfo>
                        <S.MetaInfo>제공: {provider}</S.MetaInfo>
                        <S.MetaInfo>출판사: {publisher}</S.MetaInfo>
                    </S.InfoWrap>
                </S.BookInfoWrap>
                <S.Description>디스크립션 태그입니다.</S.Description>
                <LibrariesPaging libraryResponses={libraryResponses}/>
            </S.PopupCard>
        </S.PopupOverlay>
    );
};

export default BookInfoModal;
