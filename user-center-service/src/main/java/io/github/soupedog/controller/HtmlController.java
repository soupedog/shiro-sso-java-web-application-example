package io.github.soupedog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 描述
 *
 * @author Xavier
 * @date 2024/9/19
 * @since 1.0
 */
@Controller
public class HtmlController {
    @GetMapping(value = "/")
    public String index() {
        return "forward:login.html";
        // 配置了 spring.mvc.view.suffix ，等效于 return "login";
    }
}
