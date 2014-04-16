//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.iface.model.NovedadRSSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.rec.buss.bean.NovedadRS;
import ar.gov.rosario.siat.rec.buss.bean.TipoTramiteRS;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class NovedadRSDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(NovedadRSDAO.class);	

	private static long migId = -1;
	
	public NovedadRSDAO() {
		super(NovedadRS.class);
	}


	public List<NovedadRS> getListTramitesByCuit(String cuit) throws Exception {
		boolean flagAnd=false;
		
		String queryString  = " FROM NovedadRS n";
  			   queryString += " WHERE ";
			   queryString += "  n.cuit = " + cuit;
		       queryString += " ORDER BY n.fechaTransaccion DESC";
		
		log.debug("QUERYSTRING: "+queryString);

		 Session session = SiatHibernateUtil.currentSession();
		 Query query = session.createQuery(queryString);
		 return  (ArrayList<NovedadRS>) query.list();
		
	}
	
	

	public NovedadRS getLastNovedadRS(String tipoCuenta, Cuenta cuenta, Date fechaCorte) throws Exception {
		String queryString  = " FROM NovedadRS n";
  			   queryString += " WHERE ";
  		
  		// setea la cuenta	   
  		if (tipoCuenta.equals("DREI"))
  			queryString += "  n.cuentaDRei.id = " + cuenta.getId();
  		else
  			queryString += "  n.cuentaEtur.id = " + cuenta.getId();
  		
  		// indica una fecha de corte
  		if (fechaCorte!=null)
  			queryString += "   and n.fechaTransaccion <= " + fechaCorte;
  				
       queryString += " ORDER BY n.fechaNovedad DESC";
		
		log.debug("QUERYSTRING: "+queryString);

		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		return  (NovedadRS) query.uniqueResult();
		
	}
	
	
	
	
	public List<NovedadRS> getListBySearchPage(NovedadRSSearchPage novedadRSSearchPage) throws Exception{
		
		Session session = SiatHibernateUtil.currentSession();
	
		boolean flagAnd=false;
		
		String queryString = "FROM NovedadRS n";
		
		if (!StringUtil.isNullOrEmpty(novedadRSSearchPage.getNovedadRS().getNroCuenta())){
			queryString += (flagAnd==true)?" AND ":" WHERE ";
			flagAnd=true;
			queryString += "n.nroCuenta = " + novedadRSSearchPage.getNovedadRS().getNroCuenta();
		}
		
		if (!ModelUtil.isNullOrEmpty(novedadRSSearchPage.getNovedadRS().getTipoTramiteRS())){
			queryString += (flagAnd==true)?" AND ":" WHERE ";
			flagAnd=true;
			queryString += "n.tipoTramiteRS.id = " + novedadRSSearchPage.getNovedadRS().getTipoTramiteRS().getId();
		}
		
		if (novedadRSSearchPage.getFechaNovedadDesde()!=null){
			queryString += (flagAnd==true)?" AND ":" WHERE ";
			flagAnd=true;
			 queryString += " n.fechaTransaccion >= TO_DATE('" +DateUtil.formatDate(novedadRSSearchPage.getFechaNovedadDesde(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";

		}
		
		if (novedadRSSearchPage.getFechaNovedadHasta()!=null){
			queryString += (flagAnd==true)?" AND ":" WHERE ";
			flagAnd=true;
			queryString += " n.fechaTransaccion <= TO_DATE('" +DateUtil.formatDate(novedadRSSearchPage.getFechaNovedadHasta(), DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') ";

		}
		
		if (novedadRSSearchPage.getNovedadRS().getEstado().getId() != null && novedadRSSearchPage.getNovedadRS().getEstado().getId().intValue() != 0){
			queryString += (flagAnd==true)?" AND ":" WHERE ";
			flagAnd=true;
			queryString += "n.estado = " + novedadRSSearchPage.getNovedadRS().getEstado().getId();
		}
		
		if (novedadRSSearchPage.getFiltroConObservacion()){
			queryString += (flagAnd==true)?" AND ":" WHERE ";
			flagAnd=true;
			queryString += "n.conObservacion = " + SiNo.SI.getId();
		}
		
		queryString += " ORDER BY n.id";
		
		log.debug("QUERYSTRING: "+queryString);
		
		List<NovedadRS>listNovedad = (ArrayList<NovedadRS>) executeCountedSearch(queryString, novedadRSSearchPage);
		
		
		return listNovedad;
	}
	
	
	/**
	 * Obtiene la lista de Novedades de Regimen Simplificado en estado Registrado (sin procesar) y unicamente para tipo de tramite "ADHESION"
	 * 
	 * @return
	 */
	public List<NovedadRS> getListRegistrado(){
		
		String queryStr = " FROM NovedadRS t WHERE t.estado = "+NovedadRS.REGISTRADO;			
		   	   queryStr += " AND t.tipoTramiteRS.id = "+TipoTramiteRS.ID_TRAMITE_ALTA;	   
		   	   queryStr += " ORDER BY t.id";
			   
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query = session.createQuery(queryStr);			
		
		return (ArrayList<NovedadRS>) query.list();
	}

	/**
	 * Devuelve true si existe al menos una Novedad de regimen simplificado en estado Registrado (sin procesar) y tipo de tramite "ADHESION"
	 * 
	 * @return
	 */
	public boolean existenNovedadRSSinProcesar(){
		
		String queryStr = " FROM NovedadRS t WHERE t.estado = "+NovedadRS.REGISTRADO;			
			   queryStr += " AND t.tipoTramiteRS.id = "+TipoTramiteRS.ID_TRAMITE_ALTA;
			   
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query = session.createQuery(queryStr);			
		query.setMaxResults(1);
		NovedadRS novedadRS = (NovedadRS) query.uniqueResult();
		if(novedadRS != null)
			return true;
		
		return false;
	}
	
	
	/**
	 * Obtiene la lista de Novedades de Regimen Simplificado en estado Registrado (sin procesar) y utilizando los filtros pasados como paramtetros.
	 * 
	 * @return
	 */
	public List<NovedadRS> getListRegistradoByFiltros(TipoTramiteRS tipoTramiteRS, Date fechaNovedadDesde, Date fechaNovedadHasta){
		
		String queryStr = " FROM NovedadRS t WHERE t.estado = "+NovedadRS.REGISTRADO;			
		  	if(tipoTramiteRS != null)
		  		queryStr += " AND t.tipoTramiteRS.id = "+tipoTramiteRS.getId();	   
		   	if(fechaNovedadDesde != null)
		   		queryStr += " AND t.fechaTransaccion >= :fechaDesde ";
		   	if(fechaNovedadHasta != null)
		   		queryStr += " AND t.fechaTransaccion <= :fechaHasta ";
		  	
		  	queryStr += " ORDER BY t.id";
			   
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query = session.createQuery(queryStr);
		
		if(fechaNovedadDesde != null)
			query.setDate("fechaDesde", fechaNovedadDesde);
		if(fechaNovedadHasta != null)
			query.setDate("fechaHasta", fechaNovedadHasta);
		
		return (ArrayList<NovedadRS>) query.list();
	}
	
	
}
