<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/cyq/AdministrarDeudaCyq.do">
	
	<h1><bean:message bundle="cyq" key="cyq.messageDeudaCyqAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- LiqDeuda -->
	<fieldset>
		<legend><bean:message bundle="cyq" key="cyq.messageDeudaCyqAdapter.fieldset.title"/></legend>
			
			<p>
	      		<label><bean:message bundle="def" key="def.recurso.label"/>:</label>
	      		<bean:write name="liqDeudaAdapterVO" property="cuenta.desRecurso"/>
			</p>
			
			<p>
				<label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>:</label>
				<bean:write name="liqDeudaAdapterVO" property="cuenta.numeroCuenta"/>
			</p>
			
			
				<!-- Mensajes y/o Advertencias -->
				<%@ include file="/base/warning.jsp" %>
				<!-- Errors  -->
				<html:errors bundle="base"/>
			
			
			
			<!-- Titulares -->
			<dl class="listabloqueSiat">
	     	   	<dt><bean:message bundle="cyq" key="cyq.messageDeudaCyqAdapter.titulares.title"/>: </dt>
				<logic:notEmpty name="liqDeudaAdapterVO" property="cuenta.listTitular" >
					<logic:iterate id="Titular" name="liqDeudaAdapterVO" property="cuenta.listTitular">
						<dd>
			      			<bean:write name="Titular" property="desTitular"/>
						</dd>
					</logic:iterate>
				</logic:notEmpty>
			</dl>
	
	
			<!-- Convenios Asociados -->
			<dl class="listabloqueSiat">
				<logic:notEmpty name="liqDeudaAdapterVO" property="cuenta.listConvenio" >
	     	   		<dt><bean:message bundle="cyq" key="cyq.messageDeudaCyqAdapter.conveniosVigentes.title"/>: </dt> 
					<!-- Convenios Vigentes -->					
					<logic:iterate id="Convenio" name="liqDeudaAdapterVO" property="cuenta.listConvenio">
						<logic:notEqual name="Convenio" property="estaCaduco" value="true">
							<dd>
								<bean:write name="Convenio" property="nroConvenio"/> -
						      	<bean:write name="Convenio" property="desPlan"/> -
						      	<bean:write name="Convenio" property="desViaDeuda"/>
							</dd>
						</logic:notEqual>
					</logic:iterate>
					
					<!-- Convenios Caducos -->
					<dt><bean:message bundle="cyq" key="cyq.messageDeudaCyqAdapter.conveniosCaducos.title"/>: </dt> 
					<logic:iterate id="Convenio" name="liqDeudaAdapterVO" property="cuenta.listConvenio">
						<logic:equal name="Convenio" property="estaCaduco" value="true">
							<dd>
								<bean:write name="Convenio" property="nroConvenio"/> -
						      	<bean:write name="Convenio" property="desPlan"/> -
						      	<bean:write name="Convenio" property="desViaDeuda"/> - 
						      	<bean:write name="Convenio" property="desEstadoConvenio"/>
							</dd>
						</logic:equal>
					</logic:iterate>
					
				</logic:notEmpty>
			</dl>		
			
		  	<div style="text-align:center">
		  		<button type="button" name="btnAceptar" onclick="submitForm('continuar', '');" class="boton">
		  			<bean:message bundle="cyq" key="cyq.messageDeudaCyqAdapter.button.continuar"/>
		  		</button>	
		  	</div>
	</fieldset>	
	<!-- LiqDeuda -->
	
	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
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