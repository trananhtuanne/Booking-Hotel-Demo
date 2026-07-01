<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Room Types</h1>
    <p>Manage room categories, capacity, and prices.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-tags"></i></span>
        <div>
          <p class="eyebrow">Room Type Management</p>
          <h1 class="h3 mb-1">Room Types And Prices</h1>
          <p class="muted-copy">Create room categories used by the booking workflow.</p>
        </div>
      </div>
    </div>

    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-plus-circle"></i><span>Add Room Type</span></h2>
          <p class="muted-copy">Define the room type name, nightly price, and capacity.</p>
        </div>
      </div>
      <form method="post" class="hotel-form">
        <input type="hidden" name="id" value="0">
        <div class="row">
          <div class="col-md-3">
            <label>Room Type Name</label>
            <input name="name" placeholder="Deluxe Room" required>
          </div>
          <div class="col-md-3">
            <label>Price Per Night</label>
            <input name="pricePerNight" type="number" step="1000" placeholder="1200000" required>
          </div>
          <div class="col-md-2">
            <label>Capacity</label>
            <input name="capacity" type="number" placeholder="2" required>
          </div>
          <div class="col-md-4">
            <label>Description</label>
            <input name="description" placeholder="Room type description">
          </div>
          <div class="col-md-12">
            <button class="boxed-btn3 small" type="submit">Save Room Type</button>
          </div>
        </div>
      </form>
    </section>
    </c:if>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-list"></i><span>Room Type List</span></h2>
          <p class="muted-copy">Current room categories available in the system.</p>
        </div>
      </div>
      <div class="table-responsive">
        <table class="hotel-table">
          <tr>
            <th>Room Type</th>
            <th>Price Per Night</th>
            <th>Capacity</th>
            <th>Description</th>
            <c:if test="${sessionScope.currentUser.role == 'ADMIN'}"><th>Action</th></c:if>
          </tr>
          <c:forEach var="t" items="${roomTypes}">
            <tr>
              <td>${t.name}</td>
              <td><fmt:formatNumber value="${t.pricePerNight}" type="currency" currencySymbol="VND "/></td>
              <td>${t.capacity}</td>
              <td>${t.description}</td>
              <c:if test="${sessionScope.currentUser.role == 'ADMIN'}"><td><form method="post" action="${pageContext.request.contextPath}/room-types/delete" class="action-form"><input type="hidden" name="id" value="${t.id}"><button class="boxed-btn3 small" type="submit">Delete</button></form></td></c:if>
            </tr>
          </c:forEach>
        </table>
      </div>
    </section>

    <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
    <section class="panel">
      <div class="panel-header"><div><h2 class="section-title"><i class="fa fa-edit"></i><span>Update Room Type</span></h2><p class="muted-copy">Enter an existing room type ID to update its information.</p></div></div>
      <form method="post" class="hotel-form">
        <div class="row">
          <div class="col-md-1"><label>ID</label><input name="id" type="number" required></div>
          <div class="col-md-3"><label>Name</label><input name="name" required></div>
          <div class="col-md-3"><label>Price</label><input name="pricePerNight" type="number" step="1000" required></div>
          <div class="col-md-2"><label>Capacity</label><input name="capacity" type="number" required></div>
          <div class="col-md-3"><label>Description</label><input name="description"></div>
          <div class="col-md-12"><button class="boxed-btn3 small" type="submit">Update Room Type</button></div>
        </div>
      </form>
    </section>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-trash"></i><span>Delete Room Type</span></h2>
          <p class="muted-copy">Enter a room type ID to delete a type that is not assigned to any room.</p>
        </div>
      </div>
      <form method="post" action="${pageContext.request.contextPath}/room-types/delete" class="hotel-form">
        <div class="row">
          <div class="col-md-4"><label>Room Type ID</label><input name="id" type="number" required></div>
          <div class="col-md-8"><label>&nbsp;</label><button class="boxed-btn3 small" type="submit">Delete Room Type</button></div>
        </div>
      </form>
    </section>
    </c:if>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
