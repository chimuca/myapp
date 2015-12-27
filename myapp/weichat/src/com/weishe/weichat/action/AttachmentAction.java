package com.weishe.weichat.action;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.weishe.weichat.bean.Attachment;
import com.weishe.weichat.bean.Result;
import com.weishe.weichat.service.AttachmentService;

@Controller
@RequestMapping(value = "attachment")
public class AttachmentAction {

	@Autowired
	private AttachmentService attachmentService;

	/**
	 * 
	 * @param request
	 * @param condition
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addAttachment.json")
	public Result addAttachment(
			HttpServletRequest request,
			@RequestParam(value = "attachment", defaultValue = "") String attachment) {
		Attachment a = JSON.parseObject(attachment, Attachment.class);

		if (a != null) {
			attachmentService.saveAttachment(a);
			return new Result(true, "添加成功", a);
		} else {
			return new Result(false, "添加失败");
		}

	}

}
