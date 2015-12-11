/**
 * hnjz.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.ybd.common.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.ybd.common.App;

/**
 * 文件的工具类
 * 
 * @author cyf
 * @version $Id: FileUtil.java, v 0.1 2013-4-25 下午2:01:15 cyf Exp $
 */
public class FileUtil {

	/** SD卡地址 */
	private String SDPATH;

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUtil() {
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	private static String root;

	/**
	 * 获取手机的根路径
	 * 
	 * @return
	 */
	public static String getPath() {
		if (StringUtil.isBlank(root)) {
			String status = Environment.getExternalStorageState();
			StringBuffer strBuffer = new StringBuffer();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				strBuffer.append(Environment.getExternalStorageDirectory());
				strBuffer.append(File.separator);
				strBuffer.append(App.HOME);
				strBuffer.append(File.separator);
			} else {
				strBuffer.append("/data/data/cn.com.hnjz");
				strBuffer.append(File.separator);
				strBuffer.append(App.HOME);
				strBuffer.append(File.separator);
			}
			root = strBuffer.toString();
		}
		return root;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 判断某个文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static Boolean isExist(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * 根据文件类型得到文件保存目录 如果目录不存在则创建
	 * 
	 * @param dirType
	 *            文件类型
	 * @param subDirs
	 *            子目录（除固定设置外的自定义目录，如：某个任务的文件可以再以任务编号为目录区分）
	 * @return
	 */
	public static String getFileSaveDir(DirType dirType, String... subDirs) {
		StringBuffer dirBuffer = new StringBuffer(getSDPath());
		dirBuffer.append(File.separator);
		dirBuffer.append(dirType.getLocalPath()).append(File.separator);
		if (subDirs != null && subDirs.length > 0) {
			for (int i = 0; i < subDirs.length; i++) {
				dirBuffer.append(subDirs[i]).append(File.separator);
			}
		}
		File dirFile = new File(dirBuffer.toString());
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		return dirBuffer.toString();
	}

	/**
	 * 根据文件类型得到文件保存目录 如果目录不存在则创建
	 * 
	 * @param dirType
	 *            文件类型
	 * @param fileName
	 *            文件
	 * @return
	 */
	public static String getRFileSaveDir(DirType dirType, String fileName) {
		StringBuffer dirBuffer = new StringBuffer(getPath());
		dirBuffer.append(dirType.getLocalPath()).append(File.separator);
		File dirFile = new File(dirBuffer.toString());
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		dirBuffer.append(File.separator);
		dirBuffer.append(fileName);
		return dirBuffer.toString();
	}

	/**
	 * 
	 * @author zn
	 * @version $Id: FileUtil.java, v 0.1 2013-5-9 上午12:21:16 zn Exp $
	 */
	public enum DirType {
		/** 执法文书 */
		ZFWS("zfws", "执法文件", "zfws"),
		/** 客户端 */
		CLIENT("client", "客户端", "client");

		private String code;

		private String text;

		private String localPath;

		private DirType(String code, String text, String localPath) {
			this.code = code;
			this.text = text;
			this.localPath = localPath;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getLocalPath() {
			return localPath;
		}

		public void setLocalPath(String localPath) {
			this.localPath = localPath;
		}
	}

	/**
	 * 
	 * 打开文件
	 * 
	 * @param file
	 */

	public static void openFile(Activity activity, String filepath) {
		try {
			// if(filepath.endsWith("amr")){
			// return;
			// }
			Uri uri = Uri.parse("file://" + filepath);

			Intent intent = new Intent();

			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// 设置intent的Action属性

			intent.setAction(Intent.ACTION_VIEW);

			// 获取文件file的MIME类型

			String type = getMIMEType(filepath);

			// 设置intent的data和Type属性。

			intent.setDataAndType(uri, type);

			// 跳转

			activity.startActivity(intent);
		} catch (Exception e) {
			Log.e("CommUtils", e.toString(), e);
			Toast tost = Toast.makeText(activity, "手机不支持此文件格式！", Toast.LENGTH_LONG);
			tost.setGravity(Gravity.CENTER, 0, 0);
			tost.show();
		}

	}

	/** MIME类型与文件后缀名的匹配表 */
	private static final String[][] MIME_MapTable = {

			// {后缀名， MIME类型}

			{ ".3gp", "video/mp4" },

			{ ".apk", "application/vnd.android.package-archive" },

			{ ".asf", "video/x-ms-asf" },

			{ ".avi", "video/x-msvideo" },

			{ ".bin", "application/octet-stream" },

			{ ".bmp", "image/bmp" },

			{ ".c", "text/plain" },

			{ ".class", "application/octet-stream" },

			{ ".conf", "text/plain" },

			{ ".cpp", "text/plain" },

			{ ".doc", "application/msword" },

			{ ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },

			{ ".xls", "application/vnd.ms-excel" },

			{ ".xlsx", "application/vnd.ms-excel" },

			{ ".exe", "application/octet-stream" },

			{ ".gif", "image/gif" },

			{ ".gtar", "application/x-gtar" },

			{ ".gz", "application/x-gzip" },

			{ ".h", "text/plain" },

			{ ".htm", "text/html" },

			{ ".html", "text/html" },

			{ ".jar", "application/Java-archive" },

			{ ".java", "text/plain" },

			{ ".jpeg", "image/jpeg" },

			{ ".jpg", "image/jpeg" },

			{ ".js", "application/x-javascript" },

			{ ".log", "text/plain" },

			{ ".m3u", "audio/x-mpegurl" },

			{ ".m4a", "audio/mp4a-latm" },

			{ ".m4b", "audio/mp4a-latm" },

			{ ".m4p", "audio/mp4a-latm" },

			{ ".m4u", "video/vnd.mpegurl" },

			{ ".m4v", "video/x-m4v" },

			{ ".mov", "video/quicktime" },

			{ ".mp2", "audio/x-mpeg" },

			{ ".mp3", "audio/x-mpeg" },

			{ ".mp4", "video/mp4" },

			{ ".mpc", "application/vnd.mpohun.certificate" },

			{ ".mpe", "video/mpeg" },

			{ ".mpeg", "video/mpeg" },

			{ ".mpg", "video/mpeg" },

			{ ".mpg4", "video/mp4" },

			{ ".mpga", "audio/mpeg" },

			{ ".msg", "application/vnd.ms-outlook" },

			{ ".ogg", "audio/ogg" },

			{ ".pdf", "application/pdf" },

			{ ".png", "image/png" },

			{ ".pps", "application/vnd.ms-powerpoint" },

			{ ".ppt", "application/vnd.ms-powerpoint" },

			{ ".prop", "text/plain" },

			{ ".rar", "application/x-rar-compressed" },

			{ ".rc", "text/plain" },

			{ ".rmvb", "audio/x-pn-realaudio" },

			{ ".rtf", "application/rtf" },

			{ ".sh", "text/plain" },

			{ ".tar", "application/x-tar" },

			{ ".tgz", "application/x-compressed" },

			{ ".txt", "text/plain" },

			{ ".wav", "audio/x-wav" },

			{ ".wma", "audio/x-ms-wma" },

			{ ".wmv", "audio/x-ms-wmv" },

			{ ".wps", "application/vnd.ms-works" },

			// {".xml", "text/xml"},

			{ ".xml", "text/plain" },

			{ ".z", "application/x-compress" },

			{ ".zip", "application/zip" },

			{ ".amr", "video/mp4" },

			{ "", "*/*" }

	};

	/**
	 * 
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */

	private static String getMIMEType(String filepath) {
		String type = "*/*";
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = filepath.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = filepath.substring(dotIndex, filepath.length()).toLowerCase();
		if (end == "") {
			return type;
		}
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0])) {
				type = MIME_MapTable[i][1];
			}
		}

