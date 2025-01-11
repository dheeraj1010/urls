package com.dheeraj.pers.urls.util;

public class UrlEncDec {

    private UrlEncDec(){
        throw new IllegalStateException("To Prevent Instantiation");
    }

    private static final String BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static final int BASE = BASE64_CHARS.length();
    public static String encodeFromNumber(long num){
        StringBuilder sb = new StringBuilder();
        while(num > 0){
            int index = (int) (num % BASE);
            sb.insert(0, BASE64_CHARS.charAt(index));
            num /= BASE;
        }

        return sb.toString();
    }

    public static long decodeFromBase64(String value){
        char[] chars = value.toCharArray();
        long num = 0;
        for(char c: chars){
            int charValue = BASE64_CHARS.indexOf(c);
            if (charValue == -1) {
                throw new IllegalArgumentException("Invalid character in Base64 string.");
            }
            num = num * BASE + charValue;
        }
        return num;
    }


    public static String urlSanitization(String url) {
        if (url.startsWith("https://") || url.startsWith("http://")) {
            return url;
        }
        return "http://" + url;
    }

}
