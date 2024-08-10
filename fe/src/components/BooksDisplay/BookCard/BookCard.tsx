import { useEffect, useState } from "react";
import { ViewType, Book } from "../BooksDisplay";
import * as S from "@/styles/BookCardStyle"

interface BookItemProps {
    bookData: Book;
    viewMode: ViewType;
}

const BookCard = ({ bookData, viewMode }: BookItemProps) => {
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
                        src={`http://${bookData.image_url}`}
                        alt={bookData.title}
                    />
                    <S.TextContainer $viewMode="grid">
                        <S.Title>{bookData.title}</S.Title>
                        <S.Subtitle>{bookData.author}</S.Subtitle>
                    </S.TextContainer>
                </S.GridCard>
            ) : (
                <S.ListCard $isVisible={isVisible}>
                    <S.ListImage
                        src={`http://${bookData.image_url}`}
                        alt={bookData.title}
                    />
                    <S.TextContainer $viewMode="list">
                        <S.Title isList>{bookData.title}</S.Title>
                        <S.Subtitle isList>{bookData.author}</S.Subtitle>
                        <div>
                            <S.MetaInfo>{bookData.provider}</S.MetaInfo>
                            <S.MetaInfo>{bookData.publisher}</S.MetaInfo>
                            <S.MetaInfo>{bookData.publish_date}</S.MetaInfo>
                        </div>
                    </S.TextContainer>
                </S.ListCard>
            )}
        </>
    );
};

export default BookCard;
