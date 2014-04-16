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
public class OutHtml implements Out {

	private OutFormatter formatter = new OutFormatter();
	private List<String> order;
	private Writer out;
	private String attrs = "";
	private int fields = 0;
	
	public OutHtml(Writer out) {
		super();
		this.out = out;
	}

	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.Out#write(java.lang.String)
	 */
	public void write(String write) throws Exception{
		out.append(write);
	}
	
	/**
	 * Read a file and send to output  
	 */
	public void include(String filename)  throws Exception {
		String filepath = GrsUtil.findFilename(filename);
		Reader r = new FileReader(filepath);
		char[] ch = new char[1024];
		int rb = 0;
		while ((rb = r.read(ch)) != -1)
			write(new String(ch,0,rb)); //queremos siempre pasar por write.
	}

	public void close() throws Exception {
		out.close();
	}

	public String type() {
		return "html";		
	}

	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#fmt(org.mozilla.javascript.NativeObject)
	 */
	public void fmt(NativeObject in){
		fmt((GrsMap)GrsEngine.jsToJvm(in));
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#fmt(java.util.Map)
	 */
	public void fmt(GrsMap map){
		formatter = new OutFormatter(map);
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#order(java.lang.String[])
	 */
	public void order(String[] order){
		this.order = Arrays.asList(order);
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#attrs(java.util.List)
	 */
	public void attrs(String[] attrs) throws Exception{
		StringBuffer attrsbuf = new StringBuffer();
		for (String atribute : attrs) 
			attrsbuf.append(" "+atribute);
		this.attrs = attrsbuf.toString();
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#table()
	 */
	public void table() throws Exception{
		write("<table" + attrs + ">\n");
		attrs="";
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#table(java.lang.String)
	 */
	public void table(String caption) throws Exception{
		write("<table" + attrs + ">\n");
		write("<caption>"+caption+"</caption>\n");
		attrs="";
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#endtable()
	 */
	public void endtable() throws Exception{
		write("</table>\n");
	}

	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#thead()
	 */
	public void thead() throws Exception{
		write("<thead>\n");
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#endthead()
	 */
	public void endthead() throws Exception{
		write("</thead>\n");
	}

	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#thead(java.lang.String[])
	 */
	public void thead(String[] cells) throws Exception{
		thead();
		tr();
		for (String cell : cells) {
			th(cell);
		}
		endtr();
		endthead();
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#th(java.lang.String)
	 */
	public void th(String cell) throws Exception{
		write("<th"+attrs);
		if(cell.startsWith("r:"))
			write(" style='text-align:right;'>"+cell.substring(2)+"</th>");
		else if(cell.startsWith("c:"))
			write(" style='text-align:center;'>"+cell.substring(2)+"</th>");
		else if(cell.startsWith("l:"))
			write(" style='text-align:left;'>"+cell.substring(2)+"</th>");
		else
			write(">"+cell+"</th>\n");
		attrs="";
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#tbody()
	 */
	public void tbody() throws Exception{
		write("<tbody>\n");
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#endtbody()
	 */
	public void endtbody() throws Exception{
		write("</tbody>\n");
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#row(org.mozilla.javascript.NativeObject)
	 */
	@SuppressWarnings("unchecked")
	public void row(NativeObject in) throws Exception{
		row((GrsMap)GrsEngine.jsToJvm(in));
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#row(java.util.Map)
	 */
	public void row(GrsMap row) throws Exception{
		Iterator<String> it =order == null?row.keySet().iterator():order.iterator();
		tr();
		while(it.hasNext()){
			String propname = it.next();
			Object propvalue = row.get(propname);
			String format = formatter.efmt(propname, propvalue);
			tdf(propvalue,format);
		}
		endtr();
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#td(java.lang.Object, java.lang.String)
	 */
	public void tdf(Object value, String format) throws Exception{
		write("<td"+attrs);
		if(format.startsWith("r%")){
			write(" style='text-align:right;'>"+formatter.fmt(value, format.substring(1)));
		}else if(format.startsWith("c%")){
			write(" style='text-align:center;'>"+formatter.fmt(value, format.substring(1)));
		}else if(format.startsWith("l%")){
			write(" style='text-align:left;'>"+formatter.fmt(value, format.substring(1)));
		}else{
			write(" >"+formatter.fmt(value, format));
		}
		write("</td>\n");
		attrs="";
	}

	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#td(java.lang.Object)
	 */
	public void td(Object value) throws Exception{
		String format = formatter.efmt("", value);
		tdf(value, format);
	}
		
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#tr()
	 */
	public void tr() throws Exception{
		write("<tr"+attrs+">\n");
		attrs="";
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#endtr()
	 */
	public void endtr() throws Exception{
		write("</tr>\n");
	}
	
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#tfoot(java.lang.String[])
	 */
	public void tfoot(String[] cells) throws Exception{
		tfoot();
		tr();
		for (String cell : cells) {
			th(cell);
		}
		endtr();
		endtfoot();
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#tfoot()
	 */
	public void tfoot() throws Exception{
		write("<tfoot>\n");
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutTable#endtfoot()
	 */
	public void endtfoot() throws Exception{
		write("</tfoot>\n");
	}

	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutFieldset#fieldset()
	 */
	public void fieldset() throws Exception{
		write("<fieldset>\n");
		fieldtable();
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutFieldset#fieldset(java.lang.String)
	 */
	public void fieldset(String legend) throws Exception{
		write("<fieldset>\n");
		write("<legend><h2>"+legend+"</h2></legend>\n");
		fieldtable();
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutFieldset#endfieldset()
	 */
	public void endfieldset() throws Exception{
		if(fields == 1)	{
			endtr();
			fields = 0;
		}
		endtable();
		write("</fieldset>\n");
	}
	
	/* (non-Javadoc)
	 * @see coop.tecso.grs.out.OutFieldset#field(java.lang.String, java.lang.Object)
	 */
	public void field(String label, Object value) throws Exception{
		if(label.startsWith("2:"))
			// two columns label 
			dblfield(label, value);
		else
			// standard field
			stdfield(label, value);
	}
	
	/**
	 * simple standard field (colspan = 1) 
	 */
	private void stdfield(String label, Object value) throws Exception{
		if(fields == 0){
			tr();
		}	
		tdf("<label>"+label+"</label>", "r%s");
		td(value);
		if(++fields == 2){
			endtr();
			fields = 0;
		}
	}
	
	/**
	 * special double field (colspan = 3)
	 */
	private void dblfield(String label, Object value) throws Exception{
		String[] attrs = {" colspan='3' "};
		if(fields == 1)	{
			endtr();
			fields = 0;
		}
		tr();
		tdf("<label>"+label.substring(2)+"</label>", "r%s");

		attrs(attrs);
		td(value);
		endtr();
	}
	
	/**
	 * instance a new out
	 */
	private void fieldtable() throws Exception{
		String[] attrs = {"class='tablefilters'"};
		attrs(attrs);
		table();
	}

	/**
	 * Escribe el titulo de la pagina.
	 * Este metodo termina por escribi un tag 'h1' en html.
	 * @param title
	 */
	public void title(String title) throws Exception {
		write(String.format("<h1>%s</h1>\n", title));
	}
	
	/**
	 * Escribe un parrafo.
	 * Este metodo termina por escribi un tag 'p' en html.
	 * @param title
	 */
	public void p(String paragraph) throws Exception  {
		write(String.format("<p>%s</p>\n", paragraph));		
	}
}
