<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Booking Details</h1><p>View full reservation information.</p></div></div>
<div class="hotel-section"><div class="container"><section class="panel">
  <h2 class="section-title"><i class="fa fa-calendar"></i><span>Reservation #${booking.id}</span></h2>
  <div class="table-responsive"><table class="hotel-table">
    <tr><th>Customer</th><td>${booking.customerName}</td></tr>
    <tr><th>Room</th><td>${booking.roomNumber} - ${booking.roomType}</td></tr>
    <tr><th>Check-in</th><td>${booking.checkInAt}</td></tr>
    <tr><th>Check-out</th><td>${booking.checkOutAt}</td></tr>
    <tr><th>Total Amount</th><td><fmt:formatNumber value="${booking.totalAmount}" type="currency" currencySymbol="VND "/></td></tr>
    <tr><th>Status</th><td><span class="badge-status">${booking.status}</span></td></tr>
  </table></div>
  <c:choose>
    <c:when test="${sessionScope.currentUser.role == 'CUSTOMER'}">
      <a class="boxed-btn3 small mt-3" href="${pageContext.request.contextPath}/my-bookings">Back To My Bookings</a>
    </c:when>
    <c:otherwise>
      <a class="boxed-btn3 small mt-3" href="${pageContext.request.contextPath}/bookings">Back To Reservations</a>
    </c:otherwise>
  </c:choose>
</section></div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
