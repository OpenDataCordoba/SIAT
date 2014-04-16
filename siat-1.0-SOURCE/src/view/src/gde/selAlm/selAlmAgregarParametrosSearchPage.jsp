<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarSelAlmAgregarParametros.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.title"/></h1>	
		
	<p><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.legend"/></p>

		
	<logic:equal name="selAlmAgregarParametrosSearchPageVO"  property="verParametrosTipoSelAlmDetBussEnabled" value="true">
		<!-- TipoSelAlmDet -->
	    <fieldset>
	    	<legend><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.tipoSelAlmDet.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.tipoSelAlmDet.label"/>: </label></td>
					<td class="normal">
						<html:select name="selAlmAgregarParametrosSearchPageVO" property="tipoSelAlmDet.id" styleClass="select" onchange="submitForm('paramTipoSelAlmDet', '');">
								<html:optionsCollection name="selAlmAgregarParametrosSearchPageVO" property="listTipoSelAlmDet" label="desTipoSelAlm" value="id" />
						</html:select>
					</td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin TipoSelAlmDet -->
	</logic:equal>	
	
	<logic:equal name="selAlmAgregarParametrosSearchPageVO"  property="verParametrosDeudaBussEnabled" value="true">
		<!-- Parametros de la Deuda -->
	    <fieldset>
	    	<legend><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.paramDeuda.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="selAlmAgregarParametrosSearchPageVO" property="recurso.desRecurso" />	</td>
				</tr>
				<logic:iterate id="item" name="selAlmAgregarParametrosSearchPageVO" property="recurso.listRecClaDeu">
					<tr>					
						<td>
							<logic:notPresent name="verLabelRecClaDeu">
								<label><bean:message bundle="def" key="def.recClaDeu.label"/>: </label>
							</logic:notPresent>&nbsp;
						</td>
						<bean:define id="verLabelRecClaDeu" value="false"/>
						<td class="normal" colspan="3"> 								
							<html:multibox name="selAlmAgregarParametrosSearchPageVO" property="listIdRecClaDeu">
						   		<bean:write name="item" property="id" bundle="base" formatKey="general.format.id"/> 
						  	</html:multibox> 
						   <bean:write name="item" property="desClaDeu"/> &nbsp;
						</td> 
					</tr>
				</logic:iterate>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.fechaVencimientoDesde.label"/>: </label></td>
					<td class="normal">
						<html:text name="selAlmAgregarParametrosSearchPageVO" property="fechaVencimientoDesdeView" styleId="fechaVencimientoDesdeView" size="12" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaVencimientoDesdeView');" id="a_fechaVencimientoDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
					<td><label><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.fechaVencimientoHasta.label"/>: </label></td>
					<td class="normal">
						<html:text name="selAlmAgregarParametrosSearchPageVO" property="fechaVencimientoHastaView" styleId="fechaVencimientoHastaView" size="12" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaVencimientoHastaView');" id="a_fechaVencimientoHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.importeHistoricoDesde.label"/>: </label></td>
					<td class="normal"><html:text name="selAlmAgregarParametrosSearchPageVO" property="importeHistoricoDesdeView" size="10" maxlength="10" styleClass="datos" /></td>
					<td><label><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.importeHistoricoHasta.label"/>: </label></td>
					<td class="normal"><html:text name="selAlmAgregarParametrosSearchPageVO" property="importeHistoricoHastaView" size="10" maxlength="10" styleClass="datos" /></td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.aplicaAlTotalDeuda.label"/>: </label></td>
					<td class="normal">
						<html:select name="selAlmAgregarParametrosSearchPageVO" property="aplicaAlTotalDeuda.id" styleClass="select" >
								<html:optionsCollection name="selAlmAgregarParametrosSearchPageVO" property="listAplicaAlTotalDeuda" label="value" value="id" />
						</html:select>
					</td>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.ctdMinimaDeuda.label"/>: </label></td>
					<td class="normal"><html:text name="selAlmAgregarParametrosSearchPageVO" property="cantidadMinimaDeudaView" size="10" maxlength="10" styleClass="datos" /></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin Parametros de la Deuda -->
	</logic:equal>	
	
	<logic:equal name="selAlmAgregarParametrosSearchPageVO"  property="verParametrosConvenioBussEnabled" value="true">
		<!-- Parametros ConvenioCuota -->
	    <fieldset>
	    	<legend><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.convenioCuota.title"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="selAlmAgregarParametrosSearchPageVO" property="recurso.desRecurso" />	</td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.fechaVencimiento.label"/>: </label></td>
					<td class="normal">
						<html:text name="selAlmAgregarParametrosSearchPageVO" property="fechaVencimientoView" styleId="fechaVencimientoView" size="12" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaVencimientoView');" id="a_fechaVencimientoView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					</td>
				</tr>
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.cantidadMinimaCuota.label"/>: </label></td>
					<td class="normal"><html:text name="selAlmAgregarParametrosSearchPageVO" property="cantidadCuotasPlanView" size="10" maxlength="10" styleClass="datos" /></td>
				</tr>
			</table>
		</fieldset>
		<!-- Fin ConvenioCuota -->
	</logic:equal>	

	<!-- Parametros de Cuenta -->
    <fieldset>
    	<legend><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.paramCuenta.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.numeroCuenta.ref"/>: </label></td>
				<td class="normal">
					<html:text name="selAlmAgregarParametrosSearchPageVO" property="cuenta.numeroCuenta" size="10" maxlength="10" styleClass="datos" />
				</td>
				<logic:equal name="selAlmAgregarParametrosSearchPageVO" property="recurso.esCategoriaCdM" value="true">
					<td><label><bean:message bundle="rec" key="rec.obra.label"/>: </label></td>
					<td class="normal">
						<html:select name="selAlmAgregarParametrosSearchPageVO" property="obra.id" styleClass="select" >
							<html:optionsCollection name="selAlmAgregarParametrosSearchPageVO" property="listObra" label="desObra" value="id" />
						</html:select>
					</td>
				</logic:equal>
			</tr>
		</table>
	</fieldset>
	<!-- Fin Parametros de Cuenta -->

	<!-- Parametros de Excenciones -->
	<!-- fedel: 2009-01-27: ahora se excluyen siempre todas las deuda de cualquier cuenta que tenga una exencion con enviaJud 0 
    <fieldset>
    	<legend><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.paramExcenciones.title"/></legend>
		<table class="tabladatos">
				<logic:iterate id="ExencionVO" name="selAlmAgregarParametrosSearchPageVO" property="listExencion">
				<tr>
					<td>
						<label><bean:write name="ExencionVO" property="desExencion" />  &nbsp;</label>
					</td>
					
					<td class="normal" > 								
					    <bean:define id="codExencion" name="ExencionVO" property="id" /> 
						<html:select name="selAlmAgregarParametrosSearchPageVO" property="<%=&quot;dynaExcencion(&quot; + codExencion + &quot;)&quot;%>" styleClass="select" >
							<html:optionsCollection name="selAlmAgregarParametrosSearchPageVO" property="listSiNoExencion" label="value" value="id" />
						</html:select>
					</td> 
				 </tr>	
				</logic:iterate>
		</table>
	</fieldset>
	-->
	<!-- Fin Parametros de Excenciones -->

	<!-- Parametros de Contribuyente -->
    <fieldset>
    	<legend><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.paramContribuyente.title"/></legend>
		<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.label"/></label>
					</td>
					<td class="normal">
					    <bean:write name="selAlmAgregarParametrosSearchPageVO" property="persona.represent"/>
	    				<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscarPersona', '');">
		  					<bean:message bundle="base" key="abm.button.buscar"/>
						</html:button>
						<br/><span style="font-size:80%;"><bean:message bundle="gde" key="gde.selAlmDeudaAdapter.msg.buscarPersona"/></span>
						<br/>
					</td>
				</tr>				
				<logic:iterate id="AtributoVO" name="selAlmAgregarParametrosSearchPageVO" property="listAtributo">
				<tr>
					<td>
						<label><bean:write name="AtributoVO" property="desAtributo" />  &nbsp;</label>
					</td>
					<td class="normal" > 								
					    <bean:define id="idAtributo" name="AtributoVO" property="id" /> 
						<html:select name="selAlmAgregarParametrosSearchPageVO" property="<%=&quot;dynaAtributo(&quot; + idAtributo + &quot;)&quot; %>" styleClass="select" >
							<html:optionsCollection name="selAlmAgregarParametrosSearchPageVO" property="listSiNoAtributo" label="value" value="id" />
						</html:select>
					</td> 
				 </tr>	
				</logic:iterate>
		</table>
	</fieldset>
	<!-- Fin Parametros de Contribuyente -->

	<!-- Parametros de Objeto Imponible -->
    <fieldset>
    	<legend><bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.paramObjImp.title"/></legend>
		<logic:notEmpty name="selAlmAgregarParametrosSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition">
			<table class="tabladatos" width="100%">
		
				<logic:iterate id="TipObjImpAtrDefinition" name="selAlmAgregarParametrosSearchPageVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" indexId="count">
					<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
					<bean:define id="SeleccionarTodos" value="Todos..."/> 
					<%@ include file="/def/atrDefinition4Bus.jsp" %>
				</logic:iterate>
			</table>
		</logic:notEmpty>
	</fieldset>
	<!-- Fin Parametros de Objeto Imponible -->

	<!-- Parametros de Proceso por ahora se van -->
	
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
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregarParametrosSelAlm', '');">
					<bean:message bundle="gde" key="gde.selAlmAgregarParametrosSearchPage.button.agregarParametros"/>
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
<!-- Fin Tabla que contiene todos los formularios -->
