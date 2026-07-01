<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Confirm Booking</h1>
    <p>Review guest information before payment.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-user-o"></i></span>
        <div>
          <p class="eyebrow">Step 1</p>
          <h1 class="h3 mb-1">Guest Information</h1>
          <p class="muted-copy">The room will only be marked as booked after payment is completed.</p>
        </div>
      </div>
    </div>

    <section class="panel">
      <div class="row">
        <div class="col-md-5">
          <div class="single_offers">
            <div class="about_thumb">
              <img src="${pageContext.request.contextPath}${room.imageUrl}" alt="${room.roomNumber}">
            </div>
            <h3>Room ${room.roomNumber} - ${room.typeName}</h3>
            <ul>
              <li>Check-in: ${checkIn}</li>
              <li>Check-out: ${checkOut}</li>
              <li>Billable Days: ${billableDays}</li>
              <li>Price: <fmt:formatNumber value="${room.pricePerNight}" type="currency" currencySymbol="VND "/> per billable day</li>
              <li>Total: <strong><fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="VND "/></strong></li>
            </ul>
          </div>
        </div>

        <div class="col-md-7">
          <h2 class="section-title"><i class="fa fa-address-card-o"></i><span>Confirm Details</span></h2>
          <c:if test="${not empty dateError}">
            <div class="alert alert-danger" role="alert">${dateError}</div>
          </c:if>
          <c:if test="${not empty roomBookings}">
            <div class="alert alert-info" role="alert">
              <strong>Booked date/time ranges for this room:</strong>
              <ul class="mb-0">
                <c:forEach var="b" items="${roomBookings}">
                  <li>${b.checkInAt} to ${b.checkOutAt} (${b.status})</li>
                </c:forEach>
              </ul>
            </div>
          </c:if>
          <form method="post" action="${pageContext.request.contextPath}/booking/payment" class="hotel-form">
            <input type="hidden" name="roomId" value="${room.id}">
            <input type="hidden" name="customerId" value="${customer.id}">

            <div class="row">
              <div class="col-md-6">
                <label>Check-in Date Time</label>
                <input type="datetime-local" name="checkIn" value="${checkIn}" required>
              </div>
              <div class="col-md-6">
                <label>Check-out Date Time</label>
                <input type="datetime-local" name="checkOut" value="${checkOut}" required>
              </div>
              <div class="col-md-12">
                <label>Full Name</label>
                <input type="text" name="fullName" value="${customer.fullName}" required>
              </div>
              <div class="col-md-6">
                <label>Email</label>
                <input type="email" name="email" value="${customer.email}" required>
              </div>
              <div class="col-md-6">
                <label>Phone</label>
                <input type="text" name="phone" value="${customer.phone}" required>
              </div>
              <div class="col-md-12">
                <div class="booking-step-actions">
                  <button class="boxed-btn3" type="submit">Continue To Payment</button>
                  <a class="boxed-btn3" href="${pageContext.request.contextPath}/booking/search?checkIn=${checkIn}&checkOut=${checkOut}">Back</a>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </section>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
