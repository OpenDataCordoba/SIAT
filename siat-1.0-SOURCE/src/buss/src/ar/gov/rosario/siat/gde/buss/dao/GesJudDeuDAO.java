//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.GesJudDeu;
import ar.gov.rosario.siat.gde.iface.model.DeuCueGesJudSearchPage;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class GesJudDeuDAO extends GenericDAO {

	private Log log = LogFactory.getLog(GesJudDeuDAO.class);	
	
	private static long migId = -1;
	
	public GesJudDeuDAO() {
		super(GesJudDeu.class);
	}
	
	/**
	 * Busca un GesJudDeu por idDeuda
	 * @param idDeuda
	 * @return null si no encuentra nada o si el parametro es nulo
	 */
	public GesJudDeu getByIdDeuda(Long idDeuda){
		if (idDeuda==null)
			return null;
		
		String queryString = "from GesJudDeu t WHERE t.idDeuda=" + idDeuda;
		
 		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString); 
	    GesJudDeu gesJudDeu = (GesJudDeu) query.uniqueResult();
		
		return gesJudDeu;
	}
	
	 /**
	  *  Devuelve el proximo valor de id a asignar. 
	  *  Se inicializa obteniendo el ultimo id asignado en el archivo de migracion con datos pasados como parametro
	  *  y luego en cada llamada incrementa el valor.
	  * 
	  * @return long - el proximo id a asignar.
	  * @throws Exception
	  */
	 public long getNextId(String path, String nameFile) throws Exception{
		 // Si migId==-1 buscar MaxId en el archivo de load. Si migId!=-1, migId++ y retornar migId;
		 if(getMigId()==-1){
				 setMigId(this.getLastId(path, nameFile)+1);
		 }else{
			 setMigId(getMigId() + 1);
		 }
		 
		 return getMigId();
	 }


	 /**
	  *  Inserta una linea con los datos de la GesJudDeu para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param gesJudDeu, output - Relación entre Deuda y Gestion Judicial a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(GesJudDeu o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|idgesjud|iddeuda|observacion|usuario|fechaultmdf|estado 
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getGesJud().getId());		 
		 //line.append("|");
		 //if(o.getConstanciaDeu()!=null)
		 //	 line.append(o.getConstanciaDeu().getId());		 
		 line.append("|");
		 line.append(o.getIdDeuda());		 
		 line.append("|");
		 if(o.getObservacion()!=null)
			 line.append(o.getObservacion());		 
		 line.append("|");
		 line.append(DemodaUtil.currentUserContext().getUserName());
		 line.append("|");
		 line.append("2010-01-01 00:00:00");
		 line.append("|");
		 line.append("1");
		 line.append("|");
	      
		 output.addline(line.toString());
		 
		 // Seteamos el id generado en el bean.
		 o.setId(id);
	
		 return id;
	 }


	/**
	 * Busca una listda de GesJudDeu por Constancia de Deuda
	 * @param Constancia
	 * @return null si no encuentra nada o si el parametro es nulo
	 */
	 public List<GesJudDeu> getListByIdConstancia(Long idConstanciaDeu) {
		if (idConstanciaDeu==null)
			return null;
		
		String queryString = "from GesJudDeu t WHERE t.constanciaDeu.id = "+idConstanciaDeu;
		
 		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString); 
		List<GesJudDeu> listConstancia = (ArrayList<GesJudDeu>) query.list();
				
		return listConstancia;
	}

	 /**
	  * Obtiene la lista de gesJudDeu que tengan asociadas deudas(de cualquier tabla) de la cuenta pasada como parametro
	  * @param cuenta
	  * @return
	 * @throws Exception 
	  */
	 public List<GesJudDeu> getListByCuenta(DeuCueGesJudSearchPage searchPage) throws Exception{
		 
		List<GesJudDeu> listGesJudDeu = new ArrayList<GesJudDeu>();
		
		// Se hacen consultas separadas por cuestiones de performance
		
		// Deuda Admin 
		String queryString = "select gesJudDeu.* from gde_gesjuddeu gesJudDeu, gde_deudaadmin deuda " +
		 		"where gesJudDeu.iddeuda=deuda.id and deuda.idcuenta ="+searchPage.getCuenta().getId();
 		Session session = SiatHibernateUtil.currentSession();
 		Query query = session.createSQLQuery(queryString).addEntity(GesJudDeu.class);
 		listGesJudDeu.addAll(query.list());
 		
 		// Deuda Judicial
 		queryString = "select gesJudDeu.* from gde_gesjuddeu gesJudDeu, gde_deudaJudicial deuda " +
 		"where gesJudDeu.iddeuda=deuda.id and deuda.idcuenta ="+searchPage.getCuenta().getId();
 		session = SiatHibernateUtil.currentSession();
 		query = session.createSQLQuery(queryString).addEntity(GesJudDeu.class);
 		listGesJudDeu.addAll(query.list());
 		
 		// Deuda Cancelada
 		queryString = "select gesJudDeu.* from gde_gesjuddeu gesJudDeu, gde_deudaCancelada deuda " +
 		"where gesJudDeu.iddeuda=deuda.id and deuda.idcuenta ="+searchPage.getCuenta().getId();
		session = SiatHibernateUtil.currentSession();
		query = session.createSQLQuery(queryString).addEntity(GesJudDeu.class);
		listGesJudDeu.addAll(query.list());
		
		// Deuda Anulada
		queryString = "select gesJudDeu.* from gde_gesjuddeu gesJudDeu, gde_deudaAnulada deuda " +
 		"where gesJudDeu.iddeuda=deuda.id and deuda.idcuenta ="+searchPage.getCuenta().getId();
		session = SiatHibernateUtil.currentSession();
		query = session.createSQLQuery(queryString).addEntity(GesJudDeu.class);
		listGesJudDeu.addAll(query.list());
		
		if(searchPage.getReport().getImprimir())
			imprimirGenerico(listGesJudDeu, searchPage.getReport());
			
		return listGesJudDeu;
	 }

	public static void setMigId(long migId) {
		GesJudDeuDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}
	 
	 
}
