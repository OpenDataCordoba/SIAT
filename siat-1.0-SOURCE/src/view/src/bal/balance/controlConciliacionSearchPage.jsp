<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/ControlConciliacion.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.legend"/></p>		
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
			<legend><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.filtrosArchivo"/></legend>
				<table class="tabladatos">
					<!-- Fecha Banco Desde/Hasta -->
					<tr>
						<td><label><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.fechaBancoDesde.label"/>: </label></td>
						<td class="normal">
						<html:text name="controlConciliacionSearchPageVO" property="fechaBancoDesdeView" styleId="fechaBancoDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBancoDesdeView');" id="a_fechaBancoDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
						<td><label><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.fechaBancoHasta.label"/>: </label></td>
						<td class="normal">
						<html:text name="controlConciliacionSearchPageVO" property="fechaBancoHastaView" styleId="fechaBancoHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBancoHastaView');" id="a_fechaBancoHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
				    </tr>		    
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.tipoArc.label"/>: </label></td>
						<td class="normal">
						<html:select name="controlConciliacionSearchPageVO" property="tipoArc.id" styleClass="select" >
							<html:optionsCollection name="controlConciliacionSearchPageVO" property="listTipoArc" label="descripcion" value="id" />
						</html:select>
						</td>			
				   </tr>		    
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.controlConciliacionSearchPage.prefijosAExcluir.label"/>: </label></td>
						<td class="normal" colspan="4">
							<html:text name="controlConciliacionSearchPageVO" property="prefijosAExcluir" size="20" maxlength="100" />
							(&nbsp;<bean:message bundle="bal" key="bal.controlConciliacionSearchPage.prefijosAExcluir.description"/>&nbsp;)
						</td>
				    </tr>				    
				</table>
		</fieldset>	
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.filtrosCaja7"/></legend>
				<table class="tabladatos">
					<!-- Fecha AuxCaja7 Desde/Hasta -->
					<tr>
						<td><label><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.fechaCaja7Desde.label"/>: </label></td>
						<td class="normal">
						<html:text name="controlConciliacionSearchPageVO" property="fechaCaja7DesdeView" styleId="fechaCaja7DesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaCaja7DesdeView');" id="a_fechaCaja7DesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
						<td><label><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.fechaCaja7Hasta.label"/>: </label></td>
						<td class="normal">
						<html:text name="controlConciliacionSearchPageVO" property="fechaCaja7HastaView" styleId="fechaCaja7HastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaCaja7HastaView');" id="a_fechaCaja7HastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
				    </tr>		    
					<tr>
						<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
						<td class="normal">
							<html:select name="controlConciliacionSearchPageVO" property="auxCaja7.estado.id" styleClass="select" >
								<html:optionsCollection name="controlConciliacionSearchPageVO" property="listEstado" label="value" value="id" />
							</html:select>
						</td>			
				   </tr>		    		    
				</table>
			
		</fieldset>	
		<p align="center">
		  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
				<bean:message bundle="base" key="abm.button.limpiar"/>
			</html:button>
			&nbsp;
	  		<html:button property="btnCalcular"  styleClass="boton" onclick="submitForm('buscar', '');">
				<bean:message bundle="base" key="abm.button.calcular"/>
			</html:button>
		</p>
		<!-- Fin Filtro -->
		
		<!-- Aclaracion -->
		<!-- table>
			<tr>
				<td><label><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.nota.label"/>: </label></td>		
				<td><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.nota.description"/></td>		
		    </tr>				    
		</table -->
		<!-- Fin Aclaracion -->
		
	<!-- Resultado Filtro -->
	<logic:equal name="controlConciliacionSearchPageVO" property="paramResultado" value="true">		
	<div id="resultadoFiltro">
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.controlConciliacionSearchPage.resultado"/></legend>	
				<table class="tabladatos">    
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.controlConciliacionSearchPage.totalCalculado.label"/>: </label></td>
						<td class="normal"><bean:write name="controlConciliacionSearchPageVO" property="totalCalculado"/></td>
				    </tr>				    
				</table>
		</fieldset>	
	</div>
	</logic:equal>
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
	<input type="hidden" name="name"  value="<bean:write name='controlConciliacionSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>
		
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="pageNumber" value="1" id="pageNumber">
	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="text" style="display:none"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>

</html:form>

<!-- Fin Tabla que contiene todos los formularios -->		