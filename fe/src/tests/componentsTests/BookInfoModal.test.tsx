import { render, screen, fireEvent } from "@testing-library/react";
import BookInfoModal from "@/components/Modal/BookInfoModal/BookInfoModal";
import { vi } from "vitest";
import { BookContent } from "@/types/Books";

vi.mock("@/styles/BookInfoModalStyle", () => ({
    PopupOverlay: "div",
    PopupCard: "div",
    CloseBtn: "button",
    BookInfoWrap: "div",
    Img: "img",
    InfoWrap: "div",
    Title: "h2",
    MetaInfo: "p",
    Description: "p",
}));

const mockBookData: BookContent = {
    id: "1",
    title: "Test Book",
    author: "Test Author",
    publishDate: "Test PublishDate",
    publisher: "Test Publisher",
    imageUrl: "test-image-url.jpg",
    libraryResponses: [],
};

describe("BookInfoModal 컴포넌트 테스트", () => {
    const handleModalClose = vi.fn();

    beforeAll(() => {
        Object.defineProperty(window, "matchMedia", {
            writable: true,
            value: vi.fn().mockImplementation(query => ({
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
    });

    test("책 정보가 올바르게 표시되는지 확인", () => {
        render(<BookInfoModal bookData={mockBookData} handleModalClose={handleModalClose} />);

        expect(screen.getByText("Test Book")).toBeInTheDocument();
        expect(screen.getByText("저자: Test Author")).toBeInTheDocument();
        expect(screen.getByText("출판일: Test PublishDate")).toBeInTheDocument();
        expect(screen.getByText("출판사: Test Publisher")).toBeInTheDocument();

        const bookImage = screen.getByAltText("Test Book");
        expect(bookImage).toBeInTheDocument();
        expect(bookImage).toHaveAttribute("src", "http://test-image-url.jpg");
    });

    test("모달 외부를 클릭하면 모달이 닫히는지 확인", () => {
        render(<BookInfoModal bookData={mockBookData} handleModalClose={handleModalClose} />);

        fireEvent.mouseDown(document.body);

        expect(handleModalClose).toHaveBeenCalledTimes(1);
    });
});
