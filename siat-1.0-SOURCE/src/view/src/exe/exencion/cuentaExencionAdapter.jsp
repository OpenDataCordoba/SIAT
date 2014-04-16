<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/exe/AdministrarDeudaExencion.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="exe" key="exe.deudaExencionAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverAIngreso', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqDeudaAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.title"/></legend>
		<!-- Exenciones Vigentes -->
		<logic:notEmpty name="liqDeudaAdapterVO" property="exenciones.listExeVigentes">
			<dl class="listabloqueSiat">     	   
    	   		<dt><bean:message bundle="gde" key="gde.liqDeudaAdapter.exencionesVigentes.title"/>:</dt>

				<logic:iterate id="Exencion" name="liqDeudaAdapterVO" property="exenciones.listExeVigentes">
	 	   			<dd>
	 	   				<label>
	 	   					<logic:equal name="Exencion" property="selectExencionEnabled" value="true">
		   	   					<a href="/siat/exe/AdministrarDeudaExencion.do?method=seleccionarExencion&idCueExe=<bean:write name="Exencion" property="idCueExe" bundle="base" formatKey="general.format.id"/>">
			   	   					<bean:write name="Exencion" property="desExencion"/> -&nbsp;
									<bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.vigencia.label"/> &nbsp;
									<bean:write name="Exencion" property="fechaDesde"/>  -&nbsp;
									<bean:write name="Exencion" property="fechaHasta"/> &nbsp;
							    </a>
	 	   					</logic:equal>
	 	   					<logic:notEqual name="Exencion" property="selectExencionEnabled" value="true">
	 	   						<bean:write name="Exencion" property="desExencion"/> -&nbsp;
								<bean:message bundle="gde" key="gde.liqDeudaAdapter.exenciones.vigencia.label"/> &nbsp;
								<bean:write name="Exencion" property="fechaDesde"/>  -&nbsp;
								<bean:write name="Exencion" property="fechaHasta"/> &nbsp;
	 	   					</logic:notEqual>
					    </label>
					</dd>
				</logic:iterate>
			</dl>
		</logic:notEmpty>
		
		<logic:empty name="DeudaAdapterVO" property="exenciones.listExeVigentes">
			La cuenta No posee Exenciones Vigentes		
		</logic:empty>
		
	</fieldset>
	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverAIngreso', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->