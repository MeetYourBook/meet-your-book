import { useEffect } from "react";

const Home = () => {
    async function app() {
        const response = await fetch('/test')
        const user = await response.json()
        console.log(user)
    }
    useEffect(() => {
        app()
    }, []);

    return <div>Home</div>;
};

export default Home;
