<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmisionTRP.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="emisionTRPAdapterVO"/>
		<bean:define id="poseeParam" value="true" />  
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">

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
				
				<!-- Viene con Cuenta preseteada -->
				<logic:empty name="emisionTRPAdapterVO" property="listRecurso">
					<!-- Recurso -->
					<tr>
						<td><label><bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="emisionTRPAdapterVO" property="emision.recurso.desRecurso"/>
						</td>
					</tr>
					<!-- Fecha Emision -->
					<tr>
						<td><label><bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
						<td class="normal">
							<bean:write name="emisionTRPAdapterVO" property="emision.fechaEmisionView"/>
						</td>
					</tr>
					<!-- Cuenta -->
					<tr>	
						<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>:</label></td>
						<td class="normal" colspan="3">
							<bean:write name="emisionTRPAdapterVO" property="emision.cuenta.numeroCuenta"/>
						</td>
					</tr>
				</logic:empty>
				
				<!-- No Viene Cuenta preseteada -->
				<logic:notEmpty name="emisionTRPAdapterVO" property="listRecurso">
					
					<tr>
						<!-- Recurso -->
						<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="emisionTRPAdapterVO" property="emision.recurso.id" styleClass="select" style="width:90%" onchange="submitForm('paramRecurso', '');">
								<bean:define id="includeRecursoList" name="emisionTRPAdapterVO" property="listRecurso"/>
								<bean:define id="includeIdRecursoSelected" name="emisionTRPAdapterVO" property="emision.recurso.id"/>
								<%@ include file="/def/gravamen/includeRecurso.jsp" %>
							</html:select>

							<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
								<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
								src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
							</a>  
						</td>
					</tr>
		
					<tr>
						<!-- Fecha Emision -->
						<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
						<td class="normal">
							<html:text name="emisionTRPAdapterVO" property="emision.fechaEmisionView" styleId="fechaEmisionView" size="15" maxlength="10" styleClass="datos" />
							<a class="link_siat" onclick="return show_calendar('fechaEmisionView');" id="a_fechaEmisionView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>				
					</tr>
		
					<tr>
						<!-- Cuenta -->	
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>:</label></td>
						<td class="normal" colspan="3">
							<html:text name="emisionTRPAdapterVO" property="emision.cuenta.numeroCuenta" size="10" maxlength="12"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.button.buscarCuenta"/>
							</html:button>
						</td>
					</tr>
			   </logic:notEmpty>
	
				<tr>
					<!-- Cantidad de Deuda a Emitir por Periodos -->
					<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionPuntualEncEditAdapter.cantDeuPer.label"/>: </label></td>
					<td class="normal"><html:text name="emisionTRPAdapterVO" property="emision.cantDeuPerView" size="4" maxlength="4"/></td>			
				</tr>
	
				<tr>
					<!-- Inclusion de Caso -->
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="emisionTRPAdapterVO" property="emision"/>
						<bean:define id="voName" value="emision" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>
					</td>
					<!-- Fin Inclusion de Caso -->			
				</tr>

				<tr>	
					<!-- Nro. Expediente -->
					<td><label><bean:message bundle="emi" key="emi.emisionTRPAdapter.nroExpediente.label"/>: </label></td>
					<td class="normal"><html:text styleId="txtNroExpediente" name="emisionTRPAdapterVO" property="nroExpediente" size="8" maxlength="15"/></td>			

					<!-- Importe -->
					<td><label><bean:message bundle="emi" key="emi.emisionTRPAdapter.importe.label"/>: </label></td>
					<td class="normal"><html:text styleId="txtImporte" name="emisionTRPAdapterVO" property="importeView" size="8" maxlength="15"/></td>			
				</tr>
	
				<tr>	
					<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.planoDetalleAdapter.situacionInmueble.label"/>: </label></td>														   
					<td class="normal">
						<html:select name="emisionTRPAdapterVO" property="situacionInmueble.id" styleClass="select">
							<html:optionsCollection name="emisionTRPAdapterVO" property="listSituacionInmueble" label="desSituacionInmueble" value="id" />
						</html:select>
					</td>					
				</tr>
	
				<tr>
					<!-- Visacion Previa -->
					<td><label><bean:message bundle="emi" key="emi.emisionTRPAdapter.visacionPrevia.label"/>: </label></td>
					<td class="normal"><html:checkbox name="emisionTRPAdapterVO" property="visacionPrevia" /></td>
	
					<!-- Aplica Ajuste -->
					<td><label><bean:message bundle="emi" key="emi.emisionTRPAdapter.aplicaAjuste.label"/>: </label></td>
					<td class="normal"><html:checkbox name="emisionTRPAdapterVO" property="aplicaAjuste" /></td>
				</tr>
	
				<tr>
					<!-- Descuenta Visacion Previa -->
					<td><label><bean:message bundle="emi" key="emi.emisionTRPAdapter.descVisacionPrevia.label"/>: </label></td>
					<td colspan="3">
						<table>
							<tr>
								<td style="text-align:left; width: 10px"><html:checkbox name="emisionTRPAdapterVO" property="descVisacionPrevia" onchange="submitForm('paramDescVisacionPrevia', '');" /></td>
								<logic:equal name="emisionTRPAdapterVO" property="descVisacionPrevia" value="on">
									<td style="text-align:left">
										<ul class="vinieta">
											<li style="text-align:left"><bean:message bundle="emi" key="emi.emisionTRPAdapter.recibo.description"/></li>
										</ul>
									</td>
								</logic:equal>
							</tr>
						</table>
					</td>
				</tr>
	
				<logic:equal name="emisionTRPAdapterVO" property="descVisacionPrevia" value="on">
					<tr>
						<!-- Recibo 1 -->
						<td><label>1&deg;&nbsp;<bean:message bundle="emi" key="emi.emisionTRPAdapter.recibo.label"/>: </label></td>
						<td class="normal"><html:text name="emisionTRPAdapterVO" property="recibo1" size="9" maxlength="20"/></td>
					</tr>

					<tr>
						<!-- Recibo 2 -->
						<td><label>2&deg;&nbsp;<bean:message bundle="emi" key="emi.emisionTRPAdapter.recibo.label"/>: </label></td>
						<td class="normal"><html:text name="emisionTRPAdapterVO" property="recibo2" size="9" maxlength="20"/></td>
					</tr>
				</logic:equal>
	
			</table>
		</fieldset>	
	
		<fieldset>
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
				<caption><bean:message bundle="pad" key="pad.cuenta.listRecAtrCueV.label"/></caption>
				<tbody>
	               	<tr>
						<th width="1">&nbsp;</th> <!-- Eliminar -->
						<th align="left"><bean:message bundle="emi" key="emi.emisionTRPAdapter.solicitudInmueble.label"/></th>
						<th align="left"><bean:message bundle="emi" key="emi.emisionTRPAdapter.categoriaInmueble.label"/></th>
						<th align="left"><bean:message bundle="emi" key="emi.emisionTRPAdapter.supEdificar.label"/></th>
						<th align="left"><bean:message bundle="emi" key="emi.emisionTRPAdapter.supFinal.label"/></th>
					</tr>
	
					<logic:notEmpty  name="emisionTRPAdapterVO" property="listPlanoDetalle">
						<logic:iterate id="PlanoDetalleVO" name="emisionTRPAdapterVO" property="listPlanoDetalle">
							<tr>
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('eliminarDetalle', '<bean:write name="PlanoDetalleVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</td>
								<td><bean:write name="PlanoDetalleVO" property="tipoSolicitud.desSolicitudInmueble"/>&nbsp;</td>
								<td><bean:write name="PlanoDetalleVO" property="catInm.desCategoriaInmueble"/>&nbsp;</td>
								<td><bean:write name="PlanoDetalleVO" property="supEdifView"/>&nbsp;</td>
								<td><bean:write name="PlanoDetalleVO" property="supFinal.desSuperficieInmueble"/>&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
	
					<logic:empty  name="emisionTRPAdapterVO" property="listPlanoDetalle">
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</logic:empty>
	
					<tr>
						<td align="right" colspan="5">
							<input type="button" class="boton" onClick="submitForm('agregarPlanoDetalle', '');" 
								value="<bean:message bundle="base" key="abm.button.agregar"/>"
							/>
						</td>
					</tr>
				</tbody>
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
					<logic:equal name="emisionTRPAdapterVO" property="act" value="agregar">
						<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('emitir', '');">
							<bean:message bundle="emi" key="emi.emisionTRPAdapter.button.emitir"/>
						</html:button>
					</logic:equal>
	   	    	</td>   	    	
	   	    </tr>
	   	</table>
	</span>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<!-- emisionTRPEditAdapter.jsp -->