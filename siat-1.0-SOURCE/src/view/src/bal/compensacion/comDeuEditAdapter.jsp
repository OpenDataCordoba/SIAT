<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/bal/AdministrarComDeu.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="bal" key="bal.compensacionAdapter.title"/></h1>		
		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>

		<!-- ComDeu -->
		<fieldset>
			<legend><bean:message bundle="bal" key="bal.comDeu.title"/></legend>			
			<table class="tabladatos">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal">
					<html:select name="comDeuAdapterVO" property="recurso.id" styleClass="select" >
						<bean:define id="includeRecursoList" name="comDeuAdapterVO" property="listRecurso"/>
						<bean:define id="includeIdRecursoSelected" name="comDeuAdapterVO" property="recurso.id"/>
						<%@ include file="/def/gravamen/includeRecurso.jsp" %>
					</html:select>
					</td>
		   		 </tr>
				<tr>
				<!-- Cuenta -->		
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
				    <td class="normal" colspan="4">
							<html:text name="comDeuAdapterVO" property="cuenta.numeroCuenta" size="20"/>
							<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
								<bean:message bundle="bal" key="bal.compensacion.button.buscarCuenta"/>
							</html:button>
					</td>
				</tr>
				<tr>
					<td><label>&nbsp;<bean:message bundle="bal" key="bal.compensacion.saldoRestante.label"/>: </label></td>
					<td class="normal"> <bean:write name="comDeuAdapterVO" property="saldoRestanteView"/></td>				
		   	   </tr>
			</table>
			<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('paramDeuda', '');">
						<bean:message bundle="base" key="abm.button.actualizar"/>
					</html:button>
				</td>
			</tr>
		</table>
		</fieldset>
		<!-- Fin ComDeu -->
		
		<!-- Lista de Deuda a Compensar-->
		<div style="border:0px" class="horizscroll">
		<table class="tramonline" border="0" cellbalding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="bal" key="bal.comDeu.listDeuda.title"/></caption>
	    	<tbody>
			    	<tr>
				    	<th align="center">
		       				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		       				<input type="checkbox" name="checkAll" onclick="changeChk('filter', 'listIdDeudaSelected', this)"/>
			       		</th>
					  	<th align="left"><bean:message bundle="bal" key="bal.comDeu.periodoDeuda.label"/></th>
  						<th align="left"><bean:message bundle="bal" key="bal.comDeu.fechaVenDeuda.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.comDeu.importeDeuda.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.comDeu.saldoDeuda.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.comDeu.actDeuda.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.comDeu.totalDeuda.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.comDeu.importe.label"/></th>
					  	<th align="left"><bean:message bundle="bal" key="bal.comDeu.cancelarPorMenos.label"/></th>
					</tr>
					<logic:notEmpty  name="comDeuAdapterVO" property="liqDeudaAdmin.listDeuda">	    	
					<logic:iterate id="LiqDeudaAdmin" name="comDeuAdapterVO" property="liqDeudaAdmin.listDeuda">
						<tr>
							<td align="center">
				  				<html:multibox name="comDeuAdapterVO" property="listIdDeudaSelected">
	        	     	            <bean:write name="LiqDeudaAdmin" property="idDeudaView"/>
	            	     	    </html:multibox>
			  				</td>
						  	<td><bean:write name="LiqDeudaAdmin" property="periodoDeuda"/>&nbsp;</td>
							<td><bean:write name="LiqDeudaAdmin" property="fechaVto"/>&nbsp;</td>
				        	<td><bean:write name="LiqDeudaAdmin" property="importe" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaAdmin" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaAdmin" property="actualizacion" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqDeudaAdmin" property="total" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
							<!-- El Recargo se usa para guardar y mostrar el saldo a imputar de la deuda, valor que se grabara en ComDeu-->
					        <td><input type="text" size="8" maxlength="100" name="valorDeuda<bean:write name="LiqDeudaAdmin" property="idDeudaView"/>"
												value="<bean:write name="LiqDeudaAdmin" property="recargoView"/>" id="valorDeuda<bean:write name="LiqDeudaAdmin" property="idDeudaView"/>" class="datos" />
						        &nbsp;</td> 	
						     <td class="normal"><input type="checkbox" name="cancelaPorMenos<bean:write name="LiqDeudaAdmin" property="idDeudaView"/>"></td>			        
						</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty  name="comDeuAdapterVO" property="liqDeudaAdmin.listDeuda">
					<tr><td colspan="9" align="center">
					<bean:message bundle="base" key="base.noExistenRegitros"/>
					</td></tr>
				</logic:empty>					
			</tbody>
		</table>
		</div>
		<!-- Fin Lista de Deuda a Compensar -->	

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
				<td align="right">
					<logic:equal name="comDeuAdapterVO" property="act" value="agregar">
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