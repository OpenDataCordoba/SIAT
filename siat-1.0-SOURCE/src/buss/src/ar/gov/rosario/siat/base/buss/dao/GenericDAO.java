//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.buss.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.GenericAbstractDAO;
import coop.tecso.demoda.buss.dao.SqlUtil;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Datum;

/**
 * Metodos para el uso de DAOs basados en Hibernate
 * @author tecso
 *
 */
public class GenericDAO extends GenericAbstractDAO {

	private SqlUtil sqlUtil = new SqlUtil(SiatHibernateUtil.getSqlDialect());

    public GenericDAO(Class boClass) {
        super(boClass);
    }
	
	protected Session currentSession() {
		return SiatHibernateUtil.currentSession();
	}


	/**
	 *  Wrapper estatico de GenericAbstractDAO.hasReferenceGen() para los DAO de Siat
	 */
	static public boolean hasReference(BaseBO bo, Class joinClass, String joinProperty) {
		GenericDAO dao = new GenericDAO(BaseBO.class);
		return dao.hasReferenceGen(bo, joinClass, joinProperty);
	}

	/**
	 *  Wrapper estatico de GenericAbstractDAO.checkIsUniqueGen() para los DAO de Siat
	 */
	static public boolean checkIsUnique(BaseBO obj, UniqueMap um) throws Exception {
		GenericDAO dao = new GenericDAO(BaseBO.class);
		return dao.checkIsUniqueGen(obj, um);
	}

	/**
	 *  Wrapper estatico de GenericAbstractDAO.hasReferenceActivoGen() para los DAO de Siat
	 */
	static public boolean hasReferenceActivo(BaseBO bo, Class joinClass, String joinProperty) {
		GenericDAO dao = new GenericDAO(BaseBO.class);
		return dao.hasReferenceActivoGen(bo, joinClass, joinProperty);
	}

	/**
	 *  Update Statistics para los DAO de Siat
	 */
	static public boolean updateStatistics() throws Exception {
		GenericDAO dao = new GenericDAO(BaseBO.class);
		String sql = "update statistics";	 
		return dao.currentSession().connection().prepareStatement(sql).execute();
	}

	/**
	  *  Devuelve el ultimo valor de id cargado en el archivo con path y nombre pasados como parametros. 
	  * 
	  * @param	path, nameFile - dos string con el camino y el nombre del archivo de load para el bean.
	  * @return long - el ultimo id asignado.
	  * @throws Exception
	  */
	 public long getLastId(String path, String nameFile) throws Exception{
		 BufferedReader input = null;
		 long migId = -1;
		    			
		 File file = new File(path+nameFile);
		 try{
			 input = new BufferedReader(new FileReader(file));				 
		 }catch(FileNotFoundException e){
			 input = null;
		 }
		
		 if(input != null){
			 long valorToSkip = 0;
			 long fileLength = file.length();
			 if(fileLength!=0 && fileLength>512)
				 valorToSkip = file.length()-512L; 
			 input.skip(valorToSkip);
			 String line; 
			 String prevLine = "";
			 while (( line = input.readLine()) != null){
				 prevLine = line;
			 }
			 Datum datum = Datum.parse(prevLine);
			 
			 if(!StringUtil.isNullOrEmpty(datum.getCols(0)))
				 migId = datum.getLong(0);
			 else
				 migId = 0;

			 input.close();
		 }else{
			 migId = 0;
		 }
		 
	 	return migId;
	}

	 /**
	  * Obtiene el siguiente valor para la Secuencia
	  * En caso de no existir devuelve null 
	  * @param nameSequence
	  * @return Long
	  */	
	 public Long getNextVal(String nameSequence){
		//informix String queryString = "SELECT " + nameSequence + ".nextval FROM systables WHERE tabid = 1";
		String queryString = "SELECT nextval('" + nameSequence + "')";

		Session session = SiatHibernateUtil.currentSession();

		Query query = session.createSQLQuery(queryString);
		
		//pasaje de BigInteger a Long 
		BigInteger bi = (BigInteger) query.uniqueResult() ;
		if (bi != null){
			return (bi.longValue()); 
		}else {
			// no existe valores para la secuencia nameSequence
			return null;
		}

		}
	 
		protected Long countRegsTable(String tableName) throws Exception {
			Connection con = currentSession().connection();

			PreparedStatement ps = con.prepareStatement("select count(*) c from " + tableName);
			ResultSet rs = ps.executeQuery();

			long c = 0;
			while(rs.next()) {
				c = rs.getLong("c");
			}

			return c;
		}
	 
	 
	 
	static public boolean checkConcurrency(BaseBO bo, UniqueMap claveFuncional, String userName) throws Exception {
		GenericDAO dao = new GenericDAO(BaseBO.class);
		return dao.checkConcurrencyGen(bo, claveFuncional, userName);
	}

	/**
	 * Wrapper a SqlDate.sqlDate
	 */
	public String sqlDate(Date date) {
		return this.sqlUtil.sqlDate(date);
	}

	/**
	 * Wrapper a SqlDate.sqlDate
	 */
	public String sqlConcatDate(String anio, String mes, String dia) {
		return this.sqlUtil.sqlConcatDate(anio, mes, dia);
	}

	/**
	 * Wrapper a SqlDate.sqlDateTime
	 */
	public String sqlDateTime(Date date) {
		return this.sqlUtil.sqlDateTime(date);
	}

}
