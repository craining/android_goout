package com.c35.ptc.goout.util;

import java.io.File;

/**
 * 文件帮助类
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-4-16
 */

public class FileUtil {

	private static long size = 0;

	public FileUtil() {
		FileUtil.size = 0;// 获得目录大小时，需要初始化
	}

	/**
	 * 删除某文件
	 * 
	 * @Description:
	 * @param file
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-16
	 */
	public static boolean delFile(File file) {
		if (file.isDirectory()) {
			return false;
		}

		return file.delete();
	}

	/**
	 * 删除某个目录
	 * 
	 * @Description:
	 * @param dir
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-16
	 */
	public static boolean delFileDir(File dir) {
		if (dir == null || !dir.exists() || dir.isFile()) {
			return false;
		}
		for (File file : dir.listFiles()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				delFileDir(file);
			}
		}
		// dir.delete();//不删除目录,仅删除目录下的文件

		return true;
	}

	/**
	 * 递归获得文件或目录的大小
	 * 
	 * @Description:
	 * @param dir
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-16
	 */
	public long getFileSize(File dir) {

		try {
			if (!dir.isDirectory()) {
				setSize(getSize() + dir.length());
				// Log.e("", dir.toString());
			} else {
				for (File file : dir.listFiles()) {
					if (!file.isDirectory()) {
						// Log.e("", file.toString());
						setSize(getSize() + file.length());
					} else {
						getFileSize(file);// 递归
					}
				}
			}
		} catch (Exception e) {
			setSize(-1);
		}

		return getSize();
	}

	private static void setSize(long size) {
		FileUtil.size = size;
	}

	private static long getSize() {
		return FileUtil.size;
	}

	/**
	 * 将文件的字节数转换为kb、mb、或gb
	 * 
	 * @Description:
	 * @param size
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-4-24
	 */
	public static String sizeLongToString(long size) {
		if (size == 0) {
			return "0KB";
		} else {
			String a = "";
			if (size / 1024 < 1024.0) {
				a = String.format("%.2f", size / 1024.0) + "KB";
			} else if (size / 1048576 < 1024) {
				a = String.format("%.2f", size / 1048576.0) + "MB";
			} else {
				a = String.format("%.2f", size / 1073740824.0) + "GB";
			}
			return a;
		}
	}
}
