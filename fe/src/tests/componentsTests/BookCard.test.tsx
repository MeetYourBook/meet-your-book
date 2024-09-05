import { fireEvent, render, screen } from "@testing-library/react";
import BookCard from "@/components/BooksDisplay/BookCard/BookCard";
import { BookContent } from "@/types/Books";
import { vi } from "vitest";

vi.mock("@/styles/BookCardStyle", () => ({
    GridCard: 'div',
    FavoritesBtnWrap: "span",
    ListCard: 'div',
    Image: 'img',
    ListImage: 'img',
    TextContainer: 'div',
    Title: 'h2',
    Subtitle: 'h3',
    MetaInfo: 'span',
}));

const mockBook: BookContent = {
    id: "1",
    title: "Test Book",
    author: "Test Author",
    provider: "Test Provider",
    publisher: "Test Publisher",
    imageUrl: "test-image-url.com",
    libraryResponses: [],
};

describe("BookCard 컴포넌트", () => {
    test("grid 뷰 모드에서 렌더링되는지 확인", () => {
        render(<BookCard bookData={mockBook} viewMode="grid" />);

        expect(screen.getByRole("img", { name: /test book/i })).toBeInTheDocument();
        expect(screen.getByText("Test Book")).toBeInTheDocument();
        expect(screen.getByText("Test Author")).toBeInTheDocument();
    });

    test("list 뷰 모드에서 렌더링되는지 확인", () => {
        render(<BookCard bookData={mockBook} viewMode="list" />);

        expect(screen.getByRole("img", { name: /test book/i })).toBeInTheDocument();
        expect(screen.getByText("Test Book")).toBeInTheDocument();
        expect(screen.getByText("Test Author")).toBeInTheDocument();
        expect(screen.getByText("Test Provider")).toBeInTheDocument();
        expect(screen.getByText("Test Publisher")).toBeInTheDocument();
    });

    test("이미지 로드 실패 시 대체 이미지를 보여주는지 확인", () => {
        render(<BookCard bookData={mockBook} viewMode="grid" />);
        
        const imgElement = screen.getByRole("img", { name: /test book/i });

        fireEvent.error(imgElement);

        expect(imgElement).toHaveAttribute("src", "/images/errorImg.png");
        expect(imgElement).toHaveStyle("object-fit: cover");
    });
});
