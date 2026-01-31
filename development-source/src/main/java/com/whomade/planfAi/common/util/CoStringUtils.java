package com.whomade.planfAi.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <PRE>
 * 공통 문자열 처리 유틸리티
 * </PRE>
 */
public class CoStringUtils {

    /**
     * 문자열이 null이거나 빈 문자열일 경우 기본값을 반환한다.
     */
    public static String nvl(String src, String defaultValue) {
        return (src == null || "".equals(src.trim())) ? defaultValue : src;
    }

    /**
     * 문자열이 null이거나 빈 문자열일 경우 빈 스트링("")을 반환한다.
     */
    public static String nvl(String src) {
        return nvl(src, "");
    }

    /**
     * 문자 길이 자르기 (말줄임표 추가)
     */
    public static String getReSize(String contents, int length) {
        if (contents != null && contents.length() > length) {
            contents = contents.substring(0, length) + "...";
        }
        return contents;
    }

    /**
     * 문자 길이 자르기 (말줄임표 없음)
     */
    public static String getReSizeRemoveDot(String contents, int length) {
        if (contents != null && contents.length() > length) {
            contents = contents.substring(0, length);
        }
        return contents;
    }

    /**
     * 개행문자를 <br/>
     * 태그로 변환
     */
    public static String getHtmlValue(String strOriData) {
        if (strOriData == null)
            return "";
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < strOriData.length(); i++) {
            char ch = strOriData.charAt(i);
            if (ch == 10 || ch == 13) {
                buffer.append("<br/>");
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }

    /**
     * HTML 특수문자 인코딩
     */
    public static String toHtmlFormat(String src) {
        if (src == null)
            return null;
        src = src.replaceAll("&", "&amp;");
        src = src.replaceAll("<", "&lt;");
        src = src.replaceAll(">", "&gt;");
        src = src.replaceAll("\"", "&quot;");
        src = src.replaceAll("\'", "&#039;");
        src = src.replaceAll("\n", "<br/>");
        src = src.replaceAll(" ", "&nbsp;");
        return src;
    }

    /**
     * HTML 태그 제거 (정규식 기반)
     */
    public static String removeHtmlTag(String srcStr) {
        if (srcStr == null || srcStr.isEmpty())
            return srcStr;
        return srcStr.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
    }

    /**
     * 3자리마다 콤마 추가
     */
    public static String setComma(String num) {
        if (num == null || num.trim().isEmpty())
            return "0";
        num = num.trim().replace(",", "");
        try {
            BigDecimal bd = new BigDecimal(num).stripTrailingZeros();
            boolean hasFraction = bd.scale() > 0;
            DecimalFormat df = new DecimalFormat(hasFraction ? "#,##0.################" : "#,##0");
            return df.format(bd);
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    /**
     * 콤마 제거
     */
    public static String stripComma(String s) {
        return s == null ? null : s.replaceAll(",", "");
    }

    /**
     * 빈 문자열 체크
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || "null".equals(str);
    }

    /**
     * 문자열을 구분자로 분리하여 리스트로 반환
     */
    public static List<String> splitList(String str, String splitStr) {
        if (str == null)
            return new ArrayList<>();
        return Arrays.asList(str.split(splitStr, -1));
    }
}
