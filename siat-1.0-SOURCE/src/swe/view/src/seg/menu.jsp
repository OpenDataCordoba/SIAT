<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<h1>Seleccione sus Opciones de Men&uacute;</h1>
<p>
Aqu&iacute; se presentan las opciones del sistema ordenadas por m&oacute;dulo y subm&oacute;dulo. 
</p>
<!--  -->  


<!-- Menu Nivel 1 -->
<div class="col200">
<div class="solapa">
<h2>Temas</h2>
<ul class="blanco">

	<logic:iterate id="itemMenuVO" name="userMenu" property="listItemMenuNivel1" indexId="count">
	<li>

	<logic:equal name="itemMenuVO" property="seleccionadoView" value="true">
   		<a class="activo" href="/swe/SeleccionItemMenu.do?idAccionModulo=<bean:write name="itemMenuVO" property="accionModulo.id" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel1=<bean:write name="itemMenuVO" property="id" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel2=0"><bean:write name="itemMenuVO" property="titulo"/></a>
    </logic:equal>
	<logic:notEqual name="itemMenuVO" property="seleccionadoView" value="true">
   		<a href="/swe/SeleccionItemMenu.do?idAccionModulo=<bean:write name="itemMenuVO" property="accionModulo.id" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel1=<bean:write name="itemMenuVO" property="id" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel2=0"><bean:write name="itemMenuVO" property="titulo"/></a>
    </logic:notEqual>
    
	</li>
	</logic:iterate>

</ul>
</div>
</div>

<!-- Menu Nivel 2 -->
<logic:notEmpty name="userMenu" property="listItemMenuNivel2">

	<div class="col200">
	<div class="solapa">
	<h2><bean:write name="userMenu" property="tituloNivel2"/></h2>
  	<ul class="blanco">
		<logic:iterate id="itemMenuVO" name="userMenu" property="listItemMenuNivel2">
  		<li>
		<logic:equal name="itemMenuVO" property="seleccionadoView" value="true">
    		<a class="activo" href="/swe/SeleccionItemMenu.do?idAccionModulo=<bean:write name="itemMenuVO" property="accionModulo.id" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel1=<bean:write name="userMenu" property="idItemMenuNivel1" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel2=<bean:write name="itemMenuVO" property="id" bundle="sat" formatKey="general.format.id"/>"><bean:write name="itemMenuVO" property="titulo"/></a>
		</logic:equal>
		<logic:notEqual name="itemMenuVO" property="seleccionadoView" value="true">
    		<a href="/swe/SeleccionItemMenu.do?idAccionModulo=<bean:write name="itemMenuVO" property="accionModulo.id" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel1=<bean:write name="userMenu" property="idItemMenuNivel1" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel2=<bean:write name="itemMenuVO" property="id" bundle="sat" formatKey="general.format.id"/>"><bean:write name="itemMenuVO" property="titulo"/></a>
		</logic:notEqual>
  		</li>
		</logic:iterate>
	</ul>
  	</div>
	</div>
	
</logic:notEmpty>


<!-- Menu Nivel 3 -->
<logic:notEmpty name="userMenu" property="listItemMenuNivel3">

<div class="col250">
  <div class="solapa">
    <h2><bean:write name="userMenu" property="tituloNivel3" /></h2>
        <table cellpadding="0" cellspacing="0" class="tabla250">
    <thead></thead>
    <colgroup></colgroup>
    <colgroup style="width:20px; text-align:right;"></colgroup>
    <tbody>
    
	<logic:iterate id="itemMenuVO" name="userMenu" property="listItemMenuNivel3">
    <tr>
      <td>
   		<a href="/swe/SeleccionItemMenu.do?idAccionModulo=<bean:write name="itemMenuVO" property="accionModulo.id" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel1=<bean:write name="userMenu" property="idItemMenuNivel1" bundle="sat" formatKey="general.format.id"/>&idItemMenuNivel2=<bean:write name="userMenu" property="idItemMenuNivel2" bundle="sat" formatKey="general.format.id"/>"><bean:write name="itemMenuVO" property="titulo"/></a>
      </td>
      <td>      
      </td>
    </tr>
    </logic:iterate>
  
  </tbody>
  </table>
</div>
</div>
	
</logic:notEmpty>
