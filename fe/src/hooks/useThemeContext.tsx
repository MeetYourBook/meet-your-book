import { createContext, ReactNode, useContext, useState } from "react";
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
        setTheme((prevTheme) => (prevTheme === "light" ? "dark" : "light"));
    };
    return (
        <ThemeContext.Provider value={{ theme, handleToggleTheme }}>
            <ThemeProvider theme={theme === "light" ? darkTheme : lightTheme}>
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
