<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarOrdenControlFis.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
					<logic:notEqual name="ordenControlFisAdapterVO" property="act" value="verOrdenAnterior">  	    			
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
							<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>	   	    			
					</logic:notEqual>	
					<logic:equal name="ordenControlFisAdapterVO" property="act" value="verOrdenAnterior">  	    			
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverDeOrdenAnterior', '');">
							<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>	   	    			
					</logic:equal>
				</td>
			</tr>
		</table>
		
		<!-- OrdenControlFis -->
		<bean:define id="ordenControlFis" name="ordenControlFisAdapterVO" property="ordenControl"/>
		<%@include file="/ef/fiscalizacion/includeOrdenControlFisView.jsp" %>
		<!-- OrdenControlFis -->

		<!-- lista de ordenes anteriores -->
		<logic:equal name="ordenControlFisAdapterVO" property="verListaOrdenesAnt" value="true">
			<a name="ordenesAnteriores">&nbsp;</a>
			<fieldset>
				<legend><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.listOrdenesAnt.title"/></legend>		
				<logic:notEmpty  name="ordenControlFisAdapterVO" property="listOrdenControlAnt">	
					<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControl.listOrdenesAnt.title"/></caption>
	               	<tbody>
		               	<tr>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th><bean:message bundle="ef" key="ef.ordenControlFisSearchPage.nroAnio.label"/></th> <!-- nro anio -->
							<th><bean:message bundle="ef" key="ef.ordenControl.fechaEmision.label"/></th> <!-- fecha emision -->
							<th><bean:message bundle="ef" key="ef.tipoOrden.label"/></th> <!-- tipoOrden -->
							<th><bean:message bundle="ef" key="ef.origen.label"/></th> <!-- origenOrden -->
							<th><bean:message bundle="ef" key="ef.inspector.label"/></th> <!-- inspector -->
							<th><bean:message bundle="ef" key="ef.supervisor.label"/></th> <!-- supervisor -->
							<th><bean:message bundle="ef" key="ef.estadoOrden.label"/></th> <!-- estado -->							
							<!-- <#ColumnTitles#> -->
						</tr>
							
						<logic:iterate id="OrdenControlFisVO" name="ordenControlFisAdapterVO" property="listOrdenControlAnt">
							<tr>
								<!-- ver -->
								<td>
									<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verOrdenAnterior', '<bean:write name="OrdenControlFisVO" property="id" bundle="base" formatKey="general.format.id"/>');">
										<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									</a>									
								</td>
								
								
								<!-- nro - anio -->
								<td>
									<bean:write name="OrdenControlFisVO" property="numeroOrden" bundle="base" formatKey="general.format.id"/>/<bean:write name="OrdenControlFisVO" property="anioOrden" bundle="base" formatKey="general.format.id"/>
								</td>

								<!-- fecha emision -->
								<td><bean:write name="OrdenControlFisVO" property="fechaEmisionView"/></td>
								
								<!-- tipoOrden -->
								<td><bean:write name="OrdenControlFisVO" property="tipoOrden.desTipoOrden"/></td>
								
								<!-- origenOrden -->
								<td><bean:write name="OrdenControlFisVO" property="origenOrden.desOrigen"/></td>
									
								<!-- inspector -->
								<td><bean:write name="OrdenControlFisVO" property="inspector.desInspector"/></td>
								
								<!-- supervisor -->
								<td><bean:write name="OrdenControlFisVO" property="supervisor.desSupervisor"/></td>
								
								<!-- estado -->
								<td><bean:write name="OrdenControlFisVO" property="estadoOrden.desEstadoOrden"/></td>														
							</tr>
						</logic:iterate>																			
					</tbody>
				</table>
			</logic:notEmpty>
					
			<logic:empty name="ordenControlFisAdapterVO" property="listOrdenControlAnt">
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControl.listOrdenesAnt.title"/></caption>
	               	<tbody>
						<tr><td align="center">
							<bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.listOrdenesAnt.vacio"/>
						</td></tr>
					</tbody>			
				</table>
			</logic:empty>	
			</fieldset>	
		</logic:equal>
		<!-- FIN lista de ordenes anteriores -->
		
		
		<table class="tablabotones" width="100%">
			<tr>
	   	    	<td align="center">
	   	    	   <logic:equal name="ordenControlFisAdapterVO" property="verHistoricoBussEnabled" value="true">
		   	    	    <html:button property="btnHist"  styleClass="boton" onclick="submitForm('histAcciones', '');">
						    <bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.histAcciones"/>
					    </html:button>
					</logic:equal>
	   	    	   <logic:equal name="ordenControlFisAdapterVO" property="verOrdenesAntBussEnabled" value="true">					    
		   	    	    <html:button property="btnOrdAnt"  styleClass="boton" onclick="submitForm('ordenesAnteriores', '1');">
						    <bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.button.ordenesAnt"/>
					    </html:button>					    
					</logic:equal>	    
	   	    	</td>
			
			</tr>
		
	    	<tr>
  	    		<td align="left">
					<logic:notEqual name="ordenControlFisAdapterVO" property="act" value="verOrdenAnterior">  	    			
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
							<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>	   	    			
					</logic:notEqual>	
					<logic:equal name="ordenControlFisAdapterVO" property="act" value="verOrdenAnterior">  	    			
			   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volverDeOrdenAnterior', '');">
							<bean:message bundle="base" key="abm.button.volver"/>
						</html:button>	   	    			
					</logic:equal>					
					
	   	    	</td>
	   	    </tr>
	   	 </table>
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
		<logic:present name="irA">
			<script type="text/javascript">document.location = document.URL + '#<bean:write name="irA"/>';</script>
		</logic:present>
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- ordenControlFisViewAdapter.jsp -->
