package io.github.soupedog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户鉴权信息
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
public class UserCredentials {
    private String uno;
    private String token;
    private Long tokenETS;
    private String refreshKey;
}
