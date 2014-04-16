<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarPartida.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.partidaAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		<!-- Partida -->
	<fieldset><legend><bean:message bundle="bal"
		key="bal.partida.title" /></legend>

	<table class="tabladatos">
		<!-- TextCodigo -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.partida.codPartida.label" />: </label></td>
			<td class="normal">
				<bean:write name="partidaAdapterVO" property="partida.codPartida" />
			</td>
		</tr>
		<!-- Descripcion -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.partida.desPartida.label" />: </label></td>
			<td class="normal"><bean:write name="partidaAdapterVO" property="partida.desPartida"/></td>
		</tr>
		<!-- preEjeAct -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.partida.preEjeAct.label" />: </label></td>
			<td class="normal"><bean:write name="partidaAdapterVO" property="partida.preEjeAct"/></td>
		</tr>
		<!-- preEjeVen -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.partida.preEjeVen.label" />: </label></td>
			<td class="normal"><bean:write name="partidaAdapterVO" property="partida.preEjeVen"/></td>
		</tr>
    	<tr>
			<td colspan="4"><bean:define id="modificarEncabezadoEnabled" name="partidaAdapterVO" property="modificarEncabezadoEnabled"/> <input
				type="button" class="boton" <%=modificarEncabezadoEnabled%>
				onClick="submitForm('modificarEncabezado', 
							'<bean:write name="partidaAdapterVO" property="partida.id" bundle="base" formatKey="general.format.id"/>');"
				value="<bean:message bundle="base" key="abm.button.modificar"/>" />
			</td>
		</tr>
	</table>
	</fieldset>
	<!-- Partida -->
		
		<!-- Partida_Detalle -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.partida.listParCueBan.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="partidaAdapterVO" property="partida.listParCueBan">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="bal" key="bal.parCueBan.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.parCueBan.fechaHasta.label"/></th>
						<th align="left"><bean:message bundle="bal" key="bal.parCueBan.nroCuenta.label"/></th>	
					</tr>
					<logic:iterate id="ParCueBanVO" name="partidaAdapterVO" property="partida.listParCueBan">
			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="partidaAdapterVO" property="verParCueBanEnabled" value="enabled">
									<logic:equal name="ParCueBanVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verParCueBan', '<bean:write name="ParCueBanVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ParCueBanVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="partidaAdapterVO" property="verParCueBanEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Modificar-->								
							<td>
								<logic:equal name="partidaAdapterVO" property="modificarParCueBanEnabled" value="enabled">
									<logic:equal name="ParCueBanVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarParCueBan', '<bean:write name="ParCueBanVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ParCueBanVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="partidaAdapterVO" property="modificarParCueBanEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							
							<!-- Eliminar-->								
							<td>
								<logic:equal name="partidaAdapterVO" property="eliminarParCueBanEnabled" value="enabled">
									<logic:equal name="ParCueBanVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarParCueBan', '<bean:write name="ParCueBanVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="ParCueBanVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="partidaAdapterVO" property="eliminarParCueBanEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="ParCueBanVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="ParCueBanVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="ParCueBanVO" property="cuentaBanco.nroCuenta"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="partidaAdapterVO" property="partida.listParCueBan">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>
				<tr>
					<td align="right" colspan="10">
   	    				<bean:define id="agregarEnabled" name="partidaAdapterVO" property="agregarParCueBanEnabled"/>
						<input type="button" <%=agregarEnabled%> class="boton" 
							onClick="submitForm('agregarParCueBan', '<bean:write name="partidaAdapterVO" property="partida.id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.agregar"/>"
						/>
					</td>
				</tr>
			</tbody>
		</table>
		<!-- Partida_Detalle -->
				
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
