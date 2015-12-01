package com.mine.base.commom;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * project     Ants
 *
 * @author hewei
 * @verstion 15/11/30
 */
public class VadidationUtils {

    public static final int ERR_NULL = -1;
    public static final int ERR_NO = 0;
    public static final int ERR_EMAIL = 1;
    public static final int ERR_URL = 2;
    public static final int ERR_PHONE = 3;
    public static final int ERR_ID = 4;
    public static final int ERR_DIGIT = 5;
    public static final int ERR_PASSWORD = 6;

    private static final Map<Integer, String> sResultStatus;

    private List<Integer> failuers = new ArrayList<Integer>();

    private VadidateMethord vadidateMethord = new VadidateMethord();
    private VadidationMsg vadidationMsg = new VadidationMsg();
    private String errMsg = "";

    static {
        sResultStatus = new HashMap<Integer, String>();
        sResultStatus.put(ERR_NULL, "参数为空");
        sResultStatus.put(ERR_NO, "参数正确");
        sResultStatus.put(ERR_URL, "url不合法");
        sResultStatus.put(ERR_PHONE, "手机号不合法");
        sResultStatus.put(ERR_ID, "身份证不合法");
        sResultStatus.put(ERR_DIGIT, "不是全数字");
        sResultStatus.put(ERR_PASSWORD, "密码不合法");
    }

    private String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    private String getAttriValue(Object o, String attiName) {

        if (o != null) {
            Method m = null;
            try {
                m = (Method) o.getClass().getMethod(
                        "get" + getMethodName(attiName));
                String val = String.valueOf(m.invoke(o));// 调用getter方法获取属性值
                if (val != null) {
                    return val;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public boolean make(Map<String, String> rules, Object o) {
        if (rules == null) {
            throw new IllegalArgumentException("rules is null");
        }

        failuers.clear();

        Iterator<Map.Entry<String, String>> entries = rules.entrySet().iterator();

        while (entries.hasNext()) {

            Map.Entry<String, String> entry = entries.next();
            String[] splitRules = entry.getValue().split("\\|");
            String keyAttri = entry.getKey();

            String value = getAttriValue(o, keyAttri);

            if (value == null){
                continue;
            }

            for(String str : splitRules){
                Method m = null;
                Method errM = null;
                try {
                    m = (Method) vadidateMethord.getClass().getMethod(str, String.class);
                    errM = (Method) vadidationMsg.getClass().getMethod(str);
                    Integer val = (Integer) m.invoke(vadidateMethord, value);
                    String msg =  String.valueOf(errM.invoke(vadidationMsg));


                    if (val != ERR_NO){
                        failuers.add(val);
                        errMsg += msg + ",";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (failuers.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getErrorMsg(){
        return errMsg;
    }

    class VadidateMethord {
        public final String EMAIL = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
        public final String URL = "[a-zA-z]+://[^\\s]*";
        public final String PHONE_NO = "^[0-9]{6,11}$";
        public final String ID = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
        public final String PASSWORD = "$\\w{6,18}$";

        public int defaults(String vadidationStr) {
            if (vadidationStr == null || vadidationStr.equals("")){
                return ERR_NULL;
            } else {
                return ERR_NO;
            }
        }

        public int digit(String vadidationStr) {
            if (vadidationStr == null || vadidationStr.equals("")){
                return ERR_DIGIT;
            } else {
                return ERR_NO;
            }
        }

        public int password(String vadidationStr) {
            if (vadidationStr == null || vadidationStr.equals("")){
                return ERR_NULL;
            }

            Pattern p = Pattern.compile(EMAIL, Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(vadidationStr);
            if (matcher.find()) {
                return ERR_NO;
            } else {
                return ERR_PASSWORD;
            }
        }

        public int email(String vadidationStr) {
            if (vadidationStr == null || vadidationStr.equals("")){
                return ERR_NULL;
            }

            Pattern p = Pattern.compile(EMAIL, Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(vadidationStr);
            if (matcher.find()) {
                return ERR_NO;
            } else {
                return ERR_EMAIL;
            }
        }

        public int url(String vadidationStr) {
            if (vadidationStr == null || vadidationStr.equals("")){
                return ERR_NULL;
            }

            Pattern p = Pattern.compile(URL, Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(vadidationStr);
            if (matcher.find()) {
                return ERR_NO;
            } else {
                return ERR_URL;
            }
        }

        public int phone(String vadidationStr) {
            if (vadidationStr == null || vadidationStr.equals("")){
                return ERR_NULL;
            }

            Pattern p = Pattern.compile(PHONE_NO, Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(vadidationStr);
            if (matcher.find()) {
                return ERR_NO;
            } else {
                return ERR_PHONE;
            }
        }

        public int id(String vadidationStr) {
            if (vadidationStr == null || vadidationStr.equals("")){
                return ERR_NULL;
            }

            Pattern p = Pattern.compile(ID, Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(vadidationStr);
            if (matcher.find()) {
                return ERR_NO;
            } else {
                return ERR_ID;
            }
        }
    }

    class VadidationMsg {

        public String defaults() {
            return sResultStatus.get(ERR_NULL);
        }

        public String digit() {
            return sResultStatus.get(ERR_DIGIT);
        }

        public String password() {

            return sResultStatus.get(ERR_PASSWORD);

        }

        public String email() {
            return sResultStatus.get(ERR_EMAIL);
        }

        public String url() {
            return sResultStatus.get(ERR_URL);
        }

        public String phone() {
            return sResultStatus.get(ERR_PHONE);
        }

        public String id() {
            return sResultStatus.get(ERR_ID);
        }
    }
}
