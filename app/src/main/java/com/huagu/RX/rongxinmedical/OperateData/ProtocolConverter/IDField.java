package com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter;

import java.util.HashMap;
import java.util.Map;

public class IDField extends ByteUtil {

	/* =================Common 部分=================== */
	public static String getKeyByValue(Map<String, Object> map, Object value) {
		int inval = (byte) value & 0xff;
		for (Map.Entry<String, Object> m : map.entrySet()) {
			if (inval == (int) m.getValue()) {
				return m.getKey();
			}
		}
		return "null";
	}
	public enum RetCode {
		RetOK,        // 解码OK
		RetFail,      // 解码失败，找不到起始位
		RetCheck,     // 校验出错
		RetVer,       // 协议版本不匹配
		RetHdr,       // 数据包头部，错误
		RetIncomplete // 数据包不完整，传入的数据包不够长
	}

	/* =================header 部分==================== */
	public static final String Header = "header";
	public static final String MsgID = "msg_id";
	public static final String DevID = "dev_id";
	public static final String Act = "action";
	public static final String Module = "module";
	public static final String Size = "size";
	public static final String ResultCode = "result_code";
	// action 参数
	public static final String Get = "get";
	public static final String Set = "set";
	public static final String Del = "del";
	public static final String Add = "add";
	public static final String Ack = "ack";
	private final static Map<String, Object> ActMap = new HashMap<String, Object>();
	static {
		ActMap.put(Get, 0x01);
		ActMap.put(Set, 0x02);
		ActMap.put(Del, 0x03);
		ActMap.put(Add, 0x04);
		ActMap.put(Ack, 0x1f);
	}
	
	// action与 byte转换
	public static byte act2id(String act) {
		int id = (int) ActMap.get(act);
		return (byte) id;
	}
	
	public static String id2act(byte id) {
		return getKeyByValue(ActMap, id);
	}
	
	// module 参数
	public static final int LoginId = 0x11;
	public static final int ProfileId = 0x12;
	public static final int BeatId = 0x13;
	public static final int UpdateId = 0x14;
	public static final int SyncId = 0x15;
	public static final int MonitorId = 0x21;
	public static final int StatsId = 0x22;
	public static final int EventId = 0x23;
	public static final int AlarmId = 0x24;
	public static final int PrescriptionId = 0x41;
	public static final int ComfortId = 0x42;
	public static final int SetingId = 0x43;

	public static final String Login = "login";
	public static final String Profile = "profile";
	public static final String Beat = "beat";
	public static final String Update = "update";
	public static final String Sync = "sync";
	public static final String Monitor = "monitor";
	public static final String Stats = "stats";
	public static final String Alarm = "alarm";
	public static final String Event = "event";

	public static final String Prescription = "prescription";
	public static final String Comfort = "comfort";
	public static final String Seting = "seting";
	private final static Map<String, Object> ModuleMap = new HashMap<String, Object>();
	static {
		ModuleMap.put(Login, LoginId);
		ModuleMap.put(Profile, ProfileId);
		ModuleMap.put(Beat, BeatId);
		ModuleMap.put(Update, UpdateId);
		ModuleMap.put(Sync, SyncId);
		ModuleMap.put(Monitor, MonitorId);
		ModuleMap.put(Stats, StatsId);
		ModuleMap.put(Event, EventId);
		ModuleMap.put(Alarm, AlarmId);
		ModuleMap.put(Prescription, PrescriptionId);
		ModuleMap.put(Comfort, ComfortId);
		ModuleMap.put(Seting, SetingId);
	}

	// module与 byte转换
	public static byte module2id(String module) {
		int id = (int) ModuleMap.get(module);
		return (byte) id;
	}

	public static String id2module(byte id) {
		return getKeyByValue(ModuleMap, id);
	}

	/* ===================body 部分======================= */
	public static final String Body = "body";
	/* ===================login========================== */
	public static final String ProductionDate = "PRODUCTION_DATE";
	public static final String Ver = "VERSION";
	public static final String Model = "MODEL";

	public static String getProductionDate(byte[] payload, int len) {
		if (len < 4)
			return "";
		return getTimeStamp(payload, 0);
	}

	public static String getVer(byte[] payload, int len) {
		if (len < 8)
			return "";
		long ver = ntohl(payload, 4);
		return ver + "";
	}

