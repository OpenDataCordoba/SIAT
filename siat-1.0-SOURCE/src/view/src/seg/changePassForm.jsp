<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<html:form styleId="filter" action="/login/LoginSsl.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>

	<h1><bean:message bundle="seg" key="seg.changePassForm.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<fieldset>
		
		<table class="tabladatos">
			<tr>
				<td>			
					<label><bean:message bundle="seg" key="seg.userName.label"/>:</label>
				</td>
				<td class="normal">	
					<bean:write name="userSession" property="userName"/>
            	</td>
            </tr>
            <tr>
            	<td>	
					<label><bean:message bundle="seg" key="seg.changePasswordForm.newPassword.label"/>:</label>
				</td>
				<td class="normal">
					<html:password name="usuarioVO" property="newPassword" size="10" maxlength="15"/>
				</td>
			<tr>
			<tr>
            	<td>	
					<label><bean:message bundle="seg" key="seg.changePasswordForm.newPasswordReentry.label"/>:</label>
				</td>
				<td class="normal">
					<html:password name="usuarioVO" property="newPasswordReentry" size="10" maxlength="15"/>
				</td>
			<tr>
			
			<tr>
		        <td align="right">
		            <html:reset>
		                <bean:message bundle="seg" key="seg.changePasswordForm.button.limpiar"/>
		            </html:reset>
		        </td>

		        <td class="normal">
		            <html:submit>
		                <bean:message bundle="seg" key="seg.changePasswordForm.button.cambiar"/>
		            </html:submit>
		        </td>
		    </tr>
		</table>	
	</fieldset>
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>  	    	
   	    </tr>
   	</table>
	
	<input type="hidden" name="method" value="changePass"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
		
  <input type="hidden" name="anonimo" value="<%=request.getParameter("anonimo")%>"/>
  <input type="hidden" name="urlReComenzar" value="<%=request.getParameter("urlReComenzar")%>"/>

</html:form>
