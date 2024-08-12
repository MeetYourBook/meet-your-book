import { renderHook, act } from "@testing-library/react";
import useDebounce from "@/hooks/useDebounce";
import { vi } from "vitest";

describe("useDebounce 테스트", () => {
    vi.useFakeTimers();

    test("debounceValue 초기값이 업데이트 되는지 확인", () => {
        const { result } = renderHook(() => useDebounce("initial", 300));
        expect(result.current).toBe("initial");
    });

    test("딜레이 후 value가 업데이트 되는지 확인", () => {
        const { result, rerender } = renderHook(
            ({ value, delay }) => useDebounce(value, delay),
            {
                initialProps: { value: "initial", delay: 500 },
            }
        );

        rerender({ value: "updated", delay: 500 });
        expect(result.current).toBe("initial");

        act(() => {
            vi.advanceTimersByTime(500);
        });

        expect(result.current).toBe("updated");
    });

    afterAll(() => {
        vi.clearAllTimers();
    });
});
