// DropDownBox.test.jsx
import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import DropDownBox from "@/components/Navigation/DropDownBox/DropDownBox";

describe("DropDownBox 컴포넌트 테스트", () => {
    it("초기 렌더링 initial로 통합 검색이 설정된다.", () => {
        render(<DropDownBox />);
        expect(screen.getByText("통합 검색")).toBeInTheDocument();
    });

    it("드랍다운을 호버했을때 리스트가 렌더링 된다.", () => {
        render(<DropDownBox />);
        const dropdownContainer = screen.getByText("통합 검색");
        fireEvent.mouseEnter(dropdownContainer);
        expect(screen.getByText("제목")).toBeInTheDocument();
        expect(screen.getByText("저자")).toBeInTheDocument();
        expect(screen.getByText("출판사")).toBeInTheDocument();
    });

    it("리스트를 클릭했을때 클릭한 라벨로 설정이 바뀌고 리스트 박스가 사라진다.", () => {
        render(<DropDownBox />);
        const dropdownContainer = screen.getByText("통합 검색");
        fireEvent.mouseEnter(dropdownContainer);
        const titleItem = screen.getByText("제목");
        fireEvent.click(titleItem);
        expect(screen.getByText("제목")).toBeInTheDocument();
        expect(screen.queryByText("통합 검색")).not.toBeInTheDocument();
    });

    it("드랍다운을 호버하고 마우스가 떠나면 리스트가 사라진다.", () => {
        render(<DropDownBox />);
        const dropdownContainer = screen.getByText("통합 검색");
        fireEvent.mouseEnter(dropdownContainer);
        fireEvent.mouseLeave(dropdownContainer);
        expect(screen.queryByText("제목")).not.toBeInTheDocument();
        expect(screen.queryByText("저자")).not.toBeInTheDocument();
        expect(screen.queryByText("출판사")).not.toBeInTheDocument();
    });
});
