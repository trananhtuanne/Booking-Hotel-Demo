<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Reports</h1>
    <p>View revenue, occupancy, and customer booking statistics.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-bar-chart"></i></span>
        <div>
          <p class="eyebrow">Statistical Reports</p>
          <h1 class="h3 mb-1">Hotel Performance</h1>
          <p class="muted-copy">Summaries for revenue, room occupancy, and frequent customers.</p>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <section class="panel">
          <div class="panel-header">
            <div>
              <h2 class="section-title"><i class="fa fa-money"></i><span>Revenue Statistics</span></h2>
              <p class="muted-copy">Revenue by month, quarter, or year.</p>
            </div>
          </div>
          <div class="table-responsive">
            <table class="hotel-table">
              <tr>
                <th>Period</th>
                <th>Revenue</th>
              </tr>
              <c:forEach var="r" items="${revenue}">
                <tr>
                  <td>${r.label}</td>
                  <td><fmt:formatNumber value="${r.value}" type="currency" currencySymbol="VND "/></td>
                </tr>
              </c:forEach>
            </table>
          </div>
        </section>
      </div>
      <div class="col-md-6">
        <section class="panel">
          <div class="panel-header">
            <div>
              <h2 class="section-title"><i class="fa fa-pie-chart"></i><span>Occupancy Statistics</span></h2>
              <p class="muted-copy">Occupancy rate plus room count grouped by status.</p>
            </div>
          </div>
          <div class="table-responsive">
            <table class="hotel-table">
              <tr>
                <th>Status</th>
                <th>Room Count</th>
              </tr>
              <c:forEach var="o" items="${occupancy}">
                <tr>
                  <td>${o.label}</td>
                  <td>${o.value}</td>
                </tr>
              </c:forEach>
            </table>
          </div>
        </section>
      </div>
    </div>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-calendar-check-o"></i><span>Booking Statistics</span></h2>
          <p class="muted-copy">Booking count grouped by reservation status.</p>
        </div>
      </div>
      <div class="table-responsive">
        <table class="hotel-table">
          <tr>
            <th>Status</th>
            <th>Booking Count</th>
          </tr>
          <c:forEach var="b" items="${bookingStats}">
            <tr>
              <td>${b.label}</td>
              <td>${b.value}</td>
            </tr>
          </c:forEach>
        </table>
      </div>
    </section>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-star"></i><span>Top Customers</span></h2>
          <p class="muted-copy">Customers with the highest number of bookings.</p>
        </div>
      </div>
      <div class="table-responsive">
        <table class="hotel-table">
          <tr>
            <th>Customer</th>
            <th>Booking Count</th>
          </tr>
          <c:forEach var="c" items="${topCustomers}">
            <tr>
              <td>${c.label}</td>
              <td>${c.value}</td>
            </tr>
          </c:forEach>
        </table>
      </div>
    </section>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
