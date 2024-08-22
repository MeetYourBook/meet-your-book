import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout";
import Home from "../pages/Home";
import NotFound from "../pages/NotFound";
import Login from "@/pages/Login";
import SignUp from "@/pages/SignUp";
const routes = createBrowserRouter([
    {
        path: "/",
        element: <Layout/>,
        children: [
            {
                path: "/",
                element: <Home />
            },
        ]
    },
    {
        path: "/Login",
        element: <Login/>
    },
    {
        path: "/SignUp",
        element: <SignUp/>
    },
    {
        path: "*",
        element: <NotFound />
    }
])

export default routes