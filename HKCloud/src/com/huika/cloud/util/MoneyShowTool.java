package com.huika.cloud.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author fanxing 创建于 Dec 17, 2014
 */
public class MoneyShowTool {
  private static DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");// 格式化设置
  private static DecimalFormat twolastDF = new DecimalFormat("#0.00");

  private static DecimalFormat nolastDF = new DecimalFormat("#0");
  private static DecimalFormat formatnolastDF = new DecimalFormat("#,##0");

  /** 单独后面两位小数点 */

  public static String format(double d) {
    return decimalFormat.format(d);
  }

  public static String twolastDF(double d) {
    return twolastDF.format(d);
  }

  public static String nolastDF(double d) {
    return nolastDF.format(d);
  }

  public static String formatNolastDF(double d) {
    return formatnolastDF.format(d);
  }

  /**
   * @description：是否需要进行四舍五入 flase 需要 true 不需要
   * @author zc
   * @date 2015年7月10日 下午2:26:03
   */
  public static String twolastDF(double d, boolean isDown) {
    if (isDown) {
      twolastDF.setRoundingMode(RoundingMode.DOWN);
    } else {
      twolastDF.setRoundingMode(RoundingMode.HALF_UP);
    }
    return twolastDF.format(d);
  }
}
