<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/BuscarOpeInvConActas.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
		
	<h1><bean:message bundle="ef" key="ef.opeInvConActaSearchPage.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p><bean:message bundle="ef" key="ef.opeInvConActaSearchPage.legend"/></p>
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
				<td><label><bean:message bundle="ef" key="ef.planFiscal.label"/>: </label></td>
				<td class="normal">
					<html:select name="opeInvConSearchPageVO" property="opeInvCon.opeInv.planFiscal.id" styleClass="select">
						<html:optionsCollection name="opeInvConSearchPageVO" property="listPlanFiscal" label="desPlanFiscal" value="id" />
					</html:select>
				</td>					
			</tr>
						
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.investigador.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.investigador.id" styleClass="select">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listInvestigador" label="desInvestigador" value="id" />
				</html:select>
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.estadoOpeInvCon.id" styleClass="select">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listEstadoOpeInvCon" label="desEstadoOpeInvCon" value="id" />
				</html:select>
			</td>					
			<td><label><bean:message bundle="ef" key="ef.opeInvConSearchPage.zona.label"/>: </label></td>
			<td class="normal">
				<html:select name="opeInvConSearchPageVO" property="opeInvCon.zona.id" styleClass="select">
					<html:optionsCollection name="opeInvConSearchPageVO" property="listZona" label="descripcion" value="id"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:text name="opeInvConSearchPageVO" property="opeInvCon.contribuyente.persona.represent" disabled="true" size="25" maxlength="100"/>
				<html:button property="btnBuscarContribuyente" styleClass="boton" onclick="submitForm('buscarPersona', '');">
					<bean:message bundle="ef" key="ef.ordenControlFisSearchPage.button.buscarPersona"/>
				</html:button>			
			</td>				
		</tr>
		
			<!-- <#Filtros#> -->
		</table>
			
		<p align="center">
		  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.buscar"/>
			</html:button>
		</p>
	</fieldset>	
	<!-- Fin Filtro -->
		
	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="opeInvConSearchPageVO" property="viewResult" value="true">
			<logic:notEmpty  name="opeInvConSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1"> <!-- Seleccionar -->
								<input type="checkbox" onclick="changeChk('filter', 'listIdContribSelected', this)"/>
							</th>
							<th><bean:message bundle="pad" key="pad.persona.label"/></th> <!-- Contribuyente -->
							<th><bean:message bundle="ef" key="ef.opeInvConSearchPage.zona.label"/></th>
							<th><bean:message bundle="pad" key="pad.persona.domicilio.label"/></th>
							<th><bean:message bundle="ef" key="ef.opeInv.label"/></th>
							<th><bean:message bundle="ef" key="ef.investigador.label"/>
							<th><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/></th> <!-- Estado -->
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="OpeInvConVO" name="opeInvConSearchPageVO" property="listResult">
							<tr>
								<td>
									<!-- Seleccionar -->
									<html:multibox name="opeInvConSearchPageVO" property="listIdContribSelected" >
                    	            	<bean:write name="OpeInvConVO" property="idView"/>
                        	        </html:multibox>
                                </td>								
								<td>
									<bean:write name="OpeInvConVO" property="contribuyente.persona.represent"/>
								</td>
								<td>
									<bean:write name="OpeInvConVO" property="zona.descripcion"/>
								</td>
								<td>
									<bean:write name="OpeInvConVO" property="domicilio.view"/>
								</td>
								<td>
									<bean:write name="OpeInvConVO" property="opeInv.desOpeInv"/>
								</td>
								<td>
									<bean:write name="OpeInvConVO" property="investigador.desInvestigador"/>
								<td>
									<bean:write name="OpeInvConVO" property="estadoOpeInvCon.desEstadoOpeInvCon"/>
								</td>
							</tr>
						</logic:iterate>
				
						<tr>
							<td class="paginador" align="center" colspan="20">
								<bean:define id="pager" name="opeInvConSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
						
					</tbody>
				</table>
			</logic:notEmpty>
			
			<logic:empty name="opeInvConSearchPageVO" property="listResult">
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
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<logic:equal name="opeInvConSearchPageVO" property="viewResult" value="true">
				<td align="right" width="50%">
					<input type="button" class="boton" onClick="submitForm('asignar', '0');" 
									value="<bean:message bundle="ef" key="ef.opeInvConActaSearchPage.button.seleccionar"/>"/>
				</td>
				<td>
					<input type="button" class="boton" 
									onClick="submitForm('hojaRuta', '0');" 
									value="<bean:message bundle="ef" key="ef.opeInvConActaSearchPage.button.hojaRuta"/>"/>					
				</td>
			</logic:equal>
		<tr>
	</table>
		
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
