package com.yellowseed.utils;

/**
 * Created by Ashutosh Kumar on 17/12/16.
 */

public class StringUtils {


    private StringUtils() {
        throw new Error("U will not able to instantiate it");
    }


    /**
     * Determines whether the string is null or a length of 0
     *      *
     *      * @param s String to be verified
     *      * @return {@code true}: Empty {@code false}: Not empty
     */
    private static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * Determine whether the string is null or all spaces
     *      *
     *      * @param s String to be verified
     *      * @return {@code true}: null or full space <br> {@code false}: not null and not all spaces
     */
    public static boolean isBlank(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * Determine whether two strings are equal
     *      *
     *      * @param a to be verified string a
     *      * @param b String to be verified b
     *      * @return {@code true}: Equal to {@code false}: Not equal
     */
    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

//     /**
//      * Check whether the two strings are case-insensitive
//      *
//      * @param a to be verified string a
//      * @param b String to be verified b
//      * @return {@code true}: Equal to {@code false}: Not equal
//      */
//     public static boolean equalsIgnoreCase(String a, String b) {
//        return (a == b) || (b != null) && (a.length() == b.length()) && a.regionMatches(true, 0, b, 0, b.length());
//    }

    /**
     * Null to a string of length 0
     *      *
     *      * @param s to be transferred to the string
     *      * @return s is null to a string of length 0, otherwise it will not change
     */
    public static String null2Length0(String s) {
        return s == null ? "" : s;
    }

    /**
     * Returns the length of the string
     *      *
     *      * @param s String
     *      * @return null Returns 0, others returns its own length
     */
    private static int length(CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * Initial capitalization
     *      *
     *      * @param s to be transferred to the string
     *      * @return The first letter of the uppercase string
     */
    public static String upperFirstLetter(String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * First letter lowercase
     *      *
     *      * @param s to be transferred to the string
     *      * @return The first letter of the lowercase string
     */
    public static String lowerFirstLetter(String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     *      *
     *      * @param s To reverse the string
     *      * @return Reverse the string
     */
    public static String reverse(String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }


}
