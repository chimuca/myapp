<#assign menu="user">
<#assign submenu="add_user">
<#include "/manage/head.ftl">
<style type="text/css">
.m-bot15 {
    margin-bottom: 5px;
}
.form-control {
    border: 1px solid #E2E2E4;
    box-shadow: none;
    color: #C2C2C2;
}
.input-lg {
    border-radius: 6px;
    font-size: 15px;
    height: 40px;
    line-height: 1.33;
    padding: 9px 15px；
}

</style>
		<!--main content start-->
		<section id="main-content">
			<section class="wrapper">
              <!-- page start-->
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">
                            	 添加用户
                          </header>
                          <div class="panel-body">
                              <form id="add_user_form" name="add_user_form" method="post" class="form-horizontal" autocomplete="off" action="${BASE_PATH}/user/addUser.json">
                              	<fieldset>
                                 	  <div class="form-group">
	                                      <label class="col-sm-2  control-label">账户</label>
	                                      <div class="col-sm-8">
	                                          <input type="text" class="text-primary form-control" name="account"
	                                          	placeholder="账户" id="account" vaule="">
	                                      </div>
                                      </div>
                                      
                                      <div class="form-group">
	                                      <label class="col-sm-2   control-label">用户名称</label>
	                                      <div class="col-sm-8">
	                                          <input type="text" class="text-primary form-control" name="name"
	                                          	placeholder="用户名称" id="name" vaule="">
	                                      </div>
                                      </div>
                                      
                                     <div class="form-group">
	                                      <label class="col-sm-2  control-label">密码</label>
	                                      <div class="col-sm-8">
	                                          <input type="password" class="form-control" name="password"
	                                          	placeholder="密码" id="password" vaule="">
	                                      </div>
                                      </div>
                                      
                                      <div class="form-group">
	                                      <label class="col-sm-2  control-label">确认密码</label>
	                                      <div class="col-sm-8">
	                                          <input type="password" class="form-control" name="repassword"
	                                          	placeholder="确认密码" id="repassword" vaule="" onblur="checkpsd2()">
	                                          	<span class="tips text-danger" id="divpassword2"></span>
	                                      </div>
                                      </div>
                                      
                                      <div class="form-group">
	                                      <label class="col-sm-2   control-label">电话号码</label>
	                                      <div class="col-sm-8">
	                                          <input type="text" class="text-primary form-control" name="phone"
	                                          	placeholder="电话号码" id="phone" vaule="" onblur="checkphone()">
	                                          	<span class="tips text-danger" id="divphone"></span>
	                                      </div>
                                      </div>
                                      
                                      <div class="form-group">
	                                      <label class="col-sm-2   control-label">邮箱</label>
	                                      <div class="col-sm-8">
	                                          <input type="text" class="text-primary form-control" name="email"
	                                          	placeholder="邮箱" id="email" vaule="" onblur="checkmail()">
	                                          	<span class="tips text-danger" id="divmail"></span>
	                                      </div>
                                      </div>

                                  <div class="form-group">
                                  	<label class="col-sm-2 col-sm-2 control-label"></label>
                                      <button class="btn btn-danger" type="submit" id="submit_btn">增加</button>
                                  </div>
                                 </fieldset>
                              </form>
                          </div>
                      </section>
                  </div>
              </div>
              <!-- page end-->
          </section>
		</section>
		<!--main content end-->
<script type="text/javascript">
	$(function() {
		$('#add_user_form').ajaxForm({
			dataType : 'json',
			success : function(data) {
				if (data.result) {
					bootbox.alert("保存成功，将刷新页面", function() {
						window.location.reload();
					});
				}else{
					showErrors($('#add_user_form'),data.errors);
				}
			}
		});
	});	
	
	//验证确认密码 
		function checkpsd2(){ 
		
				if(add_user_form.password.value!=add_user_form.repassword.value) { 
				     divpassword2.innerHTML='<font class="tips_false">您两次输入的密码不一样</font>';
				    $("#submit_btn").attr("disabled", true);
				    $("#submit_btn").attr("disabled", true);
				} else { 
				   $("#submit_btn").attr("disabled", false);
				}
		}
		//验证邮箱
		
		function checkmail(){
					apos=add_user_form.email.value.indexOf("@");
					dotpos=add_user_form.email.value.lastIndexOf(".");
					 
					if (apos<1||dotpos-apos<2) 
					  {
					  	divmail.innerHTML='<font class="tips_false">输入错误</font>' ;
					  	$("#submit_btn").attr("disabled", true);
					  }
					else {
						$("#submit_btn").attr("disabled", false);
					}
		}
		//校验电话号码
		function checkphone(){
   			 var tel = add_user_form.phone.value;
			 var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
			 if (reg.test(tel)) {
			     
			       $("#submit_btn").attr("disabled", true);
			 }else{
			  divphone.innerHTML='<font class="tips_true">输入错误</font>' ;
			       $("#submit_btn").attr("disabled", true);
			 };
		}
</script>
<#include "/manage/foot.ftl">