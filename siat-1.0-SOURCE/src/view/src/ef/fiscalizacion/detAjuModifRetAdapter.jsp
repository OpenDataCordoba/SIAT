<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarDetAju.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.detAjuModifRetAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="detAjuAdapterVO" property="detAju.id" bundle="base" formatKey="general.format.id"/>');"
						value='<bean:message bundle="base" key="abm.button.volver"/>'/>					
				</td>
			</tr>
		</table>
		
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.detAju.label"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.detAju.fecha.label"/>: </label></td>
					<td class="normal"><bean:write name="detAjuAdapterVO" property="detAju.fechaView"/></td>
					
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="detAjuAdapterVO" property="detAju.ordConCue.cuenta.numeroCuenta" /></td>
				</tr>
				
				<tr>
					<td><label><bean:message bundle="ef" key="ef.aliComFueCol.periodo.label"/>: </label></td>
					<td class="normal"><bean:write name="detAjuAdapterVO" property="detAjuDet.plaFueDatCom.periodoAnioView"/></td>
				</tr>
															
				<tr>
					<td colspan="2" align="right"><label><bean:message bundle="ef" key="ef.detAju.retencion.label"/>: </label></td>
					<td class="normal" colspan="2" align="left"><html:text name="detAjuAdapterVO" property="detAjuDet.retencion" size="7"/></td>					
				</tr>														
			</table>
		</fieldset>	

	
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
		   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="detAjuAdapterVO" property="detAju.id" bundle="base" formatKey="general.format.id"/>');"
						value='<bean:message bundle="base" key="abm.button.volver"/>'/>					
				</td>
				<logic:greaterThan name="detAjuAdapterVO" property="detAju.cantDetAjuDet" value="0">
					<td align="right">
							<input type="button" class="boton" onClick="submitForm('modificarRetencion','');" 
								value="<bean:message bundle="ef" key="ef.detAjuAdapter.button.modifRet"/>"/>
					</td>				
				</logic:greaterThan>   	    			
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- detAjuModifRetAdapter.jsp -->