import { fireEvent, render, screen } from "@testing-library/react";
import BookCard from "@/components/BooksDisplay/BookCard/BookCard";
import { BookContent } from "@/types/Books";
import { vi } from "vitest";
Object.defineProperty(window, "matchMedia", {
    writable: true,
    value: vi.fn().mockImplementation((query) => ({
        matches: false,
        media: query,
        onchange: null,
        addListener: vi.fn(),
        removeListener: vi.fn(),
        addEventListener: vi.fn(),
        removeEventListener: vi.fn(),
        dispatchEvent: vi.fn(),
    })),
});

vi.mock("@/components/Modal/BookInfoModal/BookInfoModal", () => ({
    default: vi.fn(({ bookData, handleModalClose }) => (
        <div data-testid="book-info-modal">
            <h2>{bookData.title}</h2>
            <p>저자: {bookData.author}</p>
            <p>제공: {bookData.provider}</p>
            <p>출판일: {bookData.publishDate}</p>
            <p>소장 도서관: {bookData.libraryResponses.length}</p>
            <button onClick={handleModalClose}>Close</button>
        </div>
    )),
}));

vi.mock("@/styles/BookCardStyle", () => ({
    GridCard: "div",
    FavoritesBtnWrap: "span",
    ListCard: "div",
    Image: "img",
    ListImage: "img",
    TextContainer: "div",
    Title: "h2",
    Subtitle: "h3",
    MetaInfo: "span",
    ListTitle: "h3",
}));

const mockBook: BookContent = {
    id: "1",
    title: "Test Book",
    author: "Test Author",
    publishDate: "Test publishDate",
    publisher: "Test Publisher",
    imageUrl: "test-image-url.com",
    libraryResponses: [],
};

describe("BookCard 컴포넌트", () => {
    test("grid 뷰 모드에서 렌더링되는지 확인", () => {
        render(<BookCard bookData={mockBook} viewMode="grid" />);

        expect(
            screen.getByRole("img", { name: /test book/i })
        ).toBeInTheDocument();
        expect(screen.getByText("Test Book")).toBeInTheDocument();
        expect(screen.getByText("Test Author")).toBeInTheDocument();
    });

    test("list 뷰 모드에서 렌더링되는지 확인", () => {
        render(<BookCard bookData={mockBook} viewMode="list" />);

        expect(
            screen.getByRole("img", { name: /test book/i })
        ).toBeInTheDocument();
        expect(screen.getByText("Test Book")).toBeInTheDocument();
        expect(screen.getByText("Test Author")).toBeInTheDocument();
        expect(screen.getByText("Test publishDate")).toBeInTheDocument();
        expect(screen.getByText("Test Publisher")).toBeInTheDocument();
    });

    test("이미지 로드 실패 시 대체 이미지를 보여주는지 확인", () => {
        render(<BookCard bookData={mockBook} viewMode="grid" />);

        const imgElement = screen.getByRole("img", { name: /test book/i });

        fireEvent.error(imgElement);

        expect(imgElement).toHaveAttribute("src", "/images/errorImg.png");
        expect(imgElement).toHaveStyle("object-fit: cover");
    });

    test("책을 클릭했을때 모달이 렌더링 되는지 확인.", () => {
        render(<BookCard bookData={mockBook} viewMode="grid" />);
        fireEvent.click(screen.getByText("Test Book"));
        expect(screen.getByText('Test Author')).toBeInTheDocument();
    });
});
