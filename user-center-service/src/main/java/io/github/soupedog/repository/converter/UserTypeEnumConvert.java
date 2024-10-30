package io.github.soupedog.repository.converter;


import io.github.soupedog.domain.enums.UserTypeEnum;
import jakarta.persistence.AttributeConverter;

/**
 * 此接口泛型：第一个是 PO 对象属性类型；第二个是数据库字段类型(会影响到自动建表时具体类型)
 *
 * @author Xavier
 * @date 2021/9/23
 */
public class UserTypeEnumConvert implements AttributeConverter<UserTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserTypeEnum attribute) {
        return attribute == null ? -1 : attribute.getIndex();
    }

    @Override
    public UserTypeEnum convertToEntityAttribute(Integer dbData) {
        return dbData == null ? UserTypeEnum.ROOT : UserTypeEnum.parse(dbData);
    }
}
