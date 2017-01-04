package com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ToJson extends IDField {
	private JSONObject root;

	public ToJson() {
		root = new JSONObject();
	}
	public JSONObject getroot() {
		return root;
	}
	private class Format extends ByteUtil {
		byte stx;
		byte version;
		byte msgid;
		byte action;
		byte module;
		String devid;
		short len;
		byte []payload;
		short check;

		public Format() {
			stx = (byte) Stx;
		}

		public RetCode analysis(byte[] buf, int size) {
			int offset = 0;
			do {
				if (buf[offset++] != stx) {
					continue;
				}
				if ((size - offset) > (PktMinSize - 1)) {
					version = (byte) (buf[offset] >> 5);
					if (version != ProtocolVer) // 版本匹配
					{
						return RetCode.RetVer;
					}
					action = (byte) (buf[offset++] & 0x1f);
					msgid = buf[offset++];
					devid = getDevid(buf, offset);
					offset += DevidSize;
					module = buf[offset++];
					len = ntohs(buf, offset);
					offset += 2;
					if (len > PayloadMaxLen) // 有效数据太长
					{
						return RetCode.RetHdr;
					}
					if (len > (size - offset)) // 数据包不完整，还需接收更多数据
					{
						return RetCode.RetIncomplete;
					}
					payload = new byte[len];
					System.arraycopy(buf, offset, payload, 0, len);
					offset += len;
					check = ntohs(buf, offset);
					if (Check(payload,0,len) == check) {
						return RetCode.RetOK;
					}
				} else // 输入数据长度太短，直接返回空
				{
					return RetCode.RetIncomplete;
				}
			} while (offset != size);
			return RetCode.RetFail;
		}
	}

	public RetCode parse(byte[] buf, int size) throws JSONException {
		Format format = new Format();

		RetCode retCode;
		retCode = format.analysis(buf, size);
		if (retCode == RetCode.RetOK) {
			root.put(Header, CreateHeader(format,size));
			root.put(Body, CreateBody(format));
		}
		// to JSON
		return retCode;
	}

	public String toString() {
		return root.toString();
	}

	private JSONObject CreateHeader(Format format,int size) throws JSONException {
		JSONObject header = new JSONObject();
		int msgid = (int) (format.msgid&0xff);
		header.put(MsgID, msgid);
		header.put(DevID, format.devid);
		header.put(Act, id2act(format.action));
		header.put(Module, id2module(format.module));
		header.put(Size, size);
		return header;
	}

	private JSONObject CreateBody(Format format) throws JSONException {
		switch (format.module) {
		case LoginId:
			return CreateBodyLogin(format);
		case BeatId:
			return CreateBodyBeat(format);
		case SyncId:
			return CreateBodySync(format);
		case StatsId:
			return CreateBodyStats(format);
		case AlarmId:
			return CreateBodyAlarm(format);
		case EventId:
			return CreateBodyEvent(format);
		case MonitorId:
			return CreateBodyMonitor(format);
		case ProfileId:
			return CreateBodyProfile(format);		
		default:
			break;
		}
		return new JSONObject();
	}

	private JSONObject CreateBodyLogin(Format format) throws JSONException {
		JSONObject body = new JSONObject();
		String timestamp = getProductionDate(format.payload, format.len);
		body.put(ProductionDate, timestamp);
		// String date = TimeUtil.timeStamp2Date(timestamp, "yyyy-MM-dd
		// HH:mm:ss");
		// System.out.println("date=" + date);
		body.put(Ver, getVer(format.payload, format.len));
		body.put(Model, getModel(format.payload, format.len));
		return body;
	}

	private JSONObject CreateBodyBeat(Format format) throws JSONException {
		JSONObject body = new JSONObject();
		body.put(BeatCnt, getBeatCnt(format.payload, format.len));
		return body;
	}
	private JSONObject CreateBodySync(Format format) throws JSONException {
		JSONObject body = new JSONObject();
		body.put(Mode, getSyncMode(format.payload, format.len));
		return body;
	}
	private JSONObject CreateBodyStats(Format format) throws JSONException {
		JSONObject body = new JSONObject();
		body.put(Ventid, getStatsVentId(format.payload, format.len));
		body.put(TimeStamp, getStatsTimeStamp(format.payload, format.len));
		body.put(Usage, getStatsUsage(format.payload, format.len));
		body.put(UsageHumid, getStatsUsageHumid(format.payload, format.len));
		body.put(TimePB, getStatsTimePB(format.payload, format.len));
		body.put(AHI, getStatsAHI(format.payload, format.len));
		body.put(OAI, getStatsOAI(format.payload, format.len));
		body.put(CAI, getStatsCAI(format.payload, format.len));
		body.put(AI, getStatsAI(format.payload, format.len));
		body.put(HI, getStatsHI(format.payload, format.len));
		body.put(RERA, getStatsRERA(format.payload, format.len));
		body.put(SNI, getStatsSNI(format.payload, format.len));
		body.put(VentMode, getStatsVentMode(format.payload, format.len));
		body.put(BreathCount, getStatsBreathCount(format.payload, format.len));
		body.put(OneselfBreathCount, getStatsOneselfBreathCount(format.payload, format.len));
		return body;
	}

	private JSONObject CreateBodyAlarm(Format format) throws JSONException {
		JSONObject body = new JSONObject();
		JSONArray array = new JSONArray();
		Map<String, Object> params = new HashMap<String, Object>();
		int alarm_num = getAlarmNum(format.payload, format.len);
		for (int index = 0; index < alarm_num * AlarmItemSize; index += AlarmItemSize) {
			params.put(Ventid, getAlarmVentId(format.payload, index));
			params.put(Alarmid, getAlarmId(format.payload, index));
			params.put(Grade, getAlarmGrade(format.payload, index));
			params.put(TimeStamp, getAlarmTimeStamp(format.payload, index));
			params.put(Duration, getAlarmDuration(format.payload, index));
			array.put(params);
		}
		body.put(Mode, getAlarmMode(format.payload, format.len));
		body.put(AlarmLists, array);
		return body;
	}

	private JSONObject CreateBodyEvent(Format format) throws JSONException {
		JSONObject body = new JSONObject();
		JSONArray array = new JSONArray();
		Map<String, Object> params = new HashMap<String, Object>();
		int event_num = getEventNum(format.payload, format.len);
		for (int index = 0; index < event_num * EventItemSize; index += EventItemSize) {
			params.put(Ventid, getEventVentId(format.payload, index));
			params.put(Eventid, getEventId(format.payload, index));
			params.put(TimeStamp, getEventTimeStamp(format.payload, index));
			params.put(Duration, getEventDuration(format.payload, index));
			array.put(params);
		}
		body.put(Mode, getEventMode(format.payload, format.len));
		body.put(EventLists, array);
		return body;
	}

	private JSONObject CreateBodyMonitor(Format format) throws JSONException {
		JSONObject body      = new JSONObject();
		JSONArray ch_array   = new JSONArray();
		//JSONArray data_array = null;
		Map<String, Object> channel = null;
		int  collect_num = getMonitorCollectNum(format.payload, format.len);
		int  channel_num = getMonitorChannelNum(format.payload, format.len);
		for (int ch = 0; ch < channel_num; ch++) {
			//data_array = new JSONArray();
			String str="";
			for (int collect = 0; collect < collect_num; collect++) {
				double t = getMonitorData(format.payload,ch,collect,collect_num);
				str += t+ ",";
				//data_array.put(collect,t );	
			}
			if(collect_num != 0)
			{
				str = str.substring(0, str.lastIndexOf(","));
			}
			String chStr = id2monitor(getChannelId(format.payload, ch,collect_num));
			channel = new HashMap<String, Object>();
			//channel.put(chStr,data_array);
			channel.put(chStr,str);
			ch_array.put(channel);
			channel = null;
		//	data_array = null;
		}
		body.put(Mode, getMonitorMode(format.payload, format.len));
		body.put(Ventid, getMonitorVentId(format.payload, format.len));
		body.put(TimeStamp, getMonitorTimeStamp(format.payload, format.len));
		body.put(Rate,      getMonitorRate(format.payload, format.len));
		body.put(CollectNum, collect_num);
		body.put(MonitorLists, ch_array);
		return body;
	}
private JSONObject CreateBodyProfile(Format format) throws JSONException {
	int result =  (int) (format.payload[0] & 0xff);
	JSONObject body      = new JSONObject();
	body.put(ResultCode, result);
	return body;
   }
}
