<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/AdministrarSaldoAFavor.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.saldoAFavorEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- SaldoAFavor -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.saldoAFavor.title"/></legend>
		
		<table class="tabladatos">
		<!-- area -->
		<tr>	
			<td><label><bean:message bundle="bal" key="bal.saldoAFavor.area.label"/>: </label></td>
			<td class="normal">
				<html:select name="saldoAFavorAdapterVO" property="saldoAFavor.area.id" styleClass="select">
					<html:optionsCollection name="saldoAFavorAdapterVO" property="listArea" label="desArea" value="id" />
				</html:select>
			</td>					
		</tr>

		<!-- Recurso -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal">
					<html:select name="saldoAFavorAdapterVO" property="saldoAFavor.cuenta.recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="saldoAFavorAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="saldoAFavorAdapterVO" property="saldoAFavor.cuenta.recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>
		</tr>
		 <!-- Cuenta -->
	    <tr>
			<td><label>(*)&nbsp;<bean:message bundle="bal" key="bal.saldoAFavor.cuenta.label"/>: </label></td>
			<td class="normal"><html:text name="saldoAFavorAdapterVO" property="saldoAFavor.cuenta.numeroCuenta" size="20" maxlength="100"/></td>			
		</tr>
		<!-- fechaGeneracion -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.saldoAFavor.fechaGeneracion.label"/>: </label></td>
			<td class="normal">
				<html:text name="saldoAFavorAdapterVO" property="saldoAFavor.fechaGeneracionView" styleId="fechaGeneracionView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaGeneracionView');" id="a_fechaGeneracionView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>
		</tr>
		<!-- descripcion -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.saldoAFavor.descripcion.label"/>: </label></td>
			<td class="normal"><html:text name="saldoAFavorAdapterVO" property="saldoAFavor.descripcion" size="20" maxlength="100"/></td>			
		</tr>
		<!-- importe -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.saldoAFavor.importe.label"/>: </label></td>
			<td class="normal"><html:text name="saldoAFavorAdapterVO" property="saldoAFavor.importeView" size="20" maxlength="100"/></td>			
		</tr>

		<!-- Cuenta Banco -->
		<tr>	
			<td><label><bean:message bundle="bal" key="bal.saldoAFavor.cuentaBanco.label"/>: </label></td>
			<td class="normal">
				<html:select name="saldoAFavorAdapterVO" property="saldoAFavor.cuentaBanco.id" styleClass="select">
					<html:optionsCollection name="saldoAFavorAdapterVO" property="listCuentaBanco" label="desCuentaBanco" value="id" />
				</html:select>
			</td>					
		</tr>

		
		<!-- nro comp -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.saldoAFavor.nroComprobante.label"/>: </label></td>
			<td class="normal"><html:text name="saldoAFavorAdapterVO" property="saldoAFavor.nroComprobante" size="20" maxlength="100"/></td>			
		</tr>
		<!-- des comp -->
		<tr>
			<td><label><bean:message bundle="bal" key="bal.saldoAFavor.desComprobante.label"/>: </label></td>
			<td class="normal"><html:text name="saldoAFavorAdapterVO" property="saldoAFavor.desComprobante" size="20" maxlength="100"/></td>			
		</tr>

		<!-- Caso -->
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="saldoAFavorAdapterVO" property="saldoAFavor"/>
					<bean:define id="voName" value="saldoAFavor" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>				
				</td>
			</tr>
			<!-- Fin Inclucion de CasoView -->
	
		</table>
	</fieldset>	
	<!-- SaldoAFavor -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="saldoAFavorAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="saldoAFavorAdapterVO" property="act" value="agregar">
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
