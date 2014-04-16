//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import coop.tecso.demoda.buss.bean.BaseBO;

public class TestBean {
		
	String errorCause;

	public static int TEST_PROF0 = 0;  
	public static int TEST_PROF1 = 1;  
	public static int TEST_LIST = 2;  
	public static int TEST_SEQUENCE = 4;  
	
	public static void main(String[] args) throws Exception {
		System.out.println("TestBean\n\n");
		
		TestBean main = new TestBean();		
		Alone.init(true, true);
		
		main.beginTest(4);
		
	}
	
	private void beginTest(int testLevel) throws Exception {
		
		//Especificar ruta donde se encuentra archivo de mapeo
		//TODO: ver forma de obtener path mediante alguna ruta absoluta del contexto
		String pathHbmCfgFile = "../../view/src/WEB-INF/src/siat.hibernate.cfg.xml";
				
		//Contadores
		int successCount = 0;
		int failCount  = 0;	
		int totalCount = 0;
		
		List<String> classPathList = parseHbmMappingFile(pathHbmCfgFile);
		System.out.println("\n##INICIO TEST"+ testLevel);
		System.out.println("-----------------------------------------------\n");
		for (String path : classPathList) {
			
			SiatHibernateUtil.currentSession();		
			totalCount++;

			if (testLevel == TEST_PROF0 || testLevel == TEST_PROF1) {
				if (testBean(path, testLevel)) {
					System.out.println(totalCount +"  --EXITO - mapping path: " + path);
					successCount++;
				} else {
					System.out.println(totalCount +"  @@FALLA - mapping path: " + path);
					System.out.println(errorCause);
					failCount++;
				}
			}

			if (testLevel == TEST_SEQUENCE) {
				if (testSecuencia(path)) {
					System.out.println(totalCount +"  --EXITO - mapping path: " + path);
					successCount++;
				} else {
					System.out.println(totalCount +"  @@FALLA - mapping path: " + path);
					System.out.println(errorCause);
					failCount++;
				}
			}
			
			SiatHibernateUtil.closeSession();  
			
		}
	
		System.out.println("\n-----------------------------------------------");
		System.out.println("## FIN TEST"+ testLevel);
		System.out.println("  Sumario - TOTAL:"+totalCount+" | EXITO:"+successCount+" | FRACASO:"+failCount);

	}
	
	private boolean testBean(String classPath, int testLevel){		
		
		Class mappingClass;
		BaseBO bean;
		
		//Obtengo el nombre del Bean respecto al path
		//Busco la instancia con menor Id del Bean 
		try {
			mappingClass = Class.forName(classPath);
			bean = getBeanHasMinId(mappingClass.getName());			
		} catch (ClassNotFoundException e) {
			errorCause = "	**ERROR: no existe clase con path " + classPath+"\n";
			errorCause += "	¨¨_excepcion:" + e.toString();	
			return false;
		}
				
		//Verifico si existe una instancia de ese Bean
		if (null == bean) {
			errorCause ="	**ERROR: no se obtuvo una instancia de " + mappingClass.getName();			
			return true;
		}
				
		try {
			
			bean.toVO(testLevel,false);
		} catch (Exception e) {			
			errorCause = "	**ERROR: no pudo realizar toVO de clase " + mappingClass.getName()+"\n";
			errorCause += "	¨¨_excepcion:" + e.toString();
			
			return false;
		}	
		
		return true;
	}
	
	
	private boolean testSecuencia(String classPath) {
		
		Class mappingClass;		  
		Annotation annotations[];
		
		String annotationStr;		  
		String tableName = null;
		String generatorName = null;
		String sequenceName = null;
		String generatedValue = null;
		
		   try {
		      // Cargamos la clase
			  mappingClass = Class.forName(classPath); 			  
			  annotations = mappingClass.getDeclaredAnnotations();
			  
			  for (Annotation annotation : annotations) {
				 
				  annotationStr = annotation.toString();	

				  //Obtengo name de @Table
				  if (annotation.getClass().equals(Table.class)) {				  				  
					  tableName = annotationStr.substring(annotationStr.indexOf("name=") + 5, annotationStr.indexOf(")"));					
				  }		
				  //Obtengo name y sequenceName de @SequenceGenerator
				  if (annotation.getClass().equals(SequenceGenerator.class)) {
					  int index = annotationStr.indexOf("sequenceName=") + 13;
					  generatorName = annotationStr.substring(annotationStr.indexOf("name=") + 5, annotationStr.indexOf(","));
					  sequenceName  = annotationStr.substring(index, annotationStr.indexOf(",", index));					
				  }
				  //Obtengo generator de @GeneratedValue
				  if (annotation.getClass().equals(GeneratedValue.class)) {
					  generatedValue = annotationStr.substring(annotationStr.indexOf("generator=") + 10, annotationStr.indexOf(","));
				  }
				  
			  }
			        		      
			  if (null == sequenceName) {				 
				 errorCause ="	**ERROR: no se obtuvo sequenceName de @SequenceGenerator";
				 return false;				  
			  }
			  
			  if (!generatedValue.equals(generatorName)) {
				 errorCause ="	**ERROR: @GeneratedValue generator distinto de @SequenceGenerator name"; 
				 return false;
			  }
			  		  		
			  
			  Session session = SiatHibernateUtil.currentSession();
			  
			  //Consulta simple para verificar si existe la tabla
			  String sqlQuery = "SELECT * FROM " + sequenceName;				
			  Query query = session.createSQLQuery(sqlQuery);			  
			  try {
				  	if (null == query.uniqueResult()) {
					    errorCause ="	**ERROR: No existe secuencia "+ sequenceName + " en "+ tableName ; 
					    return false;				
				  		}
			  		} catch (HibernateException e) {
			  			errorCause ="	**ERROR: Error en secuencia "+ sequenceName + " de "+ tableName; 				
			  			errorCause += "	¨¨_excepcion:" + e.toString();
			  			return false;	
			  		}	  
		
		      
		   		} catch (Exception e) {
		   			errorCause ="	**ERROR: No se ha encontrado la clase "+ classPath; 				
		  			errorCause += "	¨¨_excepcion:" + e.toString();		
		  			return false;
		     }		    
		    
		   	 return true;
		   	 
	}
	
	private BaseBO getBeanHasMinId(String className) {
		
		BaseBO beanResult = null;
		String subQuery = "SELECT MIN (c.id) from " + className + " c";		
		String queryString = "FROM "+ className + " t WHERE t.id = ( " + subQuery + " )";	
		
		try {
			Session session = SiatHibernateUtil.currentSession();
			Query query = session.createQuery(queryString);
			beanResult = (BaseBO) query.uniqueResult();			
		} catch (Exception e) {
			errorCause = "	**ERROR: falló getBeanHasMinId \n";
			errorCause += "	¨¨_excepcion:" + e.toString();
		}
		
		return 	beanResult;	
		
	}
	
	
	
	private List<String> parseHbmMappingFile(String pathFile) throws Exception {
		
		List<String> classPathList = new ArrayList<String>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		// open hibernate mapping file, parse ClassPaths
		// append mapping attributes to Bean class path
		File file = new File(pathFile);
		Document doc = db.parse(file);
		doc.getDocumentElement().normalize();
		
		NodeList nodes = doc.getElementsByTagName("mapping");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) node;			
				classPathList.add(ele.getAttribute("class"));				
			}									
			
		}
					
		return classPathList;	
	}
}
