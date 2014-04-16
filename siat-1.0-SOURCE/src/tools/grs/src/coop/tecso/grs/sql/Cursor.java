//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import coop.tecso.grs.GrsMap;

public class Cursor {

	private ResultSet rs;
	private Statement st;
	private Connection cn;
	
	public Cursor(ResultSet rs, Statement st, Connection cn) { 
		this.rs = rs;
		this.st = st;
		this.cn = cn;
	}

	public GrsMap read() throws SQLException {
		if (rs == null || !rs.next())
			return null;
		
		GrsMap row = new GrsMap();
		Sql.loadmap(rs, row);
		return row;
	}

	public void close() {
		Sql.closeall(rs, st);
	}
}
