<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cas/AdministrarTipoSolicitud.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="cas" key="cas.tipoSolicitudAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- TipoSolicitud -->
		<fieldset>
			<legend><bean:message bundle="cas" key="cas.tipoSolicitud.title"/></legend>
			<table class="tabladatos">				
				<tr>
				<!-- BeanwriteCodigo -->
					<td><label><bean:message bundle="cas" key="cas.tipoSolicitud.codigo.ref"/>: </label></td>
					<td class="normal"><bean:write name="tipoSolicitudAdapterVO" property="tipoSolicitud.codigo"/></td>
				<!-- BeanwriteAreaDestino -->
					<td><label><bean:message bundle="cas" key="cas.tipoSolicitud.areaDestino.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoSolicitudAdapterVO" property="tipoSolicitud.areaDestino.desArea"/></td>				
				</tr>	
				<tr>
				<!-- BeanwriteDescripcion -->
					<td><label><bean:message bundle="cas" key="cas.tipoSolicitud.descripcion.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoSolicitudAdapterVO" property="tipoSolicitud.descripcion"/></td>				
				<!-- BeanwriteFieldEstado-->
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="tipoSolicitudAdapterVO" property="tipoSolicitud.estado.value"/></td>				
				</tr>

			</table>
		</fieldset>	
		<!-- TipoSolicitud -->
	
		<!-- AreaSolicitud -->
		<logic:equal name="tipoSolicitudAdapterVO" property="act" value="ver">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="cas" key="cas.areaSolicitud.title"/></caption>
		    	<tbody>
					<logic:notEmpty  name="tipoSolicitudAdapterVO" property="tipoSolicitud.listAreaSolicitud">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->													
							<th align="left"><bean:message bundle="cas" key="cas.areaSolicitud.codigo.label"/></th>
							<th align="left"><bean:message bundle="cas" key="cas.areaSolicitud.descripcion.label"/></th>							
						</tr>
					<logic:iterate id="AreaSolicitudVO" name="tipoSolicitudAdapterVO" property="tipoSolicitud.listAreaSolicitud">			
						<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="tipoSolicitudAdapterVO" property="verAreaSolicitudEnabled" value="enabled">
									<logic:equal name="AreaSolicitudVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verAreaSolicitud', '<bean:write name="AreaSolicitudVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="AreaSolicitudVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="tipoSolicitudAdapterVO" property="verAreaSolicitudEnabled" value="enabled">										
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
								</logic:notEqual>
							</td>									

							<td><bean:write name="AreaSolicitudVO" property="areaDestino.codArea"/>&nbsp;</td>
							<td><bean:write name="AreaSolicitudVO" property="areaDestino.desArea"/>&nbsp;</td>
							
						</tr>
					</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="tipoSolicitudAdapterVO" property="tipoSolicitud.listAreaSolicitud">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		</logic:equal>
		<!-- AreaSolicitud -->
		
		<table class="tablabotones">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="left">
	   	    	    <logic:equal name="tipoSolicitudAdapterVO" property="act" value="ver">
		   	    	    <html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromAdapter', '1');">
						    <bean:message bundle="base" key="abm.button.imprimir"/>
					    </html:button>
					</logic:equal>
					<logic:equal name="tipoSolicitudAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipoSolicitudAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="tipoSolicitudAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
						</html:button>
					</logic:equal>
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	<input type="hidden" name="name"  value="<bean:write name='tipoSolicitudAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->