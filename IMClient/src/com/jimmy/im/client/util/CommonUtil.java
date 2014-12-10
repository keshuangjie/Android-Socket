package com.jimmy.im.client.util;

import java.io.File;
import java.util.Calendar;

import android.os.Environment;

/**
 * @author keshuangjie
 * @date 2014-12-1 下午7:41:28
 * @package com.jimmy.im.client.util
 * @version 1.0 常用工具类
 */
public class CommonUtil {

	public static final String PATH_ROOT = "MyIM";
	public static final String PATH_VOICE = "voice";
	public static final String PATH_SEPARATOR = File.separator;
	public static final String FILE_SUFFIX = ".amr";

	public static String getAmrFilePath(String name) {
		String path = "";
		if (isSdcardMounted()) {
			path = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ PATH_SEPARATOR + PATH_ROOT 
					+ PATH_SEPARATOR + PATH_VOICE
					+PATH_SEPARATOR + name
					+ FILE_SUFFIX;
		}
		return path;
	}
	
	/**
	 * 获取.amr文件名
	 * @param path
	 * @return
	 */
	public static String getAmrFileName(String path){
		return getFileName(path, FILE_SUFFIX);
	}
	
	/**
	 * 获取文件名
	 * @param path
	 * @return
	 */
	public static String getFileName(String path, String suffix){
		String name = "";
		int index = path.lastIndexOf(CommonUtil.PATH_SEPARATOR);
		name = path.substring(index+1).replace(suffix, "");
		return name;
	}

	/**
	 *  创建目录（不存在则创建）
	 * @param dir
	 * @return
	 */
	public static boolean CreateDir(String dir) {
		boolean isSuccess = false;
		File file = new File(dir);
		File parentFile = file.getParentFile();
		if (parentFile != null) {
			if(!parentFile.exists()){
				isSuccess = file.getParentFile().mkdirs();
			}else{
				isSuccess = true;
			}
		}
		return isSuccess;
	}

	public static boolean isSdcardMounted() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getDate() {
		Calendar c = Calendar.getInstance();

		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
				+ mins);

		return sbBuffer.toString();
	}

}
