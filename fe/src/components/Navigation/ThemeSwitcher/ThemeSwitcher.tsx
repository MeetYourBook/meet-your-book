import { SunOutlined, MoonOutlined } from "@ant-design/icons";
import styled from "styled-components";
import useThemeContext from "@/hooks/useThemeContext";
const ThemeSwitcher = () => {
    const { theme, handleToggleTheme } = useThemeContext();
    return (
        <span onClick={handleToggleTheme}>
            {theme === "light" ? <LightBtn/> : <DarkBtn/>}
        </span>
    );
};

export default ThemeSwitcher;

const LightBtn = styled(SunOutlined)`
    font-size: 20px;
    cursor: pointer;
`
const DarkBtn = styled(MoonOutlined)`
    font-size: 20px;
    cursor: pointer;
`
