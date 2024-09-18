package io.github.soupedog.repository.converter;


import io.github.soupedog.domain.enums.UserSexEnum;
import javax.persistence.AttributeConverter;

/**
 * 此接口泛型：第一个是 PO 对象属性类型；第二个是数据库字段类型(会影响到自动建表时具体类型)
 *
 * @author Xavier
 * @date 2021/9/23
 */
public class UserSexEnumConvert implements AttributeConverter<UserSexEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserSexEnum attribute) {
        return attribute == null ? -1 : attribute.getIndex();
    }

    @Override
    public UserSexEnum convertToEntityAttribute(Integer dbData) {
        return dbData == null ? UserSexEnum.SECRET : UserSexEnum.parse(dbData);
    }
}
