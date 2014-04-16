//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.BussImageModel;
import coop.tecso.demoda.iface.model.Datum;
import coop.tecso.demoda.iface.model.Vigencia;

/**
 * Definicion y Valores de un Atributo abstracto
 * <p>Esta clase es la union de los datos de un atributo de defincion y los valores 
 * cargados de ese atributo. La idea es aplanar la estructura relacional, y que 
 * exista esta clase que contiene Descripcion del Atributo y Valor/Valores del Atributo.
 * 
 * <p>Esta clase posee una lista con los valores del atributo, una lista con los valores 
 * historicos del atributo, un AtributoVO junto con sus valores de dominino.
 * 
 * <p>La clase se utiliza tanto para consulta / busquedas / Modificacion / Alta de valores
 * de atributos.
 * <p>NOTA: Esta clase posee una LISTA de valores para un atributo, que representan bien,
 * <br>- Los posibles valores de un atributo Multivalor
 * <br>- Los posibles valores de un atributo en busqueda por rangos 
 * <br>Cuando solo es necesario un valor, posee una lista con un unico atributo.
 * 
 * <p>Ademas posee una lista AtrValHis, de solo lectura para acceder los valores de vigencias
 * del Atributo.
 * 
 * @author Tecso Coop. Ltda.
 *
 */
public abstract class AtrValDefinition extends BussImageModel {

	private static final long serialVersionUID = 1L;

	Logger log = Logger.getLogger(AtrValDefinition.class);
		
	public static String ACT_BUSCAR = "buscar"; 
	public static String ACT_MANUAL = "manual";
	public static String ACT_INTERFACE = "interface";
	public static String ACT_WEB = "web";
	
	public AtrValDefinition() {
	}

	private List<Object> listValor = new ArrayList<Object>();
	private List<AtrValVigDefinition> listAtrValVig = new ArrayList<AtrValVigDefinition>();
	private Date fechaDesde = null;
	private Date fechaHasta = null;
	private Date fechaNovedad = null;
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String fechaNovedadView = "";
	
	private boolean isSubmited = false;

	private List<Date> listMultiFechaDesde = new ArrayList<Date>();
	private List<Date> listMultiFechaHasta = new ArrayList<Date>();
	private List<Date> listMultiFechaNovedad = new ArrayList<Date>();

	// Getters y Setters
	/**
	 * Lista de valores vigentes del Atributo.
	 * @return the listAtrValVig
	 */
	public List<AtrValVigDefinition> getListAtrValVig() {
		return listAtrValVig;
	}
	/**
	 * @param listAtrValVig the listAtrValVig to set
	 */
	public void setListAtrValVig(List<AtrValVigDefinition> listAtrValVig) {
		this.listAtrValVig = listAtrValVig;
	}
	
	/**
	 * Valores del atributo.
	 * <p>Puede ser un unico valor, o mas de uno para el caso
	 * de busquedas por rango o atributos con multi valor.
	 * @return the listValor
	 */
	public List<Object> getListValor() {
		return listValor;
	}
	
	public List<String> getListMultiValor() {
		List<String> listString = new ArrayList<String>();
		
		for(Object o:listValor){
			Object[] arrObj = (Object[])o;
			listString.add((String) arrObj[0]);
		}
		return listString;
	}	
	
	/**
	 * @param listValores the listValor to set
	 */
	public void setListValor(List<Object> listValor) {
		this.listValor = listValor;
	}

