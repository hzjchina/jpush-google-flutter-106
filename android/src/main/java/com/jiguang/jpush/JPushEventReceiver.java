package com.jiguang.jpush;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageService;
import io.flutter.plugin.common.MethodChannel.Result;

public class JPushEventReceiver extends JPushMessageService {
    private  final static String TAG = "JPushEventReceiver";

    @Override
    public void onNotifyMessageUnShow(Context context,final NotificationMessage notificationMessage) {
        super.onNotifyMessageUnShow(context,notificationMessage);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                JPushHelper.getInstance().onNotifyMessageUnShow(notificationMessage);
            }
        });
    }
    @Override
    public void onConnected(Context context,final boolean isConnected) {
        //连接状态
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                JPushHelper.getInstance().onConnected(isConnected);
            }
        });
    }

    @Override
    public void onInAppMessageShow(Context context,final NotificationMessage message) {
        VDLog.i(TAG, "[onInAppMessageShow], " + message.toString());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                JPushHelper.getInstance().onInAppMessageShow(message);
            }
        });
    }

    @Override
    public void onInAppMessageClick(Context context,final NotificationMessage message) {
        VDLog.i(TAG, "[onInAppMessageClick], " + message.toString());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                JPushHelper.getInstance().onInAppMessageClick(message);
            }
        });
    }
    @Override
    public void onTagOperatorResult(Context context, final JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);

        final JSONObject resultJson = new JSONObject();

        final int sequence = jPushMessage.getSequence();
        try {
            resultJson.put("sequence", sequence);
        } catch (JSONException e) {
            VDLog.w(TAG,"onTagOperatorResult",e);
        }

        final Result callback = JPushHelper.getInstance().getCallback(sequence);//instance.eventCallbackMap.get(sequence);

        if (callback == null) {
            VDLog.i(TAG, "Unexpected error, callback is null!");
            return;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (jPushMessage.getErrorCode() == 0) { // success
                    Set<String> tags = jPushMessage.getTags();
                    List<String> tagList = new ArrayList<>(tags);
                    Map<String, Object> res = new HashMap<>();
                    res.put("tags", tagList);
                    callback.success(res);
                } else {
                    try {
                        resultJson.put("code", jPushMessage.getErrorCode());
                    } catch (JSONException e) {
                        VDLog.w(TAG,"onTagOperatorResult Handler",e);
                    }
                    callback.error(Integer.toString(jPushMessage.getErrorCode()), "", "");
                }

                JPushHelper.getInstance().removeCallback(sequence);
            }
        });

    }



    @Override
    public void onCheckTagOperatorResult(Context context, final JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);



        final int sequence = jPushMessage.getSequence();


        final Result callback = JPushHelper.getInstance().getCallback(sequence);;

        if (callback == null) {
            VDLog.i(TAG, "Unexpected error, callback is null!");
            return;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (jPushMessage.getErrorCode() == 0) {
                    Set<String> tags = jPushMessage.getTags();
                    List<String> tagList = new ArrayList<>(tags);
                    Map<String, Object> res = new HashMap<>();
                    res.put("tags", tagList);
                    callback.success(res);
                } else {

                    callback.error(Integer.toString(jPushMessage.getErrorCode()), "", "");
                }

                JPushHelper.getInstance().removeCallback(sequence);
            }
        });
    }

    @Override
    public void onAliasOperatorResult(Context context, final JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);

        final int sequence = jPushMessage.getSequence();


        final Result callback = JPushHelper.getInstance().getCallback(sequence);;

        if (callback == null) {
            VDLog.i(TAG, "Unexpected error, callback is null!");
            return;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (jPushMessage.getErrorCode() == 0) { // success
                    Map<String, Object> res = new HashMap<>();
                    res.put("alias", (jPushMessage.getAlias() == null)? "" : jPushMessage.getAlias());
                    callback.success(res);

                } else {
                    callback.error(Integer.toString(jPushMessage.getErrorCode()), "", "");
                }

                JPushHelper.getInstance().removeCallback(sequence);
            }
        });
    }

    @Override
    public void onNotificationSettingsCheck(Context context, boolean isOn, int source) {
        super.onNotificationSettingsCheck(context, isOn, source);


        HashMap<String, Object> map = new HashMap();
        map.put("isEnabled",isOn);
        JPushHelper.getInstance().runMainThread(map,null,"onReceiveNotificationAuthorization");
    }
}
