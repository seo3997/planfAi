package com.whomade.planfAi.common.util;

import com.whomade.planfAi.common.vo.DataMap;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class RequestUtil {

    /**
     * <PRE>
     * 1. MethodName : getDataMap
     * 2. ClassName  : RequestUtil
     * 3. Comment   : request 객체의 데이터를 dataMap 에 담기
     * 4. 작성자    : SooHyun.Seo
     * 5. 작성일    : 2013. 8. 14. 오후 1:59:28
     * </PRE>
     * 
     * @return DataMap
     * @param request
     * @return
     */
    public static DataMap getDataMap(HttpServletRequest request) {

        DataMap dMap = new DataMap();
        Enumeration<?> e = request.getParameterNames();

        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String[] values = request.getParameterValues(key);
            if (values != null && values.length > 1) {
                List<Object> list = new ArrayList<Object>(values.length);
                for (int i = 0; i < values.length; i++) {
                    list.add(htmlTagFilter(values[i]));
                }
                dMap.put(key, list);

            } else {
                dMap.put(key, htmlTagFilter(request.getParameter(key)));
            }
        }

        return dMap;
    }

    public static String htmlTagFilter(String src) {

        if (src != null) {
            src = src.replaceAll("&", "&amp;");
            src = src.replaceAll("<", "&lt;");
            src = src.replaceAll(">", "&gt;");
            src = src.replaceAll("\"", "&quot;"); // 쌍따옴표
            src = src.replaceAll("\'", "&#039;"); // 작은따옴표
        }
        return src;
    }

    /**
     * <PRE>
     * 1. MethodName : getRemoteAddr
     * 2. ClassName  : RequestUtil
     * 3. Comment   : 프록시 서버를 걸쳐온 clientIP도 가져온다.
     * 4. 작성자    : SooHyun.Seo
     * 5. 작성일    : 2014. 9. 11. 오후 8:04:46
     * </PRE>
     * 
     * @return String
     * @param request
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");

        if (null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) {
            clientIp = request.getHeader("REMOTE_ADDR");
        }

        if (null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }
}
