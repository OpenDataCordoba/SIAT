function submitConfirmForm(method, selectedId, confirmMsg) {
	var conf = true;
	if (confirmMsg && confirmMsg != "") {
	  conf = confirm(confirmMsg);
	}
	if (conf) {
		submitForm(method, selectedId)
	}
}

function submitForm(method, selectedId) {

	var form = document.getElementById('filter');
	form.target = "";
			
	form.elements["method"].value = method;
	if (method == 'refill') {
		try { form.elements["locateFocus"].value = selectedId; } catch(e) {}
	} else {
		form.elements["selectedId"].value = selectedId;
	}

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

// utilizada para visualizar un archivo generado
function submitDownload(method, fileParam) {

	var form = document.getElementById('filter');
	form.target = "";
			
	form.elements["method"].value = method;
	if(form.elements["fileParam"] == null){
		alert("Debe definir el input Hidden con nombre 'fileParam'");
	}
	form.elements["fileParam"].value = fileParam;
	
	form.elements["isSubmittedForm"].value = "false";
	
	form.target = "_blank";
	
	form.submit(); 
}

function submitImprimir(method, reportFormat) {

	var form = document.getElementById('filter');
	// limpio el div de resultados
	var resultadoFiltro = document.getElementById('resultadoFiltro');
	if(resultadoFiltro != null){
		resultadoFiltro.innerHTML = " ";
	}

	form.elements["method"].value = method;
	form.elements["reportFormat"].value = reportFormat;
	//form.target = "_blank";
	form.submit();
}


// Deshabilita los elementos de un formulario
function disableForm(form) {
	var elements = form.elements;
	for (i = 0; i < elements.length; i++) {
		if (elements[i].type == "text") {
			elements[i].oldReadOnly = elements[i].readOnly;
			elements[i].readOnly = true;
		} else if (elements[i].type == "button") {
			elements[i].oldDisabled = elements[i].disabled;
			elements[i].disabled = "true";
		} else if (elements[i].type == "submit") {
			elements[i].oldDisabled = elements[i].disabled;
			elements[i].disabled = "true";
		} else if (elements[i].type == "checkbox") {
			elements[i].oldReadOnly = elements[i].readOnly;
			elements[i].readonly = "true";
		} else if (elements[i].type == "select-one") {
			elements[i].oldReadOnly = elements[i].readOnly;
			elements[i].readonly = "true";
		}
	}
	DisableLinks();	
}

// Guarda el estado (activado/desactivado) de 
// los elementos del formulario 
function saveFormElementsStatus(form) {
	var elements = form.elements;
	for (i = 0; i < elements.length; i++) {
		if (elements[i].type == "text") {
			elements[i].oldReadOnly = elements[i].readOnly;
		} else if (elements[i].type == "button") {
			elements[i].oldDisabled = elements[i].disabled;
		} else if (elements[i].type == "submit") {
			elements[i].oldDisabled = elements[i].disabled;
		} else if (elements[i].type == "checkbox") {
			elements[i].oldReadOnly = elements[i].readOnly;
		} else if (elements[i].type == "select-one") {
			elements[i].oldReadOnly = elements[i].readOnly;
		}
	}
}


// Deshabilita los links del documento
function DisableLinks(){
	objLinks = document.links;
	for (i = 0; i < objLinks.length; i++) {
		var id = objLinks[i].id;
		//si empieza con siatmenu, no lo deshabilitamos.
		if (id != null && id.substring(0,8) == "siatmenu") {
		  break;
		}
		objLinks[i].oldDisabled = objLinks[i].disabled;
		objLinks[i].oldOnclick = objLinks[i].onclick;
		
		objLinks[i].disabled = true;
		objLinks[i].onclick = function() { return false; }
	}
	
}

// Habilita los elementos de un formulario
function restoreForm(form) {
	var elements = form.elements;
	for (i = 0; i < elements.length; i++) {
		if (elements[i].type == "text") {
			elements[i].readOnly = elements[i].oldReadOnly;
		} else if (elements[i].type == "button") {
			elements[i].disabled = elements[i].oldDisabled;
		} else if (elements[i].type == "submit") {
			elements[i].disabled = elements[i].oldDisabled;
		} else if (elements[i].type == "checkbox") {
			elements[i].readOnly = elements[i].oldReadOnly;
		} else if (elements[i].type == "select-one") {
			elements[i].readOnly = elements[i].oldReadOnly;
		}
	}
	restoreLinks();	
}


// Deshabilita los links del documento
function restoreLinks(){
	objLinks = document.links;
	for (i = 0; i < objLinks.length; i++) {
		objLinks[i].disabled = objLinks[i].oldDisabled;
		objLinks[i].onclick = objLinks[i].oldOnclick;
	}
}

/* funcion llamada en cada unload de pagina del siat*/
function unload() {
    //activamos todos los links
  	var forms = document.getElementsByTagName("form"); 
 	for (var i = 0; i < forms.length; i++) { 
		restoreForm(forms[i]);
    }
}

function cleanErrors(){
	var form = document.getElementById('filter');
	form.getElementById('strutsErrors').innerHTML=""

}


function alertNoImpl(){
	alert("Funcionalidad no implementada");
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


function clearCasoValues(voName) {

	var form = document.getElementById('filter');
	
	form.elements[voName + ".caso.numero"].value = '';
	form.elements[voName + ".caso.sistemaOrigen.id"].value=-1;
	form.elements[voName + ".caso.accion"].value = 'eliminar';
}

function init() {
	// focus en el elmento con id indicado en el hiden locateFocus 
	var ele = document.getElementById("locateFocus");
	if (ele != null && ele.value != null && ele.value != "" && ele.value != "null") {
		var eleFocus = document.getElementById(ele.value);
		if (eleFocus) { eleFocus.focus(); } else { }
	}
	
	// guardamos el estado (activado/desactivado) 
	// original de los elementos del formulario 
	var form = document.getElementById('filter');
	saveFormElementsStatus(form);
}

function requestReplace(eleid, url, interval, auto) {
	var ele = document.getElementById(eleid);
	while (ele.firstChild) {
		ele.removeChild(ele.firstChild);
	}
	
	ele.innerHTML = "Cargando...";

	var html = siatRequest(url);
	ele.innerHTML = html;
	if (interval > 0 && !auto) {
		if(window.XMLHttpRequest) {
			window.setInterval(requestReplace, interval*1000, eleid, url, interval, true);
		} else if(window.ActiveXObject) {
			var s = "requestReplace('" + eleid + "', '" + url + "', " + interval + ", true)";
			window.setInterval(s, interval*1000);		
		}
	}
}

/* si url no empieza con /siat/, le aniade /siat a la url*/
function siatRequest(url) {
    var req = httpRequest();
	var prefix = '/siat/';

	prefix = url.indexOf(prefix) == 0 ? "" : prefix.substring(0,5);
    req.onreadystatechange = processReqChange;
    req.open("GET", prefix + url, false);
    req.send("");
    return req.responseText;
}

/* si url no empieza con /siat/, le aniade /siat a la url*/
function request(url) {
    var req = httpRequest();
    req.onreadystatechange = processReqChange;
    req.open("GET", url, false);
    req.send("");
    if (req.status != 200) {
       return "ERROR: " + req.status + ": " + req.statusText;
    }
    return req.responseText;
}

function httpRequest() {
    req = false;
    // branch for native XMLHttpRequest object
    if(window.XMLHttpRequest) {
    	try {
			req = new XMLHttpRequest();
        } catch(e) {
			req = false;
        }
    // branch for IE/Windows ActiveX version
    } else if(window.ActiveXObject) {
       	try {
        	req = new ActiveXObject("Msxml2.XMLHTTP");
      	} catch(e) {
        	try {
          		req = new ActiveXObject("Microsoft.XMLHTTP");
        	} catch(e) {
          		req = false;
        	}
	}
    }
    return req;
}

function processReqChange() {
    // only if req shows "loaded"
    if (req.readyState == 4) {
        // only if "OK"
        if (req.status == 200) {
            // ...processing statements go here...
        } else {
            alert("Ocurrio un problema al recuperar datos:\n" + req.statusText);
        }
    }
}


/**
 * Chequea o deschequea la "lista" de checks de "form" segun el estado de "control" y 
 * para el bloque "procurador" correspondiente.
 */
function changeChkProcurador(form, lista, control, procurador){
	
	var form = document.getElementById(form);
	
	//alert(procurador);
	
	if (!(typeof(form.elements[lista])=='undefined')){
		
		if (control.checked == true){
			chkAllProcurador(form.elements[lista], procurador);		
		} else {
			unChkAllProcurador(form.elements[lista], procurador);
		}
	}
}
		
/* chk: el check del encabezado que chequea o deschequea a todos los demas
 * checks: son todos los demas, si hay mas de uno es un array, sino es un objeto 
 */
function chkAllProcurador( checks, procurador){
	// controla si hay y o action=""
	if ( typeof(checks.length) == 'undefined' || checks.length == 1 ){				
		if (checks.id == procurador) {
			//alert(checks.value);
			checks.checked = true;
		}
	} else {
	  for (i=0; i < checks.length; i++)	{
			if (checks[i].id == procurador){
				//alert(checks[i].value);
				
				if(!checks[i].disabled){
					checks[i].checked = true;
				}
			}
		}
	}
}


function unChkAllProcurador( checks, procurador){				
	// controla si hay y o action=""
	if ( typeof(checks.length) == 'undefined' || checks.length == 1 ){				
		if (checks.id == procurador) {
			//alert(checks.value);
			checks.checked = true;
		}
	} else {
	  for (i=0; i < checks.length; i++)	{
			if (checks[i].id == procurador){
				//alert(checks[i].value);
				
				if(!checks[i].disabled){
					checks[i].checked = false;
				}
			}
		}
	}
}

/*
* Funcion para ocultar/mostrar un span
*/
function toggle(control, elementName){
	var elem = document.getElementById(elementName);		

	if (elem.style.display == 'none' ){
		elem.style.display = "block";
		control.innerHTML = "(-)&nbsp;";
	} else {
	    elem.style.display = "none";
	    control.innerHTML = "(+)&nbsp;";
	}    
}



/*
* Setea los combos a busqueda incremental
*/
function setSelectEvents() {
    // set javascript event attributes
    // for all select items in the current page
    var selects = document.getElementsByTagName("select");
    var arrOnfocus = new Array(); // array of onfocus functions
    var arrOnkeydown = new Array(); // array of onkeydown functions
    var arrOnkeyup = new Array(); // array of onkeyup functions
    var arrOnkeypress = new Array(); // array of onkeypress functions

	try {
	
	    for (var i = 0; i < selects.length; i++) {
	        // we need to ensure that
	        // we don't overwrite any existing function
	
	        // save index to array as an element attribute
	        // (using i directly did not work)
	        selects[i].title = i;
	
	        // onfocus
	        if(typeof(selects[i].onfocus) == 'function') {
	        // there is a pre-existing function
	            // save pre-existing function
	            arrOnfocus[selects[i].title] = selects[i].onfocus;
	            selects[i].onfocus =
	              function() { arrOnfocus[this.title](); this.enteredText=''; }
	              // set event to call our code plus the pre-existing function
	        }
	        else { // there is no pre-existing function
	            selects[i].onfocus = function() { this.enteredText=''; }
	        }
	
	        // onkeydown
	        if(typeof(selects[i].onkeydown) == 'function') {
	        // there is a pre-existing function
	            // save pre-existing function
	            arrOnkeydown[selects[i].title] = selects[i].onkeydown;
	            selects[i].onkeydown =
	              function() { arrOnkeydown[this.title](); return handleKey(); }
	              // set event to call our code plus the pre-existing function
	        }
	        else { // there is no pre-existing function
	            selects[i].onkeydown = function() { return handleKey(); }
	        }
	
	        // onkeyup
	        if(typeof(selects[i].onkeyup) == 'function') {
	        // there is a pre-existing function
	            // save pre-existing function
	            arrOnkeyup[selects[i].title] = selects[i].onkeyup;
	            selects[i].onkeyup =
	              function() { arrOnkeyup[this.title]();
	                           event.cancelbubble=true;return false; }
	              // set event to call our code plus the pre-existing function
	        }
	        else { // there is no pre-existing function
	            selects[i].onkeyup =
	              function() { event.cancelbubble=true;return false; }
	        }
	
	        // onkeypress
	        if(typeof(selects[i].onkeypress) == 'function') {
	        // there is a pre-existing function
	            // save pre-existing function
	            arrOnkeypress[selects[i].title] = selects[i].onkeypress;
	            selects[i].onkeypress =
	               function() { arrOnkeypress[this.title](); return selectItem(); }
	               // set event to call our code plus the pre-existing function
	        }
	        else { // there is no pre-existing function
	            selects[i].onkeypress = function() { return selectItem(); }
	        }
	    }
	} catch(e) {
		// No hacemos nada, es para que no pinche en algunos browsers
	}	    
}


/*
*	 Busqueda incremental en combos.
*/
function fillin(ctrl, comboName) {
	try {
    	var mytext = ctrl.value;
        var combo= document.getElementById(comboName);
        var sellength = combo.length;
        var seleccionado = false;
           
        for(var i=1; i<sellength; i++) {
             if (combo.options[i].text.toLowerCase().indexOf(mytext.toLowerCase()) > -1) {
             	combo.options[i].style.display='block';
              	combo.options[i].style.clear='both';
               	
               	if (!seleccionado && mytext.length > 0){
               		combo.options[i].selected = true;
               		seleccionado = true;
               	}
                   
	         }else{
	           	combo.options[i].style.display='none';
	         }
	     }
	     if (!seleccionado){
	       	combo.options[0].selected = true;
	     }
	} catch(e){
	}      
}
    
/*
*  Utilizado para realizar el switch entre un combo de recurso y un bloque de busqueda de recurso.  
*/   
function toggleSearchRecurso(element1, element2){
	var elem1 = document.getElementById(element1);
	var elem2 = document.getElementById(element2);

	if (elem1.style.display == 'none' ){
		elem1.style.display = "block";
		elem2.style.display = "none";
	} else {
	    elem1.style.display = "none";
	    elem2.style.display = "block";
	}
}

/*
*  Cuando se selecciona un elemento de la busqueda de recurso, pasa el valor al combo de recurso.
*/
function selectRecurso(cbo1, cbo2){
	var busqueda = document.getElementById(cbo1);
	var destino = document.getElementById(cbo2);
	if (busqueda.value != null)
		destino.value = busqueda.value;
}

/*
* Setea el foco en el elemento pasado por parametro
*/
function setFocus(elemenName){
	var ctrl = document.getElementById(elemenName);
	
	if (ctrl && ctrl.focus())
		ctrl.focus();
}
