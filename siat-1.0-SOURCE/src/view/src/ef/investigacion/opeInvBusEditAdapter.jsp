<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOpeInvBus.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="ef" key="ef.opeInvBusAdapter.title"/></h1>	
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
	<!-- OpeInv -->
	<bean:define id="opeInvVO" name="opeInvBusAdapterVO" property="opeInvBus.opeInv"/>
	<%@include file="/ef/investigacion/includeOpeInvView.jsp" %>	
	<!-- OpeInv -->		
	
	<!-- Datos de la busqueda -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvBusAdapter.busqueda.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.opeInvBus.fechaBusqueda.label"/>: </label></td>
				<td class="normal">
					<html:text name="opeInvBusAdapterVO" property="opeInvBus.fechaBusquedaView" styleId="fechaBusquedaView" size="15" maxlength="10" styleClass="datos"/>
						<a class="link_siat" onclick="return show_calendar('fechaBusquedaView');" id="a_fechaBusquedaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.opeInvBus.desOpeInvBus.label"/>: </label></td>
				<td class="normal">
					<html:textarea name="opeInvBusAdapterVO" property="opeInvBus.descripcion" style="height:50px;width:300px"/>
				</td>
			</tr>				
		</table>
	</fieldset>
	
	<!-- Datos del contribuyente -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvBusAdapter.contribuyente.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.contribuyente.nroIsib.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="opeInvBusAdapterVO" property="contribuyente.nroIsib"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.contribuyente.nroCuit.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="opeInvBusAdapterVO" property="contribuyente.persona.cuit"/>
				</td>
			</tr>			
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.contribuyente.CER.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="opeInvBusAdapterVO" property="cer.id">
						<html:optionsCollection name="opeInvBusAdapterVO" property="listSiNo" label="value" value="id"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.contribuyente.promedioPago.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="opeInvBusAdapterVO" property="promedioPagoContr"/>
				</td>
			</tr>			
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.contribuyente.promedioPago.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="opeInvBusAdapterVO" property="fecPromedioPagoContrDesdeView" styleId="fecPromedioPagoContrDesdeView" size="15" maxlength="10" styleClass="datos"/>
						<a class="link_siat" onclick="return show_calendar('fecPromedioPagoContrDesdeView');" id="a_fecPromedioPagoContrDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td class="normal">
					<html:text name="opeInvBusAdapterVO" property="fecPromedioPagoContrHastaView" styleId="fecPromedioPagoContrHastaView" size="15" maxlength="10" styleClass="datos"/>
						<a class="link_siat" onclick="return show_calendar('fecPromedioPagoContrHastaView');" id="a_fecPromedioPagoContrHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			
				<logic:iterate id="TipObjImpAtrDefinition" name="opeInvBusAdapterVO" property="tipObjImpDefinition4Contr.listTipObjImpAtrDefinition" indexId="count">
					<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
					<bean:define id="SeleccionarTodos" value="Todos..."/> 
					<%@ include file="/def/atrDefinition4Edit.jsp" %>
				</logic:iterate>
							
		</table>
	</fieldset>	
	
	<!-- Parametros de la cuenta -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvBusAdapter.cuenta.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.cuenta.promedioPago.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="opeInvBusAdapterVO" property="promedioPagoDeCuenta"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.cuenta.promedioPago.fechaDesde.label"/>: </label></td>
				<td class="normal">
					<html:text name="opeInvBusAdapterVO" property="fecPromedioPagoDeCtaDesdeView" styleId="fecPromedioPagoDeCtaDesdeView" size="15" maxlength="10" styleClass="datos"/>
						<a class="link_siat" onclick="return show_calendar('fecPromedioPagoDeCtaDesdeView');" id="a_fecPromedioPagoDeCtaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td class="normal">
					<html:text name="opeInvBusAdapterVO" property="fecPromedioPagoDeCtaHastaView" styleId="fecPromedioPagoDeCtaHastaView" size="15" maxlength="10" styleClass="datos"/>
						<a class="link_siat" onclick="return show_calendar('fecPromedioPagoDeCtaHastaView');" id="a_fecPromedioPagoDeCtaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>				
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.cuenta.cantPeriodosNoDecla.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="opeInvBusAdapterVO" property="cantPeriodosNoDeclarados"/>
				</td>
			</tr>			
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.cuenta.cantPersonal.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="opeInvBusAdapterVO" property="cantPersonal"/>
				</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInvBus.cuenta.altaFiscal.label"/>: </label></td>
				<td class="normal">
					<html:select name="opeInvBusAdapterVO" property="altaFiscalView" styleClass="select">
						<html:optionsCollection name="opeInvBusAdapterVO" property="listSiNo" label="value" value="id"/>
					</html:select>
				</td>
			</tr>
						
		</table>
	</fieldset>	
	
	<!-- Datos resoluciones de otros operativos -->
    <fieldset>
    	<legend><bean:message bundle="ef" key="ef.opeInvBusAdapter.otrosOperativos.title"/></legend>
		<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="ef" key="ef.opeInvBus.estadoOtroOperativo.label"/>: </label></td>
					<td class="normal">
						<html:select name="opeInvBusAdapterVO" property="estadoOtroOperativo.id">
							<html:optionsCollection name="opeInvBusAdapterVO" property="listEstadoOpeInvConVO" label="desEstadoOpeInvCon" value="id"/>
						</html:select>
					</td>
				</tr>
		</table>
	</fieldset>		
	
	<!-- Parametros del Comercio -->
    <fieldset>
    	<legend><bean:message bundle="ef" key="ef.opeInvBusAdapter.paramObjImp.title"/></legend>
		<logic:notEmpty name="opeInvBusAdapterVO" property="tipObjImpDefinition4Comercio.listTipObjImpAtrDefinition">
			<table class="tabladatos" width="100%">		
				<logic:iterate id="TipObjImpAtrDefinition" name="opeInvBusAdapterVO" property="tipObjImpDefinition4Comercio.listTipObjImpAtrDefinition" indexId="count">
					<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
					<bean:define id="SeleccionarTodos" value="Todos..."/>
					
					<%@ include file="/def/atrDefinition4Bus.jsp" %>
									
				</logic:iterate>
			</table>
		</logic:notEmpty>
	</fieldset>
	<!-- Fin Parametros de Objeto Imponible -->
	
	

	
	<!-- Fin Filtro -->

   	 <table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="33%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
  	   		<td align="center" width="33%">
	   	    	<html:button property="btnLimpiar" styleClass="boton" onclick="submitForm('limpiar', '');">
	   	    		<bean:message bundle="base" key="abm.button.limpiar"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="34%">
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
					<bean:message bundle="ef" key="ef.opeInvBusAdapterVO.button.agregarBusqueda"/>
				</html:button>
   	    	</td>  
   	    </tr>
   	</table>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>

</html:form>
<!-- opeInvBusEditAdapter.jsp -->
<!-- Fin Tabla que contiene todos los formularios -->
