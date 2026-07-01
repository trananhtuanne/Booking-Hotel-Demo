<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Invoices</h1>
    <p>Review invoices created after check-out and process direct payments at the counter.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-file-text-o"></i></span>
        <div>
          <p class="eyebrow">Invoice Management</p>
          <h1 class="h3 mb-1">Payment Processing</h1>
          <p class="muted-copy">Review issued invoices and update payment status.</p>
        </div>
      </div>
    </div>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-credit-card"></i><span>Invoice List</span></h2>
          <p class="muted-copy">Invoices are created when a checked-in booking is checked out.</p>
        </div>
      </div>
      <div class="table-responsive">
        <table class="hotel-table">
          <tr>
            <th>Invoice ID</th>
            <th>Booking</th>
            <th>Customer</th>
            <th>Room</th>
            <th>Billable Days</th>
            <th>Amount</th>
            <th>Method</th>
            <th>Status</th>
            <th>Issued Date</th>
            <th>Payment</th>
            <th>Documents</th>
          </tr>
          <c:forEach var="i" items="${invoices}">
            <tr>
              <td>#${i.id}</td>
              <td>#${i.bookingId}</td>
              <td>${i.customerName}</td>
              <td>${i.roomNumber} - ${i.roomType}</td>
              <td>${i.nights}</td>
              <td><fmt:formatNumber value="${i.amount}" type="currency" currencySymbol="VND "/></td>
              <td>${i.paymentMethod}</td>
              <td><span class="badge-status">${i.paymentStatus}</span></td>
              <td>${i.issuedAt}</td>
              <td>
                <c:if test="${(sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN') && i.paymentStatus != 'PAID'}">
                <form method="post" action="${pageContext.request.contextPath}/invoices/pay" class="hotel-form action-form">
                  <input type="hidden" name="id" value="${i.id}">
                  <select name="method">
                    <option>CASH</option>
                  </select>
                  <button class="boxed-btn3 small" type="submit">Pay</button>
                </form>
                </c:if>
              </td>
              <td>
                <a class="boxed-btn3 small" href="${pageContext.request.contextPath}/invoices/view?id=${i.id}">View</a>
                <c:if test="${sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN'}">
                <a class="boxed-btn3 small" href="${pageContext.request.contextPath}/invoices/print?id=${i.id}">Print</a>
                </c:if>
                <a class="boxed-btn3 small" href="${pageContext.request.contextPath}/invoices/download?id=${i.id}">Download</a>
              </td>
            </tr>
          </c:forEach>
        </table>
      </div>
    </section>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
