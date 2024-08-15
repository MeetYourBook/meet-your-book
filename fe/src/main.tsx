import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider } from "react-router-dom";
import routes from "./routers/routes.tsx";
import GlobalStyle from "./styles/GlobalStyle.ts";
import { QueryClient, QueryClientProvider } from "react-query";

const main = async () => {
    if (process.env.NODE_ENV === "development") {
        const { worker } = await import("./mocks/node.ts");
        await worker.start({ onUnhandledRequest: "bypass" });
    }
};
await main();

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById("root")!).render(
    <React.StrictMode>
        <QueryClientProvider client={queryClient}>
            <GlobalStyle />
            <RouterProvider router={routes} />
        </QueryClientProvider>
    </React.StrictMode>
);
