<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Customers</h1>
    <p>Manage customer information and contact details.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="page-heading">
      <div class="page-heading-copy">
        <span class="page-icon"><i class="fa fa-users"></i></span>
        <div>
          <p class="eyebrow">Customer Management</p>
          <h1 class="h3 mb-1">Customer Profiles</h1>
          <p class="muted-copy">Store guest information used for reservations and invoices.</p>
        </div>
      </div>
    </div>

    <c:if test="${sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN'}">
    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-user-plus"></i><span>Add Customer</span></h2>
          <p class="muted-copy">Create a new customer profile.</p>
        </div>
      </div>
      <form method="post" class="hotel-form">
        <input type="hidden" name="id" value="0">
        <div class="row">
          <div class="col-md-3">
            <label>Full Name</label>
            <input name="fullName" placeholder="Customer full name" required>
          </div>
          <div class="col-md-3">
            <label>Email</label>
            <input name="email" placeholder="customer@email.com">
          </div>
          <div class="col-md-2">
            <label>Phone</label>
            <input name="phone" placeholder="Phone number">
          </div>
          <div class="col-md-2">
            <label>Identity Number</label>
            <input name="identityNumber" placeholder="ID card">
          </div>
          <div class="col-md-2">
            <label>Address</label>
            <input name="address" placeholder="Address">
          </div>
          <div class="col-md-12">
            <button class="boxed-btn3 small" type="submit">Save Customer</button>
          </div>
        </div>
      </form>
    </section>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-trash"></i><span>Delete Customer</span></h2>
          <p class="muted-copy">Enter a customer ID to delete a profile that has no active booking.</p>
        </div>
      </div>
      <form method="post" action="${pageContext.request.contextPath}/customers/delete" class="hotel-form">
        <div class="row">
          <div class="col-md-4"><label>Customer ID</label><input name="id" type="number" required></div>
          <div class="col-md-8"><label>&nbsp;</label><button class="boxed-btn3 small" type="submit">Delete Customer</button></div>
        </div>
      </form>
    </section>
    </c:if>

    <section class="panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-address-book"></i><span>Customer List</span></h2>
          <p class="muted-copy">Registered customers in the current service data.</p>
        </div>
      </div>
      <div class="table-responsive">
        <table class="hotel-table">
          <tr>
            <th>Full Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Identity Number</th>
            <th>Address</th>
            <c:if test="${sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN'}"><th>Action</th></c:if>
          </tr>
          <c:forEach var="c" items="${customers}">
            <tr>
              <td>#${c.id} - ${c.fullName}</td>
              <td>${c.email}</td>
              <td>${c.phone}</td>
              <td>${c.identityNumber}</td>
              <td>${c.address}</td>
              <c:if test="${sessionScope.currentUser.role == 'RECEPTIONIST' || sessionScope.currentUser.role == 'ADMIN'}">
                <td>
                  <form method="post" action="${pageContext.request.contextPath}/customers/delete" class="action-form">
                    <input type="hidden" name="id" value="${c.id}">
                    <button class="boxed-btn3 small" type="submit">Delete</button>
                  </form>
                </td>
              </c:if>
            </tr>
          </c:forEach>
        </table>
      </div>
    </section>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
