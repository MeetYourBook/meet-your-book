import { createBrowserRouter } from "react-router-dom";
import DefaultLayout from "@/components/Layout/DefaultLayout";
import Home from "../pages/Home";
import NotFound from "../pages/NotFound";
import ErrorFallBack from "@/components/ErrorFallBack/ErrorFallBack";
import { ErrorBoundary } from "react-error-boundary";

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
        path: "*",
        element: <NotFound />,
    },
]);

export default routes;
