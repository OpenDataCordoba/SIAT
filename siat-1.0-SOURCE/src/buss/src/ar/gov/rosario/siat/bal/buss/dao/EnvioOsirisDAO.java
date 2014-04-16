//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.bal.buss.bean.EstadoEnvio;
import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisSearchPage;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;

public class EnvioOsirisDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(EnvioOsirisDAO.class);	
	
	public EnvioOsirisDAO() {
		super(EnvioOsiris.class);
	}

	
	public EnvioOsiris getByIdEnvioAfip(Long idEnvioAfip){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM EnvioOsiris e WHERE e.idEnvioAfip = "+idEnvioAfip;
		
		Query query=session.createQuery(queryString);
		
		return (EnvioOsiris)query.uniqueResult();
	}
	
	
	public List<EnvioOsiris>getListInconsistentes(){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM EnvioOsiris e WHERE e.estadoEnvio.id = "+EstadoEnvio.ID_ESTADO_INCONSISTENTE;
		
		Query query = session.createQuery(queryString);
		
		
		return (ArrayList<EnvioOsiris>) query.list();
	}
	
	public List<EnvioOsiris> getListBySearchPage(EnvioOsirisSearchPage envioSearchPage) throws Exception{
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM EnvioOsiris e";
		
		boolean flagAnd = false;
		
		if (!ModelUtil.isNullOrEmpty(envioSearchPage.getEnvioOsiris().getEstadoEnvio())){
			queryString += (flagAnd)?" and ":" where ";
			queryString += "e.estadoEnvio.id = "+ envioSearchPage.getEnvioOsiris().getEstadoEnvio().getId();
			flagAnd=true;
		}
		
		if (envioSearchPage.getEnvioOsiris().getIdEnvioAfip()!= null){
			queryString += (flagAnd)?" and ":" where ";
			queryString += " e.idEnvioAfip = "+envioSearchPage.getEnvioOsiris().getIdEnvioAfip();
			flagAnd=true;
		}
		
		if (envioSearchPage.getFechaDesde() != null){
			queryString += (flagAnd)?" and ":" where ";
			queryString += " e.fechaRegistroMulat >= TO_DATE('" + DateUtil.formatDate(envioSearchPage.getFechaDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";
			flagAnd=true;
		}
		
		if (envioSearchPage.getFechaHasta() != null){
			queryString += (flagAnd)?" and ":" where ";
			queryString += " e.fechaRegistroMulat <= TO_DATE('" + DateUtil.formatDate(envioSearchPage.getFechaHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";
			flagAnd=true;
		}
		
		queryString += " order by e.idEnvioAfip desc";
		
		
		return (ArrayList<EnvioOsiris>) executeCountedSearch(queryString, envioSearchPage);
	}
	
	
	/**
	 *  Devuelve la ultima fecha de registro de mulator encontrada para los registros de envios de afip cargados en la tabla
	 *  (por el momento ignora el estado del envio, considerando que la fecha de registro en mulator depende del momento en que se cargan los datos en la base)
	 * 
	 * @return
	 */
	public Date getUltimaFechaRegistroMulat(){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM EnvioOsiris e order by fechaRegistroMulat desc";
		
		Query query=session.createQuery(queryString);
		query.setMaxResults(1);
		
		EnvioOsiris envioOsiris = (EnvioOsiris) query.uniqueResult();
		
		if(envioOsiris != null)
			return envioOsiris.getFechaRegistroMulat(); 
		
		return null; 
	}
	
	public List<EnvioOsiris> getListPendientes(){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM EnvioOsiris e WHERE e.estadoEnvio.id = "+EstadoEnvio.ID_ESTADO_PENDIENTE;
		
		Query query = session.createQuery(queryString);
		
		
		return (ArrayList<EnvioOsiris>) query.list();
	}
	
	public List<EnvioOsiris> getListConciliado(){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="FROM EnvioOsiris e WHERE e.estadoEnvio.id = "+EstadoEnvio.ID_ESTADO_CONCILIADO;
		
		Query query = session.createQuery(queryString);
		
		return (ArrayList<EnvioOsiris>) query.list();
	}
	
	/**
	 * Devuelve true si existe al menos un Envio Osiris en estado 'Conciliado'.
	 * 
	 * @return
	 */
	public boolean existenEnvioOsirisConciliados(){
		
		String queryStr = " FROM EnvioOsiris t WHERE t.estadoEnvio.id = "+EstadoEnvio.ID_ESTADO_CONCILIADO;			
			   
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query = session.createQuery(queryStr);			
		query.setMaxResults(1);
		EnvioOsiris envioOsiris = (EnvioOsiris) query.uniqueResult();
		if(envioOsiris != null)
			return true;
		
		return false;
	}
	
}
