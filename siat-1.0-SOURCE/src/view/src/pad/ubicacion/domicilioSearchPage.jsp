<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">		
	
	function submitFormDomicilio(method, selectedId, nombreCalle, numeroCalle, letraCalle, bis) {
	
	var form = document.getElementById('filter');
		
	form.elements["nombreCalle"].value = nombreCalle;
	form.elements["numero"].value = numeroCalle;	
	form.elements["letraCalle"].value = letraCalle;
	form.elements["bis"].value = bis;
	
	submitForm(method, selectedId);

}
	

</script>


<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/BuscarDomicilio.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="pad" key="pad.domicilioSearchPage.title"/></h1>	
		
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="pad" key="pad.domicilioSearchPage.legend"/></p>
			</td>				
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
	<!-- Filtro -->
	<fieldset>
	<legend><bean:message bundle="base" key="base.parametrosBusqueda"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.calle.nombreCalle.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioSearchPageVO" property="domicilio.calle.nombreCalle" size="20" maxlength="20" styleClass="datos" /></td>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.domicilio.numero.label"/>: </label></td>
				<td class="normal"><html:text name="domicilioSearchPageVO" property="domicilio.numeroView" size="20" maxlength="100" styleClass="datos" /></td>
			</tr>
		</table>
			
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="domicilioSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="domicilioSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver/Seleccionar -->
							<th align="left"><bean:message bundle="pad" key="pad.calle.nombreCalle.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.domicilio.numero.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.domicilio.letraCalle.label"/></th>
							<th align="left"><bean:message bundle="pad" key="pad.domicilio.bis.label"/></th>
						</tr>
							
						<logic:iterate id="DomicilioVO" name="domicilioSearchPageVO" property="listResult">
							<tr>
								<!-- Seleccionar Siempre en modo seleccion -->
								<td>	
									<a style="cursor: pointer; cursor: hand;" 
										onclick="submitFormDomicilio('seleccionar', 
											'<bean:write name="DomicilioVO" property="calle.id" bundle="base" formatKey="general.format.id"/>',
											'<bean:write name="DomicilioVO" property="calle.nombreCalle"/>', 
											'<bean:write name="DomicilioVO" property="numeroView"/>', 
											'<bean:write name="DomicilioVO" property="letraCalle"/>', 
											'<bean:write name="DomicilioVO" property="bis.abreviatura"/>');">
										<img title="<bean:message bundle="base" key="abm.button.seleccionar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>
								</td>
								<td><bean:write name="DomicilioVO" property="calle.nombreCalle"/>&nbsp;</td>
								<td><bean:write name="DomicilioVO" property="numeroView" />&nbsp;</td>
								<td><bean:write name="DomicilioVO" property="letraCalle" />&nbsp;</td>
								<td><bean:write name="DomicilioVO" property="bis.value" />&nbsp;</td>
							</tr>
						</logic:iterate>
						<!-- sin paginar porque la consulta del jar mcr no pagina -->						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="domicilioSearchPageVO" property="listResult">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
						<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>
		</logic:equal>			
	</div>
	<!-- Fin Resultado Filtro -->

	<table class="tablabotones">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
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
	
	<input type="hidden" name="nombreCalle" value=""/>
	<input type="hidden" name="numero" value=""/>
	<input type="hidden" name="letraCalle" value=""/>
	<input type="hidden" name="bis" value=""/>
		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
