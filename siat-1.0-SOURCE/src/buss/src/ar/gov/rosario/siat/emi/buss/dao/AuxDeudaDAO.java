//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.iface.model.AuxDeudaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

public class AuxDeudaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(AuxDeudaDAO.class);
	
	
	public AuxDeudaDAO() {
		super(AuxDeuda.class);
	}

	@SuppressWarnings("unchecked")
	public List<AuxDeuda> getBySearchPage(AuxDeudaSearchPage auxDeudaSearchPage) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = "from AuxDeuda t ";
	    boolean flagAnd = false;
	    
	    if (log.isDebugEnabled()) { 
			log.debug("log de filtros del AuxDeudaSearchPage: " + auxDeudaSearchPage.infoString()); 
		}
	
		// filtro por Numero de Cuenta(
 		CuentaVO cuentaVO = auxDeudaSearchPage.getAuxDeuda().getCuenta();
		if (!StringUtil.isNullOrEmpty(cuentaVO.getNumeroCuenta())) {
            queryString += flagAnd ? " and " : " where ";
            queryString += "t.cuenta.numeroCuenta = '" + 
            	StringUtil.formatNumeroCuenta(cuentaVO.getNumeroCuenta()) +"'";
			flagAnd = true;
		}

		// filtro por emision
 		EmisionVO emisionVO = auxDeudaSearchPage.getAuxDeuda().getEmision();
		if (!ModelUtil.isNullOrEmpty(emisionVO)) {
            queryString += flagAnd ? " and " : " where ";
            queryString += "t.emision.id = " + emisionVO.getId();
			flagAnd = true;
		}

 		// Order By
		queryString += " order by t.cuenta.id ";
		
		if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);

		List<AuxDeuda> listAuxDeuda = (ArrayList<AuxDeuda>) executeCountedSearch(queryString, auxDeudaSearchPage);
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listAuxDeuda;
	}

	@SuppressWarnings("unchecked")
	public List<AuxDeuda> getListAuxDeudaBy(Long idEmision) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString = ""; 
		queryString += "from AuxDeuda t where t.emision.id = :idEmision";
		queryString += " order by t.cuenta.id ";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
							  .setLong("idEmision", idEmision);
		
		List<AuxDeuda> listAuxDeuda = (ArrayList<AuxDeuda>) query.list();	
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		return listAuxDeuda;
	}
	

	public List<Object[]> getListAuxDeudaByIdEmision(Long idEmision, Integer firstResult, Integer maxResults) {
		
		String queryString = "SELECT ad.*, c.numeroCuenta FROM emi_auxdeuda ad, pad_cuenta c " +
			" WHERE ad.idemision = :idEmision and c.id = ad.idCuenta order by ad.id";
		
		Session session = currentSession();

		// obtenemos el resultado de la consulta
		Query query = session.createSQLQuery(queryString)
								.addEntity("ad", AuxDeuda.class)
								.addScalar("numeroCuenta", Hibernate.STRING)
								.setLong("idEmision", idEmision);

		query.setFirstResult(firstResult).setFetchSize(maxResults);
		
		return (ArrayList<Object[]>) query.list();
	}

	
	/**
	 * Borra los registros de AuxDeuda que corresponden a la emision
	 * @param  emision
	 * @return int 
	 * @throws Exception
	 */
	public int deleteAuxDeudaByIdEmision(Emision emision) throws Exception {
	
		String queryString = "delete from AuxDeuda ad where ad.emision = :emision";
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString).setEntity("emision", emision);
		
		return query.executeUpdate();
	}

	public AuxDeuda getByIdWithCuenta(Long id) {
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": enter");
		
		String strQuery = "";
		strQuery += "select auxDeuda from AuxDeuda auxDeuda ";
		strQuery +=	"left join fetch deuda.cuenta where auxDeuda.id = :id";
		
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": query: " + strQuery);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(strQuery)
							 .setLong("id", id);
	
		if (log.isDebugEnabled()) 
			log.debug(DemodaUtil.currentMethodName() + ": exit");
	
		return (AuxDeuda) query.uniqueResult(); 
	}
	
	
	/**
	 *  Ejecuta un update statistics high sobre la tabla emi_auxDeuda.
	 * 
	 */
	public void updateStatisticsHigh(){
		
		//String queryString = "update statistics high for table emi_auxDeuda";			
				
		//Session session = currentSession();

		// Executamos el update statistics
		//session.createSQLQuery(queryString).executeUpdate();
				
	}

}
