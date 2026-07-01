<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>System Management</h1><p>Configuration, backup, and audit logs for administrators.</p></div></div>
<div class="hotel-section"><div class="container">
  <section class="panel">
    <h2 class="section-title"><i class="fa fa-cogs"></i><span>System Configuration</span></h2>
    <p class="muted-copy">Application mode: In-memory service data, no MySQL database.</p>
  </section>
  <section class="panel">
    <h2 class="section-title"><i class="fa fa-database"></i><span>Database Backup</span></h2>
    <p class="muted-copy">Backup snapshot generated at ${backupTime}. This project stores demo data in memory.</p>
  </section>
  <section class="panel">
    <h2 class="section-title"><i class="fa fa-list"></i><span>Audit Logs</span></h2>
    <div class="table-responsive"><table class="hotel-table"><tr><th>Time</th><th>Action</th><th>User</th></tr><tr><td>${backupTime}</td><td>System management page viewed</td><td>${sessionScope.currentUser.username}</td></tr></table></div>
  </section>
</div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
