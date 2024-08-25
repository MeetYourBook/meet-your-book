import { render, screen, fireEvent } from "@testing-library/react";
import { describe, expect, test, vi } from "vitest";
import Navigation from "@/components/Navigation/Navigation";
import { MemoryRouter } from "react-router-dom"; // MemoryRouter를 사용
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";

vi.mock("react-router-dom", async () => {
    const actual = await vi.importActual("react-router-dom");
    return {
        ...actual,
        useNavigate: vi.fn(),
    };
});

const queryClient = new QueryClient();

describe("Layout Navigation Component 테스트", () => {
    test("Navigation Component가 렌더링 되었을때 MYB 로고가 보이는지 확인.", () => {
        render(
            <MemoryRouter>
                <QueryClientProvider client={queryClient}>
                    <Navigation />
                </QueryClientProvider>
            </MemoryRouter>
        );
        expect(screen.getByText("Meet Your Book")).toBeInTheDocument();
    });

    test("로고를 클릭했을때 '/'로 이동하는지 확인", () => {
        const navigate = vi.fn();
        (useNavigate as jest.Mock).mockReturnValue(navigate);

        render(
            <MemoryRouter>
                <QueryClientProvider client={queryClient}>
                    <Navigation />
                </QueryClientProvider>
            </MemoryRouter>
        );

        const logoElement = screen.getByText("Meet Your Book");
        fireEvent.click(logoElement);

        expect(navigate).toHaveBeenCalledWith("/");
    });
});
