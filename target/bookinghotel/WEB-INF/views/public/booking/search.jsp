<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Book A Room</h1>
    <p>Search available rooms by date, time, and room type.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-calendar-check-o"></i></span>
        <div>
          <p class="eyebrow">Online Booking</p>
          <h1 class="h3 mb-1">Room Availability</h1>
          <p class="muted-copy">Choose a stay period and reserve an available room.</p>
        </div>
      </div>
    </div>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-search"></i><span>Search Available Rooms</span></h2>
          <p class="muted-copy">The system checks availability directly from the service data.</p>
        </div>
      </div>
      <form method="get" class="hotel-form">
        <div class="row">
          <div class="col-md-3">
            <label>Check-in Date Time</label>
            <input type="datetime-local" name="checkIn" value="${checkIn}" required>
          </div>
          <div class="col-md-3">
            <label>Check-out Date Time</label>
            <input type="datetime-local" name="checkOut" value="${checkOut}" required>
          </div>
          <div class="col-md-3">
            <label>Room Type</label>
            <select name="roomTypeId">
              <option value="">All Room Types</option>
              <c:forEach var="t" items="${roomTypes}">
                <option value="${t.id}">${t.name}</option>
              </c:forEach>
            </select>
          </div>
          <c:if test="${not empty customers}">
          <div class="col-md-3">
            <label>Walk-in Customer</label>
            <select name="customerId">
              <c:forEach var="c" items="${customers}">
                <option value="${c.id}">${c.fullName} - ${c.phone}</option>
              </c:forEach>
            </select>
          </div>
          </c:if>
          <div class="col-md-3">
            <label>&nbsp;</label>
            <button class="boxed-btn3" type="submit">Check Availability</button>
          </div>
        </div>
      </form>
    </section>

    <div class="row">
      <c:forEach var="r" items="${rooms}">
        <div class="col-xl-4 col-md-6">
          <div class="single_offers">
            <div class="about_thumb"><img src="${pageContext.request.contextPath}${r.imageUrl}" alt="${r.roomNumber}"></div>
            <h3>Room ${r.roomNumber} - ${r.typeName}</h3>
            <ul>
              <li><fmt:formatNumber value="${r.pricePerNight}" type="currency" currencySymbol="VND "/> per billable day</li>
              <li>${r.description}</li>
              <li>Status: <span class="badge-status">${r.status}</span></li>
            </ul>
            <form method="post" action="${pageContext.request.contextPath}/booking/confirm">
              <input type="hidden" name="roomId" value="${r.id}">
              <input type="hidden" name="checkIn" value="${checkIn}">
              <input type="hidden" name="checkOut" value="${checkOut}">
              <c:if test="${not empty customers}">
                <label>Customer</label>
                <select name="customerId">
                  <c:forEach var="c" items="${customers}">
                    <option value="${c.id}">${c.fullName} - ${c.phone}</option>
                  </c:forEach>
                </select>
              </c:if>
              <button class="book_now" type="submit">Book Now</button>
            </form>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
