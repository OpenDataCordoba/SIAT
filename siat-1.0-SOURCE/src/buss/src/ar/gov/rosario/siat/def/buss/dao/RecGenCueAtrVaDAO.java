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
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.RecGenCueAtrVa;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;

public class RecGenCueAtrVaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(RecGenCueAtrVaDAO.class);	
	
	public RecGenCueAtrVaDAO() {
		super(RecGenCueAtrVa.class);
	}

	/**
	 * Obtiene lista de Atributos a Valorizar para Habilitar la Creación de Cuentas para el Recurso especificado
	 * @author Tecso
	 * @param Long idRecurso	
	 * @return List<RecGenCueAtrVa> 
	 */
	public List<RecGenCueAtrVa> getListByIdRecurso(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecGenCueAtrVa t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + id); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where t.recurso.id = " + id;

 		// Order By
        queryString += " order by t.atributo ";
        
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecGenCueAtrVa> listRecGenCueAtrVa = (ArrayList<RecGenCueAtrVa>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecGenCueAtrVa;
	}
	
	public List<RecGenCueAtrVa> getListByIdRecAtr(Long idRecurso, Long idAtributo) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecGenCueAtrVa t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + idRecurso + "  idAtributo: "+ idAtributo); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where t.recurso.id = " + idRecurso;
        
        queryString += " and t.atributo.id = " + idAtributo;

 		// Order By
        queryString += " order by t.fechaDesde desc ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecGenCueAtrVa> listRecGenCueAtrVa = (ArrayList<RecGenCueAtrVa>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecGenCueAtrVa;
	}

	
	public List<RecGenCueAtrVa> getListByIdRecAtrValor(Long idRecurso, Long idAtributo, String valor) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecGenCueAtrVa t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + idRecurso + "  idAtributo: "+ idAtributo); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where t.recurso.id = " + idRecurso;
        
        queryString += " and t.atributo.id = " + idAtributo;
        
        queryString += " and t.strValor = " + valor;
        
 		// Order By
        queryString += " order by t.fechaDesde desc ";
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecGenCueAtrVa> listRecGenCueAtrVa = (ArrayList<RecGenCueAtrVa>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecGenCueAtrVa;
	}
	
	/**
	 * Obtiene el RecGenCueAtrVa para el atributo y recurso especificado que tiene Fecha Hasta null.
	 * @author Tecso
	 * @param Long idAtributo, Long idRecurso	
	 * @return RecGenCueAtrVa 
	 */
	public RecGenCueAtrVa getAbiertoByIdRecGenCueAtrVa(Long idAtributo, Long idRecurso) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecGenCueAtrVa t ";
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros: atributo.id: " + idAtributo); 
		}
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
		queryString += " and t.recurso.id = " + idRecurso;
		
		queryString += " and t.atributo.id = " + idAtributo;
        
		queryString += " and t.fechaHasta is null";
				
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
		Query query = session.createQuery(queryString);
		
		RecGenCueAtrVa recGenCueAtrVa = (RecGenCueAtrVa) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return recGenCueAtrVa;
	}

	/** 
	 * Obtiene lista de Atributos a Valorizar para Habilitar la Creación de Cuentas para el Recurso especificado que se
	 * pueden asignar segun el TipObjImp del Recurso.
	 * @author Tecso
	 * @param Long idRecurso	
	 * @return List<Atributo> 
	 */
	public List<Atributo> getListAtributoByIdRecurso(Long id) {			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select DISTINCT t.atributo from RecGenCueAtrVa t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: idRecurso: " + id); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where t.recurso.id = " + id;

 		// Order By
		
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Atributo> listAtributo = (ArrayList<Atributo>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAtributo;
	}


	public List<RecGenCueAtrVa> getListByStrValor(String strValor) {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from RecGenCueAtrVa t ";
	    
		if (log.isDebugEnabled()) { 
			log.debug("log de filtros: strValor: " + strValor); 
		}
	
		// Armamos filtros del HQL
		
        queryString += " where t.strValor = " + strValor;

	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<RecGenCueAtrVa> listRecGenCueAtrVa = (ArrayList<RecGenCueAtrVa>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listRecGenCueAtrVa;
	}
}
