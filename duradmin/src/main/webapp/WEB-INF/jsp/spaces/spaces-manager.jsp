<%@include file="/WEB-INF/jsp/include.jsp" %>
<tiles:insertDefinition name="app-base">
	<tiles:putAttribute name="title">
		<spring:message code="spaces" />	
	</tiles:putAttribute>
	<tiles:putAttribute name="header-extensions">
			<link rel="stylesheet" href="${pageContext.request.contextPath}/jquery/plugins/fancybox/jquery.fancybox-1.3.1.css" type="text/css" media="screen" />
			<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/plugins/fancybox/jquery.fancybox-1.3.1.pack.js"></script>
			<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/plugins/fancybox/jquery.easing-1.3.pack.js"></script>
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/ui.selectablelist.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/ui.expandopanel.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/javascript/spaces-manager.js"></script>
	</tiles:putAttribute>
	<tiles:putAttribute name="body">
	<tiles:insertDefinition name="app-frame">
		<tiles:putAttribute name="mainTab" value="spaces" />
		<tiles:putAttribute name="main-content">
			<div class="center-north" id="center-pane-north">
				<div class="float-l">
					Provider: <a class="flex button" id="select-provider" href="#"><span><i class="post arw-down-liteblue">Amazon S3</i></span></a>
				</div>
		
				<!-- <div class="float-l">
		        	<ul>
		        		<li>
				        	<input type="button" value="Browse"/>
				    	</li>
			       		<li>
				        	<input type="button" value="Search"/>
				    	</li>
		        	</ul>
		        </div> -->
	
				<div class="float-r" id="pinned">
					<span>
					4 Items
					<input type="button" value="+"/>   			
					<input type="button" value="^"/>   	
					</span>	   					
		   		</div>
		   	</div>
		   	<div id="list-browser">
		   		<div id="spaces-list-view" class="dc-list-item-viewer">
					<div class="north header list-header clearfix">
						<div id="header-spaces-list" class="clearfix">
							<h2>Spaces</h2>
							<a class="flex button float-r add-space-button" href="javascript:void(1);"><span><i class="pre plus"><i class="post arw-down-green">Add Space</i></i></span></a>
						</div>
						<div class="header">
							<span class="float-r"><input class="dc-item-list-filter" type="text"/></span>
							<input id="check-all-spaces" class="dc-check-all" type="checkbox"/> 
						</div>
					</div>
				
					<div class="center dc-item-list-wrapper">
						<div class="dc-item-list" id="spaces-list">
						</div>
					</div>
				</div>
				<div id="content-item-list-view" class="dc-list-item-viewer">
					<div class="north header list-header clearfix">
						<div id="header-content-list" class="clearfix">
							<h2>Content Items</h2>
							<a class="flex button float-r add-content-item-button" href="javascript:void(1);"><span><i class="pre plus"><i class="post arw-down-green">Add Content Item</i></i></span></a>
						</div>
						<div class="header" >
							<span class="float-r"><input type="text"/></span>
							<input id="check-all-content-items" class="dc-check-all" type="checkbox"/> prev next
						</div>
					</div>
				
					<div class="center dc-item-list-wrapper">
						<div class="dc-item-list" id="content-item-list">
						</div>
					</div>
				</div>
		   	</div>
			<div id="detail-pane" class="detail-pane"></div>	
		
		<!-- 
		Space Detail Pane:  The div is invisible and used as a prototype for displaying specific space
							details.
		 -->
		<div id="spaceDetailPane" class="dc-detail-pane" style="display:none">
			<div class="north header">
				<h1>Space Detail</h1>
				<h2 class="object-name">Space Name Here</h2>
				<div class="toggle-control flex switch-holder">
				    <div class="r">
				        <span class="flex button-holder button-holder-on">
				        	<i class="pre unlock unlocked">Open</i><a class="flex button switch"><span><i class="pre lock">Close</i></span></a>
				        </span>
				        
				        <span class="flex button-holder button-holder-off">
				        	<a class="flex button switch"><span><i class="pre unlock">Open</i></span></a><i class="pre lock locked">Closed</i>
				        </span>            
				        
				        <span class="flex button-holder">
				        	<a class="flex button switch"><span><i class="pre unlock">Open</i></span></a><a class="flex button switch" style="margin-left:-2px;"><span><i class="pre lock">Close</i></span></a>
				        </span>
				    </div>
				</div>
				<div class="button-bar">
					<a href="javascript:void(1);" class="flex button add-content-item-button"><span><i class="pre plus"><i class="post arw-down-green">Add Content Item</i></i></span></a>
					<a href="javascript:void(1);" class="flex button std"><span><i class="pre trash">Delete Space</i></span></a>
				</div>
	
			</div>
			<div class="center">
			</div>
			<div class="east"><!-- used for space purposes only --></div>
	
		</div>
	
		<div id="genericDetailPane" style="display:none">
			<div class="north header">
				Nothing is selected.
			</div>
			<div class="center">
			</div>
		</div>
	
		<div id="spaceMultiSelectPane" style="display:none">
			<div class="north">
				<h3 class="header"><img src="xxx" height="25" width="25" style="background-color:#DDD"/> Space Detail</h3>
				<h5 class="header object-name">Multiple Spaces Selected</h5>
				<div class="header toggle-control">
					Access: 
					<input type="button" value="Open"/>
					<input type="button" value="Close"/>
				</div>
				<div class="header button-bar">
					<ul class="horizontal-list">
						<li>
							<input type="button" value="Delete"/>
						</li>
					</ul>
				</div>
			</div>
			<div class="center">
			</div>
		</div>
	
		<div id="contentItemMultiSelectPane" style="display:none">
			<div class="north header">
				<h3 class="header"><img src="xxx" height="25" width="25" style="background-color:#DDD"/> Content Item Detail</h3>
				<h5 class="header object-name">Multiple Content Items Selected</h5>
				<div class="header toggle-control">
					Access: 
					<input type="button" value="Open"/>
					<input type="button" value="Close"/>
				</div>
				<div class="header button-bar">
					<ul class="horizontal-list">
						<li>
							<input type="button" value="Delete"/>
						</li>
					</ul>
				</div>
			</div>
			<div class="center">
			</div>
		</div>
	
		<!-- 
			an invisible  prototype for content items details.
		 -->
		<div id="contentItemDetailPane" style="display:none" >
			<div class="north header">
				<h1>Content Detail</h1>
				<h2 class="object-name">Content Name Here</h2>
				<div class="mime-type" id="mime-image">
					<span class="label">Mime Type:</span> <span class="value">image/jpg</span>
				</div>
				
				<div class="button-bar">
					<a href="javascript:void(1);" class="flex button"><span><i class="pre pencil">Edit</i></span></a>
					<a href="javascript:void(1);" class="flex button"><span>Download</span></a>
					<a href="javascript:void(1);" class="flex button std"><span><i class="pre trash">Delete</i></span></a>
				</div>
			</div>
			<div class="center">
	
			</div>
			
			<div class="east"><!-- used for space purposes only --></div>
			
		</div>
		
		<div id="add-space-dialog" class="dc-dialog" title="Add Space">
			<p class="validateTips">All form fields are required.</p>
			<form>
			<fieldset>
				<label for="name">Name</label>
				<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
			</fieldset>
			</form>
	
		</div>
	
		<div id="add-content-item-dialog" class="dc-dialog" title="Add Content Item">
			<form>
			<fieldset>
				<label for="name">Item Name</label>
				<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
			</fieldset>
			</form>
	
		</div>
		</tiles:putAttribute>
		
</tiles:insertDefinition>	
	</tiles:putAttribute>
	
</tiles:insertDefinition>



