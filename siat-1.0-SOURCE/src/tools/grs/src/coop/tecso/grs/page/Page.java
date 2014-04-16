//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.page;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.NativeObject;

import coop.tecso.grs.Grs;
import coop.tecso.grs.GrsEngine;
import coop.tecso.grs.GrsMap;
import coop.tecso.grs.GrsUtil;

/**
 * The page formater.
 *
 * This class controls the execution flow of the HTML page file.
 * Renders the Page 
 *
 * @author Andrei
 */
public class Page {

	public final int CANT_COL_TABLE = 4;

	// Variables
	private Map<String, Object> params = new HashMap<String, Object>();
	 	
	private int colTable = 0;
	private int rowTable = 0;
	private int tabIndex = 0;

	private String callerMethod;
	private String colspan="1";

	private GrsPageContext context;
	public List<String> messages = new ArrayList<String>(); //mensages de validacion, error o lo que sea.
		
	public Page(GrsPageContext context) throws Exception {
		this.context = context;
		initParams(context.parameters);
		parseInfo(context);
	}

	// parse process descriptor
	static public void parseInfo(GrsPageContext ctx) throws Exception {
		Map<String, String> info = Grs.processInfo(ctx.process);
		
		ctx.processpath = info.get("processpath");
		ctx.jspage = info.get("page");
		ctx.jsprocess = info.get("proc");
	}

	// matchea algo como esto: /*/grs/{process}/{id}/{action}
	// process default es "" 
	// idrun default es 0 
	// action default es "get" 
	static public void parseUri(GrsPageContext ctx) {
		String[] tk = ctx.uri.split("/");
		int i = 0;
		
		while (!"grs".equals(tk[i++]));

		ctx.process = "";
		if (i < tk.length) 
			ctx.process = tk[i++].trim();
	
		ctx.id = 0;
		if (i < tk.length) 
			try { ctx.id = Long.parseLong(tk[i++]); } catch (Exception e) {}

		ctx.function = "get";
		if (i < tk.length) 
			ctx.function = tk[i++].trim();
	}

	// Metodos
	/**
	 * Sobrecargado método input que llama al GrsEngine para transformacion.
	 * @param page PageContext
	 * @param in NativeObject
	 */
	@SuppressWarnings("unchecked")
	public void input(NativeObject in) {
		input((GrsMap) GrsEngine.jsToJvm(in));
	}

	@SuppressWarnings("unchecked")
	public void fieldset(NativeObject in) {
		fieldset((GrsMap) GrsEngine.jsToJvm(in));
	}

	@SuppressWarnings("unchecked")
	public void label(NativeObject in) {
		label((GrsMap) GrsEngine.jsToJvm(in));
	}

	@SuppressWarnings("unchecked")
	public void button(NativeObject in) {
		button((GrsMap) GrsEngine.jsToJvm(in));
	}

	/**
	 * Input locaso que delega segun atributo type:
	 * 	text
	 * 	textarea
	 * 	double
	 * 	long
	 * 	date
	 * 	radio
	 * 	checkbox
	 */
	public void input(GrsMap map) {
		String type = Page.toString(map,"type");
		
		if("text".equals(type)) {
			inputText(map);
		} else 	if("textarea".equals(type)) {
			inputTextArea(map);
		} else 	if("float".equals(type)) {
			inputTextDouble(map);
		} else 	if("integer".equals(type)) {
			inputTextLong(map);
		} else 	if("date".equals(type)) {
			inputTextDate(map);
		} else 	if("radio".equals(type)) {
			inputRadio(map);
		} else 	if("checkbox".equals(type)) {
			inputCheckBox(map);
		} else 	if("select".equals(type)) {
			inputSelect(map);
		} else {
			throw new RuntimeException("Unknown type value: '" + type + "'. Only accept: text, textarea, float, integer, date, radio, checkbox, select");
		}
	}
	
	// Metodos Escritura
	
