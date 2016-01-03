package com.weishe.weichat.core.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.weishe.weichat.bean.User;
import com.weishe.weichat.service.FriendsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class FriendsServiceTest {

	@Autowired
	private FriendsService friendsService;

	@Test
	public void addFriendTest() {
		User u1 = new User();
		User u2 = new User();
		u1.setId(1);
		u2.setId(2);
		friendsService.addFriend(u1, u2);
	}

	// @Test
	public void getFriendTest() {
		// List<User> us = friendsService.getFriendsByUserId(1);
		// System.out.println("us:" + us.size());
	}

}