<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarAnularDeuda.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<!-- Busqueda de Recuso -->
	<span id="blockBusqueda" style="display:none"> 
		<bean:define id="adapterVO" name="liqDeudaAdapterVO"/>
		<bean:define id="poseeParam" value="false" />
		<%@ include file="/def/gravamen/includeRecursoSearch.jsp" %>
	</span>

	<span id="blockSimple" style="display:block">
		
		<h1><bean:message bundle="gde" key="gde.anularDeudaIngresoAdapter.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverAlMenu', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- LiqDeuda -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.anularDeudaIngresoAdapter.fieldset.title"/></legend>
			
				<p>
					<label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label>
					<html:select name="liqDeudaAdapterVO" property="idRecurso" styleClass="select" styleId="cboRecurso" style="width:80%">
						<bean:define id="includeRecursoList" name="liqDeudaAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="liqDeudaAdapterVO" property="idRecurso"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
					
					<a onclick="toggleSearchRecurso('blockSimple', 'blockBusqueda'); setFocus('recursoFilter');" >
						<img title='<bean:message bundle="def" key="def.recurso.filtro.button.busqueda"/>' border="0" 
						src="<%=request.getContextPath()%>/images/iconos/lupita.gif" width="20" height="20">
					</a>
				</p>
				<p>
		      		<label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: 
		      			<html:text name="liqDeudaAdapterVO" property="numeroCuenta" size="15" maxlength="20" styleClass="datos"/>
		      		</label>
		      		<button type="button" onclick="submitForm('buscarCuenta', '');">
		      			<bean:message bundle="gde" key="gde.anularDeudaIngresoAdapter.button.buscarCuenta"/>	      			
		      		</button>
				</p>
			
			  	<div style="text-align:right">
			  		<button type="button" name="btnAceptar" onclick="submitForm('ingresar', '');" class="boton">
			  			<bean:message bundle="gde" key="gde.anularDeudaIngresoAdapter.button.aceptar"/>
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

</html:form>
<!-- Fin formulario -->