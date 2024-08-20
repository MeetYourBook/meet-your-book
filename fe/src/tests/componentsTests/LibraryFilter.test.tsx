import { render, screen, fireEvent } from "@testing-library/react";
import LibraryFilter from "@/components/FilterDisplay/LibraryFilter/LibraryFilter";
import { useLibraryFilter } from "@/hooks/useLibraryFilter";
import useDebounce from "@/hooks/useDebounce";
import { vi } from "vitest";
import { useQuery } from "@tanstack/react-query";

vi.mock("@/styles/LibraryFilterStyle", () => ({
    Container: "div",
    Header: "div",
    Title: "div",
    ListWrap: "div",
    Input: "input",
}));

vi.mock("@/hooks/useLibraryFilter", () => ({
    useLibraryFilter: vi.fn(),
}));

vi.mock("@/hooks/useDebounce", () => ({
    default: vi.fn(),
}));

vi.mock("@tanstack/react-query", () => ({
    useQuery: vi.fn(),
}));

const mockLibraries = [
    { id: "1", name: "Library One" },
    { id: "2", name: "Library Two" },
];

describe("LibraryFilter 컴포넌트 테스트", () => {
    beforeEach(() => {
        (useLibraryFilter as jest.Mock).mockReturnValue({
            isOpen: true,
            search: "",
            librariesFilter: [],
            handleSelectLibrary: vi.fn(),
            toggleFilter: vi.fn(),
            handleSearch: vi.fn(),
        });

        (useQuery as jest.Mock).mockReturnValue({
            data: mockLibraries,
            isLoading: false,
        });

        (useDebounce as jest.Mock).mockReturnValue("");
    });
    
    afterEach(() => {
        vi.clearAllMocks();
    });

    test("컴포넌트가 올바르게 렌더링되는지 확인.", () => {
        render(<LibraryFilter />);

        expect(screen.getByText("도서관 필터")).toBeInTheDocument();
        expect(
            screen.getByPlaceholderText("도서관 검색...")
        ).toBeInTheDocument();
        expect(screen.getByLabelText("Library One")).toBeInTheDocument();
        expect(screen.getByLabelText("Library Two")).toBeInTheDocument();
    });

    test("필터 토글 기능이 작동하는지 확인.", () => {
        const { toggleFilter } = useLibraryFilter();
        render(<LibraryFilter />);

        const header = screen.getByText("도서관 필터");
        fireEvent.click(header);

        expect(toggleFilter).toHaveBeenCalled();
    });

    test("검색 기능이 제대로 작동하는지 확인.", () => {
        const { handleSearch } = useLibraryFilter();
        render(<LibraryFilter />);

        const searchInput = screen.getByPlaceholderText("도서관 검색...");
        fireEvent.change(searchInput, { target: { value: "Library One" } });

        expect(handleSearch).toHaveBeenCalledWith(expect.any(Object));
    });

    test("도서관 선택 기능이 작동하는지 확인.", () => {
        const { handleSelectLibrary } = useLibraryFilter();
        render(<LibraryFilter />);

        const checkbox = screen.getByLabelText("Library One");
        fireEvent.click(checkbox);

        expect(handleSelectLibrary).toHaveBeenCalledWith("1");
    });

    test("debouncedValue에 따라 도서관 목록이 필터링되는지 확인", () => {
        const { handleSearch } = useLibraryFilter();

        render(<LibraryFilter />);

        const searchInput = screen.getByPlaceholderText("도서관 검색...");
        fireEvent.change(searchInput, { target: { value: "Library" } });

        expect(handleSearch).toHaveBeenCalledWith(expect.any(Object));

        expect(screen.getByLabelText("Library One")).toBeInTheDocument();
        expect(screen.getByLabelText("Library Two")).toBeInTheDocument();
        expect(
            screen.queryByLabelText("Another Library")
        ).not.toBeInTheDocument();
    });

    test("검색어가 없을 때 전체 도서관 목록이 표시되는지 확인", () => {
        render(<LibraryFilter />);

        expect(screen.getByLabelText("Library One")).toBeInTheDocument();
        expect(screen.getByLabelText("Library Two")).toBeInTheDocument();
    });

    test("debouncedValue가 적용되었을 때 필터링이 제대로 되는지 확인", () => {
        (useDebounce as jest.Mock).mockReturnValue("Library");

        render(<LibraryFilter />);

        expect(screen.getByLabelText("Library One")).toBeInTheDocument();
        expect(screen.getByLabelText("Library Two")).toBeInTheDocument();
        expect(
            screen.queryByLabelText("Another Library")
        ).not.toBeInTheDocument();
    });

    test("debouncedValue가 적용되었을 때 빈 결과를 반환하는지 확인", () => {
        (useDebounce as jest.Mock).mockReturnValue("Nonexistent");

        render(<LibraryFilter />);

        expect(screen.queryByLabelText("Library One")).not.toBeInTheDocument();
        expect(screen.queryByLabelText("Library Two")).not.toBeInTheDocument();
        expect(
            screen.queryByLabelText("Another Library")
        ).not.toBeInTheDocument();
    });
});

describe("LibraryFilter 컴포넌트 - 아이콘 렌더링 테스트", () => {
    it("isOpen이 true일 때 UpOutlined 아이콘이 렌더링되는지 확인", () => {
        (useLibraryFilter as jest.Mock).mockReturnValue({
            isOpen: true,
            librariesItem: [],
            search: "",
            librariesFilter: [],
            handleSelectLibrary: vi.fn(),
            toggleFilter: vi.fn(),
            handleSearch: vi.fn(),
        });

        render(<LibraryFilter />);

        expect(document.querySelector(".anticon-up")).toBeInTheDocument();
    });

    it("isOpen이 false일 때 DownOutlined 아이콘이 렌더링되는지 확인", () => {
        (useLibraryFilter as jest.Mock).mockReturnValue({
            isOpen: false,
            librariesItem: [],
            search: "",
            librariesFilter: [],
            handleSelectLibrary: vi.fn(),
            toggleFilter: vi.fn(),
            handleSearch: vi.fn(),
        });

        render(<LibraryFilter />);

        expect(document.querySelector(".anticon-down")).toBeInTheDocument();
    });
});
