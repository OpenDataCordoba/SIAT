//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.bean;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.Vigencia;


/**
 * Esta es de la que debieran hereadar los BO que
 * no poseen id autogenerados.
 * 
 */
@SuppressWarnings("unchecked")
@MappedSuperclass
public class BaseBean extends Common {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "usuario")
    private String    usuarioUltMdf; // Usuario que ingresa o modifica los datos de la base

	@Column(name = "fechaUltMdf")
	private Date      fechaUltMdf; // Fecha de ultima modificacion en la base

	@Column(name = "estado")
	private Integer    estado; // Estado en la base
    
    @Transient
	private Log log = LogFactory.getLog(BaseBean.class);

    @Transient
    private Long errCode= new Long(0); // codigo de error para ser propagado al servicio.
    
    
    /* Atribitos utlilizados para los objetos con vigencia,
     * que son aquellos valisdos en un rango de fechas.
    */

    public BaseBean() {
    	usuarioUltMdf = "siat";
        fechaUltMdf = new Date();
        estado = new Integer(1);
    }

    public BaseBean(Long id) {
    	this();
    }
    
    
    
	/**
	 * Metodo que puede ser implementado o no segun, la clase posea caso.
	 * Es utilizado en el servicio de registro de uso de expedientes.
	 * 
	 * @author Cristian
	 * @return
	 */
	public String getIdCaso() {
		return null;
	}
	
	
	
	
    //--------------------------------- toVO --------------------------------------//    
    /**
     * Por defecto profundidad=0 y copiarListas=true
     * 
     * Asumimos: <p>Que para todo BO en: ar.gov.rosario.siat.modulo.buss.bean.xx existe un
     * VO: ar.gov.rosario.siat.modulo.iface.model.xxVO.</p> 
     * <p>Que los getter/setter que dominan el pasaje son los definidos para el VO.</p> 
     * <p>Que se llaman igual en ambos objetos.</p>
     * <p>La unica diferencia es que en el Set del VO, en la firma del metodo, se hace referencia a un VO.</p> 
     * <p>Que si para un getter del BO, no se encuentra en el VO un setter homonimo, la propiedad correspondiente,
     * no se pasa.</p>
     * 
     * 
     * <p>Procedimiento:</p>
     * <p> . Obtiene el VO asociado al BO por el nombre.</p>
     * <p> . obtenemos los getters() son los metdos que comienzan con getXXX()</p>
     * <p> . para cada uno</p>
     * <p> 		. obtenemos el class del tipo que retorna</p>
     * <p>      . analizamos de que se trata, en tres grandes grupos</p>
     * <p>      . si es un TipoSimple (Long,String,Date,Integer, etc.) => se lo pasa al vO // vo.setXXX(this.getXXX());</p>
     * <p>      . si es otro BO => le ejecuta el .toVO() y obtiene el VO => se lo pasa al vO vo.setXXX(vo);</p>
     * <p> . si es un Set (o algo que implementa un List) => invoca: colToVO(con el List) . copia la colVO obtenida al VO // vo.setXXX()</p>
     */
    public Object toVO() throws Exception {
    	// default, profundidad=0, copiarListas=true
    	return this.toVO(new ArrayList(), new ArrayList(), 0, 0, true);
    }
    
    /**
     * Por defecto profundidad = 0.
     * 
     * @param copiarList
     * @return
     */
    public Object toVO(boolean copiarList) throws Exception {
    	// default, profundidad 0
    	return this.toVO(new ArrayList(), new ArrayList(), 0, 0, copiarList);
    }
    
    /**
     * Por defecto copiarLista=true
     * 
     * @param topeProfundidad
     * @return
     */
    public Object toVO(int topeProfundidad) throws Exception {
    	// la profundidad que se le pase
    	return this.toVO(new ArrayList(), new ArrayList(), 0, topeProfundidad, true);
    }
    
    /**
     * Ambos parametros seteables 
     * 
     * @param topeProfundidad
     * @param copiarList
     * @return
     */
	public Object toVO(int topeProfundidad, boolean copiarList) throws Exception {
    	// la profundidad que se le pase
    	return this.toVO(new ArrayList(), new ArrayList(), 0, topeProfundidad, copiarList);
    }
    
    
    public Object toVO(Collection vOListaBlanca, int topeProfundidad) throws Exception {
    	// la profundidad que se le pase
    	return this.toVO(vOListaBlanca, new ArrayList(), 0, topeProfundidad, true);
    }
        
    public Object toVO(Collection vOListaBlanca, int topeProfundidad, boolean copiarList) throws Exception {
    	// la profundidad que se le pase
    	return this.toVO(vOListaBlanca, new ArrayList(), 0, topeProfundidad, copiarList);
    }
    
    public Object toVO(Collection vOListaBlanca, Collection vOPasados, int profundidad, int topeProfundidad, boolean copiarList) throws Exception {
    	String funcName = this.getClass().getName() + ".toVO()";
    	
    	log.debug("|" + getSpaces(profundidad) + "## " + funcName + " - profundidad: " + new Long(profundidad ).toString() + " - tope: " + new Long(topeProfundidad).toString()  );
    	
    	/*if(((copiarList && topeProfundidad>1) || topeProfundidad > 2) &&  profundidad == 0)
    		log.info("Atencion!: " + funcName + " topeProfundidad: " + new Long(topeProfundidad).toString() + " copiarListas: "+ copiarList);
    	*/
    	
        String vOName;
        Class vOClass;
        Object vOObject;

        Method bOMethod, bOMethods[];
        
        Collection myPasados = new ArrayList();
        for (Iterator it=vOPasados.iterator();it.hasNext();) {
        	myPasados.add(new String((String)it.next() ) );
        }
        
        int myProxProfundidad = new Long(profundidad).intValue() + 1;
        
        try {
        	
        	// si no existe en la lista de los ya pasados, lo agrega
        	if ( !existeEnCol(vOPasados, this.getClass().getName()) && 
        		 !isCommonKey(this.getClass()) &&
        		 !existeEnCol(vOListaBlanca, this.getClass().getName()) )  {
        		
        		//vOPasados.add(this.getClass().getName());
        		myPasados.add(this.getClass().getName() );
        		//log.debug(funcName + "agrega a la col: " + this.getClass().getName() );

        	}
        	
        	
            // obtenemos el nombre del VO relacionado
            // ar.gov.rosario.aps.bus.bean.xx =>
            // ar.gov.rosario.aps.iface.model.xxVO
            vOName = replaceStr(this.getClass().getName(), "buss.bean", "iface.model") + "VO";
            // obtenemos una nueva instancia del vo
            vOClass = Class.forName(vOName);
            // obtenemos una nueva instancia del vo
            vOObject = vOClass.newInstance();

            
            
            // recorremos los metodos de esta clase (BO)
            bOMethods = this.getClass().getMethods();
            for (int i = 0; i < bOMethods.length; i++) {
                bOMethod = bOMethods[i];
                //System.out.println("## " + bOMethod.getName());

                
                // si es un getter y no es "static"
                if (isGetter(bOMethod)) {

                    //log.debug(funcName + " Metodo: " + bOMethod.getName() + " retorna: " + bOMethod.getReturnType().getName());

                    // analizaremosmos el tipo de datos que retorna y veremos...
                    if (isDemodaBO(bOMethod)) {

                    	//log.debug("## es DemodaBO - " + bOMethod.getName()  + " - profundidad: " + new Long(profundidad ).toString() + " - tope: " + new Long(topeProfundidad).toString()  );

                    	if (!existeEnCol(vOPasados, bOMethod.getReturnType().getName())) {
                    		//log.debug("no existe en col");

                    		if (profundidad<topeProfundidad) {
                    			//log.debug("mandamos a copiar con profundidad++");
                    			
                    			// es un BO
                    			copyBO(bOMethod, vOClass, vOObject, myPasados, myProxProfundidad, topeProfundidad, vOListaBlanca, copiarList);
                    		}
                    	}

                    } else if (isSet(bOMethod)) {
                    	
                    	if (profundidad< topeProfundidad ) {
                    		// es un Set que adentro puede tener BOs, Integers,
                    		copySet(bOMethod, vOClass, vOObject, myPasados, myProxProfundidad, topeProfundidad, vOListaBlanca, copiarList);
                    	}
                    	
                    } else if (isList(bOMethod)  ) {
                    	
                    	if ( (profundidad < topeProfundidad) && copiarList) {
                    		// es un List que adentro puede tener BOs, Integers, etc., Sets
                    		//log.debug("entramos al isList con: " + bOMethod.getName());
                    		copyList(bOMethod, vOClass, vOObject, myPasados, myProxProfundidad, topeProfundidad, vOListaBlanca, copiarList);
                    	}
                    	
                    } else if (isEnum(bOMethod, vOObject)) { // Copiado especial para el caso de las enumeraciones 
                    	
                    	// Dado el valor del BO buscamos la enumeracion correspondiente en el VO 
                    	setEmunById(bOMethod, vOClass, vOObject);
                    	                    
                    } else {
                    	//log.debug("## copia la propiedad simple: " + bOMethod.getName() + "retorna: " + bOMethod.getReturnType().getName() );
                        // es un Integer, Long, Double, Date, etc.
                        copySimple(bOMethod, vOClass, vOObject);

                    }
                }
            }
            return vOObject;
        } catch (Exception e) {
            //System.out.println("Excepcion: " + e);
			if (e instanceof org.hibernate.HibernateException) {
				throw e;
			}
            return null;
        }
    }

    /**
     * 
     * Dada un propiedad de un BO, que es un objeto de negocios (otro BO), .
     * obtiene el VO asociado a la propiedad . la instancia . la copia al VO
     * 
     * @param bOMethod
     * @param vOClass
     * @param vOObject
     */
    private void copySimple(Method bOMethod, Class vOClass, Object vOObject) {

        try {
        	
        	//log.debug("#############  copySimple ############# " );        	
        	//log.debug("##### copySimple: " + bOMethod.getName() + "()");
        	
            // obtenemos el nombre del metodo set del VO que tenemos que
            // ejecutar
            String vOMetSetName;
            vOMetSetName = replaceStr(bOMethod.getName(), "get", "set");

            
            // indicamos que el tipo de datos del parametro del set, es
            // justamente el que retorna el get
            Class[] classesParamSet;
            classesParamSet = new Class[1];
            classesParamSet[0] = Class.forName(bOMethod.getReturnType().getName());

            // ahora obtenemos el metodo set del VO con el nombre y los
            // parametros que tenemos
            Method vOMetSet;
            vOMetSet = vOClass.getMethod(vOMetSetName, classesParamSet);
            
            
            // ahora, obtenemos el parametro del set
            Object[] paramSet;
            paramSet = new Object[1];
            paramSet[0] = bOMethod.invoke(this, (Object[]) null); // le paso lo que me
                                                        // devuelve el get de
                                                        // this
            // ahora, finalmente, invocamos el set del VO
            vOMetSet.invoke(vOObject, paramSet);

            //log.debug("valor asignado correctamente al vO." + vOMetSetName);
			log.debug("##### copySimple: " + bOMethod.getName() + "() ok");
        } catch (Exception e) {
            // no se hace nada
			log.debug("##### copySimple: " + bOMethod.getName() + "() fail");
        	//e.printStackTrace();

        }
    }
    
    private void setEmunById(Method bOMethod, Class vOClass, Object vOObject) {
    	try {
    		String voMetGetName =  bOMethod.getName();
	     	Method voMetGet = vOClass.getMethod(voMetGetName);
	     	
	         // obtenemos el nombre del metodo set del VO que tenemos que
	         // ejecutar
	         String vOMetSetName;
	         vOMetSetName = replaceStr(bOMethod.getName(), "get", "set");
	         
			 // indicamos que el tipo de datos del parametro del set, es
			 // justamente el que retorna el get
			 Class[] classesParamSet;
			 classesParamSet = new Class[1];
			 classesParamSet[0] = Class.forName(voMetGet.getReturnType().getName());
			 
	         // ahora obtenemos el metodo set del VO con el nombre y los
	         // parametros que tenemos
	         Method vOMetSet;
	         vOMetSet = vOClass.getMethod(vOMetSetName, Class.forName(voMetGet.getReturnType().getName()));
	         
	         // ahora, obtenemos el parametro del set
	         Object[] paramSet;
	         paramSet = new Object[1];
	         paramSet[0] = bOMethod.invoke(this, (Object[]) null); // le paso lo que me
	                                                     // devuelve el get de
	                                                     // this
	         
	        Method getByIdMethod = classesParamSet[0].getMethod("getById", Class.forName("java.lang.Integer"));
	         
	        //log.debug(funcName + " paramSet[0] intValue: " + ((Integer)paramSet[0]).intValue());
	        
	        Object enu = getByIdMethod.invoke(classesParamSet[0], paramSet[0]);
                         
	         // ahora, finalmente, invocamos el set del VO
	         vOMetSet.invoke(vOObject, enu);
	
	         //log.debug("valor asignado correctamente al vO." + vOMetSetName);
	
	     } catch (Exception e) {
	        // no se hace nada
	     	log.error("##### setEmunById - error al intentar setear la enumeracion byId" );
	     	e.printStackTrace();
	     	//e.printStackTrace();
	
	     }	
    
    }
    
    /**
     * Obj: Devolver un Set listo para ser pasado a un VO
     *  . Para cada elemento de la lista
     *  . Si es un TipoSimple (Long,String,Date,Integer, etc.) . instancia un
     * elemento del mismo tipo . lo agrega al Set de salida
     *  . Si es otro BO (que hay que ver como hacemos para saberlo, la mejor
     * parece ser implementar una interface vacia) . ejecuta un .toVO() . lo
     * agrega a la lista de salida
     *  . Si es un Set . invoca: colToVO() . copia la colVO obtenida al VO
     * 
     * --- copySet(bOMethod, vOClass, vOObject);
     */
    private Set copySet(Method bOMethod, Class vOClass, Object vOObject, Collection vOPasados, int profundidad, int topeProfundidad, Collection vOListaBlanca, boolean copiarList) {
        try {
            Set ret = new HashSet();
            
            //log.debug("#############  copySet ############# " );
            		
            // obtenemos el nombre del metodo set del VO que tenemos que
            // ejecutar
            String vOMetSetName = replaceStr(bOMethod.getName(), "get", "set");

            // indicamos que el tipo de datos del parametro del set, es
            // justamente el que retorna el get
            Class[] classesParamSet;
            classesParamSet = new Class[1];
            classesParamSet[0] = Class.forName("java.util.Set");

            // ahora obtenemos el metodo set del VO con el nombre y los
            // parametros que tenemos
            Method vOMetSet = vOClass.getMethod(vOMetSetName, classesParamSet);

            // ahora obtenemos el set
            Set propertySet = (Set) bOMethod.invoke(this, (Object[]) null);
            
            //log.debug(" copySet #############  " + bOMethod.getReturnType().toString());
            
            if (isPrimitivo(vOMetSet)){
            	
            	
           
            } else {
                // para cada elemento del set
	            for (Iterator it = propertySet.iterator(); it.hasNext();) {
	                Object elem = (Object) it.next();
	
	                if (isDemodaBO(elem.getClass())) {
	                    //ret.add(((BaseBO) elem).toVO());   ggiro
	                	ret.add(((BaseBean) elem).toVO(vOListaBlanca, vOPasados, profundidad, topeProfundidad, copiarList));
	                	
	
	                } else {
	                    // es un objeto cualunque
	                    ret.add(elem);
	
	                }
	            }
            }
            // ahora, obtenemos el parametro del set
            Object[] paramSet;
            paramSet = new Object[1];
            paramSet[0] = ret;

            // ahora, finalmente, invocamos el set del VO
            vOMetSet.invoke(vOObject, paramSet);

            return ret;
        } catch (Exception e) {
            //System.out.println("imposible pasar el Set metodo: " + e);
            return null;
        }
    }

    private boolean isPrimitivo(Method vOMethod){
        
    	Type objectType = vOMethod.getGenericReturnType();
    	
    	log.debug("isPrimitivo: " +  objectType.toString());
    	
    	if (vOMethod.getGenericReturnType().toString().indexOf("boolean") > -1 ||
    			vOMethod.getGenericReturnType().toString().indexOf("int") > -1 ||
    				vOMethod.getGenericReturnType().toString().indexOf("long") > -1){
    		log.debug("isPrimitivo: true");
            return true;
    	} else {
    		log.debug("isPrimitivo: false");
            return false;
        }
    }
        
    private List copyList(Method bOMethod, Class vOClass, Object vOObject, Collection vOPasados, int profundidad, int topeProfundidad, Collection vOListaBlanca, boolean copiarList) {
        try {
            List ret = new ArrayList();

            // obtenemos el nombre del metodo set del VO que tenemos que
            // ejecutar
            String vOMetSetName = replaceStr(bOMethod.getName(), "get", "set");

            // indicamos que el tipo de datos del parametro del set, es
            // justamente el que retorna el get
            Class[] classesParamSet;
            classesParamSet = new Class[1];
            classesParamSet[0] = Class.forName("java.util.List");

            // ahora obtenemos el metodo set del VO con el nombre y los
            // parametros que tenemos
            Method vOMetSet = vOClass.getMethod(vOMetSetName, classesParamSet);

            // ahora obtenemos el set
            List propertySet = (List) bOMethod.invoke(this, (Object[]) null);

            // para cada elemento del set
            for (Iterator it = propertySet.iterator(); it.hasNext();) {
                Object elem = (Object) it.next();

                if (isDemodaBO(elem.getClass())) {
                    //ret.add(((BaseBO) elem).toVO());   ggiro
                	ret.add(((BaseBean) elem).toVO(vOListaBlanca, vOPasados, profundidad, topeProfundidad, copiarList));

                } else {
                    // es un objeto cualunque
                    ret.add(elem);

                }
            }

            // ahora, obtenemos el parametro del set
            Object[] paramSet;
            paramSet = new Object[1];
            paramSet[0] = ret;

            // ahora, finalmente, invocamos el set del VO
            vOMetSet.invoke(vOObject, paramSet);

            return ret;
        } catch (Exception e) {
            //System.out.println("imposible pasar el Set metodo: " + e);
            return null;
        }
    }

    /**
     * Copia un BO
     * 
     * @param bOMethod
     * @param vOClass
     * @param vOObject
     */
    private void copyBO(Method bOMethod, Class vOClass, Object vOObject, Collection vOPasados, int profundidad, int topeProfundidad, Collection vOListaBlanca, boolean copiarList) throws Exception {
        try {
            // obtenemos el nombre del metodo set del VO que tenemos que
            // ejecutar
            String vOMetSetName;
            vOMetSetName = replaceStr(bOMethod.getName(), "get", "set");

            // indicamos que el tipo de datos del parametro del set, es el VO
            // que corresponde al BO
            Class[] classesParamSet;
            classesParamSet = new Class[1];
            classesParamSet[0] = Class.forName(replaceStr(bOMethod
                    .getReturnType().getName(), "buss.bean", "iface.model")
                    + "VO");

            // ahora obtenemos el metodo set del VO con el nombre y los
            // parametros que tenemos
            Method vOMetSet;
            vOMetSet = vOClass.getMethod(vOMetSetName, classesParamSet);

            // ahora, obtenemos el parametro del set
            Object[] paramSet;
            paramSet = new Object[1];

            // obtengo el objeto del get
            BaseBean getX = (BaseBean) bOMethod.invoke(this, (Object[]) null);
	    
            paramSet[0] = getX.toVO(vOListaBlanca, vOPasados, profundidad, topeProfundidad, copiarList); // le paso lo que me devuelve el get de
                                        // this ggiro
            
            // ahora, finalmente, invocamos el set del VO
            vOMetSet.invoke(vOObject, paramSet);

			log.debug("##### copyBO: " + bOMethod.getName() + "() ok.");
        } catch (Exception e) {
			if (e instanceof org.hibernate.HibernateException) {
				log.error("##### copyBO: " + bOMethod.getName() + "() fail: ", e);
				throw e;
			} else if (e instanceof java.lang.NoSuchMethodException) {
				log.debug("##### copyBO: " + bOMethod.getName() + "() fail: " + e);
			} else {
				log.debug("##### copyBO: " + bOMethod.getName() + "() fail" + e);
			}
        }
    }

    
    //--------------------------------- fin toVO --------------------------------------//


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
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        //System.out.println("str=" + str);
        //System.out.println("pattern=" + pattern);
        //System.out.println("replace=" + replace);

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));

        //System.out.println("result=" + result);
        return result.toString();
    }

    /**
     * Retorna true si el metodo comienza con: get
     * Pero si es un metodo "static" retorna falso, ya que esos metodos no se copian al VO
     * 
     * @param bOMethod
     * @return
     */
    private boolean isGetter(Method bOMethod) {
    	
    	// Con esta linea no se tienen en cuenta los metodo estaticos.    	
    	if (bOMethod.getModifiers() == 9)
    		return false;
    	
    	if (bOMethod.getName().substring(0, 3).equals("get"))
            return true;
        else
            return false;
    }
    
    /**
     * Retorna true si es un CommonKey
     * 
     * @param bOMethod
     * @return
     */
    private boolean isCommonKey(Class bOClass) {
        if (bOClass.getName().indexOf("CommonKey") > -1 )
            return true;
        else
            return false;
    }
    
    /**
     * Retorna true si es un bean de negocio de Demoda
     * 
     * @param bOMethod
     * @return
     */
    private boolean isDemodaBO(Method bOMethod) {
    	if (bOMethod.getReturnType().getName().indexOf("buss.bean") > 0)
            return true;
        else
            return false;
    }

    /**
     * isDemodaBO
     * 
     * @param className
     * @return
     */
    private boolean isDemodaBO(Class className) {
        if (className.getName().indexOf("buss.bean") > -1)
            return true;
        else
            return false;
    }

    /**
     * Retorna true si es un Set
     * 
     * @param bOMethod
     * @return
     */
    private boolean isSet(Method bOMethod) {
        if (bOMethod.getReturnType().getName().indexOf("java.util.Set") > -1) {
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
    
    /**
     * Devuelve true si el metodo retorna una enumeracion. 
     * 
     * @param bOMethod
     * @return
     */
    private boolean isEnum(Method bOMethod, Object vOObject) {
    	try{
	    	Method voMethod = vOObject.getClass().getMethod(bOMethod.getName());
	    	
	    	if (voMethod.getReturnType().isEnum()) {
	    		log.debug("  #######  isEnum: " + voMethod.getReturnType().getName().toString() );
	    		return true;
	
	        } else {
	            return false;
	        }
    	
    	} catch (Exception e ){
    		return false;
    	}
    }
    
    /**
     * Retorna true si es un Set
     * 
     * @param bOMethod
     * @return
     */
    private boolean existeEnCol(Collection col, String bOName) {
    	for (Iterator it=col.iterator();it.hasNext();){
    		if (it.next().equals(bOName))
    			return true;
    	}
    	return false;
    }
        
    private String getSpaces(int i) {
    	String ret="";
    	for (int j=0;j<=i;j++) {
    		ret += "    ";
    	}
    	return ret;
    }

	/**
     * @return Devuelve el atributo estado.
     */
    public Integer getEstado() {
        return estado;
    }

    
    /**
     * @param estado Fija el atributo estado
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }
    
    /**
	 * @return Devuelve el atributo fechaUltMdf.
	 */
	public Date getFechaUltMdf() {
		return fechaUltMdf;
	}

	/**
	 * @param fechaUltMdf Fija el atributo fechaUltMdf.
	 */
	public void setFechaUltMdf(Date fechaUltMdf) {
		this.fechaUltMdf = fechaUltMdf;
	}

	@Deprecated
	public Long getErrCode() {
		return errCode;
	}

	@Deprecated
	public void setErrCode(Long errCode) {
		this.errCode = errCode;
	}

	public String getUsuarioUltMdf() {
		return usuarioUltMdf;
	}

	public void setUsuarioUltMdf(String usuarioUltMdf) {
		this.usuarioUltMdf = usuarioUltMdf;
	}

	public String getUsuario() {
		return usuarioUltMdf;
	}

	public boolean isEnabled(){
		return this.estado.intValue() == Estado.ACTIVO.getId();
	}
	
	/* Este metodo deve ser reescrito en la clase hija.*/
	public Long getId() {
		return -1L;
	}
	
	/**
	 * Debe implementarse en la subclase
	 * @return
	 */
	public Date getFechaInicioVig() {
		return null;
	}

	/**
	 * Debe implementarse en la subclase
	 * @return
	 */
	public Date getFechaFinVig() {
		return null;
	}

	/** Retorna true si el objeto actual esta vigente
	 * 
	 * @return
	 * @throws Exception
	 */
    public boolean isVigente() throws Exception {

    	return Vigencia.VIGENTE.getId().equals(this.getVigencia());

    }

	/** Si la fecha inicio vigencia es menor o igual a la actual y 
	 *  la fecha fin vigencia es nula el objeto actual esta vigente.
	 *  Si la fecha actual esta entre la fecha inicio vigencia y fin
	 *  vigencia el objeto actual esta vigente.
	 *  
	 *  Sino esta no vigente.
	 *  
	 */

    public Integer getVigencia() throws Exception {

    	return this.getVigenciaForDate(new Date());

    }

    /**
     * Retorna la descripcion de la vigencia al dia de hoy de este objeto.
     * @return
     * @throws Exception
     */
    public String getDesVigencia() throws Exception {
    	Integer idVigencia = getVigencia();
    	Vigencia vig = Vigencia.getById(idVigencia);
    	if (vig == null) return "No Posee";
    	return vig.getValue();
    }
    
	/** Si la fecha desde es menor o igual a la fecha a validar 
	 *  y la fecha hasta es nula el atrVal esta vigente.
	 *  Si la fecha a validar esta entre la fecha Desde y Hasta el atrVal
	 *  esta vigente.
	 *  Sino esta no vigente.
	 *  
	 */
    public Integer getVigenciaForDate(Date dateToValidate) throws Exception {

    	Date fechaInicioValue = this.getFechaInicioVig();
    	Date fechaFinValue = this.getFechaFinVig();
    	
    	Integer vigencia = Vigencia.NOVIGENTE.getId();
    	
    	// si fecha desde es nula esta creado
    	if (fechaInicioValue == null) {
    		vigencia = Vigencia.CREADO.getId();
    	}

    	if (fechaInicioValue != null) {
    		// fecha hasta nula
	    	if (fechaFinValue == null) {
	    		if ( DateUtil.isDateBeforeOrEqual(fechaInicioValue, dateToValidate) )  {
	    			vigencia = Vigencia.VIGENTE.getId();
	    		}
	    	}
	
	    	// fecha hasta existente, y no son iguales la fecha desde y hasta
	    	if (fechaFinValue != null) {
	    		if ( DateUtil.isDateBeforeOrEqual(fechaInicioValue, dateToValidate)
	    			&& DateUtil.isDateAfter(fechaFinValue, dateToValidate) )  {
	    			vigencia = Vigencia.VIGENTE.getId();
	    		}
	    	}
    	}

    	return vigencia;
    }

    public String getEstadoView(){
    	return Estado.getById(this.getEstado()).getValue();
    }
    
    private Object invokeGetter(Object obj, String getterName) throws Exception{
    	System.out.println("getter: "+getterName);
		return obj.getClass().getMethod(getterName).invoke(obj, (Object[]) null);
    }
    
    private void invokeSetter(Object obj, String setterName, Object value) throws Exception{
    	
    	String strForName="";
    	strForName=value.getClass().getName();
	    if (strForName.indexOf("$$EnhancerByCGLIB")>0)
	    	strForName = strForName.substring(0, strForName.indexOf("$$EnhancerByCGLIB"));
	        
    	try{
	    	Object objectValue[]=new Object[1];
	    	objectValue[0]=value;
	    	Class[] classesParamSet;
	        classesParamSet = new Class[1];
	        System.out.println("Clase: "+value.getClass().getName());
	       
	        System.out.println("strClass: "+strForName);
	        if (strForName.equals("java.sql.Timestamp"))
	        	strForName = "java.util.Date";
	        classesParamSet[0] = Class.forName(strForName);
	    	System.out.println("setter: "+setterName);
	    	System.out.println("valor: "+ value.toString());
			obj.getClass().getMethod(setterName,classesParamSet).invoke(obj, objectValue);
    	}catch(Exception e){
    		System.out.println("No se pudo setear: "+setterName);
    	}
    }
    
    private boolean isGetterForClone(Method method){
    	  if(!method.getName().startsWith("get"))      return false;
    	  if (method.getName().equals("getFechaUltMdf")) return false;
    	  if (method.getName().equals("getUsuario")) return false;
    	  if (method.getName().equals("getEstado")) return false;
    	  if(method.getParameterTypes().length != 0)   return false;  
    	  if(void.class.equals(method.getReturnType())) return false;
    	  return true;
    	}

    
    public Object cloneObject (Object object) throws Exception{
    	Class clase = object.getClass();
    	System.out.println("CLASE: "+clase.getName());
    	Object newObject = clase.newInstance();
    	
    	Method methods[]=clase.getMethods();	
    	for (int i = 0; i < methods.length; i++) {
    		Method method=methods[i];
    		System.out.println("methodName:"+ method.getName());
    		
    		if (isGetterForClone(method) && !isList(method)
    				&& !method.getName().equals("getId")){
    			String setterName= replaceStr(method.getName(),"get","set");
    			Object result = invokeGetter(object,method.getName());
    			if (result!=null)
    				invokeSetter(newObject,setterName,result);
    		}
         }
    	
	

    	return newObject;
    	
    }
    
    
}
