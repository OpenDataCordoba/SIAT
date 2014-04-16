<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarTipObjImpAtr.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="def" key="def.tipObjImpAtrAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- TipObjImp y Atributo -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.tipObjImpAtr.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImp.codTipObjImp.ref"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.tipObjImp.codTipObjImp"/></td>
				<td><label><bean:message bundle="def" key="def.tipObjImp.desTipObjImp.ref"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.tipObjImp.desTipObjImp"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.atributo.codAtributo.ref"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.atributo.codAtributo"/></td>
				<td><label><bean:message bundle="def" key="def.atributo.desAtributo.ref"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.atributo.desAtributo"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- TipObjImp y Atributo -->

	<!-- TipObjImpAtr -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.tipObjImpAtr.caracteristicasGenerales.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esMultivalor.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esMultivalor.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esMultivalor.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.poseeVigencia.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.poseeVigencia.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.poseeVigencia.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esClave.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esClave.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esClave.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esClaveFuncional.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esClaveFuncional.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esClaveFuncional.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esCodGesCue.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esCodGesCue.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esCodGesCue.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esDomicilioEnvio.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esDomicilioEnvio.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esDomicilioEnvio.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esUbicacion.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esUbicacion.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esUbicacion.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.valorDefecto.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.valorDefecto"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.valorDefecto.description"/>
				</li></ul></td>
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend><bean:message bundle="def" key="def.tipObjImpAtr.caracteristicasVisBusq.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esVisible.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esVisible.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esVisible.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esVisConDeu.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esVisConDeu.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esVisConDeu.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esAtributoBus.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esAtributoBus.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esAtributoBus.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esAtriBusMasiva.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esAtriBusMasiva.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esAtriBusMasiva.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.admBusPorRan.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.admBusPorRan.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.admBusPorRan.description"/>
				</li></ul></td>
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend><bean:message bundle="def" key="def.tipObjImpAtr.caracteristicasActualizacion.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esAtributoSIAT.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esAtributoSIAT.value"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esAtributoSIAT.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.posColInt.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.posColIntView"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.posColInt.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.posColIntHas.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.posColIntHasView"/></td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.posColIntHas.description"/>
				</li></ul></td>
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend><bean:message bundle="def" key="def.tipObjImpAtr.vigencias.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.fechaDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.fechaDesdeView"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.fechaHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.fechaHastaView"/></td>				
			</tr>
		</table>
	</fieldset>	
	<!-- TipObjImpAtr -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="tipObjImpAtrAdapterVO" property="act" value="eliminar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
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