package com.weishe.weichat.core.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.weishe.weichat.bean.User;

public class JsonTest {
	static String json = "[{\"account\":\"account0\",\"password\":\"XXXXX\",\"name\":\"name0\",\"id\":0},{\"account\":\"account1\",\"password\":\"XXXXX\",\"name\":\"name1\",\"id\":1},{\"account\":\"account2\",\"password\":\"XXXXX\",\"name\":\"name2\",\"id\":2}]";

	public static void main(String[] args) {
		List<User> list = new ArrayList<User>();
		for (int i = 0; i < 3; i++) {
			User u = new User();
			u.setAccount("account" + i);
			u.setId(i);
			u.setName("name" + i);
			u.setPassword("XXXXX");
			list.add(u);
		}
		Gson g = new Gson();
		String s = JSON.toJSONString(list);
		String s2 = g.toJson(list);
		System.out.println(s);
		System.out.println(s2);

		List<User> l = (List<User>) JSON.parseArray(json, User.class);

		System.out.println(l.size() + "    " + l.get(2).getAccount());
	}
}
