import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import DefaultLayout from "@/components/Layout/DefaultLayout";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ThemeContextProvider } from "@/hooks/useThemeContext";
import { vi } from "vitest";

const queryClient = new QueryClient();

const mockIntersectionObserver = vi.fn();
mockIntersectionObserver.mockReturnValue({
    observe: () => null,
    unobserve: () => null,
    disconnect: () => null,
});
window.IntersectionObserver = mockIntersectionObserver;

test("Navigation이 렌더링 되었을때 Outlet이 렌더링 되는지 확인", () => {
    render(
        <MemoryRouter initialEntries={["/test"]}>
            <QueryClientProvider client={queryClient}>
                <ThemeContextProvider>
                    <Routes>
                        <Route path="/" element={<DefaultLayout />}>
                            <Route
                                path="test"
                                element={<div>Test Component</div>}
                            />
                        </Route>
                    </Routes>
                </ThemeContextProvider>
            </QueryClientProvider>
        </MemoryRouter>
    );

    expect(screen.getByText("Test Component")).toBeInTheDocument();
});