	/**
	 * @return the fechaDesde
	 */
	public Date getFechaDesde() {
		return fechaDesde;
	}
	/**
	 * @param fechaDesde the fechaDesde to set
	 */
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);

	}
	/**
	 * @return the fechaHasta
	 */
	public Date getFechaHasta() {
		return fechaHasta;
	}
	/**
	 * @param fechaHasta the fechaHasta to set
	 */
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	/**
	 * @return the fechaNovedad
	 */
	public Date getFechaNovedad() {
		return fechaNovedad;
	}
	/**
	 * @param fechaNovedad the fechaNovedad to set
	 */
	public void setFechaNovedad(Date fechaNovedad) {
		this.fechaNovedad = fechaNovedad;
		this.fechaNovedadView = DateUtil.formatDate(fechaNovedad, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public String getFechaNovedadView() {
		return fechaNovedadView;
	}
	public void setFechaNovedadView(String fechaNovedadView) {
		this.fechaNovedadView = fechaNovedadView;
	}
	
	public boolean getIsSubmited() {
		return isSubmited;
	}
	public void setIsSubmited(boolean isSubmited) {
		this.isSubmited = isSubmited;
	}

	/**
	 * Indica si el atributo posee dominio de valores
	 * @return true si posee dominio.
	 */
	public boolean getPoseeDominio() {
		
		if ( this.getAtributo() != null && 
				!ModelUtil.isNullOrEmpty( this.getAtributo().getDomAtr()) )
			return true;
		else		
			return false;
	}

	
	// Metodos Abstractos

	/**	
	 * Indica si el atributo es multivalor.
	 * @note Este metodo debe ser implementado en la clase hija
	 * @return true si es multivalor
	 */
	public abstract boolean getEsMultivalor();
	
	
	/**
	 * Indica si el atributo es requerido para la edicion. 
	 * @return
	 */	
	public abstract boolean getEsRequerido();
	
	
	/**	
	 * Indica si el atributo esta marcado como que posee vigencia.
	 * @note Este metodo debe ser implementado en la clase hija
	 * @return true si posee vigencia
	 */
	public abstract boolean getPoseeVigencia();

	/**	
	 * Indica si el atributo Adminite Busquedas por Rango
	 * @note Este metodo debe ser implementado en la clase hija
	 * @return true si admite busquedas.
	 */
	public abstract boolean getAdmBusPorRan();
	
	/**
	 * Informacion del Atributo. e.g: Tipo de Atributo, Descripcion, Codigo,
	 * Dominio, Valores del Dominico, etc.
	 * @note Este metodo debe ser implementado en la clase hija
	 * @return the atributo
	 */
	public abstract AtributoVO getAtributo();

	/**
	 * Id de atributo especializado o tipo de atributo. Es decir:
	 * para el caso de de Objetos Imponible, es el id de TipObjImpAtr,
	 * para el caso de Contribuyente es el id de ConAtr, y asi. 
	 * @note Este metodo debe ser implementado en la clase hija
	 * @return the atributo
	 */
	public abstract Long getIdDefinition();

	
	// Metodos de negocio y vista
	/**
	 * Agrega un valor de vigencia a la lista de vigencias.
	 * <p>IMPORTANTE: la lista de vigencias se utiliza solo a nivel informativo.
	 * Dichos valores no se utilizan durante el alta o modificaciones, para ello
	 * se usa los de AtrVal.
	 */
	public void addAtrValVig(String valor, Date fechaNovedad, Date fechaDesde, Date fechaHasta) {		
		AtrValVigDefinition atrValVigDef = new AtrValVigDefinition();
		atrValVigDef.setValor(valor);
		atrValVigDef.setFechaNovedad(fechaNovedad);
		atrValVigDef.setFechaDesde(fechaDesde);
		atrValVigDef.setFechaHasta(fechaHasta);
		listAtrValVig.add(atrValVigDef);
	}
	
	/**
	 * Crea un array de object para agregar a listValor.
	 * En la posicion 0 se guarda un String formateado para la vista.
	 * En la posicion 1 un Object obtenido a partir del valorOrig y el tipo de dato del atributo.
	 * 
	 * @param valorOrig tiene que se un string en formato YYYYMMDD, o NN.NN, o NN para
	 * fechas, doubles y enteros.
	 * @return true si valorOrig posee un valor adecuado y el tipo del valor corresponde al del atributo
	 */
	public boolean addValor(String valorOrig) {
		Object[] strVal = new Object[2];
		String strView = valorOrig;
		boolean tipoOk = true;
		Object objectFormated = this.convertFromDB(valorOrig);
		
		// si falla la convesion, dejamos intacto el valor original
		// y salimos
		if (objectFormated == null) {
			strVal[0] = strView; 
			strVal[1] = null; 					
			listValor.add(strVal);
			return false;			
		}
	
		String codTipoDato = this.getAtributo().getTipoAtributo().getCodTipoAtributo();
		// Validacion que corresponda el tipo de dato recibido con el de la definicion
		if (TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(codTipoDato)){
			if (!Long.class.isInstance(objectFormated)){
				tipoOk = false;
			}else{
				strView = StringUtil.formatLong((Long) objectFormated);
			}
		}
		if (TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(codTipoDato)){
			if (!Double.class.isInstance(objectFormated)){
				tipoOk = false;
			}else{
				strView = StringUtil.formatDouble((Double) objectFormated);
			}
		}
		if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(codTipoDato)){
			if (!Date.class.isInstance(objectFormated)) {
				tipoOk = false;
			}else{
				strView = DateUtil.formatDate((Date)objectFormated, DateUtil.ddSMMSYYYY_MASK);
			}					
		}
		if (TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(codTipoDato)){
			if (!String.class.isInstance(objectFormated))
				tipoOk = false;
			else
				strView = (String) objectFormated; 
		}
		if (TipoAtributoVO.COD_TIPO_ATRIB_CATASTRAL.equals(codTipoDato)){
			if (!String.class.isInstance(objectFormated))
				tipoOk = false;
			else
				strView = (String) objectFormated; 
		}
		
		strVal[0] = strView; 
		strVal[1] = objectFormated; 
				
		listValor.add(strVal);
		return tipoOk;
	}
	
	/**
	 * Agrega un valor submitido por la vista. 
	 * 
	 * @param valorView
	 * @return true si valorView posee un valor con formato adecuado
	 */
	public boolean addValorView(String valorView) {		
		Object objectFormated = this.convertFromView(valorView);
		Object[] strVal = new Object[2];
		
		// si falla la convesion, dejamos intacto el valor original
		// y salimos
		if (objectFormated == null) {
			strVal[0] = valorView; 
			strVal[1] = null; 					
			listValor.add(strVal);
			return false;			
		}		
		
		strVal[0] = valorView; 
		strVal[1] = objectFormated;
		listValor.add(strVal);
		return true;
	}
	
	/** 
	 * Agrega un valor a la lista de valores seteando fechas de vigencia
	 * <p>Estos valores son utilizados durante alta y modif del atributo
	 * @param valor valor a agregar
	 */
	public boolean addValor(String valorOrig, Date fechaNovedad, Date fechaDesde, Date fechaHasta) {		
		setFechaNovedad(fechaNovedad);
		setFechaDesde(fechaDesde);
		setFechaHasta(fechaHasta);
		
		/* 2009-11-04 fedel: fix bug 922, la idea es solo usar estas listas al momento de 
		 * guardar los valores en ObjImp.updateObjImpAtrDefinition() */
		/* como en fix, y para no causar mucho impacto, creamos tres array, envezde usar una estructura propia
		 * o usar una lista AtrValDefinition */
		this.listMultiFechaDesde.add(fechaDesde);
		this.listMultiFechaHasta.add(fechaHasta);
		this.listMultiFechaNovedad.add(fechaNovedad);

		return addValor(valorOrig);
	}
	
	/**
	 * Devuelve el String de la posicion 0 del array de Object, que se encuentra en la posision 0 de listValor.
	 * 
	 * @return String
	 */
	public String getValorView() {		
		try{
			if (listValor != null && listValor.size() > 0 && listValor.get(0) != null){
				Object[] strVal= (Object[]) listValor.get(0);			
				return (String) strVal[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	
	
	/**
	 * Metodo para simular un cambio de valor
	 * Por ejemplo cuando se levanta una definicion y con valores por defecto y luego hay que cambiarla.
	 * 
	 * @author Cristian
	 * @param valor
	 */
	public void setValor(String valor){
		this.reset();
		this.addValor(valor);		
	}
	
	
	/**
	 * Devuelve el Object de la posicion 1 del array de object, de la posision 0 de listValor.
	 * Se utiliza para realizar calculos con objectos debidamente tipados.
	 * 
	 * @return Object
	 */
	public Object getValorObjet() {		
		try{
			if (listValor != null && listValor.size() > 0 && listValor.get(0) != null){
				Object[] strVal= (Object[]) listValor.get(0);			
				return strVal[1];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Devuelve el String formateado para guardar en el campo "strValor" del atrVal correspondiente.
	 * 
	 * @return String
	 */
	public String getValorString() {		
		try{
			 
			if (listValor != null && listValor.size() > 0 && listValor.get(0) != null){
				Object[] arrObj= (Object[]) listValor.get(0);			
				
				Object valObject = arrObj[1];
				
				String codTipoDato = this.getAtributo().getTipoAtributo().getCodTipoAtributo();
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(codTipoDato)){
					return (String) valObject;
				}
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_CATASTRAL.equals(codTipoDato)){
					return (String) valObject;
				}
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(codTipoDato)){
					return StringUtil.formatLong((Long) valObject);
				}
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(codTipoDato)){
					return StringUtil.formatDouble((Double) valObject);
				}
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(codTipoDato)){
					return DateUtil.formatDate((Date) valObject, DateUtil.YYYYMMDD_MASK);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Devuelve el String formateado de una posicion especifica de listValor, 
	 * para guardar en el campo "strValor" del atrVal correspondiente.
	 * 
	 * @param inx
	 * @return
	 */
	public String getValorString(int inx) {		
		try{
			
			if (listValor != null && listValor.size() > 0 && listValor.size() > inx && listValor.get(inx) != null){
				Object[] arrObj= (Object[]) listValor.get(inx);			
				
				Object valObject = arrObj[1];
				
				String codTipoDato = this.getAtributo().getTipoAtributo().getCodTipoAtributo();
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(codTipoDato)){
					return (String) valObject;
				}
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_CATASTRAL.equals(codTipoDato)){
					return (String) valObject;
				}
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(codTipoDato)){
					return StringUtil.formatLong((Long) valObject);
				}
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(codTipoDato)){
					return StringUtil.formatDouble((Double) valObject);
				}
				
				if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(codTipoDato)){
					return DateUtil.formatDate((Date) valObject, DateUtil.YYYYMMDD_MASK);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * Devuelve un array de String, utilizado en la vista con los valores de listValor
	 * 
	 * @return listValorView
	 */
	public List<String> getListValorView() {
		
		List<String> listValorView = new ArrayList<String>();
		
		for(Object item:listValor){
			Object[] arrObj= (Object[]) item;
			listValorView.add((String)arrObj[0]);
		}
		
		return listValorView;
	}
	
	/**
	 * Devuelve la lista String de un Atributo Multivalor formateada para guardar en el 
	 * campo "strValor" de los atrVal correspondientes.
	 * 
	 * @return listValor
	 */
	public List<String> getMultiValor() {
		
		List<String> listStrValor = new ArrayList<String>();
		
		if (listValor != null){
			for (int c = 0; c < listValor.size(); c++){
				listStrValor.add(this.getValorString(c));
			}
		}
		
		return listStrValor;
	}
	
	/**
	 * Devuelve la lista String de un Atributo Multivalor formateada para mostrar en la vista.
	 * 
	 * @return listValor
	 */
	public List<String> getMultiValorView() {
		
		List<String> listStrValor = new ArrayList<String>();
		
		if (listValor != null){
			for (int c = 0; c < listValor.size(); c++){
				listStrValor.add(this.getFromDominioView(this.getValorString(c)));
			}
		}
		
		return listStrValor;
	}	
	
	/**
	 * Lista de multi valores teniendo encuenta vigencias.
	 * @param fechaAnalisis
	 * @return
	 */
	public List<String> getMultiValorView(Date fechaAnalisis) {		
		List<String> listStrValor = new ArrayList<String>();

		if (listValor != null){
			for (int c = 0; c < listValor.size(); c++){
				Date fechaHasta = this.listMultiFechaHasta.get(c);
				if (fechaHasta == null ||fechaAnalisis.compareTo(fechaHasta) <= 0 ) {				
					listStrValor.add(this.getFromDominioView(this.getValorString(c)));
				}
			}
		}
		
		return listStrValor;
	}	
	
	/**
	 * Lista de multi valores teniendo encuenta vigencias (sin preparar para la vista: no busca el dominio).
	 * @param fechaAnalisis
	 * @return
	 */
	public List<String> getMultiValor(Date fechaAnalisis) {		
		List<String> listStrValor = new ArrayList<String>();

		if (listValor != null){
			for (int c = 0; c < listValor.size(); c++){
				Date fechaHasta = this.listMultiFechaHasta.get(c);
				if (fechaHasta == null ||fechaAnalisis.compareTo(fechaHasta) <= 0 ) {				
					listStrValor.add(this.getValorString(c));
				}
			}
		}
		
		return listStrValor;
	}	

	/**
	 * Obtiene el valor a mostrar en la vista, para el caso de los multivalores.
	 * 
	 * @param _valor
	 * @return
	 */
	private String getFromDominioView(String _valor) {

		String valorRet = "";
		AtributoVO atributoVO = this.getAtributo();
		
		if ( atributoVO != null ) {
			
			DomAtrVO domAtrVO = atributoVO.getDomAtr();
			
			if (domAtrVO != null) {
				valorRet = domAtrVO.getDesValorByCodigo(_valor);
			}
		}		

		return valorRet;

	}
	
	/**
	 * Devuelve el codigo de TipoAtributoVO correspondiente al Atributo.
	 * 
	 * Long - LONG
	 * String - STRING
	 * Double - DOUBLE
	 * Date - DATE
	 * Catastral - CATASTRAL 
	 * 
	 * @return
	 */
	public String getCodTipoAtributo(){
		
		return this.getAtributo().getTipoAtributo().getCodTipoAtributo();
	}
	
	/**
	 * Limpia la lista de valores
	 * @param valor valor a agregar
	 */
	public void resetListValor() {		
		listValor = new ArrayList<Object>();
	}
	
	/**
	 * Limpia la lista de valores, vigencias y las fechas.
	 * @param valor valor a agregar
	 */
	public void reset() {		
		listValor = new ArrayList<Object>();
		listAtrValVig = new ArrayList<AtrValVigDefinition>();
		fechaDesde = null;
		fechaHasta = null;
		fechaNovedad = null;
	}
	
	/**
	 *  
	 * @return El primero de los dos valores que "DEBE" tener listValor para el caso de:
	 * No posee dominio y si Admite busqueda por rango.
	 * Este valor puede ser nulo, pero no puede no existir, para porder tener en la segunda posicion de listValor el valor
	 * correspondiente a ValorHasta. 
	 */
	public String getValorDesdeView() {
		return getValorView();
	}
	
	/**
	 * 
	 * @return El segundo de los dos valores o ""
	 */
	public String getValorHastaView() {		
		if (listValor != null && listValor.size() > 1 && listValor.get(1) != null){
			Object[] strVal = (Object[]) listValor.get(1);			
			return (String) strVal[0];
		}

		return "";
	}
	
	/**
	 *  
	 * @return El "valoDesde" formateado para realizar busquedas. 
	 */
	public String getValorDesdeString() {
		return getValorString();
	}
	
	/**
	 *  
	 * @return El "valoHasta" formateado para realizar busquedas. 
	 */
	public String getValorHastaString() {		
		return getValorString(1);
	}
	
	/**
	 * Valida el/los valores del atributo respecto de la sintaxis y dentro del 
	 * dominio cuando corresponde.
	 * Tiene en cuenta el "act" para el cual se realiza la validacion, osea el mismo para el cual se 
	 * obtuvo previamente al definicion.
	 * 
	 * @return true si el valor es correcto.
	 */
	public boolean validate(String act) {
		
		if (ACT_BUSCAR.equals(act)){
			String codTipoDato = this.getAtributo().getTipoAtributo().getCodTipoAtributo();
			// No posee dominio
			if( !getPoseeDominio()){
				// Adminte Busqueda por rango
				if (getAdmBusPorRan()){					
					String strValorDesde = this.getValorDesdeView();
					String strValorHasta = this.getValorHastaView();
					
					if ( !validarTipoDato(strValorDesde, codTipoDato) || 
							!validarTipoDato(strValorHasta, codTipoDato))
						return false;
					
					// Si es un rango de Fechas o numerico, chequeo que desde sea menor que hasta
					// Si los dos valores fueron ingresados
					if (!strValorDesde.equals("") && !strValorHasta.equals("") ){
						// Rango de fechas
						if(TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(codTipoDato)){
							Date fechaDesde = DateUtil.getDate(strValorDesde);
							Date fechaHasta = DateUtil.getDate(strValorHasta);
						
							if (DateUtil.isDateBefore(fechaHasta, fechaDesde))
								return false;
						}
					
					    // Rango de valores long
						if ( TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(codTipoDato)){
							Long longDesde = new Long(strValorDesde);
							Long longHasta = new Long(strValorHasta);
							
							if (longHasta < longDesde)
								return false;
						}
					   
						// Rango de valores double
						if ( TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(codTipoDato)){
							Double doubleDesde = new Double(strValorDesde);
							Double doubleHasta = new Double(strValorHasta);
							
							if (doubleHasta < doubleDesde)
								return false;
						}
					}
				
				// No Adminte Busqueda por rango
				}  else {
					String strValor = this.getValorView();
					return validarTipoDato(strValor, codTipoDato);
				}
			}
			
		} else if (ACT_MANUAL.equals(act)){
			String codTipoDato = this.getAtributo().getTipoAtributo().getCodTipoAtributo();
			// No posee dominio
			if( !getPoseeDominio()){
				// Si es Requerido veo que tenga valor seteado, o no sea valor Seleccionar si es combo  
				if (getEsRequerido() && (this.getValorView() == null || this.getValorView().equals("") )){
					return false;
				}
				
				String strValor = this.getValorView();
				return validarTipoDato(strValor, codTipoDato);
			}
			
			// Posee dominio
			if( getPoseeDominio()){
				// Si es Requerido veo que tenga valor seteado, o no sea valor Seleccionar si es combo  
				if (getEsRequerido() && (this.getValorView() == null || this.getValorView().equals("") || this.getValorView().equals("-1") )){
					return false;
				}
			}
			
		} else if (ACT_INTERFACE.equals(act)){
			String codTipoDato = this.getAtributo().getTipoAtributo().getCodTipoAtributo();
			String strValor = this.getValorView();
			if(!StringUtil.isNullOrEmpty(strValor)){
				if( !getPoseeDominio()){
					return validarTipoDato(strValor, codTipoDato);
				}else{
					return validarValorEnDomAtr(strValor,this.getAtributo().getDomAtr());
				}
			}
		} else {
			// No fue seteado el act
			return false;			
		}
		
		return true;
		
	}
	

	/**
	 * 
	 * Helper para validar formato segun el tipo de datos del atributo.
	 * 
	 * @param strValor
	 * @param codTipoDato
	 * @return
	 */
	private boolean validarTipoDato( String strValor, String codTipoDato){
		
		if (!strValor.trim().equals("")){
			   
		   // Valido Tipo de datos LONG
		   if ( TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(codTipoDato)){
			   if (!StringUtil.isLong(strValor)){
				   return false;			   
			   }							   
		   }
		   
		   // Valido Tipo de datos DOUBLE
		   if ( TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(codTipoDato)){
			   if (!StringUtil.isDouble(strValor)){
				   return false;
			   }							   
		   }
		   
		   // Valido Tipo de datos DATE
		   if ( TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(codTipoDato)){
			   if (!DateUtil.isValidDate(strValor)){
				   return false;
			   }
		   }
		   
		}
		
		return true;
	}
	
	/**
	 * Valida que un valor sea parate de del dominio de un atributo.
	 * 
	 * @param strValor
	 * @param domAtr
	 * @return
	 */
	private boolean validarValorEnDomAtr( String strValor, DomAtrVO domAtr){
		for (DomAtrValVO item: domAtr.getListDomAtrVal()){
			if(strValor.equals(item.getValor()))
				return true;
		}
		return false;
	}
	
	/**
	 * Dado el valor recibido y el tipo de datos del atributo, devuelve un Object formateado segun los formatos convenidos
	 * guardados en strValor de la DB.
	 * 
	 * 
	 * @param valor
	 * @return
	 */
	public Object convertFromDB(String valor){
		try{
			if (TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(this.getCodTipoAtributo()) ){
				return valor;
			} else if (TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals( this.getCodTipoAtributo()) ){
				return new Long(valor);
			} else if (TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals( this.getCodTipoAtributo()) ){
				return new Double(valor);
			} else if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals( this.getCodTipoAtributo()) ){
				return DateUtil.getDate(valor, DateUtil.YYYYMMDD_MASK);
			} else if (TipoAtributoVO.COD_TIPO_ATRIB_DOMICILIOENVIO.equals( this.getCodTipoAtributo())){
				// TODO: colocar aqui el comportamiento correspondiente
				return valor;
			} else if (TipoAtributoVO.COD_TIPO_ATRIB_DOMICILIO.equals( this.getCodTipoAtributo())){
				// TODO: colocar aqui el comportamiento correspondiente
				return valor;
			} else if (TipoAtributoVO.COD_TIPO_ATRIB_CATASTRAL.equals( this.getCodTipoAtributo())){
				// TODO: colocar aqui el comportamiento correspondiente
				return valor;
			} else {
				log.error("covertFromDB: El tipo de atributo no esta seteado");
				return null;
			}
		} catch (Exception e){
			if (log.isDebugEnabled()) log.debug("convertFromDB: " + e.getMessage()); 
			return null;
		}
	}
	
	/**
	 * Intenta convertir los valores capturados en la vista, a los formatos convenidos.
	 * Si no puede devuelve null.
	 * 
	 * @param valor
	 * @return
	 */
	public Object convertFromView(String valor){
		try{
			if (TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(this.getCodTipoAtributo()) ){				 
				return valor;
			} else if (TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals( this.getCodTipoAtributo()) ){
				return new Long(valor);
			} else if (TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals( this.getCodTipoAtributo()) ){
				return new Double(valor);
			} else if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals( this.getCodTipoAtributo()) ){
				return DateUtil.getDate(valor, DateUtil.ddSMMSYYYY_MASK);
			} else if(TipoAtributoVO.COD_TIPO_ATRIB_CATASTRAL.equals( this.getCodTipoAtributo()) ){
				// Si el Atributo es de tipo Catastral, lo formateamos completando con ceros a la izquierda en cada una de 
				// sus partes (seccion/manzana/grafico/subdivision/subparcela)
				if(!"".equals(valor)){
					Datum datum = Datum.parseAtChar(valor, '/');
					String[] catastral = new String[datum.getColNumMax()]; 
					catastral[0] = StringUtil.completarCerosIzq(datum.getCols(0), 2);
					for(int i=1;i<datum.getColNumMax();i++)
						catastral[i] = StringUtil.completarCerosIzq(datum.getCols(i), 3);
					valor = "";
					for(int i=0;i<datum.getColNumMax()-1;i++)
						valor += catastral[i]+"/";		
					valor += catastral[datum.getColNumMax()-1];
				}
				return valor;
			}else {
				log.error("covertFromView: El tipo de atributo no esta seteado");
				return null;
			}
		} catch (Exception e){
			return null;
		}
	}
	
	/**
	 * Utilizado para devolver la descripcion de un atributo valor de un dominio dado su codigo
	 * 
	 * @param strId
	 * @return String
	 */
	public String getValorByCodigoFromDominio(String strCodigo) {

		String valor = "";
		AtributoVO atributoVO = this.getAtributo();
		
		if ( atributoVO != null ) {
			
			DomAtrVO domAtrVO = atributoVO.getDomAtr();
			
			if (domAtrVO != null) {
				valor = domAtrVO.getDesValorByCodigo(strCodigo);
			}
		}		
		

		return valor;

	}
	
	/**
	 * Utilizado para devolver la descripcion de un dominio. que tiene 
	 * asociado el atributo del objeto actual.
	 * 
	 * @return String
	 */
	public String getValorFromDominioView() {

		String valor = "";
		AtributoVO atributoVO = this.getAtributo();
		
		if ( atributoVO != null ) {
			
			DomAtrVO domAtrVO = atributoVO.getDomAtr();
			
			if (domAtrVO != null) {
				valor = domAtrVO.getDesValorByCodigo(this.getValorView());
			}
		}		

		return valor;

	}
	
	/**
	 * Devuelve true cuando un atributo que posee vigencia, ademas tiene un historico de vigencias cargados.
	 * 
	 * @return
	 */
	public boolean getPoseeHistoricoVigencia(){
		if (getListAtrValVig() != null && getListAtrValVig().size() > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * Devuelve la fecha de novedad del registro vigente actualmente, si exite historico.
	 * 
	 * @return
	 */
	public Date getFechaNovedadFromVigente(){
		
		AtrValVigDefinition atrValVigDefinition = this.getAtrValDefinitionVigente();
		
		if (atrValVigDefinition != null) {
			return atrValVigDefinition.getFechaNovedad();		
		}
		
		return null;

	}
	
	/**
	 * Devuelve la fecha de desde del registro vigente actualmente, si exite historico.
	 * 
	 * @return
	 */
	public Date getFechaDesdeFromVigente(){
		
		AtrValVigDefinition atrValVigDefinition = this.getAtrValDefinitionVigente();
		
		if (atrValVigDefinition != null) {
			return atrValVigDefinition.getFechaDesde();		
		}
		
		return null;
		
	}

	/**
	 * Devuelve la fecha de Hasta del registro vigente actualmente, si exite historico.
	 * 
	 * @return
	 */
	public Date getFechaHastaFromVigente(){

		AtrValVigDefinition atrValVigDefinition = this.getAtrValDefinitionVigente();
		
		if (atrValVigDefinition != null) {
			return atrValVigDefinition.getFechaHasta();		
		}
		
		return null;

	}

	/**
	 * Devuelve el valor del registro vigente actualmente, si exite historico.
	 * 
	 * @return
	 */
	public String getValorFromVigente(){

		AtrValVigDefinition atrValVigDefinition = this.getAtrValDefinitionVigente();
		
		if (atrValVigDefinition != null) {
			return atrValVigDefinition.getValor();		
		}
		
		return null;		

	}
	
	/**
	 * Metodo utilizado en la vista para decidir si se muetra el valor, o se presenta un combo seleccionado
	 * para el caso de la edicion de atributos teniendo en cuanta la vigencia o no de los mismos.
	 * 
	 * 
	 * @return
	 */
	public boolean getShowValor(){
		if (getPoseeVigencia() && !isSubmited)
			return false;
		else 
			return true;
		
	}
	
	
	/**
	 * Realiza la validacion de requerido, tipo de dato y las validaciones
	 * correspondientes para fechaDesde y fechaHasta si posee vigencia.
	 * <p> Si no posee dominio valida el formato del dato. Si posee dominio, valida que el dato pertenezca a los 
	 * valores del dominio.</p>
	 * <p> Si posee vigencia valida el formato de Fecha Desde y Fecha Hasta. Que Fecha Desde no sea null. 
	 * Que la fecha desde ingresada no sea mayor o igual a la fecha hasta ingresada. 
	 * La fecha desde ingresada no sea menor que la fecha actual.
	 * Que la fecha desde ingresada no sea menor a la fecha hasta del registro vigente, si existiera/n alguno/s 
	 * previo/s con fecha hasta distinta de null. 
	 * Que la fecha desde ingresada no sea igual a la fecha desde del registro vigente, si existiera alguno 
	 * previo con fecha hasta igual a null. 
	 * Valida por cada registro de vigencia futura (fechaDesde mayor a la actual) que se quiera insertar con 
	 * fechaDesde anterior a la de estos registros, que la fechaHasta ingresada sea menor que la fechaDesde de estos.</p>
	 * <p>Se valida que no se vuelva a ingresar o seleccionar el mismo valor que tiene el registro vigente
	 *  actualmente.
	 * 
	 */
	public boolean validate4EditVig() {
		// No posee Dominio
		if (!getPoseeDominio()){
			if (getValorView() == null || getValorView().equals("")){		
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + getAtributo().getDesAtributo());
			}
			
			if (getValorView() != null &&  !getValorView().equals("") ){
				if(!validarTipoDato(getValorView(), getAtributo().getTipoAtributo().getCodTipoAtributo()))
					this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "&" + getAtributo().getDesAtributo());
			}
		}	
		// Posee Dominio
		if (getPoseeDominio()){
			if (getValorView() == null || getValorView().equals("") || getValorView().equals("-1") ){		
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + getAtributo().getDesAtributo());
			}
		}
		
		if (getPoseeVigencia()){
			if ( !validarTipoDato(getFechaDesdeView(), TipoAtributoVO.COD_TIPO_ATRIB_DATE))
				this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "& Fecha Desde");
			if(getFechaHastaView()!=null){
				if(!validarTipoDato(getFechaHastaView(), TipoAtributoVO.COD_TIPO_ATRIB_DATE))
					this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "& Fecha Hasta");
			}
			// Si no pasa la validacion de formatos no se puede seguir			
			if(this.hasErrorRecoverable())  	
				return false;
			
			if (getFechaDesde() == null){
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Fecha Desde");
			}
			
			if (getFechaDesde() != null){
				//La fecha desde ingresada no puede ser menor que la fecha actual.
				if(DateUtil.isDateBefore(getFechaDesde(), new Date())){
					this.addRecoverableError(BaseError.MSG_VALORMENORQUE, "& Fecha de Desde", "& Fecha Actual.");					
				}
				/* La fecha desde ingresada no puede ser menor a la fecha hasta del registro vigente,
				  si existiera/n alguno/s previo/s con fecha hasta distinta de null. */
				if (getPoseeHistoricoVigencia() && getFechaHastaFromVigente()!=null &&
						DateUtil.isDateBefore(getFechaDesde(), getFechaHastaFromVigente()) ){
					this.addRecoverableError(BaseError.MSG_VALORMENORQUE, "& Fecha de Desde", "& el Registro Vigente Actualmente.");					
				}
				/* La fecha desde ingresada no puede ser igual a la fecha desde del registro vigente,
				  si existiera alguno previo con fecha hasta igual a null. */
				if (getPoseeHistoricoVigencia() && getFechaHastaFromVigente()==null &&
						DateUtil.isDateEqual(getFechaDesde(), getFechaDesdeFromVigente()) ){
					this.addRecoverableError(BaseError.MSG_IGUALQUE, "& Fecha de Desde", "& el Registro Vigente Actualmente.");					
				}
				
				// Valida por cada registro de vigencia futura (fechaDesde mayor a la actual) que se quiera insertar 
				// con fechaDesde anterior a la de estos registros, que la fechaHasta ingresada sea menor que la 
				// fechaDesde de estos.
				if(getPoseeHistoricoVigencia()){
					for(AtrValVigDefinition item: listAtrValVig){
						if(DateUtil.isDateAfter(item.getFechaDesde(),new Date())){
							if((item.getFechaHasta()!=null && DateUtil.isDateBefore(getFechaDesde(),item.getFechaHasta())) ||
									(item.getFechaHasta()==null && DateUtil.isDateBefore(getFechaDesde(),item.getFechaDesde()))){
								if(getFechaHasta()==null || DateUtil.isDateAfter(getFechaHasta(), item.getFechaDesde())){
									this.addRecoverableError(BaseError.MSG_VALORMAYORQUE, "& Fecha de Hasta", "& un Registro de Vigencia futura.");									
								}
							}
						}
					}
				}
				
				// La fecha desde ingresada no puede ser mayor o igual que la fecha hasta ingresada.
				if (getFechaHasta()!=null && DateUtil.isDateBeforeOrEqual(getFechaHasta(), getFechaDesde())){
					this.addRecoverableError(BaseError.MSG_VALORMENORIGUALQUE, "& Fecha de Hasta", "& Fecha de Desde");					
				}
			}
			
			// Se valida que no se vuelva a ingresar o seleccionar el mismo valor que tiene el registro vigente actualmente
			// Se utiliza el ValorView porque, en el historico de vigencias se cargan valores formateados para la vista
			if (getPoseeHistoricoVigencia()){				
				boolean valida = true;
				
				// Si posee dominio 
				if (getPoseeDominio()){
					if(getValorFromDominioView().equals(getValorFromVigente())){
						valida = false;							
					}
				} else {
					// Para date o string pregunto por valorView
					if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(getCodTipoAtributo()) || 
							TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(getCodTipoAtributo())){
						if(getValorView().equals(getValorFromVigente())){
							valida = false;							
						}
					}
					// Para los numero pregunto por valorString
					if(TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(getCodTipoAtributo()) || 
							TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(getCodTipoAtributo())){						
						if(getValorString().equals(getValorFromVigente())){
							valida = false;							
						}						
					}
				}
							
				if (!valida)			
					this.addRecoverableError(BaseError.MSG_IGUALQUE, "&" + getAtributo().getDesAtributo() , "& el Registro Vigente Actualmente.");
			}
		}
		
		if (this.hasErrorRecoverable())
			return false;
	
		
		return true;
	}
	
	/**
	 * Realiza la validacion de requerido, tipo de dato y las validaciones
	 * correspondientes para fechaDesde y fechaHasta si posee vigencia.
	 * <p> Si no posee dominio valida el formato del dato. Si posee dominio, valida que el dato pertenezca a los 
	 * valores del dominio.</p>
	 * <p> Si posee vigencia valida el formato de Fecha Desde y Fecha Hasta. Que Fecha Desde no sea null. 
	 * Que la fecha desde ingresada no sea mayor o igual a la fecha hasta ingresada. 
	 * Que la fecha desde ingresada no sea menor a la fecha hasta del registro vigente, si existiera/n alguno/s 
	 * previo/s con fecha hasta distinta de null. 
	 * Que la fecha desde ingresada no sea igual a la fecha desde del registro vigente, si existiera alguno 
	 * previo con fecha hasta igual a null. 
	 * Valida por cada registro de vigencia futura (fechaDesde mayor a la actual) que se quiera insertar con 
	 * fechaDesde anterior a la de estos registros, que la fechaHasta ingresada sea menor que la fechaDesde de estos.</p>
	 * <p>Se valida que no se vuelva a ingresar o seleccionar el mismo valor que tiene el registro vigente
	 *  actualmente.
	 * 
	 */
	public boolean validate4Contribuyente() {
		// No posee Dominio
		if (!getPoseeDominio()){
			if (getValorView() == null || getValorView().equals("")){		
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + getAtributo().getDesAtributo());
			}
			
			if (getValorView() != null &&  !getValorView().equals("") ){
				if(!validarTipoDato(getValorView(), getAtributo().getTipoAtributo().getCodTipoAtributo()))
					this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "&" + getAtributo().getDesAtributo());
			}
		}	
		// Posee Dominio
		if (getPoseeDominio()){
			if (getValorView() == null || getValorView().equals("") || getValorView().equals("-1") ){		
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + getAtributo().getDesAtributo());
			}
		}
		
		if (getPoseeVigencia()){
			if ( !validarTipoDato(getFechaDesdeView(), TipoAtributoVO.COD_TIPO_ATRIB_DATE))
				this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "& Fecha Desde");
			if(getFechaHastaView()!=null){
				if(!validarTipoDato(getFechaHastaView(), TipoAtributoVO.COD_TIPO_ATRIB_DATE))
					this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "& Fecha Hasta");
			}
			// Si no pasa la validacion de formatos no se puede seguir			
			if(this.hasErrorRecoverable())  	
				return false;
			
			if (getFechaDesde() == null){
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Fecha Desde");
			}
			
			if (getFechaDesde() != null){
		
				
				// Valida por cada registro de vigencia futura (fechaDesde mayor a la actual) que se quiera insertar 
				// con fechaDesde anterior a la de estos registros, que la fechaHasta ingresada sea menor que la 
				// fechaDesde de estos.
				if(getPoseeHistoricoVigencia()){
					for(AtrValVigDefinition item: listAtrValVig){
						if(DateUtil.isDateAfter(item.getFechaDesde(),new Date())){
							if((item.getFechaHasta()!=null && DateUtil.isDateBefore(getFechaDesde(),item.getFechaHasta())) ||
									(item.getFechaHasta()==null && DateUtil.isDateBefore(getFechaDesde(),item.getFechaDesde()))){
								if(getFechaHasta()==null || DateUtil.isDateAfter(getFechaHasta(), item.getFechaDesde())){
									this.addRecoverableError(BaseError.MSG_VALORMAYORQUE, "& Fecha de Hasta", "& un Registro de Vigencia futura.");									
								}
							}
						}
					}
				}
				
				// La fecha desde ingresada no puede ser mayor o igual que la fecha hasta ingresada.
				if (getFechaHasta()!=null && DateUtil.isDateBeforeOrEqual(getFechaHasta(), getFechaDesde())){
					this.addRecoverableError(BaseError.MSG_VALORMENORIGUALQUE, "& Fecha de Hasta", "& Fecha de Desde");					
				}
			}
			
			// Se valida que no se vuelva a ingresar o seleccionar el mismo valor que tiene el registro vigente actualmente
			// Se utiliza el ValorView porque, en el historico de vigencias se cargan valores formateados para la vista
			if (getPoseeHistoricoVigencia()){				
				boolean valida = true;
				
				// Si posee dominio 
				if (getPoseeDominio()){
					if(getValorFromDominioView().equals(getValorFromVigente())){
						valida = false;							
					}
				} else {
					// Para date o string pregunto por valorView
					if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(getCodTipoAtributo()) || 
							TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(getCodTipoAtributo())){
						if(getValorView().equals(getValorFromVigente())){
							valida = false;							
						}
					}
					// Para los numero pregunto por valorString
					if(TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(getCodTipoAtributo()) || 
							TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(getCodTipoAtributo())){						
						if(getValorString().equals(getValorFromVigente())){
							valida = false;							
						}						
					}
				}
							
				if (!valida)			
					this.addRecoverableError(BaseError.MSG_IGUALQUE, "&" + getAtributo().getDesAtributo() , "& el Registro Vigente Actualmente.");
			}
		}
		
		if (this.hasErrorRecoverable())
			return false;
	
		
		return true;
	}
	
	
	/**
	 * Realiza la validacion de requerido, tipo de dato y las validaciones
	 * correspondientes para fechaNovedad y fechaDesde si posee vigencia.
	 * 
	 */
	public boolean validate4EditNovedad() {

		// Si act es Manual
		if (!this.validate(ACT_MANUAL)){
			
			// No posee Dominio
			if (!getPoseeDominio()){
				if (getEsRequerido() && (getValorView() == null || getValorView().equals(""))){		
					this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + getAtributo().getDesAtributo());
				}
				
				if (getValorView() != null &&  !getValorView().equals("") ){
					this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "&" + getAtributo().getDesAtributo());
				}
			}	
			// Posee Dominio
			if (getPoseeDominio()){
				if (getEsRequerido() && 
						(getValorView() == null || getValorView().equals("") || getValorView().equals("-1") )){		
					this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "&" + getAtributo().getDesAtributo());
				}
			}
		}
		
		if (getPoseeVigencia()){
			
			if ( !validarTipoDato(getFechaNovedadView(), TipoAtributoVO.COD_TIPO_ATRIB_DATE))
				this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "& Fecha Novedad");
				
			if(!validarTipoDato(getFechaDesdeView(), TipoAtributoVO.COD_TIPO_ATRIB_DATE))
				this.addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, "& Fecha desde");
				
			// Si no pasa la validacion de formatos no se puede seguir			
			if(this.hasErrorRecoverable())  	
				return false;
			
			if (getFechaNovedad() == null){
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Fecha Novedad");
			}
			
			if (getFechaDesde() == null){
				this.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Fecha Desde");
			}
			
			if (getFechaNovedad() != null){
				// la fecha de novedad no puede ser mayor a la fecha actual
				if (DateUtil.isDateAfter(getFechaNovedad(), new Date()) ){
					this.addRecoverableError(BaseError.MSG_VALORMAYORQUE, "& Fecha Novedad", BaseError.MSG_FECHA_ACTUAL);					
				}
				
				/* La fecha novedad ingresada no puede ser menor a la fecha novedad del registro vigente,
				  si existiera/n alguno/s previo/s */
				if (getPoseeHistoricoVigencia() &&
						DateUtil.isDateBefore(getFechaNovedad(), getFechaNovedadFromVigente()) ){
					this.addRecoverableError(BaseError.MSG_VALORMENORQUE, "& Fecha de Novedad", "& el Registro Vigente Actualmente.");					
				}				
			}
			
			if (getFechaDesde() != null){
				// la fecha desde no puede ser mayor a la fecha actual
				if (DateUtil.isDateAfter(getFechaDesde(), new Date()) ){
					this.addRecoverableError(BaseError.MSG_VALORMAYORQUE, "& Fecha Desde", BaseError.MSG_FECHA_ACTUAL);					
				}
			}
			
			// Se valida que no se vuelva a ingresar o seleccionar el mismo valor que tiene el registro vigente actualmente
			// Se utiliza el ValorView porque, en el historico de vigencias se cargas valores formateados para la vista
			if (getPoseeHistoricoVigencia()){				
				boolean valida = true;
				
				// Si posee dominio 
				if (getPoseeDominio()){
					if(getValorFromDominioView().equals(getValorFromVigente())){
						valida = false;							
					}
				} else {
					// Para date o string pregunto por valorView
					if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(getCodTipoAtributo()) || 
							TipoAtributoVO.COD_TIPO_ATRIB_STRING.equals(getCodTipoAtributo())){
						if(getValorView().equals(getValorFromVigente())){
							valida = false;							
						}
					}
					// Para los numero pregunto por valorString
					if(TipoAtributoVO.COD_TIPO_ATRIB_LONG.equals(getCodTipoAtributo()) || 
							TipoAtributoVO.COD_TIPO_ATRIB_DOUBLE.equals(getCodTipoAtributo())){						
						if(getValorString().equals(getValorFromVigente())){
							valida = false;							
						}						
					}
				}
							
				if (!valida)			
					this.addRecoverableError(BaseError.MSG_IGUALQUE, "&" + getAtributo().getDesAtributo() , "& el Registro Vigente Actualmente.");
			}
		}
		
		if (this.hasErrorRecoverable())
			return false;
		
		return true;
	}
	
	/**
	 * Helper utilizado en la carga de atributo, para formatear los valores de las vigencias
	 * 
	 * @return string formateado
	 */
	public String getValor4VigView(String valor){
		String valorRet = "";
		// Si posee Dominio en el valor de cada registro de vigencia coloco la Descripcion del valor  				
		if (getPoseeDominio()){
			// Busco la descripcion del valor dentro del dominio
			valorRet = getValorByCodigoFromDominio( valor);
		} else {
			// Si es de tipo date lo formateo, sino, se muestra tal cual esta en la DB
			if (TipoAtributoVO.COD_TIPO_ATRIB_DATE.equals(getCodTipoAtributo())){
				valorRet = DateUtil.formatDate(DateUtil.getDate(valor, DateUtil.YYYYMMDD_MASK), DateUtil.ddSMMSYYYY_MASK);
			} else {
				valorRet = valor;
			}
		}
		
		//log.debug("          		### --> valor: " + valor + "  ->  valorRet: " + valorRet); 
		
		return valorRet;
	}

	/** Recupera el Atr val definition vigente. 
	 * 
	 * @param idAtributo
	 */
	public AtrValVigDefinition getAtrValDefinitionVigente() {

		for (AtrValVigDefinition atrValDefinition:this.getListAtrValVig() ) {
			if(atrValDefinition.getVigencia().equals(Vigencia.VIGENTE)) {
				return atrValDefinition;			
			}

		}
		
		return null;
		
	}
	
	/** Si la fecha desde es menor o igual a la actual y la fecha hasta es nula
	 *  el atrVal esta vigente.
	 *  Si la fecha actual esta entre la fecha Desde y Hasta el atrVal
	 *  esta vigente.
	 *  Sino esta no vigente.
	 *  
	 */

    public Vigencia getVigencia() {

    	return Vigencia.getById(this.getVigenciaForDate(new Date()));

    }

	/** Si la fecha desde es menor o igual a la fecha a validar 
	 *  y la fecha hasta es nula el atrVal esta vigente.
	 *  Si la fecha a validar esta entre la fecha Desde y Hasta el atrVal
	 *  esta vigente.
	 *  Sino esta no vigente.
	 *  
	 */
    public Integer getVigenciaForDate(Date dateToValidate) {

    	Date fechaHastaValue = this.getFechaHasta();
    	Date fechaDesdeValue = this.getFechaDesde();

    	Integer vigencia = Vigencia.NOVIGENTE.getId();
    	
    	if(DateUtil.isDateInRange(dateToValidate, fechaDesdeValue, fechaHastaValue)){
    		vigencia = Vigencia.VIGENTE.getId();
    	}
    	
    	return vigencia;
    }
	
    
    public void populateAtrVal4Busqueda(Map<String,String> parametrosRequest) {

		try {
			String funcName = "populateAtrVal4Busqueda";
			
			log.debug(funcName + ": enter -------------------------------------------------");
			
			String codAtributo = this.getAtributo().getCodAtributo();
			// Banderas utilizadas para los checkbox, 
			boolean limpiarListValor = true;			
			boolean checkboxSubmited = false;
			
			
			for (Iterator iter = parametrosRequest.keySet().iterator(); iter.hasNext();) {
				String strElemetName = (String) iter.next();
				String strValor = (String) parametrosRequest.get(strElemetName);
			   
			   String[] arrName = strElemetName.split("\\.");
			   String strName = arrName[0]; 
			   
			   if (strName.toUpperCase().equals(codAtributo.toUpperCase()) ){
				   
				   log.debug(funcName + " strElemetName: " + strElemetName + "    clave: " + strName + "   valor: " + strValor);
				   log.debug(funcName + " poseeDominio: " + this.getPoseeDominio() 
						   			  + "   admBusPorRan: " + this.getAdmBusPorRan()
						   			  + "   tipoDato: " + this.getAtributo().getTipoAtributo().getCodTipoAtributo());
				   
				   // blanqueo la lista de valores
				   if (limpiarListValor){
					   this.resetListValor();
					   limpiarListValor = false;
				   }
				   
				   // Posee dominio
				   if (this.getPoseeDominio()){
					   // Admite Bus por rango: clave.id1 -> on, clave.id2 -> on, clave.idn -> on (CHECKBOX)
					   if (this.getAdmBusPorRan()){
						   this.addValorView(arrName[1]);
						   
						   checkboxSubmited = true;
					   // NO Admite Bus por rango: clave -> id (COMBO)
					   } else {						   
						   this.addValorView(strValor);
						   return;
					   }					   
				   }
				   
				   // No posee dominio
				   if (!this.getPoseeDominio()){
					   // Admite Bus por rango: clave.desde -> valor , clave.hasta -> valor (TEXTBOX.DESDE Y TEXTBOX.HASTA)
					   if (this.getAdmBusPorRan()){ 						  
						   String valDesde = parametrosRequest.get(strName + ".desde");
						   String valHasta = parametrosRequest.get(strName + ".hasta");
						   this.addValorView(valDesde);
						   this.addValorView(valHasta);
						   						   
						   return;
					   // NO admite Bus por rango: clave -> valor (TEXTBOX)
					   } else {						   
						   this.addValorView(strValor);
						   
						   return;
					   }
				   }
			   }
			}  
			
			// Si el atrVal fue dibujado con un checkbox y ninguno fue submitido
			if (this.getPoseeDominio() && 
					this.getAdmBusPorRan() &&
					   checkboxSubmited == false){
				
				this.resetListValor();				
			}
			
			log.debug(funcName + ": exit ---------------------------------------------------------");
			
		} catch (Exception e){
			e.printStackTrace();			
		}
		
	}
    
    public boolean validateRequerido() {
    	return !(getEsRequerido() && StringUtil.isNullOrEmpty(getValorView())); 
	}

    /* retorna el valor i-esimo de un atributo multi valor */
    public String getMultiValor(int i) {
		/* 2009-11-04 fedel: fix bug 922, la idea es solo usar estas listas al momento de 
		 * guardar los valores en ObjImp.updateObjImpAtrDefinition() */
		if (listValor != null){
			return this.getValorString(i);
		}
		return "";
    } 

    /* retorna la fecha desde i-esimo de un atributo multi valor */
    public Date getMultiFechaDesde(int i) {
		/* 2009-11-04 fedel: fix bug 922, la idea es solo usar estas listas al momento de 
		 * guardar los valores en ObjImp.updateObjImpAtrDefinition() */
    	return this.listMultiFechaDesde.get(i);
    } 

    /* retorna la fecha Hasta i-esimo de un atributo multi valor */
    public Date getMultiFechaHasta(int i) {
		/* 2009-11-04 fedel: fix bug 922, la idea es solo usar estas listas al momento de 
		 * guardar los valores en ObjImp.updateObjImpAtrDefinition() */
    	return this.listMultiFechaHasta.get(i);
    } 

    /* retorna la fecha Novedad i-esimo de un atributo multi valor */
    public Date getMultiFechaNovedad(int i) {
		/* 2009-11-04 fedel: fix bug 922, la idea es solo usar estas listas al momento de 
		 * guardar los valores en ObjImp.updateObjImpAtrDefinition() */
    	return this.listMultiFechaNovedad.get(i);
    } 

    /* retorna la cantidad de valores almanceandos en este atr multivalor. */
    public int getMultiSize() {
		/* 2009-11-04 fedel: fix bug 922, la idea es solo usar estas listas al momento de 
		 * guardar los valores en ObjImp.updateObjImpAtrDefinition() */
    	return this.listValor.size();
    } 

	/**
	 * Retorna un indice de multi valores teniendo encuenta vigencias (sin preparar para la vista: no busca el dominio).
	 * Luego el valor y los datos del atributo se pueden acceder via cada indice de la lista.
	 * @param fechaAnalisis
	 * @return
	 */
	public List<Integer> getMultiValorIndex(Date fechaAnalisis) {		
		List<Integer> ret = new ArrayList<Integer>();

		log.debug("ListValor: " + listValor);
		log.debug("ListValor size: " + listValor != null ? listValor.size() : "NULL");
		
		if (listValor != null){
			for (int c = 0; c < listValor.size(); c++){
				Date fechaHasta = this.listMultiFechaHasta.get(c);
				log.debug(String.format("ListValor; %s, %s ", c, fechaHasta));

				if (fechaHasta == null ||fechaAnalisis.compareTo(fechaHasta) <= 0 ) {				
					ret.add(c);
				}
			}
		}
		
		return ret;
	}	

}
