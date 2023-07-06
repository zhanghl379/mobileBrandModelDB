import javax.servlet.http.HttpServletRequest;

/**
 * @author zhanghl
 * @version UserAgentService.java
 */
public interface UserAgentService {
    /**
     * 从request中获取UA信息
     *
     * @param request
     * @return
     */
    String getUserAgent(HttpServletRequest request);

    /**
     * 从request中解析UA信息获取手机系统与品牌
     *
     * @param request
     * @return
     */
    UserAgentResult parseUserAgent(HttpServletRequest request);

    /**
     * 从UA信息中获取手机系统与品牌
     *
     * @param ua
     * @return
     */
    UserAgentResult parseUserAgent(String ua);
}