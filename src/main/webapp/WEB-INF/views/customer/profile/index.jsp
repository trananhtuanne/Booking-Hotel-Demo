<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Customer Profile</h1><p>View profile, update information, booking history, and invoices.</p></div></div>
<div class="hotel-section"><div class="container">
  <section class="panel">
    <h2 class="section-title"><i class="fa fa-user"></i><span>Profile Information</span></h2>
    <form method="post" class="hotel-form">
      <input type="hidden" name="id" value="${customer.id}">
      <input type="hidden" name="userId" value="${customer.userId}">
      <div class="row">
        <div class="col-md-4"><label>Full Name</label><input name="fullName" value="${customer.fullName}" required></div>
        <div class="col-md-4"><label>Email</label><input name="email" value="${customer.email}"></div>
        <div class="col-md-4"><label>Phone</label><input name="phone" value="${customer.phone}"></div>
        <div class="col-md-4"><label>Identity Number</label><input name="identityNumber" value="${customer.identityNumber}"></div>
        <div class="col-md-8"><label>Address</label><input name="address" value="${customer.address}"></div>
        <div class="col-md-12"><button class="boxed-btn3 small" type="submit">Update Profile</button></div>
      </div>
    </form>
  </section>
  <section class="panel"><h2 class="section-title"><i class="fa fa-history"></i><span>Booking History</span></h2><div class="table-responsive"><table class="hotel-table"><tr><th>ID</th><th>Room</th><th>Check-in</th><th>Check-out</th><th>Status</th></tr><c:forEach var="b" items="${bookings}"><tr><td>#${b.id}</td><td>${b.roomNumber} - ${b.roomType}</td><td>${b.checkInAt}</td><td>${b.checkOutAt}</td><td><span class="badge-status">${b.status}</span></td></tr></c:forEach></table></div></section>
  <section class="panel"><h2 class="section-title"><i class="fa fa-file-text-o"></i><span>Invoices</span></h2><div class="table-responsive"><table class="hotel-table"><tr><th>ID</th><th>Booking</th><th>Amount</th><th>Status</th></tr><c:forEach var="i" items="${invoices}"><tr><td>#${i.id}</td><td>#${i.bookingId}</td><td><fmt:formatNumber value="${i.amount}" type="currency" currencySymbol="VND "/></td><td><span class="badge-status">${i.paymentStatus}</span></td></tr></c:forEach></table></div></section>
</div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
