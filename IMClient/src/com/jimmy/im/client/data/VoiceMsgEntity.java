
package com.jimmy.im.client.data;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:39:33
 * @package com.jimmy.im.client.data
 * @version 1.0
 * 语音实体类
 */
public class VoiceMsgEntity extends MsgEntity{
	
	/** 文件名称 */
	public String fileName;
    
	 /** 文件路径 */
    public String filePath;
    
    /** 文件时长，以秒为单位 */
    public int time;
    
    /** 文件大小 */
    public long size;
    
}
