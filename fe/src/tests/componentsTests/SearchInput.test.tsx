import { render, screen, fireEvent } from "@testing-library/react";
import SearchInput from "@/components/Navigation/SearchInput/SearchInput";
import { vi } from "vitest";

const mockSetSearchText = vi.fn();
vi.mock("@/stores/queryStore", () => ({
    __esModule: true,
    default: () => ({
        setSearchText: mockSetSearchText,
    }),
}));

describe("SearchInput 컴포넌트 테스트", () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    test("검색 아이콘을 클릭하면 입력값으로 setSearchText가 호출되는지 확인.", () => {
        render(<SearchInput/>)

        const inputElement = screen.getByPlaceholderText("Search For Book...") as HTMLInputElement;
        const buttonElement = screen.getByRole("button");

        fireEvent.change(inputElement, {target: {value: "test search"}})
        fireEvent.click(buttonElement)

        expect(mockSetSearchText).toHaveBeenCalledWith("test search");
    });

    test("Enter 키를 누르면 입력값으로 setSearchText가 호출되는지 확인", () => {
        render(<SearchInput />);

        const inputElement = screen.getByPlaceholderText("Search For Book...") as HTMLInputElement;

        fireEvent.change(inputElement, { target: { value: "test search" } });

        fireEvent.keyDown(inputElement, { key: "Enter", code: "Enter", charCode: 13 });

        expect(mockSetSearchText).toHaveBeenCalledWith("test search");
    });
});
