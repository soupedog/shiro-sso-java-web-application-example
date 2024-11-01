import axios from "axios";
import {message} from "antd";

const instance = axios.create({
    headers: {'auth-no-redirect': 'true'}
});

const instance_A = axios.create({
    headers: {'auth-no-redirect': 'true'}
});

const instance_B = axios.create({
    headers: {'auth-no-redirect': 'true'}
});

instance_A.interceptors.response.use(function (response) {
    let status = response.status;
    alert(status)
    alert(status == 403)

    if (status == 200) {
        return response;
    } else if (status == 403) {
        return Promise.reject(response);
    } else {
        message.error(response.data, 5);
        return Promise.reject(response);
    }
}, function (error) {
    if (error.response.status === 403) {
        let info = error.response.data["main"];
        let backUrl = "http://localhost:8081"
        if (info == null) {
            window.location.href = "http://localhost:8080/login?back_url=" + backUrl;
        } else {
            window.location.href = "http://localhost:8080/login?back_url=" + backUrl + "&info=" + info;
        }
    }

    // 对响应错误做点什么
    message.error(error.message, 5);
    return Promise.reject(error);
});

instance_B.interceptors.response.use(function (response) {
    let status = response.status;

    if (status == 200) {
        return response;
    } else if (status == 403) {
        return Promise.reject(response);
    } else {
        message.error(response.data, 5);
        return Promise.reject(response);
    }
}, function (error) {
    if (error.response.status === 403) {
        let info = error.response.data["main"];
        let backUrl = "http://localhost:8082"
        if (info == null) {
            window.location.href = "http://localhost:8080/login?back_url=" + backUrl;
        } else {
            window.location.href = "http://localhost:8080/login?back_url=" + backUrl + "&info=" + info;
        }
    }

    // 对响应错误做点什么
    message.error(error.message, 5);
    return Promise.reject(error);
});

export interface ServiceResponse<T> {
    msg?: string;
    main?: T;
}

export interface LoginInInfo {
    credentials: UserCredentials;
    user: UserDto;
}

export interface UserDto {
    uno: string;
}

export interface UserCredentials {
    uid: string;
    name: string;
    userType: string;
    createTs: string;
    lastUpdateTs: number;
}

export class LoginService {
    static login(account?: string, password?: string,
                 successHook?: (input?: ServiceResponse<String>) => void,
                 beforeHook?: () => void,
                 finallyHook?: () => void) {

        if (beforeHook != null) {
            beforeHook();
        }

        instance.post("http://localhost:8080/user/login", {account: account, password: password})
            .then(response => {
                if (successHook && response.status == 200) {
                    let data: ServiceResponse<String> = response.data;
                    successHook(data);
                }
            })
            .finally(() => {
                    if (finallyHook != null) {
                        finallyHook();
                    }
                }
            )
    }
}

export class AService {
    static clickButton() {
        instance_A.get("http://localhost:8081/api/a/needlogin")
            .then(response => {
                message.success(response.data);
            })

    }
}

export class BService {
    static clickButton() {
        instance_B.get("http://localhost:8082/api/b/needlogin")
            .then(response => {
                message.success(response.data);
            })

    }
}