package com.weishe.weichat.util;

import java.io.File;

import com.weishe.weichat.bean.Attachment;

public class FileUtil {
	public static int getFileType(File file) {

		String type = getExtensionName(file.getName());
		switch (type.toUpperCase()) {
		case "JPG":
			return Attachment.TYPE_JPG;

		case "PNG":
			return Attachment.TYPE_PNG;

		case "GIF":
			return Attachment.TYPE_GIF;

		case "DOC":
			return Attachment.TYPE_DOC;

		case "DOCX":
			return Attachment.TYPE_DOCX;

		case "PPT":
			return Attachment.TYPE_PPT;

		case "PPTX":
			return Attachment.TYPE_PPTX;

		case "XLS":
			return Attachment.TYPE_XLS;

		case "XLSX":
			return Attachment.TYPE_XLSX;

		case "PDF":
			return Attachment.TYPE_PDF;

		case "TXT":
			return Attachment.TYPE_TXT;

		case "RAR":
			return Attachment.TYPE_RAR;

		case "ZIP":
			return Attachment.TYPE_ZIP;

		case "MP3":
			return Attachment.TYPE_VOICE;

		case "WMA":
			return Attachment.TYPE_VOICE;

		case "MP4":
			return Attachment.TYPE_VIDEO;

		case "AVI":
			return Attachment.TYPE_VIDEO;

		case "RM":
			return Attachment.TYPE_VIDEO;
		case "RMVB":
			return Attachment.TYPE_VIDEO;

		case "MOV":
			return Attachment.TYPE_VIDEO;

		case "MKV":
			return Attachment.TYPE_VIDEO;

		default:
			return Attachment.TYPE_UNKNOWN;
		}
	}

	/*
	 * Java文件操作 获取文件扩展名
	 * 
	 * Created on: 2011-8-2 Author: blueeagle
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/*
	 * Java文件操作 获取不带扩展名的文件名
	 * 
	 * Created on: 2011-8-2 Author: blueeagle
	 */
	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}
}
