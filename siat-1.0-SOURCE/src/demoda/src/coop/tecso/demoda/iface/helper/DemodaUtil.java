//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.UserContext;

public class DemodaUtil {
	
	private static Log log = LogFactory.getLog(DemodaUtil.class);
	/*
	 * Debe Existir un Archivo: base.properties.
	 * Debe contener la entrada: base.formatoInvalido=El formato del campo {0} es Inv&aacute;lido
	 */ 
	private static final String FORMATO_INVALIDO_KEY = "0 base.formatoInvalido";
	private static final String MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO_KEY = "0 base.formatoPorcentajeInvalido";
	public static final String POPULATEVO_TRIMSTRING = "POPULATEVO_TRIMSTRING";
	private static final String POPULATEVO_ALLOWANYSTR = "POPULATEVO_ALLOWANYSTR";
	private static final String INVALID_STRING_PATTERN = "([^A-Za-z0-9\u00a0-\u00ff.,'\"% _-]+)";
	private static Pattern STR_PATTERN = Pattern.compile(INVALID_STRING_PATTERN);
			
	public DemodaUtil() {
		super();
	}

	/** Obtiene el numbre del metodo desde el cual fue invocado este metodo.
	 * NOTA: Este metodo esta disponible solo a fines de loggear en modo debug, 
	 * ya que es poco performante.
	 * 
	*/
	public static String currentMethodName() {
		String funcName = new Exception().fillInStackTrace().getStackTrace()[1].getMethodName();
		return funcName;
	}


	/* UserContext TLS Helper */
    private static ThreadLocal<UserContext> userContext = new ThreadLocal<UserContext>();
    public static UserContext currentUserContext() throws DemodaServiceException {    	
    	UserContext uc  = userContext.get();
        if (uc == null) {
        	throw new DemodaServiceException("El hilo no posee UserContext seteado. Talvez se llamo a currentUserContext() sin llamada previa a setCurrentUserContext() o se seteo con un valor nulo.");
        }
        return uc;  
    }

