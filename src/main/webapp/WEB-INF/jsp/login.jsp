<%@include file="head/headerPrefixAndLanguage.jsp"%>

<%@include file="head/head.jsp"%>

<body>


<div class="center1">
    <form class="inner_logo" method="post" action="login"> <%--action="controller"--%>
        <%--<input type="hidden" name="command" value="login" />--%>
        <div class="mb-3">
            <label for="login1" class="form-label"> Login </label>
            <input type="text" class="form-control" id="login1" name="login" value="${oldLogin != null ? oldLogin :''}">
            <small class="text-danger">
                <c:if test="${not empty wrongLogin}"> <fmt:message key="wrongLogin"/> </c:if>
            </small>
        </div>
        <div class="mb-3">
            <label for="passwordField" class="form-label">  <fmt:message key="password"/>  </label>
            <input type="password" class="form-control" id="passwordField" name="log_pass">
            <small class="text-danger">
                <c:if test="${not empty wrongPass}"> <fmt:message key="wrongPass"/> </c:if>
            </small>
        </div>
        <button type="submit" class="btn btn-outline-success">  <fmt:message key="loginNow"/>  </button>
        <button type="button" class="btn btn-outline-danger" onclick="window.location='${not empty backUrl ? backUrl : '/'}'" >
            <fmt:message key="cancel"/>
        </button>
        <div style="margin-top: 30px; font-size: 1.3em">
            <a href='<c:url value="/registration" />'> <fmt:message key="registration"/> </a>
        </div>
    </form>
</div>
</body>
</html>