import { renderHook, act } from "@testing-library/react";
import { vi } from "vitest";
import { useLibraryFilter } from "@/hooks/useLibraryFilter";

const mockSetLibrariesFilter = vi.fn();
const mockSetPage = vi.fn();

vi.mock("@/stores/queryStore", () => ({
    __esModule: true,
    default: () => ({
        librariesFilter: [],
        setLibrariesFilter: mockSetLibrariesFilter,
        setPage: mockSetPage,
    }),
}));

const mockLibraries = [
    { id: 1, name: "Library 1" },
    { id: 2, name: "Library 2" },
    { id: 3, name: "Library 3" },
];

vi.mock('@/hooks/queries/useLibrariesQuery', () => ({
    default: () => ({
        data: mockLibraries,
        isLoading: false,
    }),
}));

vi.mock('@/hooks/useInfiniteScroll', () => ({
    default: () => ({
        observe: vi.fn(),
    }),
}));

describe("useLibraryFilter 테스트", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    test("초기 상태 확인", () => {
        const { result } = renderHook(() => useLibraryFilter());

        expect(result.current.isOpen).toBe(true);
        expect(result.current.isLoading).toBe(false);
    });

    test("toggleFilter 기능 확인", () => {
        const { result } = renderHook(() => useLibraryFilter());

        act(() => {
            result.current.toggleFilter();
        });
        expect(result.current.isOpen).toBe(false);

        act(() => {
            result.current.toggleFilter();
        });
        expect(result.current.isOpen).toBe(true);
    });
});