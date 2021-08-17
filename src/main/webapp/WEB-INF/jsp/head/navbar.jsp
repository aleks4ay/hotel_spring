<nav class="navbar navbar-expand-sm bg-dark navbar-dark sticky-top">
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="navbar-nav mr-auto" style="font-size: 1.5rem">
            <li class="nav-item">
                <a class="nav-link gap-item" href="/"><fmt:message key="home"/></a>
            </li>
            <c:if test="${empty sessionScope.get('user')}">
                <li class="nav-item">
                    <a class="nav-link gap-item" href="/user?action=room"><fmt:message key="booking"/></a>
                </li>
            </c:if>
            <c:if test="${sessionScope.get('user').client}">
                <li class="nav-item">
                    <a class="nav-link gap-item" href="/user?action=room"><fmt:message key="booking"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link gap-item" href="/user?action=account&ap=order"><fmt:message key="account"/></a>
                </li>
            </c:if>
            <c:if test="${sessionScope.get('user').admin}">
                <li class="nav-item">
                    <a class="nav-link gap-item" href="/admin"><fmt:message key="admin"/></a>
                </li>
            </c:if>
            <c:if test="${sessionScope.get('user').manager}">
                <li class="nav-item">
                    <a class="nav-link gap-item" href="/manager"><fmt:message key="manager"/></a>
                </li>
            </c:if>
        </ul>

        <ul class="navbar-nav" style="font-size: 1.2rem; align-items: flex-end">
            <li>
                <label style="color: rgba(60, 189, 234, 0.54)">
                    <c:if test="${not empty sessionScope.get('user')}">
                        '${sessionScope.get('user').name}' (${sessionScope.get('user').role.title})
                    </c:if>
                    <c:if test="${empty sessionScope.get('user')}">
                        <fmt:message key="notEntered"/>
                    </c:if>
                </label>
            </li>
            <li>
                &nbsp;&nbsp;&nbsp;
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/lang?language=ru"/>" <c:if test="${language=='ru'}">style="color: #d54d38"</c:if> >RU</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/lang?language=en"/>" <c:if test="${language=='en'}">style="color: #d54d38"</c:if> >EN</a>
            </li>
            <li>
                &nbsp;
            </li>

            <c:if test="${not empty sessionScope.get('user')}">
                <li class="nav-item">
                    <a class="nav-link" href="logout"><fmt:message key="logoutNow"/></a>
                </li>
            </c:if>

            <c:if test="${empty sessionScope.get('user')}">
                <li class="nav-item">
                    <a class="nav-link" href="login"><fmt:message key="loginNow"/></a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="registration"><fmt:message key="registration"/></a>
                </li>
            </c:if>
        </ul>
    </div>
</nav>

<%@include file="role.jsp"%>

