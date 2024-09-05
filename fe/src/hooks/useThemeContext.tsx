import { createContext, ReactNode, useContext, useEffect, useState } from "react";
import { ThemeProvider } from "styled-components";
import { darkTheme, lightTheme } from "@/styles/Theme";
type ThemeType = "light" | "dark";

interface ThemeContextType {
    theme: ThemeType;
    handleToggleTheme: () => void;
}

const ThemeContext = createContext<ThemeContextType | null>(null);

export const ThemeContextProvider = ({ children }: { children: ReactNode }) => {
    const [theme, setTheme] = useState<ThemeType>("light");

    const handleToggleTheme = () => {
        const updatedTheme = theme === "light" ? "dark" : "light"
        setTheme(updatedTheme);
        localStorage.setItem("theme", updatedTheme);
    };

    useEffect(() => {
        const savedTheme = localStorage.getItem("theme");
        const prefersDark = window.matchMedia && window.matchMedia("(prefers-color-scheme: dark)").matches;
        const newTheme = (savedTheme === "dark" || savedTheme === "light") 
            ? savedTheme : prefersDark ? "dark" : "light";
        
        setTheme(newTheme);
    }, []);
    return (
        <ThemeContext.Provider value={{ theme, handleToggleTheme }}>
            <ThemeProvider theme={theme === "light" ? lightTheme : darkTheme}>
                {children}
            </ThemeProvider>
        </ThemeContext.Provider>
    );
};

const useThemeContext = () => {
    const context = useContext(ThemeContext);
    if (context === null) {
        throw new Error("ThemeContext를 찾을 수 없다.")
    }
    return context;
};

export default useThemeContext;
