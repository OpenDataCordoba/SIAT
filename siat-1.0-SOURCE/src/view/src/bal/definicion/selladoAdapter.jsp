<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarSellado.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.selladoViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Sellado -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.sellado.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="bal" key="bal.sellado.codSellado.label"/>: </label></td>
					<td class="normal"><bean:write name="selladoAdapterVO" property="sellado.codSellado"/></td>
	
					<td><label><bean:message bundle="bal" key="bal.sellado.desSellado.label"/>: </label></td>
					<td class="normal"><bean:write name="selladoAdapterVO" property="sellado.desSellado" /></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="selladoAdapterVO" property="sellado.estado.value"/></td>
				</tr>

				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="selladoAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="selladoAdapterVO" property="sellado.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- Sellado -->
		
		<!-- listImpSel -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.sellado.listImpSel.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="selladoAdapterVO" property="sellado.listImpSel">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.impSel.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.impSel.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.impSel.tipoSellado.label"/></th>											
						<th align="left"><bean:message bundle="bal" key="bal.impSel.costo.label"/></th>

					</tr>
					<logic:iterate id="impSelVO" name="selladoAdapterVO" property="sellado.listImpSel">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="selladoAdapterVO" property="verEnabled" value="enabled">
									<logic:equal name="impSelVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verImpSel', '<bean:write name="impSelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="impSelVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="selladoAdapterVO" property="verEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="selladoAdapterVO" property="modificarEnabled" value="enabled">
									<logic:equal name="impSelVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarImpSel', '<bean:write name="impSelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="impSelVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="selladoAdapterVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="selladoAdapterVO" property="eliminarEnabled" value="enabled">
									<logic:equal name="impSelVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarImpSel', '<bean:write name="impSelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="impSelVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="selladoAdapterVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="impSelVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="impSelVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="impSelVO" property="tipoSellado.desTipoSellado"/>&nbsp;</td>
							<td><bean:write name="impSelVO" property="costoView"/>&nbsp;</td>

						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="selladoAdapterVO" property="sellado.listImpSel">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="selladoAdapterVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarImpSel', '<bean:write name="selladoAdapterVO" property="sellado.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!--ImpSel  -->
		
		<!-- listAccionSellado -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.sellado.listAccionSellado.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="selladoAdapterVO" property="sellado.listAccionSellado">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.accionSellado.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.accionSellado.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.accionSellado.accion.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.accionSellado.recurso.label"/></th>

					</tr>
					<logic:iterate id="accionSelladoVO" name="selladoAdapterVO" property="sellado.listAccionSellado">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="selladoAdapterVO" property="verEnabled" value="enabled">
									<logic:equal name="accionSelladoVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verAccionSellado', '<bean:write name="accionSelladoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="accionSelladoVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="selladoAdapterVO" property="verEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="selladoAdapterVO" property="modificarEnabled" value="enabled">
									<logic:equal name="accionSelladoVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarAccionSellado', '<bean:write name="accionSelladoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="accionSelladoVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="selladoAdapterVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="selladoAdapterVO" property="eliminarEnabled" value="enabled">
									<logic:equal name="accionSelladoVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarAccionSellado', '<bean:write name="accionSelladoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="accionSelladoVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="selladoAdapterVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="accionSelladoVO" property="fechaDesdeView" /></td>
							<td><bean:write name="accionSelladoVO" property="fechaHastaView" /></td>								
							<td><bean:write name="accionSelladoVO" property="accion.desAccion" /></td>
							<td><bean:write name="accionSelladoVO" property="recurso.desRecurso" /></td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="selladoAdapterVO" property="sellado.listAccionSellado">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="selladoAdapterVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarAccionSellado', '<bean:write name="selladoAdapterVO" property="sellado.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!--  -->
		
		<!-- listParSel -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.sellado.listParSel.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="selladoAdapterVO" property="sellado.listParSel">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.parSel.partida.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.parSel.tipoDistrib.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.parSel.monto.label"/></th>
					</tr>
					<logic:iterate id="parSelVO" name="selladoAdapterVO" property="sellado.listParSel">
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="selladoAdapterVO" property="verEnabled" value="enabled">
									<logic:equal name="parSelVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verParSel', '<bean:write name="parSelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="parSelVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="selladoAdapterVO" property="verEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="selladoAdapterVO" property="modificarEnabled" value="enabled">
									<logic:equal name="parSelVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarParSel', '<bean:write name="parSelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="parSelVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="selladoAdapterVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="selladoAdapterVO" property="eliminarEnabled" value="enabled">
									<logic:equal name="parSelVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarParSel', '<bean:write name="parSelVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="parSelVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="selladoAdapterVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="parSelVO" property="partida.desPartidaView" /></td>
							<td><bean:write name="parSelVO" property="tipoDistrib.desTipoDistrib" /></td>								
							<td><bean:write name="parSelVO" property="montoView" /></td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="selladoAdapterVO" property="sellado.listParSel">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="selladoAdapterVO" property="agregarEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarParSel', '<bean:write name="selladoAdapterVO" property="sellado.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!--  -->
				
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
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
