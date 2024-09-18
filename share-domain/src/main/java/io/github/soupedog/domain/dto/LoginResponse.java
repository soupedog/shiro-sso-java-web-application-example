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
 * @date 2024/9/18
 * @since 1.0
 */
@Getter
@Setter
@Generated
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private UserDTO user;
}
