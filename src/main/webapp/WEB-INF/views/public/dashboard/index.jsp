<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="slider_area">
  <div class="slider_active owl-carousel">
    <div class="single_slider d-flex align-items-center justify-content-center slider_bg_1">
      <div class="container">
        <div class="row">
          <div class="col-xl-12">
            <div class="slider_text text-center">
              <h3>Hotel Booking</h3>
              <p>Manage rooms, reservations, invoices, and reports.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="single_slider d-flex align-items-center justify-content-center slider_bg_2">
      <div class="container">
        <div class="row">
          <div class="col-xl-12">
            <div class="slider_text text-center">
              <h3>Hotel Management</h3>
              <p>Search available rooms and process bookings quickly.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-dashboard"></i></span>
        <div>
          <p class="eyebrow">System Overview</p>
          <h1 class="h3 mb-1">Hotel Management Dashboard</h1>
          <p class="muted-copy">Track room availability, active bookings, and monthly revenue.</p>
        </div>
      </div>
      <a href="${pageContext.request.contextPath}/booking/search" class="boxed-btn3 small">New Booking</a>
    </div>

    <div class="row">
      <div class="col-xl-3 col-md-6">
        <div class="hotel-card text-center">
          <h3>Total Rooms</h3>
          <span class="stats-number">${stats.totalRooms}</span>
        </div>
      </div>
      <div class="col-xl-3 col-md-6">
        <div class="hotel-card text-center">
          <h3>Available Rooms</h3>
          <span class="stats-number">${stats.availableRooms}</span>
        </div>
      </div>
      <div class="col-xl-3 col-md-6">
        <div class="hotel-card text-center">
          <h3>Active Bookings</h3>
          <span class="stats-number">${stats.activeBookings}</span>
        </div>
      </div>
      <div class="col-xl-3 col-md-6">
        <div class="hotel-card text-center">
          <h3>Monthly Revenue</h3>
          <span class="stats-number"><fmt:formatNumber value="${stats.monthlyRevenue}" type="currency" currencySymbol="VND "/></span>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-xl-4 col-md-4">
        <div class="single_offers">
          <div class="about_thumb"><img src="${pageContext.request.contextPath}/assets/img/offers/1.png" alt="Room booking"></div>
          <h3>Search And Book Rooms</h3>
          <ul>
            <li>Search rooms by check-in and check-out date.</li>
            <li>Filter available rooms by room type.</li>
            <li>Create online reservations.</li>
          </ul>
          <a href="${pageContext.request.contextPath}/booking/search" class="book_now">Book Now</a>
        </div>
      </div>
      <div class="col-xl-4 col-md-4">
        <div class="single_offers">
          <div class="about_thumb"><img src="${pageContext.request.contextPath}/assets/img/offers/2.png" alt="Hotel operation"></div>
          <h3>Manage Hotel Operations</h3>
          <ul>
            <li>Manage rooms and room types.</li>
            <li>Manage customers and reservations.</li>
            <li>Support check-in and check-out.</li>
          </ul>
          <a href="${pageContext.request.contextPath}/rooms" class="book_now">Manage Rooms</a>
        </div>
      </div>
      <div class="col-xl-4 col-md-4">
        <div class="single_offers">
          <div class="about_thumb"><img src="${pageContext.request.contextPath}/assets/img/offers/3.png" alt="Invoices and reports"></div>
          <h3>Invoices And Reports</h3>
          <ul>
            <li>Generate invoices and process payments.</li>
            <li>View revenue statistics.</li>
            <li>Monitor room occupancy.</li>
          </ul>
          <a href="${pageContext.request.contextPath}/reports" class="book_now">View Reports</a>
        </div>
      </div>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
