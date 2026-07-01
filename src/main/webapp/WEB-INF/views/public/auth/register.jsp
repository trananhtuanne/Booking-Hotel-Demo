<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>

<div class="hero-lite">
  <div class="container">
    <h1>Register</h1>
    <p>Create a customer account for online booking.</p>
  </div>
</div>

<div class="hotel-section">
  <div class="container">
    <div class="login-panel">
      <div class="panel-header">
        <div>
          <h2 class="section-title"><i class="fa fa-user-plus"></i><span>Create Account</span></h2>
          <p class="muted-copy">Enter customer account information.</p>
        </div>
      </div>
      <form:form method="post" modelAttribute="user" cssClass="hotel-form">
        <label>Username</label>
        <form:input path="username" placeholder="Enter username" required="true"/>
        <label>Password</label>
        <input type="password" name="password" placeholder="Enter password" required>
        <label>Full Name</label>
        <form:input path="fullName" placeholder="Enter full name" required="true"/>
        <label>Email</label>
        <form:input path="email" placeholder="Enter email"/>
        <label>Phone</label>
        <form:input path="phone" placeholder="Enter phone number"/>
        <button class="boxed-btn3" type="submit">Create Account</button>
        <a class="line-button ml-3" href="${pageContext.request.contextPath}/login">Back To Login</a>
      </form:form>
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
