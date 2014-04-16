// Reporte de Novedades de RS
// Mantis 0007883: Prod. I - GRS -> AFIP - Reporte de Novedades de RS
// Fecha: 27/04/2011
//===================================================================
// Criterios de búsqueda:
//----------------------
//	- categoríaRS: [1, 2, 3, 4]
//	- codactividad
//	- rango de ingresos brutos anuales, superficie afectada, cant personal
//	- rango de fechas de adhesion y de fechas de baja

Grs.load("<grs.js>");
Grs.load("<siat/siat.js>");
Grs.load("<siat/const.js>");

Grs.setDebug(0);

Siat.ReporteNovedadRS = new function () {
	var Sql = Grs.sql(Siat.DsName);
	var Adp = Grs.adp();
	var Const = Siat.Const;
	
	//Sqls
	var SqlCtasRS = 
			" SELECT nrocuenta, MAX(id) maxoper " +
			" FROM dre_novedadrs " +
			" WHERE tipotransaccion <> 'BAJA' " +
			" GROUP BY 1 " +
			" INTO TEMP tmpCtasRS";

	var SqlUltAdhe = 
			" SELECT nrocuenta, MAX(id) maxoper " +
			" FROM dre_novedadrs " +
			" WHERE tipotransaccion = 'ADHESION' " +
			" GROUP BY 1 " +
			" INTO TEMP tmpUltAdhe";
	
	var SqlAdhesiones = 
			" SELECT n.nrocuenta, n.cuit, id idadhesion, fechatransaccion fechaadhesion, " +
			" mesinicio, anioinicio " +
			" FROM dre_novedadrs n, tmpUltAdhe a " +
			" WHERE n.nrocuenta = a.nrocuenta and a.maxoper = n.id " +
			" INTO TEMP tmpAdhesiones" ;
	
	var SqlUltBaja = 
			" SELECT nrocuenta, MAX(id) maxbaja " +
			" FROM dre_novedadrs " +
			" WHERE tipotransaccion = 'BAJA'" +
			" GROUP BY 1 " +
			" INTO TEMP tmpUltbaja";
	
	var SqlBajas = 
			" SELECT n.nrocuenta, n.cuit, id idbaja, fechatransaccion fechabaja " +
			" FROM dre_novedadrs n, tmpUltbaja b " +
			" WHERE n.nrocuenta = b.nrocuenta and b.maxbaja = n.id " +
			" INTO TEMP tmpBajas";
	
	var SqlTmpResultado = 
			" SELECT t.maxoper ultidnovedad, fechatransaccion, t.nrocuenta, d.cuit, descont, " +
			" domlocal, telefono, email, isib, preciounitario, canper, ingbruanu, supafe, " + 
			" descategoria, despublicidad, desetur, cuna cumur, " + 
			" importedrei, importeetur, importeadicional, " +
			" (importedrei + importeetur + importeadicional) importetotal, " +
			" listactividades, a.fechaadhesion, a.mesinicio, a.anioinicio, b.fechabaja " +
			" FROM tmpCtasRS t, dre_novedadrs d, tmpAdhesiones a, OUTER tmpBajas b " +
			" WHERE t.maxoper = d.id " +
			" AND d.nrocuenta = a.nrocuenta " +
			" AND d.nrocuenta = b.nrocuenta " +
//			" AND d.idcatrsdrei = #i " + --categoríaRS: [1, 2, 3, 4]
//			" AND d.listactividades = #s " + --%codactividad%
//			" AND d.ingbruanu >= i# AND d.ingbruanu =< i# " + --rango de ingresos brutos anuales
//			" AND d.supafe >= i# AND d.supafe =< i# " + --superficie afectada
//			" AND d.canper >= i# AND d.canper =< i# " + --cant personal
//			" AND a.fechaadhesion >= d# AND a.fechaadhesion =< d# " + --rango de fechas de adhesion
//			" AND b.fechabaja >= d# AND b.fechabaja =< d# " + --rango de fechas de baja
			" INTO TEMP tmpResultado";
	
	var SqlOut = "SELECT * from tmpResultado"; 
	
	
	// processReport: Ejecuta las consultas necesarias para generar la 
	// tabla temporal necesaria para luego generar la salida.
	var processReport = function (params) {
		Adp.log("Comienza Procesamiento consulta del Reporte de Novedades de RS");
		
		Adp.log("- Seleccionando Novedades distintas de BAJA...");
		Sql.exec(SqlCtasRS);			
		
		Adp.log("- Seleccionando Novedades de tipo ADHESION...");
		Sql.exec(SqlUltAdhe);	
		
		Adp.log("- Determinando adhesiones...");
		Sql.exec(SqlAdhesiones);	
		
		Adp.log("- Seleccionando Novedades de tipo BAJA...");
		Sql.exec(SqlUltBaja);	
		
		Adp.log("- Determinando bajas...");
		Sql.exec(SqlBajas);

		Adp.log("Generando tabla de Resultados");
		Sql.exec(SqlTmpResultado)
					
		Adp.log("Finalizando construccion consulta de Novedades de RS\n")
	}

	// outputReport: Recorre las tablas de resultado y genera los archivos de salida.
	// Tipicamente este metodo recorre con un cursor las tabla temporal generada,
	// y formatea y genera el resultado en un archivo o pdf/html/csv
	var outputReport = function (params) {

		Adp.log("Iniciando construccion reporte Novedades de RS\n");

		var filename = Adp.makeFileName("NovedadesRS", "csv");  
		var filepath = Adp.addFile(filename, "Novedades de Regimen Simplificado", "Reporte de Novedades de RS");
			
		var out = Grs.outCsv(filepath);
		
		out.separator("|");
		out.head(["Id ult. Novedad","Fecha Transaccion","Cuenta","CUIT","Contribuyente",
		         	"Domicilio Local","Telefono","Email","ISIB","Precio Unitario","Can. Per.",
		         	"Ing. Bru. Anu.","Sup. Afe.","Des. Categoria"," Des Publicidad","Des ETur",
		         	"Cumur","Importe DReI","Importe ETur","Importe Adicional","Importe Total",
		         	"Actividades","Fecha Adhesion","Mes Inicio","Anio Inicio","Fecha Baja"]);
		// Ordena key del mapa resultado.
		out.order(["ultidnovedad","fechatransaccion","nrocuenta","cuit","descont",
		         	"domlocal","telefono","email","isib","preciounitario","canper",
		         	"ingbruanu","supafe","descategoria"," despublicidad","desetur",
		         	"cumur","importedrei","importeetur"," importeadicional","importetotal",
		         	"listactividades"," fechaadhesion","mesinicio","anioinicio","fechabaja"]);
		
		var cursor = Sql.cursor(SqlOut);
		var count = 0;
		while (row = cursor.read()) {
			out.row(row);
			++count;
		}
		
		if (count == 0) {
			out.cell("Sin Resultados");	
		}
		
		out.close();
		Adp.log("Finaliza Procesamiento del Reporte");
	}

	this.main = function () {
		Grs.debug("Reporte de Novedades RS\n");

		var params = Adp.getParameters();
		
		// Parametros de test como si fueran chantados por la interfaz.
//		if (params == null) {
//			params = {idselam: 116, idtipobjimpatr: 52};
//		}
		// Fin parametros test
		
		processReport(params);
		outputReport(params);
		Sql.close();
	}
}

Siat.ReporteNovedadRS.main();