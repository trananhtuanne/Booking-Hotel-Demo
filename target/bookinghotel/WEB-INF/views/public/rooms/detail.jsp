<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Room Details</h1><p>View room information.</p></div></div>
<div class="hotel-section"><div class="container"><section class="panel">
  <div class="row">
    <div class="col-md-5"><img class="w-100" src="${pageContext.request.contextPath}${room.imageUrl}" alt="${room.roomNumber}"></div>
    <div class="col-md-7">
      <h2 class="section-title"><i class="fa fa-bed"></i><span>Room ${room.roomNumber}</span></h2>
      <p><strong>Room Type:</strong> ${room.typeName}</p>
      <p><strong>Price:</strong> <fmt:formatNumber value="${room.pricePerNight}" type="currency" currencySymbol="VND "/> per billable day</p>
      <p><strong>Status:</strong> <span class="badge-status">${room.status}</span></p>
      <p><strong>Description:</strong> ${room.description}</p>
      <a class="boxed-btn3 small" href="${pageContext.request.contextPath}/rooms">Back To Rooms</a>
      <c:if test="${not empty sessionScope.currentUser && (sessionScope.currentUser.role == 'STAFF' || sessionScope.currentUser.role == 'ADMIN')}">
        <a class="boxed-btn3 small" href="${pageContext.request.contextPath}/rooms/edit?id=${room.id}">Edit Room</a>
      </c:if>
    </div>
  </div>
</section></div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
