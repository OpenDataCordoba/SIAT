//siat.js debe ser cargado antes.


/*
Modulo de constantes funcionales de siat

Convensiones:
- para constantes que representan valores en tablas usar 
la siguiende convencion:

	[Tabla][Columna][Valor]

donde las partes deben recordar los nombres de tablas
colunas y valores, puden ser iguales o si existe un 
mejor nombre se puede usar.
pej:
	RecursoIdTgi = 14
	RecursoCodTgi = 'TGI'
	EstadoDeudaIdJud = 2
*/

Siat.Const = new function () {
	
	//Conexiones a DB
	
	this.DsSiat = "java:comp/env/ds/siat";
	this.DsSeguridadweb = "java:comp/env/ds/seguridadwebdb";
	this.DsSwe = "java:comp/env/ds/swe";
	this.DsGis = "java:comp/env/ds/gisdb";
	this.DsGeneral = "java:comp/env/ds/generaldb";
	this.DsIndeterminados = "java:comp/env/ds/indet";
	this.DsVariosweb = "java:comp/env/ds/variosweb";  
	this.DsGravamenes = "java:comp/env/ds/gravamenes";
	this.DsDebitoaut = "java:comp/env/ds/debitoaut";  
	this.DsCatastronew = "java:comp/env/ds/catastro";
	this.DsRodados = "java:comp/env/ds/rodados";
	this.DsMulator = "java:comp/env/ds/osirisEnvio";
	
	// Entidades de Siat
	this.RecursoIdTgi = 14;
	this.RecursoIdDrei = 15;
	this.RecursoIdEtur = 16;
	this.RecursoIdDP = 21;
	//public functions

}
