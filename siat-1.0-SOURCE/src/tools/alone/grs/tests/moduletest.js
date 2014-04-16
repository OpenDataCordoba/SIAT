/*
TestModule
Ejemplo blanco para la construccion de Reportes y/o procesos
en gral via Grs.  


//reglas de estilo: (cuando sea necesario borrar estos comentarios)
- Las variables y denfiniciones ponerlas al principio del modulo. 
- Las variables que se suponen 'Constantes', ponerlas en camel case con mayuscula inicial
- Las variables, ponerlas en camel case con minuscula inicial.
- Las funciones camel case con minuscula incial.
- Usar el prefijo 'process' para funciones que recorren cursores 
  de gran longitud, y/o que gobiernan el proceso.
- Usar el prefijo 'make' para funciones crean Objectos (pej, rows que seran insertando).
- Usar el prefijo 'insert/update' para metodos que insertan o actualizan datos a apartir de objectos.
- Usar el prefijo 'get' para funciones que retornan Objectos. (pej rows que ya existen en la DB). 
- Usar caracter tab para indentar
- Comentarios:
*/

Grs.load("<grs.js>")
Grs.load("<siat/siat.js>")

Siat.TestModule = new function () {
//private 'Const'
var Sql = Grs.sql(Siat.DsName);

//private variables 
var privateVar = "PrivateVar";


//private functions
var testPrivate = function () {
	Grs.debug("test private: " + privateVar + "\n");
}

//public  functions
this.publicVar = "PublicVar";
this.testPublic = function (arg) {
	testPrivate()
	Grs.debug("test public: " + privateVar + " " + arg + "\n");
}

}//end TestModule

function main() {
	var arg = Siat.TestModule.publicVar;
	Siat.TestModule.testPublic(arg);

	Grs.debug("dsName: %s\n", Siat.DsName);
}
