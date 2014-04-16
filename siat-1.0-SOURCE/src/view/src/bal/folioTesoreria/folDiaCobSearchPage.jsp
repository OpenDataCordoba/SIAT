<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript">

function colorearFila(check) {
	var colorNoConciliado="#FFFFFF";
	var colorConciliado="#E0E0E0";
	var fila = document.getElementById(check.value); 
	if(check.checked)
		fila.style.backgroundColor=colorConciliado;
	else
		fila.style.backgroundColor=colorNoConciliado;
}

function colorearLista() {
	
	var form = document.getElementById('filter');
	
	if (!(typeof(form.elements['listIdFolDiaCobConciliado'])=='undefined')){
	
		var checks = form.elements['listIdFolDiaCobConciliado'];		
		if ( typeof(checks.length) == 'undefined' || checks.length == 1 ){				
			colorearFila(checks);
		} else {
		  for (i=0; i < checks.length; i++)	{
				colorearFila(checks[i]);
			}
		}
	}
}


</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarFolDiaCob.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.folDiaCobSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="bal" key="bal.folDiaCobSearchPage.legend"/></p>
					
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
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.folDiaCobSearchPage.fechaFolioDesde.label"/>: </label></td>
						<td class="normal">
						<html:text name="folDiaCobSearchPageVO" property="fechaFolioDesdeView" styleId="fechaFolioDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaFolioDesdeView');" id="a_fechaFolioDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
						<td><label><bean:message bundle="bal" key="bal.folDiaCobSearchPage.fechaFolioHasta.label"/>: </label></td>
						<td class="normal">
						<html:text name="folDiaCobSearchPageVO" property="fechaFolioHastaView" styleId="fechaFolioHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaFolioHastaView');" id="a_fechaFolioHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
				    </tr>	
				    <tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.folDiaCobSearchPage.nroFolioDesde.label"/>: </label></td>
						<td class="normal"><html:text name="folDiaCobSearchPageVO" property="nroFolioDesdeView" size="10" maxlength="10" /></td>						
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.folDiaCobSearchPage.nroFolioHasta.label"/>: </label></td>
						<td class="normal"><html:text name="folDiaCobSearchPageVO" property="nroFolioHastaView" size="10" maxlength="10" /></td>						
				    </tr>
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.folDiaCobSearchPage.fechaCobDesde.label"/>: </label></td>
						<td class="normal">
						<html:text name="folDiaCobSearchPageVO" property="fechaCobDesdeView" styleId="fechaCobDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaCobDesdeView');" id="a_fechaCobDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
						<td><label><bean:message bundle="bal" key="bal.folDiaCobSearchPage.fechaCobHasta.label"/>: </label></td>
						<td class="normal">
						<html:text name="folDiaCobSearchPageVO" property="fechaCobHastaView" styleId="fechaCobHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaCobHastaView');" id="a_fechaCobHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
				    </tr>	
				     <tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.folDiaCobSearchPage.descripcion.label"/>: </label></td>
						<td class="normal"><html:text name="folDiaCobSearchPageVO" property="descripcion" size="15" maxlength="15" /></td>	
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.tipoCob.totalesCol.label"/>: </label></td>
						<td class="normal"><html:text name="folDiaCobSearchPageVO" property="importeTotalFilaFiltroView" size="10" maxlength="10" /></td>							
				    </tr>		  
				    <tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.estadoFol.label"/>: </label></td>
						<td class="normal">
						<html:select name="folDiaCobSearchPageVO" property="folDiaCob.folio.estadoFol.id" styleClass="select" >
							<html:optionsCollection name="folDiaCobSearchPageVO" property="listEstadoFol" label="descripcion" value="id" />
						</html:select>
						</td>	
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.folDiaCobSearchPage.conciliado"/>: </label></td>
						<td class="normal">	
							<html:select name="folDiaCobSearchPageVO" property="folDiaCob.estaConciliado.id" styleClass="select">
								<html:optionsCollection name="folDiaCobSearchPageVO" property="listConciliado" label="value" value="id" />
							</html:select>
						</td>	
				    </tr>	
				</table>
			
			<p align="center">
			  	<html:button property="btnLimpiar"  styleClass="boton" onclick="submitForm('limpiar', '');">
					<bean:message bundle="base" key="abm.button.limpiar"/>
				</html:button>
				&nbsp;
				<html:button property="btnImprimir"  styleClass="boton" onclick="submitImprimir('imprimirReportFromSearchPage', '1');">
					<bean:message bundle="base" key="abm.button.imprimir"/>
				</html:button>
				&nbsp;
				<html:button property="btnPlanilla"  styleClass="boton" onclick="submitDownload('generarPlanilla', '');">
					<bean:message bundle="base" key="abm.button.planilla"/>
				</html:button>
				&nbsp;
		  		<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</p>
		</fieldset>	
		<!-- Fin Filtro -->

	<!-- Resultado Filtro -->
	<div id="resultadoFiltro"  style="border:0px" class="horizscroll">
		<logic:equal name="folDiaCobSearchPageVO" property="viewResult" value="true">		
			<logic:notEmpty  name="folDiaCobSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
	                	<tr>
						  	<th align="left"><bean:message bundle="bal" key="bal.folio.numero.ref"/></th>
  							<th align="left"><bean:message bundle="bal" key="bal.folDiaCob.fechaCob.label"/></th>
  							<logic:notEmpty  name="folDiaCobSearchPageVO" property="listTipoCob">	    	
								<logic:iterate id="TipoCobVO" name="folDiaCobSearchPageVO" property="listTipoCob">				
									<th align="left"><bean:write name="TipoCobVO" property="descripcion"/>&nbsp;</th>
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty  name="folDiaCobSearchPageVO" property="listTipoCob">
									<th align="left"><bean:message bundle="bal" key="bal.tipoCob.noExistenColumnas.label"/></th>
							</logic:empty>	
							<th align="left"><bean:message bundle="bal" key="bal.tipoCob.totalesCol.label"/></th>
	                		<th align="center">&nbsp;</th> <!-- Conciliado -->
						</tr>
						
						<logic:iterate id="FolDiaCobVO" name="folDiaCobSearchPageVO" property="listResult">
							<bean:define id="idFila" name="FolDiaCobVO" property="idView"/>
							<tr id="<%=idFila%>">	
						  	    <td><bean:write name="FolDiaCobVO" property="folio.numeroView"/>&nbsp;</td>
						 		<!-- td><bean:write name="FolDiaCobVO" property="folio.estadoFol.descripcion"/>&nbsp;</td -->
						 		<td align="right"><bean:write name="FolDiaCobVO" property="fechaOrDesc"/>&nbsp;</td>
								<logic:notEmpty  name="FolDiaCobVO" property="listFolDiaCobCol">	    	
									<logic:iterate id="FolDiaCobColVO" name="FolDiaCobVO" property="listFolDiaCobCol">				
										<td align="right"><bean:write name="FolDiaCobColVO" property="importeView"/>&nbsp;</td>
									</logic:iterate>
								</logic:notEmpty>
								<logic:empty  name="FolDiaCobVO" property="listFolDiaCobCol">
										<td>&nbsp;</td>
								</logic:empty>	
								<td align="right"><bean:write name="FolDiaCobVO" property="totalView"/>&nbsp;</td>
								<!-- Conciliado -->
								<td align="center">
									<html:multibox name="folDiaCobSearchPageVO" property="listIdFolDiaCobConciliado" onchange="colorearFila(this);">
	                         		   	<bean:write name="FolDiaCobVO" property="id" bundle="base" formatKey="general.format.id"/>
		                            </html:multibox>	  								
								</td>
							</tr>
						</logic:iterate>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<logic:notEmpty  name="folDiaCobSearchPageVO" property="listTotales">	    	
								<logic:iterate id="FolDiaCobColVO" name="folDiaCobSearchPageVO" property="listTotales">				
									<td align="right"><bean:write name="FolDiaCobColVO" property="importeView"/>&nbsp;</td>
								</logic:iterate>
							</logic:notEmpty>
							<logic:empty  name="folDiaCobSearchPageVO" property="listTotales">
									<td>&nbsp;</td>
							</logic:empty>
							<td align="right"><bean:write name="folDiaCobSearchPageVO" property="totalDiaCobView"/>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="folDiaCobSearchPageVO" property="listResult">
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
				<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
       	    	<tbody>
					<tr><td align="center">
							<bean:message bundle="base" key="base.resultadoVacio"/>
						</td>
					</tr>
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
			<td align="right">
				<bean:define id="guardarEnabled" name="folDiaCobSearchPageVO" property="guardarEnabled"/>
				<input type="button" <%=guardarEnabled%> class="boton" 
					onClick="submitForm('guardar', '0');" 
					value="<bean:message bundle="base" key="abm.button.guardar"/>"/>
			</td>
		</tr>
	</table>
		
	<input type="hidden" name="method" value=""/>
    <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
    <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>

	<input type="hidden" name="pageMethod" value="buscar" id="pageMethod">
	<input type="hidden" name="isSubmittedForm" value="true"/>
	<input type="hidden" name="name" value="<bean:write name='folDiaCobSearchPageVO' property='name'/>" id="name"/>
	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/>	
	<input type="hidden" name="fileParam" value=""/>

	<input type="text" style="display:none"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>

<script type="text/javascript">colorearLista();</script>
<!-- Fin Tabla que contiene todos los formularios -->		
