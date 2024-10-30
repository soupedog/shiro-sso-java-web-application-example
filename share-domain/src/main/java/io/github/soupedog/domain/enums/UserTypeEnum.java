package io.github.soupedog.domain.enums;

/**
 * @author Xavier
 * @date 2021/9/23
 */
public enum UserTypeEnum {
    /**
     * 最高权限
     */
    ROOT(-1, "ROOT"),
    /**
     * 管理员权限
     */
    ADMIN(0, "ADMIN"),
    /**
     * 普通用户
     */
    COMMON(1, "COMMON");

    UserTypeEnum(Integer index, String value) {
        this.index = index;
        this.value = value;
    }

    public static UserTypeEnum parse(Integer index) {
        if (index == null) {
            throw new IllegalArgumentException("Unexpected index of UserTypeEnum,it can't be null.");
        }
        switch (index) {
            case -1:
                return UserTypeEnum.ROOT;
            case 1:
                return UserTypeEnum.COMMON;
            case 0:
                return UserTypeEnum.ADMIN;
            default:
                throw new IllegalArgumentException("Unexpected index of UserTypeEnum,it can't be " + index + ".");
        }
    }

    public static UserTypeEnum parse(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Unexpected value of UserTypeEnum,it can't be null.");
        }
        switch (value) {
            case "ROOT":
                return UserTypeEnum.ROOT;
            case "COMMON":
                return UserTypeEnum.COMMON;
            case "ADMIN":
                return UserTypeEnum.ADMIN;
            default:
                throw new IllegalArgumentException("Unexpected value of UserTypeEnum,it can't be " + value + ".");
        }
    }

    /**
     * 序号
     */
    private Integer index;
    /**
     * 枚举值
     */
    private String value;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
