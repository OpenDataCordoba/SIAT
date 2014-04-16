//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.ef.buss.bean.CompFuente;
import ar.gov.rosario.siat.ef.buss.bean.PlaFueDatCom;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class PlaFueDatComDAO extends GenericDAO {

	private Log log = LogFactory.getLog(PlaFueDatComDAO.class);
	
	public PlaFueDatComDAO() {
		super(PlaFueDatCom.class);
	}
	
	/**
	 * Obtiene un PlaFueDatCom por su codigo
	 */
	public PlaFueDatCom getByCodigo(String codigo) throws Exception {
		PlaFueDatCom plaFueDatCom;
		String queryString = "from PlaFueDatCom t where t.codPlaFueDatCom = :codigo";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setString("codigo", codigo);
		plaFueDatCom = (PlaFueDatCom) query.uniqueResult();	

		return plaFueDatCom; 
	}


	public PlaFueDatCom getByPeriodoAnio(CompFuente compFuente,	Integer periodo, Integer anio) {
		PlaFueDatCom plaFueDatCom;
		String queryString = "from PlaFueDatCom t where t.periodo = "+periodo + " and t.anio="+anio+
						" and t.compFuente = :compFuente";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("compFuente", compFuente);
		plaFueDatCom = (PlaFueDatCom) query.uniqueResult();	

		return plaFueDatCom;

	}
	
	public void deleteListByCompFuente(CompFuente compFuente){
		Session session = SiatHibernateUtil.currentSession();
		String queryString = "DELETE PlaFueDatCom p where p.compFuente.id = "+compFuente.getId();
		
		Query query= session.createQuery(queryString);
		
		query.executeUpdate();
	}
	
	public List<Object[]> getListPeriodosDePlaFueDatCom (CompFuente compFuenteMin, CompFuente compFuenteSus){
		Session session = SiatHibernateUtil.currentSession();
		
		Date fechaDesdeMin =DateUtil.getDate("01/"+StringUtil.completarCerosIzq(compFuenteMin.getPeriodoDesde().toString(),2)+"/"+compFuenteMin.getAnioDesde(),DateUtil.ddSMMSYYYY_MASK); 
		Date fechaHastaMin =DateUtil.getDate("01/"+StringUtil.completarCerosIzq(compFuenteMin.getPeriodoHasta().toString(),2)+"/"+compFuenteMin.getAnioHasta(),DateUtil.ddSMMSYYYY_MASK);
		
		Date fechaDesdeSus=DateUtil.getDate("01/"+StringUtil.completarCerosIzq(compFuenteSus.getPeriodoDesde().toString(),2)+"/"+compFuenteSus.getAnioDesde(),DateUtil.ddSMMSYYYY_MASK);
		Date fechaHastaSus=DateUtil.getDate("01/"+StringUtil.completarCerosIzq(compFuenteSus.getPeriodoHasta().toString(),2)+"/"+compFuenteSus.getAnioHasta(),DateUtil.ddSMMSYYYY_MASK);
		
		String queryString = "SELECT DISTINCT p.periodo as periodo, p.anio as anio FROM ef_plaFueDatCom p WHERE (p.idCompFuente="+compFuenteMin.getId();
				
		queryString += " AND TO_DATE(('01/' || p.periodo || '/'  || p.anio), '%d/%m/%Y') >="
		+ " TO_DATE(('"+DateUtil.formatDate(fechaDesdeMin, DateUtil.ddSMMSYYYY_MASK)+"'), '%d/%m/%Y')";
		
		queryString += " AND TO_DATE(('01/' || p.periodo || '/'  || p.anio), '%d/%m/%Y') <="
			+ " TO_DATE(('"+DateUtil.formatDate(fechaHastaMin, DateUtil.ddSMMSYYYY_MASK)+"'), '%d/%m/%Y'))";
		
		
		queryString += " OR (p.idCompFuente="+compFuenteSus.getId();
		
		queryString += " AND TO_DATE(('01/' || p.periodo || '/'  || p.anio), '%d/%m/%Y') >="
		+ " TO_DATE(('"+DateUtil.formatDate(fechaDesdeSus, DateUtil.ddSMMSYYYY_MASK)+"'), '%d/%m/%Y')";
		
		queryString += " AND TO_DATE(('01/' || p.periodo || '/'  || p.anio), '%d/%m/%Y') <="
			+ " TO_DATE(('"+DateUtil.formatDate(fechaHastaSus, DateUtil.ddSMMSYYYY_MASK)+"'), '%d/%m/%Y'))";
		
		
		
		queryString += " ORDER BY p.anio, p.periodo";
		
	/*	String queryString = "SELECT DISTINCT p.periodo as periodo, p.anio as anio FROM PlaFueDatCom p WHERE (p.compFuente.id="+compFuenteMin.getId();
		
		queryString += " AND TO_DATE(('01/' || p.periodo || '/'  || p.anio), '%d/%m/%Y') >= :fechaDesdeMin";
			
		queryString += " AND TO_DATE(('01/' || p.periodo || '/'  || p.anio), '%d/%m/%Y') <= :fechaHastaMin )";			
			
		queryString += " OR (p.compFuente.id="+compFuenteSus.getId();
			
		queryString += " AND TO_DATE(('01/' || p.periodo || '/'  || p.anio), '%d/%m/%Y') >= :fechaDesdeSus";
		
		queryString += " AND TO_DATE(('01/' || p.periodo || '/'  || p.anio), '%d/%m/%Y') <= :fechaHastaSus )";
		*/	
			
		log.debug("queryString: "+queryString);
		
		SQLQuery query = session.createSQLQuery(queryString);
		
		/*query.setDate("fechaDesdeMin", fechaDesdeMin)
			.setDate("fechaHastaMin", fechaHastaMin)
			.setDate("fechaDesdeSus", fechaDesdeSus)
			.setDate("fechaHastaSus", fechaHastaSus);
		*/
		query.addScalar("periodo",Hibernate.INTEGER)
			.addScalar("anio",Hibernate.INTEGER);
		
		List<Object[]>listPeriodos = (List<Object[]>)query.list();
		
		return listPeriodos;
		
		
	}
	
}
