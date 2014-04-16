//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.classic.Session;

import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Clases util que de acceso a hibernate y chequeos de registros. 
*/
public class SweHibernateUtil {
	static private Logger log = Logger.getLogger(SweHibernateUtil.class);

    static private SessionFactory sessionFactory;
    static private ThreadLocal<Session> session = new ThreadLocal<Session>();
	static private String HIBERNATE_CFG_RESOURCE = "/swe.hibernate.cfg.xml";

    static {
        try {
         	 // Create the SessionFactory
            AnnotationConfiguration cfg = new AnnotationConfiguration();            ;
            sessionFactory = cfg.configure(HIBERNATE_CFG_RESOURCE).buildSessionFactory();
            
        } catch (Throwable ex) {
        	ex.printStackTrace();
        	// Make sure you log the exception, as it might be swallowed
            System.out.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session currentSession() throws HibernateException {
    	
        Session s = session.get();
       
        if (s == null) {
//                        //Throwable e = new Exception().fillInStackTrace();
//                        StringBuffer sb = new StringBuffer();
//                        //sb.append(coop.tecso.demoda.buss.dao.JDBCConnManager.dataSourceInfo("java:comp/env/ds/gmi")).append("-");
//                        sb.append(coop.tecso.demoda.buss.dao.JDBCConnManager.dataSourceInfo("java:comp/env/ds/swe")).append("-");                     
//                        log.info(sb.toString());
                s = sessionFactory.openSession();
                session.set(s);
        }
              

        
        return s;
    }
    
    /**
     *  Obtiene la session de Hibernate y se setea el modo AutoFlush cada "maxTransactionToFlush" transacciones.
     *  Donde "maxTransactionToFlush" es pasado como parametro.
     *
     * @param maxTransactionToFlush
     * @return
     * @throws HibernateException
     */
    public static Session currentSession(Long maxTransactionToFlush) throws HibernateException {       
        Session s = session.get();
        if (s == null) {
                s = sessionFactory.openSession();
                session.set(s);
        }
        return s;  
    }

    public static void closeSession() {
    	try {
	    	Session s = session.get();
	        if (s != null) {
				try {
					s.close();
				} catch (Exception e) {
					s = null;
				}
			}
	    	session.set(null);
    	} catch (Exception e) {
    		System.out.println("ATENCION: Si ve esto, significa que pueden existir sesiones sin cerrar.");
    	}
    }

	/**
	 * @deprecated Usar hasReference de GenericAbstractDAO
	 * Retorna true si el parametro bo, posee hijos de la clase joinClass, relacionados mediante el parametro joinProperty.
	 * Este metodo requiere que exista previamente los mapeos de hibernate adecuados.
	 * <p>Ej: Para saber si una aplicacion posee usuarios asignados:
	 * <code>hasReference(apl, UsrApl.class, "aplicacion");</code>
	 * <br>donde apl es la instancia de la aplicacion que se desea saber si tiene o no usuarios.
	 * <br>donde UsrApl.class es la clase hija
	 * <br>"aplicacion" es el nombre de la propiedad que relaciona el hijo con el padre.
	 * <p>IMPLEMENTACION: este metodo simplemente terminado ejecutando el query: from UsrApl u where u.aplicacion = :apl
	*/
	static public boolean hasReference(BaseBO bo, Class joinClass, String joinProperty) {
		String queryString = "from %s t where t.%s = :bo";
		Session session = SweHibernateUtil.currentSession();
		Query query = session.createQuery(String.format(queryString, joinClass.getName(), joinProperty));
		query.setEntity("bo", bo);
    	List list = (List) query.list();
		return list == null || list.isEmpty() ? false : true;
	}

	/**
	 * @deprecated Usar hasReference de GenericAbstractDAO
	 */
	static private Object invokeGetter(String propName, Object obj) throws Exception {
		String getterName = "get" + propName.substring(0,1).toUpperCase() + propName.substring(1);
		return obj.getClass().getMethod(getterName).invoke(obj, (Object[]) null);
	}

	/* Solo llamarla al destruir contexto de app */
    public static void closeSessionFactory() {
    	sessionFactory.close(); // Free all resources
    }
}
