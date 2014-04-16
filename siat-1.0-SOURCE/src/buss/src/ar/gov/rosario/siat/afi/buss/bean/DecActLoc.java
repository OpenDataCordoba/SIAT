//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a DecActLoc - Declaración de Actividades 
 * por Local para el Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_decActLoc")
public class DecActLoc extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idLocal")
	private Local local;	
	
	@Column(name = "numerocuenta")
	private String  numeroCuenta;
	
	@Column(name = "codactividad")
	private Long	codActividad;
	
	@Column(name = "baseimpexenta")	
	private Double  baseImpExenta;
	
	@Column(name = "baseimponible")
	private Double  baseImponible;
	
	@Column(name = "ajucamcoe")
	private Double  ajuCamCoe;
	
	@Column(name = "baseimpajustada")
	private Double  baseImpAjustada;
	
	@Column(name = "alicuota")
	private Double  aliCuota;
	
	@Column(name = "derechocalculado")
	private Double  derechoCalculado;
	
	@Column(name = "cantidad")
	private Double  cantidad;
	
	@Column(name = "unidadmedida")
	private Integer unidadMedida;
	
	@Column(name = "tipounidad")
	private Integer  tipoUnidad;
	
	@Column(name = "minimoporunidad")
	private Double   minimoPorUnidad;
	
	@Column(name = "minimocalculado")
	private Double   minimoCalculado;
	
	@Column(name = "derechodet")
	private Double  derechoDet;
	
	@Column(name = "alcanceetur")
	private Integer alcanceEtur;
		
	@Column(name = "difbaseenero")
	private Double 	difBaseEnero;
	
	@Column(name = "difbasefebrero")
	private Double 	difBaseFebrero;
	
	@Column(name = "difbasemarzo")
	private Double	difBaseMarzo;
	
	@Column(name = "difbaseabril")
	private Double 	difBaseAbril;
	
	@Column(name = "difbasemayo")
	private Double 	difBaseMayo;
	
	@Column(name = "difbasejunio")
	private Double 	difBaseJunio;
	
	@Column(name = "difbasejulio")
	private Double  difBaseJulio;
	
	@Column(name = "difbaseagosto")
	private Double 	difBaseAgosto;
	
	@Column(name = "difbaseseptiembre")
	private Double 	difBaseSeptiembre;
	
	@Column(name = "difbaseoctubre")
	private Double 	difBaseOctubre ;
	
	@Column(name = "difbasenoviembre")
	private Double  difBaseNoviembre ;
	
	@Column(name = "difbasediciembre")
	private Double  difBaseDiciembre;
	
	
	// Constructores
	public DecActLoc(){
		super();
		// Seteo de valores default			
	}
	
	public DecActLoc(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static DecActLoc getById(Long id) {
		return (DecActLoc) AfiDAOFactory.getDecActLocDAO().getById(id);
	}
	
	public static DecActLoc getByIdNull(Long id) {
		return (DecActLoc) AfiDAOFactory.getDecActLocDAO().getByIdNull(id);
	}
	
	public static List<DecActLoc> getList() {
		return (ArrayList<DecActLoc>) AfiDAOFactory.getDecActLocDAO().getList();
	}
	
	public static List<DecActLoc> getListActivos() {			
		return (ArrayList<DecActLoc>) AfiDAOFactory.getDecActLocDAO().getListActiva();
	}
	
	
	// Getters y setters
		
	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public Long getCodActividad() {
		return codActividad;
	}

	public void setCodActividad(Long codActividad) {
		this.codActividad = codActividad;
	}

	public Double getBaseImpExenta() {
		return baseImpExenta;
	}

	public void setBaseImpExenta(Double baseImpExenta) {
		this.baseImpExenta = baseImpExenta;
	}

	public Double getBaseImponible() {
		return baseImponible;
	}

	public void setBaseImponible(Double baseImponible) {
		this.baseImponible = baseImponible;
	}

	public Double getAjuCamCoe() {
		return ajuCamCoe;
	}

	public void setAjuCamCoe(Double ajuCamCoe) {
		this.ajuCamCoe = ajuCamCoe;
	}

	public Double getBaseImpAjustada() {
		return baseImpAjustada;
	}

	public void setBaseImpAjustada(Double baseImpAjustada) {
		this.baseImpAjustada = baseImpAjustada;
	}

	public Double getAliCuota() {
		return aliCuota;
	}

	public void setAliCuota(Double aliCuota) {
		this.aliCuota = aliCuota;
	}

	public Double getDerechoCalculado() {
		return derechoCalculado;
	}

	public void setDerechoCalculado(Double derechoCalculado) {
		this.derechoCalculado = derechoCalculado;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(Integer unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Double getDifBaseEnero() {
		return difBaseEnero;
	}

	public void setDifBaseEnero(Double difBaseEnero) {
		this.difBaseEnero = difBaseEnero;
	}

	public Double getDifBaseFebrero() {
		return difBaseFebrero;
	}

	public void setDifBaseFebrero(Double difBaseFebrero) {
		this.difBaseFebrero = difBaseFebrero;
	}

	public Double getDifBaseMarzo() {
		return difBaseMarzo;
	}

	public void setDifBaseMarzo(Double difBaseMarzo) {
		this.difBaseMarzo = difBaseMarzo;
	}

	public Double getDifBaseAbril() {
		return difBaseAbril;
	}

	public void setDifBaseAbril(Double difBaseAbril) {
		this.difBaseAbril = difBaseAbril;
	}

	public Double getDifBaseMayo() {
		return difBaseMayo;
	}

	public void setDifBaseMayo(Double difBaseMayo) {
		this.difBaseMayo = difBaseMayo;
	}

	public Double getDifBaseJunio() {
		return difBaseJunio;
	}

	public void setDifBaseJunio(Double difBaseJunio) {
		this.difBaseJunio = difBaseJunio;
	}

	public Double getDifBaseJulio() {
		return difBaseJulio;
	}

	public void setDifBaseJulio(Double difBaseJulio) {
		this.difBaseJulio = difBaseJulio;
	}

	public Double getDifBaseAgosto() {
		return difBaseAgosto;
	}

	public void setDifBaseAgosto(Double difBaseAgosto) {
		this.difBaseAgosto = difBaseAgosto;
	}

	public Double getDifBaseSeptiembre() {
		return difBaseSeptiembre;
	}

	public void setDifBaseSeptiembre(Double difBaseSeptiembre) {
		this.difBaseSeptiembre = difBaseSeptiembre;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public Local getLocal() {
		return local;
	}

	public Double getDifBaseOctubre() {
		return difBaseOctubre;
	}

	public void setDifBaseOctubre(Double difBaseOctubre) {
		this.difBaseOctubre = difBaseOctubre;
	}

	public Double getDifBaseNoviembre() {
		return difBaseNoviembre;
	}

	public void setDifBaseNoviembre(Double difBaseNoviembre) {
		this.difBaseNoviembre = difBaseNoviembre;
	}

	public Double getDifBaseDiciembre() {
		return difBaseDiciembre;
	}

	public void setDifBaseDiciembre(Double difBaseDiciembre) {
		this.difBaseDiciembre = difBaseDiciembre;
	}

	public Integer getTipoUnidad() {
		return tipoUnidad;
	}

	public void setTipoUnidad(Integer tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	public Double getMinimoPorUnidad() {
		return minimoPorUnidad;
	}

	public void setMinimoPorUnidad(Double minimoPorUnidad) {
		this.minimoPorUnidad = minimoPorUnidad;
	}

	public Double getMinimoCalculado() {
		return minimoCalculado;
	}

	public void setMinimoCalculado(Double minimoCalculado) {
		this.minimoCalculado = minimoCalculado;
	}

	public Double getDerechoDet() {
		return derechoDet;
	}

	public void setDerechoDet(Double derechoDet) {
		this.derechoDet = derechoDet;
	}

	public Integer getAlcanceEtur() {
		return alcanceEtur;
	}

	public void setAlcanceEtur(Integer alcanceEtur) {
		this.alcanceEtur = alcanceEtur;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
			
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
//		if (StringUtil.isNullOrEmpty(getCodDeclaracionActLocal())) {
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_COD${BEAN} );
//		}
//		
//		if (StringUtil.isNullOrEmpty(getDesDeclaracionActLocal())){
//			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, AfiError.${BEAN}_DES${BEAN});
//		}
//		
//		if (hasError()) {
//			return false;
//		}
//		
//		// Validaciones de unique
//		UniqueMap uniqueMap = new UniqueMap();
//		uniqueMap.addString("codDeclaracionActLocal");
//		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
//			addRecoverableError(BaseError.MSG_CAMPO_UNICO, AfiError.${BEAN}_COD${BEAN});			
//		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el DecActLoc. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getDecActLocDAO().update(this);
	}

	/**
	 * Desactiva el DecActLoc. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getDecActLocDAO().update(this);
	}
	
	/**
	 * Valida la activacion del DecActLoc
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del DecActLoc
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}

	