<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqDecJur.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	
	<h1><bean:message bundle="gde" key="gde.liqDecJurAdapterInit.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="gde" key="gde.liqDecJurAdapterInit.legend"/></p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="liqDecJurAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>
	
	<fieldset>
		<legend>
			<bean:message bundle="gde" key="gde.liqDeudaAdapter.cuentaSeleccionada"/>: 
			<bean:write name="liqDecJurAdapterVO" property="cuenta.nroCuenta"/>
		</legend>
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.desRecurso.label"/>:</label>
	      		<bean:write name="liqDecJurAdapterVO" property="cuenta.desRecurso"/>
			</p>
	</fieldset>
			
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqDecJurAdapter.actividades.title"/> </legend>
	
		<p align="center">
			<label><bean:message bundle="gde" key="gde.decJurDetAdapter.filtroActividad.label"/>: </label>
			<input type="text" name="mytext" onkeyup="fillin(this, 'wholetext');" ><br/>
		</p>
		<p>
			<html:select name="liqDecJurAdapterVO" property="actividad.id" style="width:80%" styleClass="select" styleId="wholetext">					
				<html:optionsCollection name="liqDecJurAdapterVO" property="listActividad" label="codYDescripcion" value="id"/>
			</html:select>
			
			<input type="button" class="boton"onClick="submitForm('agregarActividad', '');" 
				value="<bean:message bundle="base" key="abm.button.agregar"/>"
			/>
		</p>
	
		<p>
		
			<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.actividades.title"/></caption>
		    	<tbody>
					<logic:notEmpty name="liqDecJurAdapterVO" property="listActividadDec">
				    	<tr>
							<th width="1">&nbsp;</th> <!-- Quitar -->
							<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.actividad.label"/></th>							
						</tr>
						<logic:iterate id="ActividadVO" name="liqDecJurAdapterVO" property="listActividadDec">
				
							<tr>
								<!-- Quitar -->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('quitarActividad', '<bean:write name="ActividadVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
									</a>
								</td>
								<td><bean:write name="ActividadVO" property="codYDescripcion"/>&nbsp;</td>

							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty name="liqDecJurAdapterVO" property="listActividadDec">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>					
				</tbody>
			</table>
		</p>
	
	</fieldset>
	
	<a name="personal">&nbsp;</a>

	<logic:equal name="liqDecJurAdapterVO" property="esDReI" value="true">
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqDecJurAdapter.personal.title"/> </legend>
		
			<p>
				<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.periodoDesde.label"/>:</label>
	      			
	      			<html:select name="liqDecJurAdapterVO" property="periodoDesde.id" styleClass="select">					
						<html:optionsCollection name="liqDecJurAdapterVO" property="listPeriodosDesde" label="periodoAnioView" value="id"/>
					</html:select>
	      			
	      		&nbsp;
	      		<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.periodoHasta.label"/>:</label>
					<html:select name="liqDecJurAdapterVO" property="periodoHasta.id" styleClass="select">					
						<html:optionsCollection name="liqDecJurAdapterVO" property="listPeriodosHasta" label="periodoAnioView" value="id"/>
					</html:select>
	
	      		&nbsp;
	      		<label><bean:message bundle="gde" key="gde.liqDecJurAdapter.valor.label"/>: 	      		
	      			<html:text name="liqDecJurAdapterVO" property="cantPersonal.valor" size="5" maxlength="10" styleClass="datos"/>
	      		</label>
	      		&nbsp;
	      		
	      		<input type="button" class="boton"onClick="submitForm('agregarRecMin', '');" 
					value="<bean:message bundle="base" key="abm.button.agregar"/>"
				/>
	      		
			</p>
		
			<p>
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="gde" key="gde.liqDecJurAdapter.listPersonal.label"/></caption>
			    	<tbody>
						<logic:notEmpty name="liqDecJurAdapterVO" property="listRecMin">
					    	<tr>
								<th width="1">&nbsp;</th> <!-- Quitar -->
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.periodoDesde.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.periodoHasta.label"/></th>
								<th align="left"><bean:message bundle="gde" key="gde.liqDecJurAdapter.valor.label"/></th>							
							</tr>
							<logic:iterate id="RecMinVO" name="liqDecJurAdapterVO" property="listRecMin">
					
								<tr>
									<!-- Quitar -->
									<td>
										<logic:equal name="RecMinVO" property="quitarEnabled" value="true">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('quitarRecMin', '<bean:write name="RecMinVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="base" key="abm.button.eliminar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/eliminar0.gif"/>
											</a>
										</logic:equal>
										<logic:notEqual name="RecMinVO" property="quitarEnabled" value="true">
											&nbsp;
										</logic:notEqual>
									</td>
									<td>
										<bean:write name="RecMinVO" property="periodoDesdeView"/>/
										<bean:write name="RecMinVO" property="anioDesdeView"/>&nbsp;
									</td>
									<td>
										<bean:write name="RecMinVO" property="periodoHastaView"/>/
										<bean:write name="RecMinVO" property="anioHastaView"/>&nbsp;
									</td>
									<td><bean:write name="RecMinVO" property="valor"/>&nbsp;</td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<logic:empty name="liqDecJurAdapterVO" property="listRecMin">
							<tr><td align="center">
							<bean:message bundle="base" key="base.noExistenRegitros"/>
							</td></tr>
						</logic:empty>					
					</tbody>
				</table>
			</p>
		
		</logic:equal>
		
	</fieldset>
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
				<!-- Volver -->
				<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="liqDecJurAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
			  	    <bean:message bundle="base" key="abm.button.volver"/>
				</button>
   	    	</td>
   	    	<td align="right" width="50%">
				<html:button property="btnSiguiente"  styleClass="boton" onclick="submitForm('irDetalle', '');">
					<bean:message bundle="gde" key="gde.liqDecJurAdapter.siguiente"/>
				</html:button>
   	    	</td>   	    	
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

<script type="text/javascript">
	function irAPersonal() {
   		document.location = document.URL + '#personal';
	}
</script>

<logic:equal name="liqDecJurAdapterVO" property="irPersonal" value="true">
	<script type="text/javascript">irAPersonal();</script>
</logic:equal>