//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.out;

import org.mozilla.javascript.NativeObject;

import coop.tecso.grs.GrsMap;

public interface Out {
	
	/* general output */
	
	String DefaultSeparator = ";";
	String DefaultEndLn = "\n";
	String DefaultInclude = "0";

	/**
	 * write any line to out
	 */
	public void write(String write) throws Exception;

	/**
	 * include
	 */
	public void include(String filename)  throws Exception;
	
	/**
	 * Close file
	 */
	public void close() throws Exception;

	/* format and style helpers */

	/**
	 * instance the table formatter with NativeObject
	 */
	public void fmt(NativeObject in);
	public void fmt(GrsMap in);

	/**
	 * indicates the order of data cells in table 
	 */
	public void order(String[] order);
	
	/**
	 * attributes to add the tag
	 */
	public void attrs(String[] attrs) throws Exception;

	/* table  */
	
	/**
	 * open table tag
	 */
	public void table() throws Exception;

	/**
	 * open table tag with caption
	 */
	public void table(String caption) throws Exception;

	/**
	 * close table tag
	 */
	public void endtable() throws Exception;

	/**
	 * open table header tag
	 */
	public void thead() throws Exception;

	/**
	 * close table header tag
	 */
	public void endthead() throws Exception;

	/**
	 * overloaded table head 
	 */
	public void thead(String[] cells) throws Exception;

	/**
	 *  table header cell
	 */
	public void th(String cell) throws Exception;

	/**
	 * open table body tag
	 */
	public void tbody() throws Exception;

	/**
	 * close table body tag
	 */
	public void endtbody() throws Exception;

	/**
	 * table body with multiple data rows and cells
	 */
	public void row(NativeObject in) throws Exception;
	public void row(GrsMap in) throws Exception;

	/**
	 * table data cell
	 */
	public void tdf(Object value, String format) throws Exception;

	/**
	 * table data cell
	 */
	public void td(Object value) throws Exception;

	/**
	 * open table row tag
	 */
	public void tr() throws Exception;

	/**
	 * close table row tag
	 */
	public void endtr() throws Exception;

	/**
	 * overloaded table foot 
	 */
	public void tfoot(String[] cells) throws Exception;

	/**
	 * open table footer tag
	 */
	public void tfoot() throws Exception;

	/**
	 * close table footer tag
	 */
	public void endtfoot() throws Exception;

	/* fieldset */
	
	/**
	 * open fieldset tag
	 */
	public void fieldset() throws Exception;

	/**
	 * overloaded open fieldset tag with legend
	 */
	public void fieldset(String legend) throws Exception;

	/**
	 * close fieldset tag
	 */
	public void endfieldset() throws Exception;

	/**
	 * add new field to fieldset
	 */
	public void field(String label, Object value) throws Exception;

	/**
	 * write title
	 */
	void title(String title) throws Exception;

	/**
	 * write a paragraph
	 */
	void p(String paragraph) throws Exception;

	/**
	 * type of implemetation
	 */
	String type();
}