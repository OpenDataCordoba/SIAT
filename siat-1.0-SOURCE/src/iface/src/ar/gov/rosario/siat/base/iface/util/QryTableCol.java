//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.util;

/**
 * Query Table Column: 
 * Representa una columna de una tabla de consultas 
 * 
 * @author Tecso Coop. Ltda.
 * */
public class QryTableCol {
	
	private QryTable table;
	private String colName;
	private QryTableDataType type;

	QryTableCol(QryTable table, String colName, QryTableDataType type) {
		this.table	 = table;
		this.colName = colName;
		this.type    = type;
	}

	// Getters y Setters
	public QryTable getTable() {
		return table;
	}

	public void setTable(QryTable table) {
		this.table = table;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public QryTableDataType getType() {
		return type;
	}

	public void setType(QryTableDataType type) {
		this.type = type;
	}
}
