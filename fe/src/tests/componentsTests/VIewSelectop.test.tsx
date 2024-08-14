import { render, screen, fireEvent } from "@testing-library/react";
import ViewSelector from "@/components/BooksDisplay/ViewSelector/ViewSelector";
import { vi } from "vitest";

vi.mock("@/styles/ViewSelectorStyle", () => ({
    Container: 'div',
    Title: 'h1',
    IconButtonGroup: 'div',
    ListButton: 'button',
    GridButton: 'button',
}));

describe("ViewSelector 컴포넌트 테스트", () => {
    test("grid 뷰가 활성화된 상태로 제대로 렌더링되는지 확인", () => {
        const setViewMode = vi.fn();
        render(<ViewSelector viewMode="grid" setViewMode={setViewMode} />);

        expect(screen.getByText("Books")).toBeInTheDocument();
    });

    test("리스트 버튼을 클릭하면 'list'로 setViewMode가 호출되는지 확인", () => {
        const setViewMode = vi.fn();
        render(<ViewSelector viewMode="grid" setViewMode={setViewMode} />);

        const listButton = screen.getByRole("button", { name: /menu/i });
        fireEvent.click(listButton);

        expect(setViewMode).toHaveBeenCalledWith("list");
    });

    test("그리드 버튼을 클릭하면 'grid'로 setViewMode가 호출되는지 확인", () => {
        const setViewMode = vi.fn();
        render(<ViewSelector viewMode="list" setViewMode={setViewMode} />);

        const gridButton = screen.getByRole("button", { name: /appstore/i });
        fireEvent.click(gridButton);

        expect(setViewMode).toHaveBeenCalledWith("grid");
    });
});
