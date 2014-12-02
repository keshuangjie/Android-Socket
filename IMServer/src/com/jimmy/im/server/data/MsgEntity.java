
package com.jimmy.im.server.data;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:44:51
 * @package com.jimmy.im.server.data
 * @version 1.0
 * 实体类
 */
public class MsgEntity {
    
    public static final int TYPE_TEXT = 1;
    public static final int TYPE_VOICE = 2;

    /** 用户名 */
    public String userName;

    /** 日期 */
    public String date;

    /** 是否是自己发送 */
    public boolean isSelf;

}
