<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Forgot Password</h1><p>Reset your password using username and email.</p></div></div>
<div class="hotel-section"><div class="container"><div class="login-panel">
  <h2 class="section-title"><i class="fa fa-key"></i><span>Reset Password</span></h2>
  <c:if test="${not empty message}"><div class="alert alert-success">${message}</div></c:if>
  <c:if test="${not empty error}"><div class="alert alert-warning">${error}</div></c:if>
  <form method="post" class="hotel-form">
    <label>Username</label><input name="username" required>
    <label>Email</label><input type="email" name="email" required>
    <label>New Password</label><input type="password" name="newPassword" required>
    <button class="boxed-btn3" type="submit">Reset Password</button>
    <a class="line-button ml-3" href="${pageContext.request.contextPath}/login">Back To Login</a>
  </form>
</div></div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
