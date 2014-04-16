<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
  	    <%@include file="/base/calendar.js"%>   
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/def/AdministrarRecurso.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="def" key="def.recursoAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
			
		<!-- Recurso -->

		<!-- Caracteristicas Principales -->
		<fieldset>
			<legend><bean:message bundle="def" key="def.recursoAdapter.fieldset1"/></legend>
			
			<table>
				<tr>
					<!-- Categoria -->										
					<td align="right"><label><bean:message bundle="def" key="def.categoria.label"/>:</label></td>
					<td colspan="2" class="normal"><bean:write name="recursoAdapterVO" property="recurso.categoria.desCategoria"/></td>
				</tr>
				<tr>
					<!-- Codigo -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.codRecurso.label"/>:</label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.codRecurso"/></td>
				</tr>					
				<tr>
					<!-- Descripcion -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.desRecurso.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.desRecurso"/></td>					
				</tr>
				<tr>
					<!-- FechaAlta -->
					<td align="right"><label><bean:message bundle="def" key="def.recurso.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="recursoAdapterVO" property="recurso.fechaAltaView"/></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Caracteristicas Principales -->
		<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<bean:define id="modificarEncabezadoEnabled" name="recursoAdapterVO" property="modificarEncabezadoEnabled"/>
					<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
						'<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>
				</td>
			</tr>
		</table>

		<!-- Fin Recurso -->
		
		
		<!-- RecCon -->		
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="def" key="def.recurso.listRecCon.ref"/></caption>
	    	<tbody>
				<logic:notEmpty  name="recursoAdapterVO" property="recurso.listRecCon">	    	
			    	<tr>
						<th width="1">&nbsp;</th> <!-- Ver -->
						<th width="1">&nbsp;</th> <!-- Modificar -->
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="def" key="def.recCon.fechaDesde.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recCon.fechaHasta.label"/></th>						
						<th align="left"><bean:message bundle="def" key="def.recCon.codRecCon.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recCon.desRecCon.label"/></th>
						<th align="left"><bean:message bundle="def" key="def.recCon.abrRecCon.label"/></th>
					</tr>
					<logic:iterate id="RecConVO" name="recursoAdapterVO" property="recurso.listRecCon">
							<tr>
							<!-- Ver -->
							<td>
								<logic:equal name="recursoAdapterVO" property="verRecConEnabled" value="enabled">							
									<logic:equal name="RecConVO" property="verEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecCon', '<bean:write name="RecConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecConVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="verRecConEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Modificar-->
								<logic:equal name="recursoAdapterVO" property="modificarRecConEnabled" value="enabled">
									<logic:equal name="RecConVO" property="modificarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecCon', '<bean:write name="RecConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="RecConVO" property="modificarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="modificarRecConEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</td>
							<td>
								<!-- Eliminar-->
								<logic:equal name="recursoAdapterVO" property="eliminarRecConEnabled" value="enabled">
									<logic:equal name="RecConVO" property="eliminarEnabled" value="enabled">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecCon', '<bean:write name="RecConVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
										</a>
									</logic:equal>	
									<logic:notEqual name="RecConVO" property="eliminarEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
									</logic:notEqual>
								</logic:equal>
								<logic:notEqual name="recursoAdapterVO" property="eliminarRecConEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</td>
							<td><bean:write name="RecConVO" property="fechaDesdeView"/>&nbsp;</td>
							<td><bean:write name="RecConVO" property="fechaHastaView"/>&nbsp;</td>
							<td><bean:write name="RecConVO" property="codRecCon" />&nbsp;</td>
							<td><bean:write name="RecConVO" property="desRecCon" />&nbsp;</td>
							<td><bean:write name="RecConVO" property="abrRecCon" />&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="recursoAdapterVO" property="recurso.listRecCon">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			<td colspan="20" align="right">
  				<bean:define id="agregarRecursoEnabled" name="recursoAdapterVO" property="agregarRecConEnabled"/>
				<input type="button" <%=agregarRecursoEnabled%> class="boton" 
					onClick="submitForm('agregarRecCon', '<bean:write name="recursoAdapterVO" property="recurso.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
			</td>
			</tbody>
			</table>
		<!-- RecCon -->	
		
			
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