//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.buss.bean;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Parametro;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.buss.dao.ParametroDAO;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.dao.JDBCConnManager;


/**
 * Manejador de la consola siat
 * 
 * @author tecso
 *
 */
public class ConsoleManager {
	
	private static Logger log = Logger.getLogger(BaseManager.class);
	
	public static final ConsoleManager INSTANCE = new ConsoleManager();
	
	/**
	 * Constructor privado
	 */
	private ConsoleManager() {
		
	}
	
	/**
	 * Devuelve unica instancia
	 */
	public static ConsoleManager getInstance() {
		return INSTANCE;
	}


	public boolean testConcur(PrintWriter cons) {
		boolean stat;
		Session session = null;
		Transaction tx = null; 

		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			ParametroDAO dao = DefDAOFactory.getParametroDAO();

 			cons.println("Siat Test Concurrencia: " + new Date()); cons.println();
			long milis = System.currentTimeMillis();
			ArrayList<Long> ids = new ArrayList<Long>();
			while (System.currentTimeMillis() < (milis + 180*1000)) { //120 segundos de test
				cons.println("Insertando...."); cons.println();
				ids = new ArrayList();
				for(int j=0; j<40; j++) {
					Parametro param;
					param = new Parametro();
					param.setCodParam("TestConcur");
					param.setDesParam("TestConcur " + j);
					param.setValor("TestConcur " + j);
					dao.update(param);
					ids.add(param.getId());
					System.out.println("TID: " + Thread.currentThread().getId());
					Thread.sleep(100 + (long)Math.random()*100);
				}
				tx.commit();
				tx = session.beginTransaction();
				cons.println("Leyendo...."); cons.println();
				for(int j=0; j<40; j++) {
					dao.getList();
					System.out.println("TID: " + Thread.currentThread().getId());
					Thread.sleep(100 + (long)Math.random()*100);
				}

				cons.println("Actualizando...."); cons.println();
				for(int j=0; j<40; j++) {
					Parametro param;
					param = Parametro.getById(ids.get(j));
					param.setDesParam("Hola");
					dao.update(param);
					System.out.println("TID: " + Thread.currentThread().getId());
					Thread.sleep(100 + (long)Math.random()*100);
				}
				tx.commit();
				tx = session.beginTransaction();								
			}
		} catch (Exception e) {
			cons.println("Error: " + e); cons.println();
			if(tx != null) tx.rollback();
			return false;
		} finally {
			SiatHibernateUtil.closeSession();
		}
		cons.println("Fin de test " + new Date()); cons.println();
		return true;
	}

	public boolean testConcurSelect(PrintWriter cons) {
		boolean stat;
		Session session = null;
		Transaction tx = null; 

		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

 			cons.println("Siat Test Concurrencia Select: " + new Date()); cons.println();
			long milis = System.currentTimeMillis();
			
			//Query zarpado y largo
			String queryString = " SELECT SKIP 0 FIRST 1000 deuda.*"
				+ " FROM gde_deudaadmin deuda"
				+ " order by idcuenta, idProcurador";			
			Query query = session.createSQLQuery(queryString).addEntity("deuda", DeudaAdmin.class);
			query.list();
			
			tx.commit();
		} catch (Exception e) {
			cons.println("Error: " + e); cons.println();
			if(tx != null) tx.rollback();
			return false;
		} finally {
			SiatHibernateUtil.closeSession();
		}
		cons.println("Fin de test " + new Date()); cons.println();
		return true;
	}


	public boolean testConcurUpdate(PrintWriter cons) {
		boolean stat;
		Session session = null;
		Transaction tx = null; 

		try {
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();

 			cons.println("Siat Test Concurrencia Update: " + new Date()); cons.println();
			long milis = System.currentTimeMillis();

			DeudaAdmin deuda = DeudaAdmin.getById(16495688L);
			deuda.setObsMotNoPre("FEDEL");
			GdeDAOFactory.getDeudaAdminDAO().update(deuda);
			tx.commit();

		} catch (Exception e) {
			cons.println("Error: " + e); cons.println();
			if(tx != null) tx.rollback();
			return false;
		} finally {
			SiatHibernateUtil.closeSession();
		}
		cons.println("Fin de test " + new Date()); cons.println();
		return true;
	}

	public boolean status(PrintWriter cons) {
		boolean stat = true;
		
		cons.println("Siat status: " + new Date()); cons.println();
		cons.println("version: " + SiatParam.version() +
				(SiatParam.isWebSiat()?" (webSiat)":"") + 
				(SiatParam.isIntranetSiat()?" (intraSiat)":"")); 
		cons.println();		

		if (stat) { cons.println(); stat = statusMemory(cons); }
		if (stat) { cons.println(); statusDBCon(cons); }
		if (stat) {	cons.println(); stat = statusFileShare(cons); }
		
		return stat;
	}

	public boolean statusFileShare(PrintWriter cons) {
		cons.println("File Share Status");
		cons.println("not implemented!");
		return true;
	}

	public boolean statusMemory(PrintWriter cons) {
		cons.println("Memory Status");
		cons.println("Used Memory: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) /1024/1024) + "M");
		return true;
	}

	public boolean statusDBCon(PrintWriter cons) {
		int result = 1;
		cons.println("Database Connections");

		result *= statusDatasource(cons, "java:comp/env/ds/siat");
		result *= statusDatasource(cons, "java:comp/env/ds/swe");
		result *= statusDatasource(cons, "java:comp/env/ds/gisdb");
		result *= statusDatasource(cons, "java:comp/env/ds/generaldb");
		result *= statusDatasource(cons, "java:comp/env/ds/indet");
		result *= statusDatasource(cons, "java:comp/env/ds/variosweb");
		result *= statusDatasource(cons, "java:comp/env/ds/gravamenes");
		result *= statusDatasource(cons, "java:comp/env/ds/debitoaut"); 
		result *= statusDatasource(cons, "java:comp/env/ds/catastro"); 
		result *= statusDatasource(cons, "java:comp/env/ds/osirisEnvio");
		
		return result == 1;
	}

	private int statusDatasource(PrintWriter cons, String ds) {
		Connection cn = null;
		try {
			cons.print("Conectando...: " + ds);
			System.out.println("Conectando...: " + ds);
			cn = JDBCConnManager.getConnection(ds);
			System.out.println("Conectando...: " + ds + "OK");
			try { cn.close(); } catch (Exception ex) {}
			cons.println(" ok" );
		} catch (Exception e) {
			try { cn.close(); } catch (Exception ex) {}
			cons.println(" !ERROR!: " + ds + " " + e);
			e.printStackTrace(cons);
			cons.println();
			return 0;
		}
		return 1;
	} 	
}
