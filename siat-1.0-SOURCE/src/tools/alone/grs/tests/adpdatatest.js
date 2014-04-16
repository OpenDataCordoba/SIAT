var ADP = Grs.adp();

// Metodos que necesitan que existan una corrida en ADP.

// OK
var params ={"param1":10			
			,"param2":20.5
			,"param3":0
			//,"param4":45,5509 NOT OK
			,"param5":0.0
			,"param6":"Texto"
			,"param7":"Hola"
			};

//OK
ADP.message("Mensaje no temporal");

//OK
ADP.message("Mensaje temporal 300",300);

//OK
ADP.log("Voy a agregar un log debido a que %s el %s %s se la %s", "Hernan", "Mono", "Richard", "Lastra");

//NOT TESTED
var arr = getParameters();
for(var i=0 ;i < arr.leng; i++) {
	Grs.printf(arr[]);
}


//OK
ADP.saveParameters(params);

//OK
ADP.changeState("5","Mensaje 1");

//OK
ADP.changeState(6,"Mensaje 2", false);