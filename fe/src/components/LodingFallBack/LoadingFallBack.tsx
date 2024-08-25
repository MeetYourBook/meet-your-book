import { Spin } from "antd";

const LoadingFallBack = () => {
    return (
        <div
            style={{
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                width: "100%",
                height: "30px",
                margin: "30px auto"
            }}
        >
            <Spin />
        </div>
    );
};

export default LoadingFallBack;
