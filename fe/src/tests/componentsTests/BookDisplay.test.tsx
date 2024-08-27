import { render, screen, waitFor } from "@testing-library/react";
import BooksDisplay from "@/components/BooksDisplay/BooksDisplay";
import { vi } from "vitest";
import useQueryData from "@/hooks/useQueryData";

vi.mock("@/styles/BookDisplayStyle", () => ({
    BookContainer: "div",
    BookWrap: "div",
    LastPageView: "div"
}));

vi.mock("@/hooks/useQueryData", () => ({
    default: vi.fn(),
}));

const mockBooks = {
    content: [
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
    ],
    totalPages: 3,
};

beforeAll(() => {
    class MockIntersectionObserver {
        private callback: (entries: IntersectionObserverEntry[], observer: IntersectionObserver) => void;
    
        constructor(callback: (entries: IntersectionObserverEntry[], observer: IntersectionObserver) => void) {
            this.callback = callback;
        }
    
        observe() {
            const entries: IntersectionObserverEntry[] = [
                {
                    isIntersecting: true,
                    target: document.createElement('div'),
                    intersectionRatio: 1,
                    time: 0,
                    boundingClientRect: {} as DOMRectReadOnly,
                    intersectionRect: {} as DOMRectReadOnly,
                    rootBounds: null,
                }
            ];
            this.callback(entries, this as unknown as IntersectionObserver);
        }
    
        disconnect() {
            return null;
        }
    
        unobserve() {
            return null;
        }
    }
    
    Object.defineProperty(window, "IntersectionObserver", {
        writable: true,
        configurable: true,
        value: MockIntersectionObserver,
    });
});

describe("BooksDisplay 컴포넌트", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    test("초기 렌더링 시 컴포넌트가 올바르게 표시되는지 확인", async () => {
        (useQueryData as jest.Mock).mockReturnValue({
            data: mockBooks,
            isLoading: false,
        });

        render(<BooksDisplay />);

        await waitFor(() => {
            expect(screen.getByText("Book One")).toBeInTheDocument();
            expect(screen.getByText("Author One")).toBeInTheDocument();
            expect(screen.getByText("Book Two")).toBeInTheDocument();
            expect(screen.getByText("Author Two")).toBeInTheDocument();
        });
    });

    test("로딩 중일 때 Spin 컴포넌트가 화면에 표시되는지 확인", async () => {
        (useQueryData as jest.Mock).mockReturnValue({
            data: null,
            isLoading: true,
        });

        render(<BooksDisplay />);

        const spinElement = document.querySelector('.ant-spin');
        expect(spinElement).toBeInTheDocument();
    });

    test("스크롤하여 페이지가 증가하는지 확인", async () => {
        let currentPage = 1;
        (useQueryData as jest.Mock).mockReturnValue(() => {
            return {
                data: {
                    ...mockBooks,
                    content: mockBooks.content.slice(0, currentPage),
                },
                isLoading: false,
                isFetching: false,
            };
        });

        const { container } = render(<BooksDisplay />);

        await waitFor(() => {
            expect(screen.getByText("Book One")).toBeInTheDocument();
        });

        const observerDiv = container.querySelector('div[style="height: 10px;"]');
        expect(observerDiv).toBeInTheDocument();

        currentPage = 2;
        observerDiv && (new IntersectionObserver(() => {}).observe(observerDiv));

        await waitFor(() => {
            expect(screen.getByText("Book Two")).toBeInTheDocument();
        });
    });
});
