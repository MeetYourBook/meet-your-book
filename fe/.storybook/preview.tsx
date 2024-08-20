import React from "react";
import { initialize, mswDecorator } from "msw-storybook-addon";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import type { Preview } from "@storybook/react";
import "../src/styles/GlobalStyle";
import GlobalStyle from "../src/styles/GlobalStyle";
import { libraryHandlers } from "../src/mocks/handlers/libraryHandler";
import { booksHandlers } from "../src/mocks/handlers/booksHandler";
import { BrowserRouter } from "react-router-dom";

initialize({
    serviceWorker: {
        url: "/mockServiceWorker.js",
    },
    onUnhandledRequest: "bypass",
});

const queryClient = new QueryClient();

export const decorators = [
    (Story: React.ComponentType) => (
        <QueryClientProvider client={queryClient}>
            <GlobalStyle />
            <BrowserRouter>
                <Story />
            </BrowserRouter>
        </QueryClientProvider>
    ),
    mswDecorator,
];

const preview: Preview = {
    parameters: {
        controls: {
            matchers: {
                color: /(background|color)$/i,
                date: /Date$/i,
            },
        },
        msw: {
            handlers: [...libraryHandlers, ...booksHandlers],
        },
    },
};

export default preview;
