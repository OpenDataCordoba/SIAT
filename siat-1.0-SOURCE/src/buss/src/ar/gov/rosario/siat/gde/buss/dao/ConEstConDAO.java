//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ConEstCon;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConvenio;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ConEstConDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConEstConDAO.class);	
	
	private static long migId = -1;
	
	public ConEstConDAO() {
		super(ConEstCon.class);
	}
	
	/**
	 * Obtiene un ConEstCon por su codigo
	 */
	public ConEstCon getByCodigo(String codigo) throws Exception {
		ConEstCon conEstCon;
		String queryString = "from ConEstCon t where t.codConEstCon = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		conEstCon = (ConEstCon) query.uniqueResult();	

		return conEstCon; 
	}
	
	public Date getFechaUltEstRecompuesto (Long idConvenio){
		ConEstCon conEstCon;
		String queryString = "from ConEstCon t where t.convenio.id = :idConvenio ";
		queryString += " and t.estadoConvenio.id = :idEstadoConvenio ";
		queryString += " ORDER BY t.fechaConEstCon DESC";
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString).setLong("idConvenio", idConvenio);
		query.setLong("idEstadoConvenio", EstadoConvenio.ID_RECOMPUESTO);
		query.setMaxResults(1);
		conEstCon = (ConEstCon) query.uniqueResult();
		return conEstCon.getFechaConEstCon();
	}
	
	public List<ConEstCon>getByIdConvenio(Long idConvenio) throws Exception{
		List<ConEstCon> listConEstCon;
		
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "FROM ConEstCon c WHERE c.convenio.id = "+idConvenio;
		queryString += " ORDER BY c.fechaConEstCon DESC, c.id DESC";
		
		Query query = session.createQuery(queryString);
		
		listConEstCon = (List<ConEstCon>)query.list();
		
		return listConEstCon;
	}
	/**
	 *  Devuelve el proximo valor de id a asignar. 
	 *  Para se inicializa obteniendo el ultimo id asignado el archivo de migracion con datos pasados como parametro
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
	 *  Inserta una linea con los datos Historicos de Estado de Convenio para luego realizar un load desde Informix.
	 *  (la linea se inserta en el archivo pasado como parametro a traves del LogFile)
	 * @param conEstCon, output - El ConEstCon a crear y el Archivo al que se le agrega la linea.
	 * @return long - El id generado.
	 * @throws Exception
	 */
	public Long createForLoad(ConEstCon o, LogFile output) throws Exception {

		 // Obtenemos el valor del id autogenerado a insertar.
		 long id = 0;
		 if(getMigId() == -1){
			 id = this.getLastId(output.getPath(), output.getNameFile())+1;
			 // Id Preseteado (Inicialmente usado para la migracion de CdM)
			 // Archivo con una unica linea:
			 // 54378|
			 long idPreset = this.getLastId(output.getPath(), "idConEstCon.set");
			 if(id <= idPreset){
				 id = idPreset;
			 }
			 setMigId(id);				 
		 }else{
			 id = getNextId(output.getPath(), output.getNameFile());
		 }
		 
		// Estrucura de la linea:
		// id|idconvenio|idestadoconvenio|fechaconestcon|observacion|usuario|fechaultmdf|estado
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getConvenio().getId());
		line.append("|");
		line.append(o.getEstadoConvenio().getId());		 
		line.append("|");
		line.append(DateUtil.formatDate(o.getFechaConEstCon(), "yyyy-MM-dd"));// HH:mm:ss"));		 
		line.append("|");
		if(o.getObservacion() != null)
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

	public static void setMigId(long migId) {
		ConEstConDAO.migId = migId;
	}

	public static long getMigId() {
		return migId;
	}
}
