import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import LibrariesPaging from "@/components/BookInfoModal/LibrariesPaging";
import { LibrariesResponseType } from "@/types/LibrariesResponse";
import { vi } from "vitest";

vi.mock("@/styles/LibrariesPagingStyle", () => ({
    Container: "div",
    Input: "input",
    LibrariesWrap: "div",
    LibraryItem: "a",
    PagingWrap: "div",
}));

const mockLibraries: LibrariesResponseType[] = [
    {id: "Id A", LibraryName: "Library A", BookLibraryUrl: "http://library-a.com" },
    {id: "Id B", LibraryName: "Library B", BookLibraryUrl: "http://library-b.com" },
    {id: "Id C", LibraryName: "Library C", BookLibraryUrl: "http://library-c.com" },
    {id: "Id D", LibraryName: "Library D", BookLibraryUrl: "http://library-d.com" },
];

describe("LibrariesPaging 컴포넌트 테스트", () => {
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

    test("초기 렌더링 시 도서관 목록이 올바르게 표시되는지 확인", () => {
        render(<LibrariesPaging libraryResponses={mockLibraries} />);

        expect(screen.getByText("Library A")).toBeInTheDocument();
        expect(screen.getByText("Library B")).toBeInTheDocument();
        expect(screen.getByText("Library C")).toBeInTheDocument();
        expect(screen.getByText("Library D")).toBeInTheDocument();
    });

    test("검색 필터링이 올바르게 동작하는지 확인", async() => {
        render(<LibrariesPaging libraryResponses={mockLibraries} />);

        const searchInput = screen.getByPlaceholderText("도서관 검색...");
        fireEvent.change(searchInput, { target: { value: "Library A" } });

        await waitFor(() => {
            expect(screen.getByText("Library A")).toBeInTheDocument();
            expect(screen.queryByText("Library B")).not.toBeInTheDocument();
            expect(screen.queryByText("Library C")).not.toBeInTheDocument();
            expect(screen.queryByText("Library D")).not.toBeInTheDocument();
        })
    });
});
