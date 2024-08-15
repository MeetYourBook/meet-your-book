import { createGlobalStyle } from "styled-components";
import reset from "styled-reset";
import NotoSansFont from "@/fonts/NotoSansKR-VariableFont_wght.ttf";

const GlobalStyle = createGlobalStyle`
    ${reset}

    :root {
    --border-color: #e9e7e7;
    }
    @font-face {
        font-family: 'NotoSansFont';
        src: local('NotoSansFont'), local('NotoSansFont');
        font-style: normal;
        src: url(${NotoSansFont}) format('truetype');
    }
    
    body,
    button,
    input,
    textarea {
    font-family: 'NotoSansFont', -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue', 'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol', sans-serif;
    }
`;

export default GlobalStyle;
