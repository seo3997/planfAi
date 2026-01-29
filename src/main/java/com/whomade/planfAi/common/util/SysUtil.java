package com.whomade.planfAi.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import jakarta.servlet.http.HttpServletRequest;
import com.whomade.planfAi.common.vo.DataMap;

/**
 * <PRE>
 * 시스템 공통적으로 사용되는 메소드들을 갖고 있다.
 * </PRE>
 *
 * @author 박재현
 * @version $Revision: 1.2 $
 */
public class SysUtil {

    public static Random generator = new Random();
    private static final String docIdSymbols = "1234567890QWERTYUIOPASDFGHJKLZXCVBNM";

    public static String getDocId() {

        String randomId = "";

        for (int i = 0; i < 20; i++) {
            randomId = randomId + docIdSymbols.charAt(generator.nextInt(docIdSymbols.length()));
        }

        return System.currentTimeMillis() + randomId;

    }

    public static String getFileId() {

        String randomId = "";

        for (int i = 0; i < 25; i++) {
            randomId = randomId + docIdSymbols.charAt(generator.nextInt(docIdSymbols.length()));
        }

        return System.currentTimeMillis() + randomId;

    }

    public static String newLineForEapprovForHwp(String strOriData) {
        StringBuffer buffer = new StringBuffer();
        strOriData = strOriData.replaceAll("\\\\", "￦");
        for (int i = 0; i < strOriData.length(); i++) {

            int intCode = String.valueOf(strOriData.charAt(i)).hashCode();
            if (intCode == 10 || intCode == 13) {
                buffer.append("\\r\\n");
                i++;
            } else if (intCode == 34) // ["]
            {
                buffer.append("\\\"");
            } else if (intCode == 39) // [']
            {
                buffer.append("\\\'");
            } else {
                buffer.append(String.valueOf(strOriData.charAt(i)));
            }
        }
        String strReturn = buffer.toString();

        return strReturn;
    }

    public static String fileImg(String fileName) {
        String[] fileGif = { "bmp", "doc", "etc", "exe", "gif", "gul", "htm", "html", "hwp", "ini", "jpg", "mgr", "mpg",
                "pdf", "ppt", "print", "tif", "txt", "wav", "xls", "xml",
                "zip" };
        if (fileName == null || "".equals(fileName)) {
            return "";
        }

        int start = fileName.lastIndexOf(".");
        String name = "";
        if (start > -1) {
            name = fileName.substring(start + 1).toLowerCase();
        }

        boolean retFlag = false;
        for (int fileInx = 0; fileInx < fileGif.length; fileInx++) {
            if (name.equals(fileGif[fileInx])) {
                retFlag = true;
                break;
            }
        }
        String retStr = "";
        if (retFlag) {
            retStr = "<img src='/zz/img/attach_" + name + ".gif'>";
        } else {
            retStr = "<img src='/zz/img/icon_doc.gif'>";
        }
        return retStr;

    }

    public static boolean fileImgyn(String fileExmName) {
        boolean retFlag = false;
        String[] fileGif = { "bmp", "gif", "jpg", "png" };

        if (fileExmName == null || "".equals(fileExmName)) {
            return retFlag;
        }

        String exmName = fileExmName.toLowerCase();

        for (int fileInx = 0; fileInx < fileGif.length; fileInx++) {
            if (exmName.equals(fileGif[fileInx])) {
                retFlag = true;
                break;
            }
        }

        return retFlag;

    }

    /*
     * 파일 명을 구해온다
     */
    public static String getFileName(String fileName) {

        String stringStr = "";

        if (fileName.lastIndexOf(".") != -1) {
            stringStr = fileName.substring(0, fileName.lastIndexOf("."));
        }

        return stringStr;
    }

    /*
     * 파일 확장자를 구해온다
     */
    public static String getFileExtName(String fileName) {

        String stringStr = "";

        if (fileName.lastIndexOf(".") != -1) {
            stringStr = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        }

        return stringStr;
    }

    /**
     * <PRE>
     * 페이지 리다이렉트시 필요한 파라메터를 생성한다.
     * </PRE>
     *
     * @author 박재현
     * @version $Revision: 1.2 $
     */
    public static String createInputObj(DataMap dataMap) {

        int max = dataMap.size() - 1;
        Iterator<Map.Entry<String, Object>> it = dataMap.entrySet().iterator();
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i <= max; i++) {

            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
            String key = entry.getKey().toString();
            String value = null;
            Object o = entry.getValue();

            if (o == null) {
                value = "";
            } else {

                Class<?> c = o.getClass();

                if (c.getName().equalsIgnoreCase("java.util.ArrayList")) {

                    @SuppressWarnings("unchecked")
                    ArrayList<String> tmpList = (ArrayList<String>) o;

                    for (int k = 0; k < tmpList.size(); k++) {

                        buf.append("<input type = \"hidden\" name = \"" + key + "\" value = \"" + tmpList.get(k)
                                + "\"/>\n");
                    }
                } else {
                    value = o.toString();
                    buf.append("<input type = \"hidden\" name = \"" + key + "\" value = \"" + value + "\"/>\n");
                }
            }
        }
        return buf.toString();
    }

    public static String byteCalculation(String bytes) {
        String retFormat = "0";
        Double size = Double.parseDouble(bytes);
        String[] s = { "bytes", "KB", "MB", "GB", "TB", "PB" };

        if (!bytes.equals("0")) {
            int idx = (int) Math.floor(Math.log(size) / Math.log(1024));
            DecimalFormat df = new DecimalFormat("#,###.##");
            double ret = ((size / Math.pow(1024, Math.floor(idx))));
            retFormat = df.format(ret) + " " + s[idx];
        } else {
            retFormat += " " + s[0];
        }
        return retFormat;
    }

    /**
     * <PRE>
     * 1. MethodName : getBrowser
     * 2. ClassName  : SysUtil
     * 3. Comment   : 브라우저 종류
     * 4. 작성자    : SooHyun.Seo
     * 5. 작성일    : 2014. 12. 29. 오전 10:00:28
     * </PRE>
     * 
     * @return String
     * @param header
     * @return
     */
    public static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        String browser = "";
        if (header.indexOf("MSIE") > -1) {
            browser = "MSIE";
        } else if (header.indexOf("rv:11.0") > -1) {
            browser = "MSIE";
        } else if (header.indexOf("Chrome") > -1 || header.indexOf("Android") > -1) {
            browser = "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            browser = "Opera";
        } else {
            browser = "Firefox";
        }

        return browser;
    }

    public static String getBrowserFileName(HttpServletRequest request, String filename) {
        String browser = getBrowser(request);
        String dispositionPrefix = "attachment; filename=";
        String encodedFilename = null;

        if (browser.equals("MSIE")) {
            try {
                encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (browser.equals("Firefox")) {
            try {
                encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (browser.equals("Opera")) {
            try {
                encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    try {
                        sb.append(URLEncoder.encode("" + c, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        }

        return dispositionPrefix + encodedFilename;
    }

}
