//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.rod.buss.bean.TramiteRA;
import ar.gov.rosario.siat.rod.iface.model.TramiteRASearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class TramiteRADAO extends GenericDAO {

	private Log log = LogFactory.getLog(TramiteRADAO.class);
	
	public TramiteRADAO() {
		super(TramiteRA.class);
	}
	
	public List<TramiteRA> getBySearchPage(TramiteRASearchPage tramiteRASearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from TramiteRA t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del TramiteRASearchPage: " + tramiteRASearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (tramiteRASearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
	
		
		// filtro por nroComuna
 		if (tramiteRASearchPage.getTramiteRA().getNroComuna()!=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroComuna=" +tramiteRASearchPage.getTramiteRA().getNroComuna();
			flagAnd = true;
		}
 		
 		// filtro por nroTramite
 		if (tramiteRASearchPage.getTramiteRA().getNroTramite()!=null) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.nroTramite=" +tramiteRASearchPage.getTramiteRA().getNroTramite();
			flagAnd = true;
		}

 		// filtro por mandatario
 		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(tramiteRASearchPage.getTramiteRA().getMandatario().getDescripcion())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.mandatario.descripcion)) like '%" + 
				StringUtil.escaparUpper(tramiteRASearchPage.getTramiteRA().getMandatario().getDescripcion()) + "%'";
			flagAnd = true;
		}
 		
		// filtro Fecha Envio Desde
 		if (tramiteRASearchPage.getFechaEmisionDesde() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fecha >= TO_DATE('" + 
				DateUtil.formatDate(tramiteRASearchPage.getFechaEmisionDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}
 		// filtro Fecha Envio Hasta
 		if (tramiteRASearchPage.getFechaEmisionHasta() != null ) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.fecha <= TO_DATE('" + 
			DateUtil.formatDate(tramiteRASearchPage.getFechaEmisionHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";
			flagAnd = true;
		}

 		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<TramiteRA> listTramiteRA = (ArrayList<TramiteRA>) executeCountedSearch(queryString, tramiteRASearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTramiteRA;
	}

	/**
	 * Obtiene un TramiteRA por su codigo
	 */
	public TramiteRA getByCodigo(String codigo) throws Exception {
		TramiteRA tramiteRA;
		String queryString = "from TramiteRA t where t.codTramiteRA = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		tramiteRA = (TramiteRA) query.uniqueResult();	

		return tramiteRA; 
	}
	
}
