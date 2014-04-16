//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

/**
 * 
 */
package coop.tecso.adpcore;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author fedel
 *
 */
public class AdpExec {
	
	ClassLoader classLoader = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AdpExec exec = new AdpExec();
		
			exec.init();
			exec.test();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

	public void test() {
		try {
			// Use
			InitialContext ic = new InitialContext();
			
			DataSource ds = (DataSource) ic.lookup("java:comp/env/ds/indet");
			Connection con = ds.getConnection();

			PreparedStatement pstmt = null;
			ResultSet         rs    = null;

			String sql = "select sist_o, cuenta_o, clave_o, resto_o from indet_tot";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			List<String> ret = new ArrayList<String>();
			while (rs.next()) {
				String s;
					System.out.println(
							rs.getString("sist_o")+
							rs.getString("cuenta_o")+
							rs.getString("clave_o")+
							rs.getString("resto_o"));
			}
			try {rs.close();}    catch (Exception ex) {}
			try {pstmt.close();} catch (Exception ex) {}
			try {con.close();}   catch (Exception ex) {}
			
			Class classToLoad = Class.forName("ar.gov.rosario.siat.pas.buss.bean.Cuenta", true, classLoader);
			Method method = classToLoad.getDeclaredMethod("getById");
			Object instance = classToLoad.newInstance();
			Object result = method.invoke(instance, new Object[] {new Long(118L)});

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

	
	private void init() throws Exception {		
		Map<String, Map<String, String>> dsm = parseDatasoruces("/tmp/server.xml", "/tmp/context.xml");
		//classLoader = initClassLoader();
		bindDatasources(dsm);
	}
	
	
	private ClassLoader initClassLoader() throws Exception {
		// segun la documentacion de tomcat se carga el class loader:
		// BootStrap -> System -> Common -> Shared -> WebApp 
		
		//Tomcat Common ClassLoader
		List<File> files = new ArrayList<File>();
		files.add(new File(System.getProperty("catalina.home") + "/common/classes/"));
		files.addAll(Arrays.asList(new File(System.getProperty("catalina.home") + "/common/i18n").listFiles()));
		files.addAll(Arrays.asList(new File(System.getProperty("catalina.home") + "/common/endorsed").listFiles()));
		files.addAll(Arrays.asList(new File(System.getProperty("catalina.home") + "/common/lib").listFiles()));

		URL[] urls = new URL[files.size()]; int i = 0;
		for (File f : files) { urls[i++] = f.toURL(); System.out.println("common cl: " + f.getPath()); }
		URLClassLoader commonCL = new URLClassLoader(urls , ClassLoader.getSystemClassLoader());

		//Tomcat Shared ClassLoader
		files = new ArrayList<File>();
		files.add(new File(System.getProperty("catalina.base") + "/shared/classes"));
		files.addAll(Arrays.asList(new File(System.getProperty("catalina.base") + "/shared/lib").listFiles()));

		urls = new URL[files.size()]; i = 0;
		for (File f : files) { urls[i++] = f.toURL(); System.out.println("shared cl: " + f.getPath()); }
		URLClassLoader sharedCL = new URLClassLoader(urls, commonCL);
		
		//AdpSiat WebApp ClassLoader
		files = new ArrayList<File>();
		files.add(new File(System.getProperty("catalina.base") + "/webapps/adpsiat/WEB-INF/classes/"));
		files.addAll(Arrays.asList(new File(System.getProperty("catalina.base") + "/webapps/adpsiat/WEB-INF/lib").listFiles()));

		urls = new URL[files.size()]; i = 0;
		for (File f : files) { urls[i++] = f.toURL(); System.out.println("webapp cl: " + f.getPath()); }
		URLClassLoader webappCL = new URLClassLoader(urls, sharedCL);
		
		return webappCL;
	}
	
	
	
	private void bindDatasources(Map<String, Map<String, String>> dsm) throws Exception {
		  System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
		    "com.sun.jndi.fscontext.RefFSContextFactory");
		  System.setProperty(Context.PROVIDER_URL, "file:///tmp");
		  InitialContext ic = new InitialContext();


		  for(String resourceName : dsm.keySet()) {
			  Map<String, String> rm = dsm.get(resourceName);
			  
			  // Construct DriverAdapterCPDS reference
			  Reference cpdsRef = new Reference("org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS",
					  "org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS", null);
			  cpdsRef.add(new StringRefAddr("driver", rm.get("Resource_driverClassName")));
			  cpdsRef.add(new StringRefAddr("url", rm.get("Resource_url")));
			  cpdsRef.add(new StringRefAddr("user", rm.get("Resource_username")));
			  cpdsRef.add(new StringRefAddr("password", rm.get("Resource_password")));
			  System.out.println(rm.get("Resource_name"));
			  ic.rebind("java:comp/env/" + rm.get("Resource_name"), cpdsRef);
			  
			  // Construct PerUserPoolDataSource reference
			  Reference ref = new Reference("org.apache.commons.dbcp.datasources.PerUserPoolDataSource",
			    "org.apache.commons.dbcp.datasources.PerUserPoolDataSourceFactory", null);
			  ref.add(new StringRefAddr("dataSourceName", rm.get("ResourceLink_global")));
			  ref.add(new StringRefAddr("defaultMaxActive", rm.get("ResourceLink_maxActive")));
			  ref.add(new StringRefAddr("defaultMaxIdle", rm.get("ResourceLink_maxIdle")));
			  ref.add(new StringRefAddr("defaultMaxWait", rm.get("ResourceLink_maxWait")));
			  ref.add(new StringRefAddr("defaultMinIdle", rm.get("ResourceLink_minIdle")));
			  System.out.println(rm.get("ResourceLink_name"));
			  ic.rebind("java:comp/env/" + rm.get("ResourceLink_name"), ref);
		  }
		  
	}

	/**
	 * Parsea un archivo xml con formato server.xml de tomcat.
	 * Y crea las referencias de los drivers de conecciones a jdbc
	 * @param filename
	 */
	private Map<String, Map<String,String>> parseDatasoruces(String serverFilename, String contextFilename) throws Exception {
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
					System.out.println("ResourceLink_" + attr.getNodeName() + " " + attr.getNodeValue());
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

