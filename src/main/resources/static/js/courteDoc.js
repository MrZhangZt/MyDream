$(function() {
	let setting = {
			view: {
				dblClickExpand: true
			},
			async: {
				enable: true,
				url:"json/court.json",
				dataFilter: filter,
				type: "get"
			},
			callback: {
				onClick: onClick
			}
		};
	
	/* 获取存在session里的登陆人信息 */
	const USER_INFO= eval('('+ $('#session').val() +')');
	$.fn.zTree.init($("#treeDemo"), setting, []);
	$.fn.datepicker.dates['cn'] = {   //切换为中文显示  
		    days: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],  
		            daysShort: ["日", "一", "二", "三", "四", "五", "六", "七"],  
		            daysMin: ["日", "一", "二", "三", "四", "五", "六", "七"],  
		            months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],  
		            monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],  
		            today: "今天",  
		            clear: "清除"  
		    }; 
	
	$('.selectData').datepicker({  
	    autoclose: true, //自动关闭  
	    beforeShowDay: $.noop,    //在显示日期之前调用的函数  
	    calendarWeeks: false,     //是否显示今年是第几周  
	    clearBtn: true,          //显示清除按钮  
	    daysOfWeekDisabled: [],   //星期几不可选  
	    endDate: Infinity,        //日历结束日期 
	    forceParse: true,         //是否强制转换不符合格式的字符串
	    format: 'yyyy-mm-dd',     //日期格式  
	    keyboardNavigation: true, //是否显示箭头导航  
	    language: 'cn',           //语言  
	    minViewMode: 0,  
	    orientation: "auto",      //方向  
	    rtl: false,  
	    startDate: -Infinity,     //日历开始日期  
	    startView: 0,             //开始显示  
	    todayBtn: false,          //今天按钮  
	    todayHighlight: false,    //今天高亮  
	    weekStart: 0              //星期几是开始  
	});
	
	search();
	
	//打印按钮
	$('#print').click(function() {
		$('#printDiv').print({
		    globalStyles:true,//是否包含父文档的样式，默认为true
		    mediaPrint:false,//是否包含media='print'的链接标签。会被globalStyles选项覆盖，默认为false
		    stylesheet:null,//外部样式表的URL地址，默认为null
		    noPrintSelector:".no-print",//不想打印的元素的jQuery选择器，默认为".no-print"
		    iframe:true,//是否使用一个iframe来替代打印表单的弹出窗口，true为在本页面进行打印，false就是说新开一个页面打印，默认为true
		    append:null,//将内容添加到打印内容的后面
		    prepend:null,//将内容添加到打印内容的前面，可以用来作为要打印内容
		    deferred:
		 $.Deferred()//回调函数
		});
	});
		
		//下载按钮
		$('#download').click(function() {
			Html2Word();
		});
    
    //搜索按钮事件
    $('#search').click(function() {
    	/*if ($('#caseType').val() == '0') {
    		if ($('#courtname').val()=='' &&
 				$('#process').val()=='' &&
				$('#content').val()=='' &&
				$('#judgedatestart').val()=='' &&
				$('#judgedateend').val()=='' &&
				$('#head').val()=='') {
    			alert("请输入至少一个查询条件！");
    			return false;
    		}
    	}*/
    	$('#table').bootstrapTable('showLoading');
    	$.ajax({
    		url: '/docmentsController/getDocListByCondition.do',
    		data : {
 				pageSize: 10,  
 				pageNumber: 1,
 				type: $('#caseType').val(),
 				courtname: $('#courtname').val(),
 				process: $('#process').val(), 
				content: $('#content').val(),
				judgedatestart: $('#judgedatestart').val(),
				judgedateend: $('#judgedateend').val(),
				head: $('#head').val()
			},
    		dataType : 'json',
    		success : function(data) {
    			$('#table').bootstrapTable('load',data);
    			$('#table').bootstrapTable('hideLoading');
    		}
    	}); 
    });
    
    /* 清除查询条件 */
    $('#reset').click(function() {
    	$('#courtname').val("");
		$('#process').val("");
		$('#content').val("");
		$('#caseid').val("");
		$('#judgedatestart').val("");
		$('#judgedateend').val("");
		$('#head').val("");
		$('#search').click();
    });
    
    /*  */
    $('#courtname').click(function(){
    	showMenu();
    })
    
});


