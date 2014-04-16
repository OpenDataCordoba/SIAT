//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Query Table Row: 
 * Representa una fila de una tabla de consultas
 * 
 * @author Tecso Coop. Ltda.
 * */
public class QryTableRow {

	private QryTable table;
	private List<Object> listElements;

	// Opcodes de las operaciones soportadas para consulta
	private static final int OP_LT	= 1; // Menor estricto
	private static final int OP_LE	= 2; // Menor igual
	private static final int OP_EQ	= 3; // Igual
	private static final int OP_GT	= 4; // Mayor estricto
	private static final int OP_GE	= 5; // Mayor igual

	public QryTableRow(QryTable table) {
		this.table = table;
		this.listElements = new ArrayList<Object>();
	}

	public void addElement(Object o) {
		this.listElements.add(o);
	} 

	public Object getValor(String column) {
		int index = table.getColIndexBy(column);
		return listElements.get(index);
	}

	public boolean validCriteria(String ... args) {
		boolean valid = true; 
		for (int i=0; i < args.length; i+=3) {
		    String column = args[i].toLowerCase().trim();
			int opcode 	  = getOpcode(args[i + 1].trim());
			String value  = args[i + 2].trim();
			switch (opcode) {
				case OP_LT: valid &= ((Double) getValor(column)).longValue() <   Double.parseDouble(value); break;
				case OP_LE: valid &= ((Double) getValor(column)).longValue() <=  Double.parseDouble(value); break;
				case OP_EQ: valid &= ((Double) getValor(column)).longValue() ==  Double.parseDouble(value); break;
				case OP_GT: valid &= ((Double) getValor(column)).longValue() >   Double.parseDouble(value); break;
				case OP_GE: valid &= ((Double) getValor(column)).longValue() >=  Double.parseDouble(value); break;
			}
			
			if (!valid)
				return false;
		}
		
		return true;
	}

	private int getOpcode(String op) {
		if (op.equals("<"))
			return OP_LT; 
		if (op.equals("<="))
			return OP_LE; 
		if (op.equals("=="))
			return OP_EQ; 
		if (op.equals(">"))
			return OP_GT; 
		if (op.equals(">="))
			return OP_GE; 
		return 0;
	}

	public void print() {
		System.out.println();
		for (Object o: listElements) {
			System.out.print(o + " ");
		}
		System.out.println();
	}
}
