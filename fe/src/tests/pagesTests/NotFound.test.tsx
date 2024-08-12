import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import NotFound from "@/pages/NotFound";

test("라이터가 없는 url로 접속했을때 NotFound 페이지가 렌더링 되는지 확인.", () => {
    render(
        <MemoryRouter initialEntries={["/non-existent-route"]}>
            <Routes>
                <Route path="*" element={<NotFound />} />
            </Routes>
        </MemoryRouter>
    );

    expect(screen.getByText("404 - Page Not Found")).toBeInTheDocument();
    expect(
        screen.getByText("찾으시는 페이지가 존재하지 않습니다.")
    ).toBeInTheDocument();
});
