package com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter;

public class ByteUtil {

	public static final byte Stx = (byte) 0xfd;
	public static final byte ProtocolVer = (byte) 0x01;
	public static final int PktHdrSize = 12;
	public static final int PktMinSize = 14;
	public static final int DevidSize = 6;
	public static final int LengthSize = 2;
	public static final int PktCheckSize = 2;
	public static final int PayloadMaxLen = 1024;

	public static void htons(byte[] b, short s, int index) {
		b[index + 0] = (byte) (s >> 8);
		b[index + 1] = (byte) (s >> 0);
	}

	public static short ntohs(byte[] b, int index) {
		return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
	}

	public static void htonl(byte[] b, int x, int index) {
		b[index + 0] = (byte) (x >> 24);
		b[index + 1] = (byte) (x >> 16);
		b[index + 2] = (byte) (x >> 8);
		b[index + 3] = (byte) (x >> 0);
	}

	public static long ntohl(byte[] b, int index) {
		long val = 0,val1 = 0,val2 = 0,val3 = 0;
		val = (b[index + 0] & 0xff);
		val = val << 24;
		val1 = (b[index + 1] & 0xff);
		val1 = val1 << 16;
		val2 = (b[index + 2] & 0xff);
		val2 = val2 << 8;
		val3 = (b[index + 3] & 0xff);
		val3 = val3 << 0;
		return val+val1+val2+val3;
	}

	public static String getDevid(byte[] b, int index) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = index; n < DevidSize + index; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
		}
		return sb.toString().toUpperCase().trim();
	}
	public static byte[] setDevid(String devid) {
		String str = "0123456789ABCDEF";
		char[] hexs = devid.toCharArray();
		byte[] bytes = new byte[devid.length() / 2];
		int n;

		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return bytes;
	}
	//讲获取的mac转化为指定的string
	public static String DevidToSn(String devid) {
		if(devid.length() > 4)
		{
			StringBuilder Sn = new StringBuilder("");
			char[] hexs = devid.toCharArray();
			byte[] bytes = setDevid(devid);
			char sn0 = (char) (bytes[0] - 10 + 'A');
			char sn1 = (char) (bytes[1] - 10 + 'A');
			Sn.append(String.valueOf(sn0));
			Sn.append(String.valueOf(sn1));
			Sn.append(String.valueOf('-'));
			for (int i = 4; i < hexs.length; i++) {
				Sn.append(String.valueOf(hexs[i]));
			}
			return Sn.toString().toUpperCase().trim();
		}
		else
		{
			return "";
		}
	}
	public static String getStr(byte[] b, int index) {
		int i;
		for (i = index; i < b.length; i++) {
			if (b[i] == 0)
				break;
		}
		if (i == index)
			return "";
		byte[] str = new byte[i - index];
		System.arraycopy(b, index, str, 0, i - index);
		return new String(str);
	}

	public static String getTimeStamp(byte[] b, int index) {
		return ntohl(b,index) + "";
	}

	public static short Check(byte[] bb, int index,int len) {
		// �����׶β���У��
		return (short) 0xa5a5;
	}
}
