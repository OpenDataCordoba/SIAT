//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.out;

import java.io.FileReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.mozilla.javascript.NativeObject;

import coop.tecso.grs.GrsEngine;
import coop.tecso.grs.GrsMap;
import coop.tecso.grs.GrsUtil;

/**
 *
 * @author leonardo.fagnano
 *
 */
public class OutCsv implements Out {

	private Writer writer;	
	private String separator = ";";
	private String endln = "\n";
	private OutFormatter formatter = new OutFormatter();
	private List<String> order;
	private int tdcount = 0;
	private int include = 0; // permite operacion include
	
	/**
	 * overloaded constructor with out writer
	 * @param writer : el stream de salida 
	 * @param separator : es la cadena separadora 
	 * @param endline  : la cadena indicadora de fin de linea 
	 * @param include  : "0" indica ignorar llamadas a include()
	 *        otro llevarlas a cabo. 
	 */
	public OutCsv(Writer writer, String separator, String endline, String include){
		this.writer = writer;
		this.separator = separator;
		this.endln = endline;
		this.include = Integer.parseInt(include);
	}

	public void write(String str) throws Exception {
		writer.append(str);
	}
	
	public void close() throws Exception {
		writer.close();
	}

	/**
	 * Read a file and send to output  
	 */
	public void include(String filename)  throws Exception {
		if (include == 0)
			return;
		
		String filepath = GrsUtil.findFilename(filename);
		Reader r = new FileReader(filepath);
		char[] ch = new char[1024];
		int rb = 0;
		while ((rb = r.read(ch)) != -1)
			write(new String(ch,0,rb)); //queremos siempre pasar por write.
	}

	public String type() {
		return "csv";		
	}

	/**
	 * instance the table formatter with NativeObject
	 */
	public void fmt(NativeObject in){
		fmt((GrsMap)GrsEngine.jsToJvm(in));
	}

	/**
	 * instance the csv formatter 
	 */
	public void fmt(GrsMap map){
		formatter = new OutFormatter(map);
	}

	/**
	 * indicates the order of data cells in csv 
	 */
	public void order(String[] order){
		this.order = Arrays.asList(order);
	}

	/**
	 * csv header
	 */
	public void thead(String[] cells) throws Exception{
		for(int i=0; i < cells.length; i++) {
			td(cells[i]);
		}
		endtr();
	}

	/**
	 * csv body with multiple data rows and cells with NativeObject
	 */
	@SuppressWarnings("unchecked")
	public void row(NativeObject in) throws Exception{
		this.row((GrsMap) GrsEngine.jsToJvm(in));
	}

	/**
	 * csv with multiple data rows and cells 
	 */
	public void row(GrsMap row) throws Exception{
		Iterator<String> it = this.order == null?row.keySet().iterator():this.order.iterator();
		while(it.hasNext()){
			String propname = it.next();
			Object propvalue = row.get(propname);
			
			String format = formatter.efmt(propname, propvalue);
			tdf(propvalue, format);
			if (!it.hasNext())
				endtr();
		}
	}

	/**
	 * csv data cell
	 */
	public void tdf(Object value, String format) throws Exception {
		if (tdcount != 0) 
			write(separator);
		tdcount++;
		
		if(format.startsWith("%"))
			write(formatter.fmt(value, format));
		else
			write(formatter.fmt(value, format.substring(1)));
	}

	/**
	 * csv data cell
	 */
	public void td(Object value) throws Exception{
		String format = formatter.efmt("", value);
		tdf(value, format);
	}

	public void attrs(String[] attrs) throws Exception {
	}

	public void endtable() throws Exception {
	}

	public void endtbody() throws Exception {
	}

	public void endtfoot() throws Exception {
	}

	public void endthead() throws Exception {
	}

	public void endtr() throws Exception {
		tdcount = 0;
		write(endln);
	}

	public void table() throws Exception {
	}

	public void table(String caption) throws Exception {
	}

	public void tbody() throws Exception {
	}

	public void tfoot(String[] cells) throws Exception {
		thead(cells);
	}

	public void tfoot() throws Exception {
	}

	public void th(String cell) throws Exception {
		td(cell);
	}

	public void thead() throws Exception {
	}

	public void tr() throws Exception {
	}

	public void endfieldset() throws Exception {
		endtr();
	}

	public void field(String label, Object value) throws Exception {
		td(label);
		td(value);
		endtr();
	}

	public void fieldset() throws Exception {
		endtr();
	}

	public void fieldset(String legend) throws Exception {
		td(legend);
		endtr();
	}
	
	public void title(String title) throws Exception {
		td(title);
		endtr();
	}
	
	public void p(String paragraph) throws Exception  {
		td(paragraph);
		endtr();
	}

}