Grs.setDebug(1);

var DsSiat = "java:comp/env/ds/siat";
var Sql = Grs.sql(DsSiat);

var SqlTest  = {};

SqlTest.tests = function() {
	Grs.printf("%s\n", "Begin Grs Sql test");
	mktest("test1", SqlTest.test1);
	mktest("test2", SqlTest.test2);
	mktest("test3", SqlTest.test3);

	mktest("test4", SqlTest.test4);
	mktest("test5", SqlTest.test5);
	mktest("test6", SqlTest.test6);
	mktest("test7", SqlTest.test7);
	mktest("test8", SqlTest.test8);
}

//test conection datasources
SqlTest.test1 = function () {
	var vias = Sql.list("select desviadeuda label, id val from def_viadeuda where id in (1,2,3)");
	Grs.printf("%s\n", vias);
	if (vias.length != 3)
		return "ERROR";

	return "OK";
}

//test macro expansion
SqlTest.test2 = function () {
	var vias = Sql.list("select desviadeuda #m, id #m from #m", "label", "id", "def_viadeuda");
	Grs.printf("%s\n", vias);

	return "OK";
}

//test prepare statement parameters
SqlTest.test3 = function () {
	var vias = Sql.list("select desviadeuda label, id val from def_viadeuda where id = #i or id = #i", 1, 2);
	Grs.printf("%s\n", vias);

	return "OK";
}

//test statements
SqlTest.test4 = function () {
	var vias = Sql.list("select desviadeuda #m, id val from #m where id = #i or #m = #i", "label", "def_viadeuda", 1, "id", 2);
	Grs.printf("%s\n", vias);

	return "OK";
}

//test sql.row
SqlTest.test5 = function () {
	var via = Sql.row("select desviadeuda label, id val from def_viadeuda where id = -1");
	if (via != null)
		return "ERROR 1";

	via = Sql.row("select desviadeuda label, id val from def_viadeuda order by id");
	if (via == null)
		return "ERROR 2";

	Grs.printf("%s\n", via);
	if (via.val != 1)
		return "ERROR 3";

	
	return "OK";
}

//test sql.row
SqlTest.test6 = function () {
	var count = Sql.value("select count(*) from def_viadeuda where id in (1,2,3)");
	if (count != 3)
		return "ERROR";
	
	return "OK";
}

//test conditional section
SqlTest.test7 = function () {
	var row = Sql.row("select * from pad_cuenta where 1=1 [[and numerocuenta = #s]]", '10001208');
	if (row.numerocuenta != '10001208') 	
		return "ERROR";

	return "OK";
}

//test sql.get / save
SqlTest.test8 = function () {
	
	var ret = Sql.exec("[id] insert into def_viadeuda (desviadeuda, usuario, fechaultmdf, estado) values (#o, #o, #o, #o)",
	"Test", "fedel", new Date(), 1);
	Grs.printf("Ret: %s\n", ret);	
	
	var ret = Sql.exec("[id] insert into def_viadeuda (desviadeuda, usuario, fechaultmdf, estado) values (#o, #o, #d, #o)",
	"Test", "fedel", new Date(), 1);
	
	var row = Sql.get("def_viadeuda", ret);
	Grs.printf("Ret: %s\n", row);	
	
	row.usuario = 'otro';
	Sql.save("def_viadeuda", row);
	
	row = Sql.get("def_viadeuda", ret);
	Grs.printf("Ret: %s\n", row);	
	
	row.id = null;
	var ret = Sql.save("[id]def_viadeuda", row);
	Grs.printf("Ret: %s\n", ret);	

	ret = Sql.remove("def_viadeuda", ret);
	Grs.printf("Ret: %s\n", ret);	

	ret = Sql.exec("delete from def_viadeuda where id > #i", 3);
	Grs.printf("Ret: %s\n", ret);	

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
