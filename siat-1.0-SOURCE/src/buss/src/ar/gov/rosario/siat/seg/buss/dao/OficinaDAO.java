//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.seg.buss.bean.Oficina;
import ar.gov.rosario.siat.seg.iface.model.OficinaSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class OficinaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OficinaDAO.class);	
	
	public OficinaDAO() {
		super(Oficina.class);
	}
	
	
	public List<Oficina> getBySearchPage(OficinaSearchPage oficinaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Oficina t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OficinaSearchPage: " + oficinaSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (oficinaSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}

		// area de la oficina
		if (!ModelUtil.isNullOrEmpty(oficinaSearchPage.getOficina().getArea())) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.area.id = "+ oficinaSearchPage.getOficina().getArea().getId();
	      flagAnd = true;
		}
		
		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(oficinaSearchPage.getOficina().getDesOficina())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desOficina)) like '%" + 
				StringUtil.escaparUpper(oficinaSearchPage.getOficina().getDesOficina()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.desOficina ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Oficina> listOficina = (ArrayList<Oficina>) executeCountedSearch(queryString, oficinaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOficina;
	}

	/**
	 * Obtiene un Oficina por su codigo
	 */
	public Oficina getByCodigo(String codigo) throws Exception {
		Oficina Oficina;
		String queryString = "from Oficina t where t.codOficina = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		Oficina = (Oficina) query.uniqueResult();	

		return Oficina; 
	}
	
}
