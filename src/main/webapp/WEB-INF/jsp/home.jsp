<%@include file="head/headerPrefixAndLanguage.jsp"%>
<%@include file="head/head.jsp"%>

<body>

<%--<%@include file="head/headerImg.jsp"%>--%>
<%@include file="head/navbar.jsp"%>

<div class="container-fluid" style="margin-top:10px">
    <div class="row">
        <div class="col-sm-2">
            <form class="sticky rounded-lg shadow p-4 mb-4"
            style="height:700px; height: 350px; background-color: rgba(96, 162, 218, 0.2);">
                <p><fmt:message key="filters"/></p>
            </form>
        </div>

        <div class="col-sm-8">
            <div class="btn-group" style="width: 100%">
                <button type="button" class="btn btn-primary btn-my-r"><h5><fmt:message key="type_room_1"/></h5></button>
                <button type="button" class="btn btn-primary btn-my-r"><h5><fmt:message key="type_room_2"/></h5></button>
                <button type="button" class="btn btn-primary btn-my-r"><h5><fmt:message key="type_room_3"/></h5></button>
                <button type="button" class="btn btn-primary btn-my-r"><h5><fmt:message key="type_room_4"/></h5></button>
            </div>


            <div>
                <table class="table">
                    <thead class="thead-light">
                        <tr>
                            <th scope="col"><fmt:message key="n"/></th>
                            <th scope="col"><fmt:message key="number"/></th>
                            <th scope="col"><fmt:message key="roomCategory"/></th>
                            <%--<th scope="col"><fmt:message key="roomStatus"/></th>--%>
                            <th scope="col"><fmt:message key="guests"/></th>
                            <th scope="col"><fmt:message key="description"/></th>
                            <th scope="col"><fmt:message key="price"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="room" items="${rooms}" varStatus="position" begin="${10 * (pg - 1)}" end="${10 * (pg - 1) + 9}">
                            <tr>
                                <td scope="row">
                                    <c:out value="${10 * (pg - 1) + position.count}" />
                                </td>
                                <td><c:out value="${room.number}" /></td>
                                <td><c:out value="${room.roomCategory}" /></td>
                                <%--<td><c:out value="${room.roomStatus}" /></td>--%>
                                <td><c:out value="${room.guests}" /></td>
                                <td><c:out value="${room.description}" /></td>
                                <td><c:out value="${room.price}" /></td>
                                <%--<td><c:out value="${room.dateReceiving}" /></td>--%>
                                <td><a href='<c:url value="/order?action=select&id=${room.number}&pg=${pg}" />'><fmt:message key="act_1"/></a></td>
                                <td><a href='<c:url value="/order?action=booking&id=${room.number}&pg=${pg}" />'><fmt:message key="act_2"/></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <ul class="pagination">
                    <li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>
                    <li class="page-item ${pg == 1 ? 'active' : ''}"><a class="page-link" href="/home?pg=1">1</a></li>
                    <li class="page-item ${pg == 2 ? 'active' : ''}"><a class="page-link" href="/home?pg=2">2</a></li>
                    <li class="page-item ${pg == 3 ? 'active' : ''}"><a class="page-link" href="/home?pg=3">3</a></li>
                    <li class="page-item ${pg == 4 ? 'active' : ''}"><a class="page-link" href="/home?pg=4">Next</a></li>
                </ul>
            </div>
        </div>


        <div class="col-sm-2">
            <form  class="sticky rounded-lg shadow p-4 mb-4" method="post" action="/proposal"
                            style="background-color: rgba(96, 162, 218, 0.2);">
                <h3><fmt:message key="cart_1"/></h3>
                <div class="mb-3">
                    <label for="date1" class="form-label">Date arrival:</label>
                    <input type="date" class="form-control" id="date1"  name="dateStart" />
                    <%--<input type="datetime" class="form-control" id="date1" name="dateStart" />--%>
                </div>
                <div class="mb-3">
                    <label for="date2" class="form-label">Date arrival:</label>
                    <input type="date" class="form-control" id="date2" name="dateEnd" />
                </div>
                <div class="mb-3">
                    <label for="field1" class="form-label"> Guests </label>
                    <select id="field1" name="field1" class="col-md-12 form-control">
                        <%--<optgroup>--%>
                            <option value="1"> 1 </option>
                            <option value="2"> 2 </option>
                            <option value="3"> 3 </option>
                            <option value="4"> 4 </option>
                        <%--</optgroup>--%>
                    </select>
                    <%--<input type="number" class="form-control" id="field1" name="field1">--%>
                </div>
                <div class="mb-3">
                    <label for="field2" class="form-label"> Room category </label>
                    <select id="field2" name="field2" class="col-md-12 form-control">
                        <%--<optgroup>--%>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category}"> ${category} </option>
                            </c:forEach>
                        <%--</optgroup>--%>
                    </select>
                    <%--<input type="o" class="form-control" id="field2" name="field2">--%>
                </div>
                <button type="submit" class="btn btn-outline-success">  <fmt:message key="cart_2"/>  </button>

            </form>
<%--            <form class="sticky rounded-lg shadow p-4 mb-4" style="height: 250px; background-color: rgba(96, 162, 218, 0.2);">

                <p> Date arrival: ${order.dateStart} </p>
                <p> Date departure: ${order.dateEnd} </p>
                <p> Number of guest: ${order.room.guests} </p>
                <p><c:out value="${not empty id ? id : '-'}"></c:out>
            </form>--%>
        </div>
    </div>
</div>

<%--<div class="jumbotron text-center" style="margin-bottom:0">
  <p>Footer</p>
</div>--%>

</body>
</html>