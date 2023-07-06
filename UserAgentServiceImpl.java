import cn.hutool.core.util.ReUtil;
import cn.hutool.http.useragent.Platform;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;

import jodd.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhanghl
 * @version UserAgentServiceImpl.java
 */
@Slf4j
@Component
public class UserAgentServiceImpl implements UserAgentService {
    /**
     * 解析UA前先去除包含的内容，需要保留该正则中的空格，否则带空格的手机型号"OPPO R11"匹配不到
     */
    private static final String FILTER_REG = "zh-CN; ";
    private static final Pattern pattern = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");
    @Resource
    private MobileBrandModelDAO mobileBrandModelDAO;

    /**
     * 从request中获取UA信息
     *
     * @param request
     * @return
     */
    @Override
    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

    /**
     * 从request中解析UA信息获取手机系统与品牌
     *
     * @param request
     * @return
     */
    @Override
    public UserAgentResult parseUserAgent(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return this.parseUserAgent(userAgent);
    }

    /**
     * 从UA信息中获取手机系统与品牌
     *
     * @param ua
     * @return
     */
    @Override
    public UserAgentResult parseUserAgent(String ua) {
        if (StringUtils.isBlank(ua)) {
            return null;
        }

        UserAgent parse = UserAgentUtil.parse(ua);
        Platform platform = parse.getPlatform();

        UserAgentResult userAgent = new UserAgentResult();
        userAgent.setIsIos(platform.isIos());
        userAgent.setIsAndroid(platform.isAndroid());
        if (userAgent.getIsIos()) {
            userAgent.setBrand(MobileBrandEnum.IPHONE.getBrand());
            userAgent.setMobileId(MobileBrandEnum.IPHONE.getMobileId());
            return userAgent;
        }

        // 解析出设备型号
        String newUa = ReUtil.delFirst(FILTER_REG, ua);
        Matcher matcher = pattern.matcher(newUa);
        String model = StringPool.EMPTY;
        if (matcher.find()) {
            model = matcher.group(1).trim();
        }

        if (StringUtils.isBlank(model)) {
            userAgent.setBrand(MobileBrandEnum.OTHER.getBrand());
            userAgent.setMobileId(MobileBrandEnum.OTHER.getMobileId());
            return userAgent;
        }

        userAgent.setEquipmentModel(model);

        /*
         * 根据具体型号查询对应的手机品牌
         * SELECT * FROM TP_NEW_AD_MOBILE_EQUIPMENT WHERE EQUIPMENT_MODEL = #{equipmentModel,jdbcType=VARCHAR} AND IS_DEL = 0 LIMIT 1
         */
        MobileBrandModelDO mobileBrandModelDO = mobileBrandModelDAO.getBrandByEquipmentModel(model);
        if (Objects.isNull(mobileBrandModelDO)) {
            userAgent.setBrand(MobileBrandEnum.OTHER.getBrand());
            userAgent.setMobileId(MobileBrandEnum.OTHER.getMobileId());
            return userAgent;
        }

        userAgent.setBrand(mobileBrandModelDO.getBrand());
        userAgent.setMobileId(mobileBrandModelDO.getMobileId());
        return userAgent;
    }
}