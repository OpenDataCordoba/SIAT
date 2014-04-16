<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarRelCla.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.nodoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- RelCla -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.relCla.title"/></legend>			
			<table class="tabladatos">
				<!-- Nodo 1 (Origen) -->		
				<tr>
					<!-- Clave -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.clave.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo1.label"/>: </label></td>
					<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo1.clave"/></td>
				</tr>
				<tr>
					<!-- Descripcion -->		
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo1.label"/>: </label></td>
					<td colspan="3" class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo1.descripcion"/></td>
				</tr>
				<!-- Clasificador -->		
				<logic:equal name="relClaAdapterVO" property="act" value="agregar">		
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.clasificador.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo2.label"/>: </label></td>
						<td class="normal">
							<html:select name="relClaAdapterVO" property="relCla.nodo2.clasificador.id" styleClass="select" onchange="submitForm('paramClasificador', '');">
								<html:optionsCollection name="relClaAdapterVO" property="listClasificador" label="descripcion" value="id" />
							</html:select>
						</td>
					</tr>
					<!-- Nodo 2 (Destino) -->		
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo2.label"/>: </label></td>
						<td colspan="6" class="normal">	
							<html:select name="relClaAdapterVO" property="relCla.nodo2.id" styleClass="select">
								<html:optionsCollection name="relClaAdapterVO" property="listNodo" label="descripcionView" value="id" />
							</html:select>
						</td>
					</tr>
				</logic:equal>
				<logic:equal name="relClaAdapterVO" property="act" value="modificar">		
					<!-- Nodo 2 (Destino) -->		
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.clasificador.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo2.label"/>: </label></td>
						<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo2.clasificador.descripcion"/></td>
					</tr>
					<tr>
						<!-- Nivel -->		
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.nivel.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo2.label"/>: </label></td>
						<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo2.nivelView"/></td>
					</tr>
					<tr>
						<!-- Clave -->		
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.clave.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo1.label"/>: </label></td>
						<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo2.clave"/></td>
					</tr>
					<tr>
						<!-- Descripcion -->		
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.nodo.descripcion.label"/>&nbsp;<bean:message bundle="bal" key="bal.relCla.nodo1.label"/>: </label></td>
						<td colspan="3" class="normal"><bean:write name="relClaAdapterVO" property="relCla.nodo2.descripcion"/></td>
					</tr>
				</logic:equal>
				<tr>
					<logic:equal name="relClaAdapterVO" property="act" value="agregar">	
						<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.relCla.fechaDesde.label"/>: </label></td>
						<td class="normal">
							<html:text name="relClaAdapterVO" property="relCla.fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</logic:equal>
					<logic:equal name="relClaAdapterVO" property="act" value="modificar">	
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.relCla.fechaDesde.label"/>: </label></td>
						<td class="normal"><bean:write name="relClaAdapterVO" property="relCla.fechaDesdeView"/></td>
			
					</logic:equal>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.relCla.fechaHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="relClaAdapterVO" property="relCla.fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>		
			</table>
		</fieldset>
		<!-- Fin RelCla -->
		
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="relClaAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="relClaAdapterVO" property="act" value="agregar">
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