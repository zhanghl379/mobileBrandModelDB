import org.apache.commons.lang3.StringUtils;

/**
 * @author zhanghl
 * @version MobileBrandEnum.java
 */
public enum MobileBrandEnum {
    IPHONE("iphone", "苹果"),
    HUAWEI("huawei", "华为"),
    XIAOMI("xiaomi", "小米"),
    HONOR("honor", "荣耀"),
    VIVO("vivo", "VIVO"),
    OPPO("oppo", "OPPO"),
    SAMSUNG("samsung", "三星"),
    MEIZU("meizu", "魅族"),
    ONEPLUS("oneplus", "一加"),
    OTHER("other", "其他"),
    ;

    private String mobileId;
    private String brand;

    MobileBrandEnum() {
    }

    MobileBrandEnum(String mobileId, String brand) {
        this.mobileId = mobileId;
        this.brand = brand;
    }

    public static MobileBrandEnum getByMobileId(String mobileId) {
        MobileBrandEnum[] valueList = MobileBrandEnum.values();
        for (MobileBrandEnum v : valueList) {
            if (StringUtils.equalsIgnoreCase(v.getMobileId(), mobileId)) {
                return v;
            }
        }
        return OTHER;
    }

    public static MobileBrandEnum getByBrand(String brand) {
        MobileBrandEnum[] valueList = MobileBrandEnum.values();
        for (MobileBrandEnum v : valueList) {
            if (StringUtils.equalsIgnoreCase(v.getBrand(), brand)) {
                return v;
            }
        }
        return OTHER;
    }

    public String getMobileId() {
        return mobileId;
    }

    public String getBrand() {
        return brand;
    }
}