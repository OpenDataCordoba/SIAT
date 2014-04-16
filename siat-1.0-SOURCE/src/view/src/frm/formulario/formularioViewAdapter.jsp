<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/frm/AdministrarFormulario.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="frm" key="frm.formularioViewAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
		<!-- Formulario -->
		<fieldset>
			<legend><bean:message bundle="frm" key="frm.formulario.title"/></legend>
			<table class="tabladatos">
				<!-- BeanwriteCodigo -->
				<tr>
					<td><label><bean:message bundle="frm" key="frm.formulario.codFormulario.label"/>: </label></td>
					<td class="normal"><bean:write name="formularioAdapterVO" property="formulario.codFormulario"/></td>
				</tr>
				<!-- BeanwriteDescripcion -->
				<tr>
					<td><label><bean:message bundle="frm" key="frm.formulario.desFormulario.label"/>: </label></td>
					<td class="normal"><bean:write name="formularioAdapterVO" property="formulario.desFormulario"/></td>
				</tr>
				<!-- BeanwritePropiedadComponente -->
				<tr>
					<td><label><bean:message bundle="frm" key="frm.desImp.ref"/>: </label></td>
					<td class="normal"><bean:write name="formularioAdapterVO" property="formulario.desImp.desDesImp" /></td>
				</tr>				
				<!-- FieldEstado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="formularioAdapterVO" property="formulario.estado.value"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- Formulario -->
	
		<!-- ForCam -->
		<logic:equal name="formularioAdapterVO" property="act" value="ver">
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="frm" key="frm.formulario.listForCam.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="formularioAdapterVO" property="formulario.listForCam">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th align="left"><bean:message bundle="frm" key="frm.forCam.fechaDesde.label"/></th>
							<th align="left"><bean:message bundle="frm" key="frm.forCam.fechaHasta.label"/></th>							
							<th align="left"><bean:message bundle="frm" key="frm.forCam.codForCam.label"/></th>
							<th align="left"><bean:message bundle="frm" key="frm.forCam.desForCam.label"/></th>
						</tr>
						<logic:iterate id="ForCamVO" name="formularioAdapterVO" property="formulario.listForCam">
				
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="ForCamVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verForCam', '<bean:write name="ForCamVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									
									<logic:notEqual name="ForCamVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</td>
								<td><bean:write name="ForCamVO" property="fechaDesdeView"/>&nbsp;</td>
								<td><bean:write name="ForCamVO" property="fechaHastaView"/>&nbsp;</td>								
								<td><bean:write name="ForCamVO" property="codForCam"/>&nbsp;</td>
								<td><bean:write name="ForCamVO" property="desForCam"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="formularioAdapterVO" property="formulario.listForCam">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		</logic:equal>
		<!-- ForCam -->
		
		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="formularioAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="formularioAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="formularioAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
