<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Users And Roles</h1><p>Manage users, roles, and permissions.</p></div></div>
<div class="hotel-section"><div class="container">
  <section class="panel"><h2 class="section-title"><i class="fa fa-user-plus"></i><span>Add Or Update User</span></h2>
    <form method="post" class="hotel-form"><div class="row">
      <div class="col-md-1"><label>ID</label><input type="number" name="id" value="0"></div>
      <div class="col-md-2"><label>Username</label><input name="username" required></div>
      <div class="col-md-2"><label>Password</label><input name="passwordHash" placeholder="Default 123456"></div>
      <div class="col-md-2"><label>Full Name</label><input name="fullName" required></div>
      <div class="col-md-2"><label>Email</label><input name="email"></div>
      <div class="col-md-1"><label>Phone</label><input name="phone"></div>
      <div class="col-md-1"><label>Role</label><select name="role"><option>ADMIN</option><option>MANAGER</option><option>RECEPTIONIST</option><option>STAFF</option><option>CUSTOMER</option></select></div>
      <div class="col-md-1"><label>Status</label><select name="status"><option>ACTIVE</option><option>LOCKED</option></select></div>
      <div class="col-md-12"><button class="boxed-btn3 small" type="submit">Save User</button></div>
    </div></form>
  </section>
  <section class="panel"><h2 class="section-title"><i class="fa fa-shield"></i><span>Role Permissions</span></h2><p class="muted-copy">ADMIN: full access. MANAGER: reports and operations. RECEPTIONIST: reservations and invoices. STAFF: room status and maintenance. CUSTOMER: profile, booking, history, and invoices.</p></section>
  <section class="panel"><h2 class="section-title"><i class="fa fa-trash"></i><span>Delete User</span></h2><p class="muted-copy">Enter a user ID to delete an account. The default admin account is protected.</p><form method="post" action="${pageContext.request.contextPath}/users/delete" class="hotel-form"><div class="row"><div class="col-md-4"><label>User ID</label><input type="number" name="id" required></div><div class="col-md-8"><label>&nbsp;</label><button class="boxed-btn3 small" type="submit">Delete User</button></div></div></form></section>
  <section class="panel"><h2 class="section-title"><i class="fa fa-users"></i><span>User List</span></h2><div class="table-responsive"><table class="hotel-table"><tr><th>ID</th><th>Username</th><th>Full Name</th><th>Email</th><th>Phone</th><th>Role</th><th>Status</th><th>Actions</th></tr><c:forEach var="u" items="${users}"><tr><td>${u.id}</td><td>${u.username}</td><td>${u.fullName}</td><td>${u.email}</td><td>${u.phone}</td><td>${u.role}</td><td><span class="badge-status">${u.status}</span></td><td><form class="action-form" method="post" action="${pageContext.request.contextPath}/users/status"><input type="hidden" name="id" value="${u.id}"><input type="hidden" name="status" value="LOCKED"><button class="boxed-btn3 small" type="submit">Lock</button></form><form class="action-form" method="post" action="${pageContext.request.contextPath}/users/status"><input type="hidden" name="id" value="${u.id}"><input type="hidden" name="status" value="ACTIVE"><button class="boxed-btn3 small" type="submit">Unlock</button></form><form class="action-form" method="post" action="${pageContext.request.contextPath}/users/delete"><input type="hidden" name="id" value="${u.id}"><button class="boxed-btn3 small" type="submit">Delete</button></form></td></tr></c:forEach></table></div></section>
</div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
