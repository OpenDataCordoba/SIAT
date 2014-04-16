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
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.ef.buss.bean.EstadoActa;
import ar.gov.rosario.siat.ef.buss.bean.EstadoOpeInvCon;
import ar.gov.rosario.siat.ef.buss.bean.OpeInv;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvCon;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConVO;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlContrSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Contribuyente;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

public class OpeInvConDAO extends GenericDAO {

	private Log log = LogFactory.getLog(OpeInvConDAO.class);
	
	public OpeInvConDAO() {
		super(OpeInvCon.class);
	}
	
	public List<OpeInvCon> getBySearchPage(OpeInvConSearchPage opeInvConSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = generarQuery4SearchPage(opeInvConSearchPage);

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OpeInvConSearchPage: " + opeInvConSearchPage.infoString()); 
		}			
 			
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OpeInvCon> listOpeInvCon = (ArrayList<OpeInvCon>) executeCountedSearch(queryString, opeInvConSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOpeInvCon;
	}

	public List<OpeInvCon> getBySearchPage4ActasInicio(OpeInvConSearchPage opeInvConSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = generarQuery4SearchPage(opeInvConSearchPage);
		boolean investigadorNotNull = opeInvConSearchPage.getOpeInvCon().getInvestigador().getId()!=null &&
						opeInvConSearchPage.getOpeInvCon().getInvestigador().getId().longValue()>-3;
						
	    boolean flagAnd = !ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getOpeInv().getPlanFiscal())
	    				|| !ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getEstadoOpeInvCon())
	    				|| investigadorNotNull
	    				|| !ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getZona())
	    				|| !ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getContribuyente().getPersona());

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OpeInvConSearchPage: " + opeInvConSearchPage.infoString()); 
		}			
 		
 		// filtro por estado
 		if (ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getEstadoOpeInvCon())) {
             queryString += flagAnd ? " and " : " where ";
			 queryString += "( t.estadoOpeInvCon.id=" + EstadoOpeInvCon.ID_ASIG_INV + 
			 									" OR t.estadoOpeInvCon.id=" + EstadoOpeInvCon.ID_VISITAR + " )";
			 flagAnd = true;
		}
 		
 		
 		 			
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OpeInvCon> listOpeInvCon = (ArrayList<OpeInvCon>) executeCountedSearch(queryString, opeInvConSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOpeInvCon;
	}
	
	public List<OpeInvCon> getBySearchPage4ADMActas(OpeInvConSearchPage opeInvConSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = generarQuery4SearchPage(opeInvConSearchPage);
		boolean investigadorNotNull = opeInvConSearchPage.getOpeInvCon().getInvestigador().getId()!=null &&
									opeInvConSearchPage.getOpeInvCon().getInvestigador().getId().longValue()>-3;
		
	    boolean flagAnd = investigadorNotNull ||
	    				!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getOpeInv().getPlanFiscal())
	    				|| !ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getOpeInv())
	    				|| !ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getEstadoOpeInvCon());
	    				//|| !ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getActaInv().getEstadoActa());

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OpeInvConSearchPage: " + opeInvConSearchPage.infoString()); 
		}			
 		
 		// filtro por estado
 		if (ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getEstadoOpeInvCon())) {
             queryString += flagAnd ? " and " : " where ";
			 queryString += "( t.estadoOpeInvCon.id=" + EstadoOpeInvCon.ID_ASIG_INV + 
			 								" OR t.estadoOpeInvCon.id=" + EstadoOpeInvCon.ID_NO_EXISTE +
		 									" OR t.estadoOpeInvCon.id=" + EstadoOpeInvCon.ID_CON_ACTA +
		 									" OR t.estadoOpeInvCon.id=" + EstadoOpeInvCon.CON_INTERES_FISCAL+
		 									" OR t.estadoOpeInvCon.id=" + EstadoOpeInvCon.SIN_INTERES_FISCAL +
		 									" OR t.estadoOpeInvCon.id=" + EstadoOpeInvCon.CON_INTERES_A_FUTURO +
		 									" )";
			 flagAnd = true;
		}
 		
 		// Si el usuario es investigador, filtra hasta los del día, sino busca todos
 		if (!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getInvestigador())){
			queryString += flagAnd ? " and " : " where ";
 			queryString +=" t.fechaVisita<= TO_DATE('"+DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK)+
 																							"', '%d/%m/%Y')";
 			flagAnd = true;
 		}
 		
 		// filtro por estado de Acta
 		Long idEstadoActa = opeInvConSearchPage.getOpeInvCon().getActaInv().getEstadoActa().getId();
		if (idEstadoActa!=null && idEstadoActa.longValue()>-2){
			if(idEstadoActa.equals(-1L)){ // sin acta
				queryString += flagAnd ? " and " : " where ";
				queryString +=" t.actaInv.estado is null ";				
			}else if(idEstadoActa.longValue()>0){// con un determinado estado
				queryString += flagAnd ? " and " : " where ";
				queryString +=" t.actaInv.estado.id = "+idEstadoActa;				
			} 														
			flagAnd = true;				
 		}
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OpeInvCon> listOpeInvCon = (ArrayList<OpeInvCon>) executeCountedSearch(queryString, opeInvConSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOpeInvCon;
	}
	
	public List<OpeInvCon> getBySearchPage4AprobActas(OpeInvConSearchPage opeInvConSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = generarQuery4SearchPage(opeInvConSearchPage);

	    boolean flagAnd =true;
	    
 		// filtro por estado de contribuyente
 		if (ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getEstadoOpeInvCon())) {
             queryString += flagAnd ? " and " : " where ";
			 queryString += "( t.estadoOpeInvCon.id=" +  EstadoOpeInvCon.CON_INTERES_FISCAL+
		 									" OR t.estadoOpeInvCon.id=" + EstadoOpeInvCon.SIN_INTERES_FISCAL +
		 									" OR t.estadoOpeInvCon.id=" + EstadoOpeInvCon.CON_INTERES_A_FUTURO +
		 									" )";
			 flagAnd = true;
		}
 		
 		// filtro por estado de Acta
 		if (!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getActaInv().getEstadoActa())){
			queryString += flagAnd ? " and " : " where ";
			queryString +=" t.actaInv.estadoActa.id = "+opeInvConSearchPage.getOpeInvCon().getActaInv().getEstadoActa().getId();				
		}else{ 														
			queryString += flagAnd ? " and " : " where ";
			queryString +=" t.actaInv.estadoActa.id in("+EstadoActa.ID_APROBADA+","+
																		EstadoActa.ID_EN_ESPERA_APROBACION+")";
 		}
 		flagAnd=true;
 		
 		// filtro por orden de control (que no tenga ninguna)
 		queryString += flagAnd ? " and " : " where ";
 		queryString +=" t.ordenControl is null"; 				
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OpeInvCon> listOpeInvCon = (ArrayList<OpeInvCon>) executeCountedSearch(queryString, opeInvConSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOpeInvCon;
	}

	public List<OpeInvCon> getBySearchPage4OrdenContrl(OrdenControlContrSearchPage ordenControlContrSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String queryString = "from OpeInvCon t WHERE t.ordenControl is null and t.estadoOpeInvCon.id=" +
					EstadoOpeInvCon.CON_INTERES_FISCAL +" and t.actaInv.estadoActa.id="+EstadoActa.ID_APROBADA;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OpeInvConSearchPage: " + ordenControlContrSearchPage.infoString()); 
		}
	
 		//filtro por opeInv
 		if (!ModelUtil.isNullOrEmpty(ordenControlContrSearchPage.getOpeInvCon().getOpeInv())) {
            queryString += "and t.opeInv.id=" + 
            ordenControlContrSearchPage.getOpeInvCon().getOpeInv().getId();			
		}
 		
 		//filtro de planFiscal
 		if (!ModelUtil.isNullOrEmpty(ordenControlContrSearchPage.getOpeInvCon().getOpeInv().getPlanFiscal())){
			queryString += "and t.opeInv.planFiscal.id=" + ordenControlContrSearchPage.getOpeInvCon().getOpeInv().getPlanFiscal().getId();
 		}

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<OpeInvCon> listOpeInvCon = (ArrayList<OpeInvCon>) executeCountedSearch(queryString, ordenControlContrSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listOpeInvCon;

	}

	/**
	 * Genera el query para los filtros:
	 * - opeInvCon excluidos
	 * - opeInv
	 * - planFiscal
	 * - investigador (todos, ninguno, o uno en particular)
	 * - zona
	 * - busqueda masiva
	 * - estado
	 * @param opeInvConSearchPage
	 * @return
	 */
	private String generarQuery4SearchPage(OpeInvConSearchPage opeInvConSearchPage){
		String queryString = "from OpeInvCon t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del OpeInvConSearchPage: " + opeInvConSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (opeInvConSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		
		// filtro opeInvCon excluidos
 		List<OpeInvConVO> listOpeInvConExcluidos = (List<OpeInvConVO>) opeInvConSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listOpeInvConExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listOpeInvConExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		

 		//filtro por opeInv
 		if (!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getOpeInv())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.opeInv.id=" + 
				opeInvConSearchPage.getOpeInvCon().getOpeInv().getId();				
			flagAnd = true;
		}
 		
 		//filtro de planFiscal
 		if (!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getOpeInv().getPlanFiscal())){
 			queryString += flagAnd ? " and " : " where ";
			queryString += " t.opeInv.planFiscal.id=" + opeInvConSearchPage.getOpeInvCon().getOpeInv().getPlanFiscal().getId();
			flagAnd = true;
 		}
 		
 		//filtro de investigador
 		if(opeInvConSearchPage.getOpeInvCon().getInvestigador().getId()!=null && 
			opeInvConSearchPage.getOpeInvCon().getInvestigador().getId().equals(-2L)){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.investigador is null"; 
			flagAnd = true;
		}else if(opeInvConSearchPage.getOpeInvCon().getInvestigador().getId()!=null && 
			opeInvConSearchPage.getOpeInvCon().getInvestigador().getId().equals(-1L)){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.investigador is not null"; 
			flagAnd = true;			
		}else if (!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getInvestigador())){
 			queryString += flagAnd? " and " : " where ";
 			queryString += " t.investigador.id="+opeInvConSearchPage.getOpeInvCon().getInvestigador().getId();
 			flagAnd=true;
 		}
 		 		
 		//filtro de zona
 		if (!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getZona())){
 			queryString += flagAnd? " and " : " where ";
 			queryString += " t.zona.id="+opeInvConSearchPage.getOpeInvCon().getZona().getId();
 			flagAnd = true;
 		}
		
 		// filtro por busqueda masiva
 		if (!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getOpeInvBus())){
 			queryString += flagAnd? " and " : " where ";
 			queryString += " t.opeInvBus.id="+opeInvConSearchPage.getOpeInvCon().getOpeInvBus().getId();
 			flagAnd = true;
 		}
 		
 		// filtro por estado
 		if (!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getEstadoOpeInvCon())) {
            queryString += flagAnd ? " and " : " where ";
			queryString += " t.estadoOpeInvCon.id=" + 
				opeInvConSearchPage.getOpeInvCon().getEstadoOpeInvCon().getId();				
			flagAnd = true;
		}
 		
 		if(!ModelUtil.isNullOrEmpty(opeInvConSearchPage.getOpeInvCon().getContribuyente().getPersona())){
 			queryString += flagAnd ? " and " : " where ";
 			queryString += " t.idContribuyente = "+opeInvConSearchPage.getOpeInvCon().getContribuyente().getPersona().getId();
 			flagAnd = true;
 		}
 		return queryString;
	}
	
	/**
	 * Obtiene un OpeInvCon por su codigo
	 */
	public OpeInvCon getByCodigo(String codigo) throws Exception {
		OpeInvCon opeInvCon;
		String queryString = "from OpeInvCon t where t.codOpeInvCon = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		opeInvCon = (OpeInvCon) query.uniqueResult();	

		return opeInvCon; 
	}


	/**
	 * Obtiene un opeInvCon con los valores pasados como parametros
	 * @param contribuyente
	 * @param opeInv2
	 * @return NULL si no encuentra nada
	 */
	public OpeInvCon getByContrYOpeInv(Contribuyente contribuyente,OpeInv opeInv) {
		String queryString = "from OpeInvCon t where t.idContribuyente = :idContribuyente and " +
				"t.opeInv = :opeInv";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setLong("idContribuyente", contribuyente.getId())
						.setEntity("opeInv", opeInv);
		return (OpeInvCon) query.uniqueResult(); 

	}
	

	public List<OpeInvCon> getListByOpeInv(OpeInv opeInv) {
		String queryString = "from OpeInvCon t where t.opeInv = :opeInv";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("opeInv", opeInv)
		;
		
		return query.list(); 
	}


	/**
	 * Obtiene una lista de ids de contribuyentes que esten en algun operativo (exista en un registro de opeInvCon) y en el estado pasado como parametro
	 * @param idEstadoOpeInv
	 * @return
	 */
	public List<Long> getlistContrByEstOpeInv(Long idEstadoOpeInv) {
		String queryString = "SELECT distinct(idContribuyente) FROM OpeInvCon where estadoOpeInvCon.id="+idEstadoOpeInv;
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		return query.list(); 

	}


	public List<OpeInvCon> getListByIds(Long[] idsOpeInvConSelected) {
		String queryString = "from OpeInvCon t where t.id in("+StringUtil.getStringComaSeparate(
																					 idsOpeInvConSelected)+")";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString);
		
		return query.list(); 

	}
	
	/**
	 * Obtiene una lista de opeInvCon ordenados por zona y catastral de la cuenta asociada (haciendo los join).<br>
	 * Incluye también a los opeInvCon que no tengan cuenta asociada
	 * @param idsOpeInvCon
	 * @return
	 * @throws Exception 
	 */
	public List<OpeInvCon> getListByIdsOrderByZonaYCataDesc(Long[] idsOpeInvConSelected) throws Exception {
		String queryString = "select opeInvCon.*, objImpAtrVal.strvalor " +
				"from ef_opeinvcon opeInvCon left join pad_cuenta cuenta on opeInvCon.idcuenta=cuenta.id" +
				" left join pad_objimpatrval objImpAtrVal on cuenta.idobjimp=objImpAtrVal.idobjimp" +
				" left join def_tipobjimpatr tipObjImpAtr on objImpAtrVal.idtipobjimpatr=tipObjImpAtr.id" +
				" where opeInvCon.id in("+StringUtil.getStringComaSeparate(idsOpeInvConSelected)+") " +
				"and (opeInvCon.idcuenta is null or tipObjImpAtr.idatributo="+
				Atributo.getByCodigo(Atributo.COD_CATASTRAL).getId()+
				") order by objImpAtrVal.strvalor desc";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString).addEntity(OpeInvCon.class);
		
		log.debug("query generada:"+queryString);
		return query.list(); 

	}



	
}
