package io.github.soupedog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 描述
 *
 * @author Xavier
 * @date 2024/9/19
 * @since 1.0
 */
@Controller
public class HtmlController {
    @GetMapping(value = "/login")
    public String index(@RequestParam("back_url") String back_url, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("back_url", back_url);
        return "forward:login.html";
        // 配置了 spring.mvc.view.suffix ，等效于 return "login";
    }
}
