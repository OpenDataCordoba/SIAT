//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.bean.DesGen;
import ar.gov.rosario.siat.gde.iface.model.DesGenSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DesGenVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;

public class DesGenDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DesGenDAO.class);	
	
	public DesGenDAO() {
		super(DesGen.class);
	}

	public List<DesGen> getListBySearchPage(DesGenSearchPage desGenSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from DesGen t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del DesGenSearchPage: " + desGenSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		// Si estoy en modo seleccionar solo muestro los activos
		if (desGenSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
 		// filtro atributos excluidos
 		List<DesGenVO> listDesGenExcluidos = (ArrayList<DesGenVO>) desGenSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listDesGenExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listDesGenExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
 		// Order By
		queryString += " order by t.id ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<DesGen> listDesGen = (ArrayList<DesGen>) executeCountedSearch(queryString, desGenSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listDesGen;
	}

/*	public static List<DesGen> getDesGenVigentes(Cuenta cuenta) {
		// TODO Auto-generated method stub
		return null;
	}
*/
	/**
	 * Obtiene el DesGen vigente para un recurso.
	 * Solo lo puede haber 1
	 */
	public List<DesGen> getVigente(Recurso recurso, Date fecha) {
		String query = "Select serBan.desGen FROM SerBanDesGen serBan, SerBanRec banRec WHERE serBan.servicioBanco.id=banRec.servicioBanco.id AND " +
						" banRec.recurso.id="+recurso.getId()+" AND " +
						" serBan.fechaDesde <= TO_DATE('" +DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
						" AND (serBan.fechaHasta IS NULL OR serBan.fechaHasta >= TO_DATE('"+DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') )" +
						" AND serBan.desGen.estado="+Estado.ACTIVO.getId();
		
		Session ses = SiatHibernateUtil.currentSession();
		List<DesGen> listDesGen = (ArrayList<DesGen>)ses.createQuery(query).list();
		return listDesGen;
	}
	
}
