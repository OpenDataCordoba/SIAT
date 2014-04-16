//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone; 

import java.util.Date;
import java.util.List;

import coop.tecso.demoda.iface.helper.DateUtil;

public class Test {

	public static void main(String[] args) throws Exception {
		System.out.printf("Test\n\n");
		Test main = new Test();
		/*
		//Alone.init(false, false);
		String s = "1$$2";
		Datum d = Datum.parseAtChar(s, '$');
		System.out.println(d.getColNumMax());*/
		if(args.length < 2){
			System.out.println("Faltan parametros. Ejemplo:alone.sh Test 01/01/2009 12/04/2013");
			return;
		}
		System.out.println("Parametros ingresados: "+args[1]+" , "+args[2]);
		Date fechaDesde = DateUtil.getDate(args[1]);
		Date fechaHasta = DateUtil.getDate(args[2]);
		if(fechaDesde == null || fechaHasta == null){
			System.out.println(" Formato de las fechas es incorrecto. Ejemplo:alone.sh Test 01/01/2009 12/04/2013");
			return;			
		}
		List<Date[]> listRangos  = DateUtil.getListDateRangeForYears(fechaDesde, fechaHasta);
		for(Date[] rango: listRangos){
			int periodoDesde = DateUtil.getMes(rango[0]);
			int periodoHasta = DateUtil.getMes(rango[1]);
			int anioDesde = DateUtil.getAnio(rango[0]);
			int anioHasta = DateUtil.getAnio(rango[1]);
			System.out.println("Año: "+anioDesde+" Periodos: "+periodoDesde+" a "+periodoHasta+"  (año para validación: "+anioHasta+")");
		}

	}	
}
