//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ProRec;
import ar.gov.rosario.siat.gde.buss.bean.ProRecCom;
import coop.tecso.demoda.iface.helper.DateUtil;

public class ProRecComDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ProRecComDAO.class);	
	
	public ProRecComDAO() {
		super(ProRecCom.class);
	}

	/**
	 * Chekea si existe una Comisison del Recurso, delimitado por desde-hasta, en el intevalo delimitado 
	 * por fechaDesde-fechaHasta.
	 * @param  idProRecComActual
	 * @param  idProRec
	 * @param  fecVtoDeuDes
	 * @param  fecVtoDeuHas
	 * @param fechaDesde
	 * @param fechaHasta
	 * @return Boolean
	 */
	public Boolean existeComision(Long idProRecComActual, Long idProRec, Date fecVtoDeuDes , Date fecVtoDeuHas, Date fechaDesde, Date fechaHasta) {
		
		String queryString = "FROM ProRecCom prCom WHERE prCom.proRec.id = :idProRec "; 
		
		if(idProRecComActual!=null && idProRecComActual>0)
			queryString += " AND prCom.id<>"+idProRecComActual;
		
		if (fecVtoDeuDes != null && fecVtoDeuHas != null) {
			queryString += " AND ( ( prCom.fecVtoDeuDes BETWEEN TO_DATE('"+
									DateUtil.formatDate(fecVtoDeuDes, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fecVtoDeuHas, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR ( prCom.fecVtoDeuHas BETWEEN TO_DATE('"+
									DateUtil.formatDate(fecVtoDeuDes, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fecVtoDeuHas, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR"+
									"( prCom.fecVtoDeuDes <=TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
										" AND ( prCom.fecVtoDeuHas IS NULL OR prCom.fecVtoDeuHas >= TO_DATE('"+
												DateUtil.formatDate(fecVtoDeuHas, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+										
											  ")"+
									")"+
								")";	
		}
		
		if (fechaDesde != null && fechaHasta != null) {
			
			queryString += " AND ( ( prCom.fechaDesde BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR ( prCom.fechaHasta BETWEEN TO_DATE('"+
									DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') AND TO_DATE('"+
									DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') "+
									") OR"+
									"( prCom.fechaDesde <=TO_DATE('"+
										DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+
										" AND ( prCom.fechaHasta IS NULL OR prCom.fechaHasta >= TO_DATE('"+
												DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')"+										
											  ")"+
									")"+
								")";	
		}
		
		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createQuery(queryString)
			.setLong("idProRec", idProRec);
		
		List<ProRec> listResult = (ArrayList<ProRec>)query.list();	

		return (listResult!=null && !listResult.isEmpty()?true:false);
	
	}


	/**
	 * Obtiene el vigente para el proRec pasado como parametro a la fecha pasada como parametro 
	 * @param proRec - Si es nulo no se tiene en cuenta
	 * @param fechaVtoDeuda - Si es nula no se tiene en cuenta
	 * @return
	 * 
	 * 24-11-08 : agregamos anio de vencimienro
	 * 
	 * en los caso que se recuperen mas de un registro
	 *  if (anio) < 94 -> corresponde ProRecCom mayor
	 *  sino -> corresponde el menor
	 * 
	 * 
	 * 
	 */
	public ProRecCom getVigente(ProRec proRec, Date fechaVtoDeuda, Date fechaVigencia, Long anio) {
		boolean flagAnd = false;
		String queryString = "FROM ProRecCom t ";
		
		if(proRec!=null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString +=" t.proRec.id ="+proRec.getId();
			flagAnd = true;
		}
		if(fechaVtoDeuda!=null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " t.fecVtoDeuDes <= TO_DATE('"+
							DateUtil.formatDate(fechaVtoDeuda, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";

			queryString += " AND (t.fecVtoDeuHas IS NULL OR t.fecVtoDeuHas >= TO_DATE('"+
							DateUtil.formatDate(fechaVtoDeuda, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') )";
		}
		
		if(fechaVigencia!=null){
			queryString += flagAnd?" AND ":" WHERE ";
			queryString += " t.fechaDesde <= TO_DATE('"+
							DateUtil.formatDate(fechaVigencia, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";

			queryString += " AND (t.fechaHasta IS NULL OR t.fechaHasta >= TO_DATE('"+
							DateUtil.formatDate(fechaVigencia, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y') )";
		}
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		log.debug("Va a buscar en ProRecCom - query:"+query.getQueryString());

		List<ProRecCom> listProRecCom = query.list();
		
		if (listProRecCom.size()==0) {
			return null;
			
		} else if (listProRecCom.size()==1) {
			ProRecCom proRecCom = (ProRecCom ) listProRecCom.get(0);
			return proRecCom;
			
		} else if (listProRecCom.size() > 1) {

			// en los caso que se recuperen mas de un registro
			//  if (anio) < 94 -> corresponde ProRecCom mayor
			//  sino -> corresponde el menor
			
			ProRecCom proRecComMenor = (ProRecCom) listProRecCom.get(0);
			ProRecCom proRecComMayor = (ProRecCom) listProRecCom.get(0);
			
			// itera desde el segundo elemento
			// size = 2 -> 0,1
			for (int i=1; i<listProRecCom.size(); i++) {
				ProRecCom proRecCom = (ProRecCom ) listProRecCom.get(i);
				
				if (proRecCom.getPorcentajeComision().doubleValue() < proRecComMenor.getPorcentajeComision().doubleValue() )
					proRecComMenor = proRecCom;
				
				if (proRecCom.getPorcentajeComision().doubleValue() > proRecComMayor.getPorcentajeComision().doubleValue() )
					proRecComMayor = proRecCom;
			
			}

			// aqui tenemos la mayor y la menor

			if (anio==null)
				return proRecComMenor;
			
			else if (anio.longValue()>=1994) 
				return proRecComMenor;
			
			else
				return proRecComMayor;
			
			
		}
			
		
		return null;
	
	
	}

}
