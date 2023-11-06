package com.sh3h.hotline.util;

public class StringCheckUtils {

  public static boolean isNumber(String s) {
    return isInt(s) || isDouble(s);
  }

  public static boolean isDouble(String s) {

    try {
      Double.parseDouble(s);
      return true;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return false;
    }

  }

  public static boolean isInt(String s) {

    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return false;
    }

  }

}
