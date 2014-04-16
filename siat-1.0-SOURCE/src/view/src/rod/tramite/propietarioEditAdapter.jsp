<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rod/AdministrarPropietario.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rod" key="rod.propietarioAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
		<fieldset>
		<legend><bean:message bundle="rod" key="rod.propietarios.label"/></legend>
		
		<table class="tabladatos">
		
			<!-- C -->
			<!-- C-DATOS DEL PROPIETARIO -->
			
	<!-- Propietarios -->		
					
			<!-- Apellido o Razon -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rod" key="rod.tramiteRA.CApellidoORazon.label"/>: </label></td>
				<td><html:text name="propietarioAdapterVO" property="propietario.apellidoORazon" size="30" maxlength="100"/></td>
							
			</tr>
			<!-- Tipo Doc -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rod" key="rod.tramiteRA.CTipoDoc.label"/>: </label></td>
				<td class="normal">
					<html:select name="propietarioAdapterVO" property="propietario.codTipoDoc" styleClass="select" onchange="submitForm('paramTipoDoc', '');">
						<html:optionsCollection name="propietarioAdapterVO" property="listTipoDoc" label="tipoDocView" value="codTipoDoc" />
					</html:select>
				</td>
			
				<td><label>(*)&nbsp;<bean:message bundle="rod" key="rod.tramiteRA.CNroDoc.label"/>: </label></td>
				<td class="normal"><html:text name="propietarioAdapterVO" property="propietario.nroDocView" size="10" maxlength="100"/></td>			
			</tr>
			<tr>
				<!-- Numero Ingreso Bruto -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CNroIB.label"/>: </label></td>
				<td class="normal"><html:text name="propietarioAdapterVO" property="propietario.nroIBView" size="10" maxlength="100"/></td>
				
				
			<!-- Número productor agropec. -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CNroProdAgr.label"/>: </label></td>
				<td class="normal"><html:text name="propietarioAdapterVO" property="propietario.nroProdAgrView" size="10" maxlength="100"/></td>			
						
			</tr>
			<!-- cuit -->
			<tr>
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CNroCuit.label"/>: </label></td>
				<td class="normal"><html:text name="propietarioAdapterVO" property="propietario.nroCuit" size="10" maxlength="100"/></td>			
			
			
			<!-- Cod Tipo Prop. -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CCodTipoProp.label"/>: </label></td>
				<td class="normal">
					<html:select name="propietarioAdapterVO" property="propietario.codTipoProp" styleClass="select" onchange="submitForm('paramTipoProp', '');">
						<html:optionsCollection name="propietarioAdapterVO" property="listTipoPropietario" label="tipoPropView" value="codTipoProp" />
					</html:select>
				</td>			
			</tr>
			<tr>
			<!-- Fecha Nac. -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CFechaNac.label"/>: </label></td>
				<td class="normal">
					<html:text name="propietarioAdapterVO" property="propietario.fechaNacView" styleId="fechaNacView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaNacView');" id="a_fechaNacView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>	
			</tr>
			<tr>
			<!-- Cod Estado Civil -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CCodEstCiv.label"/>: </label></td>
				<td class="normal">
					<html:select name="propietarioAdapterVO" property="propietario.codEstCiv" styleClass="select" onchange="submitForm('paramEstadoCivil', '');">
						<html:optionsCollection name="propietarioAdapterVO" property="listEstadoCivil" label="estadoCivilView" value="codEstCiv" />
					</html:select>
				</td>
				
			
			<!-- Cod Sexo -->
			
				<td><label><bean:message bundle="rod" key="rod.tramiteRA.CCodSexo.label"/>: </label></td>
				<td class="normal">
					<html:select name="propietarioAdapterVO" property="propietario.sexo.id" styleClass="select">
						<html:optionsCollection name="propietarioAdapterVO" property="listSexo" label="value" value="id" />
					</html:select>
				</td>
			
			
			</tr>
		</table>
	</fieldset>
		
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	
			<td align="right" width="50%">
				<html:button property="btnValidarPersona"  styleClass="boton" onclick="submitForm('validarPersona', '');">
					<bean:message bundle="rod" key="rod.personaEditAdapter.button.validarPersona"/>
				</html:button>
			</td>					
   	    	<td align="right" width="50%">
				<logic:equal name="propietarioAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="propietarioAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->