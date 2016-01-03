<#assign menu="article">
<#assign submenu="article_list">
<#include "/manage/head.ftl">
<style type="text/css">
.pagination {
    border-radius: 4px;
    display: inline-block;
    margin: 0;
    padding-left: 0;
}

.howto, .nonessential, #edit-slug-box, .form-input-tip, .subsubsub {
    color: #666666;
}

.subsubsub {
    float: left;
    font-size: 12px;
    list-style: none outside none;
    margin: 8px 0 5px;
    padding: 0;
}

.form-group{
	width:100%;
}

.count{
	position:absolute ;
	right:0px;
}

.arrticle_status{
	float:left;
}
</style>
	<!--main content start-->
	<section id="main-content">
		<section class="wrapper">
		 
			<div class="row">
	                  <div class="col-lg-12">
	                      <!--breadcrumbs start -->
	                      <ul class="breadcrumb">
								<li>
									<a href="${BASE_PATH}/gpdatasrc/list.htm?deleteFlag=-1">全部API(${allCount})</a>
								</li>
								<li>
									<a href="${BASE_PATH}/gpdatasrc/list.htm?deleteFlag=0">生效API(${initCount})</a>
								</li>
								<li>
									<a href="${BASE_PATH}/gpdatasrc/list.htm?deleteFlag=1">失效API(${noCount})</a>
								</li>
	                     
						
	                      </ul>
	                  </div>
	               
	              </div>
   
        	<!-- page start-->
            <section class="panel">
	        
			<div class="panel-heading">
			<div class="col-lg-12">
				<section class="panel">
					<header class="panel-heading">添加</header>
					 	 <form id="add_form" method="post" class="form-horizontal" autocomplete="off" >
							<!--<fieldset>-->
								  <div class="form-group">
                                      <label class="col-sm-2 col-sm-2 control-label">网站名称</label>
                                      <div class="col-sm-10">
                                          <select  id="name" > 
											<option value="美团网">美团网</option>
											<option value="拉手网">拉手网</option> 
											<option value="窝窝团">窝窝团</option> 
											<option value="大众点评">大众点评</option> 
											<option value="糯米网">糯米网</option> 
											<option value="团800">团800</option>  
										 </select>	
                                      </div>
                                  </div>
                                  <div class="form-group">
                                      <label class="col-sm-2 col-sm-2 control-label">数据分类</label>
                                      <div class="col-sm-10">
                                         <select  id="dataType" > 
											<option value="CITY"   >城市</option>
											<option value="PRODUCT" >商品</option> 
										</select>	
                                      </div>
                                  </div>
                                  <div class="form-group">
                                      <label class="col-sm-2 col-sm-2 control-label">数据文件类型</label>
                                      <div class="col-sm-10">
                                          <select id="fileType" > 
										  <option value="XML"   >XML</option>
										  <option value="JSON" >JSON</option> 
										  </select>	
                                      </div>
                                  </div>
                                  <div class="form-group">
                                      <label class="col-sm-2 col-sm-2 control-label">API地址</label>
                                      <div class="col-sm-10">
                                          <textarea type="text" class="form-control" name="apiUrl"
                                            placeholder="数据API地址"></textarea>
                                      </div>
                                  </div>
                                  <div class="form-group">
                                    <label class="col-sm-2 col-sm-2 control-label"></label>
                                    <div class="col-sm-10">
                                      <!--<button class="btn btn-danger" type="submit" >增加</button>-->
                                      <a href="javascript:void(0);" class="btn btn-danger js_add"  >添加</a>
                                    </div>
                                  </div>		
							<!--</fieldset>-->
						</form>
					 
				</section>
			</div>
			</div>
			

			
			<div class="panel-heading">
                <div class="panel-body col-lg-12">
                	<div class="adv-table">
                    	<div role="grid" class="dataTables_wrapper" id="hidden-table-info_wrapper">
                            <table class="table table-striped table-advance table-hover">
                            	<thead>
                                	<tr>
                                	    <th>网站名</th>
										<th>数据类别</th>
										<th>数据文件类型</th>
										<th>API地址</th>
                						<th>添加者</th>
                						<th>最后更新时间</th>
              						</tr>
                                </thead>
                            	<tbody role="alert" aria-live="polite" aria-relevant="all">
                            		<#list pageVo.list as e>
                            		<tr class="gradeA odd">
                						<td>${e.name}</td>
                						<td>${e.dataType}</td>	
                						<td>${e.fileType}</td>	
                						<td>${e.apiUrl}</td>
								     	<td>${e.createBy.name}</td>
								     	<td>${e.createTime?string("yyyy-MM-dd")}</td>
                                    	 <td>
                                           <div class="btn-group" role="group" aria-label="...">  
 										   <a href="javascript:void(0);" class="btn btn-danger js_delete" id="${e.id}" title="是否删除分类">删除</a>
										 </div>
                						</td>
                                	</tr>
                                	</#list>
                               	</tbody>
                              </table>
	                            
                              <nav>
                              ${pageVo.pageNumHtml}  
                              </nav>
                           </div>
                        </div>
                  </div>
                </div>
              </section>
              	                
              <!-- page end-->
          </section>
		</section>
		<!--main content end-->
		
	
<script>
$(function(){
var pageNum = "${p}";
	$('.js_delete').click(function(){
		var id = $(this).attr('id');
		var status= "trash";
		bootbox.dialog({
			message : $(this).attr('title'),
			title : "提示",
			buttons : {
				"delete" : {
					label : "确定",
					className : "btn-success",
					callback : function() {
						$.post("${BASE_PATH}/gpdatasrc/delete.json", { "id": id},function(data){
								window.location.reload();
						}, "json");
					}
				},
				"cancel" : {
					label : "取消",
					className : "btn-primary",
					callback : function() {
						
					}
				}
			}
		});					
	});	
	$(".js_add").click(
	function(){
	  var name = add_form.name.value;
	  var dataType = add_form.dataType.value;
	  var fileType = add_form.fileType.value;
	  var apiUrl = add_form.apiUrl.value;
	  $.post("${BASE_PATH}/gpdatasrc/add.json", { "name": name,"dataType":dataType,"fileType":fileType,"apiUrl":apiUrl},function(data){
								//window.location.reload();
								if (data.result) {
										bootbox.alert("添加成功，将刷新页面", function() {
											window.location.reload();
										});
									}else{
										showErrors($('#add_form'),data.errors);
									}
						}, "json");
    });
	$(".js_user_check").change(function(){
	 window.location.href="${BASE_PATH}/gpdatasrc/list.htm?deleteFlag="+$(this).val();
    });	
    $(".js_search").click(function(){
	 window.location.href="${BASE_PATH}/gpdatasrc/list.htm?name="+$(likestr).val()+"&deleteFlag="+$(deleteFlag).val();
    });		
});
  
</script>
<#include "/manage/foot.ftl">
