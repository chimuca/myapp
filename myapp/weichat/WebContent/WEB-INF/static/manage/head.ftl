<#assign config_v="20140830004">
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="Mosaddek">
<meta name="keyword"
	content="FlatLab, Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">
<link rel="shortcut icon" href="${BASE_PATH}/static/img/favicon.png">
<title>后台</title>
<!-- Bootstrap core CSS -->
<link href="${BASE_PATH}/static/manage/css/bootstrap.min.css?v=${config_v}" rel="stylesheet">
<link href="${BASE_PATH}/static/manage/css/bootstrap-reset.css?v=${config_v}"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/manage/css/gallery.css?v=${config_v}" />	
<!--external css-->
<link
	href="${BASE_PATH}/static/manage/assets/font-awesome/css/font-awesome.css?v=${config_v}"
	rel="stylesheet" />
<link href="${BASE_PATH}/static/manage/assets/fancybox/source/jquery.fancybox.css?v=${config_v}" rel="stylesheet" />	
<!-- Custom styles for this template -->
<link href="${BASE_PATH}/static/manage/css/style.css?v=${config_v}" rel="stylesheet">
<link href="${BASE_PATH}/static/manage/css/style-responsive.css?v=${config_v}" rel="stylesheet" />
<link href="${BASE_PATH}/static/manage/assets/uploadify/uploadify.css?v=${config_v}" rel="stylesheet" />
<link href="${BASE_PATH}/static/manage/assets/bootstrap.datetimepicker/css/bootstrap-datetimepicker.min.css?v=${config_v}" rel="stylesheet" />

<!-- HTML5 shim and Respond.js IE8 support of HTML5 tooltipss and media queries -->
<!--[if lt IE 9]>
      <script src="${BASE_PATH}/static/manage/js/html5shiv.js"></script>
      <script src="${BASE_PATH}/static/manage/js/respond.min.js"></script>
    <![endif]-->
	<script type="text/javascript">
		window.BasePath = "${BASE_PATH}";
		window.UEDITOR_HOME_URL = "${BASE_PATH}/";
		kindId = 0;
		kind = "article";
	</script>
<script src="${BASE_PATH}/static/manage/js/jquery.js?v=${config_v}"></script>
</head>
<body class="boxed-page">
	<div class="container">
	<section id="container" class="">
		<!--header start-->
		<header class="white-bg">
			<div class="container" style="background-color: #ffffff; padding: 10px;">
				<!--logo start-->
				<a href="${BASE_PATH}/index.htm" class="logo" title="访问前台页面">
					<img src="${BASE_PATH}/static/img/logo.png" style="height: 38px;" />
				</a>
				<!--logo end-->
				<div class="nav notify-row" id="top_menu">
					<!--  notification goes here -->
				</div>
				<div class="top-nav ">
	
					<ul class="nav pull-right top-menu">
	                  <!-- user login dropdown start-->
	                  <li class="dropdown">
	                      <a href="#" class="dropdown-toggle" data-toggle="dropdown">
	                          <span class="username">${SESSION_USER.name}</span>
	                          <b class="caret"></b>
	                      </a>
	                      <ul class="dropdown-menu extended logout">
	                          <div class="log-arrow-up"></div>
	                          <li><a href="${BASE_PATH}/manage/admin/update.htm"><i class="icon-cog"></i> 修改密码</a></li>
	                          <li><a href="${BASE_PATH}/user/logout.htm"><i class="icon-key"></i> 安全退出</a></li>
	                      </ul>
	                  </li>
	                  <!-- user login dropdown end -->
	              </ul>
	          
				</div>
			</div>
		</header>
		<!--header end-->
		<!--sidebar start-->
		<aside>
			<div id="sidebar" class="nav-collapse ">
				<!-- sidebar menu goes here-->
				<ul class="sidebar-menu" id="nav-accordion">
					 
					<li class="">
						<a <#if menu="user_list">class="active"</#if> href="${BASE_PATH}/user/list.htm"> <i class="icon-user"></i> <span>用户列表</span></a>
					</li>
				</ul>
			</div>
		</aside>
		<!--sidebar end-->		
