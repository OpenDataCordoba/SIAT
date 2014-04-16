<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarFormaPago.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rec" key="rec.formaPagoEditAdapter.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>	
	
	
	<!-- FormaPago -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.formaPago.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="formaPagoAdapterVO" property="formaPago.recurso.id" 
						onchange="submitForm('paramRecurso', '');" styleClass="select" >
						<bean:define id="includeRecursoList" name="formaPagoAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="formaPagoAdapterVO" property="formaPago.idRecurso"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
				</td>				
			</tr>

			<logic:equal name="formaPagoAdapterVO" property="act" value="modificar">
				<tr>
					<td><label><bean:message bundle="rec" key="rec.formaPago.desFormaPago.label"/>: </label></td>
					<td class="normal" colspan="3"><html:text name="formaPagoAdapterVO" property="formaPago.desFormaPago" size="68" maxlength="255"/></td>
				</tr>
			</logic:equal>

			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.formaPago.esCantCuotasFijas.label"/>: </label></td>
				<td class="normal">
					<html:select name="formaPagoAdapterVO" property="formaPago.esCantCuotasFijas.id" styleClass="select">
						<html:optionsCollection name="formaPagoAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.formaPago.cantCuotas.label"/>: </label></td>
				<td class="normal">
					<html:text name="formaPagoAdapterVO" property="formaPago.cantCuotasView" size="15" maxlength="15" />
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.formaPago.descuento.label"/>: </label></td>
				<td class="normal">
					<html:text name="formaPagoAdapterVO" property="formaPago.descuentoView" size="15" maxlength="15" />
				</td>
	
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.formaPago.interesFinanciero.label"/>: </label></td>
				<td class="normal">
					<html:text name="formaPagoAdapterVO" property="formaPago.interesFinancieroView" size="15" maxlength="15" />
				</td>
			</tr>
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.formaPago.esEspecial.label"/>: </label></td>
				<td class="normal">
					<html:select name="formaPagoAdapterVO" property="formaPago.esEspecial.id" 
						onchange="submitForm('paramEsEspecial', '');" styleClass="select">
						<html:optionsCollection name="formaPagoAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<logic:equal name="formaPagoAdapterVO" property="formaPago.exencionEnabled" value="true">					
					<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
					<td class="normal">
						<html:select name="formaPagoAdapterVO" property="formaPago.exencion.id" styleClass="select">
							<html:optionsCollection name="formaPagoAdapterVO" property="listExencion" label="desExencion" value="id" />
						</html:select>
					</td>		
				</logic:equal>
			</tr>

			<logic:equal name="formaPagoAdapterVO" property="act" value="modificar">
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="formaPagoAdapterVO" property="formaPago.estado.value"/></td>
				</tr>
			</logic:equal>

		</table>
	</fieldset>	
	<!-- FormaPago -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="formaPagoAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="formaPagoAdapterVO" property="act" value="agregar">
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
