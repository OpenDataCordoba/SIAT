//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone; 

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ActualizaDeuda;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAct;
import coop.tecso.demoda.buss.helper.LogFile;

public class MigDrei {

	public static void main(String[] args) throws Exception {
		MigDrei main = new MigDrei();
		
		Alone.init(false, false);
		main.scriptActualizacionCyQ();
	}

	public void scriptActualizacionCyQ() throws Exception {
		LogFile scriptFile; 		// Archivos sql

		scriptFile = new LogFile("./", "deuda-proced.sql", false);
		String sql = "select ";
		sql += " deuda.fechaVencimiento fechaVtoDeuda, deuda.saldo saldoDeuda, deuda.id idDeuda, ";
		sql += " proced.fechaAlta fechaAltaProced, ";
		sql += " prodet.id idProDet ";
		sql += " from gde_deudajudicial deuda, cyq_prodet prodet, cyq_procedimiento proced ";
		sql += " where deuda.id = prodet.iddeuda ";
		sql += " and proced.id = prodet.idprocedimiento ";
		sql += " and deuda.idrecurso = 15 ";

		String sqlDeuda = "update gde_deudajudicial set actualizacionCyq = %8.2f where id = %d; --fechaVto:%s fechaProced:%s saldo:%8.4f";
		String sqlProDet = "update cyq_prodet set actualizacionCyq = %8.2f where id = %d;";

		Connection cn = null;
		ResultSet rs = null;

		cn = SiatHibernateUtil.currentSession().connection();
		rs = cn.createStatement().executeQuery(sql);

		int c = 0;
		while (rs.next()) { 
			if (++c % 1000 == 0) {
				System.out.printf("scriptActulizacionCyQ(): count:%d\n", c);       
			}

			Long idDeuda = rs.getLong("idDeuda");
			Long idProDet = rs.getLong("idProDet");
			Double saldoDeuda = rs.getDouble("saldoDeuda");
			Date fechaVtoDeuda = rs.getDate("fechaVtoDeuda");
			Date fechaAltaProced = rs.getDate("fechaAltaProced");
			
			DeudaAct deudaact = ActualizaDeuda.actualizar(fechaAltaProced, fechaVtoDeuda, saldoDeuda, false, true);
			Double actualizacionCyQ = deudaact.getImporteAct();
			
			scriptFile.addline(String.format(sqlDeuda,  actualizacionCyQ, idDeuda, fechaVtoDeuda, fechaAltaProced, saldoDeuda));
			scriptFile.addline(String.format(sqlProDet, actualizacionCyQ, idProDet));
			//scriptFile.addline("");
		}
		rs.close();
		cn.close();
		scriptFile.close();
		System.out.printf("scriptActulizacionCyQ(): count:%d\n", c);
	}
}

