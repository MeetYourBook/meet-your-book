import { render, screen } from "@testing-library/react";
import BookCard from "@/components/BooksDisplay/BookCard/BookCard";
import { BookContent } from "@/types/Books";
import { vi } from "vitest";

vi.mock("@/styles/BookCardStyle", () => ({
    GridCard: 'div',
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

});