	/**
	 * Imprime en page, el tag() y content() del objeto PageInputFieldset
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputFieldset
	 */
	private void fieldset(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		PageFieldset fieldSet = new PageFieldset(context, map);
		escribir(fieldSet.tag() + fieldSet.content());
	}
	/**
	 * Imprime en page, el end() del objeto PageInputFieldset
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputFieldset
	 */
	public void endfieldset() {
		callerMethod = GrsUtil.currentMethodName();
		PageFieldset fieldSet = new PageFieldset(context, new GrsMap());
		escribir(fieldSet.end());
	}
	/**
	 * Imprime en page, el all() del objeto PageInputCheckbox
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputCheckbox
	 */
	private void inputCheckBox(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageInputCheckbox checkBox = new PageInputCheckbox(context, map);
		escribir(checkBox.all());
	}
	/**	
	 * Imprime en page, el all() del objeto PageInputLabel
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputLabel
	 */
	private void label(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageInputLabel label = new PageInputLabel(context, map);
		escribir(label.all());
	}
	/**
	 * Imprime en page, el all() del objeto PageInputButton
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputButton
	 */
	private void button(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageButton button = new PageButton(context, map);
		escribir(button.all());
	}
	//sb.append("<LINK REL=StyleSheet HREF=\"/siat/view/src/styles/tramites.css\" TYPE=\"text/css\">").append("\n");
	//sb.append("<LINK REL=StyleSheet HREF=\"/siat/view/src/styles/siat.css\" TYPE=\"text/css\">").append("\n");		
	
	/**
	 * Imprime en page, el all() del objeto PageInputRadio
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputRadio
	 */
	private void inputRadio(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageInputRadio radio = new PageInputRadio(context, map);
		escribir(radio.all());
	}

	/**
	 * Imprime en page, el all() del objeto PageInputSelect
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputSelect
	 */
	private void inputSelect(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageInputSelect select = new PageInputSelect(context, map);
		escribir(select.all());
	}

	/**
	 * Imprime en page, el all() del objeto PageInputTextArea
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputTextArea
	 */
	private void inputTextArea(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageInputTextarea textArea = new PageInputTextarea(context, map);
		escribir(textArea.all());
	}

	/**
	 * Imprime en page, el all() del objeto PageInputText
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputText
	 */
	private void inputText(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageInputText text = new PageInputText(context, map);
		escribir(text.all());
	}

	/**
	 * Imprime en page, el all() del objeto PageInputTextLong
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputTextLong
	 */
	private void inputTextLong(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageInputTextLong tlong = new PageInputTextLong(context, map);
		escribir(tlong.all());
	}

	/**
	 * Imprime en page, el all() del objeto PageInputTextDouble
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputTextDouble
	 */
	private void inputTextDouble(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageInputTextDouble tdouble = new PageInputTextDouble(context, map);
		escribir(tdouble.all());
	}

	/**
	 * Imprime en page, el all() del objeto PageInputTextFecha
	 * @param page Page donde escribir
	 * @param map Mapa de parámetros del elemento PageInputTextFecha
	 */
	private void inputTextDate(GrsMap map) {
		callerMethod = GrsUtil.currentMethodName();
		colspan = Page.toString(map,"colspan");
		PageInputTextDate textFecha = new PageInputTextDate(context, map);
		escribir(textFecha.all());
	}
	
	/**
	 * Imprime por el contenido teniendo en cuenta los distintos elementos HTML.
	 * <table>,<td>,<tr>,<head>,<body>,<footer>, etc.
	 * Formatea los elementos en una tabla  de 4 columnas. (CANT_COL_TABLE)
	 * @param page Page donde escribir
	 * @param String contenido a escribir.
	 */
	private void escribir(String contenido) {

		if("".equals(colspan) || colspan==null){
			colspan="1";
		}
		
		if(rowTable == 0 ){
			rowTable++;
			colTable = 0;
		} else {
			if (colTable == 0) {

			} else if (colTable == CANT_COL_TABLE) {
				colTable=0;
				context.write(endRow());
				context.write(beginRow());
			} else if (colTable < CANT_COL_TABLE) {

			} 
		}

		colTable += Integer.valueOf(colspan) ;

		if (callerMethod.equals("endfieldset")){
			colTable = 0;
			context.write(endRow());
			context.write(endTable());
			tabIndex--;
			context.write(tabPrint() + contenido);
		} else {
			if(callerMethod.equals("fieldset")){
				colTable = 0;
				context.write(tabPrint() + contenido);
				tabIndex++;
				context.write(beginTable());
				context.write(beginRow());
			} else {
				context.write(beginCol());
				context.write(tabPrint() + contenido);
				context.write(endCol());
			}
		}
		
		colspan = "1";
	}
	//Fin Metodos escritura
	
