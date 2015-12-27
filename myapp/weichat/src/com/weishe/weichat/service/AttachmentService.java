package com.weishe.weichat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.Attachment;
import com.weishe.weichat.dao.AttachmentDao;

@Service
public class AttachmentService {
	@Autowired
	private AttachmentDao attachmentDao;

	public Attachment getAttachmentById(int id) {
		return this.attachmentDao.getAttachmentById(id);
	}

	public Attachment getAttachmentByGroupNameAndPath(String groupName,
			String path) {
		return this.attachmentDao.getAttachmentByGroupNameAndPath(groupName,
				path);
	}

	public Attachment saveAttachment(Attachment attachment) {
		if (attachment == null || attachment.getGroupName() == null
				|| attachment.getGroupName().isEmpty()
				|| attachment.getPath() == null
				|| attachment.getPath().isEmpty()) {
			return null;
		}
		// 先判断是否存在，不存在则添加
		Attachment a = getAttachmentByGroupNameAndPath(
				attachment.getGroupName(), attachment.getPath());
		if (a != null) {
			return a;
		}

		return this.attachmentDao.saveAttachment(attachment);
	}

}
