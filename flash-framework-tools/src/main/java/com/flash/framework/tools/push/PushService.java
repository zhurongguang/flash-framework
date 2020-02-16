package com.flash.framework.tools.push;

/**
 * 推送服务
 *
 * @author zhurg
 * @date 2019/6/13 - 下午4:50
 */
public interface PushService {


    /**
     * 推送给设备标识参数的用户(弹窗)
     *
     * @param registrationIds 设备ID
     * @param message         消息
     * @return
     */
    boolean sendNotificationToRegistrationId(PushMessage message, String... registrationIds);

    /**
     * 推送给所有Android用户(弹窗)
     *
     * @param message 消息
     * @return
     */
    boolean sendNotificationToAllAndroid(PushMessage message);

    /**
     * 推送给所有IOS用户(弹窗)
     *
     * @param message
     * @return
     */
    boolean sendNotificationToAllIos(PushMessage message);

    /**
     * 推送给指定tag的Android用户(推送)
     *
     * @param message
     * @param tags
     * @return
     */
    boolean sendNotificationToTagAndroid(PushMessage message, String... tags);

    /**
     * 推送给指定tag的IOS用户(推送)
     *
     * @param message
     * @param tags
     * @return
     */
    boolean sendNotificationToTagIos(PushMessage message, String... tags);

    /**
     * 推送给设备标识参数的用户(消息)
     *
     * @param registrationIds
     * @param message
     * @return
     */
    boolean sendMessageToRegistrationId(PushMessage message, String registrationIds);

    /**
     * 推送给全部Android用户(消息)
     *
     * @param pushMessage
     * @return
     */
    boolean sendMessageToAllAndroid(PushMessage pushMessage);


    /**
     * 推送给所有IOS用户(消息)
     *
     * @param message
     * @return
     */
    boolean sendMessageToAllIos(PushMessage message);

    /**
     * 推送给指定tag的Android用户(消息)
     *
     * @param message
     * @param tags
     * @return
     */
    boolean sendMessageToTagAndroid(PushMessage message, String... tags);

    /**
     * 推送给指定tag的IOS用户(消息)
     *
     * @param message
     * @param tags
     * @return
     */
    boolean sendMessageToTagIos(PushMessage message, String... tags);

    /**
     * 推送给全部用户(弹窗)
     *
     * @param pushMessage
     * @return
     */
    boolean sendNotificationToAll(PushMessage pushMessage);

    /**
     * 推送给全部用户(消息)
     *
     * @param pushMessage
     * @return
     */
    boolean sendMessageToAll(PushMessage pushMessage);
}