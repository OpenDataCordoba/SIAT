//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.dao;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.classic.Session;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.RecConADec;
import ar.gov.rosario.siat.def.buss.bean.ValUnRecConADe;

public class ValUnRecConADeDAO extends GenericDAO {

	private Log log = LogFactory.getLog(ValUnRecConADeDAO.class);	
	
	public ValUnRecConADeDAO() {
		super(ValUnRecConADe.class);
	}

	
	public ValUnRecConADe getListVigenteByFechaYRecConADe(Date fecha, RecConADec recConADec){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM ValUnRecConADe v WHERE v.recConADec.id = "+recConADec.getId();
		
		queryString += " AND v.fechaDesde <= :fecha";
		
		queryString += " AND (v.fechaHasta IS NULL OR v.fechaHasta >= :fecha )";
		
		Query query = session.createQuery(queryString).setDate("fecha", fecha);
		
		query.setMaxResults(1);
		
		ValUnRecConADe valUnRecConADe = (ValUnRecConADe)query.uniqueResult();
		
		return valUnRecConADe;
	}
	
	public ValUnRecConADe getVigenteByFechaYRecConADeYValRef(Date fecha, RecConADec recConADec, Double valRef){
		Session session = SiatHibernateUtil.currentSession();
		
		String queryString = "FROM ValUnRecConADe v WHERE v.recConADec.id = "+recConADec.getId();
		
		queryString += " AND v.fechaDesde <= :fecha";
		
		queryString += " AND (v.fechaHasta IS NULL OR v.fechaHasta >= :fecha )";
		
		queryString += " AND v.valRefDes <= :valRef AND (v.valRefHas IS NULL OR v.valRefHas >= :valRef )"; 
		
		Query query = session.createQuery(queryString).setDate("fecha", fecha)
														.setDouble("valRef", valRef);
		
		query.setMaxResults(1);
		
		ValUnRecConADe valUnRecConADe = (ValUnRecConADe)query.uniqueResult();
		
		return valUnRecConADe;
	}
}
