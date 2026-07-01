<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Rooms</h1>
    <p>Manage hotel room information, images, and status.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-bed"></i></span>
        <div>
          <p class="eyebrow">Room Management</p>
          <h1 class="h3 mb-1">Hotel Rooms</h1>
          <p class="muted-copy">Search, review, and update room records.</p>
        </div>
      </div>
      <c:if test="${not empty sessionScope.currentUser && (sessionScope.currentUser.role == 'MANAGER' || sessionScope.currentUser.role == 'ADMIN')}">
        <a href="${pageContext.request.contextPath}/rooms/history" class="boxed-btn3 small">Room Change History</a>
      </c:if>
    </div>
    <c:if test="${not empty roomError}">
      <div class="alert alert-danger">${roomError}</div>
    </c:if>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-search"></i><span>Search Rooms</span></h2>
          <p class="muted-copy">Filter rooms by number, type, or current status.</p>
        </div>
      </div>
      <form method="get" class="hotel-form">
        <div class="row">
          <div class="col-md-3">
            <label>Room Number</label>
            <input name="keyword" placeholder="Enter room number">
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
          <div class="col-md-3">
            <label>Status</label>
            <select name="status">
              <option value="">All Statuses</option>
              <option>AVAILABLE</option>
              <option>BOOKED</option>
              <option>CHECK_IN</option>
              <option>CHECK_OUT</option>
            </select>
          </div>
          <div class="col-md-3">
            <label>&nbsp;</label>
            <button class="boxed-btn3" type="submit">Search</button>
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
              <li>ID: ${r.id}</li>
              <li><fmt:formatNumber value="${r.pricePerNight}" type="currency" currencySymbol="VND "/> per billable day</li>
              <li>Status: <span class="badge-status">${r.status}</span></li>
              <li>${r.description}</li>
            </ul>
            <a href="${pageContext.request.contextPath}/rooms/detail?id=${r.id}" class="book_now">View Details</a>
            <c:if test="${not empty sessionScope.currentUser && (sessionScope.currentUser.role == 'STAFF' || sessionScope.currentUser.role == 'ADMIN')}">
              <a href="${pageContext.request.contextPath}/rooms/edit?id=${r.id}" class="book_now">Edit Room</a>
            </c:if>
            <c:if test="${not empty sessionScope.currentUser && sessionScope.currentUser.role == 'ADMIN'}">
              <form method="post" action="${pageContext.request.contextPath}/rooms/delete" class="action-form">
                <input type="hidden" name="id" value="${r.id}">
                <button class="book_now" type="submit">Delete Room</button>
              </form>
            </c:if>
          </div>
        </div>
      </c:forEach>
    </div>

    <c:if test="${not empty sessionScope.currentUser && (sessionScope.currentUser.role == 'STAFF' || sessionScope.currentUser.role == 'ADMIN')}">
    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-plus-circle"></i><span>Add Room</span></h2>
          <p class="muted-copy">Create a new room record. Use ID 0 for auto-increment or enter a custom ID.</p>
        </div>
      </div>
      <form method="post" class="hotel-form" enctype="multipart/form-data">
        <div class="row">
          <div class="col-md-1">
            <label>ID</label>
            <input name="id" type="number" min="0" value="0" required>
          </div>
          <div class="col-md-2">
            <label>Room Number</label>
            <input name="roomNumber" placeholder="101" required>
          </div>
          <div class="col-md-2">
            <label>Room Type</label>
            <select name="roomTypeId">
              <c:forEach var="t" items="${roomTypes}">
                <option value="${t.id}">${t.name}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-md-2">
            <label>Status</label>
            <select name="status">
              <option>AVAILABLE</option>
              <option>BOOKED</option>
              <option>CHECK_IN</option>
              <option>CHECK_OUT</option>
            </select>
          </div>
          <div class="col-md-5">
            <label>Image File</label>
            <input name="imageFile" type="file" accept="image/*">
          </div>
          <div class="col-md-12">
            <label>Description</label>
            <input name="description" placeholder="Room description">
          </div>
          <div class="col-md-12">
            <button class="boxed-btn3 small" type="submit">Save Room</button>
          </div>
        </div>
      </form>
    </section>
    </c:if>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
