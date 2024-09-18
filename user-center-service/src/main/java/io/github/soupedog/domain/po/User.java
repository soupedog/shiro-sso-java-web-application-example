package io.github.soupedog.domain.po;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.soupedog.repository.converter.UserSexEnumConvert;
import io.github.soupedog.domain.enums.UserSexEnum;
import io.github.soupedog.utils.LongTimeStampSerializer;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "t_user")
// 其实没什么卵用，复杂时间修改还得看自定义
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    private String name;
    private String password;
    // 指定枚举实际存储数据类型，此处指定为字符串
    @Enumerated(value = EnumType.STRING)
    @Convert(converter = UserSexEnumConvert.class)
    private UserSexEnum userSex;
    // 其实没什么卵用，复杂时间修改还得看自定义
    @CreatedDate
    @JsonSerialize(using = LongTimeStampSerializer.class)
    private Long createTs;
    // 其实没什么卵用，复杂时间修改还得看自定义
    @LastModifiedDate
    @JsonSerialize(using = LongTimeStampSerializer.class)
    private Long lastUpdateTs;
}
