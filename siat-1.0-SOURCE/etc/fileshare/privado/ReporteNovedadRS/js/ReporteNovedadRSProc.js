// Reporte de Novedades de RS
// Mantis 0007883: Prod. I - GRS -> AFIP - Reporte de Novedades de RS
// Fecha: 27/04/2011
//===================================================================
// Filtros de búsqueda:
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
	var SqlCtasRSDrop = 
			"[noerr] drop table tmpCtasRS";

	var SqlUltAdhe = 
			" SELECT nrocuenta, MAX(id) maxoper " +
			" FROM dre_novedadrs " +
			" WHERE tipotransaccion = 'ADHESION' " +
			" GROUP BY 1 " +
			" INTO TEMP tmpUltAdhe";
	var SqlUltAdheDrop = 
			"[noerr] drop table tmpUltAdhe";
	
	var SqlAdhesiones = 
			" SELECT n.nrocuenta, n.cuit, id idadhesion, fechatransaccion fechaadhesion, " +
			" mesinicio, anioinicio " +
			" FROM dre_novedadrs n, tmpUltAdhe a " +
			" WHERE n.nrocuenta = a.nrocuenta and a.maxoper = n.id " +
			" INTO TEMP tmpAdhesiones" ;
	var SqlAdhesionesDrop = 
			"[noerr] drop table tmpAdhesiones";
	
	var SqlUltBaja = 
			" SELECT nrocuenta, MAX(id) maxbaja " +
			" FROM dre_novedadrs " +
			" WHERE tipotransaccion = 'BAJA'" +
			" GROUP BY 1 " +
			" INTO TEMP tmpUltbaja";
	var SqlUltBajaDrop = 
			"[noerr] drop table tmpUltbaja";
	
	var SqlBajas = 
			" SELECT n.nrocuenta, n.cuit, id idbaja, fechatransaccion fechabaja " +
			" FROM dre_novedadrs n, tmpUltbaja b " +
			" WHERE n.nrocuenta = b.nrocuenta and b.maxbaja = n.id " +
			" INTO TEMP tmpBajas";
	var SqlBajasDrop = 
			"[noerr] drop table tmpBajas";
	
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
			//--%codactividad%
			" [[ AND d.listactividades like '%'||(select codconcepto from def_recconadec where id = #m)||'%' ]] " + 
			//--rango de fechas de adhesion
			" [[ AND a.fechaadhesion >= #d]] "+ 
			" [[ AND a.fechaadhesion <= #d]] " +
			 //--rango de fechas de baja
			" [[ AND b.fechabaja >= #d ]] " +
			" [[ AND b.fechabaja <= #d ]] " +
			//--rango de ingresos brutos anuales
			" [[ AND d.ingbruanu >= #i ]]"+
			" [[ AND d.ingbruanu <= #i ]]" +
			 //--superficie afectada
			" [[ AND d.supafe >= #i ]] " +
			" [[ AND d.supafe <= #i ]] " +
			//--cant personal
			" [[ AND d.canper >= #i ]] " +
			" [[ AND d.canper <= #i ]] " + 
			 //categoríaRS: [1, 2, 3, 4]
			" [[AND d.idcatrsdrei = #i]] " +
			" INTO TEMP tmpResultado";
	var SqlTmpResultadoDrop = 
			"[noerr] drop table tmpResultado";
	
	var SqlOut = "SELECT * from tmpResultado"; 
	
	// processReport genera el resultado del proceso 
	var process = function (p) {
		var params = [p.idActividad, p.fechaAdhesionDesde, p.fechaAdhesionHasta, p.fechaBajaDesde,
					  p.fechaBajaHasta,p.ingBruAnuDesde,p.ingBruAnuHasta, p.supAfeDesde, p.supAfeHasta,
					  p.canPerDesde, p.canPerHasta,p.categoria];
		
		Adp.log("Comienza Procesamiento consulta del Reporte de Novedades de RS");
		
		Adp.log("- Seleccionando Novedades distintas de BAJA...");
		Sql.exec(SqlCtasRSDrop);		
		Sql.exec(SqlCtasRS);			
		
		Adp.log("- Seleccionando Novedades de tipo ADHESION...");
		Sql.exec(SqlUltAdheDrop);	
		Sql.exec(SqlUltAdhe);	
		
		Adp.log("- Determinando adhesiones...");
		Sql.exec(SqlAdhesionesDrop);	
		Sql.exec(SqlAdhesiones);	
		
		Adp.log("- Seleccionando Novedades de tipo BAJA...");
		Sql.exec(SqlUltBajaDrop);	
		Sql.exec(SqlUltBaja);	
		
		Adp.log("- Determinando bajas...");
		Sql.exec(SqlBajasDrop);
		Sql.exec(SqlBajas);

		Adp.log("Generando tabla de Resultados");
		Sql.exec(SqlTmpResultadoDrop);
		Sql.exec(SqlTmpResultado, params);
					
		Adp.log("Finalizando construccion consulta de Novedades de RS\n");
	}

	// outputReport: Recorre las tablas de resultado y genera los archivos de salida.
	var output = function (params) {
		Adp.log("Comienza construccion de salida\n");
		
		//generamos csv
		var csvpath, csvout, csvcount;
		csvpath = Adp.makeFileName("NovedadesRS", "csv");  
		csvout = Grs.outCsv(csvpath, {separator:"|"});
		csvcount = report(csvout, params);
		Adp.addFile(csvpath, "Novedades de Regimen Simplificado", "Reporte de Novedades de RS CSV", csvcount);
		csvout.close();
		  
		//generamos html
		var htmlpath, htmlout, htmlcount;
		htmlpath = Adp.makeFilePath("NovedadesRS", "html");
		htmlout = Grs.outHtml(htmlpath, {});
		htmlcount = report(htmlout, params);		
		Adp.addFile(htmlpath, "Novedades de Regimen Simplificado", "Reporte de Novedades de RS PDF", htmlcount);
		htmlout.close();
	
		Adp.log("Finaliza construccion de salida");

	}
	
	// report: Genera un reporte en el archivo independiente del formato.
	// retorna la cantidad de registros procesados.
	var report = function (out, params) {
		
		// Seccion de campos
		out.fieldset("Parametros");
		
		out.field("Id. Actividad:", params.idActividad);
		out.field("Fecha Adhesion Desde:", params.fechaAdhesionDesde);
		out.field("Fecha Adhesion Hasta:", params.fechaAdhesionHasta);
		out.field("Fecha Baja Desde:", params.fechaBajaDesde);
		out.field("Fecha Baja Hasta:", params.fechaBajaHasta);
		out.field("IIBB anuales Desde:", params.ingBruAnuDesde);
		out.field("IIBB anuales Hasta:", params.ingBruAnuHasta);
		out.field("Sup. afectada Desde:", params.supAfeDesde);
		out.field("Sup. afectada Hasta:", params.supAfeHasta);
		out.field("Cant. Personal Desde:", params.canPerDesde);
		out.field("Cant. Personal Hasta:", params.canPerHasta);
		if(params.categoria == "")
			out.field("Categoria :", "Todas");
		else
			out.field("Categoria :", params.categoria);
		out.endfieldset();

		out.table("Resultados");
				
		// titulos de las columnas
		out.thead(["Id ult. Novedad","Fecha Transaccion","Cuenta","CUIT","Contribuyente",
		         	"Domicilio Local","Telefono","Email","ISIB","Precio Unitario","Can. Per.",
		         	"Ing. Bru. Anu.","Sup. Afe.","Des. Categoria"," Des Publicidad","Des ETur",
		         	"Cumur","Importe DReI","Importe ETur","Importe Adicional","Importe Total",
		         	"Actividades","Fecha Adhesion","Mes Inicio","Anio Inicio","Fecha Baja"]);

		// ordenar segun estos key del row
		out.order(["ultidnovedad","fechatransaccion","nrocuenta","cuit","descont",
		         	"domlocal","telefono","email","isib","preciounitario","canper",
		         	"ingbruanu","supafe","descategoria"," despublicidad","desetur",
		         	"cumur","importedrei","importeetur"," importeadicional","importetotal",
		         	"listactividades"," fechaadhesion","mesinicio","anioinicio","fechabaja"]);
		
		var count = 0;
		var cursor = Sql.cursor(SqlOut);
		while (row = cursor.read()) {
			out.row(row);
			++count;
		}
		
		if (count == 0)
			out.td("Sin Resultados");	

		out.endtable();
		return count;
	}

	this.main = function () {
		var params = Adp.parameters();

		process(params);
		output(params);
		Sql.close();
	}
}

Siat.ReporteNovedadRS.main();