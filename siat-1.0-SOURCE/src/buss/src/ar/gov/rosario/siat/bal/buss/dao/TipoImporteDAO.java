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

import ar.gov.rosario.siat.bal.buss.bean.TipoImporte;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;

public class TipoImporteDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(TipoImporteDAO.class);
	
	public TipoImporteDAO(){
		super(TipoImporte.class);
	}
	
	/**
	 * Obtiene la lista de Tipos de Importe que no abren por concepto.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TipoImporte> getListActivosNoAbreConcepto() throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from TipoImporte t ";
	    
		// Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
 		
        queryString += " and t.abreConceptos = "+ SiNo.NO.getId();
        
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<TipoImporte> listTipoImporte = (ArrayList<TipoImporte>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTipoImporte; 
	}
}
