import { message } from "antd";
import { useEffect } from "react";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import { ERROR_MESSAGE } from "@/constants";

const PrivateAdminLayout = () => {
    const isToken = !!sessionStorage.getItem("token");
    const location = useLocation();

    useEffect(() => {
        if (!isToken) message.error(ERROR_MESSAGE.LOGIN_REQUIRED_MESSAGE);
    }, [isToken, location]);

    return <>{isToken ? <Outlet /> : <Navigate to="/login" />}</>;
};

export default PrivateAdminLayout;
