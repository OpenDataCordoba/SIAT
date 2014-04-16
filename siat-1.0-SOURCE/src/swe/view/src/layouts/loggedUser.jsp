<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<table class="tablaloggeduser">
	<tr>
		<td width="182" align="left">
			<bean:write name="userSession" property="fechaHoy" />
		</td>
		<td align="left">
			<bean:write name="userSession" property="userRoleStr" />		
		</td>
		<td align="rigth">
			<bean:write name="userSession" property="loggedUserStr" />
		</td>
	</tr>
</table>