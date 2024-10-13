import React, { ComponentType, ReactNode } from "react";
import { ErrorProps } from "@/components/ErrorFallBack/ErrorFallBack";

interface ErrorBoundaryProps {
    fallback: ComponentType<ErrorProps>;
    onReset: () => void;
    children: ReactNode;
}

interface ErrorBoundaryState {
    hasError: boolean;
    error: Error | null;
}

class ErrorBoundary extends React.Component<ErrorBoundaryProps, ErrorBoundaryState> {
    constructor(props: ErrorBoundaryProps) {
        super(props);
        this.state = { hasError: false, error: null };
        this.captureReject = this.captureReject.bind(this);
        this.resetError = this.resetError.bind(this);
    }

    static getDerivedStateFromError(error:Error) {
        return { hasError: true, error };
    }

    componentDidMount() {
        window.addEventListener("unhandledrejection", this.captureReject);
    }

    componentWillUnmount() {
        window.removeEventListener("unhandledrejection", this.captureReject);
    }

    captureReject(e: PromiseRejectionEvent) {
        e.preventDefault();
        const error = e.reason instanceof Error ? e.reason : new Error(String(e.reason));
        this.setState({ hasError: true, error });
    }

    resetError() {
        this.props.onReset()
        this.setState({ hasError: false, error: null });
    }

    getStatusCode(error: Error | null): number {
        if (!error) return 500;

        const errorMessage = error.message;
        const statusCode = parseInt(errorMessage);

        if (!isNaN(statusCode) && statusCode >= 100 && statusCode < 600) {
            return statusCode;
        }

        if (error instanceof TypeError) return 400;
        if (errorMessage.includes("Network Error")) return 503;
        
        return 500;
    }

    render(): ReactNode {
        const { fallback: Fallback, children } = this.props;
        if (this.state.hasError) {
            const statusCode = this.getStatusCode(this.state.error);

            return <Fallback 
                statusCode={statusCode}
                resetError={this.resetError}
            />;
        }

        return children;
    }
}

export default ErrorBoundary;