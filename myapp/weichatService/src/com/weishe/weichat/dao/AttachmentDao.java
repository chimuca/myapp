package com.weishe.weichat.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.weishe.weichat.bean.Attachment;

@Repository
public class AttachmentDao extends BaseDao {

	public Attachment getAttachmentById(int id) {
		String hql = "from  Attachment where id =? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setInteger(0, id);
		Attachment g = (Attachment) query.uniqueResult();
		return g;
	}

	public Attachment getAttachmentByGroupNameAndPath(String groupName,
			String path) {
		String hql = "from  Attachment where groupName =?  and path=? ";
		Query query = this.getSessionFactory().getCurrentSession()
				.createQuery(hql);
		query.setString(0, groupName);
		query.setString(1, path);
		Attachment g = (Attachment) query.uniqueResult();
		return g;
	}

	public Attachment saveAttachment(Attachment attachment) {
		Integer id = (Integer) this.save(attachment);
		attachment.setId(id);
		return attachment;
	}

}
