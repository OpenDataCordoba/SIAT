//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

/*
 * Aloja la propiedades del objeto base del model
 *
 * @author tecso:
 * @version  2.0
 */

package coop.tecso.demoda.iface.model;
                                                                         
import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coop.tecso.demoda.iface.helper.ModelUtil;

public class Common implements Serializable {
	
	static final long serialVersionUID = 0;
	
	public final static long HASACCESS_NOMBRESNULOS_ERROR = -1;
	public final static long HASACCESS_SINACCESO = 0;
	public final static long HASACCESS_OK = 1;
	public final static long HASACCESS_OK_NOEXISTE = 2;

    public static final long ERROR_TYPE_RECOVERABLE = 0;
    public static final long ERROR_TYPE_NON_RECOVERABLE = 1;
    
    private Long id;
    private long errorType = ERROR_TYPE_RECOVERABLE;
    private List<DemodaStringMsg> listError = new ArrayList<DemodaStringMsg>();
    private List<DemodaStringMsg> listMessage = new ArrayList<DemodaStringMsg>();
    
	public Common()  {
        super();
    }

	/**
	 * @return Devuelve el atributo id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id Fija el atributo id.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/* Obtiene el tipo de error que se correponde con los errores cargados 
	 * los estados pueden ser: Common.ERROR_TYPE_RECOVERABLE = 0, Common.ERROR_TYPE_NON_RECOVERABLE = 1 
	*/
	public long getErrorType() {
		return this.errorType;
	}

	/** Por favor, use este metodo solo si sabe lo que esta haciendo. 
	 * <p>Es recomendable que utilice los metodos addRecoverableError(),  
	 * addNonRecoverableError() y clearError() que manejan el estado correctamente.
	*/
	public void setErrorType(long errorType) {
		this.errorType = errorType;
	}

	public List<DemodaStringMsg> getListError() {
		return listError;
	}

	public void setListError(List<DemodaStringMsg> listError) {
		this.listError = listError;
	}

	public List<DemodaStringMsg> getListMessage() {
		return listMessage;
	}

	public void setListMessage(List<DemodaStringMsg> listMessage) {
		this.listMessage = listMessage;
	}	

	/** Limpia la lista de mensajes
	*/
	public void clearMessage() {
		listMessage = new ArrayList<DemodaStringMsg>();
	}
	
	/** 
	 * Agrega un Mensaje a la lista de Mensajes del Common
	 * Sin parametros
	*/
	public void addMessage(String messageConst) {
		DemodaStringMsg dsm = new DemodaStringMsg(messageConst);
		addMessage(dsm);
	}
	
	/**
	 * Con un lista de parametros 
	 */
	public void addMessage(String messageConst, Object[] arrStr) {
		 
		DemodaStringMsg dsm = new DemodaStringMsg(messageConst, arrStr);
		addMessage(dsm);
	}
	
	/**
	 * Con 1 parametro 
	 */
	public void addMessage(String messageConst, String str) {
		Object[] arrStr = new Object[]{str};		
		addMessage(messageConst, arrStr);
	}
	
	/**
	 * Con 2 parametros 
	 */
	public void addMessage(String messageConst, String str0, String str1) {
		Object[] arrStr = new Object[]{str0, str1};		
		addMessage(messageConst, arrStr);
	}
	
	/**
	 * Con 3 parametros 
	 */
	public void addMessage(String messageConst, String str0, String str1, String str2) {
		Object[] arrStr = new Object[]{str0, str1, str2};		
		addMessage(messageConst, arrStr);
	}
	
	/**
	 * Agrega el valor de un mensaje de error, no sera buscado por clave en propertie
	 *  
	 * @param errorValue
	 */
	public void addMessageValue(String errorValue) {
		// agrego el ampersand para indicar que es un valor
		DemodaStringMsg dsm = new DemodaStringMsg(0, "&" + errorValue);
		addMessage(dsm);
	}
	
