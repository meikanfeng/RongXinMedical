/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huagu.RX.rongxinmedical.Service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.IDField;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.ToJson;
import com.huagu.RX.rongxinmedical.OperateData.ProtocolConverter.ToPacket;
import com.huagu.RX.rongxinmedical.OperateData.spring.Hex;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class UartService extends Service {
    private final static String TAG = UartService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    
    private int mConnectionState = STATE_DISCONNECTED;

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED ="com.huagu.fmk.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED ="com.huagu.fmk.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED ="com.huagu.fmk.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE ="com.huagu.fmk.ACTION_DATA_AVAILABLE";
    public final static String ACTION_DATA_Notification ="com.huagu.fmk.ACTION_DATA_Notification";
    public final static String EXTRA_DATA ="com.huagu.fmk.EXTRA_DATA";
    public final static String DEVICE_DOES_NOT_SUPPORT_UART ="com.huagu.fmk.DEVICE_DOES_NOT_SUPPORT_UART";
    public final static String DEVICE_WRITE_SUCCESS ="com.huagu.fmk.DEVICE_WRITE_SUCCESS";
    public final static String DEVICE_WRITE_FAIL ="com.huagu.fmk.DEVICE_WRITE_FAIL";
    public final static String IS_SUCCESS = "IS_SUCCESS";

    /*医疗*/
    public static final UUID SERVICE_UUID = UUID.fromString("1B7E8251-2877-41C3-B46E-CF057C562023");
    public static final UUID READ_CHAR_UUID = UUID.fromString("8AC32D3F-5CB9-4D44-BEC2-EE689169F626");//read notify indicate(读，通知，..)
    public static final UUID WRITE_CHAR_UUID = UUID.fromString("5E9BF2A8-F93F-4481-A67E-3B2F4A07891A");//read write （读，写）

    public void setdiscoverServices(){
        if(mBluetoothGatt != null){
            Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
        }
    }

    /*
    public static final UUID RX_SERVICE_UUID = UUID.fromString("00001818-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_CHAR_UUID = UUID.fromString("00002a80-0000-1000-8000-00805f9b34fb"); 
    */
    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (status == 133)close();
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.e("TAG","服务连接成功");
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                // Attempts to discover services after successful connection.
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.e("TAG","断开服务");
                if (mBluetoothGatt != null)
//                    mBluetoothGatt.disconnect();
                close();
                mBluetoothGatt = null;
                intentAction = ACTION_GATT_DISCONNECTED;
                if(mConnectionState == STATE_CONNECTED) connect(device.getAddress());
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.e("TAG","发现服务");
            if (status == BluetoothGatt.GATT_SUCCESS) {
            	Log.w(TAG, "mBluetoothGatt = " + mBluetoothGatt);
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e("TAG","获取数据成功");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.e("TAG","获取数据成功");
            byte[] txValue = characteristic.getValue();
            IDField.RetCode retCode = SuperpositionDatac(txValue);
            if (retCode == IDField.RetCode.RetOK){
                broadcastUpdate(ACTION_DATA_Notification, reqjson_OK.toString());
            }
        }
    };

    public ToJson reqjson_OK;

    public IDField.RetCode SuperpositionDatac(byte[] txValue){
        try {
            reqjson_OK = new ToJson();
            byte[] txValue1 = JointData(txValue);
            IDField.RetCode retCode = reqjson_OK.parse(txValue1, txValue1.length);
            Log.e("xxxxxxxxxxxxx","xxxxxxxxx: "+retCode);
            if(retCode == IDField.RetCode.RetOK){
                Log.e("xxxxxxxxxxxxx", reqjson_OK.toString());
                data =null; data1 =null;
                return retCode;
            }else {
                Log.e("xxxxx","xxxxxxxxx: "+retCode);
                return retCode;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return IDField.RetCode.RetFail;
    }


    private byte[] data = null;
    private byte[] data1 = null;
    public byte[] JointData(byte[] txValue) throws JSONException {
        if (data == null){
            data = new byte[txValue.length];
            data = txValue;
        }else{
            data = new byte[data1.length+txValue.length];
            for (int i=0;i<data.length;i++){
                if (i<data1.length){
                    data[i] = data1[i];
                }else{
                    data[i] = txValue[i-data1.length];
                }
            }
        }
        data1 = new byte[data.length];
        for (int i=0;i<data.length;i++){
            data1[i] = data[i];
        }
        String xxx = Hex.byte2HexStr(data1, data1.length);
        Log.e("xxxxxxxxx",xxx);
        return data1;
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,boolean boo) {
        final Intent intent = new Intent(action);
        intent.putExtra(IS_SUCCESS, boo);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (READ_CHAR_UUID.equals(characteristic.getUuid())) {
        	byte[] data=characteristic.getValue();
            //Log.d(TAG, String.format("Received TX: %d",data));
            intent.putExtra(EXTRA_DATA, data);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    private void broadcastUpdate(String action,String str) {
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_DATA, str);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    //����server���������ⲿ����
    public class LocalBinder extends Binder {
        public UartService getService() {
            return UartService.this;
        }
    }
    
    private final IBinder mBinder = new LocalBinder();
    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    public int isconnected(){
        return mConnectionState;
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        Log.e("TAG","开始找服务");
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            mBluetoothGatt.close();
        }
        device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }

        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }
    BluetoothDevice device;
    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
       // mBluetoothGatt.close();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        Log.w(TAG, "mBluetoothGatt closed");
        mBluetoothDeviceAddress = null;
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

                
        if (READ_CHAR_UUID.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(READ_CHAR_UUID);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * 写入数据到蓝牙设备
     * @param str   数据数组
     * @param type
     * @param leng
     */
    public void write(final byte[] str,final String type,final int leng){
        if (mBluetoothGatt == null) {
            showMessage("mBluetoothGatt null" + mBluetoothGatt);
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothGattService TxService = mBluetoothGatt.getService(SERVICE_UUID);
                BluetoothGattCharacteristic TxChar = TxService.getCharacteristic(WRITE_CHAR_UUID);
                if (TxChar == null) {
                    showMessage("Tx charateristic not found!");
                    broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
                    return;
                }
                boolean status = false;
                if (str==null){
                    byte[] Scmd = new byte[2];
                    Scmd[0] = (byte)0xa8;
                    Scmd[1] = (byte)0xa8;
                    TxChar.setValue(Scmd);
                    status = mBluetoothGatt.writeCharacteristic(TxChar);
                }else{
                    byte[] bytedata;
                    int count = leng/20;
                    if (leng%20>0)
                        count += 1;
                    for (int i=0;i<count;i++){
                        if (i==count-1){
                            bytedata = Arrays.copyOfRange(str,20*i,leng);
                        }else{
                            bytedata = Arrays.copyOfRange(str,20*i,20*(i+1));
                        }
                        try {
                            Thread.sleep(120);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        TxChar.setValue(bytedata);
                        status = mBluetoothGatt.writeCharacteristic(TxChar);
//                        IDField.RetCode retcode = datac(bytedata);
                    }
                    if (status){
                        broadcastUpdate(DEVICE_WRITE_SUCCESS,type);
                        Log.e("TAG","wifi发送成功");
                    }else{
                        Log.e("TAG","wifi发送失败");
                        broadcastUpdate(DEVICE_WRITE_FAIL,type);
                    }
                }
            }
        }).start();
    }

//    String reqstatus = "{\"header\":{\"msg_id\":14,\"dev_id\":\"EEEEEEEEEEEE\",\"action\":\"get\",\"module\":\"profile\"},\"body\":{}}";
//    tojsonsend(reqstatus);


    /**
     * Enable TXNotification
     *
     * @return 
     */
    public void enableTXNotification() {
        Log.e("TAG","注册广播");
    	if (mBluetoothGatt == null) {
    		showMessage("mBluetoothGatt null" + mBluetoothGatt);
    		broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
    		return;
    	}

    	BluetoothGattService TxService = mBluetoothGatt.getService(SERVICE_UUID);
    	BluetoothGattCharacteristic TxChar = TxService.getCharacteristic(READ_CHAR_UUID);
        if (TxChar == null) {
            showMessage("Tx charateristic not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }

        mBluetoothGatt.setCharacteristicNotification(TxChar,true);
        List<BluetoothGattDescriptor> bl=TxChar.getDescriptors();/*��֪��Ϊ�Σ�getDescriptor(TX_CHAR_UUID)����Ϊnull*/
        BluetoothGattDescriptor descriptor=bl.get(0);//descriptor = TxChar.getDescriptor(TX_CHAR_UUID);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
    }

    public void writeRXCharacteristic(byte[] value) {
    	/*
    	BluetoothGattService RxService = mBluetoothGatt.getService(RX_SERVICE_UUID);
    	showMessage("mBluetoothGatt null"+ mBluetoothGatt);

    	if (RxService == null) {

            showMessage("Rx service not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }

    	BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(RX_CHAR_UUID);
        if (RxChar == null) {
            showMessage("Rx charateristic not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        RxChar.setValue(value);
    	boolean status = mBluetoothGatt.writeCharacteristic(RxChar);

        Log.d(TAG, "write TXchar - status=" + status);

        char[] digital = "0123456789ABCDEF".toCharArray();
        StringBuffer sb = new StringBuffer("");
        for(byte d:value)
        {
        	sb.append(digital[(d&0xf0)>>8]);
        	sb.append(digital[d&0x0f]);
        	sb.append(" ");
        }
        showMessage("TX data:"+sb.toString());
        */
    }
    
    private void showMessage(String msg) {
        Log.e(TAG, msg);
    }
    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) 
        	return null;

        return mBluetoothGatt.getServices();
    }
}
