//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.ws.drei;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


public class MapaHelper {

	private String encoding;
	private Map<String, String> record = new TreeMap<String, String>();

	
	public MapaHelper(String xmlString, String encoding) throws Exception {
		byte[] bytes = xmlString.getBytes(encoding);
		InputStream in = new ByteArrayInputStream(bytes);
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(in);
	
		if (!encoding.equalsIgnoreCase(parser.getEncoding())) {
			throw new Exception("xml encoding must be:" + encoding);
		}
		
		setEncoding(encoding);
		
		int event; //event type
		while(parser.hasNext()) {
			event = parser.next();
			switch (event) {	
				case XMLStreamConstants.START_ELEMENT:
					String ename = parser.getLocalName();
					if (ename.equals("ric")) {
						//record.put("ric.name", parser.getAttributeValue(null, "name"));
						//record.put("ric.data_quality", parser.getAttributeValue(null, "data_quality"));
					}

					if (ename.equals("fid")) {
						record.put(parser.getAttributeValue(null, "id"), parser.getElementText());
					}
					break;
			}
		}
		
	}
	

	public MapaHelper(String encoding) throws Exception {
		setEncoding(encoding);
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


	public Integer getInteger(String key) {
		if (StringUtil.isNullOrEmpty(record.get(key)))
			return null;
		else
			return new Integer( (String) record.get(key));
	}

	public Long getLong(String key) {
		if (StringUtil.isNullOrEmpty(record.get(key)))
			return null;
		else
			return new Long( (String) record.get(key));
	}

	
	public String getString(String key) {
		if (StringUtil.isNullOrEmpty(record.get(key)))
			return null;
		else
			return record.get(key);
	}

	public Double getDouble(String key) {
		if (StringUtil.isNullOrEmpty(record.get(key)))
			return null;
		else
			return new Double( (String) record.get(key));
	}
	
	public Date getDate(String key) {
		if (StringUtil.isNullOrEmpty(record.get(key)))
			return null;
		else
			return DateUtil.getDate( (String) record.get(key), "dd/MM/yyyy");
	}

	
	
	
	public void setInteger(String key, Integer value) {
		if (value==null)
			record.put(key, "");
		else
			record.put(key, value.toString());
	}

	public void setLong(String key, Long value) {
		if (value==null)
			record.put(key, "");
		else
			record.put(key, value.toString());
	}

	
	public void setDouble(String key, Double value) {
		if (value==null)
			record.put(key, "");
		else
			record.put(key, NumberUtil.round(value, SiatParam.DEC_IMPORTE_VIEW).toString() );
	}
	
	// hacer bien
	public void setDate(String key, Date value) {
		if (value==null)
			record.put(key, "");
		else
			record.put(key, DateUtil.formatDate(value, DateUtil.ddSMMSYYYY_MASK) );
	}

	public void setString(String key, String value) {
		if (value==null)
			record.put(key, "");
		else
			record.put(key, value);
	}
	
	/**
	 * Recorre el mapa y arma un string XML
	 * @return
	 */
	public String getXML() {
		String resp = "";

		try {
			
			resp  ="<?xml version=\"1.0\" encoding=\"" + getEncoding() + "\" ?>";
			resp += "<ric>\n";
			
			for(String key : record.keySet()) {
				resp += "<fid id=\"" + key + "\">" + record.get(key) + "</fid>\n";
			}

			resp += "</ric>\n";

		} catch (Exception e) {
			return "Error en construccion de XML";
		
		}

		return resp.toString();
	}
	

	/**
	 * Recorre el mapa y arma un string XML
	 * @return
	 */
	public String getXMLHeader() {
		String resp = "";

		try {
			
			resp  ="<?xml version=\"1.0\" encoding=\"" + getEncoding() + "\" ?>\n";

		} catch (Exception e) {
			return "Error en construccion de XML";
		
		}

		return resp.toString();
	}

	
	
	public String getXMLRIC() {
		String resp = "";

		try {
			resp += "<ric>\n";
			for(String key : record.keySet()) {
				resp += "<fid id=\"" + key + "\">" + record.get(key) + "</fid>\n";
			}
			resp += "</ric>\n";

		} catch (Exception e) {
			return "Error en construccion de XML";
		
		}

		return resp.toString();
	}


	public String getXMLTRAMITE() {
		String resp = "";

		Long idTramite= getLong("ID-TRAMITE");
		try {
			resp += "<tramite id=\"" + idTramite + "\">\n";
			for(String key : record.keySet()) {
				resp += "<fid id=\"" + key + "\">" + record.get(key) + "</fid>\n";
			}
			resp += "</tramite>\n";

		} catch (Exception e) {
			return "Error en construccion de XML";
		
		}

		return resp.toString();
	}

	
	
}
