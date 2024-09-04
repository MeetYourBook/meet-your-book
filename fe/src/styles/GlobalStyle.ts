import { createGlobalStyle } from "styled-components";
import reset from "styled-reset";

const GlobalStyle = createGlobalStyle`
    ${reset}

    :root {
    --border-color: #e9e7e7;
    --border-dark-color: "#374151";
    }

    body {
        background-color: ${({ theme }) => theme.body};
        color: ${({ theme }) => theme.text};
        transition: background 0.15s ease-in, color 0.15s ease-in;
    }
    
    body,
    button,
    input,
    textarea {
    font-family: 'NotoSansFont', -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', sans-serif;
    }
`;

export default GlobalStyle;
