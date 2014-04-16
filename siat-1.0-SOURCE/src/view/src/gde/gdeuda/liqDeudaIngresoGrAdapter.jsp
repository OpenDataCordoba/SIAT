<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqDeuda.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="liqDeudaAdapterVO"/>
		<bean:define id="poseeParam" value="true" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>		

	<span id="blockSimple" style="display:block">
	
	<h1><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.title"/></h1>	
	
		<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>	Seleccionando un Recurso e ingresando su n&uacute;mero de Cuenta puede consultar la liquidaci&oacute;n de deuda.</p>

				<p>	Asimismo obtendr&aacute;, en caso de corresponder, informaci&oacute;n sobre:</p>
				<ul class="vinieta">
					<li>Cuentas relacionadas entre Recursos.</li>
					<li>Exenciones.</li>
					<li>Estado de los convenios de pago pendientes.</li>
			
				</ul>
				<p>El Sistema permite adem&aacute;s reimprimir per&iacute;odos de deuda e informar pagos realizados que no figuran 
				en su liquidaci&oacute;n de cuenta y solicitar su asentamiento.</p>

			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverAlMenu', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- LiqDeuda -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.fieldset.title"/></legend>
			
			<p>
			
				<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
				<html:select name="liqDeudaAdapterVO" property="cuenta.idRecurso" styleClass="select" styleId="cboRecurso" style="width:80%" onchange="submitForm('paramRecurso', '');">
					<bean:define id="includeRecursoList" name="liqDeudaAdapterVO" property="listRecurso"/>
					<bean:define id="includeIdRecursoSelected" name="liqDeudaAdapterVO" property="cuenta.idRecurso"/>
					<%@ include file="/def/gravamen/includeRecurso.jsp" %>
				</html:select>
				
				<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
					<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
					src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
				</a>
			</p>
			<p>
	      		<label><bean:message bundle="pad" key="pad.cuenta.label"/>: 	      		
	      			<html:text name="liqDeudaAdapterVO" property="cuenta.numeroCuenta" size="15" maxlength="20" styleClass="datos"/>
	      		</label>
			</p>
			
			<logic:equal name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable" value="true">
				<p>
					<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.periodo.label"/>: </label>
					<html:select name="liqDeudaAdapterVO" property="cuenta.estadoPeriodo.id" styleClass="select" style="width:25%">
						<html:optionsCollection name="liqDeudaAdapterVO" property="listEstadoPeriodo" label="value" value="id" />
					</html:select>
					&nbsp;
					<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.clasificacionDeuda.label"/>: </label>
					<html:select name="liqDeudaAdapterVO" property="cuenta.recClaDeu.id" styleClass="select">
						<html:optionsCollection name="liqDeudaAdapterVO" property="listRecClaDeu" label="desClaDeu" value="id" />
					</html:select>
				</p>
				
				<p>
					<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.fechaVtoDesde.label"/>: </label>
					<html:text name="liqDeudaAdapterVO" property="cuenta.fechaVtoDesdeView" styleId="fechaVtoDesdeView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVtoDesdeView');" id="a_fechaVtoDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
					&nbsp;
					<label><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.fechaVtoHasta.label"/>: </label>
					<html:text name="liqDeudaAdapterVO" property="cuenta.fechaVtoHastaView" styleId="fechaVtoHastaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVtoHastaView');" id="a_fechaVtoHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</p>
			</logic:equal>

		    <jsp:include page="/guide.jsp?g=liqDeudaIngresoGr" />
		
			<html:hidden name="liqDeudaAdapterVO" property="cuenta.esRecursoAutoliquidable"/>
			<input type="hidden" name="validAuto" value="false"/>
			
		  	<div style="text-align:right">
		  		<button type="button" name="btnAceptar" onclick="submitForm('ingresarLiqDeudaGr', '');" class="boton">
		  			<bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.button.aceptar"/>
		  		</button>
		  	</div>


	</fieldset>	
	<!-- LiqDeuda -->
	
	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volverAlMenu', '');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</html:button>
	
	</span>

    <input type="text" style="display:none"/>	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="hidden" name="liqDeudaVieneDe" value="recursoCuenta" />
		
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>	
</html:form>
<!-- Fin formulario -->

<script type="text/javascript">setFocus('cboRecurso');</script>
