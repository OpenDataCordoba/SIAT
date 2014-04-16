<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOpeInvConActas.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>

	<h1><bean:message bundle="ef" key="ef.actasInicioAdapter.AsignarInvestigador.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right">
	 			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<logic:greaterThan name="actasInicioAdapterVO" property="cantOpeInvCon" value="15">
		<div class="scrolable" style="height: 545px;">		
	</logic:greaterThan>					
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
		<caption><bean:message bundle="ef" key="ef.actasInicioAdapter.listaContrib.title"/></caption>
             	<tbody>
              	<tr>
					<th width="1"> <!-- Seleccionar -->
						<input type="checkbox" onclick="changeChk('filter', 'idsSelectedForAsignar', this)"/>
					</th>
					<th><bean:message bundle="pad" key="pad.persona.label"/></th> <!-- Contribuyente -->
					<th><bean:message bundle="ef" key="ef.opeInvConSearchPage.zona.label"/></th>
					<th><bean:message bundle="pad" key="pad.persona.domicilio.label"/></th>
					<th><bean:message bundle="ef" key="ef.opeInv.label"/></th>
					<th><bean:message bundle="ef" key="ef.investigador.label"/>
					<th><bean:message bundle="ef" key="ef.estadoOpeInvCon.label"/></th> <!-- Estado -->
					<th><bean:message bundle="ef" key="ef.opeInvConActas.asignInv.fechaVisita.label"/></th> <!-- Estado -->
					<!-- <#ColumnTitles#> -->
			</tr>
				
			<logic:iterate id="OpeInvConVO" name="actasInicioAdapterVO" property="listOpeInvCon">
				<tr>
					<td>
							<!-- Seleccionar -->
							<html:multibox name="actasInicioAdapterVO" property="idsSelectedForAsignar" >
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

					<td>
						<bean:write name="OpeInvConVO" property="fechaVisitaView"/>
					</td>					
					
				</tr>
			</logic:iterate>			
		</tbody>
	</table>
	
	<logic:greaterThan name="actasInicioAdapterVO" property="cantOpeInvCon" value="15">
		</div>
	</logic:greaterThan>
	
	<table width="100%">
		<!-- investigador -->
		<tr>
			<td align="right">
				<label>(*)&nbsp;<bean:message bundle="ef" key="ef.investigador.label"/>: </label>
			</td>	
			<td align="left">						
				<html:select name="actasInicioAdapterVO" property="investigador.id">
					<html:optionsCollection name="actasInicioAdapterVO" property="listInvestigador" label="desInvestigador" value="id"/>
				</html:select>
			</td>
		</tr>
		
		<!-- fecha visita -->
		<tr>
			<td align="right">
					<label>(*)&nbsp;<bean:message bundle="ef" key="ef.opeInvConActas.asignInv.fechaVisita.label"/>: </label>
			</td>
			<td align="left">							
					<html:text name="actasInicioAdapterVO" property="fechaVisitaView" styleId="fechaVisitaView" size="15" maxlength="10" styleClass="datos" />
						<a class="link_siat" onclick="return show_calendar('fechaVisitaView');" id="a_fechaVisitaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
			</td>				
		</tr>	
	</table>
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left" width="50%">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
				<td align="right" width="50%">
					<input type="button" class="boton" onClick="submitForm('asignar', '0');" 
									value="<bean:message bundle="ef" key="ef.actasInicioAdapter.button.asignar"/>"/>
				</td>
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
<!-- pageName: actasInicioInvestigadorAdapter.jsp -->