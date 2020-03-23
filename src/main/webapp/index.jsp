<%--
  Created by IntelliJ IDEA.
  User: yqy
  Date: 2020/3/19
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery/jquery-2.1.1.min.js"></script>
</head>
<body>
欢迎参于秒杀
<br/>
苹果11PRO
<form action="${pageContext.request.contextPath}/doSecondKill" method="post">
    <input type="hidden" name="prodid" value="10001"/>
    <button id="secKillbtn" type="button">点我秒杀</button>
</form>

<script type="text/javascript">
    $("#secKillbtn").click(function () {

        $.ajax({
           url:"${pageContext.request.contextPath}/doSecondKill" ,
            type:"post",
            data:$("form").serialize(),
            success:function (result) {
                if(result == "seccuess"){
                    alert("秒杀成功");
                }else if(result == "10000"){
                    alert("用户已参与过秒杀");
                }else if(result == "20000"){
                    alert("还没开始秒杀活动");
                }else if(result == "30000"){
                    alert("库存不足");
                }
            }
        });
    })

</script>

</body>
</html>
