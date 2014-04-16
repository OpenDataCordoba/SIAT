<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<bean:define id="confAction" name="userSession" property="navModel.confAction"/>
<bean:define id="confActionParameter" name="userSession" property="navModel.confActionParameter"/>
	
<form id="filter" action="<%=request.getContextPath()%><%=confAction%>.do?method=<%=confActionParameter%>" method="post">

	<table class="tablamenu">
		<tr>
    		<th>SIAT &gt; Mensaje</th>
		</tr>
	</table>
	<table width="100%" border="0">
		<tr><td>&nbsp;</td></tr>
		<tr valign="middle">
			<td align="center">
				<table class="mensaje">
					<!--tr>
						<th colspan="2">Mensaje</th>
					</tr-->
					<tr height="30"><td colspan="2">&nbsp;</td></tr>
					<tr>
						<td colspan="2" align="center">
							<table border="0">
								<tr>
									<td align="right">
										<a href="<%=request.getContextPath()%>/base/siatException.jsp" target="_blank">
											<img border="0" src="<%=request.getContextPath()%>/images/message<bean:write name="userSession" property="navModel.messageType" format="#"/>.png"/>
										</a>										
									</td>
									<td align="left">
										<bean:write name="userSession" property="navModel.messageStr" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr height="30"><td colspan="2">&nbsp;</td></tr>
				</table>
			</td>
		</tr>
		<tr height="30"><td>&nbsp;</td></tr>
	</table>
	<table width="100%" border="0">
		<tr>
			 <td id="btoContinuar" align="right">
			  	<html:submit property="btnContinuar"  styleClass="boton">
					<bean:message bundle="base" key="abm.button.continuar"/>
				</html:submit>
			</td>
		</tr>
	</table>
</form>
