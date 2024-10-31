package io.github.soupedog.domain.constant;

import org.apache.shiro.lang.util.ByteSource;

/**
 * 描述
 *
 * @author Xavier
 * @date 2024/9/24
 * @since 1.0
 */
public class AccountConstant {
    public static final String ALGORITHM_NAME = "SHA-256";
    public static final ByteSource SALT = ByteSource.Util.bytes("AAAA");
    public static final Integer HASH_ITERATIONS = 2;
}
