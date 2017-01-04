package com.huagu.RX.rongxinmedical.OperateData.spring;
//import java.util.*;

import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.IDField;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.ToJson;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.ToPacket;

import org.json.JSONException;

import java.io.*;
import java.net.*;

public class UpdServ {
    static final int INPORT = 3301;
    private byte[] buf = new byte[1024];
    private DatagramPacket req = new DatagramPacket(buf, buf.length);
    private DatagramSocket socket;
    private ToJson reqjson = new ToJson();
    private ToPacket resp = new ToPacket();
    public String toString(byte[] b,int len) {

        return Hex.byte2HexStr(b,len);
    }
    public UpdServ() throws JSONException {
        try {
            socket = new DatagramSocket(INPORT);// 创建一接收消息的对象，而不是每次接收消息都创建一个
            System.out.println("Server started");
            while (true) {
                socket.receive(req);
                //接收到客户端的消息
                String rcvd = toString(req.getData(),req.getLength()) + ",from address:"
                        + req.getAddress() + ",port:" + req.getPort();
                System.out.println("From Client:"+rcvd);
                IDField.RetCode retCode = reqjson.parse(req.getData(),req.getLength());
                if( retCode == IDField.RetCode.RetOK)
                {
                    System.out.println("josn parse:"+reqjson.toString());
                }
                else
                {
                    System.out.println("josn parse fail"+retCode);
                }
                //retCode = resp.build(reqjson.getroot());
                //if( retCode == RetCode.RetOK)
                //{
                //	System.out.println("resp:"+toString(resp.getData(),resp.getLength()));
                //}
            }
        } catch (SocketException e) {
            System.err.println("Can't open socket");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Communication error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JSONException {
        new UpdServ();
    }
}