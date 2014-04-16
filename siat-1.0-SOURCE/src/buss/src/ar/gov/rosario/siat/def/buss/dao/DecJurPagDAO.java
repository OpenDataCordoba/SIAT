//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.DecJurPag;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;



public class DecJurPagDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DecJurPagDAO.class);	
	
	public DecJurPagDAO() {
		super(DecJurPag.class);
	}
	
	/**
	 * Obtiene la sumatoria de Otros Pagos asociados a la declaracion jurada
	 * sin tener en cuenta los del tipo de la lista pasada como parametro.
	 * 
	 * @param idDecJur
	 * @param listIdsAExcluir
	 * @return
	 */	@Deprecated
	public Double getSumOtrosPagosNotInTipPagDecJur(Long idDecJur, Long[] listIdsAExcluir){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Double sumOtrosPagos = null;
		String strIdsTipPagDecJur = StringUtil.getStringComaSeparate(listIdsAExcluir);
		
		String queryString = "SELECT SUM(d.importe) FROM DecJurPag d ";
			   queryString+= " WHERE d.decJur.id = "+idDecJur;
			   queryString+= " AND d.tipPagDecJur.id NOT IN ("+strIdsTipPagDecJur+")";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		sumOtrosPagos = (Double) query.uniqueResult();
		if (null == sumOtrosPagos) sumOtrosPagos = 0D;
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return sumOtrosPagos;
	}
	
	/**
	 * Elimina los registros de pago asociados a la declaracion jurada omitiendo aquellos del tipo
	 * de la lista pasada como parametro 
	 * 
	 * @param idDecJur
	 * @param listIdsAExcluir
	 */
	public int deleteByDecJurNotInTipPagDecJur(Long idDecJur, Long[] listIdsAExcluir){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String strIdsTipPagDecJur = StringUtil.getStringComaSeparate(listIdsAExcluir);
		
		String queryString = "DELETE FROM DecJurPag d ";
			   queryString+= " WHERE d.decJur.id = "+idDecJur;
			   queryString+= " AND d.tipPagDecJur.id NOT IN ("+strIdsTipPagDecJur+")";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
			    
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return query.executeUpdate();
	}
	
	
	/**
	 * Obtiene Pago/Retencion declarada por Afip (de tipo Osiris) para una Declaración Jurada.
	 * @param idDecJur
	 * @return
	 */
	public DecJurPag getByDecJurAndTipPagDecJur(Long idDecJur, Long idTipPagDecJur){
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
				
		String queryString = "FROM DecJurPag d ";
			   queryString+= " WHERE d.decJur.id = "+idDecJur;
			   queryString+= " AND d.tipPagDecJur.id = "+idTipPagDecJur;
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
			    
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return (DecJurPag) query.uniqueResult();
	}
}
