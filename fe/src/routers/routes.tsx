import { createBrowserRouter } from "react-router-dom";
import DefaultLayout from "@/components/Layout/DefaultLayout";
import Home from "../pages/Home";
import NotFound from "../pages/NotFound";
import ErrorFallBack from "@/components/ErrorFallBack/ErrorFallBack";
import { ErrorBoundary } from "react-error-boundary";
import Login from "@/pages/Login";
import Admin from "@/pages/Admin";

const routes = createBrowserRouter([
    {
        path: "/",
        element: (
            <ErrorBoundary fallbackRender={ErrorFallBack}>
                <DefaultLayout />
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
        path: "/login",
        element: <Login />,
    },
    {
        path: "/admin",
        element: <Admin />,
    },
    {
        path: "*",
        element: <NotFound />,
    },
]);

export default routes;
