import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout";
import Home from "../pages/Home";
import NotFound from "../pages/NotFound";
import Login from "@/pages/Login";
import SignUp from "@/pages/SignUp";
import ErrorFallBack from "@/components/ErrorFallBack/ErrorFallBack";
import { ErrorBoundary } from "react-error-boundary";

const routes = createBrowserRouter([
    {
        path: "/",
        element: (
            <ErrorBoundary fallbackRender={ErrorFallBack}>
                <Layout />
            </ErrorBoundary>
        ),
        children: [
            {
                path: "/",
                element: <Home />,
            },
        ],
    },
    {
        path: "/Login",
        element: <Login />,
    },
    {
        path: "/SignUp",
        element: <SignUp />,
    },
    {
        path: "*",
        element: <NotFound />,
    },
]);

export default routes;
