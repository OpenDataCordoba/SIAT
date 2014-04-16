//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ProRec;
import ar.gov.rosario.siat.gde.buss.bean.ProRecDesHas;
import coop.tecso.demoda.iface.helper.DateUtil;

public class ProRecDesHasDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(ProRecDesHasDAO.class);	
	
	public ProRecDesHasDAO() {
		super(ProRecDesHas.class);
	}

	/**
	 * Chekea si existe un Rango del Recurso, delimitado por desde-hasta, en el intevalo delimitado 
	 * por fechaDesde-fechaHasta.
	 * @param  idProRecDesHasActual
	 * @param  idProRec
	 * @param  desde
	 * @param  hasta
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return Boolean
	 */
	public Boolean existeRango(Long idProRecDesHasActual, Long idProRec, String desde ,String hasta, Date fechaDesde, Date fechaHasta) {
		
		String queryString = "FROM ProRecDesHas prDesHas WHERE prDesHas.proRec.id = :idProRec "; 
		
		if(idProRecDesHasActual!=null && idProRecDesHasActual>0)
			queryString += " AND prDesHas.id<>"+idProRecDesHasActual;
		
		queryString += " AND ( 	  (prDesHas.desde BETWEEN '" + desde + "' AND '" + hasta + "')" +
							 " OR (prDesHas.hasta BETWEEN '" + desde + "' AND '" + hasta + "')" + 
							 " OR ((prDesHas.desde < '" + desde +"') AND (prDesHas.hasta > '" + hasta +"'))" +
							 ")"; 
	
		if (fechaDesde != null && fechaHasta != null) {
			
			queryString += " AND ( ( prDesHas.fechaDesde BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR ( prDesHas.fechaHasta BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR"+
									"( prDesHas.fechaDesde <=TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
										" AND ( prDesHas.fechaHasta IS NULL OR prDesHas.fechaHasta >= TO_DATE('"+
												DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+										
											  ")"+
									")"+
								")";	
		}
		
		if (fechaDesde!=null) {
			queryString +=" AND ("+
								  	"prDesHas.fechaDesde >= TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
									// que termine después
									" OR prDesHas.fechaHasta >= TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+	
									//que empiece antes y no termine
									" OR (prDesHas.fechaDesde <= TO_DATE('"+
											DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND "+
											"prDesHas.fechaHasta IS NULL"+
										 ")"+
								")";
		}
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setLong("idProRec", idProRec);
		

		List<ProRec> listResult = (ArrayList<ProRec>)query.list();	

		return (listResult!=null && !listResult.isEmpty()?true:false);
	
	}
}
