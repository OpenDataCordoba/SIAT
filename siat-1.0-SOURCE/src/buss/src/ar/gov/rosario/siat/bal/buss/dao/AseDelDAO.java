//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.AseDel;
import ar.gov.rosario.siat.bal.buss.bean.Balance;
import ar.gov.rosario.siat.bal.iface.model.AseDelSearchPage;
import ar.gov.rosario.siat.bal.iface.model.AseDelVO;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import ar.gov.rosario.siat.pro.buss.bean.EstadoCorrida;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AseDelDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AseDelDAO.class);
	
	public AseDelDAO(){
		super(AseDel.class);
	}
	
	public List<AseDel> getListBySearchPage(AseDelSearchPage aseDelSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from AseDel t ";
	    boolean flagAnd = false;

		if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AseDelSearchPage: " + aseDelSearchPage.infoString()); 
		}
	
		// Armamos filtros del HQL
		if (aseDelSearchPage.getModoSeleccionar()) {
		  queryString += flagAnd ? " and " : " where ";
	      queryString += " t.estado = "+ Estado.ACTIVO.getId();
	      flagAnd = true;
		}
		
		// Filtros aqui
		// filtro AseDel excluidos
 		List<AseDelVO> listAseDelExcluidos = (ArrayList<AseDelVO>) aseDelSearchPage.getListVOExcluidos();
 		if (!ListUtil.isNullOrEmpty(listAseDelExcluidos)) {
 			queryString += flagAnd ? " and " : " where ";

 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listAseDelExcluidos);
			queryString += " t.id NOT IN ("+ listIdExcluidos + ") "; 
			flagAnd = true;
		}
		
		// filtro por Ejercicio
 		if(!ModelUtil.isNullOrEmpty(aseDelSearchPage.getAseDel().getEjercicio())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.ejercicio = " +  aseDelSearchPage.getAseDel().getEjercicio().getId();
			flagAnd = true;
		}

		// filtro por Servicio Banco
 		if(!ModelUtil.isNullOrEmpty(aseDelSearchPage.getAseDel().getServicioBanco())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.servicioBanco = " +  aseDelSearchPage.getAseDel().getServicioBanco().getId();
			flagAnd = true;
		}

 		// filtro por EstadoCorrida
 		if(!ModelUtil.isNullOrEmpty(aseDelSearchPage.getAseDel().getCorrida().getEstadoCorrida())){
			queryString += flagAnd ? " and " : " where ";
			queryString += " t.corrida.estadoCorrida = " +  aseDelSearchPage.getAseDel().getCorrida().getEstadoCorrida().getId();
			flagAnd = true;
		}

 		// 	 filtro por Fecha Balance Desde
		if (aseDelSearchPage.getFechaBalanceDesde()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaBalance >= TO_DATE('" + 
					DateUtil.formatDate(aseDelSearchPage.getFechaBalanceDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// 	 filtro por Fecha Balance Hasta
		if (aseDelSearchPage.getFechaBalanceHasta()!=null) {
		  queryString += flagAnd ? " and " : " where ";	  
		  queryString += " (t.fechaBalance <= TO_DATE('" + 
					DateUtil.formatDate(aseDelSearchPage.getFechaBalanceHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')) ";
	      flagAnd = true;
		}

 		// Order By
		queryString += " order by t.fechaBalance DESC, t.id DESC";
		
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<AseDel> listAseDel = (ArrayList<AseDel>) executeCountedSearch(queryString, aseDelSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAseDel;
	}

	/**
	 *  Verifica si existe un Asentamiento Delegado para el Servicio Banco pasado con estado "En Preparacion", 
	 *  "En espera comenzar", "Procesando" o "En espera continuar". Retorna true o false.
	 *  (Excluyendo al AseDel que realiza la consulta)
	 * @param servicioBanco
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existAseDelBySerBanForCreate(ServicioBanco servicioBanco, AseDel ase) throws Exception{
		AseDel aseDel;
		String queryString = "from AseDel t where t.servicioBanco.id = "+servicioBanco.getId();
		
		queryString += " and (t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_PREPARACION;  
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_ESPERA_COMENZAR;
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_PROCESANDO;
		queryString += " or t.corrida.estadoCorrida.id = "+EstadoCorrida.ID_EN_ESPERA_CONTINUAR+" )";
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);
		query.setMaxResults(1);
		aseDel = (AseDel) query.uniqueResult();	
		
		if(aseDel != null && (ase.getId()==null || aseDel.getId().longValue() != ase.getId().longValue()))
			return true;
		else
			return false;		
	}

	/**
	 *  Obtener el Asentamiento Delegado asociado al balance pasado y para el Servicio Banco.
	 *   
	 * @param balance
	 * @param servicioBanco
	 * @return AseDel
	 * @throws Exception
	 */
	public AseDel getByBalanceYServicioBanco(Balance balance, ServicioBanco servicioBanco) throws Exception{
		AseDel aseDel;
		String queryString = "from AseDel t where t.servicioBanco.id = "+servicioBanco.getId();
		queryString += " and t.balance.id = "+balance.getId();  
		
		Session session = SiatHibernateUtil.currentSession();
		
		Query query = session.createQuery(queryString);

		aseDel = (AseDel) query.uniqueResult();	
		
		return aseDel;
	}
	
}
