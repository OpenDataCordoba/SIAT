//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.Options;
import org.apache.fop.apps.TraxInputHandler;
import org.apache.log4j.Logger;

import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * @author tecso
 *
 */
public class PrintModel extends Common {
	static private Logger log = Logger.getLogger(PrintModel.class);

	private static final long serialVersionUID = 1L;

	public static int RENDER_PDF = 1;
	public static int RENDER_TXT = 2;

	private int topeProfundidad = 0; 	// Profundidad maxima para generar al XML
	//private Object object;				// Object a imprimir
	private int renderer = RENDER_PDF; // Tipo de archivo a generar
	//private String fileOutPutName; // Nombre del archivo que se imprimira
	private String xslTxtString = null; //xsl para salida TXT
	private String xslPdfString = null;   //xsl para salida PDF
	private String xmlData = null;
	
	private String excludeFileName = "";
	private ArrayList excludeList = new ArrayList();
	private StringBuffer excludeXml = new StringBuffer();

	private Map<String, Tupla> cabecera = new HashMap<String, Tupla>();
	private Map<String, Tupla> formulario = new HashMap<String, Tupla>();
	private Object data;

	private String tituloReporte; // Para la impresion de los reportes
	private String fechaHoraActual = ""; // Para la impresion de los reportes, adecuadamente String
	private String blankCells; // Para la impresion, cuando en planilla es necesario tener filas en blanco

	private boolean deleteXMLFile = true; // Borrar o no el archivo XML que se genera temporalmente para la impresion.
	private long timeAppendXml= 0;
	
	private HashMap<String, Boolean> strIncludes = new HashMap<String, Boolean>();
	
	private boolean noAplicarTrim = false; // Para pasar los strings sin quitar espacios en blanco. 
	
	// Constructor
	public PrintModel(){
		putCabecera("FechaActual", DateUtil.formatDate(new Date(), DateUtil.ddSMMSYYYY_MASK));
		
	}
	
	// Getters y setters ******************************************************************************************
	public int getTopeProfundidad() {
		return topeProfundidad;
	}
	public void setTopeProfundidad(int topeProfundidad) {
		this.topeProfundidad = topeProfundidad;
	}

	public void putCabecera(String codigo, String valor) {
		cabecera.put(codigo, new Tupla(codigo, valor));
	}

	public void putCabecera(String codigo, String etiqueta, String valor) {
		cabecera.put(codigo, new Tupla(codigo, etiqueta, valor));
	}

	public void putFormulario(String codigo, String etiqueta, String valor) {
		formulario.put(codigo, new Tupla(codigo, etiqueta, valor));		
	}

	public String getFormularioValor(String codigo) {
		Tupla t = formulario.get(codigo);
		return t==null?"":t.valor;
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}  

	public int getRenderer() {
		return renderer;
	}
	public void setRenderer(int renderer) {
		this.renderer = renderer;
	}
	
	private String getFileSharePath() throws DemodaServiceException {
		String ret = null;
		try{
			ret =cabecera.get("FileSharePath").valor;
		}catch(Exception e){
			throw new DemodaServiceException("Se produjo un error buscando el FileSharePath en la cabecera del archivo XML", e);
		}
		return ret;
	}

	private void parseExcludeFile() throws Exception {
		if ("".equals(this.excludeFileName)) {
			return;
		}
		
		try {
			log.info("parseExcludeFile(): Leyendo " + this.excludeFileName);
			this.excludeList = new ArrayList();
			BufferedReader is = new BufferedReader(new FileReader(this.excludeFileName));
			String line = "";
			long c = 0;
			while ((line = is.readLine()) != null) {
				c++;
				line = line.trim();
				if (line.length()==0 || line.startsWith("#")) continue;
				try {
					Pattern p = Pattern.compile(line);
					this.excludeList.add(p);
				} catch (Exception e) {
					log.warn("parseExcludeFile(): ignorando linea " + c + ". " + e);
					System.out.println("parseExcludeFile(): ignorando linea " + c + ". " + e);
				}
			}
		} catch (Exception e) {
			log.warn("parseExcludeFile(): Fallo la lectura del achivo de exclusion: " + this.excludeFileName + ". " + e);
		}

		/*
		this.excludeXml = new StringBuffer();

		File fileIn = new File(this.excludeFileName);
		FileInputStream fileinputstream = new FileInputStream(fileIn);

		byte [] myByteArray = new byte [(int)fileIn.length()];
		fileinputstream.read(myByteArray);
		fileinputstream.close();

		String cadenaConvertida =  new String(myByteArray); 
		excludeXml.append(cadenaConvertida);
		*/
	}

