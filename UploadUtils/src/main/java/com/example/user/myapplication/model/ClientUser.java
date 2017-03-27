package com.example.user.myapplication.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Cloudsoar(wangyb)
 * @datetime 2015-12-31 19:29 GMT+8
 * @email 395044952@qq.com
 */
public class ClientUser implements Serializable{
    /** 用户id */
    public String userId;
    /** 用户名 */
    public String user_name;
    /** 密码 */
    public String userPwd;
    /** 本地头像地址*/
    public String face_local;
    /** 头像URL **/
    public String face_url;
    /** 生日 **/
    public String birthday;
    /** 个性签名 **/
    public String signature;
    /** 电话号码 **/
    public String mobile;
    /** 微信号 **/
    public String weixin_no;
    /** QQ号 **/
    public String qq_no;
    /** 性别 **/
    public String sex;
    /**年龄**/
    public int age;
    /**身高**/
    public String tall;
    /**体重**/
    public String weight;
    /**血型**/
    public String blood_type;
    /**距离**/
    public String distance;
    /**情感状态**/
    public String state_marry;
    /**星座**/
    public String constellation;
    /** 个性特征 */
    public String personalityTag;
    /**魅力部位**/
    public String part_tag;
    /**是否vip**/
    public boolean is_vip;
    /**是否公开社交帐号**/
    public boolean publicSocialNumber;
    /**是否验证手机**/
    public boolean isCheckPhone;
    /**头像状态 0=待审核 1=审核通过 2=审核失败 3=需要人工审核*/
    public int portraitStatus;
    /**登录的sessiondid**/
    public String sessionId;
    /**用户上传的图片**/
    public List<String> imgUrls;
    /**查看的用户是否已关注**/
    public boolean isFollow = false;
    /**职业 **/
    public String occupation;
    /** 收入 */
    public String salary;
    /** 学历 */
    public String education;
    /** 是否购房 */
    public String isHasHouse;
    /** 是否接受异地恋 */
    public String isAcceptOtherLove;
    /** 喜欢的异性类型 */
    public String loveType;
    /** 是否接受婚前性行为 */
    public String isSexLove;
    /** 是否有车 */
    public String isHasCar;
    /** 是否想要小孩 */
    public String isWantChild;
    /** 是否和父母同住 */
    public String isWithParent;
    /** 是否和父母同住 */
    public Integer roseNum;

}
