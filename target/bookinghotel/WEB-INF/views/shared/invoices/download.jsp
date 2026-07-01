<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Download Invoice</h1><p>Invoice information prepared for saving.</p></div></div>
<div class="hotel-section"><div class="container"><section class="panel">
  <h2 class="section-title"><i class="fa fa-download"></i><span>Invoice #${invoice.id}</span></h2>
  <textarea class="form-control" rows="8" readonly>Invoice #${invoice.id}
Booking: #${invoice.bookingId}
Customer: ${invoice.customerName}
Room: ${invoice.roomNumber} - ${invoice.roomType}
Check-in: ${invoice.checkInAt}
Check-out: ${invoice.checkOutAt}
Billable Days: ${invoice.nights}
Daily Price: ${invoice.roomPricePerNight}
Amount: ${invoice.amount}
Method: ${invoice.paymentMethod}
Status: ${invoice.paymentStatus}
Issued: ${invoice.issuedAt}</textarea>
  <p class="muted-copy mt-3">Use this page as the invoice download view required by the project.</p>
</section></div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
