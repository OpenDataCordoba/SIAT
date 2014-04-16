//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.dao;

import java.util.Date;

public class SqlUtil {
	public static final int SQL_INFORMIX = 1;
	public static final int SQL_POSTGRESQL = 2;
	
	private int sqlDialect;
	private String sqlToDateFormat;
	private String sqlToDateTimeFormat;
	private String sqlToDateSplited;

	public SqlUtil(String hibernateDialect) {
		init(hibernateDialect);
	}
	
	/*
	 * Por razones de historia, en SIAT no se usan 'Hibernate Criteria'
	 * Para abstraernos del formateo de fechas, usamos esta funcion. 
	 */
	private void init(String hibernateDialect) {
		initSqlDialect(hibernateDialect);
		initDateFormat();		
	}
	
	private void initDateFormat() {
		switch (sqlDialect) {
		case SQL_INFORMIX:
			sqlToDateFormat = " TO_DATE('%1$tY%1$tm%1$td', '%%Y%%m%%d') ";
			sqlToDateTimeFormat = " TO_DATE('%1$tY%1$tm%1$td %1$tk:%1$tM:%1$tS', '%%Y%%m%%d %%H:%%M:%%S') ";
			sqlToDateSplited = " TO_DATE('%s'||'%s'||'%s', '%%Y%%m%%d')";
			break;
		case SQL_POSTGRESQL:
			sqlToDateFormat = " TO_DATE('%1$tY%1$tm%1$td', 'YYYYMMDD') ";
			sqlToDateTimeFormat = " TO_DATE('%1$tY%1$tm%1$td %1$tk:%1$tM:%1$tS', 'YYYYMMDD HH24:MI:SS') ";
			sqlToDateSplited = " TO_DATE('%s'||'%s'||'%s', 'YYYYMMDD')";
			break;
		default:
			throw new RuntimeException("Cannot, format date. Unknow hibernate sql dialect.");
		}
	}

	/**
	 * Identificar en una constante la base de datos que se usa.
	 * El metodo usa la propiedad "dialect" de hibenate para identificar la db.
	 * @return 0 si no puede determinar la db.
	 */
	private void initSqlDialect(String hibernateDialect) {
		if (hibernateDialect.contains("PostgreSQLDialect")) {
			sqlDialect = SQL_POSTGRESQL;
		} else if (hibernateDialect.contains("InformixDialect")) {
			sqlDialect = SQL_INFORMIX;
		}
	}

	/**
	 * Arma una sentencia SQL valida para la base de datos de siat para poder usar el tipo java Date en una sentencia SQL.
	 * Tipicamente retorna: to_date('20091201, 'YYYYMMDD');
	 * Lo que varia de DB en DB es la cadena de formateo del segundo parametro de to_date.
	 * Esta funcion se usa principalmente para concatenar tipos Date en SQL
	 * @param date fecha a utilizar.
	 * @return
	 */
	public String sqlDate(Date date) {
		if (date == null)
			return "";
		return String.format(sqlToDateFormat, date);
	}
	
	/**
	 * Retorna un string: to_date('anio'||'mes'||'dia', '...')
	 * @param anio cadena para formar el año, pj: 2000, o tabla.anio o cualquier expresion sql valida,
	 * @param mes cadena para formar el mes, pj: 12, o tabla.mes o cualquier expresion sql valida
	 * @param dia cadena para formar el dia. pj: 24, o tabla.dia, o cualquier expresion sql valida,
	 * @return
	 */
	public String sqlConcatDate(String anio, String mes, String dia) {
		return String.format(sqlToDateSplited, anio, mes, dia);
	}

	public String sqlDateTime(Date date) {
		if (date == null)
			return "";
		return String.format(sqlToDateTimeFormat, date);
	}


}
