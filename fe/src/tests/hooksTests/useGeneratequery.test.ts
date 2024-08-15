import { renderHook } from "@testing-library/react";
import useGenerateQuery from "@/hooks/useGenerateQuery";
import useQueryStore from "@/stores/queryStore";
import { vi } from "vitest";

vi.mock("@/stores/queryStore")

describe("useGenerateQuery 훅 테스트", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    })

    test("초기 쿼리 상태가 올바르게 설정되는지 확인.", () => {
        (useQueryStore as unknown as jest.Mock).mockReturnValue({
            searchText: "",
            page: 1,
            size: 20,
            selectedValue: "title",
            librariesFilter: [],
        })

        const { result } = renderHook(() => useGenerateQuery());

        expect(result.current).toBe("books?page=1&size=20")
    })

    test("검색어와 선택된 값이 있을 때 쿼리가 올바르게 생성되는지 확인", () => {
        (useQueryStore as unknown as jest.Mock).mockReturnValue({
            searchText: "George",
            page: 1,
            size: 20,
            selectedValue: "author",
            librariesFilter: [],
        });

        const { result } = renderHook(() => useGenerateQuery());

        expect(result.current).toBe("books?page=1&size=20&author=George");
    });

    test("librariesFilter가 있을 때 쿼리가 올바르게 생성되는지 확인", () => {
        (useQueryStore as unknown as jest.Mock).mockReturnValue({
            searchText: "",
            page: 2,
            size: 20,
            selectedValue: "all",
            librariesFilter: ["Library1", "Library2"],
        });

        const { result } = renderHook(() => useGenerateQuery());

        expect(result.current).toBe("books?page=2&size=20&libraries=Library1,Library2");
    });

    test("searchText가 있고 selectedValue가 'all'일 때 여러 필드에 검색어가 포함되는지 확인", () => {
        (useQueryStore as unknown as jest.Mock).mockReturnValue({
            searchText: "George",
            page: 1,
            size: 20,
            selectedValue: "all",
            librariesFilter: [],
        });

        const { result } = renderHook(() => useGenerateQuery());

        expect(result.current).toBe("books?page=1&size=20&title=George&author=George&publisher=George");
    });
})

