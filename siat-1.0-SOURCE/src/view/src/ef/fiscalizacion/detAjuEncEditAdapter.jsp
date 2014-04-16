<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/ef/AdministrarEncDetAju.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="ef" key="ef.detAjuEditAdapter.title"/></h1>		

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- DetAju -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.detAju.title"/></legend>
		
		<table class="tabladatos">
		<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/></label></td>
				<td class="normal" colspan="3" align="left">
					
					<!-- se muestran las cuentas agrupadas por recurso, pero se submite el id de ordConCue -->
					<html:select name="encDetAjuAdapterVO" property="detAju.ordConCue.id" styleClass="select" >
						<% String idCategoriaAnt = "";%>
						<bean:define id="listOrdConCue" name="encDetAjuAdapterVO" property="listOrdConCue"/>
						<logic:notEmpty name="listOrdConCue">
							<logic:iterate id="ordConCue" name="listOrdConCue">
								
								<bean:define id="recurso" name="ordConCue" property="cuenta.recurso"/>
								
								<logic:equal name="ordConCue" property="id" value="-1">
									<option value='<bean:write name="ordConCue" property="id" bundle="base" formatKey="general.format.id"/>'>
										Seleccionar...
									</option>
								</logic:equal>
								<logic:notEqual name="recurso" property="id" value="-1">
									<logic:notEqual name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">
										<optgroup label='<bean:write name="recurso" property="categoria.desCategoria"/>'>
										<bean:define id="catId" name="recurso" property="categoria.id"/>
										<% idCategoriaAnt = ""+catId+"";%>
									</logic:notEqual>
									<logic:equal name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">
										<bean:define id="ordConCueId" name="encDetAjuAdapterVO" property="detAju.ordConCue.id"/>
										<% String idOrdConCue = ""+ordConCueId+"";%>				
											<option value='<bean:write name="ordConCue" property="id" bundle="base" formatKey="general.format.id"/>'												 					
												<logic:equal name="ordConCue" property="id" value="<%=idOrdConCue%>">
													selected="selected"											
												</logic:equal>
											>
											<bean:write name="ordConCue" property="cuenta.numeroCuenta"/>
										</option>
									</logic:equal>
									<logic:notEqual name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">	
										</optgroup>
									</logic:notEqual>
								</logic:notEqual>
							</logic:iterate>
						</logic:notEmpty>
					</html:select>	

				
				</td>			
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>
	<!-- DetAju -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encDetAjuAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encDetAjuAdapterVO" property="act" value="agregar">
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
<!-- Fin formulario -->
<!-- detAjuEncEditAdapter.jsp -->
