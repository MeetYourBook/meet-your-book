import { render, screen, waitFor } from "@testing-library/react";
import BooksDisplay from "@/components/BooksDisplay/BooksDisplay";
import { vi } from "vitest";

vi.mock("@/styles/BookDisplayStyle", () => ({
    BookContainer: 'div',
    BookWrap: 'div',
}));

const mockBooks = [
    {
        id: "1",
        title: "Book One",
        author: "Author One",
        provider: "Provider One",
        publisher: "Publisher One",
        publish_date: "2023-01-01",
        image_url: "/images/book1.jpg",
    },
    {
        id: "2",
        title: "Book Two",
        author: "Author Two",
        provider: "Provider Two",
        publisher: "Publisher Two",
        publish_date: "2023-02-01",
        image_url: "/images/book2.jpg",
    },
];

global.fetch = vi.fn(() =>
    Promise.resolve({
        json: () => Promise.resolve(mockBooks),
    })
) as unknown as typeof fetch;

describe("BooksDisplay 컴포넌트", () => {
    test("초기 렌더링 시 컴포넌트가 올바르게 표시되는지 확인", async () => {
        render(<BooksDisplay />);

        await waitFor(() => {
            expect(screen.getByText("Book One")).toBeInTheDocument();
            expect(screen.getByText("Author One")).toBeInTheDocument();
            expect(screen.getByText("Book Two")).toBeInTheDocument();
            expect(screen.getByText("Author Two")).toBeInTheDocument();
        });
    });

});
