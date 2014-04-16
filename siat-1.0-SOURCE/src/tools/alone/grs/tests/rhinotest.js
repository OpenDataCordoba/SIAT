var RhinoTest  = {};

RhinoTest.tests = function() {
	Grs.printf("%s\n", "Begin Rhino test");
	mktest("test0", RhinoTest.test0);
	mktest("test1", RhinoTest.test1);
}

//test type conversion with rhino
RhinoTest.test0 =  function () {
	var ActDeu = Packages.ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
	var fechaVto = new Date(2010, 0, 1);
	Grs.printf(ActDeu.actualizar(fechaVto, 120.34, false, true).getImporteAct());
	Grs.printf(ActDeu.actualizar(fechaVto, 120.34, false, true).getImporteAct());
	Grs.printf(ActDeu.actualizar(fechaVto, 120.34, false, true).getImporteAct());
	Grs.printf(ActDeu.actualizar(fechaVto, 120.34, false, true).getImporteAct());

	var Test = Grs.test();
	Grs.printf("\n");
	//Test.inarray(["hola", 1, 1.0, 1.1, new Date(), [0,1,2]]);
	
	Grs.printf("\n");
	//Test.inmap({uno:1, hola:"hola", fecha:new Date(), smap:{iduno:1, value:"value"}, array:[0,1,2]});

	Grs.printf("\n");
	var m = {uno:1, hola:"hola", fecha:new Date(), smap:{iduno:1, value:"value"}, array:[0,1,2]};
	//Test.inmap(m);

	Grs.printf("\n");
	m = {uno:1.0, dos:2.0, ochetanueva:89.0};
	//Test.inmap(m);

	var a = [-4, -3, -2, -1, 0, 1, 2.0, 3, 4, 5, 6, 7, 8, 9, 10, 89];
	//Test.inarray(a);


	string("");
	string(1);
	string(new Date());
	string(a);
	string(m);
	string(RhinoTest.test1);

	return "OK";
}

//test type conversion with rhino
RhinoTest.test1 =  function () {
	//TestLoaded is define in loadtest.js
	//Grs.load("grs/loadtest.js");
	//if (!TestLoaded) return "ERROR";
	
	return "OK";
}

var string = function (obj) {
	var ret = [];
	if (obj instanceof Array) {
		Grs.printf("type: %s\n", "Array");
	} 

	if (obj instanceof Function) {
		Grs.printf("type: %s\n", "Object");
	}

	if (obj instanceof Object) {
		Grs.printf("type: %s\n", "Object");
	}

	Grs.printf(".\n");
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


RhinoTest.tests();
