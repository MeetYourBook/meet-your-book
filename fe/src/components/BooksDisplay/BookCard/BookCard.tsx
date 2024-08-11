import { useEffect, useState } from "react";
import { ViewType, Book } from "../BooksDisplay";
import * as S from "@/styles/BookCardStyle";

interface BookItemProps {
    bookData: Book;
    viewMode: ViewType;
}

const BookCard = ({ bookData, viewMode }: BookItemProps) => {
    const { image_url, title, author, provider, publisher, publish_date } = bookData;
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
                        src={`http://${image_url}`}
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
                        src={`http://${image_url}`}
                        alt={title}
                    />
                    <S.TextContainer $viewMode="list">
                        <S.Title isList>{title}</S.Title>
                        <S.Subtitle isList>{author}</S.Subtitle>
                        <div>
                            <S.MetaInfo>{provider}</S.MetaInfo>
                            <S.MetaInfo>{publisher}</S.MetaInfo>
                            <S.MetaInfo>{publish_date}</S.MetaInfo>
                        </div>
                    </S.TextContainer>
                </S.ListCard>
            )}
        </>
    );
};

export default BookCard;
