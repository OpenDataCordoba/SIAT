//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.def.iface.model.ServicioBancoSearchPage;
import coop.tecso.demoda.buss.helper.ListUtilBean;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class ServicioBancoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ServicioBancoDAO.class);	
	
	public ServicioBancoDAO() {
		super(ServicioBanco.class);
	}
	 
	public List<ServicioBanco> getListBySearchPage(ServicioBancoSearchPage servicioBancoSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ServicioBanco t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ServicioBancoSearchPage: " + servicioBancoSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (servicioBancoSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro por codServicioBanco
 		if (!StringUtil.isNullOrEmpty(servicioBancoSearchPage.getServicioBanco().getCodServicioBanco())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codServicioBanco)) like '%" + 
				StringUtil.escaparUpper(servicioBancoSearchPage.getServicioBanco().getCodServicioBanco()) + "%'";
			flagAnd = true;
		}
 		
		// filtro por desServicioBanco
 		if (!StringUtil.isNullOrEmpty(servicioBancoSearchPage.getServicioBanco().getDesServicioBanco())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desServicioBanco)) like '%" + 
				StringUtil.escaparUpper(servicioBancoSearchPage.getServicioBanco().getDesServicioBanco()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codServicioBanco ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ServicioBanco> listServicioBanco = (ArrayList<ServicioBanco>) executeCountedSearch(queryString, servicioBancoSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listServicioBanco;
	}

	/**
	 * Devuelve el servicioBanco vigente para un recurso a la fecha del dia
	 * @param recurso
	 * @return
	 */
	public List<ServicioBanco> getVigente(Recurso recurso){
		Date fechaHoy = new Date();
		log.debug("%%%% getVigente(Recurso recurso");
		String query = "Select serBanRec.servicioBanco FROM SerBanRec serBanRec WHERE " +
						" serBanRec.recurso.id="+recurso.getId()+" AND " +
						" serBanRec.fechaDesde <= TO_DATE('" +DateUtil.formatDate(fechaHoy, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
						" AND (serBanRec.fechaHasta IS NULL OR serBanRec.fechaHasta >= TO_DATE('"+DateUtil.formatDate(fechaHoy, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') )";
		Session ses = SiatHibernateUtil.currentSession();
		List<ServicioBanco> listServicioBanco = (ArrayList<ServicioBanco>)ses.createQuery(query).list();
		if (listServicioBanco==null)
			log.debug("%%%% lista nula");
		else if (listServicioBanco.size()==0)
			log.debug("%%%% lista vacia");
		else
			log.debug("%%%% recupera: " + listServicioBanco.size());
		
		return listServicioBanco;
	}
	
	
	/**
	 * Obtiene un ServicioBanco por su codigo
	 */
	public ServicioBanco getByCodigo(String codigo) {
		ServicioBanco servicioBanco;
		String queryString = "from ServicioBanco t where t.codServicioBanco = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		servicioBanco = (ServicioBanco) query.uniqueResult();	

		return servicioBanco; 
	}
	
	
	public List<ServicioBanco> getListPoseeRecurso(){
		
		String query = "FROM ServicioBanco servicioBanco WHERE " +
						" size(servicioBanco.listSerBanRec) > 0 ";
		
		Session ses = SiatHibernateUtil.currentSession();
		List<ServicioBanco> listServicioBanco = (ArrayList<ServicioBanco>)ses.createQuery(query).list();
				
		return listServicioBanco;
		
	}
	
	/**
	 * Devuelve el servicioBanco vigente para una lista de recursos a la fecha del dia
	 * 
	 * @param listRecurso
	 * @return
	 */
	public List<ServicioBanco> getListVigenteByListRecurso(List<Recurso> listRecurso) throws Exception{
		Session session = SiatHibernateUtil.currentSession();

		Date fechaHoy = new Date();
		
		String listIdRecurso = ListUtil.getStringIdsFromListModel(ListUtilBean.toVO(listRecurso, 0, false));
		
		String queryString = "Select serBanRec.servicioBanco FROM SerBanRec serBanRec WHERE " +
						" serBanRec.recurso.id IN ("+listIdRecurso+") AND " +
						" serBanRec.fechaDesde <= TO_DATE('" +DateUtil.formatDate(fechaHoy, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
						" AND (serBanRec.fechaHasta IS NULL OR serBanRec.fechaHasta >= TO_DATE('"+DateUtil.formatDate(fechaHoy, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') )";
		
		Query query = session.createQuery(queryString);
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<ServicioBanco> listServicioBanco = (ArrayList<ServicioBanco>) query.list();
		
		return listServicioBanco;
	}
	
	
	/**
	 * Devuelve el recursos vigentes a la fecha del dia que envien a judicial 
	 * 
	 * @param servicioBanco
	 * @return
	 */
	public List<Recurso> getListRecursoVigenteQueEnviaJudicial(ServicioBanco servicioBanco) throws Exception{
		Session session = SiatHibernateUtil.currentSession();
		
		Date fechaHoy = new Date();
		
		String queryString = "select serBanRec.recurso FROM SerBanRec serBanRec WHERE " +
						" serBanRec.servicioBanco.id = " + servicioBanco.getId()+" AND " +
						" serBanRec.recurso.enviaJudicial = " + SiNo.SI.getId()+" AND " +
						" serBanRec.fechaDesde <= TO_DATE('" +DateUtil.formatDate(fechaHoy, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
						" AND (serBanRec.fechaHasta IS NULL OR serBanRec.fechaHasta >= TO_DATE('"+DateUtil.formatDate(fechaHoy, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') )";
		
		Query query = session.createQuery(queryString);
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Recurso> listRecurso = (ArrayList<Recurso>) query.list();
		
		return listRecurso;
	}

}
