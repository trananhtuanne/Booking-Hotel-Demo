<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Payment History</h1><p>Review direct counter payments recorded by Receptionist.</p></div></div>
<div class="hotel-section"><div class="container"><section class="panel">
  <h2 class="section-title"><i class="fa fa-credit-card"></i><span>Payments</span></h2>
  <div class="table-responsive"><table class="hotel-table"><tr><th>ID</th><th>Invoice</th><th>Customer</th><th>Amount</th><th>Method</th><th>Status</th><th>Paid At</th></tr>
    <c:forEach var="p" items="${payments}"><tr><td>#${p.id}</td><td>#${p.invoiceId}</td><td>${p.customerName}</td><td><fmt:formatNumber value="${p.amount}" type="currency" currencySymbol="VND "/></td><td>${p.method}</td><td><span class="badge-status">${p.status}</span></td><td>${p.paidAt}</td></tr></c:forEach>
  </table></div>
</section></div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
