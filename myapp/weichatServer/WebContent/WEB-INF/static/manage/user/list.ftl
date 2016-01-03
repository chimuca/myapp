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
									<a href="${BASE_PATH}/user/list.htm?deleteFlag=-1">全部用户(${allCount})</a>
								</li>
								<li>
									<a href="${BASE_PATH}/user/list.htm?deleteFlag=0">生效用户(${initCount})</a>
								</li>
								<li>
									<a href="${BASE_PATH}/user/list.htm?deleteFlag=1">失效用户(${noCount})</a>
								</li>
	                     
						
	                      </ul>
	                  </div>
	               
	              </div>
   
        	<!-- page start-->
            <section class="panel">
	                <header class="panel-heading">
		                <div class="row">
		                  		<div class="col-lg-2">
							<ul class="breadcrumb" style="margin-bottom:0px;">
								<li>
									<select class="js_user_check" id="deleteFlag" > 
										<option value="0"  <#if deleteFlag==0> selected </#if> >生效用户</option>
										<option value="1" <#if deleteFlag==1>selected</#if>>失效用户</option>
										<option value="-1" <#if deleteFlag==-1>selected</#if>  >全部用户</option>
									</select>
								</li>
			 
							</ul>
						   </div>
						   <div class="col-lg-5">
						        
				        		<div class="form-group">  
				           			 <div class="input-group"> 
				              		  <input type="text" class="form-control" id="nameLike" name="nameLike" placeholder="用户名（如罗*浩 或 王自*）" value="${name!''}"> 
				               		 <span class="input-group-btn"> 
				                 		   <button class="btn js_search_user btn-danger" type="button">查询</button> 
				               		 </span> 
				            </div> 
				        </div> 
				        </div>
      
   
				   <div class="col-lg-5">
						<a class="btn btn-primary" style="float:right;" href="${BASE_PATH}/user/add.htm">新建用户</a>
				   </div>
				</div>
			</header>
                <div class="panel-body">
                	<div class="adv-table">
                    	<div role="grid" class="dataTables_wrapper" id="hidden-table-info_wrapper">
                            <table class="table table-striped table-advance table-hover">
                            	<thead>
                                	<tr>
                                	    <th>姓名</th>
										<th>账户</th>
										<th>邮箱</th>
										<th>电话</th>
                						<th>最后更新时间</th>
                						<th>操作</th>
              						</tr>
                                </thead>
                            	<tbody role="alert" aria-live="polite" aria-relevant="all">
                            		<#list pageVo.list as e>
                            		<tr class="gradeA odd">
               							<td>
               								<a href="${BASE_PATH}/manage/article/update.htm?id=${e.id}">${e.name}</a>
               							</td> 
                						<td>${e.account}</td>	
								     	<td>${e.email}</td>
                            			<td>${e.phone}</td>
                                        <td>${e.lastUpdateTime}</td>
                                    	 <td>
                                           <div class="btn-group" role="group" aria-label="...">
                                           <a class="btn btn-info" href="${BASE_PATH}/user/preview.htm?id=${e.id}">预览</a>
  										   <a class="btn btn-warning" href="${BASE_PATH}/user/update.htm?id=${e.id}" title="修改">编辑</a>
 										   <a href="javascript:void(0);" class="btn btn-danger js_user_delete" id="${e.id}" title="是否删除文章">删除</a>
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
              </section>
              	                
              <!-- page end-->
          </section>
		</section>
		<!--main content end-->
		
	
<script>
$(function(){
var pageNum = "${p}";
	$('.js_user_delete').click(function(){
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
	$(".js_user_check").change(function(){
	 window.location.href="${BASE_PATH}/user/list.htm?deleteFlag="+$(this).val();
    });	
    $(".js_search_user").click(function(){
	 window.location.href="${BASE_PATH}/user/search.htm?name="+$(nameLike).val()+"&deleteFlag="+$(deleteFlag).val();
    });		
});
  
</script>
<#include "/manage/foot.ftl">
