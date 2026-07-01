<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Maintenance</h1><p>Create maintenance requests and update maintenance status.</p></div></div>
<div class="hotel-section"><div class="container">
  <section class="panel"><h2 class="section-title"><i class="fa fa-wrench"></i><span>Create Maintenance Request</span></h2>
    <form method="post" class="hotel-form"><div class="row">
      <div class="col-md-3"><label>Room</label><select name="roomId"><c:forEach var="r" items="${rooms}"><option value="${r.id}">${r.roomNumber} - ${r.typeName}</option></c:forEach></select></div>
      <div class="col-md-3"><label>Title</label><input name="title" required></div>
      <div class="col-md-3"><label>Status</label><select name="status"><option>OPEN</option><option>IN_PROGRESS</option><option>DONE</option></select></div>
      <div class="col-md-3"><label>Description</label><input name="description"></div>
      <div class="col-md-12"><button class="boxed-btn3 small" type="submit">Create Request</button></div>
    </div></form>
  </section>
  <section class="panel"><h2 class="section-title"><i class="fa fa-history"></i><span>Maintenance History</span></h2><div class="table-responsive"><table class="hotel-table"><tr><th>ID</th><th>Room</th><th>Title</th><th>Description</th><th>Status</th><th>Updated</th><th>Action</th></tr>
    <c:forEach var="m" items="${requests}"><tr><td>#${m.id}</td><td>${m.roomNumber}</td><td>${m.title}</td><td>${m.description}</td><td><span class="badge-status">${m.status}</span></td><td>${m.updatedAt}</td><td><form method="post" action="${pageContext.request.contextPath}/maintenance/status" class="action-form"><input type="hidden" name="id" value="${m.id}"><select name="status"><option>OPEN</option><option>IN_PROGRESS</option><option>DONE</option></select><button class="boxed-btn3 small" type="submit">Update</button></form></td></tr></c:forEach>
  </table></div></section>
</div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
