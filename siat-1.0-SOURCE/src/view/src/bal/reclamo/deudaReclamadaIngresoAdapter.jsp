<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/bal/AdministrarDeudaReclamada.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="bal" key="bal.deudaReclamadaIngresoAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverAlMenu', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- LiqDeuda -->
	<fieldset>
		<legend><bean:message bundle="bal" key="bal.deudaReclamadaIngresoAdapter.fieldset.title"/></legend>
		
			<p>
				<label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label>
				<html:select name="liqDeudaAdapterVO" property="idRecurso" styleClass="select" >
					<bean:define id="includeRecursoList" name="liqDeudaAdapterVO" property="listRecurso"/>
					<bean:define id="includeIdRecursoSelected" name="liqDeudaAdapterVO" property="idRecurso"/>
					<%@ include file="/def/gravamen/includeRecurso.jsp" %>
				</html:select>				
			</p>
			<p>
	      		<label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: 	      		
	      			<html:text name="liqDeudaAdapterVO" property="numeroCuenta" size="15" maxlength="20" styleClass="datos"/>
	      		</label>
	      		<button type="button" onclick="submitForm('buscarCuenta', '');">
	      			<bean:message bundle="bal" key="bal.deudaReclamadaIngresoAdapter.button.buscarCuenta"/>	      			
	      		</button>
			</p>
		
		  	<div style="text-align:right">
		  		<button type="button" name="btnAceptar" onclick="submitForm('ingresar', '');" class="boton">
		  			<bean:message bundle="bal" key="bal.deudaReclamadaIngresoAdapter.button.aceptar"/>
		  		</button>
		  	</div>
	</fieldset>	
	<!-- LiqDeuda -->
	
	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverAlMenu', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</html:button>

    <input type="text" style="display:none"/>	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->