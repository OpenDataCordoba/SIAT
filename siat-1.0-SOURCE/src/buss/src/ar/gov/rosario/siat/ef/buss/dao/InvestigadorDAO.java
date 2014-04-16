//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.Investigador;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorSearchPage;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class InvestigadorDAO extends GenericDAO {

	private Log log = LogFactory.getLog(InvestigadorDAO.class);
	
	public InvestigadorDAO() {
		super(Investigador.class);
	}
	
	public List<Investigador> getBySearchPage(InvestigadorSearchPage investigadorSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Investigador t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del InvestigadorSearchPage: " + investigadorSearchPage.infoString()); 
		}
	
		if (investigadorSearchPage.getModoSeleccionar()) {
			  queryString += flagAnd ? " and " : " where ";
		      queryString += " t.estado = "+ Estado.ACTIVO.getId();
		      // filtra los vigentes
		      queryString += " AND t.fechaDesde<=TO_DATE('" +DateUtil.formatDate(
		    		  new Date(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
		      queryString +=" AND (t.fechaHasta IS NULL OR t.fechaHasta>=TO_DATE('" +DateUtil.formatDate(
		    		  new Date(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))" ;
		      flagAnd = true;
			}else{
				// filtro fechaDesde
		 		if (investigadorSearchPage.getInvestigador().getFechaDesde()!=null) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.fechaDesde>=TO_DATE('" +DateUtil.formatDate(
						investigadorSearchPage.getInvestigador().getFechaDesde(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
					flagAnd = true;
				}
		 		
				// filtro fechaDesde
		 		if (investigadorSearchPage.getInvestigador().getFechaHasta()!=null) {
		            queryString += flagAnd ? " and " : " where ";
					queryString += " t.fechaHasta<=TO_DATE('" +DateUtil.formatDate(
						investigadorSearchPage.getInvestigador().getFechaHasta(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" ;
					flagAnd = true;
				} 	
			}
			
			// Filtros aqui		
			
			// filtro inspector excluidos
	 		List<InspectorVO> listInspectorExcluidos = (List<InspectorVO>) investigadorSearchPage.getListVOExcluidos();
	 		if (!ListUtil.isNullOrEmpty(listInspectorExcluidos)) {
	 			queryString += flagAnd ? " and " : " where ";

	 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listInspectorExcluidos);
				queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
				flagAnd = true;
			}				

			// filtro por descripcion
	 		if (!StringUtil.isNullOrEmpty(investigadorSearchPage.getInvestigador().getDesInvestigador())) {
	            queryString += flagAnd ? " and " : " where ";
				queryString += " UPPER(TRIM(t.desInvestigador)) like '%" + 
					StringUtil.escaparUpper(investigadorSearchPage.getInvestigador().getDesInvestigador()) + "%'";
				flagAnd = true;
			}
	 		 		
		
	 		
	 		// Order By
			queryString += " order by t.desInvestigador ";
			
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<Investigador> listInvestigador = (ArrayList<Investigador>) executeCountedSearch(queryString, investigadorSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listInvestigador;
	}

	public List<Investigador> getListActivosVigentes() {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from Investigador t WHERE  t.estado = "+ Estado.ACTIVO.getId()+
			" AND t.fechaDesde<=TO_DATE('" +DateUtil.formatDate(
	    		  new Date(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
	      	" AND (t.fechaHasta IS NULL OR t.fechaHasta>=TO_DATE('" +DateUtil.formatDate(
	    		  new Date(),DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))"+
	    	" order by t.desInvestigador ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

	    List<Investigador> listInvestigador = (ArrayList<Investigador>) SiatHibernateUtil.
	    											currentSession().createQuery(queryString).list();	    
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listInvestigador;

	}
	
}
