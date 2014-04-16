//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.frm.buss.bean.ForCam;
import coop.tecso.demoda.iface.helper.DateUtil;

public class ForCamDAO extends GenericDAO {

//	private Log log = LogFactory.getLog(ForCamDAO.class);	
	
	public ForCamDAO() {
		super(ForCam.class);
	}
	
/*	public List<ForCam> getBySearchPage(ForCamSearchPage forCamSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from ForCam t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del ForCamSearchPage: " + forCamSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (forCamSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		/* Ejemplos:
		
		// filtro forCam excluidos
 		List<ForCamVO> listForCamExcluidos = (List<ForCamVO>) forCamSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listForCamExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listForCamExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por codigo
 		if (!StringUtil.isNullOrEmpty(forCamSearchPage.getForCam().getCodForCam())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.codForCam)) like '%" + 
				StringUtil.escaparUpper(forCamSearchPage.getForCam().getCodForCam()) + "%'";
			flagAnd = true;
		}

		// filtro por descripcion
 		if (!StringUtil.isNullOrEmpty(forCamSearchPage.getForCam().getDesForCam())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " UPPER(TRIM(t.desForCam)) like '%" + 
				StringUtil.escaparUpper(forCamSearchPage.getForCam().getDesForCam()) + "%'";
			flagAnd = true;
		}
 		
 		// Order By
		queryString += " order by t.codForCam ";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<ForCam> listForCam = (ArrayList<ForCam>) executeCountedSearch(queryString, forCamSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listForCam;
	}*/

	/**
	 * Obtiene un ForCam por su codigo
	 */
	public ForCam getByCodigo(String codigo) throws Exception {
		ForCam forCam;
		String queryString = "from ForCam t where t.codForCam = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		forCam = (ForCam) query.uniqueResult();	

		return forCam; 
	}

	public boolean existe(Long idForm, Long idForCamActual, Date fechaDesde, Date fechaHasta) {
		// Trae las que empiezen y/o terminen entre esas fechas ó las que contengan al rango ingresado
		String queryString = "from ForCam t where t.formulario.id="+idForm;
		if(idForCamActual!=null && idForCamActual>0)
				queryString += " AND t.id<>"+idForCamActual;
		if(fechaDesde!=null && fechaHasta!=null){
									// registros que empiecen o terminen entre esas fechas
			queryString += " AND ( ( t.fechaDesde BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR ( t.fechaHasta BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR"+
									// registros que contengan el rango de fechas ingresado (empiezan antes y terminan después o no terminan)
									"( t.fechaDesde <=TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
										" AND ( t.fechaHasta IS NULL OR t.fechaHasta >= TO_DATE('"+
												DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+										
											  ")"+
									")"+
								")";	
		}else{
			if(fechaDesde!=null){
				queryString +=" AND ("+
										// que empiece después
									  	"t.fechaDesde >= TO_DATE('"+
											DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
										// que termine después
										" OR t.fechaHasta >= TO_DATE('"+
											DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+	
										//que empiece antes y no termine
										" OR (t.fechaDesde <= TO_DATE('"+
												DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND "+
												"t.fechaHasta IS NULL"+
											 ")"+
									")";
			}
			if(fechaHasta!=null){
				//TODO implementar esta parte, pero hay que ver si se da, porque fechaDesde no puede ser nula gralmente
			}
		}
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		List<ForCam> listResult = (ArrayList<ForCam>)query.list();	

		return (listResult!=null && !listResult.isEmpty()?true:false);
	}
	
}
