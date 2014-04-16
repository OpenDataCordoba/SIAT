<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarCierreComercio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.cierreComercio.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="cierreComercioAdapterVO" property="idCuenta" bundle="base" formatKey="general.format.id"/>');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.cierreComercio.title"/></legend>
	<!-- Atributos del Objeto Imponible -->
			<logic:notEmpty name="cierreComercioAdapterVO" property="listAtributoObjImp" >
				<logic:iterate id="AtrVal" name="cierreComercioAdapterVO" property="listAtributoObjImp">
					<p>
			      		<label><bean:write name="AtrVal" property="label"/>:</label>
			      		<bean:write name="AtrVal" property="value"/>
					</p>
				</logic:iterate>
			</logic:notEmpty>
	<!-- Objeto Imponible -->
	</fieldset>
	
	<!-- Deuda En Gestion Administrativa, un Bloque por Cuenta -->	
	<fieldset>
		<legend> 
			<a onclick="toggle(this, 'bloqueDeudaAdmin')" style="cursor: pointer;"> (-) &nbsp; </a> 
			<bean:message bundle="gde" key="gde.cierreComercioAdapter.blockAdmin.title"/>				
		</legend>

		<span id="bloqueDeudaAdmin" style="display:block">
						
	<table class="tramonline" border="1" cellpadding="0" cellspacing="1" width="100%">
	    <caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.title"/></caption>
	</table>
	
	<logic:notEmpty name="cierreComercioAdapterVO" property="listGestionDeudaAdmin">
		<div class="scrolable">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<!--caption><bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.title"/></caption-->
	      	<tbody>
	      	<!-- Item LiqDeudaAdminVO -->
	       	<logic:iterate id="GDeudaAdmin" name="cierreComercioAdapterVO" property="listGestionDeudaAdmin">
		       	
		       	<tr>
		       		<td colspan="6">
		       			<b><bean:message bundle="def" key="def.recurso.label"/>:</b> 
		       			<bean:write name="GDeudaAdmin" property="desRecurso"/> &nbsp;&nbsp;&nbsp;&nbsp;
		       			<b><bean:message bundle="pad" key="pad.cuenta.label"/>:</b> 
		       			<bean:write name="GDeudaAdmin" property="numeroCuenta"/>
		       		</td>
		       	</tr>
		       	
		       	<tr>		       		
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.periodoDeuda"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.fechaVto"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.saldo"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.actualizacion"/></th>
				  	<th align="left"><bean:message bundle="gde" key="gde.liqDeudaAdapter.liqDeuda.total"/></th>

				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaAdmin" name="GDeudaAdmin" property="listDeuda">
					<tr>			  			
			  			<td><bean:write name="LiqDeudaAdmin" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaAdmin" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaAdmin" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaAdmin" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqDeudaAdmin" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<!-- Fin Item LiqDeudaVO -->
		       	<tr>       		
		       		<td colspan="7" class="celdatotales" align="right">
			        	<bean:message bundle="gde" key="gde.liqDeudaAdapter.blockAdmin.subtotal.label"/>: 
			        	<b><bean:write name="GDeudaAdmin" property="subTotal" bundle="base" formatKey="general.format.currency"/></b>
			        </td>
		       	</tr>	       	
	       	</logic:iterate>
	       	<!-- Fin LiqDeudaAdminVO -->	       	
	      	</tbody>
		</table>
		</div>
	</logic:notEmpty>
	</fieldset>
	
	<fieldset>
		<legend>  
			<bean:message bundle="gde" key="gde.cierreComercioAdapter.title"/>				
		</legend>
		
		<table class="tabladatos">
				<logic:equal name="cierreComercioAdapterVO" property="permiteIniCierreCom" value="true">			
					<tr>
						<td><label><bean:message bundle="gde" key="gde.cierreComercio.fechaCeseActividad.label"/>: </label></td>
						<td class="normal">
							<html:text name="cierreComercioAdapterVO" property="cierreComercio.fechaCeseActividadView" styleId="fechaCeseActividadView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaCeseActividadView');" id="a_fechaCeseActividadView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>
					</tr>
				
					<!-- Combo Motivo Cierre-->
					<tr>	
						<td><label><bean:message bundle="gde" key="gde.motivoCierre.label"/>: </label></td>
						<td class="normal">
							<html:select name="cierreComercioAdapterVO" property="cierreComercio.motivoCierre.id" styleClass="select" onchange="submitForm('paramMotivoCierre', '');">
								<html:optionsCollection name="cierreComercioAdapterVO" property="listMotivoCierre" label="desMotivo" value="id" />
							</html:select>
						</td>					
					</tr>

					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<!-- Inclucion de CasoView -->
						<td colspan="3">
							<bean:define id="IncludedVO" name="cierreComercioAdapterVO" property="cierreComercio"/>
							<bean:define id="voName" value="cierreComercio" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>				
						</td>
					</tr>
					<!-- Filtros si el motivo es fallecimiento de titular -->
					<logic:equal name="cierreComercioAdapterVO" property="cierreComercio.motivoCierre.id" value="1">
						<tr>
							<td><label><bean:message bundle="gde" key="gde.cierreComercio.fechaFallecimientoTitular.label"/>: </label></td>
				      		<td class="normal">
								<html:text name="cierreComercioAdapterVO" property="cierreComercio.fechaFallecimientoView" styleId="fechaFallecimientoView" size="15" maxlength="10" styleClass="datos" />
								<a class="link_siat" onclick="return show_calendar('fechaFallecimientoView');" id="a_fechaFallecimientoView">
									<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
							</td>
						</tr>
					</logic:equal>
						<!-- Agregar Cierre Comercio -->
						<table class="tablabotones" width="100%">
							<tr>
								<td align="right">
									<logic:empty name="cierreComercioAdapterVO" property="cierreComercio.id">
							 			<button type="button" name="btnAgregar" class="boton" onclick="submitForm('agregarCierreComercio', '<bean:write name="cierreComercioAdapterVO" property="idCuenta" bundle="base" formatKey="general.format.id"/>');">
									  	    <bean:message bundle="base" key="abm.button.agregar"/>
										</button>
									</logic:empty>
									<logic:notEmpty name="cierreComercioAdapterVO" property="cierreComercio.id">
							 			<button type="button" name="btnModificar" class="boton" onclick="submitForm('modificarCierreComercio', '<bean:write name="cierreComercioAdapterVO" property="idCuenta" bundle="base" formatKey="general.format.id"/>');">
									  	    <bean:message bundle="base" key="abm.button.modificar"/>
										</button>
									</logic:notEmpty>
								</td>
							</tr>
						</table>
					
				</logic:equal>

				<logic:notEqual name="cierreComercioAdapterVO" property="permiteIniCierreCom" value="true">
					<tr>
						<td><label><bean:message bundle="gde" key="gde.cierreComercio.fechaCeseActividad.label"/>: </label></td>
						<td class="normal">
							<bean:write name="cierreComercioAdapterVO" property="cierreComercio.fechaCeseActividadView"/>
						</td>
					</tr>
					<tr>	
						<td><label><bean:message bundle="gde" key="gde.motivoCierre.label"/>: </label></td>
						<td class="normal">
							<bean:write name="cierreComercioAdapterVO" property="motivoCierre.desMotivo"/>
						</td>					
					</tr>
					<!-- Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td class="normal">
							<bean:write name="cierreComercioAdapterVO" property="cierreComercio.caso.sistemaOrigen.desSistemaOrigen"/> &nbsp;
							<bean:write name="cierreComercioAdapterVO" property="cierreComercio.caso.numero"/>
						</td>
					</tr>
				</logic:notEqual>			
			</table>
		
	</fieldset>
	
	
	<!-- Multas -->
	<fieldset>
		<legend>  
			<bean:message bundle="gde" key="gde.multa.title"/>				
		</legend>
	<!-- Tabla Multas -->
	
	<div id="resultadoFiltro">
			<logic:notEmpty  name="cierreComercioAdapterVO" property="listMulta">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th align="left"><bean:message bundle="gde" key="gde.tipoMulta.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.estadoDeuda.label"/></th>
						</tr>
							
						<logic:iterate id="MultaVO" name="cierreComercioAdapterVO" property="listMulta">
							<tr>						
								
								<td><bean:write name="MultaVO" property="tipoMulta.desTipoMulta"/>&nbsp;</td>
								<td><bean:write name="MultaVO" property="deuda.estadoDeuda.desEstadoDeuda" />&nbsp;</td>
						
							</tr>
						</logic:iterate>
						
					</tbody>
				</table>
				<logic:equal name="cierreComercioAdapterVO" property="aplicaMulta" value="false">
					<tbody>
						<tr><td align="center">
							<bean:message bundle="gde" key="gde.infoNoAplicaMulta"/>
						</td></tr>
					</tbody>
				</logic:equal>
			</logic:notEmpty>
			
			<logic:empty name="cierreComercioAdapterVO" property="listMulta">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
				<logic:equal name="cierreComercioAdapterVO" property="aplicaMulta" value="false">
				<logic:notEqual name="cierreComercioAdapterVO" property="cierreComercio.id" value="">
					<tbody>
						<tr><td align="center">
							<bean:message bundle="gde" key="gde.noAplicaMulta"/>
						</td></tr>
					</tbody>
				</logic:notEqual>
				</logic:equal>
			</logic:empty>
	</div>
	
	<!-- Fin Tabla Multas -->
	<logic:equal name="cierreComercioAdapterVO" property="aplicaMulta" value="true">
	<!-- Caso NoEmiMul -->
		<logic:empty name="cierreComercioAdapterVO" property="cierreComercio.multa.id">
			<table class="tablabotones" width="80%">
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<!-- Inclucion de CasoView -->
					<td colspan="3">
						<!-- Caso -->
							<td colspan="3">
							<bean:define id="IncludedVO" name="cierreComercioAdapterVO" property="cierreComercio.casoNoEmiMul"/>
							<bean:define id="voName" value="cierreComercio.casoNoEmiMul" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>				
						</td>
						
						<!-- Fin caso -->				
					</td>
				</tr>	
			</table>
		</logic:empty>
	
	<logic:equal name="cierreComercioAdapterVO" property="cierreComercio.multa.id" value="">
			<table class="tablabotones" width="100%">
				<tr>
					<td align="right">
			 			<button type="button" name="btnAgregar" class="boton" onclick="submitForm('agregarMulta', '<bean:write name="cierreComercioAdapterVO" property="idCuenta" bundle="base" formatKey="general.format.id"/>');">
					  	    <bean:message bundle="base" key="abm.button.agregar"/>
						</button>
					</td>
				</tr>
			</table>
		
	</logic:equal>
	</logic:equal>
	
	</fieldset>
	<!-- Fin Multas -->
	
	<table class="tablabotones">
	<tr>
		<!-- Volver -->
		<td align="left">
			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="cierreComercioAdapterVO" property="idCuenta" bundle="base" formatKey="general.format.id"/>');">
		  	    <bean:message bundle="base" key="abm.button.volver"/>
			</button>
		</td>
		<logic:equal name="cierreComercioAdapterVO" property="permiteIniCierreCom" value="true">	
			<!-- Inicio Cierre -->
			<logic:notEqual name="cierreComercioAdapterVO" property="cierreComercio.id" value="">
				<td align="right" width="100%">
					<button type="button" name="btnInicioCierreComercio" class="boton" onclick="submitForm('inicioCierreComercio', '<bean:write name="cierreComercioAdapterVO" property="idCuenta" bundle="base" formatKey="general.format.id"/>');">
				  	    <bean:message bundle="base" key="abm.button.inicioCierre"/>
					</button>
				</td>
			</logic:notEqual>
		</logic:equal>
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
<!-- Fin formulario -->