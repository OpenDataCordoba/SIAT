//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.DecJur;
import ar.gov.rosario.siat.gde.buss.bean.Deuda;
import ar.gov.rosario.siat.gde.iface.model.DecJurSearchPage;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.model.Estado;



public class DecJurDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DecJurDAO.class);	
	
	public DecJurDAO() {
		super(DecJur.class);
	}
	
	public List<DecJur> getListBySearchPage(DecJurSearchPage decJurSearchPage) throws Exception{
		//Session session = SiatHibernateUtil.currentSession();
		
		List<DecJur>listDecJur;
		
		String queryString = "FROM DecJur d ";
		
		if (decJurSearchPage.getDecJur().getIdDeuda() != null){
			queryString += "WHERE d.idDeuda = " + decJurSearchPage.getDecJur().getIdDeuda();
			
		} else {
		
			queryString += "WHERE d.cuenta.id = "+ decJurSearchPage.getDecJur().getCuenta().getId();
			
			if (decJurSearchPage.getPeriodoDesde()!=null){
				//queryString += " AND TO_DATE(('01/' || d.periodo || '/'  || d.anio), '%d/%m/%Y') >= :fechaDesde";
				queryString += " AND d.periodo >= " + decJurSearchPage.getPeriodoDesde(); 
				queryString += " AND d.anio >= " + + decJurSearchPage.getAnioDesde();
			}
			
			if (decJurSearchPage.getPeriodoHasta()!=null){
				//queryString += " AND TO_DATE(('01/' || d.periodo || '/'  || d.anio), '%d/%m/%Y') <= :fechaHasta";
				queryString += " AND d.periodo <= " + decJurSearchPage.getPeriodoHasta();
				queryString += " AND d.anio <= " + decJurSearchPage.getAnioHasta() ;
			}
			
			//Query query = session.createQuery(queryString);
			
			
					
			log.debug("periodoDesde: " + decJurSearchPage.getPeriodoDesde());
			log.debug("anioDesde: " + decJurSearchPage.getAnioDesde());
			log.debug("periodoHasta: " + decJurSearchPage.getPeriodoHasta());
			log.debug("anioHasta: " + decJurSearchPage.getAnioHasta());
			
			/*if (decJurSearchPage.getPeriodoDesde()!=null){
				//Date fechaDesde = DateUtil.getDate(decJurSearchPage.getAnioDesde(), decJurSearchPage.getPeriodoDesde(), 1);
				//query.setDate("fechaDesde", fechaDesde);
				
				query.setInteger("periodoDesde", decJurSearchPage.getPeriodoDesde());
				query.setInteger("anioDesde", decJurSearchPage.getAnioDesde());
			}
			
			if (decJurSearchPage.getPeriodoHasta()!=null){
				//Date fechaHasta = DateUtil.getDate(decJurSearchPage.getAnioHasta(), decJurSearchPage.getPeriodoHasta(), 1);
				//query.setDate("fechaHasta", fechaHasta);
				query.setInteger("periodoHasta", decJurSearchPage.getPeriodoHasta());
				query.setInteger("anioHasta", decJurSearchPage.getAnioHasta());
			}*/
		}	
		
		log.debug("queryString :" + queryString);
		
		listDecJur = (List<DecJur>)executeCountedSearch(queryString, decJurSearchPage);
		
		return listDecJur;
	}
	
	public List<DecJur>getListByDeuda(Deuda deuda){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM DecJur d WHERE d.idDeuda = "+deuda.getId();
		
		Query query = session.createQuery(queryString);
		
		List<DecJur> listDecJur = (List<DecJur>)query.list();
		
		return listDecJur;
	}
	
	public DecJur getLastDecJurByCuenta (Cuenta cuenta){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT max(id) FROM DecJur d WHERE d.cuenta.id = "+cuenta.getId();
			   queryString += " AND d.estado = 1";
		
		Query query = session.createQuery(queryString);
		
		Long idDecJur = (Long)query.uniqueResult();
		
		DecJur decJur = DecJur.getByIdNull(idDecJur);
		
		return decJur;
	}
	
	public List<DecJur>getListByDeudaDecJurExcluir(Deuda deuda, DecJur decJur){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM DecJur d WHERE d.idDeuda = "+deuda.getId();
			   queryString += " AND d.id <> "+decJur.getId();
			   queryString += " AND d.estado = "+Estado.ACTIVO.getId();
		
		Query query = session.createQuery(queryString);
		
		List<DecJur> listDecJur = (List<DecJur>)query.list();
		
		return listDecJur;
	}
	
	public DecJur getLastByCuentaPeriodoYAnio(Cuenta cuenta, Integer periodo, Integer anio){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "SELECT max(id) FROM DecJur d WHERE d.cuenta.id = "+cuenta.getId();
			   queryString += " AND d.estado = 1";
			   queryString += " AND d.periodo = "+periodo;
			   queryString += " AND d.anio = "+anio;
		
		Query query = session.createQuery(queryString);
		
		Long idDecJur = (Long)query.uniqueResult();
		
		DecJur decJur = DecJur.getByIdNull(idDecJur);
		
		return decJur;
	}
	
	public List<DecJur> getByDeudaFechaNovedad(Long idDeuda, Date fecha){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM DecJur d WHERE d.idDeuda = "+idDeuda;
			   queryString += " AND d.fechaNovedad >= :fecha";
			   queryString += " AND d.estado = "+Estado.ACTIVO.getId();
		
		Query query = session.createQuery(queryString).setDate("fecha",fecha);
		
		List<DecJur> listDecJur = (List<DecJur>)query.list();
		
		return listDecJur;
	}
	
	/**
	 * Retorna lista de declaracion juradas correspondientes a una cuenta, para un anio y periodo.
	 * @param cuenta
	 * @param periodo
	 * @param anio
	 * @return
	 */
	public List<DecJur> getListByCuentaPeriodoYAnio(Cuenta cuenta, Integer periodo, Integer anio){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM DecJur d WHERE d.cuenta.id = "+cuenta.getId();
			   queryString += " AND d.periodo = "+periodo;
			   queryString += " AND d.anio = "+anio;
			   queryString += " AND d.estado = "+ Estado.ACTIVO.getId();
		
		Query query = session.createQuery(queryString);
		
		return query.list();
	}
	
}
