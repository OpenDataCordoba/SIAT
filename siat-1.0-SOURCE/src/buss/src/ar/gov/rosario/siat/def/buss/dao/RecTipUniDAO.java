//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecTipUni;
import ar.gov.rosario.siat.def.buss.bean.Recurso;

public class RecTipUniDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecTipUniDAO.class);	
	
	public RecTipUniDAO() {
		super(RecTipUni.class);
	}
	
	
	public List<RecTipUni>getListByRecurso(Recurso recurso){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString= "FROM RecTipUni r WHERE r.recurso.id = "+recurso.getId();
		
		Query query = session.createQuery(queryString);
		
		List<RecTipUni> listRecTipUni = (List<RecTipUni>)query.list();
		
		return listRecTipUni;
		
	}
	
	public List<RecTipUni> getListTipUniByRecurso(Recurso recurso){
		Session session = SiatHibernateUtil.currentSession();
		
		List<RecTipUni> listRecTipUni;
		
		String queryString = "FROM RecTipUni r where  r.recurso.id = "+recurso.getId();
		
		queryString += " AND r.estado = 1";
				
		Query query = session.createQuery(queryString);
		
		listRecTipUni = (List<RecTipUni>) query.list();
		
		return listRecTipUni;
	}
	
	
	/** 
	 *  Devuelve el registro de Tipo Unidad (Unidad de Medida) segun el codigo de sincronismo afip indicado.
	 * 
	 * @param codigoAfip
	 * @return
	 */
	public RecTipUni getByCodigoAfip(String codigoAfip){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString= "FROM RecTipUni r WHERE r.codigoAfip = '"+codigoAfip+"'";
		
		Query query = session.createQuery(queryString);
		
		query.setMaxResults(1);
		
		RecTipUni recTipUni = (RecTipUni) query.uniqueResult();
		
		return recTipUni;
	}
	
}
