import { createBrowserRouter } from "react-router-dom";
import Layout from "../pages/Layout";
import Home from "../pages/Home";
import NotFound from "../pages/NotFound";
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
        path: "*",
        element: <NotFound />
    }
])

export default routes