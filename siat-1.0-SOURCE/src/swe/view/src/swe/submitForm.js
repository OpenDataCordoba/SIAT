
function limpiaResultadoFiltro() {
	document.getElementById("resultadoFiltro").innerHTML="&nbsp;"
}

function submitForm(method, selectedId) {

	var form = document.getElementById('filter');
	form.target = "";
			
	form.elements["method"].value = method;
	form.elements["selectedId"].value = selectedId;

	if(method=="volver" || method=="seleccionar" || method=="seleccionarPersona"){
		form.elements["isSubmittedForm"].value = "false";
	}
	
	if (method.substring(0,8)=="imprimir") {
		form.target = "_blank";
	} else {
		disableForm(form);
	}
	
	form.submit();
}

// Deshabilita los elementos de un formulario
function disableForm(form) {

	var elements = form.elements;
	for (i = 0; i < elements.length; i++) {
		if (elements[i].type == "text") {
			elements[i].readOnly = true;
		} else if (elements[i].type == "button") {
			elements[i].disabled = "true";
		} else if (elements[i].type == "submit") {
			elements[i].disabled = "true";
		} else if (elements[i].type == "checkbox") {
			elements[i].readonly = "true";
		} else if (elements[i].type == "select-one") {
			elements[i].readonly = "true";
		}
	}
	DisableLinks();
	
}

// Deshabilita los links del documento
function DisableLinks(){
	
	objLinks = document.links;
	
	for (i = 0; i < objLinks.length; i++) {
		objLinks[i].disabled = true;
		objLinks[i].onclick = function() { return false; }
	}
	
}

/**
 * Chequea o deschequea la "lista" de checks de "form" seguen el estado de "control"
 */
function changeChk(form, lista, control){
	
	var form = document.getElementById(form);
	
	if (!(typeof(form.elements[lista])=='undefined')){
		if (control.checked == true){
			chkAll(form.elements[lista]);		
		} else {
			unChkAll(form.elements[lista]);
		}
	}
}
		
/* chk: el check del encabezado que chequea o deschequea a todos los demas
 * checks: son todos los demas, si hay mas de uno es un array, sino es un objeto 
 */
function chkAll( checks){				
	// controla si hay y o action=""
	if ( typeof(checks.length) == 'undefined' || checks.length == 1 ){				
		checks.checked = true;
	} else {
	  for (i=0; i < checks.length; i++)	{
			if(!checks[i].disabled){
				checks[i].checked = true;
			}
		}
	}
}

function unChkAll( checks){				
	// controla si hay y o action=""
	if ( typeof(checks.length) == 'undefined' || checks.length == 1 ) {				
		checks.checked = false;
	} else	{
	  for (i=0; i < checks.length; i++)	{
			if(!checks[i].disabled){
				checks[i].checked = false;
			}
		}
	}
}