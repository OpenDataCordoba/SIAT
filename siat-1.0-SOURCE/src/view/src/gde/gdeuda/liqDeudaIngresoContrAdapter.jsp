<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqDeuda.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<div id="contenido">
		<bean:define id="verCambioDomicilio" name="liqDeudaAdapterVO" property="verCambioDomicilio" toScope="session" />
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	<div style="clear: both;"> </div>
		
	<h1>
		<bean:message bundle="gde" key="gde.liqDeudaIngresoAdapter.prefijoTitulo"/>
		<bean:write name="liqDeudaAdapterVO" property="recurso.desRecurso"/> 
	</h1>	
		
	<p>
		<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.ingresoLiquidacionDeuda.textoAyudaEncabezadoParrafoUno")%>
	</p>
	
	<p>
		<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.ingresoLiquidacionDeuda.textoAyudaEncabezadoParrafoDos")%>
	</p>
	
	<p>
		<%=ar.gov.rosario.siat.fra.iface.util.Frase.getInstance().getMessageBykey("gde.ingresoLiquidacionDeuda.textoAyudaEncabezadoParrafoTres")%>
	</p>
	
		
	<!-- LiqDeuda -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.fieldset.title"/></legend>
		
			<p>
				<html:hidden name="liqDeudaAdapterVO" property="cuenta.idRecurso"/>
				
				<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
				<bean:write name="liqDeudaAdapterVO" property="cuenta.recurso.desRecurso"/>
			</p>
			<p> 
	      		<label><bean:message bundle="pad" key="pad.cuenta.label"/>: 	      		
	      			<html:text name="liqDeudaAdapterVO" property="cuenta.numeroCuenta" size="15" maxlength="20" styleClass="datos"/>
	      		</label>
	      		<logic:equal name="liqDeudaAdapterVO" property="codGesPerRequerido" value="true">
		      		<label><bean:message bundle="gde" key="gde.liqDeudaAdapter.cuenta.codGestionPersonal.label"/>: 	      		
		      			<html:text name="liqDeudaAdapterVO" property="cuenta.codGesPer" size="15" maxlength="20" styleClass="datos"/>
		      		</label>
	      		</logic:equal>
			</p>
		
		  	<div style="text-align:right">
		  		<button type="button" name="btnAceptar" onclick="submitForm('ingresarLiqDeudaContr', '');" class="boton">
		  			<bean:message bundle="gde" key="gde.liqDeudaIngresoGRAdapter.button.aceptar"/>
		  		</button>
		  	</div>
	</fieldset>	
	<!-- LiqDeuda -->
	
    <!-- requerimientos -->
	<br/>
			<h3>Requerimientos m&iacute;nimos del Sistema:</h3>
			<ul class="vinieta">
	          <li>Impresora (resoluci&oacute;n m&iacute;nima requerida 300 dpi).</li>
	          <li>Papel tama&ntilde;o A4; 80 gramos (recomendado).</li>
	
	          <li>Acrobat Reader instalado. [<a 
	              href="http://www.adobe.com/products/acrobat/readstep2.html" 
	              target=_blank>Descargar el programa</a>]. </li>
	        </ul>
	<p>
		<strong>.::</strong><a href="<%= request.getContextPath()%>/gde/gdeuda/mesa_ayuda.jsp"/> 
		Mesa de Ayuda y soporte a trav&eacute;s de correo electr&oacute;nico 
	          </a>
	</p>
	
	<!-- fin requerimientos -->
	
	<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->