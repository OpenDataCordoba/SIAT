<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarOtrIngTes.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.otrIngTesAdapter.title"/></h1>		
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
			<legend><bean:message bundle="bal" key="bal.otrIngTes.title"/></legend>			
			<table class="tabladatos" width="100%">
			<!-- Recurso -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.recurso.desRecurso"/></td>
				</tr>
				<tr>
			<!-- Area -->		
					<td><label>&nbsp;<bean:message bundle="def" key="def.area.label"/>: </label></td>
					<td  colspan="4" class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.areaOrigen.desArea"/></td>
				</tr>
				<tr>
					<!-- CuentaBanco Origen -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.cueBanOrigen.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.cueBanOrigen.nroCuenta"/></td>
				</tr>	
				<tr>
					<!-- Fecha -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.fechaOtrIngTes.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.fechaOtrIngTesView"/></td>
				</tr>
				<!-- Importe -->	
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.importe.label"/>: </label></td>
					<td class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.importeView"/></td>
				</tr>	
				<!-- Descripcion -->		
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.otrIngTes.descripcion.label"/>: </label></td>
					<td colspan="4" class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.descripcion"/></td>
				</tr>
				<!-- Observacion-->		
				<tr>
					<td><label><bean:message bundle="bal" key="bal.otrIngTes.observacion.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="otrIngTesAdapterVO" property="otrIngTes.observaciones"/></td>					
				</tr>
				<!-- Lista de conceptos -->
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td>&nbsp;</td>				
					<td>				
						<table class="tramonline" border="0" cellpadding="0" cellspacing="1">
							<caption>Conceptos</caption>
	          				<tbody>
								<logic:notEmpty name="otrIngTesAdapterVO" property="otrIngTes.listOtrIngTesRecCon">
									<logic:iterate id="OtrIngTesRecConVO" name="otrIngTesAdapterVO" property="otrIngTes.listOtrIngTesRecCon">
										<tr>
											<td><bean:write name="OtrIngTesRecConVO" property="recCon.desRecCon"/></td>
											<td><bean:write name="OtrIngTesRecConVO" property="importeView"/></td>
	  									</tr>	
									</logic:iterate>
								</logic:notEmpty>
								<logic:empty name="otrIngTesAdapterVO" property="otrIngTes.listOtrIngTesRecCon">								
									<tr><td colspan="2"><bean:message bundle="bal" key="bal.otrIngTesAdapter.listOtrIngTesRecCon.vacia"/></td></tr>
								</logic:empty>			
							</tbody>
						</table>	
					</td>
					<td>&nbsp;</td>				
				</tr>			
			</table>
		</fieldset>
	
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="otrIngTesAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
 			</tr>
		</table>
		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
			
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
		
		