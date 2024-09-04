import { SunOutlined, MoonOutlined } from "@ant-design/icons";
import styled from "styled-components";
import useThemeContext from "@/hooks/useThemeContext";
import { useEffect } from "react";
const ThemeSwitcher = () => {
    const { theme, handleToggleTheme } = useThemeContext();
    useEffect(() => {
    }, [theme])
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
