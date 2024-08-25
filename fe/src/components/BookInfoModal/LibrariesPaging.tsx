import { LibrariesType } from "@/types/Libraries";
import * as S from "@/styles/LibrariesPagingStyle";
interface LibrariesPagingProps {
    libraryResponses: LibrariesType[];
}
const LibrariesPaging = ({ libraryResponses }: LibrariesPagingProps) => {
    return (
        <S.Container>
            <p>소장 도서관: {libraryResponses.length}</p>
            <S.Input placeholder="도서관 검색..." />
            <S.LibrariesWrap>
                {libraryResponses.map((curLibrary, idx) => (
                    <S.LibraryItem key={idx}>{curLibrary.LibraryName}</S.LibraryItem>
                ))}
            </S.LibrariesWrap>
        </S.Container>
    );
};

export default LibrariesPaging;