	/**
	 * recorre cada valor del mapa, y lo normaliza con la
	 * funcion Grs.paramToObject
	 */
	private void initParams(Map<String, Object>  map) {
		params.clear();
		for(String key:map.keySet()) {
			Object value = map.get(key);
			if (value != null) {
				params.put(key, Grs.paramToObject(Grs.paramToString(value)));
			}
		}
	}

	public Map<String, Object> parameters() {
		return params;
	}
	
	/**
	 * Imprime una cantidad de \t dependiendo de la variable tabIndex que se actualiza para formatear el
	 * codigo de salida. 
	 */
	private String tabPrint() {
		String s = "";
		for(int i=0; i<=tabIndex;i++){
			s += "\t";
		}
		return s;
	}
	
	/**
	 * Devuelve un string con el beginCol
	 * @return String beginCol.
	 */
	private String beginCol(){
		String s = "";
		if(colTable % 2 == 0) s = tabPrint() + "<td colspan=\""+ colspan + "\" class=\"normal\">";
		else s = tabPrint() + "<td colspan=\""+ colspan + "\">";
		tabIndex++;
		return s;
	}
	
	/**
	 * Devuelve un string con el endCol
	 * @return String endCol.
	 */
	private String endCol(){
		tabIndex--;
		return tabPrint() + "</td>";
	}
	
	/**
	 * Devuelve un string con el beginRow
	 * @return String beginRow.
	 */
	private String beginRow(){
		String s = tabPrint() + "<tr>";
		tabIndex++;
		return s;
	}
	
	/**
	 * Devuelve un string con el endRow
	 * @return String endRow.
	 */
	private String endRow(){
		tabIndex--;
		return tabPrint() + "</tr>";
	}
	
	/**
	 * Devuelve un string con el beginTable
	 * @return String beginTable.
	 */
	private String beginTable(){
		String s =  tabPrint() + "<table class=\"tabladatos\">";
		tabIndex++;
		return s;
	}

	/**
	 * Devuelve un string con el endTable
	 * @return String endTable.
	 */
	private String endTable(){
		tabIndex--;	
		return tabPrint() + "</table >";
	}

	public GrsPageContext getContext() {
		return context;
	}
	
	/**
	 * Escribe el titulo de la pagina.
	 * Este metodo termina por escribi un tag 'h1' en html.
	 * @param title
	 */
	public void title(String title) {
		write(String.format("<h1>%s</h1>\n", title));
	}
	
	/**
	 * Escribe un parrafo.
	 * Este metodo termina por escribi un tag 'p' en html.
	 * @param title
	 */
	public void p(String paragraph) {
		write(String.format("<p>%s</p>\n", paragraph));		
	}
	
	public void write(String s) {
		context.write(s);
	}

	public void request() throws Exception {
		try {			
			File jsfile = new File(context.processpath, context.jspage);
			Reader r = new FileReader(jsfile); //use jvm locale encoding
			Grs grs = new Grs(this);
			GrsEngine.eval(jsfile.getPath(), r, grs);
		} catch (Exception e) {
			e.printStackTrace();
			write("\n<pre>\n");
			write(GrsUtil.stackTrace(e));
			write("\n</pre>\n");
			throw e;
		} finally {
			context.writer.flush();			
		}
	}

	/**
	 * Agregar un mensaje a la lista de mensajes de la Pagina.
	 * Si format es null, limpia la lista de mensajes.
	 * @param format el formato tipo printf() del mensaje. Si es null, limpia la lista de mensajes.
	 * @param args argumentos del mensaje
	 * @return El mensaje formateado.
	 */
	public String message(String format, Object... args) {
		if (format == null)
			messages.clear();
		String msg = String.format(format, args);
		messages.add(msg);
		return msg; 
	}
	
	/**
	 * @return a copy of messages
	 */
	public String[] messages() {
		return messages.toArray(new String[messages.size()]); 
	}
	
	/**
	 * Lee el valor de un mapa y lo pasa String, si el valor es null,
	 * retorna "" 
	 */
	static public String toString(Map<?,?> map, Object key) {
		return toString(map, key, "");
	}

	/**
	 * Lee el valor de un mapa y lo pasa String, si el valor es null,
	 * retorna def 
	 */
	static public String toString(Map<?,?> map, Object key, String def) {
		Object o = map.get(key);
		if (o == null)
			return def;
		
		if (o instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			o = sdf.format((Date)o);
		}
				
		return o.toString().trim();
	}
}
