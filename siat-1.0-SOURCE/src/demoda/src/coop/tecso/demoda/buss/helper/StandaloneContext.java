//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.helper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Ejemplo de uso:
 * 
 * 	Map<String, Map<String, String>> dsm = parseDatasoruces("/tmp/server.xml", "/tmp/context.xml");
 *  bindDatasources(dsm);
 *
 * @author fedel
 *
 */
public class StandaloneContext {
	
	static public void bindDatasources(Map<String, Map<String, String>> dsm) throws Exception {

		System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				  			"org.apache.naming.java.javaURLContextFactory");

		  InitialContext ic = new InitialContext();

		  ic.createSubcontext("java:");
		  ic.createSubcontext("jdbc");
		  ic.createSubcontext("jdbc/siat");
          ic.createSubcontext("java:comp");
          ic.createSubcontext("java:comp/env");
          ic.createSubcontext("java:comp/env/jdbc");
          ic.createSubcontext("java:comp/env/ds");

 		  for(String resourceName : dsm.keySet()) {

 			  Map<String, String> rm = dsm.get(resourceName);
			  
			  // Construct DriverAdapterCPDS reference
			  Reference cpdsRef = new Reference("org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS",
					  							"org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS", 
					  							null);

			  cpdsRef.add(new StringRefAddr("driver", 	rm.get("Resource_driverClassName")));
			  cpdsRef.add(new StringRefAddr("url", 	  	rm.get("Resource_url")));
			  cpdsRef.add(new StringRefAddr("user",     rm.get("Resource_username")));
			  cpdsRef.add(new StringRefAddr("password", rm.get("Resource_password")));
			
			  if (rm.get("Resource_name") == null) 
				  continue;

			  ic.bind("java:comp/env/" + rm.get("Resource_name"), cpdsRef);
			  ic.rebind(rm.get("Resource_name"), cpdsRef);

			  // Construct PerUserPoolDataSource reference
			  Reference ref = new Reference("org.apache.commons.dbcp.datasources.PerUserPoolDataSource",
					  						"org.apache.commons.dbcp.datasources.PerUserPoolDataSourceFactory", 
					  						 null);

			  ref.add(new StringRefAddr("dataSourceName",   rm.get("ResourceLink_global")));
			  ref.add(new StringRefAddr("defaultMaxActive", rm.get("ResourceLink_maxActive")));
			  ref.add(new StringRefAddr("defaultMaxIdle",   rm.get("ResourceLink_maxIdle")));
			  ref.add(new StringRefAddr("defaultMaxWait",   rm.get("ResourceLink_maxWait")));
			  ref.add(new StringRefAddr("defaultMinIdle",   rm.get("ResourceLink_minIdle")));

			  if (rm.get("ResourceLink_name") == null) 
				  continue;
	 
			  ic.rebind("java:comp/env/" + rm.get("ResourceLink_name"), ref);
		  }
		  
	}

	/**
	 * Parsea un archivo xml con formato server.xml de tomcat.
	 * Y crea las referencias de los drivers de conecciones a jdbc
	 * @param filename
	 */
	static public Map<String, Map<String,String>> parseDatasoruces(String serverFilename, 
			String contextFilename) throws Exception {

		Map<String, Map<String,String>> dsm = new HashMap<String, Map<String,String>>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		// open docContext, parse ResourcesLink
		// append context attributes to resources en dsm
		File file = new File(contextFilename);
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();			  
		
		NodeList nodes = doc.getElementsByTagName("ResourceLink");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;
				String resourceName = ele.getAttribute("global");
				Map<String, String> resourceMap = new HashMap<String, String>();
				
				if (!"javax.sql.DataSource".equals(ele.getAttribute("type"))) {
					continue;
				} 
				
				NamedNodeMap attributes = ele.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					Node attr =  attributes.item(j);
					resourceMap.put("ResourceLink_" + attr.getNodeName(), attr.getNodeValue());
				}
				dsm.put(resourceName, resourceMap);
			}
		}

		// open docServer, parse Resources
		// store Resource_attributes in dsm Map
		file = new File(serverFilename);
		doc = db.parse(file);
		doc.getDocumentElement().normalize();			  

		nodes = doc.getElementsByTagName("Resource");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;
				String resourceName = ele.getAttribute("name");
				Map<String, String> resourceMap = dsm.get(resourceName);
				
				if (resourceMap == null) {
					System.out.println("'" + resourceName + "' not found in server xml. Ignored");
					continue;
				}
				
				if (!"javax.sql.DataSource".equals(ele.getAttribute("type"))) {
					continue;
				} 

				NamedNodeMap attributes = ele.getAttributes();
				for (int j = 0; j < attributes.getLength(); j++) {
					Node attr =  attributes.item(j);
					resourceMap.put("Resource_" + attr.getNodeName(), attr.getNodeValue());
				}
			}
		}
		
		return dsm;
	} 

}
