var DsSiat = "java:comp/env/ds/siat";

var SqlTest  = {};

SqlTest.tests = function() {
	Grs.printf("%s\n", "Begin Grs Sql test");
	mktest("test1", SqlTest.test1);
	mktest("test2", SqlTest.test2);
	mktest("test3", SqlTest.test3);

	mktest("test4", SqlTest.test4);
	mktest("test5", SqlTest.test5);
}

//test conection datasources
SqlTest.test1 = function () {
	var Sql = Grs.sql(DsSiat);
	var vias = Sql.list("select desviadeuda label, id value from def_viadeuda");
	Grs.printf("%s\n", vias);

	return "OK";
}

//test macro expansion
SqlTest.test2 = function () {
	var Sql = Grs.sql(DsSiat);
	var vias = Sql.list("select desviadeuda #m, id #m from #m", "label", "id", "def_viadeuda");
	Grs.printf("%s\n", vias);

	return "OK";
}

//test prepare statement parameters
SqlTest.test3 = function () {
	var Sql = Grs.sql(DsSiat);
	var vias = Sql.list("select desviadeuda label, id value from def_viadeuda where id = #i or id = #i", 1, 2);
	Grs.printf("%s\n", vias);

	return "OK";
}

//test both
SqlTest.test4 = function () {
	var Sql = Grs.sql(DsSiat);
	var vias = Sql.list("select desviadeuda #m, id value from #m where id = #i or #m = #i", "label", "def_viadeuda", 1, "id", 2);
	Grs.printf("%s\n", vias);

	return "OK";
}

//test both
// js  en i5, contra informix: tarda 30seg 50%CPU (400000 registros)
// java tarda (30seg promedio) 40%CP
// todas las pruebas parace indicar que el tiempo de rhino no es siginificativo
// dado que el cuello de botella esta en IO de los SQL
SqlTest.test5 = function () {
	var Sql = Grs.sql(DsSiat);
	var cursor = Sql.cursor("select * from pad_cuenta where idrecurso = #i", 14);
	while (row = cursor.read()) {
		Grs.printf("id:%s nro:%s domenv:%s \n", row.id, row.numerocuenta, row.desdomenv); 
	}	
	cursor.close()
	
	return "OK";
}

// en i5, contra informix: tarda 27min (21.5 millones de registros)
SqlTest.test6 = function () {
	var Sql = Grs.sql(DsSiat);
	var cursor = Sql.cursor("select * from gde_deudaadmin");
	while (row = cursor.read()) {
		Grs.printf("id:%s idcuenta:%s\n", row.id, row.idcuenta); 
	}	
	cursor.close()
	
	return "OK";
}

// util
mktest = function (name, fnt) {
	var result = ""; 
	var except;
	try {
		result = fnt();
	} catch (e) {
		except = e;
		result = "ERROR " + e.fileName +":" + e.lineNumber + " " + e.toString();
	}
	Grs.printf("mktest: %s:%s\n", name, result);
	if (except && except.javaException) except.javaException.printStackTrace(); 
}

SqlTest.tests();
