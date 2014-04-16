<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarDesEsp.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.desEspEditAdapter.title"/></h1>
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- DesEsp -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.desEsp.title"/></legend>
			
			<table class="tabladatos">
			<!-- Descripcion -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desEsp.desDesEsp.label"/>:</label></td>
				<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.desDesEsp"/></td>			
			</tr>
			
			<!-- Recurso -->
			<tr>	
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="desEspAdapterVO" property="desEsp.recurso.desRecurso"/>
				</td>					
			</tr>
			
			<!-- Tipo Deuda -->
			<tr>	
				<td><label><bean:message bundle="gde" key="gde.desEsp.tipoDeuda.label"/>: </label></td>
				<td class="normal">
					<bean:write name="desEspAdapterVO" property="desEsp.tipoDeuda.desTipoDeuda"/>
				</td>
				<!-- Via Deuda -->
				<td><label><bean:message bundle="gde" key="gde.desEsp.viaDeuda.label"/>: </label></td>
				<td class="normal">
					<bean:write name="desEspAdapterVO" property="desEsp.viaDeuda.desViaDeuda"/>
				</td>
			</tr>			
			<!-- Fecha Vto. Desde -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desEsp.fechaVtoDeudaDesde.label"/>: </label></td>
				<td class="normal">
					<bean:write name="desEspAdapterVO" property="desEsp.fechaVtoDeudaDesdeView"/>
				</td>
				<!-- Fecha Vto. Hasta -->
				<td><label><bean:message bundle="gde" key="gde.desEsp.fechaVtoDeudaHasta.label"/>: </label></td>
				<td class="normal">
					<bean:write name="desEspAdapterVO" property="desEsp.fechaVtoDeudaHastaView"/>
				</td>				
			</tr>
			
			<!-- % Desc Capital -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desEsp.porDesCap.label"/>: </label></td>
				<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.porDesCapView"/></td>
			</tr>
			
			<!-- % Desc Actualiz. -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desEsp.porDesAct.label"/>: </label></td>
				<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.porDesActView"/></td>
				<!-- % Desc interés -->
				<td><label><bean:message bundle="gde" key="gde.desEsp.porDesInt.label"/>: </label></td>
				<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.porDesIntView"/></td>
			</tr>			
			
			<!-- Leyenda -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desEsp.leyenda"/>: </label></td>
				<td class="normal"><bean:write name="desEspAdapterVO" property="desEsp.leyendaDesEsp"/></td>
			</tr>
			
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="desEspAdapterVO" property="desEsp"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->
			
				
				<tr>
					<td colspan="4"> 
						<bean:define id="modificarEncabezadoEnabled" name="desEspAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="desEspAdapterVO" property="desEsp.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- DesEsp -->
		
		<!-- ClasificDeuda -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.desEsp.listDesRecClaDeu.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="desEspAdapterVO" property="desEsp.listDesRecClaDeu">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.desRecClaDeu.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desRecClaDeu.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desRecClaDeu.desClasificacion.label"/></th>						
					</tr>
					<logic:iterate id="desRecClaDeuVO" name="desEspAdapterVO" property="desEsp.listDesRecClaDeu">
			
						<tr>					
							<!-- Modificar-->								
							<td>
								<logic:equal name="desEspAdapterVO" property="modificarClasificDeudaEnabled" value="enabled">
									<logic:equal name="desRecClaDeuVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarClasificDeuda', '<bean:write name="desRecClaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="desRecClaDeuVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="desEspAdapterVO" property="modificarClasificDeudaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="desEspAdapterVO" property="eliminarClasificDeudaEnabled" value="enabled">
									<logic:equal name="desRecClaDeuVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarClasificDeuda', '<bean:write name="desRecClaDeuVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="desRecClaDeuVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="desEspAdapterVO" property="eliminarClasificDeudaEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="desRecClaDeuVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="desRecClaDeuVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="desRecClaDeuVO" property="recClaDeu.desClaDeu" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="desEspAdapterVO" property="desEsp.listDesRecClaDeu">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
				<logic:equal name="desEspAdapterVO" property="esPlanPagos" value="false">
								
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="desEspAdapterVO" property="agregarClasificDeudaEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarClasificDeuda', '<bean:write name="desEspAdapterVO" property="desEsp.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</logic:equal> 
				<logic:equal name="desEspAdapterVO" property="esPlanPagos" value="true">
								
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="desEspAdapterVO" property="agregarClasificDeudaEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarClasificDeuda', '<bean:write name="desEspAdapterVO" property="desEsp.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar" />" disabled="true"
						/>
					</td>
				</logic:equal> 
				</tr>
			</tbody>
		</table>
		<!-- ClasificDeuda -->
				
				
		<!-- Atributos -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.desEsp.listDesAtrVal.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="desEspAdapterVO" property="desEsp.listDesAtrVal">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.atributo.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.valor.label"/></th>
					</tr>
					<logic:iterate id="desAtrValVO" name="desEspAdapterVO" property="desEsp.listDesAtrVal">
			
						<tr>					
							<!-- Modificar-->								
							<td>
								<logic:equal name="desEspAdapterVO" property="modificarDesAtrValEnabled" value="enabled">
									<logic:equal name="desAtrValVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarDesAtrVal', '<bean:write name="desAtrValVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="desAtrValVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="desEspAdapterVO" property="modificarDesAtrValEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="desEspAdapterVO" property="eliminarDesAtrValEnabled" value="enabled">
									<logic:equal name="desAtrValVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarDesAtrVal', '<bean:write name="desAtrValVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="desAtrValVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="desEspAdapterVO" property="eliminarDesAtrValEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="desAtrValVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="desAtrValVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="desAtrValVO" property="atributo.desAtributo"/>&nbsp;</td>
							<td><bean:write name="desAtrValVO" property="valor"/></td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="desEspAdapterVO" property="desEsp.listDesAtrVal">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="desEspAdapterVO" property="agregarDesAtrValEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarDesAtrVal', '<bean:write name="desEspAdapterVO" property="desEsp.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- Atributos -->


		<!-- Exenciones -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.desEsp.listDesEspExe.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="desEspAdapterVO" property="desEsp.listDesEspExe">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.desAtrVal.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.exencion.codExencion.ref"/></th>
						<th align="left"><bean:message bundle="exe" key="exe.exencion.desExencion.ref"/></th>
					</tr>
					<logic:iterate id="desEspExeVO" name="desEspAdapterVO" property="desEsp.listDesEspExe">
			
						<tr>					
							<!-- Modificar-->								
							<td>
								<logic:equal name="desEspAdapterVO" property="modificarDesEspExeEnabled" value="enabled">
									<logic:equal name="desEspExeVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarDesEspExe', '<bean:write name="desEspExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="desEspExeVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="desEspAdapterVO" property="modificarDesEspExeEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="desEspAdapterVO" property="eliminarDesEspExeEnabled" value="enabled">
									<logic:equal name="desEspExeVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarDesEspExe', '<bean:write name="desEspExeVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="desEspExeVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="desEspAdapterVO" property="eliminarDesEspExeEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="desEspExeVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="desEspExeVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="desEspExeVO" property="exencion.codExencion"/>&nbsp;</td>
							<td><bean:write name="desEspExeVO" property="exencion.desExencion"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="desEspAdapterVO" property="desEsp.listDesEspExe">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="desEspAdapterVO" property="agregarDesEspExeEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarDesEspExe', '<bean:write name="desEspAdapterVO" property="desEsp.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- Exenciones -->
		
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
