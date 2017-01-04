package com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter;

import org.json.*;

public class ToPacket extends IDField{
	private byte[] pkt;
	private int len;

	public ToPacket() {
		pkt = new byte[PayloadMaxLen];
	}

	public byte[] getData() {
		return pkt;
	}

	public int getLength() {
		return len;
	}

	public RetCode build(JSONObject root) throws JSONException {
		RetCode retCode;
		short payloadlen;
		JSONObject header = root.getJSONObject(Header);
		retCode = buildHeader(root.getJSONObject(Header));
		if (retCode == RetCode.RetOK) {
			retCode = buildbody(root.getJSONObject(Body),module2id(header.getString(Module))
					  ,act2id(header.getString(Act)));
		}
		payloadlen = (short) (len - PktHdrSize);
		htons(pkt,payloadlen,PktHdrSize-LengthSize);
		short check = Check(pkt,PktHdrSize,payloadlen);
		htons(pkt,check,len);
		len+=PktCheckSize;
		return retCode;
	}

	private RetCode buildHeader(JSONObject header) throws JSONException {
		RetCode retCode = RetCode.RetFail;
		len=0;
		pkt[len++] = Stx;
		pkt[len]   = ProtocolVer << 5;
		pkt[len++]|= act2id(header.getString(Act))&0x1f; 
		pkt[len++] = (byte) header.getInt(MsgID);
		byte[] dev_id = setDevid(header.getString(DevID));
		System.arraycopy(dev_id, 0, pkt, len, DevidSize);
		len+=DevidSize;
		pkt[len++] = module2id(header.getString(Module));
		len+=LengthSize;
		retCode = RetCode.RetOK;
		return retCode;
	}

	private RetCode buildbody(JSONObject body,byte module,byte act) throws JSONException {
		RetCode retCode = RetCode.RetFail;
		switch (module) {
		case LoginId:
		case AlarmId:
		case EventId:
		case BeatId:	
			pkt[len++] = (byte) body.getInt(ResultCode);
			break;
		case ProfileId:
			{
				if(act == 0x02){
					String ssid = body.getString(Ssid);
					String key = body.getString(Key);
					String keytype = body.getString(Type);
					int keyid=0;
					for (int i = 0; i < SecurityType.length; i++) {
						if(SecurityType[i].equals(keytype))
						{
							keyid = SecurityTypeId[i];
							break;
						}
					}
					// ssid
					byte []buf = new byte[32];
					byte []ssidbyte = ssid.getBytes();
					System.arraycopy(ssidbyte,0, buf, 0,ssidbyte.length);
					System.arraycopy(buf,0,pkt,len,buf.length);
					buf = new byte[64];
					// key
					byte []keybyte = key.getBytes();
					System.arraycopy(keybyte,0, buf, 0,keybyte.length);
					System.arraycopy(buf,0,pkt,len+33,buf.length);
					// keyid
					pkt[len+32] = (byte) keyid;
					len+=97;
				}
			}
			break;
		case SyncId:
			{
				JSONArray array = body.getJSONArray(SyncLists);
				JSONObject param;
				String type;
				int timestamp;
				pkt[len] = (byte) body.getInt(ResultCode);
				
				for (int i = 0; i < array.length(); i++) {
					param = array.getJSONObject(i);
					type = param.getString(Type);
					timestamp = Integer.parseInt(param.getString(TimeStamp));
					if (type == Monitor) {
						htonl(pkt, timestamp, len+1);
					} else if (type == Stats) {
						htonl(pkt, timestamp, len+5);
					} else if (type == Event) {
						htonl(pkt, timestamp, len+9);
					} else if (type == Alarm) {
						htonl(pkt, timestamp, len+13);
					}
				}
				len+=17;
			}
			break;	
		case StatsId:
		case MonitorId:	
			{
				int ret;
				int timestamp = 0;
				ret = body.getInt(ResultCode);
				pkt[len++] = (byte) ret;
				if(ret != 0)
				{
					timestamp = body.getInt(TimeStamp);
				}
				htonl(pkt, timestamp, len);
				len+=4;
			}
			break;
		default:
			return retCode;
		}
		retCode = RetCode.RetOK;
		return retCode;
	}
}
