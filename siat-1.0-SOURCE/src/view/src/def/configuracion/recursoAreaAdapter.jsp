<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarArea.do">

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

	<!-- Area -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.area.title"/></legend>
		<table class="tabladatos">
			<!-- Codigo -->
			<tr>
				<td><label><bean:message bundle="def" key="def.area.codArea.label"/>: </label></td>
				<td class="normal"><bean:write name="areaAdapterVO" property="area.codArea"/></td>
			</tr>
			<!-- Descricion -->
			<tr>
				<td><label><bean:message bundle="def" key="def.area.desArea.label"/>: </label></td>
				<td class="normal"><bean:write name="areaAdapterVO" property="area.desArea"/></td>
			</tr>
			<!-- Estado -->
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="areaAdapterVO" property="area.estado.value"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Area -->
		
	<!-- RecursoArea -->		
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="def" key="def.area.listRecursoArea.label"/></caption>
    	<tbody>
			<logic:notEmpty  name="areaAdapterVO" property="area.listRecursoArea">	    	
		    	<tr>
					<th width="1">&nbsp;</th> <!-- Ver -->
					<th width="1">&nbsp;</th> <!-- Modificar -->
					<th width="1">&nbsp;</th> <!-- Eliminar -->
					<th align="left"><bean:message bundle="def" key="def.recurso.label"/></th>
					<th align="left"><bean:message bundle="def" key="def.recursoArea.perCreaEmi.label"/></th>
				</tr>
				<logic:iterate id="RecursoAreaVO" name="areaAdapterVO" property="area.listRecursoArea">
						<tr>
						<!-- Ver -->
						<td>
							<logic:equal name="areaAdapterVO" property="verRecursoAreaEnabled" value="enabled">							
								<logic:equal name="RecursoAreaVO" property="verEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verRecursoArea', '<bean:write name="RecursoAreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="RecursoAreaVO" property="verEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="areaAdapterVO" property="verRecursoAreaEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec01.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Modificar-->
							<logic:equal name="areaAdapterVO" property="modificarRecursoAreaEnabled" value="enabled">
								<logic:equal name="RecursoAreaVO" property="modificarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('modificarRecursoArea', '<bean:write name="RecursoAreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.modificar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/modif0.gif"/>
									</a>
								</logic:equal>
								<logic:notEqual name="RecursoAreaVO" property="modificarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="areaAdapterVO" property="modificarRecursoAreaEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/modif1.gif"/>
							</logic:notEqual>
						</td>
						<td>
							<!-- Eliminar-->
							<logic:equal name="areaAdapterVO" property="eliminarRecursoAreaEnabled" value="enabled">
								<logic:equal name="RecursoAreaVO" property="eliminarEnabled" value="enabled">
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarRecursoArea', '<bean:write name="RecursoAreaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</logic:equal>	
								<logic:notEqual name="RecursoAreaVO" property="eliminarEnabled" value="enabled">
									<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
								</logic:notEqual>
							</logic:equal>
							<logic:notEqual name="areaAdapterVO" property="eliminarRecursoAreaEnabled" value="enabled">
								<img border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar1.gif"/>
							</logic:notEqual>
						</td>
						<td><bean:write name="RecursoAreaVO" property="recurso.desRecurso"/>&nbsp;</td>
						<td><bean:write name="RecursoAreaVO" property="perCreaEmi.value"/>&nbsp;</td>
						
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty  name="areaAdapterVO" property="area.listRecursoArea">
				<tr><td align="center">
				<bean:message bundle="base" key="base.noExistenRegitros"/>
				</td></tr>
			</logic:empty>
			<tr>					
				<td colspan="20" align="right">
	 				<bean:define id="agregarRecursoAreaEnabled" name="areaAdapterVO" property="agregarRecursoAreaEnabled"/>
					<input type="button" <%=agregarRecursoAreaEnabled%> class="boton" 
					onClick="submitForm('agregarRecursoArea', '<bean:write name="areaAdapterVO" property="area.id" bundle="base" formatKey="general.format.id"/>');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"	align="left" />
				</td>
			</tr>
		</tbody>
	</table>
	<!-- RecursoArea -->

	<table class="tablabotones" width="100%">
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
		
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
	