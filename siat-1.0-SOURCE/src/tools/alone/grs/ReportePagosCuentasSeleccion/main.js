//Reporte Reporte Pagos Cuentas Seleccion
//Mantis 0007908: Prod. I - GRS -> Reporte de Convenios Caducos 
//Fecha: 11/05/2011
//Toma como la selección almacenada hecha en el proceso Atributos

Grs.load("<grs.js>")
Grs.load("<siat/siat.js>")
Grs.load("<siat/const.js>")
Grs.load("<siat/util.js>");

Grs.setDebug(0);

Siat.ReportePagosSeleccion = new function() {

	var Sql = Grs.sql(Siat.DsName);
	var Adp = Grs.adp();
	var Const = Siat.Const;
	var Util = Siat.Util;

	var SqlCuentaPagos = 
		"SELECT numerocuenta, desviadeuda, d.anio, d.periodo, descladeu, codrefpag, fechavencimiento, importe, saldo, p.id, p.descripcion , q.numero||\"/\"|| q.anio procedcyq " +
		"FROM #m d, pad_cuenta c, def_reccladeu r, def_viadeuda v, " +
		"OUTER (gde_procurador p), " +
		"OUTER (cyq_procedimiento q) " +
		"WHERE d.idcuenta in ( " +
			"SELECT idelemento " +
			"FROM gde_selalmdet " +
			"WHERE idselalm = #i " +
			") " +
		"AND fechapago IS NOT NULL " +
		"AND d.idcuenta = c.id " +
		"AND d.idreccladeu = r.id " +
		"AND d.idviadeuda = v.id " +
		"AND d.idprocurador = p.id " +
		"AND d.idprocedimientocyq = q.id ";
		
	var SqlIntoTemp = "INTO TEMP tmp_#m";
	var SqlFiltroFechaVencimiento = "AND (fechavencimiento >= #d  AND fechavencimiento <= #d) ";
	var SqlFiltroAnio = "AND ((d.anio > #i OR (d.anio = #i AND d.periodo >= #i)) AND (d.anio < #i OR (d.anio = #i AND d.periodo <= #i))) ";
	var SqlFiltroImporte = "AND (importe >= #f AND importe <= #f) ";
	var SqlFiltroViaDeuda = "AND (d.idviadeuda = #i) ";
	var SqlFiltroFechaPago = "AND (fechapago >= #d  AND fechapago <= #d) ";
	var SqlFiltroTipoPagoNoVencida = "AND (fechapago <= fechavencimiento) ";
	var SqlFiltroTipoPagoVencida = "AND (fechapago > fechavencimiento) ";

	var SqlUnionFinal = "(SELECT * FROM tmp_#m UNION SELECT * FROM tmp_#m UNION SELECT * FROM tmp_#m) INTO TEMP tmp_final";
	var	SqlOut = "SELECT * FROM tmp_final";

	
	// processPagosSeleccion 
	// Gobierna el proceso del reporte.	
	var processPagosSeleccion = function(param) {

		var query;
		
		Adp.log("Comienza Procesar Deuda Administrativa.\n");
		query = makeQuery("gde_deudaadmin", param);
		Sql.exec(query.sql, query.param);

		Adp.log("Comienza Procesar Deuda Judicial.\n");
		query = makeQuery("gde_deudajudicial", param);
		Sql.exec(query.sql, query.param);

		Adp.log("Comienza Procesar Deuda Cancelada.\n");
		query = makeQuery("gde_deudacancelada", param);
		Sql.exec(query.sql, query.param);
		
		Sql.exec(SqlUnionFinal, "gde_deudaadmin", "gde_deudajudicial", "gde_deudacancelada");
	}
	
	// makeQuery 
	// Construye el objeto que representa la consulta SQL y sus parametros.
	var makeQuery = function(tabla, param) {
		var sqlParam = [];
		
		// Consulta Inicial
		var sql = SqlCuentaPagos;
		sqlParam.push(tabla);
		sqlParam.push(param.idselalm);
		
		//+ Filtro Fecha Vencimiento desde y hasta
		if(param.fechaVenDesde != null && param.fechaVenHasta != null){
			sql += SqlFiltroFechaVencimiento;
			sqlParam.push(param.fechaVenDesde);
			sqlParam.push(param.fechaVenHasta);
		}	
		//+ Filtro Año y Periodo desde y hasta
		if(param.anioDesde != null && param.periodoDesde != null && param.anioHasta != null && param.periodoHasta != null){
			sql += SqlFiltroAnio;
			sqlParam.push(param.anioDesde);
			sqlParam.push(param.anioDesde);
			sqlParam.push(param.periodoDesde);
			sqlParam.push(param.anioHasta);
			sqlParam.push(param.anioHasta);
			sqlParam.push(param.periodoHasta);
		}			
		//+ Filtro Importe desde y hasta
		if(param.importeDesde != null && param.importeHasta != null){
			sql += SqlFiltroImporte;
			sqlParam.push(param.importeDesde);
			sqlParam.push(param.importeHasta);
		}			
		//+ Filtro IdViaDeuda
		if(param.idviadeuda != null){
			sql += SqlFiltroViaDeuda;
			sqlParam.push(param.idviadeuda);
		}	
		//+ Filtro Fecha Pago desde y hasta
		if(param.fechaPagoDesde != null && param.fechaPagoHasta != null){
			sql += SqlFiltroFechaPago;
			sqlParam.push(param.fechaPagoDesde);
			sqlParam.push(param.fechaPagoHasta);
		}	
		//+ Filtro Tipo de pago
		if(param.tipopago != null){
			if(param.tipopago == "Pagada antes del vencimiento"){
				sql += SqlFiltroTipoPagoNoVencida;
			}	

			if(param.tipopago == "Pagada vencida"){
				sql += SqlFiltroTipoPagoVencida;
			}	
		}
		
		//+ Into temporal table
		sql += SqlIntoTemp;
		sqlParam.push(tabla);
		
		Grs.printf(sql+"\n\n");
		return {sql: sql, param: sqlParam};
	}

	// outputReport: Recorre las tablas de resultado y genera los archivos de salida.
	// Tipicamente este metodo recorre con un cursor las tabla temporal generada,
	// y formatea y genera el resultado en un archivo o pdf/html/csv
	var outputReport = function (param) {

		if(param.reporte == "csv"){
			Adp.log("Iniciando construccion Reporte Pagos segun Seleccion\n");

			var filename = Adp.makeFileName("ReportePagos", "csv");  
			var filepath = Adp.addFile(filename, "Reporte Pagos", "Reporte Pagos segun Seleccion");

			var out = Grs.outCsv(filepath);

			out.separator("|");
			out.head(["Numero Cuenta", "Via Deuda", "Año", "Periodo", "Clasificacion Deuda", "Codigo Ref pagina", "Fecha Vencimiento", "Importe", "Saldo", "Id Procuardor", "Descripcion" , "Numero\\Año"]);
			out.order(["numerocuenta, desviadeuda, anio, periodo, descladeu, codrefpag, fechavencimiento, importe, saldo, id, descripcion , procedcyq "]);

			//var cursor = Sql.cursor(SqlOut);
			var count = 0;
			/*while (row = cursor.read()) {
				out.row(row);
				++count;
			}*/

			if (count == 0) {
				out.cell("Sin Resultados");	
			}

			out.close();
		}


		Adp.log("Finaliza Procesamiento del Reporte");

	}

	//	public functions //
	this.main = function() {
		var params = Adp.getParameters();
		if (params == null) {
			params = {};
			params.idselalm = 12;
			params.fechaVenDesde = new Date(2009,01,01);
			params.fechaVenHasta = new Date(2010,01,01);
			params.anioDesde = 2009;
			params.periodoDesde = 10;
			params.anioHasta = 2010;
			params.periodoHasta = 10;
			params.importeDesde = 0;
			params.importeHasta = 10000;
			params.idviadeuda = 2;
			params.fechaPagoDesde = new Date(2009,01,01);
			params.fechaPagoHasta = new Date(2010,01,01);
			params.tipopago = "Pagada antes del vencimiento";
			params.reporte = "csv";
		}

		processPagosSeleccion(params);
		outputReport(params);
		Sql.close();
	}

}// end ReportePagosSeleccion

//start
Siat.ReportePagosSeleccion.main();