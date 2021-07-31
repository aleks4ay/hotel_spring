<nav class="navbar navbar-expand-sm bg-dark navbar-dark sticky-top">
    <div class="collapse navbar-collapse" id="collapsibleNavbar">
        <ul class="navbar-nav mr-auto" style="font-size: 1.5rem">
            <li class="nav-item">
                <a class="nav-link gap-item" href="home"><fmt:message key="home"/></a>
            </li>
            <li class="nav-item">
                <a class="nav-link gap-item" href="#"><fmt:message key="account"/></a>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="text" placeholder="Search">
            <button class="btn btn-success my-2 my-sm-0" type="button">Search</button>
        </form>
        <ul class="navbar-nav" style="font-size: 1.2rem; align-items: flex-end">
            <li>
                &nbsp;
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/lang?language=ru" <c:if test="${language=='ru'}">style="color: #d54d38"</c:if> >RU</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/lang?language=en" <c:if test="${language=='en'}">style="color: #d54d38"</c:if> >EN</a>
            </li>
            <li>
                &nbsp;
            </li>
            <%--<li class="nav-item">
                <a class="nav-link" href="newuser"><fmt:message key="registration"/></a>
            </li>--%>
            <li class="nav-item">
                <a class="nav-link" href="login"><fmt:message key="loginNow"/></a>
            </li>
        </ul>
    </div>
</nav>

