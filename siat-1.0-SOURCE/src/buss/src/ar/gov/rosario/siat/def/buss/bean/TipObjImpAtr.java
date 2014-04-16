//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.buss.dao.SiatBussCache;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pad.buss.bean.ObjImpAtrVal;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a TipoObjImpAtr
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_tipobjimpatr")
public class TipObjImpAtr extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_PARCELA_TIPO_PARCELA = 41L;
	public static final Long ID_ZONA_TIPO_PARCELA = 22L;   // atributo Zona, tipObjImp Parcela
	public static final Long ID_SECCION_TIPO_PARCEL= 28L; // Atributo seccion , tipObjImp Parcela
	public static final Long ID_RUBROS_TIPO_COMERCIO = 53L; // Atributo rubros, tipObjImp Comercio
	public static final Long ID_RADIO_COMERCIO = 57L;
	public static final Long ID_PERMISO = 60L;
	public static final Long ID_DESCRIP_ACTIVIDAD_DREI=63L;
	
	public static final Long ID_DOMICILIOFINCA=36L;
	public static final Long ID_UBICACIONTERRENO=42L;
	
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idtipobjimp")	
	private TipObjImp tipObjImp;
	
	@ManyToOne(optional=false, fetch=FetchType.EAGER) 
    @JoinColumn(name="idatributo")
	private Atributo atributo;
	
	@Column(name = "esMultivalor") // NO NULO
	private Integer esMultivalor; 
	
	@Column(name = "esRequerido") // NO NULO
	private Integer esRequerido;
	
	@Column(name = "poseeVigencia") // NO NULO
	private Integer poseeVigencia; 

	@Column(name = "esClave") // NO NULO
	private Integer esClave; 

	@Column(name = "esClaveFuncional") // NO NULO
	private Integer esClaveFuncional; 
	
	@Column(name = "esDomicilioEnvio")
	private Integer esDomicilioEnvio; 

	@Column(name = "valorDefecto")
	private String valorDefecto; 

	@Column(name = "esVisConDeu") // NO NULO
	private Integer esVisConDeu; 

	@Column(name = "esAtributoBus") // NO NULO
	private Integer esAtributoBus;
	
	@Column(name = "esAtrBusMasiva") // NO NULO por ahora es nulo
	private Integer esAtriBusMasiva; 

	@Column(name = "admBusPorRan") // NO NULO
	private Integer admBusPorRan; 

	@Column(name = "esAtributoSIAT") // NO NULO
	private Integer esAtributoSIAT;
	
	@Column(name = "esUbicacion") // NO NULO
	private Integer esUbicacion; 

	@Column(name = "posColInt")
	private Integer posColInt; 
	
	@Column(name = "posColIntHas")
	private Integer posColIntHas; 

	@Column(name = "fechaDesde") // YEAR TO DAY NOT NULL
	private Date fechaDesde;

	@Column(name = "fechaHasta") // YEAR TO DAY 
	private Date fechaHasta;
	
	@Column(name="valDefEsRef")
	private Integer valDefEsRef;
	
	@Column(name = "esVisible") // NO NULO
	private Integer esVisible; 

	@Column(name = "esCodGesCue") 
	private Integer esCodGesCue; 
	
	// Constructores
	public TipObjImpAtr(){
		super();
		// Seteo de valores default	
	}
	
	//	Getters y setters
	public Integer getAdmBusPorRan() {
		return admBusPorRan;
	}
	public void setAdmBusPorRan(Integer admBusPorRan) {
		this.admBusPorRan = admBusPorRan;
	}
	public Atributo getAtributo() {
		return atributo;
	}
	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
	}
	public Integer getEsAtributoBus() {
		return esAtributoBus;
	}
	public void setEsAtributoBus(Integer esAtributoBus) {
		this.esAtributoBus = esAtributoBus;
	}
	public Integer getEsAtriBusMasiva() {
		return esAtriBusMasiva;
	}
	public void setEsAtriBusMasiva(Integer esAtriBusMasiva) {
		this.esAtriBusMasiva = esAtriBusMasiva;
	}
	public Integer getEsAtributoSIAT() {
		return esAtributoSIAT;
	}
	public void setEsAtributoSIAT(Integer esAtributoSIAT) {
		this.esAtributoSIAT = esAtributoSIAT;
	}
	public Integer getEsClave() {
		return esClave;
	}
	public void setEsClave(Integer esClave) {
		this.esClave = esClave;
	}
	public Integer getEsClaveFuncional() {
		return esClaveFuncional;
	}
	public void setEsClaveFuncional(Integer esClaveFuncional) {
		this.esClaveFuncional = esClaveFuncional;
	}
	public Integer getEsDomicilioEnvio() {
		return esDomicilioEnvio;
	}
	public void setEsDomicilioEnvio(Integer esDomicilioEnvio) {
		this.esDomicilioEnvio = esDomicilioEnvio;
	}
	public Integer getEsMultivalor() {
		return esMultivalor;
	}
	public void setEsMultivalor(Integer esMultivalor) {
		this.esMultivalor = esMultivalor;
	}
	public Integer getEsUbicacion() {
		return esUbicacion;
	}
	public void setEsUbicacion(Integer esUbicacion) {
		this.esUbicacion = esUbicacion;
	}
	public Integer getEsVisConDeu() {
		return esVisConDeu;
	}
	public void setEsVisConDeu(Integer esVisConDeu) {
		this.esVisConDeu = esVisConDeu;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	public Integer getPosColInt() {
		return posColInt;
	}
	public void setPosColInt(Integer posColInt) {
		this.posColInt = posColInt;
	}
	public Integer getPosColIntHas() {
		return posColIntHas;
	}
	public void setPosColIntHas(Integer posColIntHas) {
		this.posColIntHas = posColIntHas;
	}
	public Integer getPoseeVigencia() {
		return poseeVigencia;
	}
	public void setPoseeVigencia(Integer poseeVigencia) {
		this.poseeVigencia = poseeVigencia;
	}
	public TipObjImp getTipObjImp() {
		return tipObjImp;
	}
	public void setTipObjImp(TipObjImp tipObjImp) {
		this.tipObjImp = tipObjImp;
	}
	public String getValorDefecto() {
		return valorDefecto;
	}
	public void setValorDefecto(String valorDefecto) {
		this.valorDefecto = valorDefecto;
	}
	
	public Integer getEsRequerido() {
		return esRequerido;
	}
	public void setEsRequerido(Integer esRequerido) {
		this.esRequerido = esRequerido;
	}
		
	public Integer getValDefEsRef() {
		return valDefEsRef;
	}

	public void setValDefEsRef(Integer valDefEsRef) {
		this.valDefEsRef = valDefEsRef;
	}

	public Integer getEsVisible() {
		return esVisible;
	}

	public void setEsVisible(Integer esVisible) {
		this.esVisible = esVisible;
	}
	// Metodos de Clase

	public void setEsCodGesCue(Integer esCodGesCue) {
		this.esCodGesCue = esCodGesCue;
	}

	public Integer getEsCodGesCue() {
		return esCodGesCue;
	}

	/**
	 * Obtiene el Tipo Objeto Imponible Atributo a partir de su Id 
	 * @param id
	 * @return TipObjImpAtr
	 */
	public static TipObjImpAtr getById(Long id) {
		return (TipObjImpAtr) DefDAOFactory.getTipObjImpAtrDAO().getById(id);
	}

	public static TipObjImpAtr getByIdNull(Long id) {
		return (TipObjImpAtr) DefDAOFactory.getTipObjImpAtrDAO().getByIdNull(id);
	}
	
	/**
	 * Obtiene el Tipo Objeto Imponible Atributo a partir de su Id de Atributo 
	 * @param idAtributo
	 * @return TipObjImpAtr
	 */
	public static TipObjImpAtr getByIdAtributo(Long idAtributo) {
		return (TipObjImpAtr) DefDAOFactory.getTipObjImpAtrDAO().getByIdAtributo(idAtributo);
	}
	
	/**
	 * Obtiene la lista de todos los Tipo Objeto Imponible Atributo.
	 * @return List<TipObjImpAtr>
	 */
	public static List<TipObjImpAtr> getList() {
		return (ArrayList<TipObjImpAtr>) DefDAOFactory.getTipObjImpAtrDAO().getList();
	}
	
	/**
	 * Obtiene la lista de los Tipo Objeto Imponible Atributo activos.
	 * @return List<TipObjImpAtr>
	 */
	public static List<TipObjImpAtr> getListActivos() {			
		return (ArrayList<TipObjImpAtr>) DefDAOFactory.getTipObjImpAtrDAO().getListActiva();
	}
	
	/**
	 * Obtiene la lista de los Tipo Objeto Imponible Atributo activos para determinado Tipo Objeto Imponible.
	 * @return List<TipObjImpAtr>
	 */
	public static List<TipObjImpAtr> getListActivosByIdTipObjImp(Long id) {			
		return (List<TipObjImpAtr>) DefDAOFactory.getTipObjImpAtrDAO().getListActivosByIdTipObjImp(id);
	}
	
	/**
	 * Obtiene la lista de los Tipo Objeto Imponible Atributo activos para determinado Tipo Objeto Imponible 
	 * excluyendo los pasados en la lista. Sirve para seleccionar el atributo en para RecGenCueAtrVa.
	 * @return List<TipObjImpAtr>
	 */
	public static List<Atributo> getListAtributoByIdTipObjImpForRecurso(Long id, List<AtributoVO> listAtributosExcluidos) {			
		return (List<Atributo>) DefDAOFactory.getTipObjImpAtrDAO().getListAtributoByIdTipObjImpForRecurso(id, listAtributosExcluidos);
	}
	
	public static List<TipObjImpAtr> getList(Long idTipObjImp, boolean esSiat, boolean esReferenciado, Estado estado){
		return (ArrayList<TipObjImpAtr>) DefDAOFactory.getTipObjImpAtrDAO().getList(idTipObjImp, esSiat,esReferenciado,
				estado);
	}
	
	
	/**
	 * Obtiene el Tipo Objeto Imponible Atributo a partir de un Id TipObjImp y un Id de Atributo. 
	 * 
	 * @param idAtributo
	 * @return TipObjImpAtr
	 */
	public static TipObjImpAtr getByIdTipObjImpyIdAtributo(Long idTipObjImp, Long idAtributo) {
		return (TipObjImpAtr) DefDAOFactory.getTipObjImpAtrDAO().getByIdTipObjImpyIdAtributo(idTipObjImp, idAtributo);
	}
	
	
	/**
	 * Devuelve un TipObjImpAtr buscandolo primero en el Cache.
	 * 
	 * @param idTipObjImp
	 * @param id
	 * @return
	 */
	public static TipObjImpAtr getByIdFromCache(Long idTipObjImp, Long id) {
		
		try {
			
			List<TipObjImpAtr> tipObjImpAtrDefinition = SiatBussCache.getInstance().getListTipObjImpAtrManual(idTipObjImp);
			
			for(TipObjImpAtr tipObjImpAtr:tipObjImpAtrDefinition){
				if (tipObjImpAtr.getId().equals(id))
					return tipObjImpAtr;
			}
			
		 } catch(Exception e){
			 e.printStackTrace();
		 }
		
		 return (TipObjImpAtr) DefDAOFactory.getTipObjImpAtrDAO().getById(id);
	}
	
	// Validaciones
	/**
	 * Valida la Creacion de un Tipo Objeto Imponible Atributo
	 * Ademas de las validaciones comunes, valida la unicidad de tipObjImp y atributo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones requerido y unicidad
		this.validate();
		
		// Valida que no tenga si el atributo es clave, no tenga otro que ya es clave
		if(SiNo.SI.getId().equals(this.getEsClave()) && this.getTipObjImp().hasAtrClave()){
			addRecoverableError(DefError.TIPOBJIMP_YA_TIENE_CLAVE);
		}
		
		// Valida que no tenga si el atributo es clave funcional, no tenga otro que ya es clave funcional
		if(SiNo.SI.getId().equals(this.getEsClaveFuncional()) && this.getTipObjImp().hasAtrClaveFuncional()){
			addRecoverableError(DefError.TIPOBJIMP_YA_TIENE_CLAVE_FUNCIONAL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones unicidad solo aplicables a la creacion

		UniqueMap uniqueMap = new UniqueMap();
		// tipoObjetoImponible + atributo unicos

		uniqueMap.addEntity("tipObjImp").addEntity("atributo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)){
			addRecoverableError(BaseError.MSG_CAMPO_UNICOS2, new Object[]{DefError.TIPOBJIMP_LABEL,DefError.ATRIBUTO_LABEL});
		}

		
		return (!hasError());
	}

	/**
	 * Valida la actualizacion de un Tipo Objeto Imponible Atributo
	 * @return boolean
	 */
	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		// Valida que si el atributo es clave, no tenga otro que ya es clave
		if(SiNo.SI.getId().equals(this.getEsClave()) && this.getTipObjImp().hasAtrClave(this.getId())){
			addRecoverableError(DefError.TIPOBJIMP_YA_TIENE_CLAVE);
		}
		
		// Valida que no tenga si el atributo es clave funcional, no tenga otro que ya es clave funcional
		if(SiNo.SI.getId().equals(this.getEsClaveFuncional()) && this.getTipObjImp().hasAtrClaveFuncional(this.getId())){
			addRecoverableError(DefError.TIPOBJIMP_YA_TIENE_CLAVE_FUNCIONAL);
		}
		
		if (hasError()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Validaciones comunes a la creacion y actualizacion
	 * Requeridos: esMultivalor, poseeVigencia, esClave, esClaveFuncional,  
	 * esVisConDeu, esAtributoBus, admBusPorRan, esUbicacion, esAtributoSIAT y
	 * fechaDesde. 
	 * Si esAtributoSIAT: valida requeridos posColInt y posColIntHas, posColInt > 0, posColIntHas > 0 y posColInt menor o igual a posColIntHas 
	 * Si Fecha Hasta no es nula: valida que Fecha desde no sea menor a Fecha Hasta.
	 * @return boolean
	 */
	private boolean validate() throws Exception {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de campos requeridos
		
		// tipoObjetoImponible (no hace falta) 
		// atributo (no hace falta)
		// esRequerido
		if(this.getEsRequerido() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESREQUERIDO);
		}
		
		// esMultivalor
		if(this.getEsMultivalor() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESMULTIVALOR);
		}		
		// poseeVigencia
		if(this.getPoseeVigencia() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_POSEEVIGENCIA);
		}		
		// esClave
		if(this.getEsClave() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESCLAVE);
		}		
		// esClaveFuncional
		if(this.getEsClaveFuncional() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESCLAVEFUNCIONAL);
		}
		// esCodGesCue
		if(this.getEsCodGesCue() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESCODGESCUE);
		}
		// esVisConDeu
		if(this.getEsVisConDeu() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESVISCONDEU);
		}
		// esVisible
		if(this.getEsVisible() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESVISIBLE);
		}
		// esAtributoBus
		if(this.getEsAtributoBus() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESATRIBUTOBUS);
		}
		// esAtriBusMasiva
		if(this.getEsAtriBusMasiva() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESATRIBUSMASIVA);
		}

		// admBusPorRan
		if(this.getAdmBusPorRan() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ADMBUSPORRAN);
		}
		// esUbicacion
		if(this.getEsUbicacion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESUBICACION);
		}
		// esAtributoSIAT		
		if(this.getEsAtributoSIAT() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_ESATRIBUTOSIAT);
		}
		if (SiNo.NO.getId() == this.getEsAtributoSIAT()){
			// posColInt			
			if (this.getPosColInt() == null){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_POSCOLINT);
			}else if(this.getPosColInt().intValue() <= 0){
				addRecoverableError(BaseError.MSG_VALORMENORIGUALQUECERO, DefError.TIPOBJIMPATR_POSCOLINT);
			}
			// posColIntHas			
			if (this.getPosColIntHas() == null){
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_POSCOLINTHAS);	
			}
			// posColIntHas no sea menor a cero
			if(this.getPosColIntHas() != null && this.getPosColIntHas().intValue() <= 0){
				addRecoverableError(BaseError.MSG_VALORMENORIGUALQUECERO, DefError.TIPOBJIMPATR_POSCOLINTHAS);
			} 
			// posColIntHas no sea menor a posConInt
			if(this.getPosColInt() != null && 
					this.getPosColIntHas() != null && 
					this.getPosColIntHas().intValue() < this.getPosColInt().intValue()){
				addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.TIPOBJIMPATR_POSCOLINTHAS, DefError.TIPOBJIMPATR_POSCOLINT );
			}
		}
		// fechaDesde
		if(this.getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOBJIMPATR_FECHADESDE);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de rangos de fecha:

		// valida que Fecha Desde no sea mayor a Fecha Hasta
		if ( this.getFechaHasta() != null && DateUtil.isDateAfter(getFechaDesde(), getFechaHasta()) ) {
			addRecoverableError( BaseError.MSG_VALORMAYORQUE, 
				DefError.TIPOBJIMPATR_FECHADESDE, DefError.TIPOBJIMPATR_FECHAHASTA );
		}
		
		// valida que fecha Desde no sea menor a Fecha Alta del TipObjImp
		Date fechaAltaTOI = this.tipObjImp.getFechaAlta();
		if ( DateUtil.isDateBefore(getFechaDesde(), fechaAltaTOI) ) {
			addRecoverableError( BaseError.MSG_VALORMENORQUE, 
				DefError.TIPOBJIMPATR_FECHADESDE, DefError.TIPOBJIMP_FECHAALTA );
		}
		
		// valida que fecha Hasta no sea mayor a Fecha Baja del TipObjImp
		Date fechaBajaTOI = this.tipObjImp.getFechaBaja();
		if ( fechaBajaTOI != null && DateUtil.isDateAfter(getFechaHasta(), fechaBajaTOI) ) {
			addRecoverableError( BaseError.MSG_VALORMAYORQUE, 
				DefError.TIPOBJIMPATR_FECHAHASTA, DefError.TIPOBJIMP_FECHABAJA );
		}
		
		// validar que el formato del valorDefecto coincida con el tipo correspondiente
		if(!StringUtil.isNullOrEmpty(this.getValorDefecto()) && !this.getAtributo().getTipoAtributo().validarTipoValor(this.getValorDefecto())){
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, DefError.TIPOBJIMPATR_VALORDEFECTO );
		}
		
		return (!hasError());
	}
	
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		if (GenericDAO.hasReference(this, ObjImpAtrVal.class, "tipObjImpAtr")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.TIPOBJIMPATR_LABEL , PadError.OBJIMPATRVAL_LABEL);
		}
		
		if (hasError()) {
			return false;
		}
	
		return true;
	}
	
	/**
	 * Realiza el toVO especifico  si tener en cuenta TipObjImp.
	 * 
	 * @return TipObjImpAtrVO
	 * @throws Exception
	 */
	public TipObjImpAtrVO toVOSinPadre() throws Exception{
		
		TipObjImpAtrVO toiVO = new TipObjImpAtrVO();
		
		toiVO.setId(this.getId());
		
		toiVO.setEsMultivalor(SiNo.getById(this.getEsMultivalor())); 
		toiVO.setEsRequerido(SiNo.getById(this.getEsRequerido()));
		toiVO.setPoseeVigencia(SiNo.getById(this.getPoseeVigencia())); 
		toiVO.setEsClave(SiNo.getById(this.getEsClave())); 
		toiVO.setEsClaveFuncional(SiNo.getById(this.getEsClaveFuncional())); 
		toiVO.setEsDomicilioEnvio(SiNo.getById(this.getEsDomicilioEnvio())); 
		toiVO.setValorDefecto(this.getValorDefecto()); 
		toiVO.setEsVisConDeu(SiNo.getById(this.getEsVisConDeu())); 
		toiVO.setEsAtributoBus(SiNo.getById(this.getEsAtributoBus())); 
		toiVO.setAdmBusPorRan(SiNo.getById(this.getAdmBusPorRan())); 
		toiVO.setEsAtributoSIAT(SiNo.getById(this.getEsAtributoSIAT())); 
		toiVO.setEsUbicacion(SiNo.getById(this.getEsUbicacion())); 
		toiVO.setPosColInt(this.getPosColInt()); 
		toiVO.setPosColIntHas(this.getPosColIntHas()); 
		toiVO.setFechaDesde(this.getFechaDesde());
		toiVO.setFechaHasta(this.getFechaHasta());
		toiVO.setEsVisible(SiNo.getById(this.getEsVisible()));
		
		toiVO.setAtributo((AtributoVO) this.getAtributo().toVO(3));
		
		return toiVO;
	}
	
	public String getEsMultivalorReport(){
		return "Es Multivalor: " + SiNo.getById(this.getEsMultivalor()).getValue();
	}
	public String getPoseeVigenciaReport(){
		return "Posee Vigencia: " + SiNo.getById(this.getPoseeVigencia()).getValue();
	}
	public String getEsClaveReport(){
		return "Es Clave: " + SiNo.getById(this.getEsClave()).getValue();
	}
	public String getEsClaveFuncionalReport(){
		return "Es Clave Funcional: " + SiNo.getById(this.getEsClaveFuncional()).getValue();
	}
	public String getEsDomicilioEnvioReport(){
		return "Es Domicilio Envío: " + SiNo.getById(this.getEsDomicilioEnvio()).getValue();
	}
	public String getEsUbicacionReport(){
		return "Es Ubicación: " + SiNo.getById(this.getEsUbicacion()).getValue();
	}
	public String getValorDefectoReport(){
		return "Valor por Defecto: " + this.getValorDefecto();
	}

	public String getEsVisConDeuReport(){
		return "Es Vis. Cons. Deu.: " + SiNo.getById(this.getEsVisConDeu()).getValue();
	}
	public String getEsAtributoBusReport(){
		return "Es Atributo Búsq.: " + SiNo.getById(this.getEsAtributoBus()).getValue();
	}
	public String getEsAtriBusMasivaReport(){
		return "Es Atr. Búsq. Mas.: " + SiNo.getById(this.getEsAtriBusMasiva()).getValue();
	}
	public String getAdmBusPorRanReport(){
		return "Adm. Bus. por Ran.: " + SiNo.getById(this.getAdmBusPorRan()).getValue();
	}

	public String getEsAtributoSIATReport(){
		return "Es Atributo SIAT: " + SiNo.getById(this.getEsAtributoSIAT()).getValue();
	}
	public String getPosColIntReport(){
		return "Pos. Col. Desde: " + this.getPosColInt();
	}
	public String getPosColIntHasReport(){
		return "Pos. Col. Hasta: " + this.getPosColIntHas();
	}
}
