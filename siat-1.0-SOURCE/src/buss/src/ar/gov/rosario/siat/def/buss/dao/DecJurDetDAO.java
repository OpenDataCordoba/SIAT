//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.gde.buss.bean.DecJur;
import ar.gov.rosario.siat.gde.buss.bean.DecJurDet;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;



public class DecJurDetDAO extends GenericDAO {

	private Log log = LogFactory.getLog(DecJurDetDAO.class);	
	
	public DecJurDetDAO() {
		super(DecJurDet.class);
	}
	
	
	public Boolean getRecConADecHasReference(RecConADec recConADec){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = " FROM DecJurDet d WHERE d.recConADec.id = " + recConADec.getId();
		
		Query query = session.createQuery(queryString);
		
		List<DecJurDet>listDecJurDet = (List<DecJurDet>)query.list();
		
		return listDecJurDet.size() > 0;
		
	}
	
	public Boolean getRecConADecExistente(RecConADec recConADec, DecJur decJur){
		
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = " FROM DecJurDet d WHERE d.recConADec.id = " + recConADec.getId();
		
		queryString += " AND d.decJur.id = "+decJur.getId();
		
		Query query = session.createQuery(queryString);
		
		List<DecJurDet>listDecJurDet = (List<DecJurDet>)query.list();
		
		return listDecJurDet.size() > 0;
		
	}


	public DecJurDet getByCuentaPeriodoAnioyActividad(Cuenta cuenta, Integer periodo, Integer anio, Long idActividad){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM DecJurDet d WHERE d.decJur.cuenta.id = "+cuenta.getId();
		
		queryString += " AND d.decJur.estado = 1";
		
		queryString += "AND d.decJur.periodo = "+periodo;
		
		queryString += "AND d.decJur.anio = "+anio;
		
		queryString += "AND d.recConADec.id = "+idActividad;
		
		Query query = session.createQuery(queryString);
		
		DecJurDet decJurDet = (DecJurDet)query.uniqueResult();
		
		return decJurDet;
	}

}
