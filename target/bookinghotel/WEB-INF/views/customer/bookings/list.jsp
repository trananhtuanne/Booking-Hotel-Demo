<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>My Bookings</h1>
    <p>View your booking history and cancel pending bookings.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-calendar"></i></span>
        <div>
          <p class="eyebrow">Customer Booking</p>
          <h1 class="h3 mb-1">Booking History</h1>
          <p class="muted-copy">Only your own reservations are shown here.</p>
        </div>
      </div>
      <a href="${pageContext.request.contextPath}/booking/search" class="boxed-btn3 small">Create Booking</a>
    </div>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-list-alt"></i><span>Reservations</span></h2>
          <p class="muted-copy">Bookings move through Booked, Check-in, and Check-out at the hotel counter.</p>
        </div>
      </div>
      <div class="table-responsive">
        <table class="hotel-table">
          <tr>
            <th>ID</th>
            <th>Customer</th>
            <th>Room</th>
            <th>Check-in</th>
            <th>Check-out</th>
            <th>Total Amount</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
          <c:forEach var="b" items="${bookings}">
            <tr>
              <td>#${b.id}</td>
              <td>${b.customerName}</td>
              <td>${b.roomNumber} - ${b.roomType}</td>
              <td>${b.checkInAt}</td>
              <td>${b.checkOutAt}</td>
              <td><fmt:formatNumber value="${b.totalAmount}" type="currency" currencySymbol="VND "/></td>
              <td><span class="badge-status">${b.status}</span></td>
              <td>
                <a class="boxed-btn3 small" href="${pageContext.request.contextPath}/bookings/detail?id=${b.id}">Details</a>
                <c:if test="${b.status == 'BOOKED'}">
                <form class="action-form" method="post" action="${pageContext.request.contextPath}/bookings/check-in">
                  <input type="hidden" name="id" value="${b.id}">
                  <button class="boxed-btn3 small" type="submit">Check In</button>
                </form>
                <form class="action-form" method="post" action="${pageContext.request.contextPath}/bookings/cancel">
                  <input type="hidden" name="id" value="${b.id}">
                  <button class="boxed-btn3 small" type="submit">Cancel</button>
                </form>
                </c:if>
                <c:if test="${b.status == 'CHECK_IN'}">
                <form class="action-form" method="post" action="${pageContext.request.contextPath}/bookings/check-out">
                  <input type="hidden" name="id" value="${b.id}">
                  <button class="boxed-btn3 small" type="submit">Check Out</button>
                </form>
                </c:if>
              </td>
            </tr>
          </c:forEach>
        </table>
      </div>
    </section>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
