import { render, screen } from "@testing-library/react";
import FilterDisplay from "@/components/FilterDisplay/FilterDisplay";
import { vi } from "vitest";

vi.mock("@/components/FilterDisplay/LibraryFilter/LibraryFilter", () => ({
    __esModule: true,
    default: () => <div>LibraryFilter Component</div>,
}));

test("FilterDisplay 컴포넌트가 렌더링 되는지 확인.", () => {
    render(<FilterDisplay />);

    expect(screen.getByText("Filter Option")).toBeInTheDocument();
    expect(screen.getByText("LibraryFilter Component")).toBeInTheDocument();
});
