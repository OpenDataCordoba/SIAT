<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/BuscarAuxDeuda.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="emi" key="emi.auxDeudaSearchPage.title"/></h1>

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="left">
				<p><bean:message bundle="emi" key="emi.auxDeudaSearchPage.legend"/></p>
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
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.auxDeuda.cuenta.label"/>: </label></td>
				<td class="normal">
					<html:text name="auxDeudaSearchPageVO" property="auxDeuda.cuenta.numeroCuenta" 
						size="14" maxlength="12" styleClass="datos" />
				</td>
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
		<logic:equal name="auxDeudaSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="auxDeudaSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver Deuda -->

							<th align="left"><bean:message bundle="emi" key="emi.auxDeuda.deuda.label"/></th>

							<th align="left"><bean:message bundle="emi" key="emi.auxDeuda.fechaVencimiento.label"/></th>

							<th align="left"><bean:message bundle="emi" key="emi.auxDeuda.importe.label"/></th>

							<logic:equal name="auxDeudaSearchPageVO" property="mostrarConcepto1" value="true">
								<th align="left"><bean:write name="auxDeudaSearchPageVO" property="nameConcepto1"/></th>
							</logic:equal>
							<logic:equal name="auxDeudaSearchPageVO" property="mostrarConcepto2" value="true">
								<th align="left"><bean:write name="auxDeudaSearchPageVO" property="nameConcepto2"/></th>
							</logic:equal>
							<logic:equal name="auxDeudaSearchPageVO" property="mostrarConcepto3" value="true">
								<th align="left"><bean:write name="auxDeudaSearchPageVO" property="nameConcepto3"/></th>
							</logic:equal>
							<logic:equal name="auxDeudaSearchPageVO" property="mostrarConcepto4" value="true">
								<th align="left"><bean:write name="auxDeudaSearchPageVO" property="nameConcepto4"/></th>
							</logic:equal>
						</tr>
							
						<logic:iterate id="AuxDeudaVO" name="auxDeudaSearchPageVO" property="listResult">
							<tr>
								<!-- Ver -->
								<td>
									<logic:equal name="auxDeudaSearchPageVO" property="verEnabled" value="enabled">									
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="AuxDeudaVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:equal>
									<logic:notEqual name="AuxDeudaVO" property="verEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</td>	

								<td>
									<bean:write name="AuxDeudaVO" property="periodoView"/>/<bean:write name="AuxDeudaVO" property="anioView"/>&nbsp;
								</td>

								<td><bean:write name="AuxDeudaVO" property="fechaVencimientoView"/>&nbsp;</td>

								<td><bean:write name="AuxDeudaVO" property="importeView"/>&nbsp;</td>

								<logic:equal name="auxDeudaSearchPageVO" property="mostrarConcepto1" value="true">								
									<td><bean:write name="AuxDeudaVO" property="conc1View"/>&nbsp;</td>
								</logic:equal>

								<logic:equal name="auxDeudaSearchPageVO" property="mostrarConcepto2" value="true">								
									<td><bean:write name="AuxDeudaVO" property="conc2View"/>&nbsp;</td>
								</logic:equal>

								<logic:equal name="auxDeudaSearchPageVO" property="mostrarConcepto3" value="true">								
									<td><bean:write name="AuxDeudaVO" property="conc3View"/>&nbsp;</td>
								</logic:equal>

								<logic:equal name="auxDeudaSearchPageVO" property="mostrarConcepto4" value="true">								
									<td><bean:write name="AuxDeudaVO" property="conc4View"/>&nbsp;</td>
								</logic:equal>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="auxDeudaSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="auxDeudaSearchPageVO" property="listResult">
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
		
	<input type="text" style="display:none"/>
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>		
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
