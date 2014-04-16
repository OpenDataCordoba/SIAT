<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/bal/BuscarArchivo.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.archivoSearchPage.title"/></h1>	
		
		<table class="tablabotones" width="100%">
			<tr>
				<td align="left">
					<p><bean:message bundle="bal" key="bal.archivoSearchPage.legend"/></p>		
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
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.archivo.prefix.label"/>: </label></td>
						<td class="normal"><html:text name="archivoSearchPageVO" property="archivo.prefix" size="10" maxlength="2" /></td>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.archivo.nroBanco.label"/>: </label></td>
						<td class="normal"><html:text name="archivoSearchPageVO" property="archivo.nroBancoView" size="10" maxlength="10" /></td>
					</tr>
					<!-- Fecha Banco Desde/Hasta -->
					<tr>
						<td><label><bean:message bundle="bal" key="bal.archivoSearchPage.fechaBancoDesde.label"/>: </label></td>
						<td class="normal">
						<html:text name="archivoSearchPageVO" property="fechaBancoDesdeView" styleId="fechaBancoDesdeView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBancoDesdeView');" id="a_fechaBancoDesdeView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
						<td><label><bean:message bundle="bal" key="bal.archivoSearchPage.fechaBancoHasta.label"/>: </label></td>
						<td class="normal">
						<html:text name="archivoSearchPageVO" property="fechaBancoHastaView" styleId="fechaBancoHastaView" size="10" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaBancoHastaView');" id="a_fechaBancoHastaView">
							<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
						</td>	
				    </tr>		    
					<tr>
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.tipoArc.label"/>: </label></td>
						<td class="normal">
						<html:select name="archivoSearchPageVO" property="archivo.tipoArc.id" styleClass="select" >
							<html:optionsCollection name="archivoSearchPageVO" property="listTipoArc" label="descripcion" value="id" />
						</html:select>
						</td>		
						<td><label>&nbsp;<bean:message bundle="bal" key="bal.estadoArc.label"/>: </label></td>
						<td class="normal">
						<html:select name="archivoSearchPageVO" property="archivo.estadoArc.id" styleClass="select" >
							<html:optionsCollection name="archivoSearchPageVO" property="listEstadoArc" label="descripcion" value="id" />
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
		  		<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
					<bean:message bundle="base" key="abm.button.buscar"/>
				</html:button>
			</p>
		</fieldset>	
		<!-- Fin Filtro -->

	<!-- Resultado Filtro -->
	<div id="resultadoFiltro">
		<logic:equal name="archivoSearchPageVO" property="viewResult" value="true">		
			<logic:notEmpty  name="archivoSearchPageVO" property="listResult">	
				<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
					<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
                	<tbody>
	                	<tr>
							<th align="center">
					       			<br>
					       			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<logic:equal name="archivoSearchPageVO" property="modoSeleccionar" value="true">
					       			<input type="checkbox" onclick="changeChk('filter', 'listIdArchivoSelected', this)" id="checkAll"/>
								</logic:equal>
				       		</th> <!-- Ver/Seleccionar -->
							<logic:notEqual name="archivoSearchPageVO" property="modoSeleccionar" value="true">
								<th width="1">&nbsp;</th> <!-- Aceptar -->
								<th width="1">&nbsp;</th> <!-- Anular -->
							</logic:notEqual>
						  	<th align="left"><bean:message bundle="bal" key="bal.archivo.fechaBanco.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.tipoArc.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.archivo.nroBanco.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.archivo.cantTrans.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.archivo.total.label"/></th>
						  	<th align="left"><bean:message bundle="bal" key="bal.archivo.nombre.label"/></th>
  							<th align="left"><bean:message bundle="base" key="base.estado.label"/></th>
						</tr>
						
						<logic:iterate id="ArchivoVO" name="archivoSearchPageVO" property="listResult">
							<tr>						
								<!-- Ver/Seleccionar -->
								<td align="center">
									<logic:notEqual name="archivoSearchPageVO" property="modoSeleccionar" value="true">
										<a style="cursor: pointer; cursor: hand;" onclick="submitForm('ver', '<bean:write name="ArchivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
											<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
										</a>
									</logic:notEqual>
									<logic:equal name="archivoSearchPageVO" property="modoSeleccionar" value="true">
									<!-- Seleccionar -->
	                          			<html:multibox name="archivoSearchPageVO" property="listIdArchivoSelected" >
	                         			   	<bean:write name="ArchivoVO" property="id" bundle="base" formatKey="general.format.id"/>
		                                </html:multibox>	  		
									</logic:equal>									
								</td>

								<logic:notEqual name="archivoSearchPageVO" property="modoSeleccionar" value="true">								
								<td>
									<!-- Aceptar-->								
									<logic:equal name="archivoSearchPageVO" property="aceptarEnabled" value="enabled">		
										<logic:equal name="ArchivoVO" property="aceptarEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('aceptar', '<bean:write name="ArchivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.archivoSearchPage.adm.button.aceptar"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/aceptar0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="ArchivoVO" property="aceptarEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/aceptar1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="archivoSearchPageVO" property="aceptarEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/aceptar1.gif"/>
									</logic:notEqual>
								</td>
								<td>
									<!-- Anular-->								
									<logic:equal name="archivoSearchPageVO" property="anularEnabled" value="enabled">		
										<logic:equal name="ArchivoVO" property="anularEnabled" value="enabled">
											<a style="cursor: pointer; cursor: hand;" onclick="submitForm('anular', '<bean:write name="ArchivoVO" property="id" bundle="base" formatKey="general.format.id"/>');">
												<img title="<bean:message bundle="bal" key="bal.archivoSearchPage.adm.button.anular"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/anular0.gif"/>
											</a>
										</logic:equal>	
										<logic:notEqual name="ArchivoVO" property="anularEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/anular1.gif"/>
										</logic:notEqual>									
									</logic:equal>							
									<logic:notEqual name="archivoSearchPageVO" property="anularEnabled" value="enabled">		
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/anular1.gif"/>
									</logic:notEqual>
								</td>
							</logic:notEqual>								

							    <td><bean:write name="ArchivoVO" property="fechaBancoView"/>&nbsp;</td>
						  	    <td><bean:write name="ArchivoVO" property="tipoArc.descripcion"/>&nbsp;</td>
   						  	    <td><bean:write name="ArchivoVO" property="nroBancoView"/>&nbsp;</td>
   						  	    <td><bean:write name="ArchivoVO" property="cantTransView"/>&nbsp;</td>
   						  	    <td><bean:write name="ArchivoVO" property="totalView"/>&nbsp;</td>
	 					  	    <td><bean:write name="ArchivoVO" property="nombre"/>&nbsp;</td>
						 		<td><bean:write name="ArchivoVO" property="estadoArc.descripcion" />&nbsp;</td>						
							</tr>
						</logic:iterate>
					<logic:notEqual name="archivoSearchPageVO" property="modoSeleccionar" value="true">		
						<tr>
							<td class="paginador" align="center" colspan="12">
								<bean:define id="pager" name="archivoSearchPageVO"/>
								<%@ include file="/base/pager.jsp" %>
							</td>
						</tr>
					</logic:notEqual>	
				</tbody>
			</table>
		</logic:notEmpty>
		
		<logic:empty name="archivoSearchPageVO" property="listResult">
			<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
        	    	<tbody>
						<caption><bean:message bundle="base" key="base.resultadoBusqueda"/></caption>
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
				<logic:equal name="archivoSearchPageVO" property="modoSeleccionar" value="true">
							<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('incluir', '');">
								<bean:message bundle="bal" key="bal.archivoSearchPage.adm.button.incluir"/>
							</html:button>
				</logic:equal>
			</td>
		</tr>
	</table>
	<input type="hidden" name="name"  value="<bean:write name='archivoSearchPageVO' property='name'/>" id="name"/>
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
	<script type="text/javascript">
		function setChk(){
			var form = document.getElementById('filter');		
			form.elements['checkAll'].click();		
		}
	</script>

	<script type="text/javascript">setChk();</script>
<!-- Fin Tabla que contiene todos los formularios -->		