import { useEffect, useState } from "react";
import { ViewType } from "@/types/View";
import { BookContent } from "@/types/Books";
import * as S from "@/styles/BookCardStyle";
import { ANIMATION_TIME } from "@/constants";
interface BookItemProps {
    bookData: BookContent;
    viewMode: ViewType;
}

const BookCard = ({ bookData, viewMode }: BookItemProps) => {
    const { imageUrl, title, author, provider, publisher } = bookData;
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const timer = setTimeout(() => setIsVisible(true), ANIMATION_TIME);
        return () => clearTimeout(timer);
    }, []);

    const handleImageError = (e: React.SyntheticEvent<HTMLImageElement>) => {
        e.currentTarget.src = "/images/errorImg.png";
        e.currentTarget.style.objectFit = "cover"; 
    };

    return (
        <>
            {viewMode === "grid" ? (
                <S.GridCard $isVisible={isVisible}>
                    <S.Image
                        src={`http://${imageUrl}`}
                        alt={title}
                        onError={handleImageError}
                    />
                    <S.TextContainer $viewMode="grid">
                        <S.Title>{title}</S.Title>
                        <S.Subtitle>{author}</S.Subtitle>
                    </S.TextContainer>
                </S.GridCard>
            ) : (
                <S.ListCard $isVisible={isVisible}>
                    <S.Image
                        src={`http://${imageUrl}11`}
                        alt={title}
                        onError={handleImageError}
                    />
                    <S.TextContainer $viewMode="list">
                        <S.Title>{title}</S.Title>
                        <S.Subtitle>{author}</S.Subtitle>
                        <div>
                            <S.MetaInfo>{provider}</S.MetaInfo>
                            <S.MetaInfo>{publisher}</S.MetaInfo>
                        </div>
                    </S.TextContainer>
                </S.ListCard>
            )}
        </>
    );
};

export default BookCard;
