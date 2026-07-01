<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Change Password</h1><p>Update your account password.</p></div></div>
<div class="hotel-section"><div class="container"><div class="login-panel">
  <h2 class="section-title"><i class="fa fa-lock"></i><span>Change Password</span></h2>
  <c:if test="${not empty message}"><div class="alert alert-success">${message}</div></c:if>
  <c:if test="${not empty error}"><div class="alert alert-warning">${error}</div></c:if>
  <form method="post" class="hotel-form">
    <label>Old Password</label><input type="password" name="oldPassword" required>
    <label>New Password</label><input type="password" name="newPassword" required>
    <button class="boxed-btn3" type="submit">Change Password</button>
  </form>
</div></div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
