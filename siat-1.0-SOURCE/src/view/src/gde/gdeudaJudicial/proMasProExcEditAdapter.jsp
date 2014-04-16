<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarProMasProExc.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.proMasProExcAdapter.title"/></h1>	
		
	<!-- ProcesoMasivo -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.procesoMasivo.title"/></legend>
			
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="gde" key="gde.procesoMasivo.fechaEnvio.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proMasProExcAdapterVO" property="proMasProExc.procesoMasivo.fechaEnvioView" />	</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proMasProExcAdapterVO" property="proMasProExc.procesoMasivo.recurso.desRecurso" />	</td>
			</tr>
			<!-- Inclucion de CasoView -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="proMasProExcAdapterVO" property="proMasProExc.procesoMasivo"/>
					<%@ include file="/cas/caso/includeCasoView.jsp" %>				
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.procesoMasivo.conCuentaExcSel.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="proMasProExcAdapterVO" property="proMasProExc.procesoMasivo.conCuentaExcSel.value" />	</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.procesoMasivo.observacion.label"/>: </label></td>
				<td class="normal" colspan="3">	<bean:write name="proMasProExcAdapterVO" property="proMasProExc.procesoMasivo.observacion" /> </td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.procesoMasivo.utilizaCriterio.label"/>: </label></td>
				<td class="normal" colspan="3">	<bean:write name="proMasProExcAdapterVO" property="proMasProExc.procesoMasivo.utilizaCriterio.value" /> </td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.procesoMasivo.usuarioAlta.label"/>: </label></td>
				<td class="normal">	<bean:write name="proMasProExcAdapterVO" property="proMasProExc.procesoMasivo.usuarioAlta" /> </td>
				<td><label><bean:message bundle="gde" key="gde.procesoMasivo.usrUltMdf.label"/>: </label></td>
				<td class="normal">	<bean:write name="proMasProExcAdapterVO" property="proMasProExc.procesoMasivo.usuario" /> </td>
			</tr>
			<tr>
				<td><label><bean:message bundle="pro" key="pro.estadoCorrida.proceso.label"/>: </label></td>
				<td class="normal" colspan="3">	<bean:write name="proMasProExcAdapterVO" property="proMasProExc.procesoMasivo.corrida.estadoCorrida.desEstadoCorrida" /> </td>
			</tr>								
		</table>
	</fieldset>	
	<!-- Fin ProcesoMasivo -->
	
	<!-- Procurador a Excluir -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.proMasProExc.title"/></legend>
			
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
				<td class="normal" colspan="3">	
					<html:select name="proMasProExcAdapterVO" property="proMasProExc.procurador.id" styleClass="select">
						<html:optionsCollection name="proMasProExcAdapterVO" property="listProcurador" label="descripcion" value="id" />
					</html:select>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.proMasProExc.observacion.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="proMasProExcAdapterVO" property="proMasProExc.observacion"  size="40" maxlength="255" /></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Fin Procurador a Excluir -->
	
		
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="proMasProExcAdapterVO" property="act" value="agregar">
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

</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
