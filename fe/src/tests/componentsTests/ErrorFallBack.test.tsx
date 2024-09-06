import ErrorFallBack from "@/components/ErrorFallBack/ErrorFallBack";
import { fireEvent, render, screen } from "@testing-library/react";
import { vi } from "vitest";

const mockReload = vi.fn();
Object.defineProperty(window, "location", {
    value: { reload: mockReload },
    writable: true,
});

describe("ErrorFallBack 컴포넌트 테스트", () => {
    beforeEach(() => {
        render(<ErrorFallBack />);
    });

    test("에러 메시지가 보이는지 확인.", () => {
        expect(screen.getByText("오류가 발생했습니다!")).toBeInTheDocument();
        expect(
            screen.getByText(
                "죄송합니다. 예기치 못한 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
            )
        ).toBeInTheDocument();
    });

    test("다시 시도하기 버튼이 동작되는지 확인.", () => {
        const retryButton = screen.getByText("다시 시도하기");
        fireEvent.click(retryButton);
        expect(mockReload).toHaveBeenCalledTimes(1)
    })
});
