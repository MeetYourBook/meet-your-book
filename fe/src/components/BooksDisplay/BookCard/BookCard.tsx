import { useEffect, useState } from "react";
import { ViewType } from "@/types/View";
import { BookContent } from "@/types/Books";
import * as S from "@/styles/BookCardStyle";
import { ANIMATION_TIME } from "@/constants";
import BookInfoModal from "@/components/Modal/BookInfoModal/BookInfoModal";
import { handleImageError } from "@/utils";
import FavoriteBtn from "@/components/FavoriteBtn/FavoriteBtn";
interface BookItemProps {
    bookData: BookContent;
    viewMode: ViewType;
}

const BookCard = ({ bookData, viewMode }: BookItemProps) => {
    const { imageUrl, title, author, provider, publisher } = bookData;
    const [isVisible, setIsVisible] = useState(false);
    const [isModalOpen, setModalOpen] = useState(false)

    const handleModalOpen = () => setModalOpen(true)

    const handleModalClose = () => setModalOpen(false)

    useEffect(() => {
        const timer = setTimeout(() => setIsVisible(true), ANIMATION_TIME);
        return () => clearTimeout(timer);
    }, []);

    return (
        <>
            {viewMode === "grid" ? (
                <S.GridCard $isVisible={isVisible} onClick={handleModalOpen}>
                    <S.FavoritesBtnWrap>
                        <FavoriteBtn item={bookData} storageName="books"/>
                    </S.FavoritesBtnWrap>
                    <S.Image
                        src={`//${imageUrl}`}
                        alt={title}
                        onError={handleImageError}
                        loading="lazy"
                    />
                    <S.TextContainer $viewMode="grid">
                        <S.Title>{title}</S.Title>
                        <S.Subtitle>{author}</S.Subtitle>
                    </S.TextContainer>
                </S.GridCard>
            ) : (
                <S.ListCard $isVisible={isVisible} onClick={handleModalOpen}>
                    <S.Image
                        src={`//${imageUrl}`}
                        alt={title}
                        onError={handleImageError}
                    />
                    <S.TextContainer $viewMode="list" >
                        <S.Title>{title}</S.Title>
                        <S.Subtitle>{author}</S.Subtitle>
                        <div>
                            <S.MetaInfo>{provider}</S.MetaInfo>
                            <S.MetaInfo>{publisher}</S.MetaInfo>
                        </div>
                    </S.TextContainer>
                </S.ListCard>
            )}
            {isModalOpen && <BookInfoModal bookData={bookData} handleModalClose={handleModalClose}/>}
        </>
    );
};

export default BookCard;
