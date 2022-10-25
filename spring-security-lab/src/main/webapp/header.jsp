<%@ page import="org.springframework.security.core.context.SecurityContext" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@ page import="org.springframework.security.core.Authentication" %>
<%@ page import="org.springframework.security.core.GrantedAuthority" %>
<%@ page import="java.util.Collection" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%
  SecurityContext secContext = SecurityContextHolder.getContext();
  Authentication authentication = secContext == null ? null : secContext.getAuthentication();
  String username = "Unknown";
  Collection<? extends GrantedAuthority> authorities = null;

  if(authentication != null){
    authorities = authentication.getAuthorities();
    username = authentication.getName();
  }
%>
<p>
<strong>Authenticated User: </strong>  <%= username %><br/>
<strong>Roles: </strong>  <%= StringUtils.join(authorities, ", ") %>
</p>

