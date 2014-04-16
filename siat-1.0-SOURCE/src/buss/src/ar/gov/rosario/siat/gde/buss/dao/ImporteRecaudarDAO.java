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

public class ImporteRecaudarDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ImporteRecaudarDAO.class);
	
	public ImporteRecaudarDAO() {
		super(BaseBO.class);
	}
	
	public List<Object> getList(Long idRecurso, Long idPlan, Long idProcurador, Date fechaVtoDesde, Date fechaVtoHasta, Integer firstResult, Integer maxResult/*,Long idProcurador*/) throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		String queryString="SELECT SKIP "+firstResult+" FIRST "+maxResult+" plan.id, convenio.idprocurador, year(cuota.fechaVencimiento) anio," +
				" MONTH(cuota.fechaVencimiento) mes, cuota.numeroCuota, COUNT(*), SUM(cuota.importeCuota)" +
				" FROM gde_convenioCuota cuota LEFT JOIN gde_convenio convenio ON cuota.idconvenio=convenio.id" +
				" LEFT JOIN gde_plan plan on convenio.idplan=plan.id" +
				" WHERE convenio.idrecurso=" + idRecurso;
		
		if(idPlan!=null && idPlan.longValue()>0){
			queryString += " AND plan.id="+idPlan; 
		}
		
		if(fechaVtoDesde!=null){
			queryString +=" AND cuota.fechavencimiento>= TO_DATE('"+
				DateUtil.formatDate(fechaVtoDesde, DateUtil.ddSMMSYYYY_MASK)+"','%d/%m/%Y') ";
		}
		if(fechaVtoHasta!=null){
			queryString +=" AND cuota.fechavencimiento<= TO_DATE('"+
				DateUtil.formatDate(fechaVtoHasta, DateUtil.ddSMMSYYYY_MASK) +"','%d/%m/%Y') " ;
		}
		
	 	if (idProcurador.toString()!=null && !idProcurador.equals(-1L)) {// Agregado
	 		queryString += " AND convenio.idprocurador = " + idProcurador + " ";
	 	}
	 	
		queryString +=" GROUP BY plan.id, convenio.idprocurador, 3, 4,cuota.numeroCuota " +
				" ORDER BY plan.id, convenio.idprocurador, 3 asc, 4 asc, cuota.numeroCuota";		
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createSQLQuery(queryString);
		log.debug("Va a buscar convenios: query: "+queryString);
		return query.list();
		
	}

	
}
