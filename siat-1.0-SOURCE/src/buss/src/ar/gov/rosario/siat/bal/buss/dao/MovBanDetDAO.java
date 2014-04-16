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

import ar.gov.rosario.siat.bal.buss.bean.MovBan;
import ar.gov.rosario.siat.bal.buss.bean.MovBanDet;
import ar.gov.rosario.siat.bal.iface.model.TotMovBanDetHelper;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class MovBanDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(MovBanDetDAO.class);
	
	public MovBanDetDAO() {
		super(MovBanDet.class);
	}

	/**
	 *  Obtiene una lista de totales por impuesto y cuenta corriente para los detalles de movimientos bancarios correspondientes al banco y nro de cierre banco informados.
	 * 
	 * @param banco
	 * @param nroCierreBanco
	 * @return
	 * @throws Exception
	 */
	public List<TotMovBanDetHelper> getListTotalesByCierreBanco(Long banco, Long nroCierreBanco) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select t.movBan.fechaAcredit, t.impuesto, t.nroCuenta, sum(t.debito), sum(t.credito) from MovBanDet t";
		queryString += " where t.bancoRec = "+banco+" and t.nroCierreBanco = "+nroCierreBanco;
		queryString += " and t.conciliado = "+SiNo.NO.getId();
		queryString += " group by t.movBan.fechaAcredit, t.impuesto, t.nroCuenta";
		queryString += " order by t.movBan.fechaAcredit, t.impuesto, t.nroCuenta";
	    
	     if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<Object[]> listResult = (ArrayList<Object[]>) query.list();
		
	    List<TotMovBanDetHelper> listTotMovBanDet = new ArrayList<TotMovBanDetHelper>();
	    
	    for(Object[] arrayResult: listResult){
	    	Date fechaAcredit = (Date) arrayResult[0];
	    	Long impuesto = (Long) arrayResult[1];
			String nroCuenta = (String) arrayResult[2];
			Double debito = (Double) arrayResult[3];
			Double credito = (Double) arrayResult[4];
			listTotMovBanDet.add(new TotMovBanDetHelper(fechaAcredit, impuesto,nroCuenta,debito,credito));			
		}
	    
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return listTotMovBanDet; 
	}
	
	/**
	 *  Obtiene de detalles de movimientos bancarios correspondientes al banco y nro de cierre banco informados.
	 * 
	 * @param banco
	 * @param nroCierreBanco
	 * @return
	 * @throws Exception
	 */
	public List<MovBanDet> getListByCierreBanco(Long banco, Long nroCierreBanco) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from MovBanDet t";
		queryString += " where t.bancoRec = "+banco+" and t.nroCierreBanco = "+nroCierreBanco;
		queryString += " and t.conciliado = "+SiNo.NO.getId();
	    
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    List<MovBanDet> listMovBanDet = (ArrayList<MovBanDet>) query.list();
			    
		return listMovBanDet; 
	}
	
	/**
	 *  Devuelve verdadero si existe algun detalle no conciliado asociado al 'Movimiento Bancario'
	 * 
	 * @param movBan
	 * @return
	 * @throws Exception
	 */
	public Boolean existeDetalleSinConciliarForMovBan(MovBan movBan) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from MovBanDet t";
		queryString += " where t.movBan.id = "+movBan.getId();
		queryString += " and t.conciliado = "+SiNo.NO.getId();
	    
	    if (log.isDebugEnabled()) log.debug(funcName + ": Query: " + queryString);
	    
	    Query query = session.createQuery(queryString);
	    query.setMaxResults(1);
	    MovBanDet movBanDet = (MovBanDet) query.uniqueResult();
		if(movBanDet != null)
			return true;
		
		return false;
	}
	
}
