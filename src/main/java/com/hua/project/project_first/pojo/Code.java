package com.hua.project.project_first.pojo;

/**
 * 200 OK 请求成功。
 * 400 Bad Request 由于被认为是客户端错误（例如，错误的请求语法、无效的请求消息帧或欺骗性的请求路由），服务器无法或不会处理请求。
 * 403 Forbidden 客户端没有访问内容的权限；也就是说，它是未经授权的，因此服务器拒绝提供请求的资源。与 401 Unauthorized 不同，服务器知道客户端的身份。
 * 404 Not Found服务器找不到请求的资源。在浏览器中，这意味着无法识别 URL。在 API 中，这也可能意味着端点有效，但资源本身不存在。
 *      服务器也可以发送此响应，而不是 403 Forbidden，以向未经授权的客户端隐藏资源的存在。这个响应代码可能是最广为人知的，因为它经常出现在网络上。
 * 429 Too Many Requests 用户在给定的时间内发送了太多请求（"限制请求速率"）
 * 500 Internal Server Error 服务器遇到了不知道如何处理的情况。
 */
public enum Code {
    OK_200,
    BadRequest_400,
    ForBidden_403,
    NotFound_404,
    ToManyRequests_429,
    ServerError_500
}
