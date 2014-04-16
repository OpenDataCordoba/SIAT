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
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class TipObjImpAtrDAO extends GenericDAO {

	private Log log = LogFactory.getLog(TipObjImpAtrDAO.class);	
	
	public TipObjImpAtrDAO() {
		super(TipObjImpAtr.class);
	}
	
	public List<TipObjImpAtr> getListActivosByIdTipObjImp(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from TipObjImpAtr t ";
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idTipObjImp: " + id); 
		}
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.tipObjImp.id = " + id;
        
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString);
		List<TipObjImpAtr> listTipObjImpAtr = (ArrayList<TipObjImpAtr>) query.list();
			
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipObjImpAtr;
	}
	
	/**
	 * Devuelve la lista de definicion para: 
	 *  - Un id de TipObjImpAtr 
	 *  - Un param que indica si la consulta es para busqueda, manual o interface
	 *  - Y una fecha que se utiliza para filtrar los TipObjImpAtr vigentes hasta esa fecha dada. 
	 * 	  si es nula se consulta hasta la fecha actual  
	 * 
	 * @param param
	 * @param id
	 * @param fecha
	 * @return
	 */
	public List<TipObjImpAtr> getListVigentesByIdTipObjImp(Long param, Long id, Date fecha) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from TipObjImpAtr t ";
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idTipObjImp: " + id); 
		}
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.tipObjImp.id = " + id;
        
		//1-FOR_BUSQUEDA  		
		if(param==1){
			queryString += " and (t.esAtributoBus = 1 or t.esClaveFuncional = 1 or t.esClave = 1)";
		
		}
		//2-FOR_INTERFACE
		if(param==2)
			queryString += " and t.esAtributoSIAT = 0 ";
		
		//3-FOR_MANUAL: No hay filtros, se traen todos los atributos
		if(param==3)
			queryString += " and (t.valDefEsRef is null or t.valDefEsRef=0)";
		
		//4-FOR_WEB 
		if (param==4)
			queryString += " and t.esVisConDeu = 1 ";
		
		//5-FOR_BUSQUEDA_MASIVA  		
		if(param == TipObjImp.FOR_BUS_MASIVA){
			queryString += " and (t.esAtriBusMasiva = 1 )";
		}
			
		if (fecha == null) { 
			fecha = new Date();
		}
		
		queryString += " and (t.fechaDesde <= TO_DATE('" 
			+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		
		queryString += " and (t.fechaHasta is null OR t.fechaHasta  >= TO_DATE('" 
			+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
				
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString);
		List<TipObjImpAtr> listTipObjImpAtr = (ArrayList<TipObjImpAtr>) query.list();
			
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipObjImpAtr;
	}
	
	//estado = ACTIVO, tipObjImp.id=Comercio, esSiat=1,valDefEsRef=1)
	public List<TipObjImpAtr> getList(Long idTipObjImp, boolean esSiat, boolean esReferenciado, Estado estado){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		boolean flagAnd=false;
		
		String queryString = "from TipObjImpAtr t ";
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idTipObjImp: " + idTipObjImp+"   esSiat:"+esSiat+"     esReferenciado:"+
					esReferenciado+"    estado:"+estado); 
		}
		    
	    // Armamos filtros del HQL
	    if(idTipObjImp!=null && idTipObjImp.longValue()>0){
	    	queryString += flagAnd?" AND ":" WHERE ";
	    	queryString += " t.tipObjImp.id = " + idTipObjImp;
	    	flagAnd=true;
	    }
	    
	    if(esSiat){
	    	queryString += flagAnd?" AND ":" WHERE ";
	    	queryString += " t.esAtributoSIAT = 1 ";
	    	flagAnd=true;
	    }
		
	    if(esReferenciado){
	    	queryString += flagAnd?" AND ":" WHERE ";
	    	queryString += " t.valDefEsRef= 1";
	    	flagAnd=true;
	    }
		
	    if(estado!=null){
	    	queryString += flagAnd?" AND ":" WHERE ";	    	
	    	queryString += " t.estado = "+ estado.getId();
	    	flagAnd=true;
	    }
 		
		Query query = session.createQuery(queryString);
		List<TipObjImpAtr> listTipObjImpAtr = (ArrayList<TipObjImpAtr>) query.list();

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipObjImpAtr;
	}
	
	/** 
	 * Obtiene lista de Atributos asignados al TipObjImp especificado, excluyendo los que esten en la lista de 
	 * excluidos. 
	 * @author Tecso
	 * @param Long idTipObjImp, List<Atributo> listAtributosExcluidos	
	 * @return List<Atributo> 
	 */
	public List<Atributo> getListAtributoByIdTipObjImpForRecurso(Long id, List<AtributoVO> listAtributosExcluidos) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select atributo from TipObjImpAtr t ";
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idTipObjImp: " + id); 
		}
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.tipObjImp.id = " + id;
        
		// filtro atributos excluidos
 		if (!ListUtil.isNullOrEmpty(listAtributosExcluidos)) {
 			String listIdExcluidos = ListUtil.getStringIdsFromListModel(listAtributosExcluidos);
			queryString += " and t.atributo.id NOT IN ("+ listIdExcluidos + ") "; 
		}

		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString);
		List<Atributo> listAtributo = (ArrayList<Atributo>) query.list();
			
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAtributo;
	}
	
	/**
	 * Devuelve el Atributo asociado al Tipo de Objeto Imponible que es clave. 
	 * (Si no encuentra ninguno devuelve null)
	 * Si se pasa el parametro idExcluido, se excluye el atributo correspondiente en la busqueda.
	 * 
	 * @param tipObjImp
	 * @param idExluido
	 * @return tipObjImpAtr
	 */
	public TipObjImpAtr getTipObjImpAtrClave(TipObjImp tipObjImp, Long idExcluido){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from TipObjImpAtr t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.tipObjImp.id = " + tipObjImp.getId();
		queryString += " and t.esClave = " + SiNo.SI.getId();		
				
 		if(idExcluido!=null){
 			queryString += " and t.id <> "+idExcluido; 
 		}
			
		Query query = session.createQuery(queryString);
		
		//query.setMaxResults(1);
		List<TipObjImpAtr> listTipObjImpAtr = (ArrayList<TipObjImpAtr>) query.list();

		if(!listTipObjImpAtr.isEmpty())
			return listTipObjImpAtr.get(0);
		else
			return null;
	}

	/**
	 * Devuelve el Atributo asociado al Tipo de Objeto Imponible que es clave funcional. 
	 * (Si no encuentra ninguno devuelve null)
	 * Si se pasa el parametro idExcluido, se excluye el atributo correspondiente en la busqueda.
	 * 
	 * @param tipObjImp
	 * @param idExluido
	 * @return tipObjImpAtr
	 */
	public TipObjImpAtr getTipObjImpAtrClaveFuncional(TipObjImp tipObjImp, Long idExcluido){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from TipObjImpAtr t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.tipObjImp.id = " + tipObjImp.getId();
		queryString += " and t.esClaveFuncional = " + SiNo.SI.getId();		
	    
		if(idExcluido!=null){
 			queryString += " and t.id <> "+idExcluido; 
 		}
		
		Query query = session.createQuery(queryString);
		//query.setMaxResults(1);
		List<TipObjImpAtr> listTipObjImpAtr = (ArrayList<TipObjImpAtr>) query.list();
					
		if(!listTipObjImpAtr.isEmpty())
			return listTipObjImpAtr.get(0);
		else
			return null;
	}

	
	/**
	 * Obtiene el TipObjImpAtr para el Id de Atributo pasado como parametro.
	 * 
	 * @param idAtributo
	 * @return
	 */
	public TipObjImpAtr getByIdAtributo(Long idAtributo){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from TipObjImpAtr t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.atributo.id = " + idAtributo;
									
		Query query = session.createQuery(queryString);
		
		TipObjImpAtr tipObjImpAtr = (TipObjImpAtr) query.uniqueResult();

		return tipObjImpAtr;
	}
	
	
	/**
	 * Obtiene el TipObjImpAtr para el Id TipObjImp y Id de Atributo pasados como parametro.
	 * 
	 * @param idAtributo
	 * @return
	 */
	public TipObjImpAtr getByIdTipObjImpyIdAtributo(Long idTipObjImp, Long idAtributo){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from TipObjImpAtr t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.tipObjImp.id = " + idTipObjImp;
		queryString += " and t.atributo.id = " + idAtributo;
									
		Query query = session.createQuery(queryString);
		
		TipObjImpAtr tipObjImpAtr = (TipObjImpAtr) query.uniqueResult();

		return tipObjImpAtr;
	}
}
