<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Edit Room</h1>
    <p>Update room information, image, status, and ID.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-edit"></i></span>
        <div>
          <p class="eyebrow">Room Management</p>
          <h1 class="h3 mb-1">Room ${room.roomNumber} - ${room.typeName}</h1>
          <p class="muted-copy">Old room information will be saved to room change history before this update is applied.</p>
        </div>
      </div>
      <a href="${pageContext.request.contextPath}/rooms" class="boxed-btn3 small">Back To Rooms</a>
    </div>

    <c:if test="${not empty roomError}">
      <div class="alert alert-danger">${roomError}</div>
    </c:if>

    <section class="panel">
      <div class="row">
        <div class="col-md-5">
          <img class="w-100" src="${pageContext.request.contextPath}${room.imageUrl}" alt="${room.roomNumber}">
        </div>
        <div class="col-md-7">
          <form method="post" action="${pageContext.request.contextPath}/rooms" class="hotel-form" enctype="multipart/form-data">
            <div class="row">
              <div class="col-md-6">
                <label>Current ID</label>
                <input name="originalId" type="number" value="${room.id}" readonly>
              </div>
              <div class="col-md-6">
                <label>New ID</label>
                <input name="id" type="number" value="${room.id}" required>
              </div>
              <div class="col-md-6">
                <label>Room Number</label>
                <input name="roomNumber" value="${room.roomNumber}" required>
              </div>
              <div class="col-md-6">
                <label>Room Type</label>
                <select name="roomTypeId">
                  <c:forEach var="t" items="${roomTypes}">
                    <option value="${t.id}" <c:if test="${t.id == room.roomTypeId}">selected</c:if>>${t.name}</option>
                  </c:forEach>
                </select>
              </div>
              <div class="col-md-6">
                <label>Status</label>
                <select name="status">
                  <option <c:if test="${room.status == 'AVAILABLE'}">selected</c:if>>AVAILABLE</option>
                  <option <c:if test="${room.status == 'BOOKED'}">selected</c:if>>BOOKED</option>
                  <option <c:if test="${room.status == 'CHECK_IN'}">selected</c:if>>CHECK_IN</option>
                  <option <c:if test="${room.status == 'CHECK_OUT'}">selected</c:if>>CHECK_OUT</option>
                </select>
              </div>
              <div class="col-md-6">
                <label>Image File</label>
                <input name="imageFile" type="file" accept="image/*">
              </div>
              <input name="imageUrl" type="hidden" value="${room.imageUrl}">
              <div class="col-md-12">
                <label>Description</label>
                <input name="description" value="${room.description}">
              </div>
              <div class="col-md-12">
                <button class="boxed-btn3 small" type="submit">Save Changes</button>
                <a href="${pageContext.request.contextPath}/rooms" class="boxed-btn3 small">Cancel</a>
              </div>
            </div>
          </form>
        </div>
      </div>
    </section>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
