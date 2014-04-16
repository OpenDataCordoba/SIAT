//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Query Table: 
 * Representa un tabla sobre la cual se pueden efectuar consultas.
 * 
 * @author Tecso Coop. Ltda.
 */
public class QryTable {
	
	private static final String ROW_SEPARATOR = ",";
	private static final String COL_SEPARATOR = "\\|";
	
	public static final int ERR_COLUMN_NOT_FOUND = -1; 
	
	// Columnas de la matriz
	private List<QryTableCol> listCol;
	// Filas de la matriz
	private List<QryTableRow> listRow;

	/**
	 * Constructor
	 * 
	 * @param columns (string que representa las columnas de la tabla)
	 * @param body    (string que representa el cuerpo de la tabla)
	 */
	public QryTable(String columns, String body) throws IllegalArgumentException {
		this.listCol  = new ArrayList<QryTableCol>();
		this.listRow  = new ArrayList<QryTableRow>();

		// Obtenemos las columnas
		for (String c: columns.split(ROW_SEPARATOR)) {
			String name    = c.split(COL_SEPARATOR)[0].toLowerCase().trim();
			String strType = c.split(COL_SEPARATOR)[1].toLowerCase().trim();

			// Obtenemos el tipo de dato de la columna
			QryTableDataType type = null; 
			if (strType.equals(QryTableDataType.NUMERICO.getAbreviatura())) 
				type = QryTableDataType.NUMERICO ; 

			if (strType.equals(QryTableDataType.FECHA.getAbreviatura())) 
				type = QryTableDataType.FECHA ; 

			listCol.add(new QryTableCol(this,name,type));
		}

		// Obtenemos las filas
        body = body.substring(0, body.length() - 1);
		for (String r: body.split(ROW_SEPARATOR)) {
			QryTableRow row = new QryTableRow(this);
			for (String e: r.split(COL_SEPARATOR)) {
				row.addElement(Double.parseDouble(e));
			}
			listRow.add(row);
		}
	}
	
	/**
	 * Ejecuta una consulta sobe la tabla y retorna
	 * el primer match encontrado.
	 * Retorna null si no encuentra nada.
	 * */
	public QryTableRow select(String ... criteria) {
		for (QryTableRow r: listRow) {
			if (r.validCriteria(criteria)) {
				return r;
			}
		}
		return null;
	}

	/**
	 * Obtiene el indice de la columna 
	 * correspondiente al nombre colName
	 * */
	public int getColIndexBy(String colName) {
		int i=0;
		for (QryTableCol c: listCol) {
			if (c.getColName().equals(colName))
				return i;
			i++;
		}
		
		// Si no es una columna de la tabla
		return ERR_COLUMN_NOT_FOUND;
	}

	// TODO: Solo para pruebas de la interfaz.
	// Eliminar en produccion
	public void print() {
		System.out.println();
		for (QryTableCol c: listCol) {
			System.out.print(c.getColName() + " ");
		}
		System.out.println();
		for (QryTableRow r: listRow) {
			r.print();
		}
	}
}
