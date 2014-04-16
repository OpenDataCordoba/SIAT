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

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxConvenio;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class AuxConvenioDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(AuxConvenioDAO.class);
	
	public AuxConvenioDAO(){
		super(AuxConvenio.class);
	}

	/**
	 * Obtiene la lista de AuxConvenio para el Asentamiento pasado como parametro.
	 * 
	 * @param asentamiento
	 * @return
	 * @throws Exception
	 */
	public List<AuxConvenio> getListByAsentamiento(Asentamiento asentamiento) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxConvenio t "; 		
		queryString += " where t.asentamiento.id = " +asentamiento.getId();
		
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<AuxConvenio> listAuxConvenio = (ArrayList<AuxConvenio>) query.list();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAuxConvenio; 
	}
	
	/**
	 * 	Obtiene le AuxConvenio para el Asentamiento y Convenio pasados. Si no lo encuentra devuelve null.
	 * 
	 * @param asentamiento
	 * @param convenio
	 * @return auxConvenio
	 * @throws Exception
	 */
	public AuxConvenio getByAsentamientoYConvenio(Asentamiento asentamiento, Convenio convenio, TipoSolicitud tipoSolicitud) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxConvenio t "; 		
		queryString += " where t.asentamiento.id = " +asentamiento.getId();
		queryString += " and t.convenio.id = " +convenio.getId();
		queryString += " and t.tipoSolicitud.id = " +tipoSolicitud.getId();
		
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    AuxConvenio auxConvenio = (AuxConvenio) query.uniqueResult();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return auxConvenio; 
	}
	
	/**
	 * Elimina los registros de AuxConvenio que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from AuxConvenio t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
}
