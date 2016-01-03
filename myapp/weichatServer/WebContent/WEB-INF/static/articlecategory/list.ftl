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
									<a href="${BASE_PATH}/articlecategory/list.htm?deleteFlag=-1">全部分类(${allCount})</a>
								</li>
								<li>
									<a href="${BASE_PATH}/articlecategory/list.htm?deleteFlag=0">生效分类(${initCount})</a>
								</li>
								<li>
									<a href="${BASE_PATH}/articlecategory/list.htm?deleteFlag=1">失效分类(${noCount})</a>
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
                                      <label class="col-sm-2 col-sm-2 control-label">分类名称</label>
                                      <div class="col-sm-10">
                                          <input type="text" class="form-control" name="name"
                                            placeholder="分类名称" id="name" >
                                      </div>
                                  </div>
                                  <div class="form-group">
                                      <label class="col-sm-2 col-sm-2 control-label">描述</label>
                                      <div class="col-sm-10">
                                          <textarea type="text" class="form-control" name="description"
                                            placeholder="分类描述"></textarea>
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
			
			 <header class="panel-heading">
		                <div class="row">
		                  		<div class="col-lg-2">
							<ul class="breadcrumb" style="margin-bottom:0px;">
								<li>
									<select class="js_user_check" id="deleteFlag" > 
										<option value="0"  <#if deleteFlag==0> selected </#if> >生效分类</option>
										<option value="1" <#if deleteFlag==1>selected</#if>>失效分类</option>
										<option value="-1" <#if deleteFlag==-1>selected</#if>  >全部分类</option>
									</select>
								</li>
			 
							</ul>
						   </div>
						   <div class="col-lg-5">
						        
				        		<div class="form-group">  
				           			 <div class="input-group"> 
				              		  <input type="text" class="form-control" id="likestr" name="likestr" placeholder="分类名或描述（可使用“*”作为通配符）" value="${likestr!''}"> 
				               		 <span class="input-group-btn"> 
				                 		   <button class="btn js_search btn-danger" type="button">查询</button> 
				               		 </span> 
				            </div> 
				        </div> 
				        </div>
				</div>
			</header>
			
			<div class="panel-heading">
                <div class="panel-body col-lg-12">
                	<div class="adv-table">
                    	<div role="grid" class="dataTables_wrapper" id="hidden-table-info_wrapper">
                            <table class="table table-striped table-advance table-hover">
                            	<thead>
                                	<tr>
                                	    <th>文章类名</th>
										<th>创建人</th>
										<th>创建时间</th>
										<th>是否有效</th>
                						<th>描述</th>
                						<th>操作</th>
              						</tr>
                                </thead>
                            	<tbody role="alert" aria-live="polite" aria-relevant="all">
                            		<#list pageVo.list as e>
                            		<tr class="gradeA odd">
                						<td>${e.name}</td>	
								     	<td>${e.createBy.name}</td>
								     	<td>${e.createTime?string("yyyy-MM-dd")}</td>
								     	<td>
									     	<select class="js_user_check" id="deleteFlag" > 
												<option value="0"  <#if deleteFlag==0> selected </#if> >生效分类</option>
												<option value="1" <#if deleteFlag==1>selected</#if>>失效分类</option>
											</select>
                            			</td> 
                                        <td>${e.description}</td>
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
						$.post("${BASE_PATH}/user/delete.json", { "id": id},function(data){
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
	  var description = add_form.description.value;
	  $.post("${BASE_PATH}/articlecategory/add.json", { "name": name,"decsription":description},function(data){
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
	 window.location.href="${BASE_PATH}/articlecategory/list.htm?deleteFlag="+$(this).val();
    });	
    $(".js_search").click(function(){
	 window.location.href="${BASE_PATH}/articlecategory/list.htm?name="+$(likestr).val()+"&deleteFlag="+$(deleteFlag).val();
    });		
});
  
</script>
<#include "/manage/foot.ftl">
