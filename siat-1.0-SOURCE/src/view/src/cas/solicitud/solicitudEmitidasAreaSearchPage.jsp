<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cas/BuscarSolicitud.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="cas" key="cas.solicitudSearchPage.titleEmitidasArea"/></h1>	
	
	<!-- Filtro -->
		<bean:define id="solicitudSearchPageVO" name="solicitudSearchPageVO"/>
		<%@ include file="/cas/solicitud/includeFiltroSolicitud.jsp" %>
	<!-- Fin Filtro -->	
		
	<!-- List de solicitudes Emitidas-->
		<logic:equal name="solicitudSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="solicitudSearchPageVO" property="listResult">	
				<logic:greaterThan name="solicitudSearchPageVO" property="cantResult" value="7">
					<div id="listaPendientes" class="scrolable" style="height: 545px;">
				</logic:greaterThan>						
			
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="cas" key="cas.solicitudSearchPage.listSolicitudEmitidas.title"/></caption>
	               	<tbody>							
						<logic:iterate id="SolicitudVO" name="solicitudSearchPageVO" property="listResult">
							<tr>
								<td>								
									<!-- Ver -->
										<logic:equal name="solicitudSearchPageVO" property="verEnabled" value="enabled">									
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="SolicitudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="solicitudSearchPageVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
										
									<br>
									
									<!-- Modificar-->																		
									<logic:equal name="solicitudSearchPageVO" property="modificarEnabled" value="enabled">
										<logic:equal name="SolicitudVO" property="modificarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificar', '<bean:write name="SolicitudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="SolicitudVO" property="modificarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="solicitudSearchPageVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
									
									<br>
									
									<!-- Accion cambiarEstadoEnabled -->
									<logic:equal name="solicitudSearchPageVO" property="cambiarEstadoEnabled" value="enabled">
										<logic:equal name="SolicitudVO" property="cambiarEstadoBussEnabled" value="true">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('cambiarEstado', '<bean:write name="SolicitudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="cas" key="cas.solicitudSearchPage.button.cambiarEstado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="SolicitudVO" property="cambiarEstadoBussEnabled" value="true">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="solicitudSearchPageVO" property="cambiarEstadoEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/cambiarEstado1.gif"/>
									</logic:notEqual>										
								</td>
								<td>
									<label><bean:message bundle="cas" key="cas.solicitud.numero.label"/>:</label>&nbsp;
									<bean:write name="SolicitudVO" property="idView"/>
									&nbsp;&nbsp;
									<label><bean:message bundle="cas" key="cas.estSolicitud.label"/>:</label>&nbsp;
									<bean:write name="SolicitudVO" property="estSolicitud.descripcion"/>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<label><bean:message bundle="cas" key="cas.solicitud.fechaAlta.label"/>:</label>&nbsp;
									<bean:write name="SolicitudVO" property="fechaAltaView"/>
									&nbsp;&nbsp;
									<label><bean:message bundle="cas" key="cas.solicitud.usuarioAlta.label"/>:</label>&nbsp;
									<bean:write name="SolicitudVO" property="usuarioAlta"/>
									
									<br>
									<label><bean:message bundle="cas" key="cas.tipoSolicitud.label"/>:</label>&nbsp;
									<bean:write name="SolicitudVO" property="tipoSolicitud.descripcion"/>																
									<label>&nbsp;/&nbsp;<bean:message bundle="cas" key="cas.solicitud.areaDestino.label"/>:</label>&nbsp;
									<bean:write name="SolicitudVO" property="areaDestino.desArea"/>
								
									<br>
									<label><bean:message bundle="cas" key="cas.solicitud.asuntoSolicitud.label"/>:</label>&nbsp;
									<bean:write name="SolicitudVO" property="asuntoSolicitud"/>
								
									<br>
									<label><bean:message bundle="cas" key="cas.solicitud.descripcion.label"/>:</label>&nbsp;
									<bean:write name="SolicitudVO" property="descripcionCorta"/>
								</td>
							</tr>		
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="solicitudSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
				
				<logic:greaterThan name="solicitudSearchPageVO" property="cantResult" value="7">
					</div>
				</logic:greaterThan>
			</logic:notEmpty>
			
			<logic:empty name="solicitudSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="cas" key="cas.solicitudSearchPage.listPendientes.title"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>
		</logic:equal>			
	<!-- Fin List de solicitudes Emitidas -->


	
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="getEmitidasArea" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->