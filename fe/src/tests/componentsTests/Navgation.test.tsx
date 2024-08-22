import { render } from "@testing-library/react";
import { describe, expect, test } from "vitest";
import Navigation from "@/components/Navigation/Navigation";
import { MemoryRouter } from "react-router-dom";

describe("Layout Navigation Component 테스트", () => {
    test("Navigation Component가 렌더링 되었을때 MYB 로고가 보인다.", () => {
        const { getByText } = render(
            <MemoryRouter>
                <Navigation />
            </MemoryRouter>
        );
        expect(getByText("MYB")).toBeInTheDocument();
    });
});