		return type;

	}

	/** 获取SD路径 **/
	public static String getSDPath() {
		// 判断sd卡是否存在
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.getPath();
		}
		return "/sdcard";
	}

	/** 获取文件信息 **/
	// public static FileInfo getFileInfo(File f) {
	// FileInfo info = new FileInfo();
	// info.Name = f.getName();
	// info.IsDirectory = f.isDirectory();
	// calcFileContent(info, f);
	// return info;
	// }

	/** 计算文件内容 **/
	// private static void calcFileContent(FileInfo info, File f) {
	// if (f.isFile()) {
	// info.Size += f.length();
	// }
	// if (f.isDirectory()) {
	// File[] files = f.listFiles();
	// if (files != null && files.length > 0) {
	// for (int i = 0; i < files.length; ++i) {
	// File tmp = files[i];
	// if (tmp.isDirectory()) {
	// info.FolderCount++;
	// } else if (tmp.isFile()) {
	// info.FileCount++;
	// }
	// if (info.FileCount + info.FolderCount >= 10000) { // 超过一万不计算
	// break;
	// }
	// calcFileContent(info, tmp);
	// }
	// }
	// }
	// }

	/** 转换文件大小 **/
	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = fileS + " B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + " K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + " M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + " G";
		}
		return fileSizeString;
	}

	/** 合并路径 **/
	public static String combinPath(String path, String fileName) {
		return path + (path.endsWith(File.separator) ? "" : File.separator) + fileName;
	}

	/** 复制文件 **/
	public static boolean copyFile(File src, File tar) throws Exception {
		if (src.isFile()) {
			InputStream is = new FileInputStream(src);
			OutputStream op = new FileOutputStream(tar);
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(op);
			byte[] bt = new byte[1024 * 8];
			int len = bis.read(bt);
			while (len != -1) {
				bos.write(bt, 0, len);
				len = bis.read(bt);
			}
			bis.close();
			bos.close();
		}
		if (src.isDirectory()) {
			File[] f = src.listFiles();
			tar.mkdir();
			for (int i = 0; i < f.length; i++) {
				copyFile(f[i].getAbsoluteFile(), new File(tar.getAbsoluteFile() + File.separator + f[i].getName()));
			}
		}
		return true;
	}

	/** 移动文件 **/
	public static boolean moveFile(File src, File tar) throws Exception {
		if (copyFile(src, tar)) {
			deleteFile(src);
			return true;
		}
		return false;
	}

	/** 删除文件 **/
	public static void deleteFile(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					deleteFile(files[i]);
				}
			}
		}
		f.delete();
	}

	/** 获取MIME类型 **/
	// public static String getMIMEType(String name) {
	// String type = "";
	// String end = name.substring(name.lastIndexOf(".") + 1,
	// name.length()).toLowerCase();
	// if (end.equals("apk")) {
	// return "application/vnd.android.package-archive";
	// } else if (end.equals("mp4") || end.equals("avi") || end.equals("3gp")
	// || end.equals("rmvb")) {
	// type = "video";
	// } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
	// end.equals("xmf")
	// || end.equals("ogg") || end.equals("wav")) {
	// type = "audio";
	// } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
	// || end.equals("jpeg") || end.equals("bmp")) {
	// type = "image";
	// } else if (end.equals("txt") || end.equals("log")) {
	// type = "text";
	// } else {
	// type = "*";
	// }
	// type += "/*";
	// return type;
	// }

	public static void copyFile(String oldPath, String newPath) {
		try {
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件不存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
