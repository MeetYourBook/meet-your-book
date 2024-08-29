import { render, screen, fireEvent } from "@testing-library/react";
import LibraryFilter from "@/components/FilterDisplay/LibraryFilter/LibraryFilter";
import { useLibraryFilter } from "@/hooks/useLibraryFilter";
import { vi } from "vitest";
import { LibrariesType } from "@/types/Libraries";

vi.mock("@/hooks/useLibraryFilter", () => ({
    useLibraryFilter: vi.fn(),
}));

vi.mock("@/components/LibraryList/LibraryList", () => ({
    default: vi.fn().mockReturnValue(<div data-testid="library-list" />),
}));

vi.mock("@/components/LoadingFallBack/LoadingFallBack", () => ({
    default: vi.fn().mockReturnValue(<div data-testid="loading-fallback" />),
}));

const mockLibraries: LibrariesType[] = [
    { id: "1", name: "Library One" },
    { id: "2", name: "Library Two" },
];

describe("LibraryFilter 컴포넌트 테스트", () => {
    beforeEach(() => {
        (useLibraryFilter as jest.Mock).mockReturnValue({
            isOpen: true,
            searchValue: "",
            librariesFilter: [],
            handleSelectLibrary: vi.fn(),
            toggleFilter: vi.fn(),
            handleSearch: vi.fn(),
            isLoading: false,
            filteredLibraries: mockLibraries,
        });
    });

    afterEach(() => {
        vi.clearAllMocks();
    });

    test("컴포넌트가 올바르게 렌더링되는지 확인", () => {
        render(<LibraryFilter />);

        expect(screen.getByText("도서관 필터")).toBeInTheDocument();
        expect(screen.getByPlaceholderText("도서관 검색...")).toBeInTheDocument();
    });

    test("필터 토글 기능이 작동하는지 확인", () => {
        const mockToggleFilter = vi.fn();
        (useLibraryFilter as jest.Mock).mockReturnValue({
            ...useLibraryFilter(),
            toggleFilter: mockToggleFilter,
        });

        render(<LibraryFilter />);

        const header = screen.getByText("도서관 필터");
        fireEvent.click(header);

        expect(mockToggleFilter).toHaveBeenCalled();
    });

    test("검색 기능이 제대로 작동하는지 확인", () => {
        const mockHandleSearch = vi.fn();
        (useLibraryFilter as jest.Mock).mockReturnValue({
            ...useLibraryFilter(),
            handleSearch: mockHandleSearch,
        });

        render(<LibraryFilter />);

        const searchInput = screen.getByPlaceholderText("도서관 검색...");
        fireEvent.change(searchInput, { target: { value: "Library One" } });

        expect(mockHandleSearch).toHaveBeenCalledWith(expect.any(Object));
    });

    test("로딩 중일 때 LoadingFallBack이 표시되는지 확인", () => {
        (useLibraryFilter as jest.Mock).mockReturnValue({
            ...useLibraryFilter(),
            isLoading: true,
        });

        render(<LibraryFilter />);

        expect(screen.getByTestId("loading-fallback")).toBeInTheDocument();
    });

    test("isOpen이 true일 때 UpOutlined 아이콘이 렌더링되는지 확인", () => {
        render(<LibraryFilter />);

        expect(document.querySelector(".anticon-up")).toBeInTheDocument();
    });

    test("isOpen이 false일 때 DownOutlined 아이콘이 렌더링되는지 확인", () => {
        (useLibraryFilter as jest.Mock).mockReturnValue({
            ...useLibraryFilter(),
            isOpen: false,
        });

        render(<LibraryFilter />);

        expect(document.querySelector(".anticon-down")).toBeInTheDocument();
    });
});