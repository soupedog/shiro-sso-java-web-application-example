package io.github.soupedog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 描述
 *
 * @author Xavier
 * @date 2024/9/24
 * @since 1.0
 */
@Getter
@Setter
@Generated
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo {
    private String name;
    private String token;
    private String refreshKey;
}
