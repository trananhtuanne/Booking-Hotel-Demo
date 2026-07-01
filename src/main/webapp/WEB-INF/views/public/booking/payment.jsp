<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Payment</h1>
    <p>Complete payment to create the reservation.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-credit-card"></i></span>
        <div>
          <p class="eyebrow">Step 2</p>
          <h1 class="h3 mb-1">Payment Confirmation</h1>
          <p class="muted-copy">The room status changes to BOOKED only after this payment is recorded.</p>
        </div>
      </div>
    </div>

    <section class="panel">
      <div class="row">
        <div class="col-md-6">
          <h2 class="section-title"><i class="fa fa-bed"></i><span>Booking Summary</span></h2>
          <div class="table-responsive">
            <table class="hotel-table">
              <tr><th>Guest</th><td>${customer.fullName}</td></tr>
              <tr><th>Email</th><td>${customer.email}</td></tr>
              <tr><th>Phone</th><td>${customer.phone}</td></tr>
              <tr><th>Room</th><td>${room.roomNumber} - ${room.typeName}</td></tr>
              <tr><th>Check-in</th><td>${checkIn}</td></tr>
              <tr><th>Check-out</th><td>${checkOut}</td></tr>
              <tr><th>Billable Days</th><td>${billableDays}</td></tr>
              <tr><th>Total</th><td><strong><fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="VND "/></strong></td></tr>
            </table>
          </div>
        </div>

        <div class="col-md-6">
          <h2 class="section-title"><i class="fa fa-money"></i><span>Payment Method</span></h2>
          <form method="post" action="${pageContext.request.contextPath}/booking/complete" class="hotel-form">
            <input type="hidden" name="roomId" value="${room.id}">
            <input type="hidden" name="customerId" value="${customer.id}">
            <input type="hidden" name="fullName" value="${customer.fullName}">
            <input type="hidden" name="email" value="${customer.email}">
            <input type="hidden" name="phone" value="${customer.phone}">
            <input type="hidden" name="checkIn" value="${checkIn}">
            <input type="hidden" name="checkOut" value="${checkOut}">

            <label>Method</label>
            <select name="method">
              <option value="CASH">Cash</option>
              <option value="ONLINE">Online</option>
              <option value="QR">QR Code</option>
            </select>

            <label>Amount</label>
            <input type="text" value="${totalAmount}" readonly>

            <div class="booking-step-actions">
              <button class="boxed-btn3" type="submit">Pay And Book</button>
              <a class="boxed-btn3" href="${pageContext.request.contextPath}/booking/search?checkIn=${checkIn}&checkOut=${checkOut}">Back</a>
            </div>
          </form>
        </div>
      </div>
    </section>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