	/** Agrega un Mensaje a la lista de Mensajes del Common
	*/
	public void addMessage(DemodaStringMsg message) {
		listMessage.add(message);
	}

	/** Indica si posee mensajes */
	public boolean hasMessage() {
		return !listMessage.isEmpty();
	}

	/** Agrega un error Recuperable a la lista de erores. 
	 * En caso de que halla algun error No Recuperable, entonces la funcion
	 * no agregar el error.
	*/
	public void addRecoverableError(String errorConst) {
		DemodaStringMsg dsm = new DemodaStringMsg(errorConst);
		addRecoverableError(dsm);
	}
	
	/** Agrega un error Recuperable a la lista de erores.
	 *  Este error es un valor, no se saca de la key. 
	 * En caso de que halla algun error No Recuperable, entonces la funcion
	 * no agregar el error.
	*/
	public void addRecoverableValueError(String errorValue) {
		// agrego el ampersand para indicar que es un valor
		DemodaStringMsg dsm = new DemodaStringMsg(0,"&" + errorValue);
		addRecoverableError(dsm);
	}
	
	/** 
	 * Agrega un error Recuperable a la lista de erores. 
	 * En caso de que halla algun error No Recuperable, entonces la funcion
	 * no agregar el error.
	 * 
	 * Sin Parametros
	 */
	public void addRecoverableError(DemodaStringMsg error) {
		if (!hasErrorNonRecoverable()) {
			listError.add(error);
		}
	}
	
	/**
	 * Con una lista de parametros
	 * 
	 */
	public void addRecoverableError(String errorConst, Object[] arrayStr) {
		DemodaStringMsg dsm = new DemodaStringMsg(errorConst, arrayStr);
		
		if (!hasErrorNonRecoverable()) {
			listError.add(dsm);
		}
	}
	
	/**
	 * Para 1 parametro
	 */
	public void addRecoverableError(String errorConst, String  strArg) {
		Object[] arrayStr = new Object[]{strArg};
		addRecoverableError(errorConst, arrayStr);
	}
	
	/**
	 * Para 2 parametros
	 */
	public void addRecoverableError(String errorConst, String  strArg0, String  strArg1) {
		Object[] arrayStr = new Object[]{strArg0, strArg1 };
		addRecoverableError(errorConst, arrayStr);
	}
	
	/**
	 * Para 3 parametros
	 */
	public void addRecoverableError(String errorConst, String  strArg0, String  strArg1, String strArg2) {
		Object[] arrayStr = new Object[]{strArg0, strArg1, strArg2 };
		addRecoverableError(errorConst, arrayStr);
	}
	
	/** Agrega un error No Recuperable a la lista de erores. 
	 * <p>Si hay errores Recoverable, limpia la lista de errores,
	 * coloca el nuevo error Non Recoverable.
	 * <p>Si la lista no posee errores, o posee errores Non Recoverable 
	 * simplemete agrega el error.
	*/
	public void addNonRecoverableError(DemodaStringMsg error) {
		if (hasErrorRecoverable()) {
			clearError();
		}
		listError.add(error);
	}

	/** Agrega un error No Recuperable a la lista de erores. 
	 * <p>Si hay errores Recoverable, limpia la lista de errores,
	 * coloca el nuevo error Non Recoverable.
	 * <p>Si la lista no posee errores, o posee errores Non Recoverable 
	 * simplemete agrega el error.
	*/
	public void addNonRecoverableError(String errorConstant) {
		DemodaStringMsg dsm = new DemodaStringMsg(errorConstant);
		addNonRecoverableError(dsm);
	}

	
	public void addNonRecoverableValueError(String errorValue) {
		DemodaStringMsg dsm = new DemodaStringMsg(0,"&" + errorValue);
		addNonRecoverableError(dsm);
	}
	
