//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.buss.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import coop.tecso.demoda.buss.dao.JDBCConnManager;

public class SiatJDBCDAO {

	public static final String DS_NAME = "java:comp/env/ds/siat";
       
    private Logger log = Logger.getLogger(SiatJDBCDAO.class);
	/**
	 * Constructor
	 * Pasa nombre del archivo de propiedades a la super de Demoda
	 */
    public SiatJDBCDAO() {
	}
    
    public static Connection getConnection() throws Exception {
    	return JDBCConnManager.getConnection(DS_NAME);
    }
}
