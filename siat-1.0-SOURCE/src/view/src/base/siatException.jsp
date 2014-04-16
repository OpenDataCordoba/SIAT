<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html>
	<table width="100%" border="0">
		<tr>
			<td>
				<bean:write name="userSession" property="navModel.excepcionStr"/>
			</td>
		</tr>
	</table>
</html>
