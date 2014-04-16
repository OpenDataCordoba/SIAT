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
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipRecConADec;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class RecConADecDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecConADecDAO.class);	
	
	public RecConADecDAO() {
		super(RecConADec.class);
	}

	/**
	 * Obtiene lista de Conceptos a Declarar para el Recurso especificado
	 * @author Tecso
	 * @param Long idRecurso	
	 * @return List<RecConADec> 
	 */
	public List<RecConADec> getListByIdRecurso(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecConADec r ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + id); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where r.recurso.id = " + id;

        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecConADec> listRecConADec = (ArrayList<RecConADec>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecConADec;
	}
	
	/**
	 * Obtiene lista de Conceptos vigentes a Declarar para el Recurso especificado a la fecha pasada como parametro
	 * @author Tecso
	 * @param Long idRecurso, Date fecha
	 * @return List<RecConADec> 
	 */
	public List<RecConADec> getListVigenteByIdRecurso(Long id, Date fecha, Long idTipo) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecConADec r ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + id); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where r.recurso.id = " + id;
        
        queryString += " AND r.tipRecConADec.id = "+ idTipo;

        queryString += " AND r.fechaDesde <= :fecha";
        
        queryString += " AND (r.fechaHasta is null OR r.fechaHasta < :fecha )";
        
        queryString += " ORDER BY r.desConcepto";
        
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString).setDate("fecha", fecha);
	    List<RecConADec> listRecConADec = (ArrayList<RecConADec>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecConADec;
	}
	
	public RecConADec getByCodConceptoRecurso(Long idRecurso, String strConcepto) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM RecConADec r ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + idRecurso + "  codConcepto: "+ strConcepto); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " WHERE r.recurso.id = " + idRecurso;
       
        queryString += " AND r.codConcepto = :strCodigo";

		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString).setString("strCodigo", strConcepto);
	    RecConADec recConADec = (RecConADec) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return recConADec;
	}
	

	public List<RecConADec> getByListCodConceptoRecurso(Long idRecurso, String strConcepto) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		// agrega cuotes adelante y atras
		// reemplaza las comas por ','
		strConcepto = strConcepto.replace(",", "','");
		strConcepto = "'" + strConcepto + "'";
		
		String queryString = "from RecConADec r ";
		log.debug("log de filtros: idRecurso: " + idRecurso + "  codConcepto: "+ strConcepto); 
	
		// Armamos filtros del HQL
        queryString += " where r.recurso.id = " + idRecurso;
        queryString += " and r.codConcepto in ("+ strConcepto + ")";

		
	    log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecConADec> listRecConADec = (ArrayList<RecConADec>) query.list();
		
	    if (listRecConADec==null)
	    	log.debug(funcName + " listRecConADec==null ");
	    else
	    	log.debug(funcName + " listRecConADec.size()= " + listRecConADec.size());
	    
		return listRecConADec;
	}

	
	public boolean getEsCodDuplicado(Long idRecurso, String strConcepto) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecConADec r ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + idRecurso + "  codConcepto: "+ strConcepto); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where r.recurso.id = " + idRecurso;
       
        queryString += " and r.codConcepto = :strCodigo";

		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString).setString("strCodigo", strConcepto);
	    List<RecConADec> listRecConADec = (List<RecConADec>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecConADec.size()>1;
	}
	
	
	public List<RecConADec> getListTipoUnidadVigenteByRecurso(Recurso recurso, Date fecha){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM RecConADec r WHERE r.recurso.id = "+recurso.getId();
		
		queryString += " AND r.tipRecConADec.id = "+TipRecConADec.ID_TIPO_UNIDAD;
		
		queryString += " AND r.fechaDesde <= :fecha";
		
		queryString += " AND (r.fechaHasta IS NULL OR r.fechaHasta <= :fecha )";
		
		Query query = session.createQuery(queryString).setDate("fecha", fecha);
		
		List <RecConADec> listRecConADec = (List<RecConADec>)query.list();
		
		return listRecConADec;
	}
	
	
	/**
	 * Dado un recurso, una fecha y una Unidad (idRecTipUni),
	 * devuele los tipos de unidad (RecConADec) correspondientes.
	 * 
	 * @param recurso
	 * @param fecha
	 * @param idRecTipUni
	 * @return
	 */
	public List<RecConADec> getListTipoUnidadVigenteByRecursoParamUnidad(Recurso recurso, Date fecha, Long idRecTipUni){
		
		Session session = SiatHibernateUtil.currentSession();
		
		Long idTipoUnidad=null;
		
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
			idTipoUnidad = TipRecConADec.ID_TIPO_UNIDAD;
		}
		
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DPUB)){
			idTipoUnidad = TipRecConADec.ID_TIPO_UNIDAD_DPUB;
		}
		
		String queryString = "FROM RecConADec r WHERE r.recurso.id = "+recurso.getId();
		
		
		queryString += " AND r.recTipUni.id = "+idRecTipUni;
		
		queryString += " AND r.tipRecConADec.id = "+ idTipoUnidad;
		
		queryString += " AND r.fechaDesde <= :fecha";
		
		queryString += " AND (r.fechaHasta IS NULL OR r.fechaHasta <= :fecha )";
		
		Query query = session.createQuery(queryString).setDate("fecha", fecha);
		
		List <RecConADec> listRecConADec = (List<RecConADec>)query.list();
		
		return listRecConADec;
	}

	
	/**
	 * Dado un recurso y una fecha, devuelve la lista de tipo de unidad, ordenado por Unidad (recTipUni)
	 * 
	 * 
	 * @param recurso
	 * @param fecha
	 * @return
	 */
	public List<RecConADec> getListTipoUnidadVigenteByRecurso4Map(Recurso recurso, Date fecha){
		
		Session session = SiatHibernateUtil.currentSession();
		
		Long idTipoUnidad=null;
		
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DReI)){
			idTipoUnidad = TipRecConADec.ID_TIPO_UNIDAD;
		}
		
		if (recurso.getCodRecurso().equals(Recurso.COD_RECURSO_DPUB)){
			idTipoUnidad = TipRecConADec.ID_TIPO_UNIDAD_DPUB;
		}
		
		String queryString = "FROM RecConADec r WHERE r.recurso.id = "+recurso.getId();
		
		queryString += " AND r.tipRecConADec.id = "+ idTipoUnidad;
		
		queryString += " AND r.fechaDesde <= :fecha";
		
		queryString += " AND (r.fechaHasta IS NULL OR r.fechaHasta <= :fecha )";
		
		queryString += " ORDER BY r.recTipUni.id ";
		
		Query query = session.createQuery(queryString).setDate("fecha", fecha);
		
		List <RecConADec> listRecConADec = (List<RecConADec>)query.list();
		
		return listRecConADec;
	}
	
	/** 
	 *  Devuelve el registro de Tipo de Unidad  segun el codigo de sincronismo afip indicado.
	 * 
	 * @param codigoAfip
	 * @return
	 */
	public RecConADec getByCodigoAfip(String codigoAfip){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString= "FROM RecConADec r WHERE r.codigoAfip = '"+codigoAfip+"'";
		
		Query query = session.createQuery(queryString);
		
		query.setMaxResults(1);
		
		RecConADec recConADec = (RecConADec) query.uniqueResult();
		
		return recConADec;
	}
}
