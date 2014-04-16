/*
ReporteAbc
Ejemplo blanco para la construccion de Reportes y/o procesos
en gral via Grs.  
*/


/* reglas de estilo: (cuando sea necesario borrar estos comentarios)
- Las variables y denfiniciones ponerlas al principio del modulo. 
- Las variables que se suponen 'Constantes', ponerlas en camel case con mayuscula inicial
- Las variables, ponerlas en camel case con minuscula inicial.
- Las funciones en camel case con minuscula incial.
- Usar el prefijo 'process' para funciones que recorren cursores 
  de gran longitud, y/o que gobiernan el proceso.
- Usar el prefijo 'make' para funciones crean Objectos (pej, rows que seran insertando).
- Usar el prefijo 'insert/update' para metodos que insertan o actualizan datos a apartir de objectos.
- Usar el prefijo 'get' para funciones que retornan Objectos. (pej rows que ya existen en la DB). 
- Usar caracter tab para indentar
- Comentarios:
  Usar // por norma general, y solo /* para grandes bloques
  En las funciones, comenzarlas siempre con el nombre de la funcion
  y un pequeño parrafo de que hace la funcion.
  Y explayarse en el segundo o mas parrafos.
*/



Siat.ReporteAbc = new function () {
Grs.load("<grs.js>")
Grs.load("<siat/siat.js>")
Grs.load("<siat/const.js>")

//   private 'Const'    //
var Sql = Grs.sql(Siat.DsName);
var Adp = Grs.adp();
var Const = Siat.Const;

//private variables 
var privateVar = "PrivateVar";


//   private functions  //

//processReport ejecuta las consultas necesarias
//para generar la tabla temporal necesaria para
//luego generar la salida.
var processReport = function (params) {
	Grs.debug("process Report: " + privateVar + "\n");
	Adp.log("Comienza Procesar Reporte");
}

//outputReport recorre las tablas de resultado y genera los archivos de salida.
//tipicamente este metodo recorre con un cursor las tabla temporal generada,
//y formatea y genera el resultado en un archivo o pdf/html/csv
var outputReport = function (params) {
	Grs.debug("output Report: " + privateVar + "\n");
	Adp.log("Comienza Output de Reporte");
	//filename con: NombreProceso-[Prefix-]fecha-idcorrida.Sufix
	//var filename = Adp.makeFileName("csv", "Archivo1");  
	//Adp.addFile(filename, "descripcion corto", "observacion");

	//var out = Grs.OutCsv(Apd.currdir(), filename);
	//out.fmt = {"saldo":"r%2d", "fecha":"yyyy-mm-dd"};
}

//   public  functions    //
this.publicVar = "PublicVar";
this.main = function () {
	Grs.debug("test public: %s \n", privateVar);
	
	var params = Adp.getParameters();
	processReport(params);
	outputReport(params);
	Sql.close();
}

}//end ReporteAbc

//start 
Siat.ReporteAbc.main();

