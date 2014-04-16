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
import coop.tecso.demoda.iface.helper.DateUtil;

public class ProRecDAO extends GenericDAO {

	//private Log log = LogFactory.getLog(ProRecDAO.class);	
	
	public ProRecDAO() {
		super(ProRec.class);
	}

	/**
	 * Obtiene un ProRec a partir del id de un Procurador y del id del Recurso
	 * @param  idProcurador
	 * @param  idRecurso
	 * @return ProRec
	 */
	public ProRec getByIdProcuradorRecurso(Long idProcurador, Long idRecurso) {
		
		String queryString = "FROM ProRec pr " +
				"WHERE pr.procurador.id = :idProcurador " +
				"AND pr.recurso = :idRecurso";
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setLong("idProcurador", idProcurador)
			.setLong("idRecurso", idRecurso);
		
		return (ProRec) query.uniqueResult(); 
	}

	/**
	 * Chekea si existe un Recurso determinado para un Procurador en el periodo delimitado por fechaDesde-fechaHasta.
	 * @param  idProRecActual
	 * @param  idProcurador
	 * @param  idRecurso
	 * @param  fechaDesde
	 * @param  fechaHasta
	 * @return Boolean
	 */
	public Boolean existeRecurso(Long idProRecActual, Long idProcurador, Long idRecurso, Date fechaDesde, Date fechaHasta) {
		
		String queryString = "FROM ProRec pr WHERE pr.procurador.id = :idProcurador " + 
							 "AND pr.recurso.id = :idRecurso";

		if(idProRecActual!=null && idProRecActual>0)
			queryString += " AND pr.id<>"+idProRecActual;
		
		if (fechaDesde != null && fechaHasta != null) {
			
			queryString += " AND ( ( pr.fechaDesde BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR ( pr.fechaHasta BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR"+
									"( pr.fechaDesde <=TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
										" AND ( pr.fechaHasta IS NULL OR pr.fechaHasta >= TO_DATE('"+
												DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+										
											  ")"+
									")"+
								")";	
		}
		
		if (fechaDesde!=null) {
			queryString +=" AND ("+
								  	"pr.fechaDesde >= TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
									// que termine después
									" OR pr.fechaHasta >= TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+	
									//que empiece antes y no termine
									" OR (pr.fechaDesde <= TO_DATE('"+
											DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND "+
											"pr.fechaHasta IS NULL"+
										 ")"+
								")";
		}
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setLong("idProcurador", idProcurador)
			.setLong("idRecurso", idRecurso);

		List<ProRec> listResult = (ArrayList<ProRec>)query.list();	

		return (listResult!=null && !listResult.isEmpty()?true:false);
	
	}
}
