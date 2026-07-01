<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Login</h1>
    <p>Access the Hotel Booking Management System.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="login-panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-sign-in"></i><span>User Login</span></h2>
          <p class="muted-copy">Sign in to manage hotel operations.</p>
        </div>
      </div>
      <c:if test="${not empty error}">
        <div class="alert alert-warning">${error}</div>
      </c:if>
      <c:if test="${param.registered == 1}">
        <div class="alert alert-success">Registration completed successfully.</div>
      </c:if>
      <form method="post" class="hotel-form">
        <label>Username</label>
        <input name="username" placeholder="Enter username" required>
        <label>Password</label>
        <input type="password" name="password" placeholder="Enter password" required>
        <button class="boxed-btn3" type="submit">Login</button>
        <a class="line-button ml-3" href="${pageContext.request.contextPath}/register">Register</a>
      </form>
      <p class="mt-3 mb-0">Demo account: admin / admin123</p>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
