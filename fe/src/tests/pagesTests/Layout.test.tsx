import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import Layout from "@/pages/Layout";

test("Navigation이 렌더링 되었을때 Outlet이 렌더링 되는지 확인", () => {
    render(
        <MemoryRouter initialEntries={["/test"]}>
            <Routes>
                <Route path="/" element={<Layout />}>
                    <Route path="test" element={<div>Test Component</div>} />
                </Route>
            </Routes>
        </MemoryRouter>
    );

    expect(screen.getByText("Test Component")).toBeInTheDocument();
});
