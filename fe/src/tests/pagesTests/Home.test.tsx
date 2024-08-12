import { render, screen } from "@testing-library/react";
import { MemoryRouter, Route, Routes } from "react-router-dom";
import Home from "@/pages/Home";
import { vi } from "vitest";

vi.mock("@/components/BooksDisplay/BooksDisplay", () => ({
    __esModule: true,
    default: () => <div>Mocked BooksDisplay</div>,
}));

vi.mock("@/components/FilterDisplay/FilterDisplay", () => ({
    __esModule: true,
    default: () => <div>Mocked FilterDisplay</div>,
}));

test("기본 루트로 접속했을때 Home의 child가 렌더링 되는지 확인.", () => {
    render(
        <MemoryRouter initialEntries={["/"]}>
            <Routes>
                <Route path="/" element={<Home/>}/>
            </Routes>
        </MemoryRouter>
    )

    expect(screen.getByText("Mocked FilterDisplay")).toBeInTheDocument();
    expect(screen.getByText("Mocked BooksDisplay")).toBeInTheDocument();
});
