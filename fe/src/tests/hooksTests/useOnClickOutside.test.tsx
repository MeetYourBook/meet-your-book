import { fireEvent, render, screen } from "@testing-library/react";
import useOnClickOutside from "@/hooks/useOnClickOutside";
import { useRef, useState } from "react";

const TestComponent = () => {
    const testRef = useRef(null)
    const [isOpen, setIsOpen] = useState(true)

    const closeModal = () => setIsOpen(false)

    useOnClickOutside(testRef, closeModal)
    return (
        <div>
            <div>modal</div>
            {isOpen && <div ref={testRef}>modal open</div>}
        </div>
    )
}

describe("useOnClickOutside Hook 테스트.", () => {
    test("innerRef의 외부를 클릭했을때 모달이 닫히는지 확인.", () => {
        render(<TestComponent/>)

        expect(screen.getByText("modal open")).toBeInTheDocument()

        fireEvent.mouseDown(screen.getByText("modal"));

        expect(screen.queryByTestId("modal")).not.toBeInTheDocument();
    })

    test("innerRef를 클릭했을때 모달이 닫히지 않는지 확인.", () => {
        render(<TestComponent/>)

        expect(screen.getByText("modal open")).toBeInTheDocument();

        fireEvent.mouseDown(screen.getByText("modal open"))

        expect(screen.getByText("modal open")).toBeInTheDocument();
    })
})