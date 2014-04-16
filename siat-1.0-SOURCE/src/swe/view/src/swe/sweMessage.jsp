<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<bean:define id="prevAction" name="userSession" property="navModel.prevAction"/>
<bean:define id="prevActionParameter" name="userSession" property="navModel.prevActionParameter"/>
	
<form action="<%=request.getContextPath()%><%=prevAction%>.do?method=<%=prevActionParameter%>" method="post">

	<table class="tablamenu">
		<tr>
    		<th>SWE &gt; Mensaje</th>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr><td>&nbsp;</td></tr>
		<tr valign="middle">
			<td align="center">
				<table class="mensaje" border="0">
					
					<tr>
						<th colspan="2">Mensaje</th>
					</tr>
					<tr height="30"><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td colspan="2" align="center">
							<fieldset>		
							<table border="0">
								<tr>
									<td align="right">
										<img border="0" src="<%=request.getContextPath()%>/images/message<bean:write name="userSession" property="navModel.messageType" format="#"/>.png"/>
									</td>
									<td align="left">
										<bean:write name="userSession" property="navModel.messageStr" />
									</td>
								</tr>
							</table>
							</fieldset>
						</td>
					</tr>
					<tr height="30"><td colspan="2">&nbsp;</td></tr>
				</table>
				<!--  html:textarea name="userSession" property="navModel.messageStr" cols="80" rows="15" readonly="true"/ -->
			</td>
		</tr>
		<tr height="30"><td>&nbsp;</td></tr>
	</table>
	<table width="100%" border="0">
		<tr>
			<td align="right">
			  	<html:submit property="btnContinuar"  styleClass="boton">
					<bean:message bundle="base" key="sweMessage.button.continuar"/>
				</html:submit>
			</td>
		</tr>
	</table>
</form>
