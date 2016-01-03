package com.weishe.weichat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weishe.weichat.bean.DiscussionGroup;
import com.weishe.weichat.bean.DiscussionGroupMember;
import com.weishe.weichat.dao.DiscussionGroupMemberDao;

@Service
public class DiscussionGroupMemberService {
	@Autowired
	private DiscussionGroupMemberDao discussionGroupMemberDao;

	/**
	 * 得到群中没有屏蔽群消息的成员
	 * 
	 * @param DiscussionGroupId
	 * @return
	 */
	public List<DiscussionGroupMember> getUnshieldMemberByDiscussionGroupId(
			int DiscussionGroupId) {
		return discussionGroupMemberDao
				.getUnshieldMemberByDiscussionGroupId(DiscussionGroupId);
	}

	public List<DiscussionGroup> getDiscussionGroupByMemberId(int userId) {
		return discussionGroupMemberDao.getDiscussionGroupByMember(userId);
	}
}