	/** Limpia la lista de errores */
	public void clearError() {
	    this.errorType = ERROR_TYPE_RECOVERABLE;
	    this.listError = new ArrayList<DemodaStringMsg>();
	}

	/** Indica si posee erores Recuperables */
	public boolean hasErrorRecoverable() {
		return !listError.isEmpty() && errorType == ERROR_TYPE_RECOVERABLE;
	}

	/** Indica si posee erores No Recuperables */
	public boolean hasErrorNonRecoverable() {
		return !listError.isEmpty() && errorType == ERROR_TYPE_NON_RECOVERABLE;
	}

	/** Indica si posee erores de cualquier tipo */
	public boolean hasError() {
		return !listError.isEmpty();
	}

	
	public boolean hasErrorOrMessage(){
		return !listError.isEmpty() || !listMessage.isEmpty();
	}
	
	/** Transfiere la lista de errores y mensages de este objeto al objeto target del parametro.
	 * Tambien pasa el atributo errorType de uso interno para determinar el tipo de errores en la lista.  
	*/
	public void passErrorMessages(Common target) {
		target.setListError(this.getListError());
		target.setListMessage(this.getListMessage());
		target.setErrorType(this.getErrorType());
	}

	/**
	 * Agrega la lista de errores y mensajes de este objeto al objeto target parametro.
	 * @param target
	 */
	public void addErrorMessages(Common target) {
		target.getListError().addAll(this.getListError());
		target.getListMessage().addAll(this.getListMessage());
	}


	/** Limpia la lista de errores y mensajes */
	public void clearErrorMessages() {
		this.clearError();
		this.clearMessage();
	}
	
	/** sobrecarge este metodo para mostrar un mesaje de error adecuado para usar con fines de logueos */
	public String errorString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append(this.getClass().getName()).append(": errors=[");
		for(DemodaStringMsg dsm : listError) {
			sb.append(dsm.number()).append(":").append(dsm.key()).append(", ");
		}
		sb.append("]  messages=[");
		for(DemodaStringMsg dsm : listMessage) {
			sb.append(dsm.number()).append(":").append(dsm.key()).append(", ");
		}
		sb.append("]");

		return sb.toString();
	}

	/** sobrecarge este metodo para mostrar los datos de Bean o VO para usar a fines de logueos */
	public String infoString() {
		try {
			String infoString ="[";
			Class myClass = this.getClass();
			Method methods[] = myClass.getMethods();
	
			// itero todos los metodos del BO
	        for (int i = 0; i < methods.length; i++) {

	        	Method method = methods[i];
	        	String methodName = method.getName(); 

	        	// pregunto si el metodo es un geter y si no tiene parametros
	        	if ( methodName.startsWith("get") && method.getParameterTypes().length == 0 ) {

		        	// si es un tipo basico lo loguea
		        	if ( ModelUtil.isBasicType(method) && method.invoke(this)!=null &&
		        			!methodName.substring(3, methodName.length()).equals("Usuario")) {
		        		infoString =  infoString + methodName.substring(3, methodName.length()) + 
		        		"=" + method.invoke(this) + "; ";
		        	}
		        }
	        }
			infoString += "]";

	        return infoString;			
        } catch (Exception e) {
			return this.toString();
        }

	}
	

	protected static String getStringByDate(Date fecha) {
		if (fecha==null)
			return "";
		try {
			return new SimpleDateFormat("dd/MM/yyyy").format(fecha);
		} catch (Exception e) {
			return "fecha invalida";
		}
	}

	/**
	 * Mismos metodo que hasError(), comienza con "get" para poder ser accedido desde la vista
	 * 
	 * 
	 * @return boolean
	 */
	public boolean getHasError(){
		return hasError();
	}
	
	/**
	 * Metodo que puede ser implementado o no segun, la clase posea caso.
	 * Es utilizado en el servicio de registro de uso de expedientes.
	 * 
	 * @author Cristian
	 * @return
	 */
	public String getRepresent() {
		return "";
	}
}
