//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;


import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ConDeuTit;
import ar.gov.rosario.siat.gde.buss.bean.ConstanciaDeu;
import coop.tecso.demoda.buss.helper.LogFile;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ConDeuTitDAO extends GenericDAO {

//	private Log log = LogFactory.getLog(ConDeuTitDAO.class);	
	private static long migId = -1;
	
	public ConDeuTitDAO() {
		super(ConDeuTit.class);
	}
	

	/**
	 * Obtiene un ConDeuTit por su codigo
	 */
	public ConDeuTit getByCodigo(String codigo) throws Exception {
		ConDeuTit conDeuTit;
		String queryString = "from ConDeuTit t where t.codConDeuTit = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		conDeuTit = (ConDeuTit) query.uniqueResult();	

		return conDeuTit; 
	}

	/**
	 * Borra los ConDeuTit la constancia de Deuda
	 * @param constanciaDeu
	 * @return int
	 */
	public int deleteByConstanciaDeu(ConstanciaDeu constanciaDeu) {
		
		String queryString = "DELETE FROM ConDeuTit cdt WHERE cdt.constanciaDeu = :constanciaDeu";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("constanciaDeu", constanciaDeu);
		
		return query.executeUpdate();	
	}

	
	public static long getMigId() {
		return migId;
	}

	public static void setMigId(long migId) {
		ConDeuTitDAO.migId = migId;
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
	
	
	public Long createForLoad(ConDeuTit o, LogFile output) throws Exception {
		
		long id = getNextId(output.getPath(), output.getNameFile());
		 
		// Estrucura de la linea:
		//id|idconstanciadeu|idpersona|usuario|fechaultmdf|estado
		
		StringBuffer line = new StringBuffer();
		line.append(id);		 
		line.append("|");
		line.append(o.getConstanciaDeu().getId());
		line.append("|");
		line.append(o.getIdPersona());
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
}
