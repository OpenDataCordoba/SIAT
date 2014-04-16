<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pro/AdministrarAdpCorrida.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pro" key="pro.adpCorridaViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Corrida -->
		<fieldset>
			<legend><bean:message bundle="pro" key="pro.adpCorridaViewAdapter.datosTitle"/></legend>
			<table class="tabladatos">
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="pro" key="pro.corrida.desCorrida.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="adpCorridaAdapterVO" property="corrida.desCorrida"/></td>
				</tr>
				<logic:equal name="adpCorridaAdapterVO" property="act" value="reprogramar">
					<!-- Fecha Inicio -->
					<tr>
						<td><label><bean:message bundle="pro" key="pro.corrida.fechaOriginal.ref"/>: </label></td>
						<td class="normal"><bean:write name="adpCorridaAdapterVO" property="corrida.fechaInicioView"/></td>
					<!-- Hora Inicio -->
						<td><label><bean:message bundle="pro" key="pro.corrida.horaOriginal.ref"/>: </label></td>
						<td class="normal"><bean:write name="adpCorridaAdapterVO" property="corrida.horaInicioView"/></td>
					</tr>
				</logic:equal>
				<logic:equal name="adpCorridaAdapterVO" property="act" value="cancelar">
					<!-- Fecha Inicio -->
					<tr>
						<td><label><bean:message bundle="pro" key="pro.corrida.fechaOriginal.ref"/>: </label></td>
						<td class="normal"><bean:write name="adpCorridaAdapterVO" property="corrida.fechaInicioView"/></td>
					<!-- Hora Inicio -->
						<td><label><bean:message bundle="pro" key="pro.corrida.horaOriginal.ref"/>: </label></td>
						<td class="normal"><bean:write name="adpCorridaAdapterVO" property="corrida.horaInicioView"/></td>
					</tr>
				</logic:equal>

				<logic:notEqual name="adpCorridaAdapterVO" property="act" value="reiniciar">
				<logic:notEqual name="adpCorridaAdapterVO" property="act" value="siguiente">
				<logic:notEqual name="adpCorridaAdapterVO" property="act" value="cancelar">
					<!-- Edit Fecha Inicio -->
					<tr>
						<logic:equal name="adpCorridaAdapterVO" property="act" value="reprogramar">
							<td><label><bean:message bundle="pro" key="pro.corrida.fechaInicio.ref"/>: </label></td>
						</logic:equal>
						<logic:notEqual name="adpCorridaAdapterVO" property="act" value="reprogramar">
							<td><label><bean:message bundle="pro" key="pro.corrida.fechaNueva.ref"/>: </label></td>
						</logic:notEqual>
						<td class="normal">
							<html:text name="adpCorridaAdapterVO" property="corrida.fechaInicioView" styleId="fechaInicioView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaInicioView');" id="a_fechaInicioView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
						
					<!-- Edit Hora Inicio -->
						<logic:equal name="adpCorridaAdapterVO" property="act" value="reprogramar">
							<td><label><bean:message bundle="pro" key="pro.corrida.horaInicio.ref"/>: </label></td>
						</logic:equal>
						<logic:notEqual name="adpCorridaAdapterVO" property="act" value="reprogramar">
							<td><label><bean:message bundle="pro" key="pro.corrida.horaNueva.ref"/>: </label></td>
						</logic:notEqual>
						<td class="normal">
							<html:text name="adpCorridaAdapterVO" property="corrida.horaInicioView" size="10" maxlength="10" styleClass="datos"/>
						</td>
					</tr>
				</logic:notEqual>
				</logic:notEqual>
				</logic:notEqual>
			</table>
		</fieldset>	
		<!-- Corrida -->

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="adpCorridaAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="pro" key="pro.adpCorridaViewAdapter.abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="adpCorridaAdapterVO" property="act" value="reprogramar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reprogramar', '');">
							<bean:message bundle="pro" key="pro.adpCorridaViewAdapter.abm.button.reprogramar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="adpCorridaAdapterVO" property="act" value="cancelar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('cancelar', '');">
							<bean:message bundle="pro" key="pro.adpCorridaViewAdapter.abm.button.cancelar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="adpCorridaAdapterVO" property="act" value="reiniciar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('reiniciar', '');">
							<bean:message bundle="pro" key="pro.adpCorridaViewAdapter.abm.button.reiniciar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="adpCorridaAdapterVO" property="act" value="siguiente">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('siguiente', '');">
							<bean:message bundle="pro" key="pro.adpCorridaViewAdapter.abm.button.siguiente"/>
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
