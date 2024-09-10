import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import routes from "./routers/routes.tsx";
import GlobalStyle from "./styles/GlobalStyle.ts";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import { ThemeContextProvider } from "./hooks/useThemeContext.tsx";
import "@/styles/font.css";

// const main = async () => {
//     if (process.env.NODE_ENV === "development") {
//         const { worker } = await import("./mocks/node.ts");
//         await worker.start({ onUnhandledRequest: "bypass" });
//     }
// };
// await main();

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            throwOnError: true,
        },
        mutations: {
            throwOnError: true,
        },
    },
});

ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <ThemeContextProvider>
            <QueryClientProvider client={queryClient}>
                <GlobalStyle />
                <RouterProvider router={routes} />
                <ReactQueryDevtools initialIsOpen={false} />
            </QueryClientProvider>
        </ThemeContextProvider>
    </React.StrictMode>
);
