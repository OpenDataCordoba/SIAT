<br>
<!-- fieldset-->
<table width="100%" border="0">
   <tr>
	    <!-- Ingreso de datos -->
	    <td class="normal"><label>Tipo y Nro. Documento:&nbsp;</label>
		
			<select name="cueExe.solicitante.documento.tipoDocumento.id" class="select"><option value="-1" selected="selected">Seleccionar...</option>
				<option value="1">DNI</option>
				<option value="2">LC</option>
				<option value="3">LE</option>
				<option value="4">CI</option>
				<option value="5">PAS</option>
				<option value="7">DE</option>
				<option value="8">CF</option>
			</select>
			
			<!-- ht ml:t ext name="personaVO" property="documento.numeroView"/-->
			
			<input type="text" name="cueExe.solicitante.documento.numeroView" />
				
		   &nbsp;
		
		   <html:button property="btnBuscarSolicitante"  styleClass="boton" onclick="submitForm('buscarSolicitante', '');">
				Buscar Persona
		   </html:button>
		   &nbsp;
		   <html:button property="btnBuscarSolicitante"  styleClass="boton" >
				Limpiar
		   </html:button>		   
		</td>							
    </tr>
    <tr>
	       <td class="normal">
	       		<label><bean:message bundle="pad" key="pad.persona.nombreYApellido.label"/>: </label>
	       		<bean:write name="personaVO" property="apellido"/> &nbsp; <bean:write name="personaVO" property="nombres"/> 
	       </td>		    
     </tr>
</table>
<!-- /fieldset -->
<br>