//执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {
    //1.生成分页数据
    var pageInfo = getPageInfoRemote();

    //2.填充表格
    fillTableBody(pageInfo);
}

//远程访问服务器程序获取pageInfo数据
function getPageInfoRemote() {
    //调用$.ajax()函数发送并接收$.ajax()函数的返回值
    //responseJSON: {result: 'SUCCESS', message: null, data: {…}}
    // responseText: "{\"result\":\"SUCCESS\",\"message\":null,\"data\":{\"pageNum\":1,\"pageSize\":5,\"size\":5,\"startRow\":1,\"endRow\":5,\"total\":10,\"pages\":2,\"list\":[{\"id\":1,\"name\":\"PM - 项目经理\"},{\"id\":2,\"name\":\"SE - 软件工程师\"},{\"id\":3,\"name\":\"PG - 程序员\"},{\"id\":4,\"name\":\"TL - 组长\"},{\"id\":5,\"name\":\"GL - 组长\"}],\"firstPage\":1,\"prePage\":0,\"nextPage\":2,\"lastPage\":2,\"isFirstPage\":true,\"isLastPage\":false,\"hasPreviousPage\":false,\"hasNextPage\":true,\"navigatePages\":8,\"navigatepageNums\":[1,2]}}"
    // status: 200
    // statusText: "success"
    var ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,
        "dataType": "json"
    });

    // console.log(ajaxResult);
    //判断当前响应状态码是否为200
    var statusCode = ajaxResult.status;

    //如果当前响应码不是200，说明发生了错误或其他意外情况，显示提示消息，
    //让当前函数停止执行
    if (statusCode != 200) {
        layer.msg("失败！响应状态码= " + statusCode + "说明信息= " + ajaxResult.statusText);
        return null;
    }

    //如果响应状态码是200，说明请求处理成功
    var resultEntity = ajaxResult.responseJSON;

    //从resultEntity中获取result属性
    var result = resultEntity.result;

    //判断result是否成功
    if (result == "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }

    //确认result为成功后获取pageInfo
    var pageInfo = resultEntity.data;

    //返回pageInfo
    return pageInfo;
}

function fillTableBody(pageInfo) {

    //清楚tbody中的旧内容
    $("#rolePageBody").empty();

    //没有搜索结果时不显示页码导航条 ← 清空
    $("#Pagination").empty();

    //判断pageInfo对象是否有效
    if (pageInfo == null || pageInfo == undefined || pageInfo.list == null || pageInfo.list.length == 0) {
        $("#rolePageBody").append(
            "<tr>" +
            "<td colSpan='4' align='center'>" +
            "抱歉!没有查询到您搜索的数据！" +
            "</td>" +
            "</tr>"
        );
        return;
    }

    //使用pageInfo的list属性填充tbody
    for (var i = 0; i < pageInfo.list.length; i++) {
        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;

        var numberTd = "<td>" + (i + 1) + "</td>";
        var checkboxTd = "<td><input id='" + roleId + "' class='itemBox' type='checkbox'></td>";
        var roleNameTd = "<td>" + roleName + "</td>"

        var checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class=' glyphicon glyphicon-check'></i></button>";
        //通过button标签的id属性（别的属性也可以）把roleId值传递到button按钮的单击
        //响应函数中，在单击响应函数中使用this.id
        var pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class=' glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='" + roleId + "' type='button' class='btn btn-danger btn-xs removeBtn'><i class=' glyphicon glyphicon-remove'></i></button>";
        var buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";

        var tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";
        $("#rolePageBody").append(tr);
    }

    //生成页面导航条
    generateNavigator(pageInfo);
}

function generateNavigator(pageInfo) {
    //获取总记录数
    var totalRecord = pageInfo.total;
    //声明相关属性
    var properties = {
        num_edge_entries: 3,
        num_display_entries: 5,
        callback: paginationCallBack, //用户点击“翻页”按钮之后执行翻页操作的回调函数
        current_page: pageInfo.pageNum - 1, //当前页，pageNum从1开始，必须-1后才可以赋值
        items_per_page: pageInfo.pageSize,
        prev_text: "上一页",
        next_text: "下一页"
    };

    //调用pagination()函数
    $("#Pagination").pagination(totalRecord, properties);
}

function paginationCallBack(pageIndex, jQuery) {
    //修改window对象的pageNum属性
    window.pageNum = pageIndex + 1;
    //调用分页函数
    generatePage();
    //取消页码超链接的默认行为
    return false;
}

//生成专门的函数显示确认模态框
function showConfirmModal(roleArray) {
    //打开模态框
    $("#confirmModal").modal("show");
    //清除旧的数据
    $("#roleNameDiv").empty();
    //在全局变量范围创建数组用来存放角色id
    window.roleIdArray = [];
    //遍历roleArray数组
    for (var i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br/>");
        var roleId = role.roleId;
        //调用数组对象的push()方法存入新元素
        window.roleIdArray.push(roleId);
    }
}

//声明专门的函数用来在分配Auth的模态框中显示Auth的树形结构
function fillAuthTree() {
    //1.发送Ajax请求查询Auth数据
    var ajaxResult = $.ajax({
        "url": "assgin/get/all/auth.json",
        "type": "post",
        "dataType": "json",
        "async": false
    });
    if (ajaxResult.status != 200) {
        layer.msg("请求处理出错！响应状态嘛是： " + ajaxResult.status + " 说明是：" + ajaxResult.statusText);
        return;
    }

    //2.从响应结果中获取Auth的JSON数据
    //从服务器端查询到的list不需要组装成树形结构，交给zTree去组装
    var authList = ajaxResult.responseJSON.data;
    //3.准备对zTree进行设置的JSON对象
    var setting = {
        "data": {
            "simpleData": {
                //开启简单JSON功能
                "enable": true,
                //使用categoryId属性关联父节点，不用默认的pId了
                "pIdKey": "categoryId"
            },
            "key": {
                //使用title属性显示节点名称，不用默认的name作为属性名了
                "name": "title"
            }
        },
        "check": {
            "enable": true
        }
    }
    //4.生成树形结构
    //<ul id="authTreeDemo" class="zTree"></ul>
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);

    //获取zTreeObj对象
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    //调用zTreeObj对象的方法，把节点展开
    zTreeObj.expandAll(true);

    //5.查询已分配的Auth的id组成的数组
    ajaxResult = $.ajax({
        "url": "assign/get/assigned/auth/id/by/role/id.json",
        "type": "post",
        "data": {
            "roleId": window.roleId
        },
        "dataType": "json",
        "async": false
    });
    if (ajaxResult.status != 200) {
        layer.msg("请求处理出错！响应状态码是： " + ajaxResult.status + "说明是：" + ajaxResult.statusText);
        return;
    }

    //从响应结果中获取authIdArray
    var authIdArray = ajaxResult.responseJSON.data
    //6.根据authIdArray把树形结构中对应的节点勾选上
    //①遍历authIdArray
    for (var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
        //②根据id查询树形结构中对应的
        var treeNode = zTreeObj.getNodeByParam("id", authId);
        //checked 设置为true表示节点勾选
        var checked = true;
        //checkTypeFlag 设置为false，表示“不联动”，不联动是为了避免把不该勾选的勾选上
        var checkTypeFlag = false;
        //执行
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }

}