<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Invoice</h1><p>View invoice details.</p></div></div>
<div class="hotel-section"><div class="container"><section class="panel">
  <h2 class="section-title"><i class="fa fa-file-text-o"></i><span>Invoice #${invoice.id}</span></h2>
  <div class="table-responsive"><table class="hotel-table">
    <tr><th>Booking</th><td>#${invoice.bookingId}</td></tr>
    <tr><th>Customer</th><td>${invoice.customerName}</td></tr>
    <tr><th>Room</th><td>${invoice.roomNumber} - ${invoice.roomType}</td></tr>
    <tr><th>Check-in</th><td>${invoice.checkInAt}</td></tr>
    <tr><th>Check-out</th><td>${invoice.checkOutAt}</td></tr>
    <tr><th>Billable Days</th><td>${invoice.nights}</td></tr>
    <tr><th>Daily Price</th><td><fmt:formatNumber value="${invoice.roomPricePerNight}" type="currency" currencySymbol="VND "/></td></tr>
    <tr><th>Amount</th><td><fmt:formatNumber value="${invoice.amount}" type="currency" currencySymbol="VND "/></td></tr>
    <tr><th>Payment Method</th><td>${invoice.paymentMethod}</td></tr>
    <tr><th>Status</th><td><span class="badge-status">${invoice.paymentStatus}</span></td></tr>
    <tr><th>Issued Date</th><td>${invoice.issuedAt}</td></tr>
  </table></div>
  <c:choose>
    <c:when test="${sessionScope.currentUser.role == 'CUSTOMER'}">
      <a class="boxed-btn3 small mt-3" href="${pageContext.request.contextPath}/profile">Back To Profile</a>
    </c:when>
    <c:otherwise>
      <a class="boxed-btn3 small mt-3" href="${pageContext.request.contextPath}/invoices">Back To Invoices</a>
    </c:otherwise>
  </c:choose>
</section></div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
