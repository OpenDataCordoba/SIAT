<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/rec/AdministrarObra.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="rec" key="rec.obraPlanillaObraAdapter.title"/></h1>
		
		<!-- Obra -->
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.obra.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="rec" key="rec.obra.numeroObra.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="obraAdapterVO" property="obra.numeroObraView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.obra.desObra.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="obraAdapterVO" property="obra.desObra"/></td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="rec" key="rec.obra.emiteContado.label"/>: </label></td>
					<td class="normal"><bean:write name="obraAdapterVO" property="obra.emiteContado.value"/></td>				
				</tr>				
				<tr>
					<td><label><bean:message bundle="rec" key="rec.obra.emitePlanCorto.label"/>: </label></td>
					<td class="normal"><bean:write name="obraAdapterVO" property="obra.emitePlanCorto.value"/></td>				

					<td><label><bean:message bundle="rec" key="rec.obra.emitePlanLargo.label"/>: </label></td>
					<td class="normal"><bean:write name="obraAdapterVO" property="obra.emitePlanLargo.value"/></td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="obraAdapterVO" property="obra.estadoObra.desEstadoObra"/></td>
				</tr>				
			</table>			
		</fieldset>	
		<!-- Obra -->
		
		<!-- PlanillaObra -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="rec" key="rec.obra.listPlanillaObra.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="obraAdapterVO" property="obra.listPlanillaObra">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th width="1">&nbsp;</th> <!-- Activar/Desactivar -->
						<th width="1">&nbsp;</th> <!-- Cargar Repartidor -->
						<th width="1">&nbsp;</th> <!-- Imprimir -->
						<th align="left"><bean:message bundle="rec" key="rec.planillaObra.numeroPlanilla.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.planillaObra.desPlanilla.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.planillaObra.callePpal.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.planillaObra.calleDesde.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.planillaObra.calleHasta.label"/></th>
						<th align="left"><bean:message bundle="rec" key="rec.estPlaObr.label"/></th>
					</tr>
					
					<logic:iterate id="PlanillaObraVO" name="obraAdapterVO" property="obra.listPlanillaObra">
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="obraAdapterVO" property="verPlanillaObraEnabled" value="enabled">
									<logic:equal name="PlanillaObraVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verPlanillaObra', '<bean:write name="PlanillaObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanillaObraVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="verPlanillaObraEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="obraAdapterVO" property="modificarPlanillaObraEnabled" value="enabled">
									<logic:equal name="PlanillaObraVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarPlanillaObra', '<bean:write name="PlanillaObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanillaObraVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="modificarPlanillaObraEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="obraAdapterVO" property="eliminarPlanillaObraEnabled" value="enabled">
									<logic:equal name="PlanillaObraVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarPlanillaObra', '<bean:write name="PlanillaObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="PlanillaObraVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="obraAdapterVO" property="eliminarPlanillaObraEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><!-- En estado creado -->
								<logic:equal name="PlanillaObraVO" property="estPlaObr.id" value="1">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('activar', '<bean:write name="PlanillaObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.activar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/creado0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="PlanillaObraVO" property="estPlaObr.id" value="1">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/noaccion0.gif"/>
								</logic:notEqual>
							</td> 
							<td>
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('admRepartidor', '<bean:write name="PlanillaObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="rec.planillaObraAdapter.button.cargaRepartidor"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cargarRepartidor0.gif"/>
								</a>
							</td>
							<td>
								<a style="cursor: pointer; cursor: hand;" onclick="submitForm('imprimir', '<bean:write name="PlanillaObraVO" property="id" bundle="base" formatKey="general.format.id"/>');">
									<img title="<bean:message bundle="base" key="abm.button.imprimir"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/imprimir0.gif"/>
								</a>
							</td>
							<td><bean:write name="PlanillaObraVO" property="numeroPlanillaView"/>&nbsp;</td>
							<td><bean:write name="PlanillaObraVO" property="desPlanilla"/>&nbsp;</td>					
							<td><bean:write name="PlanillaObraVO" property="callePpal.nombreCalle"/>&nbsp;</td>
							<td><bean:write name="PlanillaObraVO" property="calleDesde.nombreCalle"/>&nbsp;</td>
							<td><bean:write name="PlanillaObraVO" property="calleHasta.nombreCalle"/>&nbsp;</td>
							<td><bean:write name="PlanillaObraVO" property="estPlaObr.desEstPlaObr"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="obraAdapterVO" property="obra.listPlanillaObra">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
			
			<tr>
				<td align="right" colspan="20">
   	    				<bean:define id="agregarEnabled" name="obraAdapterVO" property="agregarPlanillaObraEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarPlanillaObra', '<bean:write name="obraAdapterVO" property="obra.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
				</td>			
			</tr>		
		</table>
		<!-- PlanillaObra -->
				
		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
