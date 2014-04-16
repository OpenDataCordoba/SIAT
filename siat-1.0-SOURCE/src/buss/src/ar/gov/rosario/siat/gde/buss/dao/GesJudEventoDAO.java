//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.GesJudEvento;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class GesJudEventoDAO extends GenericDAO {

//	private Log log = LogFactory.getLog(GesJudEventoDAO.class);	
	
	private static long migId = -1;
	
	public GesJudEventoDAO() {
		super(GesJudEvento.class);
	}
	


	/**
	 * Obtiene un GesJudEvento por su codigo
	 */
	public GesJudEvento getByCodigo(String codigo) throws Exception {
		GesJudEvento gesJudEvento;
		String queryString = "from GesJudEvento t where t.codGesJudEvento = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		gesJudEvento = (GesJudEvento) query.uniqueResult();	

		return gesJudEvento; 
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
	  *  Inserta una linea con los datos de la GesJudEvento para luego realizar un load desde Informix.
	  *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	  * @param gesJudEvento, output - Evento de Gestion Judicial a crear y el Archivo al que se le agrega la linea.
	  * @return long - El id generado.
	  * @throws Exception
	  */
	 public Long createForLoad(GesJudEvento o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = getNextId(output.getPath(), output.getNameFile());
		 
		 // Estrucura de la linea:
		 // id|idgesjud|idevento|fechaevento|observacion|usuario|fechaultmdf|estado 
		 StringBuffer line = new StringBuffer();
		 line.append(id);		 
		 line.append("|");
		 line.append(o.getGesJud().getId());		 
		 line.append("|");
		 line.append(o.getEvento().getId());		 
		 line.append("|");
		 line.append(DateUtil.formatDate(o.getFechaEvento(), "yyyy-MM-dd HH:mm:ss"));
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



	public static void setMigId(long migId) {
		GesJudEventoDAO.migId = migId;
	}



	public static long getMigId() {
		return migId;
	}

}
