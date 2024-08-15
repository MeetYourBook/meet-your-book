import { useEffect, useState } from "react";
import { ViewType } from "@/types/View";
import { BookContent } from "@/types/Books";
import * as S from "@/styles/BookCardStyle";

interface BookItemProps {
    bookData: BookContent;
    viewMode: ViewType;
}

const BookCard = ({ bookData, viewMode }: BookItemProps) => {
    const { imageUrl, title, author, provider, publisher } = bookData;
    const [isVisible, setIsVisible] = useState(false);

    useEffect(() => {
        const timer = setTimeout(() => setIsVisible(true), 100);
        return () => clearTimeout(timer);
    }, []);

    return (
        <>
            {viewMode === "grid" ? (
                <S.GridCard $isVisible={isVisible}>
                    <S.Image
                        src={`http://${imageUrl}`}
                        alt={title}
                    />
                    <S.TextContainer $viewMode="grid">
                        <S.Title>{title}</S.Title>
                        <S.Subtitle>{author}</S.Subtitle>
                    </S.TextContainer>
                </S.GridCard>
            ) : (
                <S.ListCard $isVisible={isVisible}>
                    <S.ListImage
                        src={`http://${imageUrl}`}
                        alt={title}
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
