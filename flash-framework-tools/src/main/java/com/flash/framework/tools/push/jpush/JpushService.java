package com.flash.framework.tools.push.jpush;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.flash.framework.tools.push.PushMessage;
import com.flash.framework.tools.push.PushService;
import com.flash.framework.tools.push.PushServiceConfigure;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;

/**
 * Jpush 实现
 *
 * @author zhurg
 * @date 2019/6/14 - 上午10:37
 */
@Slf4j
public class JpushService implements PushService {

    public static final int PUSH_SUCCESS_CODE = 200;

    private JPushClient jPushClient;

    private PushServiceConfigure pushServiceConfigure;

    public JpushService(PushServiceConfigure pushServiceConfigure) {
        this(pushServiceConfigure, null);
    }

    public JpushService(PushServiceConfigure pushServiceConfigure, ClientConfig clientConfig) {
        this.pushServiceConfigure = pushServiceConfigure;
        if (null == clientConfig) {
            clientConfig = ClientConfig.getInstance();
        }
        this.jPushClient = new JPushClient(pushServiceConfigure.getAppSecret(), pushServiceConfigure.getAppKey(), null, clientConfig);
    }


    @Override
    public boolean sendNotificationToRegistrationId(PushMessage message, String... registrationIds) {
        try {
            PushPayload payload = buildNotificationRegistrationIdPayload(message, registrationIds);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send notification {} to registrationIds {} failed ,ResponseCode {}", registrationIds,
                    JSON.toJSONString(message), pushResult.getResponseCode());
            return false;
        } catch (Exception e) {
            log.error("jpush send notification {} to registrationIds {} failed,cause:{}",
                    registrationIds, JSON.toJSONString(message), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendNotificationToAllAndroid(PushMessage message) {
        try {
            PushPayload payload = buildNotificationToAndroidPayload(message);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send notification {} to Android device failed", JSON.toJSONString(message));
            return false;
        } catch (Exception e) {
            log.error("jpush send notification {} to Android device failed,cause:{}", JSON.toJSONString(message), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendNotificationToAllIos(PushMessage message) {
        try {
            PushPayload payload = buildNotificationToIosPayload(message);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send notification {} to IOS device failed", JSON.toJSONString(message));
            return false;
        } catch (Exception e) {
            log.error("jpush send notification {} to IOS device failed,cause:{}", JSON.toJSONString(message), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendNotificationToTagAndroid(PushMessage message, String... tags) {
        try {
            PushPayload payload = buildNotificationToTagAndroidPayload(message);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send notification {} to Android device tags {} failed", JSON.toJSONString(message), tags);
            return false;
        } catch (Exception e) {
            log.error("jpush send notification {} to Android device tags {} failed,cause:{}", JSON.toJSONString(message), tags, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendNotificationToTagIos(PushMessage message, String... tags) {
        try {
            PushPayload payload = buildNotificationToTagIosPayload(message);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send notification {} to IOS device tags {} failed", JSON.toJSONString(message), tags);
            return false;
        } catch (Exception e) {
            log.error("jpush send notification {} to IOS device tags {} failed,cause:{}", JSON.toJSONString(message), tags, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendMessageToRegistrationId(PushMessage message, String registrationIds) {
        try {
            PushPayload payload = buildMessageToRegistrationIdPayload(message, registrationIds);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send message {} to registrationIds {} failed ,ResponseCode {}", registrationIds,
                    JSON.toJSONString(message), pushResult.getResponseCode());
            return false;
        } catch (Exception e) {
            log.error("jpush send message {} to registrationIds {} failed,cause :{}",
                    registrationIds, JSON.toJSONString(message), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendMessageToAllAndroid(PushMessage pushMessage) {
        try {
            PushPayload payload = buildMessageToAndroidPayload(pushMessage);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send message {} to Android device failed", JSON.toJSONString(pushMessage));
            return false;
        } catch (Exception e) {
            log.error("jpush send message {} to Android device failed,cause :{}", JSON.toJSONString(pushMessage), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendMessageToAllIos(PushMessage message) {
        try {
            PushPayload payload = buildMessageToIosPayload(message);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send message {} to IOS device failed", JSON.toJSONString(message));
            return false;
        } catch (Exception e) {
            log.error("jpush send message {} to IOS device failed,cause :{}", JSON.toJSONString(message), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendMessageToTagAndroid(PushMessage message, String... tags) {
        try {
            PushPayload payload = buildMessageToTagAndroidPayload(message);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send message {} to Android device tags {} failed", JSON.toJSONString(message), tags);
            return false;
        } catch (Exception e) {
            log.error("jpush send message {} to Android device tags {} failed,cause :{}", JSON.toJSONString(message), tags, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendMessageToTagIos(PushMessage message, String... tags) {
        try {
            PushPayload payload = buildMessageToTagIosPayload(message);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send message {} to IOS device tags {} failed", JSON.toJSONString(message), tags);
            return false;
        } catch (Exception e) {
            log.error("jpush send message {} to IOS device tags {} failed,cause :{}", JSON.toJSONString(message), tags, Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendNotificationToAll(PushMessage pushMessage) {
        try {
            PushPayload payload = buildNotificationAllPayload(pushMessage);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send notification {} to all device failed ,ResponseCode {}",
                    JSON.toJSONString(pushMessage), pushResult.getResponseCode());
            return false;
        } catch (Exception e) {
            log.error("jpush send notification {} to all device failed,cause :{}", JSON.toJSONString(pushMessage), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    @Override
    public boolean sendMessageToAll(PushMessage pushMessage) {
        try {
            PushPayload payload = buildMessageAllPayload(pushMessage);
            PushResult pushResult = jPushClient.sendPush(payload);
            if (pushResult.getResponseCode() == PUSH_SUCCESS_CODE) {
                return true;
            }
            log.warn("jpush send notification {} to all device failed ,ResponseCode {}",
                    JSON.toJSONString(pushMessage), pushResult.getResponseCode());
            return false;
        } catch (Exception e) {
            log.error("jpush send notification {} to all device failed,cause :{}", JSON.toJSONString(pushMessage), Throwables.getStackTraceAsString(e));
            return false;
        }
    }

    protected PushPayload buildNotificationRegistrationIdPayload(PushMessage pushMessage, String... registrationIds) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationIds))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(pushMessage.getAlert())
                                .setTitle(pushMessage.getTitle())
                                .addExtras(pushMessage.getExtras())
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(pushMessage.getAlert())
                                .incrBadge(1)
                                .setSound("sound.caf")
                                .addExtras(pushMessage.getExtras())
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(pushServiceConfigure.getJpush().isApnsProduction())
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildNotificationToAndroidPayload(PushMessage pushMessage) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(pushMessage.getAlert())
                                .setTitle(pushMessage.getTitle())
                                .addExtras(pushMessage.getExtras())
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildNotificationToIosPayload(PushMessage pushMessage) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(pushMessage.getAlert())
                                .incrBadge(1)
                                .setSound("sound.caf")
                                .addExtras(pushMessage.getExtras())
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(pushServiceConfigure.getJpush().isApnsProduction())
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildNotificationToTagAndroidPayload(PushMessage pushMessage, String... tags) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(tags))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(pushMessage.getAlert())
                                .setTitle(pushMessage.getTitle())
                                .addExtras(pushMessage.getExtras())
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildNotificationToTagIosPayload(PushMessage pushMessage, String... tags) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag(tags))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(pushMessage.getAlert())
                                .incrBadge(1)
                                .setSound("sound.caf")
                                .addExtras(pushMessage.getExtras())
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(pushServiceConfigure.getJpush().isApnsProduction())
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildMessageToRegistrationIdPayload(PushMessage pushMessage, String... registrationIds) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationIds))
                .setMessage(Message.newBuilder()
                        .setTitle(pushMessage.getTitle())
                        .setMsgContent(pushMessage.getContent())
                        .addExtras(pushMessage.getExtras())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(pushServiceConfigure.getJpush().isApnsProduction())
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildMessageToAndroidPayload(PushMessage pushMessage) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                .setMessage(Message.newBuilder()
                        .setTitle(pushMessage.getTitle())
                        .setMsgContent(pushMessage.getContent())
                        .addExtras(pushMessage.getExtras())
                        .build())
                .setOptions(Options.newBuilder()
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildMessageToIosPayload(PushMessage pushMessage) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.all())
                .setMessage(Message.newBuilder()
                        .setTitle(pushMessage.getTitle())
                        .setMsgContent(pushMessage.getContent())
                        .addExtras(pushMessage.getExtras())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(pushServiceConfigure.getJpush().isApnsProduction())
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildMessageToTagAndroidPayload(PushMessage pushMessage, String... tags) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(tags))
                .setMessage(Message.newBuilder()
                        .setTitle(pushMessage.getTitle())
                        .setMsgContent(pushMessage.getContent())
                        .addExtras(pushMessage.getExtras())
                        .build())
                .setOptions(Options.newBuilder()
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildMessageToTagIosPayload(PushMessage pushMessage, String... tags) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag(tags))
                .setMessage(Message.newBuilder()
                        .setTitle(pushMessage.getTitle())
                        .setMsgContent(pushMessage.getContent())
                        .addExtras(pushMessage.getExtras())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(pushServiceConfigure.getJpush().isApnsProduction())
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildNotificationAllPayload(PushMessage pushMessage) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(pushMessage.getAlert())
                                .setTitle(pushMessage.getTitle())
                                .addExtras(pushMessage.getExtras())
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(pushMessage.getAlert())
                                .incrBadge(1)
                                .setSound("sound.caf")
                                .addExtras(pushMessage.getExtras())
                                .build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(pushServiceConfigure.getJpush().isApnsProduction())
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }

    protected PushPayload buildMessageAllPayload(PushMessage pushMessage) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.all())
                .setMessage(Message.newBuilder()
                        .setTitle(pushMessage.getTitle())
                        .setMsgContent(pushMessage.getContent())
                        .addExtras(pushMessage.getExtras())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(pushServiceConfigure.getJpush().isApnsProduction())
                        .setTimeToLive(pushMessage.getTimeToAlive())
                        .build())
                .build();
    }
}