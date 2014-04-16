//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipObjImp;
import ar.gov.rosario.siat.def.buss.bean.TipObjImpAtr;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Vigencia;

/**
 * Representa cada uno de las instancias del cada Tipo de objeto imponibles
 * @author tecso
 *
 */
@Entity
@Table(name = "pad_objImp")
public class ObjImp extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Transient
	Logger log = Logger.getLogger(ObjImp.class);
	
	@Column(name = "clave")
	private String clave;
	
	@Column(name = "clavefuncional")
	private String claveFuncional;
	
	@Column(name="fechaAlta")
	private Date fechaAlta;
	
	@Column(name="fechaBaja")	
	private Date fechaBaja;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idTipObjImp") 
	private TipObjImp tipObjImp;

	@OneToMany(mappedBy="objImp", fetch=FetchType.LAZY)
	@JoinColumn(name="idObjImp") 
	private List<ObjImpAtrVal> listObjImpAtrVal = new ArrayList<ObjImpAtrVal>();
	
	@Transient
	private List<ObjImpAtrVal> listObjImpAtrValRaw ;
	
	@Transient
	private Cuenta cuentaPrincipalRaw;
	
	/*
	 idAreOriUltMdf
	 idCorridaInterface
	*/
	
	// Constructorses
	public ObjImp() {
		super();
	}
	
	public ObjImp(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	
	/**
	 * Hace toVO(0, false).<br>
	 * Setea en nul las listas
	 * @throws Exception 
	 * 
	 */
	public ObjImpVO toVOForPDF() throws Exception{
		ObjImpVO objImpVO = (ObjImpVO) this.toVO(0, false);
		objImpVO.setListObjImpAtrVal(null);
		return objImpVO;
	}
	
	public static ObjImp getById(Long id) {
		return (ObjImp) PadDAOFactory.getObjImpDAO().getById(id);
	}
	
	public static ObjImp getByIdNull(Long id) {
		return (ObjImp) PadDAOFactory.getObjImpDAO().getByIdNull(id);
	}

	/**
	 * Obtiene el Objeto Imponible a partir del Tipo de Objeto Imponible y el Numero de Cuenta .
	 * @return ObjImp
	 */
	public static ObjImp getByTipObjImpYNroCta(Long idTipObjImp, String nroCta) throws Exception{
		return (ObjImp) PadDAOFactory.getObjImpDAO().getByTipObjImpYNroCta(idTipObjImp,nroCta);
	}

	/**
	 * Obtiene el Objeto Imponible a partir del Tipo de Objeto Imponible y la clave funcional.
	 * @return ObjImp
	 */
	public static ObjImp getByTipObjImpYClaveFuncional(Long idTipObjImp, String claveFunc) throws Exception{
		return (ObjImp) PadDAOFactory.getObjImpDAO().getByTipObjImpYClaveFuncional(idTipObjImp, claveFunc);
	}
	
	public static List<ObjImp> getListObjImpByTipObj(Long idTipObjImp) throws Exception{
		return  PadDAOFactory.getObjImpDAO().getListObjImpByTipObj(idTipObjImp);
	}

	@SuppressWarnings(value = "unchecked")
	public static List<ObjImp> getList() {
		return (List<ObjImp>) PadDAOFactory.getObjImpDAO().getList();
	}
	
	@SuppressWarnings(value = "unchecked")
	public static List<ObjImp> getListActivos() {			
		return (List<ObjImp>) PadDAOFactory.getObjImpDAO().getListActiva();
	}
	
	/**
	* Lista de Objetos Imponibles de tipo Parcela con Catastral 
	* que contenga a la pasada y ordenada por catastral.
	* 
	* @param catastral
	* @return listObjImp
	*/
	public static List<ObjImp> getListParcelaByCatastral(String catastral) {			
		return (List<ObjImp>) PadDAOFactory.getObjImpDAO().getListParcelaByCatastral(catastral);
	}
	
	// Getters y Setters
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public TipObjImp getTipObjImp() {
		return tipObjImp;
	}

	public void setTipObjImp(TipObjImp tipObjImp) {
		this.tipObjImp = tipObjImp;
	}

	public List<ObjImpAtrVal> getListObjImpAtrVal() {
		return listObjImpAtrVal;
	}

	public void setListObjImpAtrVal(List<ObjImpAtrVal> listObjImpAtrVal) {
		this.listObjImpAtrVal = listObjImpAtrVal;
	}

	public String getClaveFuncional() {
		return claveFuncional;
	}

	public void setClaveFuncional(String claveFuncional) {
		this.claveFuncional = claveFuncional;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	public List<ObjImpAtrVal> getListObjImpAtrValRaw() {
		return listObjImpAtrValRaw;
	}

	public void setListObjImpAtrValRaw(List<ObjImpAtrVal> listObjImpAtrValRaw) {
		this.listObjImpAtrValRaw = listObjImpAtrValRaw;
	}
	
	public Cuenta getCuentaPrincipalRaw() {
		return cuentaPrincipalRaw;
	}

	public void setCuentaPrincipalRaw(Cuenta cuentaPrincipalRaw) {
		this.cuentaPrincipalRaw = cuentaPrincipalRaw;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		if (!validate()) {
			return false;
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		if (!validate()) {
			return false;
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		if (GenericDAO.hasReference(this, Cuenta.class, "objImp")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					PadError.OBJIMP_LABEL , PadError.CUENTA_LABEL);	
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	
	private boolean validate() throws Exception {
		
		//	Validaciones de Requeridos
		// Clave
		if (StringUtil.isNullOrEmpty(getClave())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.OBJIMP_CLAVE);
		}
 
		// Clave funcional
		if (StringUtil.isNullOrEmpty(getClaveFuncional())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.OBJIMP_CLAVEFUNCIONAL);
		}
		
		// Tipo Objeto Imponible
		if (getTipObjImp() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMP_LABEL);
		}
		
		// Fecha Alta
		if (getFechaAlta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.OBJIMP_FECHAALTA);
		}
		
		if (hasError()) {
			return false;
		}
		
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("clave");
		uniqueMap.addEntity("tipObjImp");
		
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, PadError.OBJIMP_CLAVE);			
		}

		if (hasError()) {
			return false;
		}

		// Valida que la Fecha Alta no sea menor que la fecha Alta del TipObjImp
		/*if(!DateUtil.isDateBeforeOrEqual(this.getTipObjImp().getFechaAlta(), this.fechaAlta)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, PadError.OBJIMP_FECHAALTA, DefError.TIPOBJIMP_FECHAALTA_REF);
		}*/
		
		return !hasError();
		
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ObjImp. Previamente valida la activacion. 
	 *
	 */
	public void activar() throws Exception{
		if(!this.validateActivar()) {
			return;
		}

		this.setFechaBaja(null);
		//this.setEstado(Estado.ACTIVO.getId());
		PadDAOFactory.getObjImpDAO().update(this);
	}

	/**
	 * Desactiva el ObjImp. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(Date fechaBajaValue) throws Exception{
		
		if(!this.validateDesactivar(fechaBajaValue)) {
			return;
		}
		
		this.setFechaBaja(fechaBajaValue);
		//this.setEstado(Estado.INACTIVO.getId());
		PadDAOFactory.getObjImpDAO().update(this);

		// Analisis del impacto sobre cuentas en funcion de la configuracion del recurso
		// Busco el Recurso Principal asociado
		Recurso recPri = this.getTipObjImp().getRecursoPrincipal();
		// Si no encuentro un Recurso Principal termino aca.
		if(recPri == null){
			return;
		}
		// Con el principal asociado, verifico si "realiza baja de cuenta por baja de objeto Imponible" 
		if(recPri.getBajaCtaPorIface()==1){
			// Se setea la baja de cuenta.
			Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recPri.getId(), this.getClave());
			if(cuenta != null){
				cuenta.desactivar(fechaBajaValue);
			}
		}
		// Analisis de las acciones sobre los recursos secundarios.
		List<Recurso> listRecursoSecundario = this.getTipObjImp().getListRecursoSecundario();
		if(!ListUtil.isNullOrEmpty(listRecursoSecundario)){
			for(Recurso recurso: listRecursoSecundario){
				// Verifica la configuracion "baja de cuenta por baja de cuenta del Recurso principal asociado"
				if(recurso.getBajaCtaPorPri()==1){
					// Se setea la baja de cuenta.
					Cuenta cuenta = Cuenta.getByIdRecursoYNumeroCuenta(recurso.getId(), this.getClave());
					if(cuenta != null){
						cuenta.desactivar(fechaBajaValue);
					}					
				}
			}
		}

	}
	
	/**
	 * Valida la activacion del ObjImp
	 * @return boolean
	 */
	private boolean validateActivar() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ObjImp
	 * @return boolean
	 */
	private boolean validateDesactivar(Date fechaBajaValue) {
		//limpiamos la lista de errores
		clearError();
		
		// Fecha Baja
		if (fechaBajaValue == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.OBJIMP_FECHABAJA);
		}
		
		// Se sacan las siguientes validaciones por nuevos requerimientos. 
		/*// que fecha baja no sea mayor a la actual
		if (fechaBajaValue != null && DateUtil.isDateBefore(new Date(), fechaBajaValue)){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
				PadError.OBJIMP_FECHABAJA, BaseError.MSG_FECHA_ACTUAL);
		}
		
		// que fecha baja no sea menor a la fecha alta
		if (fechaBajaValue != null && DateUtil.isDateBefore(fechaBajaValue, this.getFechaAlta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, 
				PadError.OBJIMP_FECHABAJA, PadError.OBJIMP_FECHAALTA);
		}*/
		
		//Validaciones 
		return !hasError();
	}
	

	/**
	 * Agrega un valor de atributo al objeto imponible
	 * @return
	 */
	public ObjImpAtrVal createObjImpAtrVal(ObjImpAtrVal objImpAtrVal) throws Exception {
		
		if (!objImpAtrVal.validateCreate()) {
			return objImpAtrVal;
		}

		PadDAOFactory.getObjImpAtrValDAO().update(objImpAtrVal);
		
		return objImpAtrVal;
	}
	
	/**
	 * Actualiza el valor de un registro ObjImpAtrVal.
	 * Este metodo utiliza el DAO directamente para actualizar el registro.
	 * @param objImpAtrVal valores a actualizar
	 * @return el objeto actualizado
	 * @throws Exception 
	 */
	public ObjImpAtrVal updateObjImpAtrVal(ObjImpAtrVal objImpAtrVal) throws Exception {
		if (!objImpAtrVal.validateUpdate()) {
			return objImpAtrVal;
		}

		PadDAOFactory.getObjImpAtrValDAO().update(objImpAtrVal);
		
		return objImpAtrVal;
	} 


	/**
	 * Actualiza el valor y/o las vigencias de un atributo valorizado del 
	 * Objeto imponible. El metodo actualiza el valor y las vigencias en caso
	 * de ser un atributo que posee vigencias.
	 * @param objImpAtrVal valores a actualizar
	 * @return el objeto actualizado
	 * @throws Exception 
	 */
	public TipObjImpDefinition updateObjImpAtrDefinition(TipObjImpDefinition definition) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		// Por cada TipObjImpAtrDefinition
		// si poseeVigencia:
		//      se recupera el objImpAtrVal vigente y se setea la fechaNovedad como fechaHasta 
		//      se inserta el nuevo registro de objImpAtrVal con fechaHasta nula.  
		// si no posee vigencia:
		// 		obtener objImpAtrVal, modificar valor, llamar updateObjImpAtrVal
		
		// Variable para identificar si se modifico un rubro de Comercio. Necesaria para actualizar el valor del atributo de asentamiento 
		// que determina la distribucion de partidas que le corresponde.
		boolean rubroDreiActualizado = false;
		 
		for (TipObjImpAtrDefinition item: definition.getListTipObjImpAtrDefinition()){
			
			ObjImpAtrVal objImpAtrVal = null;            
            
            Long idTipObjImpAtr = item.getTipObjImpAtr().getId(); 
            
            log.debug(funcName + " idTipObjImpAtr: " + idTipObjImpAtr);
            log.debug(funcName + " esMultivalor: " + item.getEsMultivalor());  
            log.debug(funcName + " poseeVigencia: " + item.getPoseeVigencia());
            
            // Si no es Multivalor:
            if(!item.getEsMultivalor()){
                // Recupero el el atributo vigente, o el unico que exista segun el caso.
                ObjImpAtrVal objImpAtrValVigente = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(idTipObjImpAtr, this.getId());
                
                if (objImpAtrValVigente == null);
                	//throw new DemodaServiceException();
                
                // Si el valor no es vacio. updteamos o inserteamos segun corresponda
                if (!StringUtil.isNullOrEmpty(item.getValorString())){
                	
                	log.debug(funcName + " valor: " + item.getValorString());
                	
	                // Posee vigencia
	                if (item.getPoseeVigencia()){
	                 	 // Se agrego el "OR" y la expresion objImpAtrValVigente!=null, para que funcione en la modificacion por interface.
	                	if (objImpAtrValVigente!=null || item.getPoseeHistoricoVigencia()){  
	                		// seteo de la fecha novedad a la fecha hasta del registro vigente actual 
	                		objImpAtrValVigente.setFechaHasta(item.getFechaNovedad());
	                		
	                		objImpAtrValVigente = this.updateObjImpAtrVal(objImpAtrValVigente);
	                	}
	
	                	// Insertar un registro nuevo con fecha hasta nula             	
	                	objImpAtrVal = new ObjImpAtrVal();
	                	objImpAtrVal.setObjImp(this);
	    				objImpAtrVal.setTipObjImpAtr(TipObjImpAtr.getById(item.getTipObjImpAtr().getId()));
	    				objImpAtrVal.setStrValor(item.getValorString());
	    				objImpAtrVal.setFechaDesde(item.getFechaDesde());
	    				objImpAtrVal.setFechaHasta(null);
	    				objImpAtrVal.setFechaNovedad(item.getFechaNovedad());
	    					
	    				objImpAtrVal = this.createObjImpAtrVal(objImpAtrVal);
	                
	    			// No posee vigencia	
	    		 	} else {
	    		 		if(objImpAtrValVigente!=null){
	    		 			// Solo se actualiza el valor del registro.
	    		 			objImpAtrVal = objImpAtrValVigente;
	    		 			objImpAtrVal.setStrValor(item.getValorString());
	    		 			objImpAtrVal = this.updateObjImpAtrVal(objImpAtrVal);		 			
	    		 		}else{
	    		 			// Insertar un registro nuevo con fecha hasta nula             	
	    	            	objImpAtrVal = new ObjImpAtrVal();
	    	            	objImpAtrVal.setObjImp(this);
	    					objImpAtrVal.setTipObjImpAtr(TipObjImpAtr.getById(item.getTipObjImpAtr().getId()));
	    					objImpAtrVal.setStrValor(item.getValorString());
	    					objImpAtrVal.setFechaNovedad(item.getFechaNovedad());
	    					
	    					objImpAtrVal = this.createObjImpAtrVal(objImpAtrVal);	            		
	    		 		}
	    		 	}
                
                }
                
                
            }else{
            // Si es Multivalor:	
                /*
                Si no es dejamos el codigo como esta. Si es multivalor armamos una lista con
                los valores entre las columnas indicadas. Recorremos la lista y buscamos el  objImpAtrValVigente para el
                valor en cuestion. Y damos de baja y modificamos o creamos el que corresponda.
               */

            	// Si no es vacia la lista de multi valores
            	if (!ListUtil.isNullOrEmpty(item.getMultiValor())){
            	
	            	for(int i=0; i < item.getMultiSize(); i++) {
	            		String strValor = item.getMultiValor(i);
	            		Date multiFechaDesde = item.getMultiFechaDesde(i);
	            		Date multiFechaHasta = item.getMultiFechaHasta(i);
	            		Date multiFechaNovedad = item.getMultiFechaNovedad(i);

	            		log.debug(funcName + " multiValor -> valor: " + strValor);
	            		log.debug("item fecha desde: "+multiFechaDesde+" ,fechaHasta: "+multiFechaHasta);
	
	            		ObjImpAtrVal objImpAtrValMulti=ObjImpAtrVal.getVigenteByIdObjImpAtrValAndValue(idTipObjImpAtr, this.getId(), strValor);
	            		if (objImpAtrValMulti==null)
	            			objImpAtrValMulti = new ObjImpAtrVal();
						objImpAtrValMulti.setObjImp(this);
						objImpAtrValMulti.setTipObjImpAtr(TipObjImpAtr.getById(item.getTipObjImpAtr().getId()));
						objImpAtrValMulti.setStrValor(strValor);
						if(item.getPoseeVigencia()){
							objImpAtrValMulti.setFechaNovedad(multiFechaNovedad);
							if(multiFechaDesde!=null)
								objImpAtrValMulti.setFechaDesde(multiFechaDesde);

							if (multiFechaHasta!=null)
								objImpAtrValMulti.setFechaHasta(multiFechaHasta);
						} else {
							if (multiFechaNovedad!=null)
								objImpAtrValMulti.setFechaNovedad(multiFechaNovedad);
							else
								objImpAtrValMulti.setFechaNovedad(this.getFechaAlta());
						}
						
						this.createObjImpAtrVal(objImpAtrValMulti);	
						
						if(!rubroDreiActualizado && TipObjImpAtr.ID_RUBROS_TIPO_COMERCIO.longValue() == objImpAtrValMulti.getTipObjImpAtr().getId().longValue())
							rubroDreiActualizado = true;
					}
            	}
            }
		}
		
		// Si la bandera 'rubroDreiActualizado' es true, significa que se modifico el atributo rubro de comercio.
		if(rubroDreiActualizado){
			// Si se el recurso principal es Drei, se verifica
			// Calcular valor de atributo de asentamiento para Recurso DRei (Determina la distribucion de partidas para DRei y depende de los rubros habilitados)
			Recurso recPri = this.getTipObjImp().getRecursoPrincipal();
			if(recPri != null && recPri.getAtributoAse() != null && Recurso.COD_RECURSO_DReI.equals(recPri.getCodRecurso())){
				TipObjImpAtr tipObjImpAtrAse = TipObjImpAtr.getByIdAtributo(recPri.getAtributoAse().getId());
				TipObjImpDefinition toidRubros = this.getDefinitionValue(TipObjImpAtr.ID_RUBROS_TIPO_COMERCIO);
				TipObjImpAtrDefinition toidAtrRubros = null;
				if(!ListUtil.isNullOrEmpty(toidRubros.getListTipObjImpAtrDefinition()) && toidRubros.getListTipObjImpAtrDefinition().size() == 1)
					toidAtrRubros = toidRubros.getListTipObjImpAtrDefinition().get(0);
				// El valor por defecto es cero. (Distribucion general)
				String atrAseVal = "0";
				if(toidAtrRubros != null){
					List<String> listRubros = toidAtrRubros.getMultiValor(new Date());
					// Si el comercio se encuentra habilitado como "Bingo" (rubro: rrrr), le corresponde valor 1
					String rubroBingo = SiatParam.getString(SiatParam.RUBRO_BINGO_DREI);
					if(!StringUtil.isNullOrEmpty(rubroBingo)){
						for(String rubro: listRubros){
							if(rubroBingo.equals(rubro)){
								atrAseVal = "1";
								break;
							}
						}					
					}
					// Si el comercio no se encuentra habilitado como "Bingo" pero si en alguno de los rubros: rrrr1,rrrr2,rrrr3,rrrr4. Le corresponde valor 2
					String listRubrosAseStr = SiatParam.getString(SiatParam.LISTA_RUBROS_DREI);
					if(!"1".equals(atrAseVal) && !StringUtil.isNullOrEmpty(listRubrosAseStr)){
						for(String rubro: listRubros){
							rubro = "|" + rubro + "|";
							if (listRubrosAseStr.indexOf(rubro) >= 0) {
								atrAseVal = "2";
								break;								
							}
						}	
					}
					
					// Guardar el valor de atributo de asentamiento para Drei
					ObjImpAtrVal objImpAtrVal = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(tipObjImpAtrAse.getId(), this.getId());
					if(objImpAtrVal == null)
						objImpAtrVal = new ObjImpAtrVal();
					objImpAtrVal.setObjImp(this);
					objImpAtrVal.setTipObjImpAtr(tipObjImpAtrAse);
					objImpAtrVal.setStrValor(atrAseVal);
					objImpAtrVal.setFechaDesde(this.getFechaAlta());
					objImpAtrVal.setFechaHasta(null);
					objImpAtrVal.setFechaNovedad(this.getFechaAlta());
					
					this.createObjImpAtrVal(objImpAtrVal);	
				}
			}
		}
		
		return definition;
	}

	
	
	/**
	 *  Permite la correccion de la valorizadion de atributos de un objeto imponible.
	 *  
	 *  Se diferencia de la "actualizacion" en no genera nuevo registro para el caso de las vigencias, sino que modifica el valor vigente.
	 * 
	 * @param definition
	 * @return
	 * @throws Exception
	 */
	public TipObjImpDefinition correctObjImpAtrDefinition(TipObjImpDefinition definition) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		for (TipObjImpAtrDefinition item: definition.getListTipObjImpAtrDefinition()){
			
			ObjImpAtrVal objImpAtrVal = null;            
            
            Long idTipObjImpAtr = item.getTipObjImpAtr().getId(); 
            
            log.debug(funcName + " idTipObjImpAtr: " + idTipObjImpAtr);
            log.debug(funcName + " esMultivalor: " + item.getEsMultivalor());  
            log.debug(funcName + " poseeVigencia: " + item.getPoseeVigencia());
            
            // Si no es Multivalor:
            if(!item.getEsMultivalor()){
                
                // Si el valor no es vacio. updteamos o inserteamos segun corresponda
                if (!StringUtil.isNullOrEmpty(item.getValorString())){

                	// Recupero el el atributo vigente, o el unico que exista segun el caso.
                	ObjImpAtrVal objImpAtrValVigente = ObjImpAtrVal.getVigenteByIdObjImpAtrVal(idTipObjImpAtr, this.getId());
                	
                	log.debug(funcName + " valor: " + item.getValorString());
                	
	                // Posee vigencia
	                if (item.getPoseeVigencia()){
	                 	 // Se agrego el "OR" y la expresion objImpAtrValVigente!=null, para que funcione en la modificacion por interface.
	                	if (objImpAtrValVigente!=null || item.getPoseeHistoricoVigencia()){  
	                		// seteo de la fecha novedad a la fecha hasta del registro vigente actual 
	                		objImpAtrValVigente.setStrValor(item.getValorString());
	                		objImpAtrValVigente = this.updateObjImpAtrVal(objImpAtrValVigente);
	                	
	                	} else {
		                	// Insertar un registro nuevo con fecha hasta nula             	
		                	objImpAtrVal = new ObjImpAtrVal();
		                	objImpAtrVal.setObjImp(this);
		    				objImpAtrVal.setTipObjImpAtr(TipObjImpAtr.getById(item.getTipObjImpAtr().getId()));
		    				objImpAtrVal.setStrValor(item.getValorString());
		    				objImpAtrVal.setFechaDesde(item.getFechaDesde());
		    				objImpAtrVal.setFechaHasta(null);
		    				objImpAtrVal.setFechaNovedad(item.getFechaNovedad());
		    					
		    				objImpAtrVal = this.createObjImpAtrVal(objImpAtrVal);
	                	}
	    			// No posee vigencia	
	    		 	} else {
	    		 		if(objImpAtrValVigente!=null){
	    		 			// Solo se actualiza el valor del registro.
	    		 			objImpAtrValVigente.setStrValor(item.getValorString());
	    		 			objImpAtrValVigente = this.updateObjImpAtrVal(objImpAtrValVigente);		 			
	    		 		}else{
	    		 			// Insertar un registro nuevo con fechas hasta nulas             	
	    	            	objImpAtrVal = new ObjImpAtrVal();
	    	            	objImpAtrVal.setObjImp(this);
	    					objImpAtrVal.setTipObjImpAtr(TipObjImpAtr.getById(item.getTipObjImpAtr().getId()));
	    					objImpAtrVal.setStrValor(item.getValorString());
	    					objImpAtrVal.setFechaNovedad(item.getFechaNovedad());
	    					objImpAtrVal = this.createObjImpAtrVal(objImpAtrVal);	            		
	    		 		}
	    		 	}
                }
                
            }else{
            // Si es Multivalor:	

            	// Si no es vacia la lista de multi valores
            	if (!ListUtil.isNullOrEmpty(item.getMultiValor())){
            	
	            	for(String strValor: item.getMultiValor()){
	            		
	            		log.debug(funcName + " multiValor -> valor: " + strValor);
	
	            		ObjImpAtrVal objImpAtrValMulti = new ObjImpAtrVal();
						objImpAtrValMulti.setObjImp(this);
						objImpAtrValMulti.setTipObjImpAtr(TipObjImpAtr.getById(item.getTipObjImpAtr().getId()));
						objImpAtrValMulti.setStrValor(strValor);
						if(item.getPoseeVigencia()){
							if((item.getFechaDesde()!=null)&&(item.getFechaNovedad()!=null)){
								objImpAtrValMulti.setFechaDesde(item.getFechaDesde());
								objImpAtrValMulti.setFechaHasta(item.getFechaHasta());
								objImpAtrValMulti.setFechaNovedad(item.getFechaNovedad());
							}else{
								objImpAtrValMulti.setFechaDesde(this.getFechaAlta());
								objImpAtrValMulti.setFechaNovedad(this.getFechaAlta());
							}
						} else {
							if (item.getFechaNovedad()!=null)
								objImpAtrValMulti.setFechaNovedad(item.getFechaNovedad());
							else
								objImpAtrValMulti.setFechaNovedad(this.getFechaAlta());
						}
						
						this.createObjImpAtrVal(objImpAtrValMulti);	
					}
            	}
            }
		}
		
		return definition;
	}
	
	
	/**
	 * Obtiene la definicion con sus valores del TipObjImp para este objeto imponible.
	 *
	 * Para obtener las vigencias, es necesario indicar una fecha para el an'alisis.
	 *  . si quiero conocer la vigencias de un atributo a la fecha actual, tengo que indicar la fecha actual
	 *  . si quiero conocer la vigencias de un atributo a la fecha en que emiti'o deuda, debe solicitarse a la fecha de emisi'on. 
	 *  
	 * En resumen, el sistema descarta las novedades posteriores a la fecha de analisis.
	 * 
	 * <p>Este metodo sirve para obtener los atributos y sus valores de un objeto imponible.
	 * se considera que por defecto se desea conocer las vigencias a la fecha del d'ia.
	 * @return el ObjImpAtrDefinition cargado con los atributos y sus valores.
	 * @throws Exception 
	 */
	public TipObjImpDefinition getDefinitionValue() throws Exception {
		Date fechaAnalisis = new Date();
		return this.getDefinitionValue(fechaAnalisis);
	}
	

	/** Carga un definition con los valores del objeto imponible 
	 */
	public void loadDefinition(TipObjImpDefinition tipObjImpDefinition, Date fechaAnalisis) throws Exception {
		// limpiamos los valores
		tipObjImpDefinition.reset();
		
		log.debug("          ###################### --> GetDefinition 2: ObjImpAtrVal.getListByIdObjImp()");
		List<ObjImpAtrVal> listObjImpAtrVal =  ObjImpAtrVal.getListByIdObjImp(this.getId());
		
		log.debug("          ###################### --> GetDefinition 3: Valorizacion de la definicion y vigencias()");

		// aqui deberia existir una lista de los ObjImpAtrVal ordenada por TipObjImpAtr + FechaDesde
		// ademas, de dicha lista se sacaron los registros que no correspondian por tener fechaNovedad posterior a la fecha de analisis
		
		// a continuacion se realiza un corte de control con dos objetivos:
		//  . setear el valor vigente
		//  . setear toda la lista de vigencias (en caso de poseer vigencias)

		// 
		TipObjImpAtrDefinition tipObjImpAtrDef = null;
		long idTipObjImpAtr = -1;
		String valor = "";
		String valorVigencia = "";
		// 
		for (ObjImpAtrVal objImpAtrVal: listObjImpAtrVal ){

			
			log.debug(" >> Comienza la valorizacion ##### idTipObjImpAtr: " + objImpAtrVal.getIdTipObjImpAtr());
			log.debug(" >> Comienza la valorizacion ##### idTipObjImpAtr2: " + objImpAtrVal.getTipObjImpAtr().getId());
			
			// Solo se valorizan los objImpAtrVal que haya devuelto la definicion obtenida
			 if(tipObjImpDefinition.getTipObjImpAtrDefinitionById(objImpAtrVal.getTipObjImpAtr().getId())!=null){
			
				 	// si es la primera vez o cambio de TipObjImpAtr
					if (objImpAtrVal.getTipObjImpAtr().getId().longValue() != idTipObjImpAtr 
							|| (tipObjImpAtrDef!=null && tipObjImpAtrDef.getEsMultivalor())) {	
						
						
						// obtiene un definition
						tipObjImpAtrDef = tipObjImpDefinition.getTipObjImpAtrDefinitionById(objImpAtrVal.getTipObjImpAtr().getId());
		
						// setea el valor de idTipObjImpAtr actual
						idTipObjImpAtr = objImpAtrVal.getTipObjImpAtr().getId();

						valor = objImpAtrVal.getStrValor();
						log.debug("valor:"+valor);
						
						// Filtramos solo los valores vigentes para la fecha de analisis 
						// Si no es multivalor lo dejamos como antes.
						if (tipObjImpAtrDef.getEsMultivalor()) {
							if(DateUtil.isDateInRange(fechaAnalisis, objImpAtrVal.getFechaDesde(), objImpAtrVal.getFechaHasta()))
							tipObjImpAtrDef.addValor(valor, 
									objImpAtrVal.getFechaNovedad(), 
									objImpAtrVal.getFechaDesde(), 
									objImpAtrVal.getFechaHasta());
							
						} else {
							tipObjImpAtrDef.addValor(valor);							
						}

						
						/*
						// si no es la primera vez, tengo que setear el valor al viejo
						if (idTipObjImpAtr != -1 || (tipObjImpAtrDef!=null && tipObjImpAtrDef.getEsMultivalor())) {
							// si es multivalor, agregar distinto 
							
							// Dado el orden en que se recuperan los valores (t.tipObjImpAtr, t.fechaDesde ),
							valor = objImpAtrVal.getStrValor();
							tipObjImpAtrDef.addValor(valor);
						}
						*/
						
					}
					
					
					log.debug(" >>>>>> posee vigencia: " + tipObjImpAtrDef.getTipObjImpAtr().getPoseeVigencia());
							
					
					if (tipObjImpAtrDef.getTipObjImpAtr().getPoseeVigencia().getEsSI()){
					
						valorVigencia = tipObjImpAtrDef.getValor4VigView(objImpAtrVal.getStrValor()); 
		
						log.debug("tiene vigencia. : " + " - valorVigencia=" + valorVigencia );
		
						// agrega el valor a la lista de vigencias
						 tipObjImpAtrDef.addAtrValVig(valorVigencia, 
														objImpAtrVal.getFechaNovedad(),  
														objImpAtrVal.getFechaDesde(), 
														objImpAtrVal.getFechaHasta() );
					}		
			 }
		}
		
		log.debug("          ###################### --> GetDefinition 4: return");
		
		log.debug("lista de definitions");
		for (TipObjImpAtrDefinition def:tipObjImpDefinition.getListTipObjImpAtrDefinition()) {
			log.debug(def.getAtributo().getDesAtributo() + " valor: " + def.getValorView() + " - " + def.getListValor().size());
		}		
	}
	
	
	
	/**
	 * Obtiene la definicion con sus valores del TipObjImp para este objeto imponible.
	 * <p>Este metodo sirve para obtener los atributos y sus valores de un objeto imponible.
	 * @return el ObjImpAtrDefinition cargado con los atributos y sus valores.
	 * @throws Exception 
	 */
	public TipObjImpDefinition getDefinitionValue(Date fechaAnalisis) throws Exception {
		// Obtiene la definicion de los atributos sin valorizar con getDefinitionForManual()
		// Recupera los todos los ObjImpAtrVal asociados al ObjImp ordenados por TipObjImpAtr y fechaNovedad desc  		
		// Ahora se valoriza la definicion de la siguiente forma:
		// 		Se recorre el resultado obtenido:
		//    	y se valoriza con corte de control por tipObjImpAtr

		
		log.debug("          ###################### --> GetDefinition 1: getDefinitionForManual()");		
		TipObjImpDefinition tipObjImpDefinition = this.getTipObjImp().getDefinitionForManual();		
		
		loadDefinition(tipObjImpDefinition, fechaAnalisis);
		
		return tipObjImpDefinition;
	}

	/**
	 * Obtiene la definicion de un unico atributo con sus valores del TipObjImp para este objeto imponible.
	 * <p>Este metodo sirve para obtener un unico atributo y sus valores de un objeto imponible.
	 * @param idTipObjImpAtr id del Atributo de Tipo Objeto Imponible a cargar.
	 * @return el ObjImpAtrDefinition cargado con el atributo y sus valores cuyo id es el del parametro.
	 * @throws Exception 
	 */
	public TipObjImpDefinition getDefinitionValue(Long idTipObjImpAtr) throws Exception {
		// Se obtiene la definicion para el idTipObjImpAtr
		// Recupera los todos los ObjImpAtrVal asociados al ObjImp y TipObjImpAtr ordenados por TipObjImpAtr y fechaNovedad desc  		
		// Ahora se valoriza la definicion de la siguiente forma:
		// 		Se recorre el resultado obtenido:
		//    	y se valoriza con corte de control por tipObjImpAtr
		
		TipObjImpDefinition tipObjImpDefinition = this.getTipObjImp().getDefinitionForManual(idTipObjImpAtr);

		log.debug("          ###################### --> GetAtrValDefinition 1: ObjImpAtrVal.getListByIdObjImpAtrVal()");
		// Es un list porque puede poseer registros de vigencias
		List<ObjImpAtrVal> listObjImpAtrVal = ObjImpAtrVal.getListByIdObjImpAtrVal(idTipObjImpAtr, this.getId());
		
		// Obtengo el primer elemento del resultado si existe valorizacion
		if (listObjImpAtrVal.size() > 0) {
			ObjImpAtrVal objImpAtrVal = listObjImpAtrVal.get(0); 
			
			log.debug("          ###################### --> GetAtrValDefinition 2: tipObjImpDefinition.getTipObjImpAtrDefinitionById()");
			// Recupero la definicon
			TipObjImpAtrDefinition tipObjImpAtrDefinition = tipObjImpDefinition.getTipObjImpAtrDefinitionById(objImpAtrVal.getTipObjImpAtr().getId());
																	//objImpAtrVal.getIdTipObjImpAtr());
			
			if(!tipObjImpAtrDefinition.getEsMultivalor()){		
				// Seteo el valor
				tipObjImpAtrDefinition.addValor(objImpAtrVal.getStrValor());
				
				log.debug("          ###################### --> GetAtrValDefinition 3: bucle de seteo de vigencias");
				// Si posee vigencia recorro y cargo todos los valores
				if (tipObjImpAtrDefinition.getTipObjImpAtr().getPoseeVigencia().getEsSI()){
					for (ObjImpAtrVal item: listObjImpAtrVal ){
						String valorVigencia = "";
						
						valorVigencia = tipObjImpAtrDefinition.getValor4VigView(item.getStrValor());
						
						// agrega un valor a la lista de vigencias
						tipObjImpAtrDefinition.addAtrValVig(valorVigencia, 
															item.getFechaNovedad(),
															item.getFechaDesde(), 
															item.getFechaHasta() );
					}	
				}
			}else{
				for (ObjImpAtrVal item: listObjImpAtrVal ){
					
					tipObjImpAtrDefinition.addValor(item.getStrValor(), item.getFechaNovedad(), item.getFechaDesde(), item.getFechaHasta());
					
				}
			}
		
		} 
		
		// Si no hay resultado es porque no fue valorizado y devuelvo unicamente la definicion
			
		log.debug("          ###################### --> GetAtrValDefinition 4: return");

		return tipObjImpDefinition;
	}
	