	public static String getModel(byte[] payload, int len) {
		if (len < 40)
			return "";
		return getStr(payload, 8);
	}

	/* ===================profile======================== */
	public static final String Ssid = "ssid";
	public static final String Type = "type";
	public static final String Key = "key";
	// 加密类型占时只支持这三种情况，不考虑细分类
	public static final String []SecurityType={"wpa","open","wep"};
	public static final int []SecurityTypeId ={7,0,1};
	/* ===================Sync=========================== */
	public static final String SyncLists = "sync_lists";
	/* ===================beat=========================== */
	public static final String BeatCnt = "beat_count";

	public static long getBeatCnt(byte[] payload, int len) {
		if (len < 4)
			return 0;
		return ntohl(payload, 0);
	}
	/* ===================Sync=========================== */
	public static String getSyncMode(byte[] payload, int len) {
		if (len < 1)
			return "";
		byte mode = payload[0];
		if(mode == HistoryId)
		{
			return History;
		}else if(mode == RealId){
			return Real;
		}else {
			return "";
		}
	}
	/* ===================stats=========================== */
	public static final String Ventid = "ventid";
	public static final String TimeStamp = "timestamp";
	public static final String Usage = "usage";
	public static final String UsageHumid = "usage_humid";
	public static final String TimePB = "TIMEPB";
	public static final String AHI = "AHI";
	public static final String OAI = "OAI";
	public static final String CAI = "CAI";
	public static final String AI = "AI";
	public static final String HI = "HI";
	public static final String RERA = "RERA";
	public static final String SNI = "SNI";
	public static final String VentMode = "VENTMODE";
	public static final String BreathCount = "BREATH_COUNT";
	public static final String OneselfBreathCount = "ONESELF_BREATH_COUNT";
	public static final String VentModeTab[]=
	{
		"Standby",
		"CPAP",
		"CPAP-V",
		"APAP",
		"APAP Women",
		"S",
		"Auto S",
		"S/T",
		"Auto S/T",
		"T",
		"S(30)",
		"Auto S(30)",
		"S/T(30)",
		"Auto S/T(30)",
		"T(30)",
		"PC"
	};
	public static short getStatsVentId(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return (short) (payload[0] & 0xff);
	}

	public static String getStatsTimeStamp(byte[] payload, int len) {
		if (len < 34)
			return "";
		return getTimeStamp(payload, 1);
	}

	public static long getStatsUsage(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohl(payload, 5);
	}

	public static long getStatsUsageHumid(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohl(payload, 9);
	}

	public static long getStatsTimePB(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohl(payload, 13);
	}

	public static short getStatsAHI(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohs(payload, 17);
	}

	public static short getStatsOAI(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohs(payload, 19);
	}

	public static short getStatsCAI(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohs(payload, 21);
	}

	public static short getStatsAI(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohs(payload, 23);
	}

	public static short getStatsHI(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohs(payload, 25);
	}

	public static short getStatsRERA(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohs(payload, 27);
	}

