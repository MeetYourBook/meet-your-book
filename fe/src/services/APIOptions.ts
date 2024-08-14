export const get = (): RequestInit => {
    return {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            // 로그인 구현시 토큰 추가 
            // 'Authorization': 'Bearer your-token'
        },
    };
};
