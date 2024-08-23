import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import Layout from "@/pages/Layout";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const queryClient = new QueryClient();

test("Navigation이 렌더링 되었을때 Outlet이 렌더링 되는지 확인", () => {
    render(
        <MemoryRouter initialEntries={["/test"]}>
            <QueryClientProvider client={queryClient}>
                <Routes>
                    <Route path="/" element={<Layout />}>
                        <Route
                            path="test"
                            element={<div>Test Component</div>}
                        />
                    </Route>
                </Routes>
            </QueryClientProvider>
        </MemoryRouter>
    );

    expect(screen.getByText("Test Component")).toBeInTheDocument();
});