/*	public TipObjImpDefinition getDefinitionValueForWeb() throws Exception {
		
		// Obtiene la definicion de los atributos sin valorizar con getDefinitionForWeb()
		// Recupera los todos los ObjImpAtrVal asociados al ObjImp ordenados por TipObjImpAtr  		
		// Ahora se valoriza la definicion de la siguiente forma:
		// 		Se recorre el resultado obtenido:
		
		log.debug("          ###################### --> getDefinitionValueForWeb 1: getDefinitionForWeb()");		
		TipObjImpDefinition tipObjImpDefinition = this.getTipObjImp().getDefinitionForWeb();		
		log.debug("          ###################### --> getDefinitionValueForWeb 2: ObjImpAtrVal.getListByIdObjImp()");
		
		List<ObjImpAtrVal> listObjImpAtrVal =  ObjImpAtrVal.getListByIdObjImpIdsTipObjImpAtr(this.getId(), tipObjImpDefinition.getListIds());
		log.debug("          ###################### --> getDefinitionValueForWeb 3: Valorizacion de la definicion y vigencias()");
		for (ObjImpAtrVal objImpAtrVal: listObjImpAtrVal ){
			// por cada uno (ObjImpAtrVal) busco en la definicion por codigo atributo y lo valorizo
			//TipObjImpAtrDefinition tipObjImpAtrDef = tipObjImpDefinition.getTipObjImpAtrDefinitionById(objImpAtrVal.getTipObjImpAtr().getId());
			TipObjImpAtrDefinition tipObjImpAtrDef = tipObjImpDefinition.getTipObjImpAtrDefinitionById(objImpAtrVal.getIdTipObjImpAtr());
				
			if (tipObjImpAtrDef != null){
				tipObjImpAtrDef.addValor(objImpAtrVal.getStrValor());
			}
		}
		log.debug("          ###################### --> getDefinitionValueForWeb 4: return");
		return tipObjImpDefinition;
		
	}*/
	
	
	/** Si la fecha de baja es mayor a la actual, o nula el Objeto Imponible 
	 *  esta Vigente, si es menor o igual el Objeto Imponible esta no vigente.
	 *  
	 */
    public Integer getVigencia() {

    	Date fechaBajaValue = this.fechaBaja;

    	if (fechaBajaValue == null) {
    		return Vigencia.VIGENTE.getId();
    	}

    	if (DateUtil.isDateAfter(fechaBajaValue, new Date()) ) {
    		return Vigencia.VIGENTE.getId();
    	} else {
    		return Vigencia.NOVIGENTE.getId();
    	}

    }
	
    /**
     * Obtiene la lista de cuentas asociadas este objeto imponible.
     * @return lista de cuentas
     */
    public List<Cuenta> getListCuentaActiva() throws Exception {
    	return (List<Cuenta>) PadDAOFactory.getCuentaDAO().getListActivaByObjImp(this);
    	
    }
    
    /**
     * Obtiene Cuenta Principal asociada a este Objeto imponible.
     * Cuenta Principal es aquella que esta asociado un recurso principal
     * @return Cuenta con recurso principal
     */
    public Cuenta getCuentaPrincipalActiva() throws Exception{
		Recurso recPri = getTipObjImp().getRecursoPrincipal();
    	return getCuentaActivaByRecurso(recPri);
    }
    
    /**
     * Obtiene Lista de Cuentas Secundarias asociada a este Objeto imponible.
     * Cuenta Secundaria es aquella que esta asociada a un recurso secundario
     * @return Cuenta con recurso principal
     */
    public List<Cuenta> getListCuentaSecundariaActiva() throws Exception {
    	return (List<Cuenta>) PadDAOFactory.getCuentaDAO().getListCuentaSecundariaActivaByObjImp(this); 
    }
    /**
     * Obtiene Cuenta Principal asociada a este Objeto imponible.
     * Cuenta Principal es aquella que esta asociado un recurso principal
     * @return Cuenta con recurso principal
     */
    public Cuenta getCuentaPrincipal() throws Exception{
		Recurso recPri = getTipObjImp().getRecursoPrincipal();
    	return getCuentaByRecurso(recPri);
    }
    
    /**
     * Obtiene la Cuenta que pertenece a este Objeto imponible y Recurso.
     * @param Recurso recurso asociado a la cuenta
     * @return Cuenta.
     */
    public Cuenta getCuentaByRecurso(Recurso recurso) throws Exception{
    	return (Cuenta) PadDAOFactory.getCuentaDAO().getCuentaByObjImpYRecurso(this, recurso);
    }
    
    
    /**
     * Obtiene la Cuenta activa que pertenece a este Objeto imponible y Recurso.
     * @param Recurso recurso asociado a la cuenta
     * @return Cuenta activa.
     */
    public Cuenta getCuentaActivaByRecurso(Recurso recurso) throws Exception{
    	return (Cuenta) PadDAOFactory.getCuentaDAO().getCuentaActivaByObjImpYRecurso(this, recurso);
    }

	/**
	 * Obtiene un Atributo-Valor del tipo especificado por idTipoAtrVal de la lista raw de ObjImpAtrVal 
	 * del Objeto Imponible 
	 *  
	 * @param idTipoAtrVal
	 * @return ObjImpAtrVal
	 */
    public ObjImpAtrVal getObjImpAtrValByIdTipo (Long idTipoAtrVal) {
		
    	for (ObjImpAtrVal atrVal: this.getListObjImpAtrValRaw()) {
			if  (atrVal.getIdTipObjImpAtr().longValue() == idTipoAtrVal.longValue()) 
				return atrVal ;
		}
		
		return null;
	}


    /**
     * Borra los objImpAtrVal del objImp que tengan el tipObjImpAtr pasado como parametro
     * @param tipObjImpAtr
     */
    public void deleteObjImpAtrValById(Long idTipObjImpAtr) {
		for(ObjImpAtrVal o:listObjImpAtrVal){
			if(o.getTipObjImpAtr().getId().longValue() == idTipObjImpAtr.longValue()){				
				PadDAOFactory.getObjImpAtrValDAO().delete(o);
				log.debug("borro un objImpAtrVal: " + o.getId() + " - " + o.getStrValor());
			}
		}
	}
}
