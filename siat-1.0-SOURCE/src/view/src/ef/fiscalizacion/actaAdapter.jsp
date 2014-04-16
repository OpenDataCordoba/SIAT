<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/ef/AdministrarActa.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.actaAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- Acta -->		
		<bean:define id="modificarEncabezadoEnabled" name="actaAdapterVO" property="modificarEncabezadoEnabled"/>
		<bean:define id="actaVO" name="actaAdapterVO" property="acta"/>
		<%@include file="/ef/fiscalizacion/includeActaViewAdapter.jsp" %>	
		<!-- Acta -->
		
		<!-- OrdConDoc -->		
		<logic:equal name="actaAdapterVO" property="acta.tipoActa.id" value="2">		
		<!-- documentacion para acta de procedimiento -->
		<a name="actaProcedimiento"/>
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="ef" key="ef.actaViewAdapter.actaProcedimiento.listOrdConDoc"/></caption>
	    	<tbody>
				<logic:notEmpty  name="actaAdapterVO" property="acta.listOrdConDoc">	    	
			    	<tr>
						<th width="1"> <!-- Seleccionar -->
							<input type="checkbox" onclick="changeChk('filter', 'idsOrdConDocSelected', this)"/>
						</th>
						<th width="1">&nbsp;</th>
						<th align="left"><bean:message bundle="ef" key="ef.documentacion.desDocumentacion.label"/></th>
						<th width="1"><bean:message bundle="ef" key="ef.ordConDoc.observaciones.label"/></th>
					</tr>
					
					<bean:define name="actaAdapterVO" property="ordConDoc.id" id="idOrdConDocSele"/>
					
					<logic:iterate id="OrdConDocVO" name="actaAdapterVO" property="acta.listOrdConDoc">
			
						<tr>							
							<td>
								<!-- Seleccionar -->
								<html:multibox name="actaAdapterVO" property="idsOrdConDocSelected" >
                   	            	<bean:write name="OrdConDocVO" property="idView"/>
                       	        </html:multibox>
                            </td>								

							<!-- modificar -->
							<logic:notEqual name="OrdConDocVO" property="id" value="<%=idOrdConDocSele.toString()%>">
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('irModificarObs', '<bean:write name="OrdConDocVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="ef" key="ef.actaAdapter.button.modificarObs"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>							
								</td>
							</logic:notEqual>
							<logic:equal name="OrdConDocVO" property="id" value="<%=idOrdConDocSele.toString()%>">
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarObs', '');">
										<img title="<bean:message bundle="ef" key="ef.actaAdapter.button.modificarObs"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modificando0.gif"/>
									</a>							
								</td>
							</logic:equal>
							
							<td><bean:write name="OrdConDocVO" property="documentacion.desDoc"/>&nbsp;</td>
							
							<!-- observaciones -->
							<logic:notEqual name="OrdConDocVO" property="id" value="<%=idOrdConDocSele.toString()%>">
								<td><bean:write name="OrdConDocVO" property="observaciones"/>&nbsp;</td>
							</logic:notEqual>
							<logic:equal name="OrdConDocVO" property="id" value="<%=idOrdConDocSele.toString()%>">
								<td><html:textarea name="actaAdapterVO" property="ordConDoc.observaciones"/></td>
							</logic:equal>								
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="actaAdapterVO" property="acta.listOrdConDoc">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
			</tbody>
		</table>		
		<!-- FIN documentacion para acta de procedimiento -->
		</logic:equal>
		
		<logic:notEqual name="actaAdapterVO" property="acta.tipoActa.id" value="2">
		<!-- documentacion para acta de inicio o requerimiento -->
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="ef" key="ef.actaViewAdapter.actaIniReq.listOrdConDoc"/></caption>
		    	<tbody>
					<logic:notEmpty  name="actaAdapterVO" property="acta.listOrdConDoc">	    	
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Eliminar -->
							<th align="left"><bean:message bundle="ef" key="ef.documentacion.desDocumentacion.label"/></th>
						</tr>
						<logic:iterate id="OrdConDocVO" name="actaAdapterVO" property="acta.listOrdConDoc">
				
							<tr>							
								<!-- Eliminar-->								
								<td>
									<logic:equal name="actaAdapterVO" property="eliminarOrdConDocEnabled" value="enabled">
										<logic:equal name="OrdConDocVO" property="eliminarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarOrdConDoc', '<bean:write name="OrdConDocVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="OrdConDocVO" property="eliminarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="actaAdapterVO" property="eliminarOrdConDocEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="OrdConDocVO" property="documentacion.desDoc"/>&nbsp;</td>							
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="actaAdapterVO" property="acta.listOrdConDoc">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
					<tr>
						<td align="right" colspan="10">
	   	    				<bean:define id="agregarEnabled" name="actaAdapterVO" property="agregarOrdConDocEnabled"/>
							<input type="button" <%=agregarEnabled%> class="boton" 
								onClick="submitForm('agregarOrdConDoc', '<bean:write name="actaAdapterVO" property="acta.id" bundle="base" formatKey="general.format.id"/>');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"
							/>
						</td>
					</tr>									
				</tbody>
			</table>
			<!-- FIN documentacion para acta de inicio o requerimiento -->
		</logic:notEqual>
		
		<!-- OrdConDoc -->
				
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<logic:equal name="actaAdapterVO" property="acta.tipoActa.id" value="2">
				<td align="right">
					<bean:define id="modificarEnabled" name="actaAdapterVO" property="modificarEnabled"/>
					<input type="button" class="boton" <%=modificarEnabled%> onClick="submitForm('modificar', 
					'<bean:write name="actaVO" property="ordenControl.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="ef" key="ef.actaAdapter.button.guardar"/>"/>		
		
				</td>
				</logic:equal>				   	    			
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<logic:present name="irA">
			<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
		</logic:present>		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- actaAdapter.jsp -->