/* 根据id获取最新状态 */
let detail = function(id) {
	 $.ajax({
		url: '/docmentsController/getDetailByDocId.do',
		data : {docid: id},
		dataType : 'json',
		success : function(data) {
			let docText =eval("(" + data.rows.caseDetail.docJsonhtmldata + ")");
			$('#docText').html(docText.Html);
			$('#docSubDate').html('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发布时间: ' +docText.PubDate);
			$('#docTitle').html(docText.Title);
			$("#hidDocID").val(id);
			$('#DocDetail').modal('show');
		}
	});
}

/* 查询本人处理的申请（包括已经处理完的） */
let search = function() {
	$('#table').bootstrapTable({
    url: '/docmentsController/getDocListByCondition.do?random='+Math.random(),
    method: 'post', //请求方式 
    contentType : 'application/x-www-form-urlencoded',
    dataType: 'json', //返回数据类型
    pagination: true, //分页
    striped: true, //隔行背景色
    singleSelect: false, //是否多选行
    search: false, //显示搜索框
    pageNumber: 1,
    pageSize: 10, 
    pageList: [10, 25, 50],
    sidePagination: 'server', //服务端处理分页
    queryParamsType: 'limit',
    queryParams: function(params) {
		return {
      				pageSize: this.pageSize,  
      				pageNumber: this.pageNumber,
      				type: $('#caseType').val(),
      				courtname: $('#courtname').val(),
      				process: $('#process').val(), 
     				content: $('#content').val(),
     				judger: $('#judger').val(),
     				judgedatestart: $('#judgedatestart').val(),
     				judgedateend: $('#judgedateend').val(),
     				head: $('#head').val()
 				} 
 			},
    showRefresh: false, //刷新按钮
    clickToSelect: false,
    columns: [
    	    {
            	checkbox: true  
            },
	    	{
				title: '序号',
				field: '',
				align: 'center',
				valign: 'middle',
				formatter: function(value,row,index) {
					return index + 1;
				}
	          },
    		  {
				title: '案件号',
				field: 'caseNum',
				align: 'left',
				valign: 'middle'
              },
              {
				title: '法院名',
				field: 'courtName',
				align: 'left',
				valign: 'middle',
              },
              {
  				title: '案件名',
  				field: 'title',
  				align: 'left',
  				valign: 'middle'
              },
        	  {
  				title: '操作',
  				field: 'docId',
  				align: 'center',
  				valign: 'middle',
  				formatter: function(value, row, index) {
  					let docid="'"+value +"'";
  					return '<button style="font-size:14px;" class="btn btn-info btn-sm btn-block" onclick="detail('+ docid +')">详情</button>';
			    }
                }]
      });
}

function onClick(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	if (treeNode && !treeNode.isParent) {
		nodes = zTree.getSelectedNodes();
		v = "";
		nodes.sort(function compare(a,b){return a.id-b.id;});
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].name + ",";
		}
		if (v.length > 0 ) v = v.substring(0, v.length-1);
		$("#courtname").val(v);
		hideMenu();
	} else {
		zTree.expandNode(treeNode);
	}
}

function filter(treeId, parentNode, childNodes) {
	if (!childNodes) return null;
	for (var i=0, l=childNodes.length; i<l; i++) {
		childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
	}
	return childNodes;
}

function asyncAll() {
	if (!check()) {
		return;
	}
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	var nodes = zTree.getNodes();
	for (var i=0, l=nodes.length; i<l; i++) {
		if (nodes[i].isParent && nodes[i].zAsync) {
			asyncNodes(nodes[i].children);
		} else {
			goAsync = true;
			zTree.reAsyncChildNodes(nodes[i], "refresh", true);
		}
	}
}

function showMenu() {
	var cityObj = $("#courtname");
	var cityOffset = $("#courtname").offset();
	$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown);
}
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
}

let Html2Word = function () {
    let $content = $("#Content");
    let contentTitle = $.trim($("#printDiv").text());
    let content = "";
    let docid = $("#hidDocID").val();
    content += $content.html();
    let body = document.body;
    let formid = 'DownloadForm';
    let url = '/docmentsController/downloadFile.do';
    let node = document.getElementById(formid);
    if (node != null) {
        node.parentNode.removeChild(node);
    }
    let theForm = document.createElement('form');
    theForm.id = formid;
    theForm.action = url;
    theForm.method = 'post';
 
    node = document.createElement('input');
    node.type = 'hidden';
    node.id = 'docid';
    node.name = 'docid';
    node.value = docid;
    theForm.appendChild(node);
    theForm.appendChild(node);
    body.appendChild(theForm);
    theForm.submit();
};