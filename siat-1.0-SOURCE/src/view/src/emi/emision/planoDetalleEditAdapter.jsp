<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarPlanoDetalle.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.title"/></h1>	

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
		<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>
		
		<table class="tabladatos">
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.planoDetalleAdapter.solicitudInmueble.label"/>: </label></td>														   
				<td class="normal">
					<html:select name="planoDetalleAdapterVO" property="planoDetalle.tipoSolicitud.id" styleClass="select">
						<html:optionsCollection name="planoDetalleAdapterVO" property="listSolicitudInmueble" label="desSolicitudInmueble" value="id" />
					</html:select>
				</td>					
			</tr>

			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.planoDetalleAdapter.categoriaInmueble.label"/>: </label></td>														   
				<td class="normal">
					<html:select name="planoDetalleAdapterVO" property="planoDetalle.catInm.id" styleClass="select">
						<html:optionsCollection name="planoDetalleAdapterVO" property="listCategoriaInmueble" label="desCategoriaInmueble" value="id" />
					</html:select>
				</td>					
			</tr>

			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.planoDetalleAdapter.supEdificar.label"/>: </label></td>
				<td class="normal"><html:text name="planoDetalleAdapterVO" property="planoDetalle.supEdifView" size="15" maxlength="15"/></td>			
			</tr>

			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.planoDetalleAdapter.supFinal.label"/>: </label></td>														   
				<td class="normal">
					<html:select name="planoDetalleAdapterVO" property="planoDetalle.supFinal.id" styleClass="select">
						<html:optionsCollection name="planoDetalleAdapterVO" property="listSuperficieInmueble" label="desSuperficieInmueble" value="id" />
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
				<logic:equal name="planoDetalleAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>
   	    	</td>   	    	
   	    </tr>
   	</table>
	
	<input type="text" style="display:none"/>
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- planoDetalleEditAdapter.jsp -->