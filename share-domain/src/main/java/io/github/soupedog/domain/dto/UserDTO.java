package io.github.soupedog.domain.dto;


import io.github.soupedog.domain.enums.UserSexEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Xavier
 * @date 2022/1/21
 */
@Getter
@Setter
@Builder
// 标记这个类存在自动生成的内容，有助于代码规范检查工具忽略这些自动生成的代码
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "用户信息", description = "用户的可见信息")
public class UserDTO {
    @Schema(title = "用户唯一标识", example = "2")
    private Long uid;
    @Schema(title = "用户名称", example = "李四")
    private String name;
    @Schema(title = "111", example = "SECRET")
    private UserSexEnum userSex;
    @Schema(title = "创建时间", example = "1642753537299")
    private Long createTs;
    @Schema(title = "最后修改时间", example = "1642753537299")
    private Long lastUpdateTs;
}
