import { http, HttpResponse } from "msw";
import loginData from "../mockData/login.json";
import { DEV_API } from ".";

interface LoginType {
    id: string;
    password: string;
}
export const loginHandlers = [
    http.post(DEV_API.LOGIN, async ({ request }) => {
        const body = (await request.json()) as LoginType;
        const loginInfo = loginData.login;

        const isValidLogin = loginInfo.id === body.id && loginInfo.password === body.password;

        if (isValidLogin)
            return HttpResponse.json(
                {
                    success: true,
                    message: "로그인 성공",
                    redirectUrl: "/admin",
                    token: "dev_jwt_token",
                },
                { status: 200 }
            );
        return HttpResponse.json(
            { success: true, message: "로그인 실패" },
            { status: 400 }
        );
    }),
];
