//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class ImporteRecaudadoDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ImporteRecaudadoDAO.class);
	
	public ImporteRecaudadoDAO() {
		super(BaseBO.class);
	}
	
	public List<Object> getForReporteDetallado(Long idRecurso, Long idPlan, Date fechaPagoDesde, 
			Date fechaPagoHasta, Long idViaDeuda, Long idProcurador, Integer firstResult, Integer maxResult) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String queryString="SELECT SKIP " + firstResult + " FIRST " + maxResult + " plan.id idPlan, plan.desplan plan," +
				" viaDeuda.desviadeuda, convenio.idprocurador, year(cuota.fechapago) anio, month(cuota.fechapago) mes," +
				" convenio.nroconvenio nroConvenio, convenio.idperfor idContribuyente, cuota.numeroCuota," +
				" cuota.fechapago, cuota.capitalcuota, cuota.interes, cuota.actualizacion, cuota.importecuota, cuota.importePago" +
				" FROM gde_convenioCuota cuota" +
				"	LEFT JOIN gde_convenio convenio ON cuota.idconvenio = convenio.id" +
				" 	LEFT JOIN gde_plan plan on convenio.idplan = plan.id" +
				"	LEFT JOIN def_viadeuda viaDeuda on convenio.idviadeuda = viaDeuda.id" +
				" WHERE convenio.idrecurso = "+idRecurso;
		
		if(idPlan!=null && idPlan.longValue()>0){
			queryString +=" AND plan.id=" + idPlan;
		}
		
		if(idViaDeuda!=null && idViaDeuda.longValue()>0){
			queryString +=" AND viaDeuda.id=" + idViaDeuda;
		}
		
		if(idProcurador!=null && idProcurador.longValue()>0){
			queryString +=" AND convenio.idprocurador=" + idProcurador;
		}
		
		if(fechaPagoDesde!=null){
			queryString +=" AND cuota.fechapago >= TO_DATE('"+
				DateUtil.formatDate(fechaPagoDesde, DateUtil.ddSMMSYYYY_MASK)+"','%d/%m/%Y') ";
		}		
		
		if(fechaPagoHasta!=null){
			queryString +=" AND cuota.fechapago <= TO_DATE('"+
			DateUtil.formatDate(fechaPagoHasta, DateUtil.ddSMMSYYYY_MASK)+"','%d/%m/%Y') ";
		}
		
		queryString +=" ORDER BY plan.id, 4, nroConvenio, 3,5,6";	
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString);
		log.debug("Va a buscar convenios: query: "+queryString);
		return query.list();		
	}

	public List<Object> getForReporteResumido(Long idRecurso, Long idPlan, Date fechaPagoDesde, 
			Date fechaPagoHasta, Long idViaDeuda, Long idProcurador, Integer firstResult, Integer maxResult) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		String queryString="SELECT SKIP "+firstResult+" FIRST "+maxResult+" plan.id idPlan, plan.desplan, viaDeuda.desviadeuda, convenio.idprocurador," +
				" YEAR(cuota.fechapago) anio, MONTH(cuota.fechapago) mes, COUNT(convenio.id) cantConvenios, SUM(cuota.capitalcuota) totCapital," +
				" SUM(cuota.interes) totInteres,SUM(cuota.actualizacion) totAct," +
				" SUM(cuota.importecuota) totImporte, SUM(cuota.importePago) totImpPag" +
				" FROM gde_convenioCuota cuota" +
				"	LEFT JOIN gde_convenio convenio ON cuota.idconvenio = convenio.id" +
				" 	LEFT JOIN gde_plan plan ON convenio.idplan = plan.id" +
				"	LEFT JOIN def_viadeuda viaDeuda ON plan.idviadeuda = viaDeuda.id" +
				" WHERE convenio.idrecurso = "+idRecurso;
		
		if(idPlan!=null && idPlan.longValue()>0){
			queryString +=" AND plan.id="+idPlan;
		}
		
		if(idViaDeuda!=null && idViaDeuda.longValue()>0){
			queryString +=" AND viaDeuda.id="+idViaDeuda;
		}
		
		if(idProcurador!=null && idProcurador.longValue()>0){
			queryString +=" AND convenio.idprocurador = " + idProcurador;
		}
		
		if(fechaPagoDesde!=null){
			queryString +=" AND cuota.fechapago >= TO_DATE('"+
				DateUtil.formatDate(fechaPagoDesde, DateUtil.ddSMMSYYYY_MASK)+"','%d/%m/%Y') ";
		}		
		
		if(fechaPagoHasta!=null){
			queryString +=" AND cuota.fechapago <= TO_DATE('"+
			DateUtil.formatDate(fechaPagoHasta, DateUtil.ddSMMSYYYY_MASK)+"','%d/%m/%Y') ";
		}
		
		queryString +=" GROUP by 1,4,2,3,5,6 ORDER BY 1,4,2,3,5,6";	
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString);
		log.debug("Va a buscar convenios: query: " + queryString);
		return query.list();
		
	}
	
}
