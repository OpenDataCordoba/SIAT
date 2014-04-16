//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.bal.buss.bean.Asentamiento;
import ar.gov.rosario.siat.bal.buss.bean.AuxPagCuo;
import ar.gov.rosario.siat.bal.buss.bean.Transaccion;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.Estado;

public class AuxPagCuoDAO extends GenericDAO {
	
	public AuxPagCuoDAO(){
		super(AuxPagCuo.class);
	}
	/**
	 * Obtiene el Pago de Cuota Auxiliar para determinado Asentamiento y Recibo Convenio.
	 * 
	 * @param asentamiento
	 * @param reciboConvenio
	 * @return
	 */
	public AuxPagCuo getByAsentamientoYReciboConvenio(Asentamiento asentamiento,ReciboConvenio reciboConvenio) {
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxPagCuo t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.reciboConvenio.id = " + reciboConvenio.getId();
		
		Query query = session.createQuery(queryString);
		
		AuxPagCuo AuxPagCuo = (AuxPagCuo) query.uniqueResult();

		return AuxPagCuo;
	}
	
	/**
	 * Obtiene el Pago de Cuota Auxiliar para determinado Asentamiento y Convenio Cuota.
	 * 
	 * @param asentamiento
	 * @param convenioCuota
	 * @return
	 */
	public AuxPagCuo getByAsentamientoYConvenioCuota(Asentamiento asentamiento,ConvenioCuota convenioCuota) {
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxPagCuo t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.estado = "+ Estado.ACTIVO.getId();
		queryString += " and t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.convenioCuota.id = " + convenioCuota.getId();
		
		Query query = session.createQuery(queryString);
		
		AuxPagCuo AuxPagCuo = (AuxPagCuo) query.uniqueResult();

		return AuxPagCuo;
	}
	
	/**
	 *  Cantidad de Registros de AuxPagCuo correspondientes a Cuotas del Convenio pasado, considerando solo los
	 *  pagos buenos y si contar la cuota nro uno.
	 * 
	 * @param asentamiento
	 * @param convenio
	 * @return cantCuotas
	 * @throws Exception
	 */
	public Integer getCantCuotas(Asentamiento asentamiento, Convenio convenio) throws Exception{
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "select count(*) from AuxPagCuo t ";
	
	    // Armamos filtros del HQL
		queryString += " where t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.convenioCuota.convenio.id = " + convenio.getId();
		queryString += " and t.estadoConCuo.id = " + EstadoConCuo.ID_PAGO_BUENO;
		queryString += " and t.nroCuotaImputada <> 1";
		queryString += " and (t.reciboConvenio is null or t.reciboConvenio.esCuotaSaldo <> 1)";
		
		Query query = session.createQuery(queryString);
		
		Long cantCuota = (Long) query.uniqueResult();
		if(cantCuota == null){
			cantCuota = 0L;
		}
			
		return new Integer(cantCuota.intValue());
	}
	
	/**
	 * Elimina los registros de AuxPagCuo que corresponden al Asentamiento
	 * 
	 * @param asentamiento
	 * @return int
	 */
	public int deleteAllByAsentamiento (Asentamiento asentamiento){

		String queryString = "delete from AuxPagCuo t ";
			   queryString += " where t.asentamiento.id = "+asentamiento.getId(); 
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
	    
		return query.executeUpdate();
	}
	
	/**
	 * Obtiene la lista de AuxPagCuo de manera paginada
	 * @param asentamiento
	 * @param firstResult
	 * @param maxResults
	 * @return List<AuxPagCuo>
	 */
	public List<AuxPagCuo> getListByAsentamiento(Asentamiento asentamiento, Integer firstResult, Integer maxResults ){
		
		String queryStringH = " FROM AuxPagCuo t  " +
			"WHERE t.asentamiento = :asentamiento ORDER BY t.fechaPago, t.id";			

		String queryString = "SELECT SKIP "+firstResult+" FIRST "+maxResults+" * FROM bal_auxPagCuo t  " +
			"WHERE t.idAsentamiento = "+asentamiento.getId();			
				
		Session session = currentSession();

		// Obtenemos el resultado de la consulta
		Query query;
		if(firstResult==null && maxResults == null){
			query = session.createQuery(queryStringH).setEntity("asentamiento", asentamiento);			
		}else{
			query = session.createSQLQuery(queryString).addEntity(AuxPagCuo.class);			
		}
		
		return (ArrayList<AuxPagCuo>) query.list();
	}
	
	/**
	 *  Obtiene la lista de registros de AuxPagCuo a asentar para la Transaccion.
	 * 
	 * @param asentamiento
	 * @param transaccion
	 * @return listAuxPagCuo
	 * @throws Exception
	 */
	public List<AuxPagCuo> getListByAsentamientoYTransaccion(Asentamiento asentamiento, Transaccion transaccion) throws Exception {
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "from AuxPagCuo t ";
	    
		// Armamos filtros del HQL		
		queryString += " where t.asentamiento.id = " + asentamiento.getId();
		queryString += " and t.transaccion.id = " + transaccion.getId();
            
	    Query query = session.createQuery(queryString);
	    List<AuxPagCuo> listAuxPagCuo = (ArrayList<AuxPagCuo>) query.list();
		
		return listAuxPagCuo; 
	}
	
	public List<Long> getListIdCuotaAuxPagCuo(List<Long> listIdCuotas) throws Exception{
		Session session = SiatHibernateUtil.currentSession();
		List<Long>idEncontrados=new ArrayList<Long>();
		if (ListUtil.isNullOrEmpty(listIdCuotas)){
			return idEncontrados;
		}
		
		String sqlString = "SELECT idConvenioCuota FROM bal_auxPagCuo WHERE idConvenioCuota IN ("+ ListUtil.getStringList(listIdCuotas) + ") ";
		
		Query query = session.createSQLQuery(sqlString).addScalar("idConvenioCuota",Hibernate.LONG);
		

		
		return query.list();
	}
}
