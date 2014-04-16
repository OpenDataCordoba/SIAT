<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/exe/AdministrarContribExe.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<h1><bean:message bundle="exe" key="exe.contribExeEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- Contribuyente -->
    <fieldset>
    	<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>
        <table class="tabladatos">
			<bean:define id="personaVO" name="contribExeAdapterVO" property="contribExe.contribuyente.persona"/>
			<%@ include file="/pad/persona/includePersonaReducida.jsp"%>

			<logic:equal name="contribExeAdapterVO" property="act" value="agregar">

			<tr>
				<td align="right" colspan="4">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('buscarContribuyente', '');">
						<bean:message bundle="exe" key="exe.contribExeEditAdapter.adm.button.buscarContribuyente"/>
					</html:button>
				</td>
			</tr>

			</logic:equal>

		</table>
	</fieldset>


      <!-- Broche -->
      <fieldset>
         <legend><bean:message bundle="pad" key="pad.broche.title"/></legend>
         <table class="tabladatos">

         <logic:equal name="contribExeAdapterVO" property="poseeBroche" value="false">
			<tr>
				<td class="normal"><label>No posee broche asignado.</label></td>
			</tr>
			<tr>
				<td align="right" colspan="4">
					<logic:equal name="contribExeAdapterVO" property="asignarBrocheEnabled" value="enabled">
					    <input type="button" class="boton" onclick="submitForm('asignarBroche',
						'<bean:write name="contribExeAdapterVO" property="contribExe.id" bundle="base" formatKey="general.format.id"/>');"
						value="<bean:message bundle="base" key="abm.button.asignar"/>"/>
					    
					</logic:equal>
				</td>
			</tr>
         </logic:equal>
         
         <logic:equal name="contribExeAdapterVO" property="poseeBroche" value="true">
			<tr>
				<!-- Numero -->		
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.nro.label"/>: </label></td>
				<td class="normal"><bean:write name="contribExeAdapterVO" property="contribExe.broche.idView"/></td>					
				<!-- Tipo Broche -->		
				<td><label>&nbsp;<bean:message bundle="pad" key="pad.tipoBroche.label"/>: </label></td>
				<td class="normal"><bean:write name="contribExeAdapterVO" property="contribExe.broche.tipoBroche.desTipoBroche"/></td>
			</tr>
			<!-- Titular o Descripcion-->		
			<tr>
				<logic:equal name="contribExeAdapterVO" property="contribExe.broche.tipoBroche.id" value="1">	<!-- TipoBroche=Administrativo -->	
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.titular.label"/>: </label></td>
				</logic:equal>
				<logic:notEqual name="contribExeAdapterVO" property="contribExe.broche.tipoBroche.id" value="1">	<!-- TipoBroche<>Administrativo -->		
					<td><label>&nbsp;<bean:message bundle="pad" key="pad.broche.desBroche.label"/>: </label></td>
				</logic:notEqual>
				<td class="normal"><bean:write name="contribExeAdapterVO" property="contribExe.broche.desBroche"/></td>					
			</tr>

			<logic:equal name="contribExeAdapterVO" property="act" value="modificar">
			<tr>				
				<td align="right" colspan="4">
					<logic:equal name="contribExeAdapterVO" property="asignarBrocheEnabled" value="enabled">
					    <input type="button" class="boton" onclick="submitForm('asignarBroche', 
						'<bean:write name="contribExeAdapterVO" property="contribExe.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.modificar"/>"/>

					    <input type="button" class="boton" onclick="submitForm('quitarBrocheInit', 
						'<bean:write name="contribExeAdapterVO" property="contribExe.id" bundle="base" formatKey="general.format.id"/>');" 
						value="<bean:message bundle="base" key="abm.button.quitar"/>"/>
					    
				    </logic:equal>
				</td>
			</tr>
		    </logic:equal>

		    <logic:equal name="contribExeAdapterVO" property="act" value="agregar">
			<tr>
				<td align="right" colspan="4">
					<logic:equal name="contribExeAdapterVO" property="asignarBrocheEnabled" value="enabled">
					    <input type="button" class="boton" onclick="submitForm('asignarBroche',
						'<bean:write name="contribExeAdapterVO" property="contribExe.id" bundle="base" formatKey="general.format.id"/>');"
						value="<bean:message bundle="base" key="abm.button.asignar"/>"/>
					    
					</logic:equal>
				</td>
			</tr>
            </logic:equal>

         </logic:equal>

         </table>
      </fieldset>
      <!-- Fin Broche -->

	<fieldset>
		<legend><bean:message bundle="exe" key="exe.exencion.title"/></legend>
		
		<table class="tabladatos">
			<tr>	
				<td><label><bean:message bundle="exe" key="exe.exencion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<bean:write name="contribExeAdapterVO" property="contribExe.exencion.desExencion"/>
			        <logic:equal name="contribExeAdapterVO" property="act" value="agregar">
					  <html:select name="contribExeAdapterVO" property="contribExe.exencion.id" styleClass="select" onchange="submitForm('paramExencion', '');"> 
						  <html:optionsCollection name="contribExeAdapterVO" property="listExencion" label="desExencion" value="id" />
					  </html:select>
                    </logic:equal>
           
				</td>					
			</tr>

			<!-- Descripcion -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="exe" key="exe.contribExe.desContribExe.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="contribExeAdapterVO" property="contribExe.desContribExe" size="50" maxlength="100"/></td>			
			</tr>
		
			<!-- Exencion -->
			
			<!-- Fecha Desde/Hasta -->			
			<tr>
				<td>
					<label>
						<logic:equal name="contribExeAdapterVO" property="poseeExencion" value="true">
							(*)&nbsp;
						</logic:equal>					
						<bean:message bundle="exe" key="exe.contribExe.fechaDesde.label"/>: 
					</label>					
				</td>
				<td class="normal">
					<html:text name="contribExeAdapterVO" property="contribExe.fechaDesdeView" styleId="fechaDesdeView" size="15" 
						maxlength="10" styleClass="datos"/>
					<a class="link_siat" onclick="return show_calendar('fechaDesdeView');" id="a_fechaDesdeView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
				<td><label><bean:message bundle="exe" key="exe.contribExe.fechaHasta.label"/>: </label></td>
				<td class="normal">
					<html:text name="contribExeAdapterVO" property="contribExe.fechaHastaView" styleId="fechaHastaView" size="15" 
						maxlength="10" styleClass="datos"/>
					<a class="link_siat" onclick="return show_calendar('fechaHastaView');" id="a_fechaHastaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>				
			</tr>
		
		</table>
	</fieldset>	
	<!-- ContribExe -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="contribExeAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="contribExeAdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
