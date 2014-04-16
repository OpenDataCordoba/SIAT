<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarProcurador.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.procuradorAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Procurador -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.procurador.title"/></legend>
		
		<table class="tabladatos">
				<!-- Descripcion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.descripcion"/></td>
				</tr>
				
				<!-- Domicilio -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.domicilio.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.domicilio"/></td>

				
				<!-- Telefono -->

					<td><label><bean:message bundle="gde" key="gde.procurador.telefono.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.telefono"/></td>
				</tr>
				
				<!-- HorarioAtencion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.horarioAtencion.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.horarioAtencion"/></td>

				
				<!-- TipoProcurador -->

					<td><label><bean:message bundle="gde" key="gde.procurador.tipoProcurador.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.tipoProcurador.desTipoProcurador"/></td>
				</tr>
				
				<!-- Observarcion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.procurador.observacion.label"/>: </label></td>
					<td class="normal"><bean:write name="procuradorAdapterVO" property="procurador.observacion"/></td>
				</tr>
			
			<tr>
				<td colspan="4"> 
					<bean:define id="modificarEncabezadoEnabled" name="procuradorAdapterVO" property="modificarEncabezadoEnabled"/>
					<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
						'<bean:write name="procuradorAdapterVO" property="procurador.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- Procurador -->
	
	<!-- listProRec -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.procurador.listProRec.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="procuradorAdapterVO" property="procurador.listProRec">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.proRec.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.proRec.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.proRec.recurso.label"/></th>
					</tr>
					<logic:iterate id="proRecVO" name="procuradorAdapterVO" property="procurador.listProRec">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="procuradorAdapterVO" property="verEnabled" value="enabled">
									<logic:equal name="proRecVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verProRec', '<bean:write name="proRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="proRecVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="procuradorAdapterVO" property="verEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="procuradorAdapterVO" property="modificarEnabled" value="enabled">
									<logic:equal name="proRecVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarProRec', '<bean:write name="proRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="proRecVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="procuradorAdapterVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="procuradorAdapterVO" property="eliminarEnabled" value="enabled">
									<logic:equal name="proRecVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarProRec', '<bean:write name="proRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="proRecVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="procuradorAdapterVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="proRecVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="proRecVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="proRecVO" property="recurso.desRecurso"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="procuradorAdapterVO" property="procurador.listProRec">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="procuradorAdapterVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarProRec', '<bean:write name="procuradorAdapterVO" property="procurador.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!--ProRec  -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
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
