<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarAuxDeuda.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="emi" key="emi.auxDeudaViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- AuxDeuda -->
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.auxDeuda.title"/></legend>
			<table class="tabladatos">
				<tr>
					<!-- Recurso -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.recurso.desRecurso"/></td>
				</tr>

				<tr>
					<!-- Cuenta -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.cuenta.label"/>: </label></td>
					<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.cuenta.numeroCuenta"/></td>
				</tr>

				<tr>
					<!-- Deuda -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.deuda.label"/>: </label></td>
					<td class="normal">
						<bean:write name="auxDeudaAdapterVO" property="auxDeuda.periodoView"/>/
						<bean:write name="auxDeudaAdapterVO" property="auxDeuda.anioView"/>
					</td>
				</tr>

				<tr>
					<!-- Cod. Ref. Pag. -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.codRefPag.label"/>: </label></td>
					<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.codRefPagView"/></td>
				</tr>

				<tr>
					<!-- Importe -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.importe.label"/>: </label></td>
					<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.importeView"/></td>
				</tr>

				<tr>
					<!-- Importe Bruto -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.importeBruto.label"/>: </label></td>
					<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.importeBrutoView"/></td>
				</tr>

				<tr>
					<!-- Fecha Vencimiento -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.fechaVencimiento.label"/>: </label></td>
					<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.fechaVencimientoView"/></td>
				</tr>

				<tr>
					<!-- Via -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.viaDeuda.label"/>: </label></td>
					<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.viaDeuda.desViaDeuda"/></td>
				</tr>

				<tr>
					<!-- Servicio Banco -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.servicioBanco.label"/>: </label></td>
					<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.servicioBanco.desServicioBanco"/></td>
				</tr>

				<tr>
					<!-- Sistema -->
					<td><label><bean:message bundle="emi" key="emi.auxDeuda.sistema.label"/>: </label></td>
					<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.sistema.desSistema"/></td>
				</tr>
			</table>
		</fieldset>	
		<!-- AuxDeuda -->

		<!-- Conceptos -->
		<fieldset>
			<legend><bean:message bundle="emi" key="emi.auxDeudaViewAdapter.conceptos.label"/></legend>
			<table class="tabladatos">
				<logic:equal name="auxDeudaAdapterVO" property="mostrarConcepto1" value="true">
					<tr>
						<td><label><bean:write name="auxDeudaAdapterVO" property="nameConcepto1"/>: </label></td>
						<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.conc1View"/>&nbsp;</td>		
					</tr>
				</logic:equal>
	
				<logic:equal name="auxDeudaAdapterVO" property="mostrarConcepto2" value="true">
					<tr>								
						<td><label><bean:write name="auxDeudaAdapterVO" property="nameConcepto2"/>: </label></td>
						<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.conc2View"/>&nbsp;</td>		
					</tr>
				</logic:equal>
	
				<logic:equal name="auxDeudaAdapterVO" property="mostrarConcepto3" value="true">								
					<tr>								
						<td><label><bean:write name="auxDeudaAdapterVO" property="nameConcepto3"/>: </label></td>
						<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.conc3View"/>&nbsp;</td>		
					</tr>
				</logic:equal>
	
				<logic:equal name="auxDeudaAdapterVO" property="mostrarConcepto4" value="true">								
					<tr>								
						<td><label><bean:write name="auxDeudaAdapterVO" property="nameConcepto4"/>: </label></td>
						<td class="normal"><bean:write name="auxDeudaAdapterVO" property="auxDeuda.conc4View"/>&nbsp;</td>		
					</tr>
				</logic:equal>
			</table>
		</fieldset>	

		<!-- Atributos -->
		<fieldset>
			<legend>Atributos</legend>
             <p align="left">
                 <html:textarea style="width:100%; height:270px; font-family: monospace; font-size: 8pt; color:grey;" name="auxDeudaAdapterVO" property="atributos" readonly="true"></html:textarea>
             </p>
		</fieldset>

		<!-- Exencion -->
		<fieldset>
			<legend>Exencion</legend>
             <p align="left">
                 <html:textarea style="width:100%; height:270px; font-family: monospace; font-size: 8pt; color:grey;" name="auxDeudaAdapterVO" property="exencion" readonly="true"></html:textarea>
             </p>
		</fieldset>

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
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
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- auxDeudaViewAdapter.jsp -->