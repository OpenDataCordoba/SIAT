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
			
			
			
			
			<logic:equal name="tipObjImpAtrAdapterVO" property="act" value="agregar">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.atributo.codAtributo.ref"/>: </label></td>
				<td class="normal"><html:text name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.atributo.codAtributo" size="10" maxlength="20" disabled="true"/></td>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.atributo.desAtributo.ref"/>: </label></td>
				<td class="normal">
					<html:text name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.atributo.desAtributo" size="20" maxlength="100" disabled="true"/>
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('buscarAtributo', '');">
							<bean:message bundle="def" key="def.tipObjImpAtrAdapter.adm.button.buscarAtributo"/>
						</html:button>
				</td>
			</tr>
			</logic:equal>
			<logic:equal name="tipObjImpAtrAdapterVO" property="act" value="modificar">
			<tr>
				<td><label><bean:message bundle="def" key="def.atributo.codAtributo.ref"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.atributo.codAtributo"/></td>
				<td><label><bean:message bundle="def" key="def.atributo.desAtributo.ref"/>: </label></td>
				<td class="normal"><bean:write name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.atributo.desAtributo"/></td>
			</tr>
			</logic:equal>
			
			
		</table>
	</fieldset>	
	<!-- TipObjImp y Atributo -->

	<!-- TipObjImpAtr -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.tipObjImpAtr.caracteristicasGenerales.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esMultivalor.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esMultivalor.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esMultivalor.description"/>
				</li></ul></td>
			</tr>
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esRequerido.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esRequerido.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esRequerido.description"/>
				</li></ul></td>
			</tr>
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.poseeVigencia.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.poseeVigencia.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.poseeVigencia.description"/>
				</li></ul></td>
			</tr>
			
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esClave.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esClave.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esClave.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esClaveFuncional.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esClaveFuncional.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esClaveFuncional.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esCodGesCue.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esCodGesCue.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esCodGesCue.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.esDomicilioEnvio.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esDomicilioEnvio.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esDomicilioEnvio.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esUbicacion.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esUbicacion.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esUbicacion.description"/>
				</li></ul></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.valorDefecto.label"/>: </label></td>
				<td class="normal">
					<html:text name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.valorDefecto" size="20" maxlength="255"/>
				</td>
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
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esVisible.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esVisible.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esVisible.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esVisConDeu.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esVisConDeu.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esVisConDeu.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esAtributoBus.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esAtributoBus.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esAtributoBus.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esAtriBusMasiva.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esAtriBusMasiva.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esAtriBusMasiva.description"/>
				</li></ul></td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.admBusPorRan.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.admBusPorRan.id" styleClass="select" >
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id"/>
					</html:select>
				</td>
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
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.esAtributoSIAT.label"/>: </label></td>
				<td class="normal">
					<html:select name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esAtributoSIAT.id" styleClass="select" onchange="submitForm('paramEsAtributoSIAT', '');">>
						<html:optionsCollection name="tipObjImpAtrAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
				<td class="normal" colspan="2"><ul class="vinieta"><li>
					<bean:message bundle="def" key="def.tipObjImpAtr.esAtributoSIAT.description"/>
				</li></ul></td>
			</tr>
			<logic:notEqual name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esAtributoSIAT.esSI" value="false">
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImpAtr.posColInt.label"/>: </label></td>
					<td class="normal">
						<html:text name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.posColIntView" size="20" maxlength="20" disabled="true" />
					</td>
					<td class="normal" colspan="2"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.tipObjImpAtr.posColInt.description"/>
					</li></ul></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.tipObjImpAtr.posColIntHas.label"/>: </label></td>
					<td class="normal">
						<html:text name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.posColIntHasView" size="20" maxlength="20" disabled="true" />
					</td>
					<td class="normal" colspan="2"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.tipObjImpAtr.posColIntHas.description"/>
					</li></ul></td>
				</tr>
			</logic:notEqual>
			<logic:equal name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.esAtributoSIAT.esSI" value="false">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.posColInt.label"/>: </label></td>
					<td class="normal">
						<html:text name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.posColIntView" size="20" maxlength="20" disabled="false" />
					</td>
					<td class="normal" colspan="2"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.tipObjImpAtr.posColInt.description"/>
					</li></ul></td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.posColIntHas.label"/>: </label></td>
					<td class="normal">
						<html:text name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.posColIntHasView" size="20" maxlength="20" disabled="false" />
					</td>
					<td class="normal" colspan="2"><ul class="vinieta"><li>
						<bean:message bundle="def" key="def.tipObjImpAtr.posColIntHas.description"/>
					</li></ul></td>
				</tr>
			</logic:equal>
		</table>
	</fieldset>	
	<fieldset>
		<legend><bean:message bundle="def" key="def.tipObjImpAtr.vigencias.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.tipObjImpAtr.fechaDesde.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.fechaDesdeView" styleId="fechaDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
						</a>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="def" key="def.tipObjImpAtr.fechaHasta.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="tipObjImpAtrAdapterVO" property="tipObjImpAtr.fechaHastaView" styleId="fechaHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
						</a>
				</td>
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
				<logic:equal name="tipObjImpAtrAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="tipObjImpAtrAdapterVO" property="act" value="agregar">
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

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->