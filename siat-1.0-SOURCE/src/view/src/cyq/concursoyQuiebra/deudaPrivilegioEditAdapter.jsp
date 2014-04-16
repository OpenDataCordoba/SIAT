<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/cyq/AdministrarDeudaPrivilegio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="deudaPrivilegioAdapterVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
	
		<h1><bean:message bundle="cyq" key="cyq.deudaPrivilegioEditAdapter.title"/></h1>	
	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DeudaPrivilegio -->
		<fieldset>
			<legend><bean:message bundle="cyq" key="cyq.deudaPrivilegio.title"/></legend>
			
			<table class="tabladatos">
				
				<!-- Procedimiento -->
				<tr>
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.label"/>: </label></td>
					<td class="normal">	
						<bean:write name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.procedimiento.numeroView"/> /
						<bean:write name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.procedimiento.anioView"/>
					</td>
				</tr>
				
				<!-- TipoPrivilegio -->
				<tr>	
					<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.tipoPrivilegio.label"/>: </label></td>
					<td class="normal">
						<html:select name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.tipoPrivilegio.id" styleClass="select">
							<html:optionsCollection name="deudaPrivilegioAdapterVO" property="listTipoPrivilegio" label="descripcion" value="id" />
						</html:select>
					</td>
				</tr>
				
				<!-- Recurso -->
				<tr>
					<logic:equal name="deudaPrivilegioAdapterVO" property="act" value="modificar">
						<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal">
							<bean:write name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.recurso.desRecurso"/>
						</td>	
					</logic:equal>
					<logic:equal name="deudaPrivilegioAdapterVO" property="act" value="agregar">
						<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
						<td class="normal">
							<bean:define id="includeRecursoList" name="deudaPrivilegioAdapterVO" property="listRecurso"/>
							<bean:define id="includeIdRecursoSelected" name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.recurso.id"/>
						
							<html:select name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.recurso.id" styleClass="select" onchange="submitForm('paramRecurso', '');" styleId="cboRecurso" style="width:90%">
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>
							
							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>
							
						</td>
					</logic:equal>
				</tr>
				
				<!-- Cuenta -->
				<tr>	
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal">
					
						<logic:notEqual name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.recurso.id" value="-1">
							<logic:notEmpty name="deudaPrivilegioAdapterVO" property="listCuenta">
								<html:select name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.cuenta.id" styleClass="select">
									<html:optionsCollection name="deudaPrivilegioAdapterVO" property="listCuenta" label="numeroCuenta" value="id" />
								</html:select>						
							</logic:notEmpty>
							
							<logic:empty name="deudaPrivilegioAdapterVO" property="listCuenta">
								<html:text name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.cuenta.numeroCuenta" size="10" maxlength="20"/>
							</logic:empty>
						</logic:notEqual>
						<logic:equal name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.recurso.id" value="-1">
							<html:select name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.cuenta.id" styleClass="select">
								<html:optionsCollection name="deudaPrivilegioAdapterVO" property="listCuenta" label="numeroCuenta" value="id" />
							</html:select>
						</logic:equal>
					</td>
				</tr>
				
				<!-- Importe -->
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="cyq" key="cyq.deudaPrivilegio.importe.label"/>: </label></td>
					<td class="normal"><html:text name="deudaPrivilegioAdapterVO" property="deudaPrivilegio.importeView" size="10" maxlength="100"/></td>			
				</tr>
				
			</table>
		</fieldset>	
		<!-- DeudaPrivilegio -->
		
		<table class="tablabotones" width="100%" >
		   	<tr>
	  	   		<td align="left" width="50%">
		   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
		   	    		<bean:message bundle="base" key="abm.button.volver"/>
		   	    	</html:button>
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="deudaPrivilegioAdapterVO" property="act" value="modificar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
							<bean:message bundle="base" key="abm.button.modificar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="deudaPrivilegioAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
							<bean:message bundle="base" key="abm.button.agregar"/>
						</html:button>
					</logic:equal>
	   	    	</td>   	    	
	   	    </tr>
	   	</table>

	</span>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- deudaPrivilegioEditAdapter.jsp -->