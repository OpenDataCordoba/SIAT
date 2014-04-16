<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarLiqDetalleDeuda.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="gde" key="gde.liqDetalleDeudaViewAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<!-- Se arma un boton volver segun sea una inclucion o la liquidacion de la deuda -->
						<logic:equal name="userSession" property="navModel.act" value="includeVerDetalleDeuda">
							<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
								<bean:message bundle="base" key="abm.button.volver"/>
							</button>
						</logic:equal>
						
						<logic:notEqual name="userSession" property="navModel.act" value="includeVerDetalleDeuda">
							<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.idCuenta" bundle="base" formatKey="general.format.id"/>');">
								<bean:message bundle="base" key="abm.button.volver"/>
							</button>
					   	</logic:notEqual>
				</td>
			</tr>
		</table>
		<!-- LiqDetalleDeuda -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqDetalleDeuda.title"/></legend>
			
			<dl class="listahorizontalSiat">
				<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.periodoDeuda.label"/>: </label></dt> 
				<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.periodoDeuda"/></dd>
			</dl>
			
			<dl class="listahorizontalSiat">
				<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.fechaEmision.label"/>: </label></dt> 
				<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.fechaEmision"/></dd>
			</dl>
				
			<dl class="listahorizontalSiat">
				<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.desRecurso.label"/>: </label></dt> 
				<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.desRecurso"/></dd>
			</dl>
			
			<dl class="listahorizontalSiat">
				<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.nroCuenta.label"/>: </label></dt> 
				<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.nroCuenta"/></dd>
			</dl>

			<dl class="listahorizontalSiat">
				<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.desViaDeuda.label"/>: </label></dt> 
				<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.desViaDeuda"/></dd>
			</dl>
			
			<dl class="listahorizontalSiat">
				<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.desClasificacionDeuda.label"/>: </label></dt> 
				<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.desClasificacionDeuda"/></dd>
			</dl>
			
			<dl class="listahorizontalSiat">
				<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.desServicioBanco.label"/>: </label></dt> 
				<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.desServicioBanco"/></dd>
			</dl>
			
			<!--
			<dl class="listahorizontalSiat">
				<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.desEstadoDeuda.label"/>: </label></dt> 
				<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.desEstadoDeuda"/></dd>
			</dl>
			-->
			<logic:equal name="LiqDetalleDeudaAdapterVO" property="act" value="modificarDeuda">
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.importe.label"/>: </label></dt> 
					<dd><html:text name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.importeView" size="10" maxlength="100"/></dd>
				</dl>
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.saldo.label"/>: </label></dt> 
					<dd><html:text name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.saldoView" size="10" maxlength="100"/></dd>
				</dl>		
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.actualizacion.label"/>: </label></dt> 
					<dd><html:text name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.actualizacionView" size="10" maxlength="100"/></dd>
				</dl>		
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.fechaPago.label"/>: </label></dt> 
					<dd>
						<html:text name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.fechaPagoView" styleId="fechaPagoView" size="10" maxlength="10" styleClass="datos"/>
							<a class="link_siat" onclick="return show_calendar('fechaPagoView');" id="a_fechaPagoView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>	
					</dd>
				</dl>					
			</logic:equal>
			<logic:notEqual name="LiqDetalleDeudaAdapterVO" property="act" value="modificarDeuda">
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.importe.label"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.importe" bundle="base" formatKey="general.format.currency"/></dd>
				</dl>
		
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.saldo.label"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.saldo" bundle="base" formatKey="general.format.currency"/></dd>
				</dl>			
			</logic:notEqual>

			<dl class="listahorizontalSiat">
				<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.fechaVencimiento.label"/>: </label></dt> 
				<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.fechaVencimiento" /></dd>
			</dl>
			
			<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.liqDetalleDeuda.usuarioEmision.label"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.usuarioEmision" /></dd>
			</dl>	
		</fieldset>	
		<!-- LiqDetalleDeuda -->

		<!-- Atributos de Emision -->
		<logic:equal name="LiqDetalleDeudaAdapterVO" property="mostrarAtributosEmision" value="true">
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.liqDetalleDeuda.listAtributosEmision.label"/></legend>
				
				<!-- Lista de Atributos -->
				<logic:notEmpty  name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.liqAtrEmisionVO.listAtributos">
					<logic:iterate id="LiqAtrValorVO" name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.liqAtrEmisionVO.listAtributos">
						<dl class="listahorizontalSiat">
							<dt><label><bean:write name="LiqAtrValorVO" property="label"/>:&nbsp;</label></dt> 
							<dd><bean:write name="LiqAtrValorVO" property="value"/></dd>
						</dl>
					</logic:iterate>
				</logic:notEmpty>
				
				<!-- Tabla de Atributos -->
				<logic:notEmpty  name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.liqAtrEmisionVO.tablaAtributos">
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
						<tbody>					
							
							<bean:define id="Header" name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.liqAtrEmisionVO.tablaAtributos[0]"/>
							<logic:iterate id="Element" name="Header" property="listElements">
								<th><bean:write name="Element" property="label"/>&nbsp;</th>
							</logic:iterate>
							
							<logic:iterate id="LiqAtrTablaFilaVO" name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.liqAtrEmisionVO.tablaAtributos">
								<tr>
									<logic:iterate id="Element" name="LiqAtrTablaFilaVO" property="listElements">
										<td><bean:write name="Element" property="value"/>&nbsp;</td>
									</logic:iterate>
								</tr>
							</logic:iterate>
						</tbody>
					</table>
				</logic:notEmpty>
			</fieldset>
		</logic:equal>
		<!-- Atributos de Emision -->
		
		<!-- Conceptos -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.liqDetalleDeuda.listConceptos.label"/></caption>
	    	<tbody>
				<logic:notEmpty  name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.listConceptos">
			    	<tr>
						<th align="left"><bean:message bundle="gde" key="gde.liqConceptoDeuda.desConcepto.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.liqConceptoDeuda.importe.label"/></th>
						<th align="left"><bean:message bundle="gde" key="gde.conDeuDet.deuda.saldo.label"/></th>							
					</tr>
					<logic:iterate id="LiqConceptoDeudaVO" name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.listConceptos">
						<tr>
							<td><bean:write name="LiqConceptoDeudaVO" property="desConcepto"/>&nbsp;</td>
							<logic:notEqual name="LiqDetalleDeudaAdapterVO" property="act" value="modificarDeuda">
								<td><bean:write name="LiqConceptoDeudaVO" property="importe" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
							</logic:notEqual>
							<logic:equal name="LiqDetalleDeudaAdapterVO" property="act" value="modificarDeuda">
								<td>
								<input type="text" size="10 maxlength="100" name="importe<bean:write name="LiqConceptoDeudaVO" property="idRecConView"/>"
											value="<bean:write name="LiqConceptoDeudaVO" property="importeView"/>"/>
								</td>
							</logic:equal>
							<td><bean:write name="LiqConceptoDeudaVO" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.listConceptos">
					<tr><td align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		<!-- Conceptos -->
		
		<!-- Datos del Procurador -->
		<logic:equal name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.poseeProcurador" value="true">
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.procurador.title"/></legend>
				
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.procurador.descripcion.label"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.desProcurador"/></dd>
				</dl>
			
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.procurador.domicilio.label"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.domProcurador"/></dd>
				</dl>
				
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.procurador.telefono.label"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.telProcurador"/></dd>
				</dl>
				
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.procurador.horarioAtencion.label"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.horAteProcurador"/></dd>
				</dl>
			</fieldset>
		</logic:equal>
		<!-- Datos del Procurador -->
		
		<!-- Datos de Anulacion de deuda -->
		<logic:equal name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.deudaAnulada" value="true">
			<fieldset>
				<legend><bean:message bundle="gde" key="gde.anulacion.title"/></legend>
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.anulacion.fechaAnulacion.label"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.fechaAnulacionView"/></dd>
				</dl>
			
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.motAnuDeu.desMotAnuDeu.ref"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.desMotAnuDeu"/></dd>
				</dl>
				
				<dl class="listahorizontalSiat">
					<dt><label><bean:message bundle="gde" key="gde.anulacion.usuario.label"/>: </label></dt> 
					<dd><bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.usuarioAnuDeu"/></dd>
				</dl>
			</fieldset>
		</logic:equal>
		<!-- Datos de Anulacion de deuda -->
		
		<logic:notEqual name="LiqDetalleDeudaAdapterVO" property="act" value="modificarDeuda">
			<!-- Declaracion Jurada -->
			<logic:equal name="LiqDetalleDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				<fieldset>
					<legend><bean:message bundle="gde" key="gde.liqDetalleDeuda.decJur.title"/></legend>
					<p>
						<a href="/siat/gde/AdministrarLiqDetalleDeuda.do?method=verCuentaDecJur&idDeuda=<bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.idDeuda" bundle="base" formatKey="general.format.id"/>">
							<bean:message bundle="gde" key="gde.liqDetalleDeuda.decJur.link"/>
						</a>
					</p>
				</fieldset>
			</logic:equal>
		</logic:notEqual>

		<!-- Se arma un boton volver segun sea una inclucion o la liquidacion de la deuda -->
		<logic:equal name="userSession" property="navModel.act" value="includeVerDetalleDeuda">
			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver"/>
			</button>
		</logic:equal>
		
		<logic:notEqual name="userSession" property="navModel.act" value="includeVerDetalleDeuda">
			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="LiqDetalleDeudaAdapterVO" property="liqDetalleDeuda.idCuenta" bundle="base" formatKey="general.format.id"/>');">
				<bean:message bundle="base" key="abm.button.volver"/>
			</button>
	   	</logic:notEqual>
	   	
	    <logic:equal name="LiqDetalleDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
			<logic:notEqual name="LiqDetalleDeudaAdapterVO" property="act" value="modificarDeuda">
				<logic:equal name="LiqDetalleDeudaAdapterVO" property="modificarDeudaEnabled" value="enabled">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('modificarDeuda', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
			   	</logic:equal>
		   	</logic:notEqual>
			<logic:equal name="LiqDetalleDeudaAdapterVO" property="act" value="modificarDeuda">
				<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('modificar', '');">
					<bean:message bundle="base" key="abm.button.guardar"/>
				</html:button>
			</logic:equal>
		</logic:equal>
					
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<input type="hidden" name="validAuto" value="false"/>
		
		<!-- Inclusion del Codigo Javascript del Calendar-->
		<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>	
	
	</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
