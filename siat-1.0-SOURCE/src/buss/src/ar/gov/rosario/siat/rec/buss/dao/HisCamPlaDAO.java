//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.dao;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.rec.buss.bean.HisCamPla;
import ar.gov.rosario.siat.rec.buss.bean.PlaCuaDet;

public class HisCamPlaDAO extends GenericDAO {

	private Log log = LogFactory.getLog(HisCamPlaDAO.class);
	
	public HisCamPlaDAO() {
		super(HisCamPla.class);
	}
	
	public HisCamPla getUltimoHistorioByPlaCuaDet(PlaCuaDet plaCuaDet){

		HisCamPla hisCamPla;
		Session session = SiatHibernateUtil.currentSession();

		String queryString = "SELECT MAX(fecha) FROM HisCamPla t WHERE t.plaCuaDet = :plaCuaDet";
		
		Query query = session.createQuery(queryString)
			.setEntity("plaCuaDet", plaCuaDet);
		
		query.setMaxResults(1);
		Date ultimaFecha = (Date) query.uniqueResult();
		
		queryString = "FROM HisCamPla t WHERE t.plaCuaDet = :plaCuaDet AND t.fecha = :ultimaFecha";
		
		query = session.createQuery(queryString)
					.setEntity("plaCuaDet", plaCuaDet)
					.setDate("ultimaFecha", ultimaFecha);
		
		hisCamPla = (HisCamPla) query.uniqueResult();	

		return hisCamPla; 
		
		
		
	}
	
}