    public static void setCurrentUserContext(UserContext uc) {
		userContext.set(uc);
    }
    
    
    /**
	 * <p>Recibe el modelo que se quiere popular y el request con los valores submintidos y 
	 * realiza la copiar de los valores a las propiedades correspondientes.</p>
	 * <p>Para todos los valores de request realiza el trim</p>
	 * <p>Para los tipos Date, chequea existencia de annotation, si existe intenta la instanciacion con la mascara correspondiente</p>
	 * <p>Para los tipos Long, Integer, Double y Float si el valor submitito es "" se asignara null</p>
	 * <p>Si es un propiedad terminada en View, se setea la misma y se intenta instanciar la correspondiente sin View segun el 
	 * tipo de datos devuelto por el metodo get</p>
	 * <p>Si hay algun error de formato al intentar setear alguna de las propiedades, se carga un RecoverableError al VO pasado como primer
	 * parametro.</p>
	 *  
	 * @param modelVO
	 * @param request
	 */
	public static void populateVO(Common modelVO, HttpServletRequest request) {
			String funcName = "populateVO";
			
			log.debug(funcName + ": enter -------------------------------------------------");
			
			//	Blanqueo la lista de errores
			modelVO.clearErrorMessages();
			
			Enumeration e = request.getParameterNames();
			
			while( e.hasMoreElements() )
			   {
			   String strName = (String) e.nextElement() ;
			   
			   log.debug(funcName + ": request: " + strName + " -> " + request.getParameter(strName)); 
			   
			   // ---> Para las propiedades simples (primer nivel)
			   if (strName.indexOf(".") == -1){
				   try {
					   Method getMethod = modelVO.getClass().getMethod("get" + strName.substring(0, 1).toUpperCase() + strName.substring(1, strName.length() ) );	
					   
					   if (getMethod != null){
						   	String propertyName = strName;
						   	
						   	if (request.getParameter(propertyName) != null){
								String propertyValue = request.getParameter(propertyName);
								log.debug(funcName + ": simple properyName " + propertyName);
								copyProperty(modelVO, modelVO, propertyName, propertyName, propertyValue, request);
							}	
						   	
							// Copiado especial para las propiedades terminadas en View
							if (propertyName.endsWith("View") ){
								String propertyValue = request.getParameter(propertyName); // En el value queda lo sumbitido como View
								
								if (propertyValue != null ){ 
										//&& !"".equals(propertyValue.trim())){
									propertyName = propertyName.substring(0, propertyName.length() -4); // Le quito el View
									log.debug(funcName + ": property View encontrada - properyName " + propertyName);
									copyProperty(modelVO, modelVO, propertyName, propertyName, propertyValue, request);
								}
							}
					   }
					   
				   } catch (Exception me) {
					   log.debug(funcName + ": propiedad NO encontrada: " + strName);						   						   
				   }
			   }
			   // <--- fin propiedades simples
			   
			   
			   // ---> Si corresponde a una propiedad iface.model
			   if (strName.indexOf(".") > -1){
				   try{
					   String[] arrPropertyName = strName.split("\\.");
					   String propertyName = arrPropertyName[0];
					   
					   Method getMethod = modelVO.getClass().getMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1, propertyName.length() ) );
					   
					   if (getMethod.getReturnType().isEnum()){
							log.debug("#### " + funcName + " Enumeration encontrada - valor: " + request.getParameter(strName));
							
							setEmunById(getMethod, modelVO, propertyName, strName, request);
							
					   } else if((getMethod.getReturnType().toString()).indexOf("iface.model.") > -1 ){
						    log.debug(funcName + ": propiedad iface.model encontrada: " + strName);
						   
						    Object voValueProperty = getMethod.invoke(modelVO);
							String voPropertyName = getPropertyName(arrPropertyName);
							log.debug(funcName + ": voPropertyName: " + propertyName);
							populateVO(modelVO, voValueProperty, strName, voPropertyName, request);
						}
					   
				   }catch (Exception me){						   
					   log.error(funcName + ": error al intentar copiar propiedad: " + strName);
				   }
			   }
			   // <--- fin propiedad iface.model
			} 
			
			log.debug(funcName + ": exit ---------------------------------------------------------");        
	}
	
	/**
	 * Realiza el copiado de los valores del request a las propiedades correspondientes de originalCmmVO o su voValue componente
	 * Se utiliza recursivamente en caso que el modelVO tenga varios niveles de composicion.
	 * 
	 * @param originalCmmVO
	 * @param voValue
	 * @param originalPropertyName
	 * @param propertyName
	 * @param request
	 */
	private static void populateVO(Common originalCmmVO, Object voValue, String originalPropertyName, String propertyName, HttpServletRequest request) {
		String funcName = "	  populateVO-2";
		log.debug(funcName + ": enter");
		// ---> Para las propiedades simples (primer nivel)
		if (propertyName.indexOf(".") == -1){
			try {

				log.debug(funcName + ": properyName:" + propertyName);

				Method getMethod = voValue.getClass().getMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1, propertyName.length() ) );	

				if (getMethod != null){
					//String propertyName = originalProperyName;

					if (request.getParameter(originalPropertyName) != null){
						String propertyValue = request.getParameter(originalPropertyName);
						log.debug(funcName + ": simple properyName " + propertyName);
						copyProperty(originalCmmVO, (Common)voValue, originalPropertyName, propertyName, propertyValue, request);
					}	

					// Copiado especial para las propiedades terminadas en View
					if (propertyName.endsWith("View") ){
						String propertyValue = request.getParameter(originalPropertyName); // En el value queda lo sumbitido como View

						if (propertyValue != null ){
							//&& !"".equals(propertyValue.trim())){
							propertyName = propertyName.substring(0, propertyName.length() -4); // Le quito el View
							log.debug(funcName + ": property View encontrada - properyName " + propertyName);

							String originalPropertyNameNoView = originalPropertyName.substring(0, originalPropertyName.length() -4);

							copyProperty(originalCmmVO, (Common)voValue, originalPropertyNameNoView, propertyName, propertyValue, request);
						}
					}
				}

			} catch (Exception me) {
				log.debug(funcName + ": propiedad NO encontrada: " + originalPropertyName);						   						   
			}
		}
		// <--- fin propiedades simples


		// ---> Si corresponde a una propiedad iface.model o una enumeration
		if (propertyName.indexOf(".") > -1){
			try {


				String[] arrPropertyName = propertyName.split("\\.");
				String voPropertyName = arrPropertyName[0];

				Method getMethod = voValue.getClass().getMethod("get" + voPropertyName.substring(0, 1).toUpperCase() + voPropertyName.substring(1, voPropertyName.length()) );

				if (getMethod.getReturnType().isEnum()){
					log.debug("#### " + funcName + ": Enumeration encontrada - valor: " +  request.getParameter(originalPropertyName));

					setEmunById(getMethod, voValue, voPropertyName, originalPropertyName, request);

				} else if((getMethod.getReturnType().toString()).indexOf("iface.model.") > -1 ){
					log.debug(funcName + ": propiedad iface.model encontrada: " + propertyName);

					Object voValueProperty = getMethod.invoke(voValue);
					String nextPropertyName = getPropertyName(arrPropertyName);
					log.debug(funcName + ": voPropertyName: " + voPropertyName);
					populateVO(originalCmmVO, voValueProperty, originalPropertyName, nextPropertyName, request);
				}

			} catch (Exception me){
				log.debug(funcName + ": error al intentar copiar iface.model: " + propertyName);
			}
		}
		// <--- fin propiedad iface.model
	}
	
	/**
	 * Dado un id sumbitido realiza la busqueda de la correspondiente Enumeracion. 
	 * 
	 * @param getMethod
	 * @param voValue
	 * @param voPropertyName
	 * @param originalPropertyName
	 * @param request
	 */
	private static void setEmunById(Method getMethod, Object voValue, String voPropertyName, String originalPropertyName,  HttpServletRequest request){
		String funcName = "	  	setEmunById";
		log.debug(funcName + ": enter");
		try{
			
			 Class[] classesParamSet;
			 classesParamSet = new Class[1];
			 classesParamSet[0] = Class.forName(getMethod.getReturnType().getName());
			 
	         Method vOMetSet;
	         vOMetSet = voValue.getClass().getMethod("set" + voPropertyName.substring(0, 1).toUpperCase() + voPropertyName.substring(1, voPropertyName.length()), Class.forName(getMethod.getReturnType().getName()));
	         					         
	         // ahora, obtenemos el parametro del set
	         Object[] paramSet;
	         paramSet = new Object[1];
	         paramSet[0] = new Integer(request.getParameter(originalPropertyName));
	         
	         Method getByIdMethod = classesParamSet[0].getMethod("getById", Class.forName("java.lang.Integer"));
	         
	         log.debug(funcName + " paramSet[0] intValue: " + ((Integer)paramSet[0]).intValue());
	        
	         Object enu = getByIdMethod.invoke(classesParamSet[0], paramSet[0]);
                         
	         // ahora, finalmente, invocamos el set del VO
	         vOMetSet.invoke(voValue, enu);
			
		} catch(Exception e){
			
		}
		
	}
	
	/**
	 * originalCmmVO es donde se cargara la lista de error si existe alguno
	 * cmmVO el es VO al cual se el invocara el metodo set correspondiente a name con el parametro value
	 * originalCmmVO y cmmVO pueden ser el mismo objeto si no existen composiciones
	 * 
	 * @param originalCmmVO
	 * @param cmmVO
	 * @param name
	 * @param value
	 */
	private static void copyProperty(Common originalCmmVO, Common cmmVO, String fullVoPropertyName, String properyName, Object value, HttpServletRequest request) {
		try {
			
			String funcName = "		copyProperty";
			
			log.debug(funcName + " originalCommVO: " + originalCmmVO.getClass().toString().substring(originalCmmVO.getClass().toString().indexOf("iface.model.") + 12) + 
										   " - cmmVO: " + cmmVO.getClass().toString().substring(cmmVO.getClass().toString().indexOf("iface.model.") + 12) +
					 					   " - properyName: " + properyName + 
					 					   " - value: " + value.toString());
			
			// Obtenemos el nombre del metodo get
			String getMethodName = "get" + properyName.substring(0, 1).toUpperCase() + properyName.substring(1, properyName.length() ) ;
			
			// Obtenemos el metodo get sin "View"
			Method voMethodGet = cmmVO.getClass().getMethod(getMethodName);
			
			
			if (voMethodGet.getReturnType().isEnum())
				log.debug(funcName + " Enumeration encontrada - valor: " + value.toString());
			
			
			// Obtenemos en tipo de datos que retorna el metodo get
			String returnType = voMethodGet.getReturnType().toString();

			// Obtenemos en nombre del metodo set
			String vOMetSetName = "s" + getMethodName.substring(1, getMethodName.length());
			
			// Indicamos que el tipo de datos del parametro del set, es justamente el que retorna el get
            Class[] classesParamSet = new Class[1];
            classesParamSet[0] = Class.forName(voMethodGet.getReturnType().getName());

            // Obtenemos el metodo set
            Method vOMethodSet = cmmVO.getClass().getMethod(vOMetSetName, classesParamSet);
            
            // Realizo el trim para cualquier tipo de datos
            String valorString = new String((String)value);
            valorString = valorString.trim();
            
            // copia los listId
			if (voMethodGet.getReturnType().isArray()){
				log.debug(funcName + " Aca va el codigo de copiado de Arrays -> returnType: " + voMethodGet.getReturnType());
				
				String[] listId = request.getParameterValues(properyName);
				
				try{
					
					Object[] arrId = new Object[1];
					
					if (listId != null){
						for (int i=0; i < listId.length; i++){
							log.debug(funcName + " id=" + listId[i]);
						}
					}
					
					arrId[0] = listId;
					
					vOMethodSet.invoke(cmmVO, arrId);
					
					return; // OJO con este return, no se deberia quitar
					
				} catch (Exception e){
					e.printStackTrace();
					log.error(funcName + " " + fullVoPropertyName + " isArray() encontrado que no se pudeo copiar");
					return; // OJO con este return, no se deberia quitar
				}				
			}
            
            // Si es de tipo String realizamos el trim
			if (returnType.indexOf("java.lang.String")>-1){
				try {
					String allow = (String) request.getAttribute(POPULATEVO_ALLOWANYSTR);
		            boolean check = !"true".equals(allow);
		            Object obj = valorString;
		            
					String atrTrimString = (String) request.getAttribute(POPULATEVO_TRIMSTRING);
		            if ("false".equals(atrTrimString)) {
		            	//solo NO hacemos trim, si esta definido el atributo y esta en false.
		            	//osea: antes llamar a populateVO, llamar a request.setAttribute("POPULATEVO_TRIMSTRING", "false");
		            	//para hacer que no trimee los tipo String
		            	obj = value;
		            }
		            
		            if (obj != null && check) {
		            	Matcher m = STR_PATTERN.matcher(obj.toString());
		            	if (m.find()) {
		            		//throw new Exception();
		            	}
		            }
		            
		            vOMethodSet.invoke(cmmVO, obj);
				} catch (Exception e){
					//e.printStackTrace();
					String claveError = getClaveError(cmmVO.getClass().toString(), fullVoPropertyName);
					log.error(funcName + " java.lang.String encontrado - valor: " + value.toString() + " con error - claveError: " + claveError);
					
					originalCmmVO.addRecoverableError(FORMATO_INVALIDO_KEY,  claveError);
				}
			}
            			 
            // Intentamos invocar el metodo set segun es tipo de dato del get
            // Si hay error cargamos la lista de error
			// Para los Date, chequea la existencia de masacara e intenta setear la la propiedad con la mascara correspondiente.
            if (returnType.indexOf("java.util.Date")>-1){
				try{
					Date valorDate = null;
					
					log.debug(funcName + " java.util.Date encontrado");
					
					// Si es "" o espacios, nuleo el valor.
					if (valorString.trim().equals("") ){
						vOMethodSet.invoke(cmmVO, valorDate);
					
					} else {
						// Si no posee Annotations se utiliza la mascara por default
						Annotation[] annotations = vOMethodSet.getAnnotations(); 
						if (annotations.length == 0 ){
							log.debug(funcName + " Annotation IS NOT Present");
							if( DateUtil.isValidDate(valorString, "dd/MM/yyyy"))
								valorDate = DateUtil.getDate(valorString, "dd/MM/yyyy");
					    
						// ddSMMSYYYY_MASK = "dd/MM/yyyy"						
						} else if (vOMethodSet.isAnnotationPresent(DDsMMsYYYY_MASK.class)){
							log.debug(funcName + " DDSMMSYYYY_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.ddSMMSYYYY_HH_MM_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.ddSMMSYYYY_HH_MM_MASK);
	
						// dd_MM_YYYY_MASK = "dd-MM-yyyy"						
						} else if (vOMethodSet.isAnnotationPresent(DD_MM_YYYY_MASK.class)){
							log.debug(funcName + " DD_MM_YYYY_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.dd_MM_YYYY_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.dd_MM_YYYY_MASK);
						
						// ddMMYYYY_MASK = "ddMMyyyy"	
						} else if (vOMethodSet.isAnnotationPresent(DDMMYYYY_MASK.class)){
							log.debug(funcName + " DDMMYYYY_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.ddMMYYYY_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.ddMMYYYY_MASK);	
	
						// YYYYMM_MASK = "yyyyMM"
						} else if (vOMethodSet.isAnnotationPresent(YYYYMM_MASK.class)){
							log.debug(funcName + " YYYYMM_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.YYYYMM_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.YYYYMM_MASK);
						
						// ddSMMSYY_MASK   = "dd/MM/yy"	
						} else if (vOMethodSet.isAnnotationPresent(DDsMMsYY_MASK.class)){
							log.debug(funcName + " DDsMMsYY_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.ddSMMSYY_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.ddSMMSYY_MASK);
	
						// dd_MM_YY_MASK   = "dd-MM-yy"
						} else if (vOMethodSet.isAnnotationPresent(DD_MM_YY_MASK.class)){
							log.debug(funcName + " DD_MM_YY_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.dd_MM_YY_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.dd_MM_YY_MASK);	
						
						// ddMMYY_MASK     = "ddMMyy"
						} else if (vOMethodSet.isAnnotationPresent(DDMMYY_MASK.class)){
							log.debug(funcName + " DDMMYY_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.ddMMYY_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.ddMMYY_MASK);	
						
						// yyyy_MM_dd_MASK = "yyyy-MM-dd"
						} else if (vOMethodSet.isAnnotationPresent(YYYY_MM_DD_MASK.class)){
							log.debug(funcName + " YYYY_MM_DD_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.yyyy_MM_dd_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.yyyy_MM_dd_MASK);	
							
						// yyyy_MM_dd_HH_MM_SS_MASK = "yyyy-MM-dd HH:MM:SS"
						} else if (vOMethodSet.isAnnotationPresent(YYYY_MM_DD_HH_MM_SS_MASK.class)){
							log.debug(funcName + " YYYY_MM_DD_HH_MM_SS_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.yyyy_MM_dd_HH_MM_SS_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.yyyy_MM_dd_HH_MM_SS_MASK);
						
						// ddSMMSYYYY_HH_MM_MASK = "dd/MM/yyyy HH:MM"
						} else if (vOMethodSet.isAnnotationPresent(DDsMMsYYYY_HH_MM_MASK.class)){
							log.debug(funcName + " DDsMMsYYYY_HH_MM_MASK IS Present");
							if( DateUtil.isValidDate(valorString, DateUtil.ddSMMSYYYY_HH_MM_MASK))
								valorDate = DateUtil.getDate(valorString, DateUtil.ddSMMSYYYY_HH_MM_MASK);
							
						//	HOUR_MINUTE_MASK = "HH:mm"	
						} else if (vOMethodSet.isAnnotationPresent(HOUR_MINUTE_MASK.class)){
							log.debug(funcName + " HOUR_MINUTE_MASK IS Present");
							if (DateUtil.isValidTime(valorString, DateUtil.HOUR_MINUTE_MASK))
								valorDate = DateUtil.getTime(valorString, DateUtil.HOUR_MINUTE_MASK);
		                    
						// MINUTE_MASK = "mm"
						} else if (vOMethodSet.isAnnotationPresent(MINUTE_MASK.class)){
							log.debug(funcName + " MINUTE_MASK IS Present");
							if (DateUtil.isValidTime(valorString, DateUtil.MINUTE_MASK))
								valorDate = DateUtil.getTime(valorString, DateUtil.MINUTE_MASK);
						}  
						
						// Si se hizo un getDate y no fallo invoco al metodo set.
						if (valorDate != null)
							vOMethodSet.invoke(cmmVO, valorDate);
						else
							throw new Exception();
					}
					
				} catch (Exception e){
					String claveError = getClaveError(cmmVO.getClass().toString(), fullVoPropertyName);
					log.error(funcName + " valor: " + valorString + " con error - claveError: " + claveError);					
					
					originalCmmVO.addRecoverableError( FORMATO_INVALIDO_KEY, claveError);
				}
			}
			
            // Para Long, Integer, Double y Float 
			// Si se submitio "" se seteara un null sino se intentara instanciar la 
            // clase correspondiente con el valor enviado.
            if (returnType.indexOf("java.lang.Long")>-1){
				try{
					Long valorLong = null;
					
					if (!"".equals(valorString)){
						valorLong = new Long(valorString);
					}
					
					vOMethodSet.invoke(cmmVO, valorLong);
					
				} catch (Exception e){
					String claveError = getClaveError(cmmVO.getClass().toString(), fullVoPropertyName);
					log.error(funcName + " java.lang.Long encontrado - valor: " + valorString + " con error - claveError: " + claveError);
					originalCmmVO.addRecoverableError( FORMATO_INVALIDO_KEY, claveError);
				}
			}
			
			if (returnType.indexOf("java.lang.Integer")>-1){
				try{
					
					Integer valorInteger = null;
					
					if (!"".equals(valorString)){
						valorInteger = new Integer(valorString);
					}
					
					vOMethodSet.invoke(cmmVO, valorInteger);

				} catch (Exception e){
					String claveError = getClaveError(cmmVO.getClass().toString(), fullVoPropertyName);
					log.error(funcName + " java.lang.Integer encontrado - valor: " + valorString + " con error - claveError: " + claveError);
					originalCmmVO.addRecoverableError( FORMATO_INVALIDO_KEY, claveError);
				}
			}
            
			if (returnType.indexOf("java.lang.Double")>-1){
				try{
					Double valorDouble = null;
					
					/* TODO: Preguntar que onda con esto, si se valida, se redondea, o que 
					* pregunta si posee anotacion.
					* - Si la posee es del tipo @Decimal(Enteros, Decimales) y hay que validar que el formato sea correcto.
					* - Si ni posee anotacion, se asume que es un monto y se validan los enteros y decimales segun las constantes
					*    para los montos que estan en ... (crick crick)   
					*
					// Estos son los valores por defecto para los montos
					if (!StringUtil.isDecimalValido(valorString, enteros, decimales))
						throw new Exception();*/
					
					if (!"".equals(valorString)){
						valorDouble = new Double(valorString);
					}
					
					
					Annotation[] annotations = vOMethodSet.getAnnotations(); 
					if (annotations.length == 0 ){
						log.debug(funcName + " Annotation IS NOT Present");
					// FormatDecimal						
					} else if (vOMethodSet.isAnnotationPresent(FormatDecimal.class)){
						log.debug(funcName + " FormatDecimal(" + ")");
						//TODO: agregar un algoritomo de busqueda porque puede poseer mas de una annotation
						FormatDecimal fd = (FormatDecimal) annotations[0];
						//enteros = fd.Enteros();
						//decimales = fd.Decimales();
					} else if (vOMethodSet.isAnnotationPresent(PORCENTAJE.class)){
						
						if (!StringUtil.isNullOrEmpty(valorString) 
								&& (valorDouble.doubleValue() < 0 || valorDouble.doubleValue() > 1)){
							String claveError = getClaveError(cmmVO.getClass().toString(), fullVoPropertyName);
							originalCmmVO.addRecoverableError(MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO_KEY, claveError);
						
						}
					}
					
					
					vOMethodSet.invoke(cmmVO, valorDouble);
					
				} catch (Exception e){
					String claveError = getClaveError(cmmVO.getClass().toString(), fullVoPropertyName);
					log.error(funcName + " java.lang.Double encontrado - valor: " + valorString + " con error - claveError: " + claveError);
					originalCmmVO.addRecoverableError( FORMATO_INVALIDO_KEY, claveError);
				}
			}
			
			if (returnType.indexOf("java.lang.Float")>-1){
				try{
					Float valorFloat = null;
					
					if (!"".equals(valorString)){
						valorFloat = new Float(valorString);
					}
					
					vOMethodSet.invoke(cmmVO, valorFloat);
					
				} catch (Exception e){
					String claveError = getClaveError(cmmVO.getClass().toString(), fullVoPropertyName);
					log.debug(funcName + " java.lang.Float encontrado - valor: " + valorString + " con error - claveError: " + claveError);
					originalCmmVO.addRecoverableError( FORMATO_INVALIDO_KEY, claveError);
				}
			}
		
			
			if (returnType.indexOf("java.lang.Boolean")>-1){
				try{
					Boolean valorBoolean = null;
					
					if (!"".equals(valorString)){
						valorBoolean = new Boolean(valorString);
					}
					
					vOMethodSet.invoke(cmmVO, valorBoolean);
					
				} catch (Exception e){
					String claveError = getClaveError(cmmVO.getClass().toString(), fullVoPropertyName);
					log.debug(funcName + " java.lang.Float encontrado - valor: " + valorString + " con error - claveError: " + claveError);
					originalCmmVO.addRecoverableError( FORMATO_INVALIDO_KEY, claveError);
				}
			}
			
		} catch (Exception e){
			//e.printStackTrace();
		}		
	}
	

	/**
	 * Devuelve la concatenacion de las posiciones de la 1 a la n
	 * 
	 * @param getMethodName
	 * @return
	 */
	private static String getPropertyName(String[] arrPropertyName) {
		String retPropertyName = "";
		try {
			
			if (arrPropertyName.length == 1){
				retPropertyName = arrPropertyName[0]; 
			} else {	
				for (int i=1; i < arrPropertyName.length ;i++ ){
					retPropertyName = retPropertyName + arrPropertyName[i];
					if (i < arrPropertyName.length -1)
						retPropertyName = retPropertyName + ".";
				}
			}		
		} catch (Exception e) {
			log.error("		getPropertyName: error al intentar obtener el propertyName");
		}
		
		return retPropertyName;
	}
	
	
	/**
	 * Arma la clave del error: 
	 * <modulo>.<className>.<propertyName>.formatError
	 * @param _className
	 * @param _properyName
	 * @return
	 */
	private static String getClaveError(String  _className, String  _properyName){
		String claveError = "";
		String formatError = "formatError";
		log.debug("		#### getClaveError ###");
		log.debug("		getClaveError ->  _className = " + _className);
		log.debug("  	getClaveError ->  _properyName = " + _properyName);
		
		try {
			 // Obtengo el nombre de la clase para armar la clave de error        
	        String className = _className.substring(_className.indexOf("iface.model.") + 12);
	        className = className.substring(0, 1).toLowerCase() + className.substring(1, className.length());
	        
	        // Obtengo en nombre del modulo para armar la clave del error
	        String moduleName = _className;
	        moduleName = moduleName.substring(0, moduleName.indexOf(".iface.model."));             
	        
	        String[] arrPkgNames = moduleName.split("\\.");
	        
	        if (arrPkgNames.length > 0)
	        	moduleName = arrPkgNames[arrPkgNames.length -1];
	        
	        // Si es un VO, no lo incluyo en la clave, porque en _properyName ya me viene el nombre del vo formateado(primera minus, sin "VO")
	        // Si es un searchPage (hay que ver si el propery name)
	        if (className.endsWith("VO"))
	        	className = "";
	        else 		
	        	className += ".";
	        
	        String[] arrPoint = _properyName.split("\\."); 
	        
	        log.debug("  	getClaveError ->  cantidad niveles composicion = " + arrPoint.length);
	        // Si el nivel de composicion es mayor que uno, obtengo los ultimos dos 
	        if (arrPoint.length > 1){
	        	className = "";
	        	_properyName = arrPoint[arrPoint.length - 2] + "." + arrPoint[arrPoint.length - 1];  
	        }
	        	
			claveError = moduleName + "." + className + _properyName + "." + formatError;
			
		} catch (Exception e){
			log.error("		getClaveError: error al intentar generar la clave de error");			
		}
		
		return claveError;
	}

    /**
     * Generates a Hashtable with JNDI name as key and Object type as value.
     *
     * @param jndiList  - the hashtable to append to
     * @param ctx       - the context for which the bindings are to be listed
     * @param path      - the path to be listed under a context
     *
     * @since 0.1
     */
    public static void listContext(Hashtable jndiList, Context ctx, String path) throws Exception {
    	if (ctx == null) {
    		return;
    	}

    	NamingEnumeration list = ctx.listBindings(path);

    	while (list.hasMore()) {
    		Binding item = (Binding) list.next();
    		String className = item.getClassName();

    		String name = item.getName();

    		if (name != null) {

    			if (name.equals("")) {
    				continue;
    			}
    		} else {
    			continue;
    		}

    		Object o = item.getObject();

    		if ((className != null) && (name != null) && (o != null)) {
    			if (o instanceof javax.naming.Context) {
    				listContext(jndiList, ctx, path + "/" + name);
    			} else {
    				jndiList.put(path + "/" + name, className);
    			}
    		}
    	}
    }


	public static void printRequest( HttpServletRequest request) {
		
		String funcName = "printRequest";
		
		log.debug(funcName + ": enter -------------------------------------------------");
		
		Enumeration e = request.getParameterNames();
		
		while( e.hasMoreElements() ) {
		   String strName = (String) e.nextElement() ;
		   
		   log.debug(funcName + ": request: " + strName + " -> " + request.getParameter(strName));
	   
	   }
	}

	
	public static void printSession( HttpServletRequest request) {
		
		String funcName = "printSession";
		
		log.debug(funcName + ": enter -------------------------------------------------");
		
		Enumeration e = request.getSession().getAttributeNames();
		
		while( e.hasMoreElements() ) {
		   String strName = (String) e.nextElement() ;
		   
		   log.debug(funcName + ": session: " + strName + " -> " + request.getParameter(strName));
	   
	   }
	}
	
	/**
	 * Copia el contenido del archivo in al archivo out.
	 * 
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	public static void copyFile(File in, File out) throws Exception {
		 	FileInputStream fis  = new FileInputStream(in);
	        FileOutputStream fos = new FileOutputStream(out);
	        byte[] buf = new byte[1024];
	        int i = 0;
	        while((i=fis.read(buf))!=-1) {
	            fos.write(buf, 0, i);
	        }
	        fis.close();
	        fos.close();
	}

	/**
	 * Crea una copia del archivo "fileName" y lo graba con el nombre "newFileName".
	 * (ambos parametros deben incluir el path del archivo)
	 * 
	 * @param fileName
	 * @param newFileName
	 * @throws Exception
	 */
	public static void copyFile(String fileName, String newFileName) throws Exception {
	 	File in = new File(fileName);
	 	File out = new File(newFileName);
		FileInputStream fis  = new FileInputStream(in);
        FileOutputStream fos = new FileOutputStream(out);
        byte[] buf = new byte[1024];
        int i = 0;
        while((i=fis.read(buf))!=-1) {
            fos.write(buf, 0, i);
        }
        fis.close();
        fos.close();
	}
	
	/**
	 * Graba el contenido en el archivo pathName
	 * @param pathName: la ruta completa del archivo
	 * @param contenido
	 * @param append indica si se escribe al final del archivo en caso de que ya exista
	 * @author arobledo
	 * @throws Exception
	 */
	public static void grabarArchivo(String pathName, byte[] contenido, boolean append ) throws Exception{
		log.debug("grabarArchivo - enter");
		FileOutputStream out = new FileOutputStream(pathName, append);
		out.write(contenido);
		out.close();
		log.debug("grabarArchivo - exit");
	}
	
	/**
	 * Elimina un archivo.
	 * @param pathName
	 * @return el resultado de la eliminacion
	 */
	public static boolean eliminarArchivo(String pathName){
		log.debug("eliminarArchivo - enter");
		File f = new File(pathName);
		boolean ret = f.delete();
		log.debug("eliminarArchivo - exit - elimino:"+ret);
		return ret;
	}

	public static Object nvl(Object o, Object defobj) {
		return o == null ? defobj : o;
	}
}
