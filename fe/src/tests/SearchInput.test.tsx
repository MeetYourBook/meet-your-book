import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import userEvent from "@testing-library/user-event";
import { vi } from "vitest";
import SearchInput from "@/components/Navigation/SearchInput/SearchInput";

describe("SearchInput 컴포넌트 테스트", () => {
    it("인풋 컴포넌트가 렌더링 되었을대 플레이스 홀더 텍스트와 버튼이 렌더링 되는지 확인.", () => {
        render(<SearchInput />);
        expect(
            screen.getByPlaceholderText("Search For Book...")
        ).toBeInTheDocument();
        expect(screen.getByRole("button")).toBeInTheDocument();
    });

    it("인풋에 검색했을때 검색한 텍스트가 보이는지 확인.", async () => {
        render(<SearchInput />);
        const input = screen.getByPlaceholderText("Search For Book...");
        await userEvent.type(input, "React");
        expect(input).toHaveValue("React");
    });

    it("버튼을 눌렀을때 콜백함수가 실행되지는 확인.", async () => {
        render(<SearchInput />);
        const input = screen.getByPlaceholderText("Search For Book...");
        const button = screen.getByRole("button");

        const logSpy = vi.spyOn(console, "log").mockImplementation(() => {});

        await userEvent.type(input, "React");
        await userEvent.click(button);

        expect(logSpy).toHaveBeenCalledWith("React");
        logSpy.mockRestore();
    });

    it("Enter 키 입력 시 handleSearchClick 호출 확인", async () => {
        render(<SearchInput />);
        const input = screen.getByPlaceholderText("Search For Book...");

        const logSpy = vi.spyOn(console, "log").mockImplementation(() => {});

        await userEvent.type(input, "React{enter}");

        expect(logSpy).toHaveBeenCalledWith("React");
        logSpy.mockRestore();
    });
});
