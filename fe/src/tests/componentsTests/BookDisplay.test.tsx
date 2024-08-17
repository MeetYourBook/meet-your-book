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
        (useQueryData as jest.Mock).mockReturnValue({
            data: mockBooks,
            isLoading: false,
        });
    });

    afterEach(() => {
        vi.clearAllMocks();
    });

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