	public static short getStatsSNI(byte[] payload, int len) {
		if (len < 34)
			return 0;
		return ntohs(payload, 29);
	}
	public static String getStatsVentMode(byte[] payload, int len) {
		short id;
		if (len < 39)
			return "";
		id=ntohs(payload, 31);
		if(id>=0&&id<16){
			return VentModeTab[id];
		}
		else {
			return "";
		}
	}
	public static long getStatsBreathCount(byte[] payload, int len) {
		if (len < 39)
			return 0;
		return ntohl(payload, 33);
	}
	public static long getStatsOneselfBreathCount(byte[] payload, int len) {
		if (len < 39)
			return 0;
		return ntohl(payload, 37);
	}
	/* ===================alarm=========================== */
	public static final String AlarmLists = "alarm_lists";
	public static final String Mode       = "mode";
	public static final byte   RealId     = 1;
	public static final byte   HistoryId  = 0;
	public static final String Real       = "REAL";
	public static final String History    = "HISTORY";
	public static final String Duration   = "DURATION";
	public static final String Alarmid    = "ALARM_ID";
	public static final String Grade      = "GRADE";
	public static final int AlarmItemSize = 11;
	enum AlarmidEnum {
		ALARM_TUBING_MASK_OFF ((byte)0x31) , 
		ALARM_LEAK_HIGH       ((byte)0x32) ,
		ALARM_APNEA           ((byte)0x33) , 
		ALARM_PRESS_HIGH      ((byte)0x34) , 
		ALARM_PRESS_LOW       ((byte)0x35) , 
		ALARM_MASK_BLOCK      ((byte)0x36) , 
		ALARM_RR_HIGH         ((byte)0x37) , 
		ALARM_RR_LOW          ((byte)0x38) ,
		ALARM_VT_LOW          ((byte)0x39) , 
		ALARM_MV_LOW          ((byte)0x40) , 
		ALARM_WATER_LEVEL_LOW ((byte)0x41) ,
		ALARM_ABORT_V         ((byte)0x42) ,
		ALARM_HEAT_MODULE_HIGH((byte)0x43)
		; 
	    private byte value = 0;
	    private AlarmidEnum(byte value) {   
	    	this.value = value;
	    }
		public byte getValue() {
			return value;
		}
	}
	enum GradeEnum {
		ALARM0 ((byte)0x00) , 
		ALARM1 ((byte)0x01) 
		; 
	    private byte value = 0;
	    private GradeEnum(byte value) {   
	    	this.value = value;
	    }
		public byte getValue() {
			return value;
		}
	}

	public static String getAlarmMode(byte[] payload, int len) {
		if (len < 1)
			return "";
		byte mode = payload[0];
		if (mode == HistoryId) {
			return History;
		} else if (mode == RealId) {
			return Real;
		} else {
			return "";
		}
	}

	public static int getAlarmNum(byte[] payload, int len) {
		int num;
		if (len < 2)
			return 0;
		num = (int) (payload[1] & 0xff);
		if ((num * AlarmItemSize) > (len - 2))
			return 0;
		return num;
	}

	public static short getAlarmVentId(byte[] payload, int index) {
		return (short) (payload[index + 2] & 0xff);
	}

	public static short getAlarmId(byte[] payload, int index) {
		return (short) (payload[index + 3] & 0xff);
	}

	public static short getAlarmGrade(byte[] payload, int index) {
		return (short) (payload[index + 4] & 0xff);
	}

	public static String getAlarmTimeStamp(byte[] payload, int index) {
		return getTimeStamp(payload, index + 5);
	}

	public static String getAlarmDuration(byte[] payload, int index) {
		return getTimeStamp(payload, index + 9);
	}
	/* ===================event=========================== */
	public static final String Eventid    = "EVENT_ID";
	public static final String EventLists = "event_lists";
	public static final int EventItemSize  = 10;
	enum EventidEnum {
		EVENT_OA              ((byte)0x11) , 
		EVENT_CA              ((byte)0x12) ,
		EVENT_HY              ((byte)0x13) , 
		EVENT_FL              ((byte)0x14) , 
		EVENT_WAKE            ((byte)0x15) , 
		EVENT_PB              ((byte)0x16) , 
		EVENT_SNORE           ((byte)0x17) , 
		; 
	    private byte value = 0;
	    private EventidEnum(byte value) {   
	    	this.value = value;
	    }
		public byte getValue() {
			return value;
		}
	}
	public static String getEventMode(byte[] payload, int len) {
		if (len < 1)
			return "";
		byte mode = payload[0];
		if(mode == HistoryId)
		{
			return History;
		}else if(mode == RealId){
			return Real;
		}else {
			return "";
		}
	}

	public static int getEventNum(byte[] payload, int len) {
		int num;
		if (len < 2)
			return 0;
		num = (int) (payload[1] & 0xff);
		if ((num * EventItemSize) > (len - 2))
			return 0;
		return num;
	}

	public static short getEventVentId(byte[] payload, int index) {
		return (short) (payload[index + 2] & 0xff);
	}

	public static short getEventId(byte[] payload, int index) {
		return (short) (payload[index + 3] & 0xff);
	}

	public static String getEventTimeStamp(byte[] payload, int index) {
		return getTimeStamp(payload, index + 4);
	}

