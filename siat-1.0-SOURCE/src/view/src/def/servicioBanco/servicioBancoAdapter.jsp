<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarServicioBanco.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.servicioBancoAdapter.title"/></h1>
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- Servicio Banco -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.servicioBanco.title"/></legend>
			
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.codServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="servicioBancoAdapterVO" property="servicioBanco.codServicioBanco"/></td>
			
					<td><label><bean:message bundle="def" key="def.servicioBanco.desServicioBanco.ref"/>: </label></td>
					<td class="normal"><bean:write name="servicioBancoAdapterVO" property="servicioBanco.desServicioBanco"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.servicioBanco.partidaIndet.label"/>: </label></td>
					<td colspan="2" class="normal"><bean:write name="servicioBancoAdapterVO" property="servicioBanco.partidaIndet.desPartidaView"/></td>
				</tr>
			</table>
			
			<table class="tablabotones" width="100%">
				<tr>				
					<td align="right">
						<bean:define id="modificarEncabezadoEnabled" name="servicioBancoAdapterVO" property="modificarEncabezadoEnabled"/>
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="servicioBancoAdapterVO" property="servicioBanco.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
					</td>
				</tr>
			</table>
		</fieldset>	
		<!-- Servicio Banco -->
		
		<!-- SerBanRec -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.servicioBanco.listSerBanRec.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="servicioBancoAdapterVO" property="servicioBanco.listSerBanRec">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="def" key="def.serBanRec.desRecurso.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.serBanRec.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.serBanRec.fechaHasta.label"/></th>						
					</tr>
					<logic:iterate id="SerBanRecVO" name="servicioBancoAdapterVO" property="servicioBanco.listSerBanRec">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="servicioBancoAdapterVO" property="verSerBanRecEnabled" value="enabled">							
									<logic:equal name="SerBanRecVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verSerBanRec', '<bean:write name="SerBanRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="SerBanRecVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="servicioBancoAdapterVO" property="verSerBanRecEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="servicioBancoAdapterVO" property="modificarSerBanRecEnabled" value="enabled">
									<logic:equal name="SerBanRecVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarSerBanRec', '<bean:write name="SerBanRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="SerBanRecVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="servicioBancoAdapterVO" property="modificarSerBanRecEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="servicioBancoAdapterVO" property="eliminarSerBanRecEnabled" value="enabled">
									<logic:equal name="SerBanRecVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarSerBanRec', '<bean:write name="SerBanRecVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="SerBanRecVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="servicioBancoAdapterVO" property="eliminarSerBanRecEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="SerBanRecVO" property="recurso.desRecurso" />&nbsp;</td>
							<td><bean:write name="SerBanRecVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="SerBanRecVO" property="fechaHastaView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="servicioBancoAdapterVO" property="servicioBanco.listSerBanRec">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
			   	    				<bean:define id="agregarRecursoEnabled" name="servicioBancoAdapterVO" property="agregarSerBanRecEnabled"/>
						<input type="button" <%=agregarRecursoEnabled%> class="boton" 
							onClick="submitForm('agregarSerBanRec', '<bean:write name="servicioBancoAdapterVO" property="servicioBanco.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						 align="left" />
			</td>
			</tbody>
			</table>
		<!-- SerBanRec -->
		
		<!-- SerBanDesGen -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.servicioBanco.listSerBanDesGen.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="servicioBancoAdapterVO" property="servicioBanco.listSerBanDesGen">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="gde" key="gde.serBanDesGen.desDesGen.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.serBanDesGen.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.serBanDesGen.fechaHasta.label"/></th>						
					</tr>
					<logic:iterate id="SerBanDesGenVO" name="servicioBancoAdapterVO" property="servicioBanco.listSerBanDesGen">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="servicioBancoAdapterVO" property="verSerBanDesGenEnabled" value="enabled">							
									<logic:equal name="SerBanDesGenVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verSerBanDesGen', '<bean:write name="SerBanDesGenVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="SerBanDesGenVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="servicioBancoAdapterVO" property="verSerBanDesGenEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="servicioBancoAdapterVO" property="modificarSerBanDesGenEnabled" value="enabled">
									<logic:equal name="SerBanDesGenVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarSerBanDesGen', '<bean:write name="SerBanDesGenVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="SerBanDesGenVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="servicioBancoAdapterVO" property="modificarSerBanDesGenEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="servicioBancoAdapterVO" property="eliminarSerBanDesGenEnabled" value="enabled">
									<logic:equal name="SerBanDesGenVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarSerBanDesGen', '<bean:write name="SerBanDesGenVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="SerBanDesGenVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="servicioBancoAdapterVO" property="eliminarSerBanDesGenEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="SerBanDesGenVO" property="desGen.desDesGen" />&nbsp;</td>
							<td><bean:write name="SerBanDesGenVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="SerBanDesGenVO" property="fechaHastaView"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="servicioBancoAdapterVO" property="servicioBanco.listSerBanDesGen">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					

  			 <td colspan="20" align="right">
   	    				<bean:define id="agregarDesGenEnabled" name="servicioBancoAdapterVO" property="agregarSerBanDesGenEnabled"/>
						<input type="button" <%=agregarDesGenEnabled%> class="boton" 
							onClick="submitForm('agregarSerBanDesGen', '<bean:write name="servicioBancoAdapterVO" property="servicioBanco.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						align="left"/>
			</td>
			</tbody>
		</table>
		<!-- SerBanDesGen -->



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