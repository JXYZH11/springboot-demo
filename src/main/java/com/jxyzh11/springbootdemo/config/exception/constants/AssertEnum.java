package com.jxyzh11.springbootdemo.config.exception.constants;

import com.jxyzh11.springbootdemo.config.exception.ExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 一般级别异常常数
 */
@Getter
@AllArgsConstructor
public enum AssertEnum implements ExceptionAssert {

    //===========================占位符效验=========================
    IS_NULL(10001, "{0}"),
    //===========================占位符效验=========================

    SERVER_ERROR(10002, "服务器异常"),

    //========================基础参数效验===========================
    ID_IS_NULL(10001, "id不能为空"),
    IDS_IS_NULL(10001, "ids不能为空"),
    APPKEY_IS_NULL(10001, "appKey不能为空"),
    APP_KEY_IS_NULL(10001, "app_key不能为空"),
    SN_IS_NULL(10001, "sn不能为空"),
    DEVICEID_IS_NULL(10001, "deviceId不能为空"),
    DEVICE_ID_IS_NULL(10001, "device_id不能为空"),
    CLIENTOSTYPE_IS_NULL(10001, "clientOsType不能为空"),
    CLIENT_OS_TYPE_IS_NULL(10001, "client_os_type不能为空"),
    PACKID_IS_NULL(10001, "packId不能为空"),
    PRODUCTTYPE_IS_NULL(10001, "productType不能为空"),
    NONCE_IS_NULL(10001, "nonce不能为空"),
    VERSION_IS_NULL(10001, "version不能为空"),
    VERSIONCODE_IS_NULL(10001, "versionCode不能为空"),
    INDUSTRYID_IS_NULL(10001, "industryId不能为空"),
    ACCESS_TOKEN_IS_NULL(10001, "access_token不能为空"),
    PAGE_IS_NULL(10001, "page不能为空"),
    COUNT_IS_NULL(10001, "count不能为空"),
    //========================基础参数效验===========================

    //========================用户信息参数效验===========================
    USERID_IS_NULL(10001, "userId不能为空"),
    USER_ID_IS_NULL(10001, "user_id不能为空"),
    UID_IS_NULL(10001, "uid不能为空"),
    NICK_NAME_IS_NULL(10001, "nick_name不能为空"),
    GENDER_IS_NULL(10001, "gender不能为空"),
    BIRTHDAY_IS_NULL(10001, "birthday不能为空"),
    AGE_GROUP_IS_NULL(10001, "age_group不能为空"),
    BABY_ID_IS_NULL(10001, "baby_id不能为空"),
    COOKIE_IS_NULL(10001, "cookie不能为空"),
    //========================用户信息参数效验===========================

    //========================资源参数效验===========================
    AGE_GROUP_ID_IS_NULL(10001, "age_group_id不能为空"),
    XXM_CATEGORY_ID_IS_NULL(10001, "xxm_category_id不能为空"),
    CURRENT_TING_LIST_ID_IS_NULL(10001, "current_ting_list_id不能为空"),
    PAY_TYPE_IS_NULL(10001, "pay_type不能为空"),
    CATEGORYID_IS_NULL(10001, "categoryId不能为空"),
    ORIGINID_IS_NULL(10001, "originId不能为空"),
    ORIGINSUPPLIERID_IS_NULL(10001, "originSupplierId不能为空"),
    ORIGINCATEGORYID_IS_NULL(10001, "originCategoryId不能为空"),
    ORIGINALBUMID_IS_NULL(10001, "originAlbumId不能为空"),
    ORIGINRESOURCEID_IS_NULL(10001, "originResourceId不能为空"),
    RESOURCENAME_IS_NULL(10001, "resourceName不能为空"),
    ALBUMID_IS_NULL(10001, "albumId不能为空"),
    ALBUM_ID_IS_NULL(10001, "album_id不能为空"),
    ALBUM_UID_IS_NULL(10001, "album_uid不能为空"),
    TRACK_ID_IS_NULL(10001, "track_id不能为空"),
    TRACK_QUALITY_LEVEL_IS_NULL(10001, "track_quality_level不能为空"),
    START_AT_IS_NULL(10001, "start_at不能为空"),
    END_AT_IS_NULL(10001, "end_at不能为空"),
    DELETE_AT_IS_NULL(10001, "delete_at不能为空"),
    DURATION_IS_NULL(10001, "duration不能为空"),
    BREEK_SECOND_IS_NULL(10001, "break_second不能为空"),
    SORT_IS_NULL(10001, "sort不能为空"),
    //========================资源参数效验===========================

    //========================支付参数效验==========================
    PAYSTATUS_IS_NULL(10001, "payStatus不能为空"),
    ITEMTYPE_IS_NULL(10001, "itemType不能为空"),
    //========================支付参数效验==========================

    //========================其他参数效验===========================
    PARAMSTRING_IS_NULL(10001, "paramString不能为空"),
    NUM_IS_NULL(10001, "num不能为空"),
    Q_IS_NULL(10001, "q不能为空"),
    SOURCE_IS_NULL(10001, "source不能为空"),
    ASC_IS_NULL(10001, "asc不能为空"),
    ACTIVITY_ID_IS_NULL(10001, "activity_id不能为空"),
    //========================其他参数效验===========================
    ;

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}