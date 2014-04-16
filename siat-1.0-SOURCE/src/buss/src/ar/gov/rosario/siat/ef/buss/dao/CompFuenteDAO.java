//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.CompFuente;
import ar.gov.rosario.siat.ef.buss.bean.Comparacion;
import ar.gov.rosario.siat.ef.buss.bean.OrdenControl;

public class CompFuenteDAO extends GenericDAO {

	private Logger log = Logger.getLogger(CompFuenteDAO.class);
	
	public CompFuenteDAO() {
		super(CompFuente.class);
	}
	

	/**
	 * Obtiene un CompFuente por su codigo
	 */
	public CompFuente getByCodigo(String codigo) throws Exception {
		CompFuente compFuente;
		String queryString = "from CompFuente t where t.codCompFuente = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		compFuente = (CompFuente) query.uniqueResult();	

		return compFuente; 
	}
	
	
	public List<CompFuente>getListCompFuenteSinComparacion(OrdenControl ordenControl) throws Exception{
		
		String queryString = "SELECT cf FROM CompFuente cf, PlaFueDat pfd WHERE cf.plaFueDat.id = pfd.id";
		queryString+=" AND pfd.ordenControl.id = "+ordenControl.getId();
		queryString += " AND cf.comparacion.id IS NULL";
		
		log.debug("queryString: "+queryString);
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		
		
		return (List<CompFuente>) query.list();
	}
	
	public void deleteListByComparacion(Comparacion comparacion){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString="DELETE CompFuente c WHERE c.comparacion.id = "+comparacion.getId();
		
		Query query = session.createQuery(queryString);
		
		query.executeUpdate();
	}
	
}
