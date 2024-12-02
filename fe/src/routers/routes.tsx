import { createBrowserRouter } from "react-router-dom";
import DefaultLayout from "@/components/Layout/DefaultLayout";
import Home from "../pages/Home";
import NotFound from "../pages/NotFound";
import Login from "@/pages/Login";
import Admin from "@/pages/Admin";
import PrivateAdminLayout from "@/components/Layout/PrivateAdminLayout";

const routes = createBrowserRouter([
    {
        path: "/",
        element: <DefaultLayout />,
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
        element: <PrivateAdminLayout />,
        children: [
            {
                path: "",
                element: <Admin/>
            }
        ]
    },
    {
        path: "*",
        element: <NotFound />,
    },
]);

export default routes;
