//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import org.hibernate.classic.Session;

import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.GenericAbstractDAO;
import coop.tecso.demoda.buss.dao.UniqueMap;

/**
 * Metodos para el uso de DAOs basados en Hibernate
 * @author tecso
 */
public class GenericDAO extends GenericAbstractDAO {

    public GenericDAO(Class boClass) {
    	super(boClass);
    }
    
    protected Session currentSession() {
    	return SweHibernateUtil.currentSession();
    }

	/**
	 *  Wrapper estatico de hasReferenceGen() para los DAO de Swe
	 */
	static public boolean hasReference(BaseBO bo, Class joinClass, String joinProperty) {
		GenericDAO dao = new GenericDAO(BaseBO.class);
		return dao.hasReferenceGen(bo, joinClass, joinProperty);
	}

	/**
	 *  Wrapper estatico de hasReferenceGen() para los DAO de Swe
	 */
	//static public boolean hasReferenceActivo(BaseBO bo, Class joinClass, String joinProperty) {
	//	GenericDAO dao = new GenericDAO(BaseBO.class);
	//	return dao.hasReferenceGen(bo, joinClass, joinProperty);
	//}


	/**
	 *  Wrapper estatico de checkIsUniqueGen() para los DAO de Swe
	 */
	static public boolean checkIsUnique(BaseBO obj, UniqueMap um) throws Exception {
		GenericDAO dao = new GenericDAO(BaseBO.class);
		return dao.checkIsUniqueGen(obj, um);
	}
}
