//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.CierreBanco;
import ar.gov.rosario.siat.bal.buss.bean.DetallePago;
import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.bal.buss.bean.EstDetPago;
import ar.gov.rosario.siat.bal.buss.bean.EstTranAfip;
import ar.gov.rosario.siat.bal.buss.bean.TranAfip;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.iface.model.ImpuestoAfip;

public class DetallePagoDAO extends GenericDAO {
	
	private Log log = LogFactory.getLog(DetallePagoDAO.class);	

	private static long migId = -1;
	
	public DetallePagoDAO() {
		super(DetallePago.class);
	}


	/**
	 *  Obtiene la cantidad de DetallePago asociados al EnvioOsiris que no son impuestos ni multas
	 * 
	 * @param envioOsiris
	 * @return Long
	 */
	public Long getCantDetallePago(EnvioOsiris envioOsiris){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT COUNT(*) FROM DetallePago d WHERE d.estDetPago.id = "+EstDetPago.ID_PENDIENTE;
			   queryString +=" AND d.impuesto <> "+ImpuestoAfip.DREI_INTERESES.getId();
			   queryString +=" AND d.impuesto <> "+ImpuestoAfip.DREI_INT_MULTAS.getId();
			   queryString +=" AND d.impuesto <> "+ImpuestoAfip.ETUR_INT_MULTAS.getId();
			   queryString +=" AND d.impuesto <> "+ImpuestoAfip.ETUR_INTERESES.getId();
			   queryString +=" AND d.tranAfip.envioOsiris.id = "+envioOsiris.getId();
			   queryString +=" AND d.tranAfip.estTranAfip.id <> "+EstTranAfip.ID_ANULADA;
			   queryString +=" AND d.importePago <> 0";
		
		Query query = session.createQuery(queryString);
				
		return (Long) query.uniqueResult();
	}
	
	/**
	 *  Obtiene sumatoria de importePago de los detallePago asociados al EnvioOsiris.
	 * 
	 * @param envioOsiris
	 * @return Long
	 */
	public Double getMontoTotalDetalleForEnvio(EnvioOsiris envioOsiris){
		Session session = SiatHibernateUtil.currentSession();
		Double montoTotal;
		
		String queryString ="SELECT SUM(d.importePago) FROM DetallePago d ";
			   queryString +=" WHERE d.tranAfip.envioOsiris.id="+envioOsiris.getId();
			   queryString +=" AND d.estDetPago.id<>"+EstDetPago.ID_ANULADO;
		
		Query query = session.createQuery(queryString);

		montoTotal = (Double) query.uniqueResult();
		if (null == montoTotal) return 0D;

		return montoTotal;
	}
	
	/**
	 *  Obtiene sumatoria de importePago de los detallePago asociados al CierreBanco.
	 * 
	 * @param cierreBanco
	 * @return Long
	 */
	public Double getMontoTotalDetalleForCierreBanco(CierreBanco cierreBanco){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString ="SELECT SUM(d.importePago) FROM DetallePago d ";
			   queryString +=" WHERE d.tranAfip.cierreBanco.id="+cierreBanco.getId();
			   queryString +=" AND d.tranAfip.fechaAnulacion IS NULL";
			   queryString +=" AND d.estDetPago.id<>"+EstDetPago.ID_ANULADO;
		
		Query query = session.createQuery(queryString);
		
		Double montoTotal = (Double) query.uniqueResult();
		if (null == montoTotal) return 0D;
		
		return montoTotal;
				
	}
	
	/**
	 *  Obtiene sumatoria de importePago de los detallePago asociados a la TranAfip.
	 * 
	 * @param envioOsiris
	 * @return Long
	 */
	public Double getMontoTotalDetalleForTranAfip(TranAfip tranAfip){
		Session session = SiatHibernateUtil.currentSession();
		Double montoTotal;
		
		String queryString ="SELECT SUM(d.importePago) FROM DetallePago d ";
			   queryString +=" WHERE d.tranAfip.id="+tranAfip.getId();
			   queryString +=" AND d.estDetPago.id<>"+EstDetPago.ID_ANULADO;
		
		Query query = session.createQuery(queryString);

		montoTotal = (Double) query.uniqueResult();
		if (null == montoTotal) return 0D;

		return montoTotal;
	}
	
	/**
	 *  Actualiza el Estado del DetallePago, seteando la Novedad que lo genera.
	 *  Este update se hace mediante SQL por cuestiones de velocidad.
	 * 
	 * @param idNovedad
	 * @param idTransaccionAfip
	 * @param idEstDetPago
	 * 
	 * @return int
	 */
	public int updateEstDetPago(Long idNovedad, Long idTransaccionAfip, Long idEstDetPago){
		Session session = SiatHibernateUtil.currentSession();
		
		String subQueryStr = "SELECT id FROM bal_tranafip WHERE idtransaccionafip="+idTransaccionAfip;
		
		String queryString =" UPDATE bal_detallePago SET idEstDetPago="+idEstDetPago+",idNovedadEnvio="+idNovedad;
			   queryString+= " WHERE idTranAfip IN ("+subQueryStr+")";
			   
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
				
		return sqlQuery.executeUpdate();
	}

	/**
	 * Obtiene un detallePago de tipo interes para DReI o ETuR [detallePago.impuesto = Interes_DRei(6059) o Interes_ETuR(6054)]
	 * 
	 * @param idCuenta
	 * @param anio
	 * @param periodo
	 * @return
	 */
	public DetallePago getInteresByCuentaAnioPeriodo(Long idCuenta, Long idTranAfip, Long anio, Long periodo){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString =" FROM DetallePago d WHERE d.cuenta.id="+idCuenta;
			   queryString +=" AND d.anio="+anio;
			   queryString +=" AND d.periodo="+periodo;
			   queryString +=" AND d.tranAfip.id="+idTranAfip;
			   queryString +=" AND (d.impuesto="+ImpuestoAfip.DREI_INTERESES.getId();
			   queryString +=" OR d.impuesto="+ImpuestoAfip.ETUR_INTERESES.getId()+")";
			   
		Query query = session.createQuery(queryString);
		
		return (DetallePago) query.uniqueResult();
	}
		
}
