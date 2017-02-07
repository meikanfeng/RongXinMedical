package com.huagu.RX.rongxinmedical.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

public class WifiUtils
{
    
    private static final int BUILD_VERSION_JELLYBEAN = 17;
    
    private static WifiUtils instance;
    
    /**
     * Default wifimanager instance
     */
    private static WifiManager mWifiManager = null;
    
    // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
    private ConnectivityManager connManager;
    
 // 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
    public enum WifiCipherType
    {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }
    
    /*private WifiUtils()
    {
        connManager = (ConnectivityManager) MainActivity.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    
    public static WifiUtils getInstance()
    {
        if (instance == null)
        {
            instance = new WifiUtils();
        }
        mWifiManager = (WifiManager) MainActivity.getInstance().getSystemService(Context.WIFI_SERVICE);
        return instance;
    }*/
    
    
    /**
     * 网络是否已连接(Wifi or mobile)
     * @return
     */
    public boolean isNetworkConnected()
    {
        boolean isConnect = false;
        
        if (connManager != null)
        {
            NetworkInfo info = connManager.getActiveNetworkInfo();
            
            if (info != null && info.isAvailable())
            {
                isConnect = info.isConnected();
            }
        }
        return isConnect;
    }
    
    /**
     * 是否连接到Wifi
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isWifiConnected()
    {
        boolean isConnect = false;
        
        if (connManager != null)
        {
            NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            
            if (info != null && info.isAvailable())
            {
                isConnect = info.isConnected();
            }
        }
        return isConnect;
    }
    
    /**
     * 是否连接到手机流量网络
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isMobileConnected()
    {
        boolean isConnect = false;
        
        if (connManager != null)
        {
            NetworkInfo info = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            
            if (info != null && info.isAvailable())
            {
                isConnect = info.isConnected();
            }
        }
        return isConnect;
    }
    
    /**
     * 获得ssid
     * @param mContext
     * @return
     */
    public String getCurrentSSID(Context mContext)
    {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        
        String ssid = wifiManager.getConnectionInfo().getSSID();
        
        if (ssid != null)
        {
            if (ssid.contains("unknown ssid"))
                return "";
            return removeSSIDQuotes(ssid);
            
        }
        return "";
    }
    
    private String removeSSIDQuotes(String connectedSSID)
    {
        int currentVersion = Build.VERSION.SDK_INT;
        
        if (currentVersion >= BUILD_VERSION_JELLYBEAN)
        {
            if (connectedSSID.startsWith("\"") && connectedSSID.endsWith("\""))
            {
                connectedSSID = connectedSSID.substring(1,
                        connectedSSID.length() - 1);
            }
        }
        return connectedSSID;
    }
    
    /**
     * 获取加密类型
     */
    private byte AuthModeOpen = 0;
    
    private byte AuthModeWPAPSK = 4;
    
    private byte AuthModeWPA2PSK = 7;
    
    private byte AuthModeWPAPSKWPA2PSK = 9;
    
    public byte getAutoMode(Context mContext, String ssid)
    {
        byte autoModeByte = 0;
        String autoMode = "";
        List<ScanResult> wifeList = new ArrayList<ScanResult>();
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wifeList = wifiManager.getScanResults();
        if(null==wifeList){
        	return 0;
        }
        for (ScanResult result : wifeList)
        {
            if (result.SSID.equals(ssid))
            {
                autoMode = result.capabilities;
                if (autoMode.contains("WPA-"))
                {
                    if (autoMode.contains("WPA2-"))
                    {
                        autoModeByte = AuthModeWPAPSKWPA2PSK;
                        break;
                    }
                    else
                    {
                        autoModeByte = AuthModeWPAPSK;
                        break;
                    }
                }
                else if (autoMode.contains("WPA2-"))
                {
                    autoModeByte = AuthModeWPA2PSK;
                    break;
                }
                else
                {
                    autoModeByte = AuthModeOpen;
                    break;
                }
                
            }
        }
        return autoModeByte;
    }
    
    public WifiConfiguration isExsits(String SSID)
    {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        if(existingConfigs == null)
        {
            return null;
        }
        for (WifiConfiguration existingConfig : existingConfigs)
        {
            if (existingConfig.SSID.equals("\"" + SSID + "\""))
            {
                return existingConfig;
            }
        }
        return null;
    }
    
    public List<ScanResult> getScanResult()
    {
        List<ScanResult> result = mWifiManager.getScanResults();
        return result;
    }
    
    public boolean startScan()
    {
        return mWifiManager.startScan();
    }
    
    /**
     * 判断wifi是否加密,并区别加密类型
     * 1 wep
     * 2 wpa
     * 0 没有密码
     * @param result
     * @return
     */
    public WifiCipherType getSecurity(ScanResult result)
    {
        if (result.capabilities.contains("WEP"))
        {
            return WifiCipherType.WIFICIPHER_WEP;
        }
        else if (result.capabilities.contains("WPA"))
        {
            return WifiCipherType.WIFICIPHER_WPA;
        }
        return WifiCipherType.WIFICIPHER_NOPASS;
    }
    
    /**
     * 判断wifi是否加密,并区别加密类型
     * 1 wep
     * 2 wpa
     * 0 没有密码
     * @param ssid
     * @return
     */
    public WifiCipherType getSecurity(String ssid)
    {
        List<ScanResult> wifiList = getScanResult();
        for (ScanResult scanResult : wifiList)
        {
            String resultAP = scanResult.SSID;
            if (resultAP.contains(ssid))
            {
                return getSecurity(scanResult);
            }
        }
        return WifiCipherType.WIFICIPHER_INVALID;
    }
    
    // 打开WIFI    
    public void openWifi()
    {
        if (!mWifiManager.isWifiEnabled())
        {
            mWifiManager.setWifiEnabled(true);
        }
    }
    
    public WifiManager getWifi()
    {
        return mWifiManager;
    }
    
    
    //创建一个Wifi信息
    public WifiConfiguration createWifiInfo(String SSID, String Password,
                                            WifiCipherType Type)
    {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        
        WifiConfiguration tempConfig = this.isExsits(SSID);
        if (tempConfig != null)
        {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) //WIFICIPHER_NOPASS 
        {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        
        if (Type == WifiCipherType.WIFICIPHER_WEP) //WIFICIPHER_WEP 
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        
        if (Type == WifiCipherType.WIFICIPHER_WPA) //WIFICIPHER_WPA 
        {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            //config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);   
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }
    
 // 添加一个网络并连接    
    public boolean addNetwork(WifiConfiguration wcg)
    {
        int wcgID = mWifiManager.addNetwork(wcg);
        if (wcgID != -1)
        {
            return mWifiManager.enableNetwork(wcgID, true);
        }
        return false;
    }
}
