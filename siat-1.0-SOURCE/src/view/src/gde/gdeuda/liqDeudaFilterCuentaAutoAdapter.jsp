<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqDeuda.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>
	
	<h1><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.title"/></h1>	
	
		<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>	El recurso de la cuenta es autoliquiedable, debe seleccionar lo filtros de visualizacion.</p>
			</td>				
			<td align="right">
				<logic:notEqual name="userSession" property="isAnonimoView" value="1">
		 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</logic:notEqual>
			</td>
		</tr>
	</table>
	
	<!-- LiqDeuda -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentaSeleccionada"/>: 
			<bean:write name="liqDeudaAdapterVO" property="cuenta.nroCuenta"/>
		</legend>
		
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desRecurso.label"/>:</label>
	      		<bean:write name="liqDeudaAdapterVO" property="cuenta.desRecurso"/>
			</p>

			<p>
				<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.periodo.label"/>: </label>
				<html:select name="liqDeudaAdapterVO" property="cuenta.estadoPeriodo.id" styleClass="select" style="width:25%">
					<html:optionsCollection name="liqDeudaAdapterVO" property="listEstadoPeriodo" label="value" value="id" />
				</html:select>
				&nbsp;&nbsp;&nbsp;
				<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.clasificacionDeuda.label"/>: </label>
				<html:select name="liqDeudaAdapterVO" property="cuenta.recClaDeu.id" styleClass="select">
					<html:optionsCollection name="liqDeudaAdapterVO" property="listRecClaDeu" label="desClaDeu" value="id" />
				</html:select>
			</p>
			
			<p>
				<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.fechaVtoDesde.label"/>: </label>
				<html:text name="liqDeudaAdapterVO" property="cuenta.fechaVtoDesdeView" styleId="fechaVtoDesdeView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaVtoDesdeView');" id="a_fechaVtoDesdeView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				&nbsp;
				<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.fechaVtoHasta.label"/>: </label>
				<html:text name="liqDeudaAdapterVO" property="cuenta.fechaVtoHastaView" styleId="fechaVtoHastaView" size="15" maxlength="10" styleClass="datos" />
				<a class="link_siat" onclick="return show_calendar('fechaVtoHastaView');" id="a_fechaVtoHastaView">
					<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</p>
			
			<html:hidden name="liqDeudaAdapterVO" property="cuenta.idRecurso"/>
			<html:hidden name="liqDeudaAdapterVO" property="cuenta.numeroCuenta"/>
			<html:hidden name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable"/>
			<input type="hidden" name="validAuto" value="false"/>
			
		  	<div style="text-align:right">
		  		<logic:notEqual name="userSession" property="isAnonimoView" value="1">
			  		<button type="button" name="btnAceptar" onclick="submitForm('ingresarLiqDeudaGr', '');" class="boton">
			  			<bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.button.aceptar"/>
			  		</button>
		  		</logic:notEqual>
		  		
		  		<logic:equal name="userSession" property="isAnonimoView" value="1">
		  			<button type="button" name="btnAceptar" onclick="submitForm('ingresarLiqDeudaContrFilter', '');" class="boton">
			  			<bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.button.aceptar"/>
			  		</button>
			  	</logic:equal>	
		  	</div>
	</fieldset>	
	<!-- LiqDeuda -->
	
	<logic:notEqual name="userSession" property="isAnonimoView" value="1">
		<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	  	    <bean:message bundle="base" key="abm.button.volver"/>
		</html:button>
	</logic:notEqual>	

    <input type="text" style="display:none"/>	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	
	<!-- Transporte de la bandera que indica a donde regresar -->
	<logic:present name="liqDeudaVieneDe">
		<input type="hidden" name="liqDeudaVieneDe" value="<bean:write name="liqDeudaVieneDe"/>" />
	</logic:present>
	
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>	
</html:form>
<!-- Fin formulario -->