export class BaseUtil {
    static getQueryParam(paramName: string) {
        // 获取URL的查询字符串部分，并去除开头的"?"
        var search = window.location.search.substring(1);
        // 将查询字符串分割成键值对数组
        var params = search.split("&");
        for (var i = 0; i < params.length; i++) {
            // 分割每个键值对
            var pair = params[i].split("=");
            // 如果键匹配，则返回对应的值
            if (pair[0] == paramName) {
                return pair[1];
            }
        }
        // 如果没有找到，返回null或你希望的其他默认值
        return null;
    }
}