//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone; 

import java.util.Date;
import java.util.Map;

import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import coop.tecso.grs.Grs;
import coop.tecso.grs.sql.Cursor;
import coop.tecso.grs.sql.Sql;

public class GrsTest {

	public static void main(String[] args) throws Exception {
		GrsTest main = new GrsTest();

		Alone.init(false, false);
		if (!"-0".equals(args[3])) {
			DefServiceLocator.getConfiguracionService().initializeSiat();
		}
		
		main.test0(args);
	}

	private void test0(String[] args) throws Exception {
		System.err.printf("%s", "Grs Test gde_deudaadmin");
		Sql sql;
		Cursor cursor;
		sql = new Grs().sql("java:comp/env/ds/siat");
		int c = 0;
		
		System.err.printf("scan pad_cuenta: %s\n", new Date());
		cursor = sql.cursor("select * from pad_cuenta where idrecurso=#i", 14);		
		Map<String, Object> row;
		while ((row = cursor.read()) != null) {
			Grs.debug("id:%d, desdomenv:%s\n", row.get("id"), row.get("idcuenta"));
			if (c++ % 10000 == 0) Grs.debug("%d\n", c);
		}
		cursor.close();
		System.err.printf("end: %s\n", new Date());

		
		System.err.printf("scan gde_deudaadmin: %s\n", new Date());
		cursor = sql.cursor("select * from gde_deudaadmin");
		while ((row = cursor.read()) != null) {
			Grs.debug("id:%d, idcuenta:%d\n", row.get("id"), row.get("idcuenta"));
		}
		cursor.close();
		System.err.printf("end: %s\n", new Date());
	}
	
}
