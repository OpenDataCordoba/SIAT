<logic:notEmpty name="gesJudDeuAdapterVO" property="constanciaDeu.id">
	<bean:define id="constancia" name="gesJudDeuAdapterVO" property="constanciaDeu"/>		
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.constanciaDeu.title"/></legend>				
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="gde" key="gde.constanciaDeu.numero.ref"/>:</label></td>
				<td class="normal"><bean:write  name="constancia" property="numeroView"/></td>
				<td><label><bean:message bundle="gde" key="gde.constanciaDeu.anio.ref"/>:</label></td>
				<td class="normal"><bean:write  name="constancia" property="anioView"/></td>						
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.constanciaDeu.cuenta.label"/>:</label></td>
				<td class="normal"><bean:write  name="constancia" property="cuenta.numeroCuenta"/></td>
				<td><label><bean:message bundle="gde" key="gde.constanciaDeu.recurso.label"/>:</label></td>
				<td class="normal"><bean:write  name="constancia" property="cuenta.recurso.desRecurso"/></td>						
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.constanciaDeu.estConDeu.label"/>:</label></td>
				<td class="normal" colspan="3"><bean:write  name="constancia" property="estConDeu.desEstConDeu"/></td>
			</tr>							
		</table>
	</fieldset>
</logic:notEmpty>	
