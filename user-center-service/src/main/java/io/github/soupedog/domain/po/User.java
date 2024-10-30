package io.github.soupedog.domain.po;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.soupedog.domain.enums.UserTypeEnum;
import io.github.soupedog.repository.converter.UserTypeEnumConvert;
import io.github.soupedog.utils.LongTimeStampSerializer;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * @author Xavier
 * @date 2021/9/21
 */
@Getter
@Setter
@Builder
// 标记这个类存在自动生成的内容，有助于代码规范检查工具忽略这些自动生成的代码
@Generated
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user", indexes = @Index(name = "index_user_account", columnList = "account", unique = true))
// 其实没什么卵用，复杂时间修改还得看自定义
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    private String uno;
    private String name;
    private String account;
    private String password;
    // 指定枚举实际存储数据类型，此处指定为字符串
    @Enumerated(value = EnumType.STRING)
    @Convert(converter = UserTypeEnumConvert.class)
    private UserTypeEnum userType;
    /**
     * 身份令牌
     */
    private String token;
    /**
     * 身份令牌刷新秘钥
     */
    private String refreshKey;
    /**
     * 身份令牌有效截止日期(Expiration Timestamp)
     */
    @JsonSerialize(using = LongTimeStampSerializer.class)
    private Long tokenETS;
    // 其实没什么卵用，复杂时间修改还得看自定义
    @CreatedDate
    @JsonSerialize(using = LongTimeStampSerializer.class)
    private Long createTs;
    // 其实没什么卵用，复杂时间修改还得看自定义
    @LastModifiedDate
    @JsonSerialize(using = LongTimeStampSerializer.class)
    private Long lastUpdateTs;
}
