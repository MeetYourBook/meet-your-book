import { useEffect } from "react";

const Home = () => {
    async function app() {
        const response = await fetch('api/books')
        const user = await response.json()
        console.log(user)
    }
    useEffect(() => {
        app()
        console.log(import.meta.env.VITE_API_URL)
    }, []);

    return <div>Home</div>;
};

export default Home;

// store에는 뭐가 있어야할까?
// 1. query
// 2. page
// 3. size
// 4. selectedValue = 옵션
// 5. librariesFilter = []