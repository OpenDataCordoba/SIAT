<%@ page import="ar.gov.rosario.siat.def.iface.util.*" %>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script>
function refreshCaches(id) {
  var ele = document.getElementById(id);
  var hosts = ele.value.split("\n");
  var result = document.getElementById("result");;
  for(var i=0; i <= hosts.length ; i++) {
    var url = hosts[i];
    if (!url || url.length < 10) continue;
    result.innerHTML += request(url) + " - " + url +"\n";
  }
}
</script>


<!-- Tabla que contiene todos los formularios -->
<form id="filter">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1>Admininstracion de Caches del Siat</h1>	
		
		<!-- Cache -->
		<fieldset>
			<legend>Refrecar Remotamente</legend>
			<table class="datos">
				<tr>					
					<td colspan="2">
						<b>Actualización remota de los cache en cada Instancia.</b>
						<span style="font-size:80%">
						<br/>OK = Significa actualizado con exito.
						<br/>ERROR = Fallo. Ocurrio un error durante la actualización.
						<br/>COLOR GRIS = Fallo. No se pudo enviar el mensage para que se refresque el cache.
						</span>
					</td>
				</tr>

				<tr>	
					<td></td>				
					<td>
						<br/><br/>						
						<a target="cache07" href="/siat/def/configuracion/cache.jsp">Refrescar siat (actual)</a>
						<iframe name="cache07" style="width:160px; height:10px;border:solid 1px gray"  scrolling="no"  src="" frameborder=0></iframe>
						<br/><br/>
						
						<a target="cache08" href="/adpsiat/cache.jsp">Refrescar adpsiat (actual)</a>
						<iframe name="cache08" style="width:160px; height:10px;border:solid 1px gray"  scrolling="no"  src="" frameborder=0></iframe>
						<br/><br/>					
					</td>
				</tr>
		</table>
		</fieldset>	

<!--
		<fieldset>
			<legend><bean:message bundle="def" key="Refrecar esta Instancia"/></legend>

				<tr>					
					<td colspan="2">
						<b>Actualización Manual en esta instancia.
						<br/>IMPORTANTE: Recuerde que la operacion de actualizar caches debe realizarce en cada instancia</b>
					</td>
				</tr>
				
				<tr>
					<td>
					<input type="button" name="btnVolver" value="Refrescar"
					onclick="location.href='/siat/seg/Login.do?method=refrescarCache&cache=<%=DefError.CACHE_INDETERMINADO%>'"/>
					</td>
					<td class="norma" style="padding:10px"><b>Información para saber si una deuda o cuota esta indeterminada.</b>
					<br/>El cache se actualiza automaticamente cada 1 hora. Y durante Asentamientos y Procesos Masivos.
					<br/>Actualice este cache, si quiere ver reflejado un cambio reciente de indeterminados.</td>
				</tr>
				<tr>					
					<td>
					<input type="button" name="btnVolver" value="Refrescar"
				    onclick="location.href='/siat/seg/Login.do?method=refrescarCache&cache=<%=DefError.CACHE_DEFATRIBUTO%>'"/>
					</td>
					<td class="normal" style="padding:10px"><b>Definición de atributos de entidades de Siat. </b>
					<br/>Actualice este cache si occurrio un cambio en la definción de un Atributo o Dominio
					o en los Atributos de Tipos de Objetos Imponibles
					o cualquiera en general.</td>
				</tr>
				<tr>					
					<td>
					<input type="button" name="btnVolver" value="Refrescar"
				    onclick="location.href='/siat/seg/Login.do?method=refrescarCache&cache=<%=DefError.CACHE_SWE%>'"/>
					</td>
					<td class="normal" style="padding:10px"><b>Menu, usuarios, roles y acciones de SWE.</b>
					<br/>Actualice este cache, si cambio algo en swe. Recuerde que es probable 
					el usario ademas tenga que reloguearse para ver el cambio.</td>
				</tr>
				<tr>					
					<td>
					<input type="button" name="btnVolver" value="Refrescar"
				    onclick="location.href='/siat/seg/Login.do?method=refrescarCache&cache=<%=DefError.CACHE_PARAMETRO%>'"/>
					</td>
					<td class="normal" style="padding:10px"><b>Parametros de configuración del Siat.</b>
					<br/>Actualice este cache si cambio algun valor en los parametros de configuración.
					</td>
				</tr>
				<tr>					
					<td>
					<input type="button" name="btnVolver" value="Refrescar"
					onclick="location.href='/siat/seg/Login.do?method=refrescarCache&cache=<%=DefError.CACHE_CASO%>'"/>
					</td>
					<td class="normal" style="padding:10px"><b>Tipos de casos.</b>
					<br/>Actualice este cache para ver reflejado los cambios en el combo de Tipo de Caso de las páginas con casos asociado.
					
					</td>

				</tr>
				<tr>					
					<td>
					<input type="button" name="btnVolver" value="Refrescar"
				    onclick="location.href='/siat/seg/Login.do?method=refrescarCache&cache=<%=DefError.CACHE_FRASE%>'"/>
					</td>
					<td class="normal" style="padding:10px"><b>Frases</b><br/>Actualice este cache para ver reflejado los cambios en el ABM de frases.</td>
				</tr>

				<tr>					
					<td>
					<input type="button" name="btnVolver" value="Refrescar"
				    onclick="location.href='/siat/seg/Login.do?method=refrescarCache'"/>
					</td>
					<td class="normal" style="padding:10px"><b>Refrescar todos los caches.</b>
					<br/>Utilice este boton si tiene que actualizar los caches y no sabe muy bien que modifico.
					</td>
				</tr>
		</table>
		</fieldset>	
-->
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	<form>
	<!-- Fin Tabla que contiene todos los formularios -->
