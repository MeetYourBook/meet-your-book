import { vi } from "vitest";
import FilterStatusBar from "@/components/Navigation/FilterStatusBar/FilterStatusBar";
import { render, screen } from "@testing-library/react";
import useQueryStore from "@/stores/queryStore";
vi.mock("@/stores/queryStore")

describe("FilterStatusBar 컴포넌트 테스트", () => {
    beforeEach(() => {
        vi.mocked(useQueryStore).mockReturnValue({
            searchText: "",
            selectedValue: "all",
            librariesFilter: [],
            resetFilter: vi.fn(),
        })
    })
    test("초기 렌더링이 올바르게 되는지 확인.", () => {
        render(<FilterStatusBar/>)
        expect(screen.getByText("home/ books/")).toBeInTheDocument();
        expect(screen.queryByLabelText("close")).not.toBeInTheDocument();
    })

    test("검색어가 있을때 올바르게 렌더링 되는지 확인.", () => {
        vi.mocked(useQueryStore).mockReturnValue({
            searchText: "자바스크립트",
            selectedValue: "all",
            librariesFilter: [],
            resetFilter: vi.fn(),
        })
        render(<FilterStatusBar/>)
        expect(screen.getByText("home/ books/ all=자바스크립트")).toBeInTheDocument();
        expect(screen.queryByLabelText("close")).toBeInTheDocument();
    })

    test("도서관 필터가 있을때 올바르게 렌더링 되는지 확인.", () => {
        vi.mocked(useQueryStore).mockReturnValue({
            searchText: "",
            selectedValue: "all",
            librariesFilter: [{id: 1, name: "test Library1"}, {id: 2, name: "test Library2"}],
            resetFilter: vi.fn(),
        })
        render(<FilterStatusBar/>)
        expect(screen.getByText("home/ books/ libraries=test Library1,test Library2")).toBeInTheDocument();
        expect(screen.queryByLabelText("close")).toBeInTheDocument();

    })
})