	public static String getEventDuration(byte[] payload, int index) {
		return getTimeStamp(payload, index + 8);
	}
	/* ===================monitor=========================== */
	public static final String Rate          = "rate";
	public static final String CollectNum    = "collect_num";
	public static final String MonitorLists  = "monitor_lists";
	public static final int MonitorHdrSize   = 10;
	public static final int MonitorDataSize  = 2;
	public static final Double Coeff          = 100.00D;
	public static final int MonitorPressId   = 0x00;
	public static final int IPAPId           = 0x01;
	public static final int EPAPId           = 0x02;
	public static final int VtId             = 0x03;
	public static final int LeakId           = 0x04;
	public static final int MvId             = 0x05;
	public static final int RRId             = 0x06;
	public static final int TiId             = 0x07;
	public static final int IEId             = 0x08;
	public static final int SpO2Id           = 0x09;
	public static final int PRId             = 0x0a;
	public static final int PressureId       = 0x0b;
	public static final int FlowId           = 0x0c;
	private final static Map<String, Object> MonitorMap = new HashMap<String, Object>();
	static {
		MonitorMap.put("MONITOR_PRESS", MonitorPressId);
		MonitorMap.put("IPAP", IPAPId);
		MonitorMap.put("EPAP", EPAPId);
		MonitorMap.put("VT", VtId);
		MonitorMap.put("LEAK", LeakId);
		MonitorMap.put("MV", MvId);
		MonitorMap.put("RR", RRId);
		MonitorMap.put("TI", TiId);
		MonitorMap.put("IE", IEId);
		MonitorMap.put("SPO2", SpO2Id);
		MonitorMap.put("PR", PRId);
		MonitorMap.put("PRESSURE", PressureId);
		MonitorMap.put("FlOW", FlowId);
	}

	// monitor与 byte转换
	public static byte monitor2id(String act) {
		return (byte) MonitorMap.get(act);
	}

	public static String id2monitor(byte id) {
		return getKeyByValue(MonitorMap, id);
	}
	public static boolean ChannelIsFloat(int ch) {
		if(ch== VtId || ch == LeakId || ch == RRId || ch == SpO2Id || ch == PRId){
			return false;
		}else {
			return true;
		}
	}
	public static String getMonitorMode(byte[] payload, int len) {
		if (len < MonitorHdrSize)
			return "";
		byte mode = payload[0];
		if (mode == HistoryId) {
			return History;
		} else if (mode == RealId) {
			return Real;
		} else {
			return "";
		}
	}

	public static short getMonitorVentId(byte[] payload, int len) {
		if (len < MonitorHdrSize)
			return 0;
		return (short) (payload[1] & 0xff);
	}

	public static String getMonitorTimeStamp(byte[] payload, int len) {
		if (len < MonitorHdrSize)
			return "";
		return getTimeStamp(payload, 2);
	}

	public static double getMonitorRate(byte[] payload, int len) {
		if (len < MonitorHdrSize)
			return 0;
		short raw = ntohs(payload, 6);
		double rate = raw/Coeff;
		return rate;
	}

	public static int getMonitorCollectNum(byte[] payload, int len) {
		int collect_num;
		int channel_num;
		if (len < MonitorHdrSize)
			return 0;
		collect_num = (int) (payload[8] & 0xff);
		channel_num = (int) (payload[9] & 0xff);
		if ((((collect_num * MonitorDataSize)+1)*channel_num) > (len - MonitorHdrSize))
			return 0;
		return collect_num;
	}
	public static int getMonitorChannelNum(byte[] payload, int len) {
		int collect_num;
		int channel_num;
		if (len < 10)
			return 0;
		collect_num = (int) (payload[8] & 0xff);
		channel_num = (int) (payload[9] & 0xff);
		if ((((collect_num * MonitorDataSize)+1)*channel_num) > (len - 10))
			return 0;
		return channel_num;
	}
	public static byte getChannelId(byte[] payload, int ch,int collect_num) {
		int index = ((collect_num * MonitorDataSize)+1)*ch;
		return payload[index + MonitorHdrSize+0];
	}

	public static double getMonitorData(byte[] payload, int ch,int collect,int collect_num) {
		int index = ((collect_num * MonitorDataSize)+1)*ch + collect*MonitorDataSize+1;
		short raw = ntohs(payload, MonitorHdrSize+index);
		if(ChannelIsFloat(ch))
		{
			double data = raw/Coeff;
			return data;
		}
		else {
			double data = raw/1.00;
			return data;
		}
	}
}
