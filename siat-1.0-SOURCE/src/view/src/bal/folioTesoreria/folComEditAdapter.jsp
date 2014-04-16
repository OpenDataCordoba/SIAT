<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarFolCom.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.folioAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- FolCom -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.folCom.title"/></legend>			
			<table class="tabladatos">
				<!-- Fecha -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.folCom.fecha.label"/>: </label></td>
					<td class="normal">
						<html:text name="folComAdapterVO" property="folCom.fechaView" styleId="fechaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar_change('fechaView');" id="a_fechaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>	
				<tr>
					<!-- Concepto -->		
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.folCom.concepto.label"/>: </label></td>
					<td class="normal"><html:text name="folComAdapterVO" property="folCom.concepto" size="20" maxlength="60" /></td>
				</tr>
				<tr>
					<!-- Importe -->		
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.folCom.importe.label"/>: </label></td>
					<td class="normal"><html:text name="folComAdapterVO" property="folCom.importeView" size="10" maxlength="22" /></td>
				</tr>	
				<tr>
					<!-- DesCueBan -->		
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.folCom.desCueBan.label"/>: </label></td>
					<td class="normal"><html:text name="folComAdapterVO" property="folCom.desCueBan" size="20" maxlength="60" /></td>
				</tr>
				<tr>
					<!-- NroComp -->		
					<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.folCom.nroComp.label"/>: </label></td>
					<td class="normal"><html:text name="folComAdapterVO" property="folCom.nroComp" size="10" maxlength="22" /></td>
				</tr>
				<tr>
				<!-- Compensacion -->		
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.compensacion.label"/>: </label></td>
					    <td class="normal" colspan="4">
								<html:text name="folComAdapterVO" property="folCom.compensacion.descripcion" size="30" disabled="true"/>
								<html:button property="btnBuscarCompensacion"  styleClass="boton" onclick="submitForm('buscarCompensacion', '');">
									<bean:message bundle="bal" key="bal.folCom.button.buscarCompensacion"/>
								</html:button>
						</td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin FolCom -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="folComAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="folComAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
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
		
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->