	public String getBlankCells() {
		return blankCells;
	}
	public void setBlankCells(String blankCells) {
		this.blankCells = blankCells;
	}
	// Genera tags para el archivo XML de reportes, para generar filas en blanco.
	public void setBlankCells(int numberBlankCells) {
		String strBlank = ""; 
		for (int i=1; i <= numberBlankCells; i++){
			strBlank =  strBlank + "<Blank>" + i + "</Blank>\r\n"; 
		}

		putCabecera("blankCells", strBlank);
		this.blankCells = strBlank;
	}

	public String getFechaHoraActual() {
		return fechaHoraActual;
	}
	public void setFechaHoraActual(String fechaHoraActual) {
		putCabecera("fechaHoraActual", fechaHoraActual);
		this.fechaHoraActual = fechaHoraActual;
	}

	public String getTituloReporte() {
		return tituloReporte;
	}
	public void setTituloReporte(String tituloReporte) {
		putCabecera("fechaHoraActual", fechaHoraActual);
		this.tituloReporte = tituloReporte;
	}
	public StringBuffer getExcludeXml() {
		return excludeXml;
	}

	public boolean getDeleteXMLFile(){
		return this.deleteXMLFile;
	}	
	public void setDeleteXMLFile(boolean deleteXMLFile) {
		this.deleteXMLFile = deleteXMLFile;
	}

	public boolean isNoAplicarTrim() {
		return noAplicarTrim;
	}

	public void setNoAplicarTrim(boolean noAplicarTrim) {
		this.noAplicarTrim = noAplicarTrim;
	}

	/**
	 * Escribe la cabecera del printmodel en el stream
	 * @param buf
	 * @throws Exception
	 */
	public void writeDataBegin(Writer buf) throws Exception {		
		// si hay cargada info cruda, usamos esta info.
		buf.append("<?xml version = '1.0' encoding = 'ISO-8859-1'?>\n");
		buf.append("<siat>\n");
	
		buf.append("<cabecera>\n");
		renderMap(buf, cabecera);
		buf.append("</cabecera>\n");
	
		buf.append("<formulario>\n");
		renderMap(buf, formulario);
		buf.append("</formulario>\n");
	
		buf.append("<data>");
	}
	
	/**
	 * Escribe el String en el stream
	 * @param buf
	 * @param obj
	 * @throws Exception
	 */
	public void writeString(Writer buf, String str) throws Exception {
		buf.write(str);
	}

	/**
	 * Escribe el objecto serializado en el stream
	 * @param buf
	 * @param obj
	 * @throws Exception
	 */
	public void writeDataObject(Writer buf, Object obj, int profundidad) throws Exception {
		encodeXml(buf, obj, profundidad);		
	}

	public void writeDataObject(Writer buf, Object obj, int profundidad, String nombreMetodo) throws Exception {
		this.encodeXml(buf, obj, 0, profundidad, nombreMetodo, ""); 		
	}
	
	public void writeDataListObject(Writer buf, List listObj, int profundidad, String nombreMetodo) throws Exception {
		
		buf.append("\n" + getSpaces(profundidad) + "<"+ nombreMetodo +">");
		for (Object obj : listObj) {
			encodeXml(buf, obj, profundidad - 1); 		
		}
		buf.append("\n" + getSpaces(profundidad) + "</"+ nombreMetodo +">");
	}


