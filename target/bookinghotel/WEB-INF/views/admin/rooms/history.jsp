<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Room Change History</h1>
    <p>Review old room information saved before each room update.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-history"></i></span>
        <div>
          <p class="eyebrow">Room Management</p>
          <h1 class="h3 mb-1">Room Information Audit</h1>
          <p class="muted-copy">Booking history uses these snapshots when the booking time is older than the room update time.</p>
        </div>
      </div>
      <a href="${pageContext.request.contextPath}/rooms" class="boxed-btn3 small">Back To Rooms</a>
    </div>

    <section class="panel">
      <div class="table-responsive">
        <table class="hotel-table">
          <tr>
            <th>ID</th>
            <th>Room ID</th>
            <th>Old ID</th>
            <th>Old Room</th>
            <th>Old Type</th>
            <th>Old Price</th>
            <th>Old Status</th>
            <th>Old Image</th>
            <th>Old Description</th>
            <th>Current Room</th>
            <th>Changed At</th>
          </tr>
          <c:forEach var="h" items="${history}">
            <tr>
              <td>#${h.id}</td>
              <td>${h.room_id}</td>
              <td>${h.old_room_id}</td>
              <td>${h.old_room_number}</td>
              <td>${h.old_type_name}</td>
              <td><fmt:formatNumber value="${h.old_price_per_night}" type="currency" currencySymbol="VND "/></td>
              <td><span class="badge-status">${h.old_status}</span></td>
              <td>
                <c:if test="${not empty h.old_image_url}">
                  <img src="${pageContext.request.contextPath}${h.old_image_url}" alt="${h.old_room_number}" style="width:90px;height:60px;object-fit:cover;">
                </c:if>
              </td>
              <td>${h.old_description}</td>
              <td>${h.current_room_number} - ${h.current_type_name}</td>
              <td>${h.changed_at}</td>
            </tr>
          </c:forEach>
        </table>
      </div>
    </section>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
