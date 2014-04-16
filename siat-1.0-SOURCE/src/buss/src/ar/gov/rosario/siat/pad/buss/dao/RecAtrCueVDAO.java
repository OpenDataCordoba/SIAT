//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecAtrCue;
import ar.gov.rosario.siat.pad.buss.bean.RecAtrCueV;
import coop.tecso.demoda.iface.helper.DateUtil;

public class RecAtrCueVDAO extends GenericDAO {

    private Log log = LogFactory.getLog(RecAtrCueVDAO.class);	
	
	public RecAtrCueVDAO() {
		super(RecAtrCueV.class);
	}
	
	/**
	 * Devuelve una lista de recAtrCueV vigentes para una cuenta a la fecha pasada como parametro
	 */
	public List<RecAtrCueV> getVigentes(Long idCuenta, Date fecha){
		
		String queryString = "from RecAtrCueV t where " +
				" t.cuenta.id = " + idCuenta +
				" AND t.fechaDesde <= TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" +
				" AND (t.fechaHasta IS NULL OR t.fechaHasta >=TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		List<RecAtrCueV> listResult = (ArrayList<RecAtrCueV>)query.list();
		
		return listResult;
	}
	
	public RecAtrCueV getRegimenVigente(Long idCuenta, Date fecha) throws Exception{
		
		String queryString = "from RecAtrCueV t where " +
				" t.cuenta.id = " + idCuenta +
				" AND t.fechaDesde <= TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" +
				" AND (t.fechaHasta IS NULL OR t.fechaHasta >=TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		queryString += " AND (t.recAtrCue.id = " + RecAtrCue.ID_REGIMEN_DREI;
		queryString += " OR t.recAtrCue.id = "+ RecAtrCue.ID_REGIMEN_ETUR +" )";
		
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		RecAtrCueV result = (RecAtrCueV)query.uniqueResult();
		
		return result;
	}

	/*
public RecAtrCueV getValorEmisionVigente(Long idCuenta, Date fecha) throws Exception{
		
		String queryString = "from RecAtrCueV t where " +
				" t.cuenta.id = " + idCuenta +
				" AND t.fechaDesde <= TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" +
				" AND (t.fechaHasta IS NULL OR t.fechaHasta >=TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		queryString += " AND (t.recAtrCue.id = " + RecAtrCue.ID_VALOREMISION_DREI;
		queryString += " OR t.recAtrCue.id = "+ RecAtrCue.ID_VALOREMISION_ETUR +" )";
		
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		RecAtrCueV result = (RecAtrCueV)query.uniqueResult();
		
		return result;
	}

*/
	
	public RecAtrCueV getValorCumurVigente(Long idCuenta, Date fecha) throws Exception{
		
		String queryString = "from RecAtrCueV t where " +
				" t.cuenta.id = " + idCuenta +
				" AND t.fechaDesde <= TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" +
				" AND (t.fechaHasta IS NULL OR t.fechaHasta >=TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		queryString += " AND (t.recAtrCue.id = " + RecAtrCue.ID_CUMUR_DREI;
		queryString += " OR t.recAtrCue.id = "+ RecAtrCue.ID_CUMUR_ETUR +" )";
		
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		RecAtrCueV result = (RecAtrCueV)query.uniqueResult();
		
		return result;
	}


	public RecAtrCueV getValorCategoriaRSVigente(Long idCuenta, Date fecha) throws Exception{
	
		String queryString = "from RecAtrCueV t where " +
				" t.cuenta.id = " + idCuenta +
				" AND t.fechaDesde <= TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" +
				" AND (t.fechaHasta IS NULL OR t.fechaHasta >=TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		queryString += " AND (t.recAtrCue.id = " + RecAtrCue.ID_CATEGORIARS_DREI;
		queryString += " OR t.recAtrCue.id = "+ RecAtrCue.ID_CATEGORIARS_ETUR +" )";
		
		log.debug("queryString: "+queryString);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		RecAtrCueV result = (RecAtrCueV)query.uniqueResult();
		
		return result;
	}
	
	
	public RecAtrCueV getValorPerIniRSVigente(Long idCuenta, Date fecha) throws Exception{
		
		String queryString = "from RecAtrCueV t where " +
				" t.cuenta.id = " + idCuenta +
				" AND t.fechaDesde <= TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" +
				" AND (t.fechaHasta IS NULL OR t.fechaHasta >=TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		queryString += " AND (t.recAtrCue.id = " + RecAtrCue.ID_PERIODOINI_DREI;
		queryString += " OR t.recAtrCue.id = "+ RecAtrCue.ID_PERIODOINI_ETUR+" )";
		log.debug("queryString: "+queryString);
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		RecAtrCueV result = (RecAtrCueV)query.uniqueResult();
		
		return result;
	}

	
	/**
	 * Devuelve el recAtrCueV vigente para una cuenta y atributo a la fecha pasada como parametro
	 *
	 * @param idRecCueAtr
	 * @param idCuenta
	 * @param fecha
	 * @return recAtrCueV
	 */
	public RecAtrCueV getVigenteByIdRecAtrCue(Long idRecCueAtr, Long idCuenta, Date fecha) {
		
		String queryString = "from RecAtrCueV t where " +
				" t.cuenta.id = " + idCuenta +
				" AND t.recAtrCue.id = " + idRecCueAtr +
				" AND t.fechaDesde <= TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')" +
				" AND (t.fechaHasta IS NULL OR t.fechaHasta >=TO_DATE('"+ DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y'))";
		
		Session session = SiatHibernateUtil.currentSession();
		Query query = session.createQuery(queryString);
		
		List<RecAtrCueV> listResult = (ArrayList<RecAtrCueV>)query.list();
		
		RecAtrCueV recAtrCueV = null;
		if(listResult.size() >= 1)
			recAtrCueV = listResult.get(0);
		
		return recAtrCueV;
	}
}