	/**
	 * Escribe la parte final de en el stream.
	 * @param buf
	 * @throws Exception
	 */
	public void writeDataEnd(Writer buf) throws Exception {
		StringBuffer ret = new StringBuffer();
		ret.append("</data>\n");
		ret.append("</siat>");
		buf.write(ret.toString());
	}
	
	private void renderDataFile(File tmpFile) throws Exception {
		// creamos un file con el xml
	    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tmpFile), "ISO-8859-1");

		// si hay cargada info cruda, usamos esta info.
		if (xmlData == null) {
			writeDataBegin(osw);
			writeDataObject(osw, this.data, this.topeProfundidad);
			writeDataEnd(osw);
		} else {
			osw.write(xmlData);
		}
		osw.close();
	}

	// Metodos ******************************************************************************************
	public byte[] getByteArray() throws Exception {
		String unidad = File.separator.equals("\\") ? "c:\\" : "";
		File xmlTmpFile = new File(unidad + "/tmp/printmodel-" + UUID.randomUUID() + ".xml");
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		this.parseExcludeFile();
		renderDataFile(xmlTmpFile);
		fopRender(xmlTmpFile, out);
		
		return out.toByteArray();
	}
	
	private File createXslFile(String xsl) throws Exception {
		// Seteo el xml que indica que metodos no encodear
		String unidad = File.separator.equals("\\") ? "c:\\" : "";
		File xslTmpFile = new File(unidad + "/tmp/printmodel-" + UUID.randomUUID() + ".xsl");
	    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(xslTmpFile), "ISO-8859-1");
		String tmp = preprocesarXSL(xsl);
		osw.write(tmp);
		osw.close();
		return xslTmpFile;
	}

	public void fopRender(File inputXmlFile, OutputStream out) throws Exception {
		
		/* Para usar la nueva version de fop
		   El problema es que la nueva version de fop rompe el formato.

		// Step 1: Construct a FopFactory  con version nueva
		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		 
		try {
			// Construct fop with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
			
			// Setup XSLT
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xslTmpFile));
			
			// Set the value of a <param> in the stylesheet
			transformer.setParameter("versionParam", "2.0");
            
			// Setup input for XSLT transformation
			Source src = new StreamSource(inputXmlFile);
            
			// Resulting SAX events (the generated FO) must be piped through to FOP
			Result res = new SAXResult(fop.getDefaultHandler());
    
			// Start XSLT transformation and FOP processing
			transformer.transform(src, res);
		} finally {
			//Clean-up
			out.close();
		}
		*/

		File xslTmpFile = null;
		// Si es tipo PLAIN TXT vamos directo por xalan.
		if (renderer == RENDER_TXT) {
			xslTmpFile = createXslFile(xslTxtString);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer(new StreamSource(xslTmpFile));
			transformer.transform(new StreamSource(inputXmlFile), new StreamResult(out));
		//Si es PDF vamos por FOP
		} else if (renderer == RENDER_PDF) {
			//Leo el archivo de metricas y fuentes -- soporte fuentes ttf
			File userConfigFile = new File("/mnt/publico/general/reportes/userconfig.xml");
			new Options(userConfigFile);
			
			xslTmpFile = createXslFile(xslPdfString);
			TraxInputHandler input = new TraxInputHandler(inputXmlFile, xslTmpFile);
			Driver driver = new Driver();
			driver.setRenderer(this.renderer);
			driver.setOutputStream(out);
			input.run(driver);
		} else {
			throw new Exception("Tipo de Reporte Desconocido. Tipo=" + renderer + "Soporta: PDF=1, TXT=2");
		}

		// Elimino el archivo xml generado temporalmente
		if (this.deleteXMLFile){
			if (inputXmlFile != null) inputXmlFile.delete();
			if (xslTmpFile != null) xslTmpFile.delete();
		}
		
	}

	
	public void  fopRender(File xml, File pdf) throws Exception {
		OutputStream out = new java.io.FileOutputStream(pdf);
		fopRender(xml, out);
		out.close();
	}
	
	/*
	public void  fopRender(File xmlfile, File pdffile) throws Exception {
	try {
        System.out.println("FOP ExampleXML2PDF\n");
        System.out.println("Preparing...");

        // Setup input and output files
        String unidad = File.separator.equals("\\") ? "c:\\" : "";
        File xsltfile = new File(unidad + "/tmp/printmodel-" + UUID.randomUUID() + ".xsl");
	    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(xsltfile), "ISO-8859-1");
		preprocesarXSL();
		osw.write(xslPdfString);
		osw.close();
        
        System.out.println("Input: XML (" + xmlfile + ")");
        System.out.println("Stylesheet: " + xsltfile);
        System.out.println("Output: PDF (" + pdffile + ")");
        System.out.println();
        System.out.println("Transforming...");
        
        // configure fopFactory as desired
        FopFactory fopFactory = FopFactory.newInstance();

        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // configure foUserAgent as desired

        // Setup output
        OutputStream out = new java.io.FileOutputStream(pdffile);
        out = new java.io.BufferedOutputStream(out);
        
        try {
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup XSLT
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));
            
            // Set the value of a <param> in the stylesheet
            transformer.setParameter("versionParam", "2.0");
        
            // Setup input for XSLT transformation
            Source src = new StreamSource(xmlfile);
        
            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            log.debug("inicio Transform");
            transformer.transform(src, res);
            log.debug("fin Transform");
            
        } finally {
            out.close();
        }
        
        System.out.println("Success!");
    } catch (Exception e) {
        e.printStackTrace(System.err);
        System.exit(-1);
    }
	}
	*/

	//--------------------------------- encodeXML --------------------------------------//

	/**
	 * Reemplaza el "@@FileSharePath" por la variable que est� en FileSharePath del xml
	 * @throws DemodaServiceException 
	 */
	private String preprocesarXSL(String xslString) throws DemodaServiceException {
		String path = File.separator.equals("\\") ? "c:" : "";
		path += getFileSharePath();
		return replaceStr(xslString, "@@FileSharePath", path);		
	}
	/**
	 * Para cada elemento del Mapa
	 * forma varios renglones tipo: <Codigo etiqueta="ValorEtiqueta">Valor</Codigo>
	 */
	private void renderMap(Writer buf, Map<String, Tupla> map) throws Exception {
		for(String key: map.keySet()) {
			Tupla t = map.get(key);

			buf.append("    <"); 
			appendXml(buf, t.codigo); 
			if (!"".equals(t.etiqueta)) {
				buf.append(" etiqueta=\"");
				appendXml(buf, t.etiqueta);
				buf.append("\"");
			}
			buf.append(">");
			appendXml(buf, t.valor);
			buf.append("</"); appendXml(buf, t.codigo); buf.append(">\n");
		}
	}

	/**
	 * Agrega la cadena str escapando los caracteres de xml a sb 
	 * @param sb string buffer al cual agregar la cadena
	 * @param str cadena a escapar y agergar
	 */
	private void appendXml(Writer buf, String str) throws Exception {
		long time0 = System.currentTimeMillis();
		
		// TODO encodear caracteres especiales de xml
		String esc = "";
		// &amp;  	&
		// &lt; 	<
		// &gt; 	>
		// &quot; 	"
		// &apos; 	'
		esc = str.replace("&", "&amp;");
		esc = esc.replace("<", "&lt;");
		esc = esc.replace(">", "&gt;");
		esc = esc.replace("\"", "&quot;");
		esc = esc.replace("'", "&apos;");
		buf.append(esc);
		
		timeAppendXml += System.currentTimeMillis() - time0;
	}
	
	private void encodeXml(Writer buf, Object objeto, int profundidad) throws Exception {
		this.encodeXml(buf, objeto, 0, profundidad, "", ""); 
	}
	
	private void appendOpenTag(Writer buf, int profundidad, String tagName) throws Exception {
		buf.append(getSpaces(profundidad));
		buf.append("<");
		buf.append(tagName);
		buf.append(">");
	}

	private void appendCloseTag(Writer buf, int profundidad, String tagName) throws Exception {
		buf.append(getSpaces(profundidad));
		buf.append("</");
		buf.append(tagName);
		buf.append(">");	
	}

	private void encodeXml(Writer buf, Object vObject, int profundidad, int topeProfundidad, String nombreMetodo, String callStack) throws Exception {
		Method bOMethod, bOMethods[];
		int myProxProfundidad = new Long(profundidad).intValue() + 1;
		String simpleName = vObject.getClass().getSimpleName();
		
		if ("".equals(nombreMetodo)){
			buf.append("\n");
			appendOpenTag(buf, profundidad, simpleName);
			callStack += ":" + simpleName;
		} else {
			buf.append("\n");
			appendOpenTag(buf, profundidad, nombreMetodo);
			callStack += ":" + nombreMetodo;
		}

		// obtengo los metodos de esta clase (BO)
		bOMethods = vObject.getClass().getMethods();

		// Recorre los metodos
		for (int i = 0; i < bOMethods.length; i++) {
			bOMethod = bOMethods[i];
			String fieldName = replaceStr(bOMethod.getName(), "get", "");
			// si es un getter
			if (isGetter(bOMethod)) {
				// Checkeo si debo incluirlo en el xml, como metodo de clase general y como particular.
				if (incluir(simpleName, bOMethod, callStack)) {
					// analizaremosmos el tipo de datos que retorna y veremos...	                    
					if (isDemodaObject(bOMethod)) {
						if (isEnum(bOMethod)) {
							//Es una enumeracion de demoda (la representamos: id - valor)
							Object myObject = null;
							try {
								myObject = ((Object) bOMethod.invoke(vObject, (java.lang.Object[]) null));
							} catch (Exception e) {
								//log.warn("Falla llamada a invoke(): clase:" + vObject.getClass().getName() + " metodo:" + bOMethod.getName() + " error:" + e.toString());
							}
							if (myObject != null) {
								IDemodaEmun e = (IDemodaEmun) myObject;
								buf.append("\n");
								appendOpenTag(buf, profundidad + 1, fieldName);
								appendXml(buf, e.getId() + "-" + e.getValue());
								appendCloseTag(buf, 0, fieldName);
							} else {
								buf.append("");
							}
						} else if (profundidad < topeProfundidad) {
							// Si es un model componente
							encodeXmlVO(buf, vObject, bOMethod, myProxProfundidad, topeProfundidad, callStack ); 
						}
					} else if (isList(bOMethod) ) {
						if ( profundidad < topeProfundidad ) {
							// es un List que adentro espramos que tenga una lista de BO's
							encodeXmlList(buf, vObject, bOMethod, myProxProfundidad, topeProfundidad, callStack);
							//buf.append( encodeXmlList( vObject, bOMethod, myProxProfundidad, topeProfundidad) );
						}
					} else {
						// es un Integer, Long, Double, Date, etc.
						// Cambio para que no tire al xml lo atributos simples que no tienen valores.
						fieldName = replaceStr(bOMethod.getName(), "get", "");
						Object myObject = null;
						try {
							try {	
								myObject = ((Object) bOMethod.invoke(vObject, (java.lang.Object[]) null));
							} catch (Exception e) {
								//log.warn("Falla llamada a invoke(): clase:" + vObject.getClass().getName() + " metodo:" + bOMethod.getName() + " error:" + e.toString());
							}
							
							if (myObject != null){
								buf.append("\n");
								appendOpenTag(buf, profundidad+1, fieldName);
								if(noAplicarTrim)
									appendXml(buf, myObject.toString());
								else
									appendXml(buf, myObject.toString().trim());
							} else {
								buf.append("");
							}
						} catch (Exception e){
							System.out.println("[ERROR AL COPIAR] " + fieldName);
						}
						if (myObject != null) {
							appendCloseTag(buf, 0, fieldName);
						}
					}
				} // fin incluir
			} // fin isGetter
		}

		if ("".equals(nombreMetodo)) {
			buf.append("\n");
			appendCloseTag(buf, profundidad, simpleName);
			buf.append("\n");
		} else {
			buf.append("\n");
			appendCloseTag(buf, profundidad, nombreMetodo);
			buf.append("\n");
		}
	}
	
	/**
	 * solicita el XML correspondiente a un BO
	 * 
	 * @param bOMethod
	 * @param vOPasados
	 * @param profundidad
	 * @param topeProfundidad
	 * @param vOListaBlanca
	 */
	private StringBuffer encodeXmlVO(Writer buf, Object vObject, Method bOMethod, int profundidad, int topeProfundidad, String callStack) throws Exception { 
		StringBuffer sf= new StringBuffer();
		// obtengo el objeto del get
		Object getX = null;
		try {
			getX = bOMethod.invoke(vObject, (Object[]) null);
		} catch (Exception e) {
			//log.warn("Falla llamada a invoke(): clase:" + vObject.getClass().getName() + " metodo:" + bOMethod.getName() + " error:" + e.toString());
		}

		// si no es nulo, lo encodeamos y salimos
		if (getX != null) {
			String fieldName = replaceStr(bOMethod.getName(), "get", "");
			encodeXml(buf, getX, profundidad, topeProfundidad, fieldName, callStack); // , returnType
		}
		return sf;
	}


	private void encodeXmlList(Writer buf, Object vObject, Method bOMethod, int profundidad, int topeProfundidad, String callStack) throws Exception {
		// nomre de metodo, se quita el get
		String fieldName = replaceStr(bOMethod.getName(), "get", "");

		buf.append("\n"); appendOpenTag(buf, profundidad+1, fieldName);
		
		callStack += ":" + fieldName;
		// ahora obtenemos el set
		List propertyList = null;
		try {
			propertyList = (List) bOMethod.invoke(vObject, (Object[])null);
		} catch (Exception e) {
			//log.warn("Falla llamada a invoke(): clase:" + vObject.getClass().getName() + " metodo:" + bOMethod.getName() + " error:" + e.toString());
		}
		
		if (propertyList != null) {
			// para cada elemento del set
			for (Iterator it = propertyList.iterator(); it.hasNext();) {
				Object elem = (Object) it.next();

				if (isDemodaObject(elem.getClass())) {
					encodeXml(buf, elem, profundidad, topeProfundidad, "", callStack);
				}
			}
		}
		
		appendCloseTag(buf, 0, fieldName);
	}


	//--------------------------------- genericas --------------------------------------//


	/**
	 * Reemplaza el substring pattern por el replace en el String str
	 * 
	 * @param str
	 * @param pattern
	 * @param replace
	 * @return
	 */
	private String replaceStr(String str, String pattern, String replace) {
		return str.replace(pattern, replace);
	}

	/**
	 * Retorna true si el metodo comienza con: get
	 * 
	 * @param bOMethod
	 * @return
	 */
	private boolean isGetter(Method bOMethod) {
		if (bOMethod.getName().substring(0, 3).equals("get"))
			return true;
		else
			return false;
	}


	/**
	 * Devuelve true si el metodo retorna una enumeracion. 
	 * 
	 * @param bOMethod
	 * @return
	 */
	private boolean isEnum(Method method) {
		if (method.getReturnType().isEnum()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Retorna true si es un VO de Demoda
	 * 
	 * @param bOMethod
	 * @return
	 */
	private boolean isDemodaObject(Method bOMethod) {
		if (bOMethod.getReturnType().getName().indexOf("iface.model") > -1
				|| bOMethod.getReturnType().getName().indexOf("buss.bean") > -1) { 
		return true;
	} else { 
		return false;
	}
	}

	/**
	 * isDemodaBO
	 * 
	 * @param className
	 * @return
	 */
	private boolean isDemodaObject(Class className) {
		if (className.getName().indexOf("iface.model") > -1
				|| className.getName().indexOf("buss.bean") > -1) { 
			return true;
		} else { 
			return false;
		}
	}

	private boolean isList(Method bOMethod) {
		if (bOMethod.getReturnType().getName().indexOf("java.util.List") > -1) {
			return true;

		} else {
			return false;
		}
	}

	private String getSpaces(int i) {
		String ret="                                                                                                                              ";
		if (i >= ret.length()) return ret;
		return ret.substring(0,i);
	}

	/** Retorna true el metodo debe ser incluido en el XML, y false si no.
	 * 
	 * @param bOMethod
	 * @return
	 */
	private boolean incluir(String className, Method bOMethod, String callStack) throws Exception {
		long time0 = System.currentTimeMillis();
		String methodName = bOMethod.getName().substring(3);
		callStack += ":" + methodName;
		if (methodName.indexOf("Enabled") > -1)
			return false;

		if (strIncludes.containsKey(callStack)) {
			log.debug("incluir: cache: ms=" + (System.currentTimeMillis() - time0));
			return strIncludes.get(callStack);
		}
		
		boolean incluir = true;
		for(Object o: this.excludeList) {
			Pattern pattern = (Pattern) o;		   
			Matcher matcher = pattern.matcher(callStack);			
			if (matcher.find())  {
				incluir = false;
				break;
			}
		}
		
		//if (!this.deleteXMLFile) {
		//	System.out.println("PrintModel:incluir():" + callStack + (incluir ? " OK" : " EXCLUIDA"));
		//}
		
		//almacenamos el valor calculado en el cache.
		strIncludes.put(callStack, incluir);
		log.debug("incluir: ms=" + (System.currentTimeMillis() - time0));

		return incluir;
	}

	//--------------------------------- FIN encodeXml --------------------------------------//

	private class Tupla {
		public Tupla(String codigo, String etiqueta, String valor) {
			this.codigo = codigo;
			this.etiqueta = etiqueta;
			this.valor = valor;
		}

		public Tupla(String codigo, String valor) {
			this.codigo = codigo;
			this.etiqueta = "";
			this.valor = valor;
		}

		public String codigo;
		public String etiqueta;
		public String valor;
	}

	public String getExcludeFileName() {
		return excludeFileName;
	}
	public void setExcludeFileName(String excludeFileName) throws Exception {
		this.excludeFileName = new File(getFileSharePath() + "/" + excludeFileName).getPath();
		if (File.separator.equals("\\")) {
			this.excludeFileName = "c:\\" + this.excludeFileName;
		}
		this.parseExcludeFile();
	}
	public String getXmlData() {
		return xmlData;
	}
	public void setXmlData(String xmlData) {
		this.xmlData = xmlData;
	}
	
	public String getXslPdfString() {
		return xslPdfString;
	}
	public void setXslPdfString(String xslPdfString) {
		this.xslPdfString = xslPdfString;
	}

	public String getXslTxtString() {
		return xslTxtString;
	}
	public void setXslTxtString(String xslTxtString) {
		this.xslTxtString = xslTxtString;
	}
	
	/**
	 * Carga la propiedad XslPdfString del PrintModel a partir de un archivo
	 * @param pathArchivo
	 * @throws Exception
	 */
	public void cargarXsl(String pathArchivo, int render) throws Exception{
		if(log.isDebugEnabled()) log.debug("cargaXslPdfString a partir del pathArchivo");
		if(log.isDebugEnabled()) log.debug("pathArchivo: " + pathArchivo);
		
		// obtencion del xsl a partir de un archivo
		StringBuffer sf = new StringBuffer();
		BufferedReader bufferedReader = new BufferedReader(
				new FileReader(pathArchivo));
		String line = "";         
		while ((line = bufferedReader.readLine()) != null) {
			sf.append(line);
		}
		bufferedReader.close();
		
		if (render == RENDER_TXT) {
			this.setXslTxtString(sf.toString());
		} else {
			this.setXslPdfString(sf.toString());
		}
	}
	
}


/*

byte[] content = out.toByteArray();

response.setContentLength(content.length);
response.setContentType("application/pdf");
response.getOutputStream().write(content);
response.getOutputStream().flush();

response.setHeader("Content-Disposition", "attachment; filename=\"" + this.fileOutPutName + "\"");
 */




