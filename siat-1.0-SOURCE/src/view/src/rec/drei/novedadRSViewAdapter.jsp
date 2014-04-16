<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarNovedadRS.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="rec" key="rec.novedadRSAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>	
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- Datos -->
	<logic:notEqual name="novedadRSAdapterVO" property="act" value="aplicarMasivo">
		<fieldset>
		<legend><bean:message bundle="rec" key="rec.novedadRS.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.nroCuenta" />
					</td>
					<td><label><bean:message bundle="rec" key="rec.tipoTramiteRS.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.tipoTramiteRS.desTipoTramite"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.cuit.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.cuit"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.fechaTransaccion.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.fechaTransaccionView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.tipoCont.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.tipoContView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.desCont.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.desCont"/>
					</td>				
				</tr>
				<tr>	
					<td><label><bean:message bundle="rec" key="rec.novedadRS.isib.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.isib"/>
					</td>	
					<td><label><bean:message bundle="rec" key="rec.novedadRS.domLocal.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.domLocal"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.telefono.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.telefono"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.email.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.email"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRSAdapter.fechaVigencia.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.fechaVigenciaView"/>
					</td>				
					<td><label><bean:message bundle="rec" key="rec.novedadRS.listActividades.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.listActividades"/>
					</td>
				
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.ingBruAnu.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.ingBruAnuView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.supAfe.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.supAfeView"/>
					</td>
				</tr>	
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.canPer.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.canPerView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.precioUnitario.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.precioUnitarioView"/>
					</td>				
				</tr>		
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.publicidad.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.publicidadView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.desPublicidad.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.desPublicidad"/>
					</td>
				</tr>	
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.adicEtur.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.adicEturView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.desEtur.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.desEtur"/>
					</td>

				</tr>	
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.redHabSoc.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.redHabSocView"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.nroCategoria.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.nroCategoriaView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.desCategoria.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.desCategoria"/>
					</td>
				</tr>
				<tr>					
					<td><label><bean:message bundle="rec" key="rec.novedadRS.importeDrei.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.importeDreiView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.cuna.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.cuna"/>
					</td>					
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.importeAdicional.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.importeAdicionalView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.codBar.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.codBar"/>
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.importeEtur.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.importeEturView"/>
					</td>				
					<td><label><bean:message bundle="rec" key="rec.novedadRS.importeTotal.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.importeTotalView"/>
					</td>				
				</tr>	
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.motivoBaja.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.motivoBajaView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.desBaja.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.desBaja"/>
					</td>
				</tr>				
				<tr>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.tipoUsuario.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.tipoUsuarioView"/>
					</td>
					<td><label><bean:message bundle="rec" key="rec.novedadRS.usrCliente.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.usrCliente"/>
					</td>
				</tr>
				<tr>				
				<td><label><bean:message bundle="rec" key="rec.novedadRS.codBarComprimido.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="novedadRS.codBarComprimido"/>
					</td>				
				</tr>				
				
			</table>
		</fieldset>	
	</logic:notEqual>
	<logic:equal name="novedadRSAdapterVO" property="act" value="aplicarMasivo">
		<!-- Proceso -->
		<fieldset>
			<legend><bean:message bundle="pro" key="pro.proceso.title"/></legend>
			<table class="tabladatos">	
				<tr>
					<!-- Descripcion -->
					<td><label><bean:message bundle="pro" key="pro.proceso.desProceso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="novedadRSAdapterVO" property="proceso.desProceso"/></td>
				</tr>
				<tr>
					<!-- TipoEjecucion -->
					<td><label><bean:message bundle="pro" key="pro.proceso.tipoEjecucion.label"/>: </label></td>
					<td class="normal">
						<bean:write name="novedadRSAdapterVO" property="proceso.tipoEjecucion.desTipoEjecucion"/>
					</td>
					<logic:equal name="novedadRSAdapterVO" property="paramPeriodic" value="true">
						<!-- CronExpression -->
						<td><label>&nbsp;<bean:message bundle="pro" key="pro.proceso.cronExpression.label"/>: </label></td>
						<td class="normal">
							<bean:write name="novedadRSAdapterVO" property="proceso.cronExpression"/>
						</td>		
					</logic:equal>
				</tr>

			</table>
		</fieldset>	
		<!-- Proceso -->
		
	<!-- Filtros -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="rec" key="rec.tipoTramiteRS.label"/>: </label></td>
				<td class="normal">
					<html:select name="novedadRSAdapterVO" property="novedadRS.tipoTramiteRS.id" styleClass="select">
						<html:optionsCollection name="novedadRSAdapterVO" property="listTipoTramiteRS" label="desTipoTramite" value="id"/>
					</html:select>
				</td>
			</tr>		
			<tr>
				<td><label><bean:message bundle="rec" key="rec.novedadRSAdapter.fechaNovedadDesde.label"/>: </label></td>
				<td class="normal"><html:text name="novedadRSAdapterVO" property="fechaNovedadDesdeView" size="12" styleId="fechaDesdeView"/>
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="rec" key="rec.novedadRSAdapter.fechaNovedadHasta.label"/>: </label></td>
				<td class="normal"><html:text name="novedadRSAdapterVO" property="fechaNovedadHastaView" size="12" styleId="fechaHastaView"/>
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- Fin Filtro -->
	</logic:equal>
		

	<logic:notEqual name="novedadRSAdapterVO" property="act" value="aplicarMasivo">
		<!-- MsgDeuda -->
		<fieldset>
			<legend><bean:message bundle="rec" key="rec.novedadRS.msgDeuda.label"/></legend>
				<!-- Msg Deuda -->
				<p align="left">
	                 <html:textarea style="width:610px; height:270px; font-family: monospace; font-size: 8pt; color:grey;" name="novedadRSAdapterVO" property="novedadRS.msgDeuda" readonly="true"></html:textarea>
	            </p>
		</fieldset>
	</logic:notEqual>
	
	<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<logic:equal name="novedadRSAdapterVO" property="act" value="aplicarMasivo">
		   	    	<td align="right" width="50%">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('aplicarMasivo', '');">
								<bean:message bundle="rec" key="rec.novedadRSSearchPage.button.aplicarMasivo"/>
							</html:button>
			    	</td>
				</logic:equal>
 			</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
