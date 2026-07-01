<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Reservations</h1>
    <p>Handle booking history, check-in, check-out, and cancellations.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-calendar"></i></span>
        <div>
          <p class="eyebrow">Booking Management</p>
          <h1 class="h3 mb-1">Reservation List</h1>
          <p class="muted-copy">Monitor every reservation and update its current status.</p>
        </div>
      </div>
      <c:if test="${sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN'}">
        <a href="${pageContext.request.contextPath}/booking/search" class="boxed-btn3 small">Create Booking</a>
      </c:if>
    </div>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-list-alt"></i><span>Reservations</span></h2>
          <p class="muted-copy">Use the action buttons to process each booking.</p>
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
                <c:if test="${(sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN') && b.status == 'BOOKED'}">
                <form class="action-form" method="post" action="${pageContext.request.contextPath}/bookings/check-in">
                  <input type="hidden" name="id" value="${b.id}">
                  <button class="boxed-btn3 small" type="submit">Check-in</button>
                </form>
                </c:if>
                <c:if test="${(sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN') && b.status == 'CHECK_IN'}">
                <form class="action-form" method="post" action="${pageContext.request.contextPath}/bookings/check-out">
                  <input type="hidden" name="id" value="${b.id}">
                  <button class="boxed-btn3 small" type="submit">Check-out</button>
                </form>
                </c:if>
                <c:if test="${(sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN') && b.status == 'BOOKED'}">
                <form class="action-form" method="post" action="${pageContext.request.contextPath}/bookings/cancel">
                  <input type="hidden" name="id" value="${b.id}">
                  <button class="boxed-btn3 small" type="submit">Cancel</button>
                </form>
                </c:if>
              </td>
            </tr>
          </c:forEach>
        </table>
      </div>
    </section>

    <c:if test="${sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN'}">
    <section class="panel">
      <div class="panel-header"><div><h2 class="section-title"><i class="fa fa-edit"></i><span>Update Booking</span></h2><p class="muted-copy">Update booking date and time by entering the booking ID.</p></div></div>
      <form method="post" action="${pageContext.request.contextPath}/bookings/update" class="hotel-form">
        <div class="row">
          <div class="col-md-4"><label>Booking ID</label><input type="number" name="id" required></div>
          <div class="col-md-4"><label>Check-in Date Time</label><input type="datetime-local" name="checkIn" required></div>
          <div class="col-md-4"><label>Check-out Date Time</label><input type="datetime-local" name="checkOut" required></div>
          <div class="col-md-12"><button class="boxed-btn3 small" type="submit">Update Booking</button></div>
        </div>
      </form>
    </section>
    </c:if>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
