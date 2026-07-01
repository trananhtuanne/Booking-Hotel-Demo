<%@ include file="/WEB-INF/views/shared/layout/header.jspf" %>
<div class="hero-lite"><div class="container"><h1>Access Denied</h1><p>Your role does not have permission to use this function.</p></div></div>
<div class="hotel-section"><div class="container"><section class="panel text-center">
  <h2 class="section-title justify-content-center"><i class="fa fa-lock"></i><span>Permission Required</span></h2>
  <p class="muted-copy">Please login with the correct role or return to the dashboard.</p>
  <a class="boxed-btn3 small mt-3" href="${pageContext.request.contextPath}/dashboard">Back To Dashboard</a>
</section></div></div>
<%@ include file="/WEB-INF/views/shared/layout/footer.jspf" %>
