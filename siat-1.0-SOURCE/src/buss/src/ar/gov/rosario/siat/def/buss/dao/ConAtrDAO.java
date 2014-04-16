//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.ConAtr;
import ar.gov.rosario.siat.def.iface.model.ConAtrSearchPage;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class ConAtrDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ConAtrDAO.class);	
	
	public ConAtrDAO() {
		super(ConAtr.class);
	}
	
	public List<ConAtr> getListBySearchPage(ConAtrSearchPage conAtrSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ConAtr t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ConAtrSearchPage: " + conAtrSearchPage.infoString()); 
		}
	
		// Filtros aqui
		// Armamos filtros del HQL
		if (conAtrSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// filtro por codigo del atributo
		String codAtributo = conAtrSearchPage.getConAtr().getAtributo().getCodAtributo();
 		if (!StringUtil.isNullOrEmpty(codAtributo)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.atributo.codAtributo)) like '%" + 
				StringUtil.escaparUpper(codAtributo) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion del atributo
		String desAtributo = conAtrSearchPage.getConAtr().getAtributo().getDesAtributo();
 		if (!StringUtil.isNullOrEmpty(desAtributo)) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.atributo.desAtributo)) like '%" + 
				StringUtil.escaparUpper(desAtributo) + "%'";
			flagAnd = true;
		}

 		// Order By
		queryString += " order by t.atributo.desAtributo ";

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ConAtr> listConAtr = (ArrayList<ConAtr>) executeCountedSearch(queryString, conAtrSearchPage);

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listConAtr;
	}
	
	/**
	 * Obtiene una lista de los Atributos del Contribuyente marcados como visibles en la consulta de deuda. 
	 * 
	 * @author Cristian
	 * @return List<ConAtr>
	 * @throws Exception
	 */
	public List<ConAtr> getListActivosForWeb() throws Exception {
		
		 String queryString = "from ConAtr c where c.esVisConDeu = 1 " +
		 					  "and c.estado = " + Estado.ACTIVO.getId();

		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		return  (ArrayList<ConAtr>) query.list();
		
	}

}
