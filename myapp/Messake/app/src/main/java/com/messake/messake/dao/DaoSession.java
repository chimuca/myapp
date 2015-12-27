package com.messake.messake.dao;

import java.util.Map;

import android.database.sqlite.SQLiteDatabase;

import com.messake.messake.bean.Attachment;
import com.messake.messake.bean.Todo;
import com.messake.messake.core.ChatMessage;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;



/**
 *
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

	private final DaoConfig chatMessageDaoConfig;
	private final DaoConfig todoDaoConfig;
	private final DaoConfig attachmentDaoConfig;

	private final ChatMessageDao chatMessageDao;
	private final TodoDao todoDao;
	private final AttachmentDao attachmentDao;

	public DaoSession(SQLiteDatabase db, IdentityScopeType type,
			Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
		super(db);

		chatMessageDaoConfig = daoConfigMap.get(ChatMessageDao.class).clone();
		chatMessageDaoConfig.initIdentityScope(type);

		todoDaoConfig = daoConfigMap.get(TodoDao.class).clone();
		todoDaoConfig.initIdentityScope(type);

		attachmentDaoConfig = daoConfigMap.get(AttachmentDao.class).clone();
		attachmentDaoConfig.initIdentityScope(type);

		chatMessageDao = new ChatMessageDao(chatMessageDaoConfig, this);
		todoDao = new TodoDao(todoDaoConfig, this);
		attachmentDao = new AttachmentDao(attachmentDaoConfig, this);

		registerDao(ChatMessage.class, chatMessageDao);
		registerDao(Todo.class, todoDao);
		registerDao(Attachment.class, attachmentDao);
	}

	public void clear() {
		chatMessageDaoConfig.getIdentityScope().clear();
		todoDaoConfig.getIdentityScope().clear();
		attachmentDaoConfig.getIdentityScope().clear();
	}

	public ChatMessageDao getChatMessageDao() {
		return chatMessageDao;
	}

	public TodoDao getTodoDao() {
		return todoDao;
	}

	public AttachmentDao getAttachmentDao() {
		return attachmentDao;
	}

}
