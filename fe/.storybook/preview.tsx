import React from "react";
import { initialize, mswDecorator } from "msw-storybook-addon";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import type { Preview } from "@storybook/react";
import "../src/styles/GlobalStyle";
import GlobalStyle from "../src/styles/GlobalStyle";
import { libraryHandlers } from "../src/mocks/handlers/libraryHandler";
import { booksHandlers } from "../src/mocks/handlers/booksHandler";

initialize({
    serviceWorker: {
        url: "/mockServiceWorker.js",
    },
    onUnhandledRequest: "bypass"
});

const queryClient = new QueryClient();

export const decorators = [
    (Story: React.ComponentType) => (
        <QueryClientProvider client={queryClient}>
            <GlobalStyle />
            <Story />
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
