package com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class TimeUtil {
	/**
	 * ʱ���ת�������ڸ�ʽ�ַ���
	 * 
	 * @param seconds
	 *            ��ȷ������ַ���
	 * @param formatStr
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * ���ڸ�ʽ�ַ���ת����ʱ���
	 * 
	 * @param date
	 *            �ַ�������
	 * @param format
	 *            �磺yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2TimeStamp(String date_str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * ȡ�õ�ǰʱ�������ȷ���룩
	 * 
	 * @return
	 */
	public static String timeStamp() {
		long time = System.currentTimeMillis();
		String t = String.valueOf(time / 1000);
		return t;
	}

	// ��������
	// timeStamp=1417792627
	// date=2014-12-05 23:17:07
	// 1417792627
/*	
	public static void main(String[] args) {
		String timeStamp = timeStamp();
		System.out.println("timeStamp=" + timeStamp);

		String date = timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss");
		System.out.println("date=" + date);

		String timeStamp2 = date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss");
		System.out.println(timeStamp2);
	}
*/
}
