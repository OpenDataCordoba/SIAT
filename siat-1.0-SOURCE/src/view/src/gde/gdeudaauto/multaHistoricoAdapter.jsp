<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/gde/AdministrarMulta.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.multaHistorico.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<input type="button" class="boton" onclick="submitForm('volverDeHistorico', '');"
						value='<bean:message bundle="base" key="abm.button.volver"/>'/>					
				</td>
			</tr>
		</table>
		
		<!-- Multa -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.multa.title"/></legend>
		
		<table class="tabladatos">
				<tr>
					<td>
						<label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.cuenta.numeroCuenta"/>
					</td>
				</tr>
				<tr>
					<td>
						<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
					</td>
					<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.cuenta.recurso.desRecurso"/>
					</td>
				</tr>
				<logic:equal name="multaAdapterVO" property="detalleValido" value="true">
				<!-- TipoMulta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.tipoMulta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.tipoMulta.desTipoMulta"/>
					</td>
				</tr>
				<!-- Filtros si la cuenta posee esImporteManual=No -->
				<logic:equal name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="0">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaActualizacion.label"/>: </label></td>
		      		<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.fechaActualizacionView"/>
					</td>
				</tr>
				</logic:equal>
				<!-- Fin filtros si la cuenta posee esImporteManual=No -->
				<tr>
		      		<td><label><bean:message bundle="ef" key="ef.ordenControl.nroOrden.label"/>: </label></td>
		      		<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.ordenControl.numeroOrdenView"/>
					</td>
		      		<td><label><bean:message bundle="ef" key="ef.ordenControl.anioOrden.label"/>: </label></td>
		      		<td class="normal">
						<bean:write name="multaAdapterVO" property="multa.ordenControl.anioOrdenView"/>
					</td>
				</tr>
				</logic:equal>
				
				<!-- Si es detalle valido false-->
				<logic:equal name="multaAdapterVO" property="detalleValido" value="false">
					<tr>
						<td><label>(*)<bean:message bundle="gde" key="gde.tipoMulta.label"/>: </label></td>
						<td class="normal">
							<html:select name="multaAdapterVO" property="multa.tipoMulta.id" styleClass="select" onchange="submitForm('paramTipoMulta', '');">
								<html:optionsCollection name="multaAdapterVO" property="listTipoMulta" label="desTipoMulta" value="id" />
							</html:select>
						</td>
					</tr>
					<!-- Filtros si la cuenta posee esImporteManual=No -->
					<logic:equal name="multaAdapterVO" property="multa.tipoMulta.esImporteManual.id" value="0">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.fechaActualizacion.label"/>: </label></td>
			      		<td class="normal">
							<html:text name="multaAdapterVO" property="multa.fechaActualizacionView" styleId="fechaActualizacionView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaActualizacionView');" id="a_fechaActualizacionView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
					</logic:equal>
					<!-- Fin filtros si la cuenta posee esImporteManual=No -->
					
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<!-- Inclucion de CasoView -->
						<td colspan="3">
							<bean:define id="IncludedVO" name="multaAdapterVO" property="multa.ordenControl"/>
							<bean:define id="voName" value="multa.ordenControl" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>				
						</td>
					</tr>
					<tr>
			      		<td><label><bean:message bundle="gde" key="gde.multaEditAdapter.ordenControl.label"/>: </label></td>
			      		<td class="normal">
			      			<html:text name="multaAdapterVO" property="multa.ordenControl.numeroOrden" size="2" maxlength="4" styleClass="datos"/>
			      				 / <html:text name="multaAdapterVO" property="multa.ordenControl.anioOrden" size="2" maxlength="4" styleClass="datos"/>
			      				 <bean:message bundle="gde" key="gde.multaEditAdapter.mascaraPeriodo"/>
						</td>
					</tr>
				</logic:equal>
				<!-- FIN es detalle valido false -->
		</table>
		</fieldset>	
		<!-- DetAju -->
		
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.aliComFueColAdapter.agregarMasivo.legend"/></legend>
		
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<!-- Inclucion de CasoView -->
					<td class="normal">
						<bean:define id="IncludedVO" name="multaAdapterVO" property="multaHistorico"/>
						<bean:define id="voName" value="multaHistorico" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>				
					</td>
				</tr>
			</table>
			<logic:equal name="multaAdapterVO" property="esTipoRevision" value="0">
				<fieldset>
					<legend><bean:message bundle="gde" key="gde.multaHistorico.modifPorcentajes.legend"/></legend>
					<table class="tabladatos">
						<tr>	
							<td>
								<label>(*)&nbsp;<bean:message bundle="gde" key="gde.multaHistorico.porcentaje.label"/>:</label>
							</td>	
							<td class="normal"><html:text name="multaAdapterVO" property="multaHistorico.porcentajeView" size="5" maxlength="6"/></td>								
						</tr>
						<tr>					
							<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.multaHistorico.periodoDesde"/>: </label></td>
							<td class="normal">
								<html:text name="multaAdapterVO" property="multaHistorico.periodoDesdeView" size="5"/> / 
								<html:text name="multaAdapterVO" property="multaHistorico.anioDesdeView" size="8"/> (MM/YYYY)
							</td>
						</tr>
						<tr>					
							<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.multaHistorico.periodoHasta"/>: </label></td>
							<td class="normal">
								<html:text name="multaAdapterVO" property="multaHistorico.periodoHastaView" size="5"/> / 
								<html:text name="multaAdapterVO" property="multaHistorico.anioHastaView" size="8"/> (MM/YYYY)
							</td>
						</tr>
					</table>
				</fieldset>
			</logic:equal>
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.multaHistorico.modifImporte.legend"/></legend>
				<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.multaHistorico.importe.label"/>: </label></td>
						<td class="normal">
							<html:text name="multaAdapterVO" property="multaHistorico.importeTotalView" size="12"/>
						</td>
					</tr>
				</table>
			</fieldset>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.multaHistorico.observacion.label"/>: </label></td>
					<td class="normal">
						<html:textarea name="multaAdapterVO" property="multaHistorico.observacion" cols="80" rows="15">
						</html:textarea>
					</td>
				</tr>														
			</table>
		</fieldset>
				
			
		<table class="tablabotones"  width="100%">
			<tr>				
				<td align="left">
		   			<input type="button" class="boton" onclick="submitForm('volverDeHistorico', '');"
						value='<bean:message bundle="base" key="abm.button.volver"/>'/>
				</td>   	    			
				
				<td align="right">
	    			<html:button property="btnAgregar"  styleClass="boton" onclick="submitForm('agregarMultaHistorico', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
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
<!-- multaHistoricoAdapter.jsp -->