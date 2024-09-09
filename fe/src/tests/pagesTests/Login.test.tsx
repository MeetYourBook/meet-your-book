import Login from "@/pages/Login";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import { vi, describe, beforeEach, test, expect } from "vitest";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const mockNavigate = vi.fn();

vi.mock("react-router-dom", async () => {
    const actual: object = await vi.importActual("react-router-dom");
    return {
        ...(actual as object),
        useNavigate: () => mockNavigate,
    };
});

vi.mock("antd", async () => {
    const actual: object = await vi.importActual("antd");
    return {
        ...(actual as object),
        Spin: () => (
            <div className="ant-spin" data-testid="antd-spin">
                Loading
            </div>
        ),
        message: {
            success: vi.fn(),
        },
    };
});

vi.mock("@/styles/AuthFormStyle", () => ({
    LogoWrap: "div",
    AuthContainer: "div",
    AuthCard: "div",
    Header: "div",
    AuthTitle: "h2",
    AuthForm: "form",
    FormGroup: "div",
    Label: "div",
    Input: "input",
    Button: "button",
}));

const mockUseLoginMutation = vi.fn();

vi.mock("@/hooks/queries/useLoginMutation", () => ({
    default: () => mockUseLoginMutation(),
}));

describe("Login 페이지 테스트", () => {
    let queryClient: QueryClient;

    beforeEach(() => {
        vi.clearAllMocks();
        queryClient = new QueryClient();
        mockUseLoginMutation.mockReturnValue({
            data: undefined,
            mutate: vi.fn(),
            isPending: false,
        });
    });

    const renderWithProviders = (ui: React.ReactElement) => {
        return render(
            <QueryClientProvider client={queryClient}>
                <BrowserRouter>{ui}</BrowserRouter>
            </QueryClientProvider>
        );
    };

    test("로그인 페이지가 렌더링 되는지 확인.", () => {
        renderWithProviders(<Login />);
        
        expect(screen.getByRole("heading", { name: /login/i, level: 2 })).toBeInTheDocument();
        expect(screen.getByText("ID")).toBeInTheDocument();
        expect(screen.getByText("Password")).toBeInTheDocument();
        expect(screen.getByRole("button", { name: /login/i })).toBeInTheDocument();
    });

    test("id, password가 입력되는지 확인", () => {
        renderWithProviders(<Login />);
        
        const idInput = screen.getByPlaceholderText("Enter your ID") as HTMLInputElement;
        const passwordInput = screen.getByPlaceholderText("Enter your password") as HTMLInputElement;

        fireEvent.change(idInput, { target: { value: "wpzhwh123" } });
        fireEvent.change(passwordInput, { target: { value: "wpzhwh123" } });

        expect(idInput.value).toBe("wpzhwh123");
        expect(passwordInput.value).toBe("wpzhwh123");
    });

    test("팬딩 상태일때 spin 컴포넌트가 표시되는지 확인.", () => {
        mockUseLoginMutation.mockReturnValue({
            data: undefined,
            mutate: vi.fn(),
            isPending: true,
        });

        renderWithProviders(<Login />);

        expect(screen.getByTestId("antd-spin")).toBeInTheDocument();
        expect(screen.getByTestId("antd-spin")).toHaveClass("ant-spin");
        expect(screen.queryByRole("button", { name: /login/i })).not.toBeInTheDocument();
    });

    test("handleSubmit이 동작하는지 확인.", () => {
        const mockMutate = vi.fn();
        mockUseLoginMutation.mockReturnValue({
            data: undefined,
            mutate: mockMutate,
            isPending: false,
        });
        
        renderWithProviders(<Login />);

        const loginButton = screen.getByRole("button", { name: /login/i });
        fireEvent.click(loginButton);

        expect(mockMutate).toHaveBeenCalledTimes(1);
    });

    test("로그인 성공 시 토큰 저장 및 페이지 이동 확인.", async () => {
        const mockToken = "test-token";
        mockUseLoginMutation.mockReturnValue({
            data: { token: mockToken },
            mutate: vi.fn(),
            isPending: false,
        });

        renderWithProviders(<Login />);

        await waitFor(() => {
            expect(sessionStorage.getItem("token")).toBe(mockToken);
            expect(mockNavigate).toHaveBeenCalledWith('/admin');
        });
    });
});