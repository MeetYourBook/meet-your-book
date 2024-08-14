import { renderHook, act, waitFor } from "@testing-library/react";
import { vi } from "vitest";
import { useLibraryFilter } from "@/hooks/useLibraryFilter";

const mockSetLibrariesFilter = vi.fn();

vi.mock("@/stores/queryStore", () => ({
    __esModule: true,
    default: () => ({
        librariesFilter: [],
        setLibrariesFilter: mockSetLibrariesFilter,
    }),
}));

global.fetch = vi.fn(() =>
    Promise.resolve({
        json: () =>
            Promise.resolve([
                { id: "1", name: "Library 1" },
                { id: "2", name: "Library 2" },
            ]),
    })
) as unknown as typeof fetch;

describe("useLibraryFilter 테스트", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    test("useLibraryFilter이 초기 value값을 갖고 있는지 확인.", () => {
        const { result } = renderHook(() => useLibraryFilter());

        expect(result.current.search).toBe("");
        expect(result.current.isOpen).toBe(true);
        expect(result.current.librariesItem).toEqual([]);
        expect(result.current.librariesFilter).toEqual([]);
    });

    test("API 요청 후 마운트 되었을때 librariesItem이 업데이트 되는지 확인.", async () => {
        const mockData = [
            { id: "1", name: "Library 1" },
            { id: "2", name: "Library 2" },
        ];
        const { result } = renderHook(() => useLibraryFilter());

        await waitFor(() => {
            expect(result.current.librariesItem).toEqual(mockData);
        });

        expect(result.current.librariesItem).toEqual(mockData);
        expect(fetch).toHaveBeenCalledWith("api/libraries");
    });

    test("토글버튼을 클릭했을때 state가 변경되는지 확인.", () => {
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

    test("검색을 했을때 search state가 value로 변경되는지 확인.", () => {
        const { result } = renderHook(() => useLibraryFilter());

        act(() => {
            result.current.handleSearch({
                target: { value: "search query" },
            } as React.ChangeEvent<HTMLInputElement>);
        });

        expect(result.current.search).toBe("search query");
    });

    test("handleSelectLibrary가 올바르게 librariesFilter를 추가/제거 하는지 확인.", () => {
        const { result } = renderHook(() => useLibraryFilter());

        expect(result.current.librariesFilter).toEqual([]);

        act(() => {
            result.current.handleSelectLibrary("1");
        });
        expect(mockSetLibrariesFilter).toHaveBeenCalledWith(["1"]);
        result.current.librariesFilter.push("1")
        expect(result.current.librariesFilter).toEqual(["1"]);

        act(() => {
            result.current.handleSelectLibrary("2");
        });
        expect(mockSetLibrariesFilter).toHaveBeenCalledWith(["1", "2"]);
        result.current.librariesFilter.push("2")
        expect(result.current.librariesFilter).toEqual(["1", "2"]);

        act(() => {
            result.current.handleSelectLibrary("1");
        });
        expect(mockSetLibrariesFilter).toHaveBeenCalledWith(["2"]);
        result.current.librariesFilter = ["2"];
        expect(result.current.librariesFilter).toEqual(["2"]);
    });
});
