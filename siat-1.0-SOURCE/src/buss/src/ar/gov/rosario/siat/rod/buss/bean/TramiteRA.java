//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

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

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.gde.buss.bean.Mandatario;
import ar.gov.rosario.siat.rod.buss.dao.RodDAOFactory;
import ar.gov.rosario.siat.rod.iface.util.RodError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a TramiteRA
 * 
 * @author tecso
 */
@Entity
@Table(name = "rod_tramitera")
public class TramiteRA extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	
	@Column(name = "nroComuna")
	private Integer nroComuna;
	
	@Column(name = "nroTramite")
	private Integer nroTramite;
	
	@Column(name = "codTipoTramite")
	private Integer codTipoTramite;
	
	@Column(name = "desTipoTramite")
	private String desTipoTramite;
	
	@Column(name = "rubros")
	private String rubros;

	// A
	@Column(name = "aNroPatente")
	private String aNroPatente;
	
	@Column(name = "aDigVerif")
	private Integer aDigVerif;
	
	// B
	@Column(name = "bCodMarca")
	private Integer bCodMarca;
	
	@Column(name = "bDesMarca")
	private String bDesMarca;
	
	@Column(name = "bCodModelo")
	private Integer bCodModelo;
	
	@Column(name = "bDesModelo")
	private String bDesModelo;
	
	@Column(name = "bAnio")
	private Integer bAnio;
	
	@Column(name = "bCodTipoVeh")
	private String bCodTipoVeh;
	
	@Column(name = "bDesTipoVeh")
	private String bDesTipoVeh;
	
	@Column(name = "bCodFab")
	private Integer bCodFab;
	
	@Column(name = "bDesFab")
	private String bDesFab;
	
	@Column(name = "bCodTM")
	private Integer bCodTipoMotor;
	
	@Column(name = "bDesTM")
	private String bDesTipoMotor;
	
	@Column(name = "bNroMotor")
	private String bNroMotor;
	
	@Column(name = "bCodUso")
	private Integer bCodUso;
	
	@Column(name = "bDesUso")
	private String bDesUso;
	
	@Column(name = "bHPoC")
	private String bHPoC;
	
	@Column(name = "bPesoVacio")
	private Double bPesoVacio;
	
	@Column(name = "bPrecioAlta")
	private Double bPrecioAlta;

	@Column(name = "bFechaFactura")
	private Date bFechaFactura;
	
	@Column(name = "bPesoCarga")
	private Double bPesoCarga;
	
	@Column(name = "bCodTipoCarga")
	private String bCodTipoCarga;
	
	@Column(name = "bDesTipoCarga")
	private String bDesTipoCarga;
	
	@Column(name = "bCapCarga")
	private Double bCapCarga;
	
	@Column(name = "bUnidadMedida")
	private String bUnidadMedida;
	
	@Column(name = "bCantidad")
	private Integer bCantidad;
	
	@Column(name = "bAsientos")
	private Integer bAsientos;
	
	@Column(name = "bCantEjes")
	private Integer bCantEjes;
	
	@Column(name = "bLargoCarr")
	private Double bLargoCarr;
	
	@Column(name = "bAltoCarr")
	private Double bAltoCarr;

	// C
	@Column(name = "cCantDuenios")
	private Integer cCantDuenios;
	
	@Column(name = "cIdPersonaActual")
	private Long cIdPersonaActual; // titular principal
	
	// D
	@Column(name = "dCodLocalidad")
	private String dCodLocalidad;
	
	@Column(name = "dDesLocalidad")
	private String dDesLocalidad;
	
	@Column(name = "dCodCalle")
	private Long dCodCalle;
	
	@Column(name = "dDesCalle")
	private String dDesCalle;
	
	@Column(name = "dNumero")
	private Long dNumero;
	
	@Column(name = "dPiso")
	private String dPiso;
	
	@Column(name = "dDpto")
	private String dDpto;
	
	@Column(name = "dBis")
	private Integer dBis;
	
	@Column(name = "dEsValido")
	private Integer dEsValido;

	// E
	@Column(name = "eDesMarca")
	private String eDesMarca;
	
	@Column(name = "eCodTipoMotor")
	private Integer eCodTipoMotor;
	
	@Column(name = "eDesTipoMotor")
	private String eDesTipoMotor;
	
	@Column(name = "eNroMotor")
	private String eNroMotor;
	
	// F
	@Column(name = "fDesMotivoBaja")
	private String fDesMotivoBaja;
	
	// G
	@Column(name = "gDesDomicilio")
	private String gDesDomicilio;
	
	
	// H
	@Column(name = "hPatentePad")
	private String hPatentePad;
	
	@Column(name = "hPatenteCorr")
	private String hPatenteCorr;
	
	//
	@Column(name = "fecha")
	private Date fecha;

	@Column(name = "observacion")
	private String observacion;
	
	@Column(name = "observacionAIP")
	private String observacionAPI;
	
	@Column(name = "observacionRNPA")
	private String observacionRNPA;
	
	// I
	@Column(name = "iCodPago")
	private Integer iCodPago;
	
	@Column(name = "iDesPago")
	private String iDesPago;
	
	@Column(name = "iDesBancoMuni")
	private String iDesBancoMuni;
	
	@Column(name = "iImporte1")
	private Double iImporte1;
	
	@Column(name = "iFecha1")
	private Date iFecha1;
	
	@Column(name = "iImporte2")
	private Double iImporte2;
	
	@Column(name = "iFecha2")
	private Date iFecha2;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idMandatario")
	private Mandatario mandatario;
	
	// cambio de estado
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstTra")
	private EstadoTramiteRA estTra; 

	@OneToMany(mappedBy="tramiteRA", fetch=FetchType.LAZY)
	@JoinColumn(name="idTramite")
	private List<HisEstTra> listHisEstTra;
	
	@OneToMany(mappedBy="tramiteRA", fetch=FetchType.LAZY)
	@JoinColumn(name="idTramiteRA")
	private List<Propietario> listPropietario;
	
	// Constructores
	public TramiteRA(){
		super();
		// Seteo de valores default	
	
	}
	
	public TramiteRA(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TramiteRA getById(Long id) {
		return (TramiteRA) RodDAOFactory.getTramiteRADAO().getById(id);
	}
	
	public static TramiteRA getByIdNull(Long id) {
		return (TramiteRA) RodDAOFactory.getTramiteRADAO().getByIdNull(id);
	}
	
	public static List<TramiteRA> getList() {
		return (ArrayList<TramiteRA>) RodDAOFactory.getTramiteRADAO().getList();
	}
	
	public static List<TramiteRA> getListActivos() {			
		return (ArrayList<TramiteRA>) RodDAOFactory.getTramiteRADAO().getListActiva();
	}
	
	
	// Getters y setters



	public Integer getNroComuna() {
		return nroComuna;
	}

	public void setNroComuna(Integer nroComuna) {
		this.nroComuna = nroComuna;
	}

	public Integer getNroTramite() {
		return nroTramite;
	}

	public void setNroTramite(Integer nroTramite) {
		this.nroTramite = nroTramite;
	}

	public Integer getCodTipoTramite() {
		return codTipoTramite;
	}

	public void setCodTipoTramite(Integer codTipoTramite) {
		this.codTipoTramite = codTipoTramite;
	}

	public String getANroPatente() {
		return aNroPatente;
	}

	public void setANroPatente(String nroPatente) {
		aNroPatente = nroPatente;
	}

	public Integer getADigVerif() {
		return aDigVerif;
	}

	public void setADigVerif(Integer digVerif) {
		aDigVerif = digVerif;
	}

	public Integer getBCodMarca() {
		return bCodMarca;
	}

	public void setBCodMarca(Integer codMarca) {
		bCodMarca = codMarca;
	}

	public String getBDesMarca() {
		return bDesMarca;
	}

	public void setBDesMarca(String desMarca) {
		bDesMarca = desMarca;
	}

	public Integer getBCodModelo() {
		return bCodModelo;
	}

	public void setBCodModelo(Integer codModelo) {
		bCodModelo = codModelo;
	}

	public String getBDedModelo() {
		return bDesModelo;
	}

	public void setBDedModelo(String desModelo) {
		bDesModelo = desModelo;
	}

	public Integer getBAnio() {
		return bAnio;
	}

	public void setBAnio(Integer anio) {
		bAnio = anio;
	}

	public String getBCodTipoVeh() {
		return bCodTipoVeh;
	}

	public void setBCodTipoVeh(String codTipoVeh) {
		bCodTipoVeh = codTipoVeh;
	}

	public Integer getBCodFab() {
		return bCodFab;
	}

	public void setBCodFab(Integer codFab) {
		bCodFab = codFab;
	}

	public Integer getBCodTipoMotor() {
		return bCodTipoMotor;
	}

	public void setBCodTipoMotor(Integer codTipoMotor) {
		bCodTipoMotor = codTipoMotor;
	}

	public String getBNroMotor() {
		return bNroMotor;
	}

	public void setBNroMotor(String nroMotor) {
		bNroMotor = nroMotor;
	}

	public Integer getBCodUso() {
		return bCodUso;
	}

	public void setBCodUso(Integer codUso) {
		bCodUso = codUso;
	}

	public String getBDesUso() {
		return bDesUso;
	}

	public void setBDesUso(String desUso) {
		bDesUso = desUso;
	}

	public String getBHPoC() {
		return bHPoC;
	}

	public void setBHPoC(String poC) {
		bHPoC = poC;
	}

	public Double getBPesoVacio() {
		return bPesoVacio;
	}

	public void setBPesoVacio(Double pesoVacio) {
		bPesoVacio = pesoVacio;
	}

	public Double getBPrecioAlta() {
		return bPrecioAlta;
	}

	public void setBPrecioAlta(Double precioAlta) {
		bPrecioAlta = precioAlta;
	}

	public Date getBFechaFactura() {
		return bFechaFactura;
	}

	public void setBFechaFactura(Date fechaFactura) {
		bFechaFactura = fechaFactura;
	}

	public Double getBPesoCarga() {
		return bPesoCarga;
	}

	public void setBPesoCarga(Double pesoCarga) {
		bPesoCarga = pesoCarga;
	}

	public String getBCodTipoCarga() {
		return bCodTipoCarga;
	}

	public void setBCodTipoCarga(String codTipoCarga) {
		bCodTipoCarga = codTipoCarga;
	}

	public Double getBCapCarga() {
		return bCapCarga;
	}

	public void setBCapCarga(Double capCarga) {
		bCapCarga = capCarga;
	}

	public String getBUnidadMedida() {
		return bUnidadMedida;
	}

	public void setBUnidadMedida(String unidadMedida) {
		bUnidadMedida = unidadMedida;
	}

	public Integer getBCantidad() {
		return bCantidad;
	}

	public void setBCantidad(Integer cantidad) {
		bCantidad = cantidad;
	}

	public Integer getBAsientos() {
		return bAsientos;
	}

	public void setBAsientos(Integer asientos) {
		bAsientos = asientos;
	}

	public Integer getBCantEjes() {
		return bCantEjes;
	}

	public void setBCantEjes(Integer cantEjes) {
		bCantEjes = cantEjes;
	}

	public Double getBLargoCarr() {
		return bLargoCarr;
	}

	public void setBLargoCarr(Double largoCarr) {
		bLargoCarr = largoCarr;
	}

	public Double getBAltoCarr() {
		return bAltoCarr;
	}

	public void setBAltoCarr(Double altoCarr) {
		bAltoCarr = altoCarr;
	}

	public Integer getCCantDuenios() {
		return cCantDuenios;
	}

	public void setCCantDuenios(Integer cantDuenios) {
		cCantDuenios = cantDuenios;
	}

	public Long getCIdPersonaActual() {
		return cIdPersonaActual;
	}

	public void setCIdPersonaActual(Long idPersonaActual) {
		cIdPersonaActual = idPersonaActual;
	}

	public String getDCodLocalidad() {
		return dCodLocalidad;
	}

	public void setDCodLocalidad(String codLocalidad) {
		dCodLocalidad = codLocalidad;
	}

	public String getDDesLocalidad() {
		return dDesLocalidad;
	}

	public void setDDesLocalidad(String desLocalidad) {
		dDesLocalidad = desLocalidad;
	}

	public Long getDCodCalle() {
		return dCodCalle;
	}

	public void setDCodCalle(Long codCalle) {
		dCodCalle = codCalle;
	}

	public String getDDesCalle() {
		return dDesCalle;
	}

	public void setDDesCalle(String desCalle) {
		dDesCalle = desCalle;
	}

	public Long getDNumero() {
		return dNumero;
	}

	public void setDNumero(Long numero) {
		dNumero = numero;
	}

	public String getDPiso() {
		return dPiso;
	}

	public void setDPiso(String piso) {
		dPiso = piso;
	}

	public String getDDpto() {
		return dDpto;
	}

	public void setDDpto(String dpto) {
		dDpto = dpto;
	}

	public Integer getDBis() {
		return dBis;
	}

	public void setDBis(Integer bis) {
		dBis = bis;
	}

	public Integer getDEsValido() {
		return dEsValido;
	}

	public void setDEsValido(Integer esValido) {
		dEsValido = esValido;
	}

	public String getEDesMarca() {
		return eDesMarca;
	}

	public void setEDesMarca(String desMarca) {
		eDesMarca = desMarca;
	}

	public Integer getECodTipoMotor() {
		return eCodTipoMotor;
	}

	public void setECodTipoMotor(Integer codTipoMotor) {
		eCodTipoMotor = codTipoMotor;
	}

	public String getEDesTipoMotor() {
		return eDesTipoMotor;
	}

	public void setEDesTipoMotor(String desTipoMotor) {
		eDesTipoMotor = desTipoMotor;
	}

	public String getENroMotor() {
		return eNroMotor;
	}

	public void setENroMotor(String nroMotor) {
		eNroMotor = nroMotor;
	}

	public String getFDesMotivoBaja() {
		return fDesMotivoBaja;
	}

	public void setFDesMotivoBaja(String desMotivoBaja) {
		fDesMotivoBaja = desMotivoBaja;
	}

	public String getHPatentePad() {
		return hPatentePad;
	}

	public void setHPatentePad(String patentePad) {
		hPatentePad = patentePad;
	}

	public String getHPatenteCorr() {
		return hPatenteCorr;
	}

	public void setHPatenteCorr(String patenteCorr) {
		hPatenteCorr = patenteCorr;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getObservacionAPI() {
		return observacionAPI;
	}

	public void setObservacionAPI(String observacionAPI) {
		this.observacionAPI = observacionAPI;
	}

	public String getObservacionRNPA() {
		return observacionRNPA;
	}

	public void setObservacionRNPA(String observacionRNPA) {
		this.observacionRNPA = observacionRNPA;
	}

	public Integer getICodPago() {
		return iCodPago;
	}

	public void setICodPago(Integer codPago) {
		iCodPago = codPago;
	}

	public String getIDesBancoMuni() {
		return iDesBancoMuni;
	}

	public void setIDesBancoMuni(String desBancoMuni) {
		iDesBancoMuni = desBancoMuni;
	}

	public Double getIImporte1() {
		return iImporte1;
	}

	public void setIImporte1(Double importe1) {
		iImporte1 = importe1;
	}

	public Date getIFecha1() {
		return iFecha1;
	}

	public void setIFecha1(Date fecha1) {
		iFecha1 = fecha1;
	}

	public Double getIImporte2() {
		return iImporte2;
	}

	public void setIImporte2(Double importe2) {
		iImporte2 = importe2;
	}

	public Date getIFecha2() {
		return iFecha2;
	}

	public void setIFecha2(Date fecha2) {
		iFecha2 = fecha2;
	}

	public Mandatario getMandatario() {
		return mandatario;
	}

	public void setMandatario(Mandatario mandatario) {
		this.mandatario = mandatario;
	}
	
	public String getDesTipoTramite() {
		return desTipoTramite;
	}

	public void setDesTipoTramite(String desTipoTramite) {
		this.desTipoTramite = desTipoTramite;
	}

	public String getBDesModelo() {
		return bDesModelo;
	}

	public void setBDesModelo(String desModelo) {
		bDesModelo = desModelo;
	}

	public EstadoTramiteRA getEstTra() {
		return estTra;
	}

	public void setEstTra(EstadoTramiteRA estTra) {
		this.estTra = estTra;
	}
	
	public String getRubros() {
		return rubros;
	}

	public void setRubros(String rubros) {
		this.rubros = rubros;
	}

	public String getBDesTipoVeh() {
		return bDesTipoVeh;
	}

	public void setBDesTipoVeh(String desTipoVeh) {
		bDesTipoVeh = desTipoVeh;
	}

	public String getBDesTipoCarga() {
		return bDesTipoCarga;
	}

	public void setBDesTipoCarga(String desTipoCarga) {
		bDesTipoCarga = desTipoCarga;
	}

	public String getBDesFab() {
		return bDesFab;
	}

	public void setBDesFab(String desFab) {
		bDesFab = desFab;
	}

	public String getBDesTipoMotor() {
		return bDesTipoMotor;
	}

	public void setBDesTipoMotor(String desTipoMotor) {
		bDesTipoMotor = desTipoMotor;
	}

	public String getIDesPago() {
		return iDesPago;
	}

	public void setIDesPago(String desPago) {
		iDesPago = desPago;
	}

	public String getGDesDomicilio() {
		return gDesDomicilio;
	}

	public void setGDesDomicilio(String desDomicilio) {
		gDesDomicilio = desDomicilio;
	}

	public List<HisEstTra> getListHisEstTra() {
		return listHisEstTra;
	}

	public void setListHisEstTra(List<HisEstTra> listHisEstTra) {
		this.listHisEstTra = listHisEstTra;
	}
	
	public List<Propietario> getListPropietario() {
		return listPropietario;
	}

	public void setListPropietario(List<Propietario> listPropietario) {
		this.listPropietario = listPropietario;
	}

	//	---> ABM HisEstTra
	public HisEstTra createHisEstTra(HisEstTra hisEstTra) throws Exception {

		// Validaciones de negocio
		if (!hisEstTra.validateCreate()) {
			return hisEstTra;
		}
		
		RodDAOFactory.getHisEstTraDAO().update(hisEstTra);

		return hisEstTra;
	}
	
	public HisEstTra updateHisEstTra(HisEstTra hisEstTra) throws Exception {
		
		// Validaciones de negocio
		if (!hisEstTra.validateUpdate()) {
			return hisEstTra;
		}

		RodDAOFactory.getHisEstTraDAO().update(hisEstTra);
		
		return hisEstTra;
	}
	
	public HisEstTra deleteHisEstTra(HisEstTra hisEstTra) throws Exception {
	
		// Validaciones de negocio
		if (!hisEstTra.validateDelete()) {
			return hisEstTra;
		}
		
		RodDAOFactory.getHisEstTraDAO().delete(hisEstTra);
		
		return hisEstTra;
	}
	// <--- ABM HisEstTra

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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							RodError.${BEAN}_LABEL, RodError. BEAN_RELACIONADO _LABEL );
		}*/
		if (GenericDAO.hasReference(this, Propietario.class, "tramiteRA")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				RodError.TRAMITERA_LABEL , RodError.PROPIETARIO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		if (hasError()) {
			return false;
		}
		
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Valida la activacion del TramiteRA
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TramiteRA
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida el cambio de estado
	 * @return boolean
	 */
	private boolean validateCambioEstado(EstadoTramiteRA estadoNuevo) throws Exception {
		//limpiamos la lista de errores
		clearError();

		// Debe haber seleccionado un estado 
		if (estadoNuevo == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RodError.TRAMITERA_LABEL);
		}

		//Validaciones 
		return true;
	}
	
	// ---> ABM HisEstTra
	public HisEstTra createHisEstTra(EstadoTramiteRA estTra, String descripcion) throws Exception {

		HisEstTra hisEstTra = new HisEstTra(estTra, this, descripcion);
		hisEstTra = this.createHisEstTra(hisEstTra);

		return hisEstTra;
	}
	// <--- ABM HisEstTra

	/**
	 * Cambia el estado del TramiteRA
	 *
	 */
	public void cambiarEstado(EstadoTramiteRA estadoNuevo, String observacion) 
		throws Exception {

		if(!this.validateCambioEstado(estadoNuevo)){
			return;
		}
		
		
		// Obtenemos el area actual
		String areaActual = "";
		if(this.getEstTra().getArea()!=null){
			areaActual = this.getEstTra().getArea().getDesArea();
		}

		// Obtenemos el area destino
		String areaDestino = "";
		if(estadoNuevo.getArea()!=null){
			estadoNuevo.getArea().getDesArea();
		}

		// Genereamos la descripcion del evento
		String desEvento = "";
		if (estadoNuevo.getId().equals(EstadoTramiteRA.ID_INGRESADO)) {
			desEvento = SiatUtil.getValueFromBundle("rod.tramiteRA.tramiteRAIngresado"); 
		}else if (estadoNuevo.getId().equals(EstadoTramiteRA.ID_VERFICADO)) {
			desEvento = SiatUtil.getValueFromBundle("rod.tramiteRA.tramiteRAVerificado");
		}else { 
			desEvento = "Se envi\u00F3 desde " + areaActual + " a " + areaDestino;
		}

		//Seteamos el nuevo estado a nivel de tramite
		this.setEstTra(estadoNuevo);
		
		// Actualizamos en la BD
		RodDAOFactory.getTramiteRADAO().update(this);

		// Generamos la descripcion completa
		String descripcion = desEvento + ((!StringUtil.isNullOrEmpty(observacion)) ? (": " + observacion) : "");

		// Guardamos el evento en el Historico
		HisEstTra hisEstTra= this.createHisEstTra(estadoNuevo, descripcion);		
			
			//Copiamos la lista de errores
		this.getListError().addAll(hisEstTra.getListError());
	}
	
	public void establecerTitularPrincipal(TramiteRA tramiteRA ) throws Exception {
		// seteo todos los otros propietario.esTitularPrincipal = NO y los actualizo de la cuenta
		Propietario propietario = Propietario.getById(tramiteRA.getCIdPersonaActual());
		if(!ListUtil.isNullOrEmpty(this.getListPropietario())){
			for (Propietario ct : this.getListPropietario()) {
				if (SiNo.SI.getId().equals(ct.getEsPropPrincipal()) && 
						!ct.getId().equals(propietario.getId())) {
					
					ct.setEsPropPrincipal(SiNo.NO.getId());
					RodDAOFactory.getPropietarioDAO().update(ct);
				}
			}			
		}
		// Seteo la cuenta titular principal		
		propietario.setEsPropPrincipal(SiNo.SI.getId());
		
		// Seteamos los campos nomTitPri, cuitTitPri sacados desde la nueva persona
		RodDAOFactory.getPropietarioDAO().update(propietario);
	}

	
	public Propietario createPropietario(Propietario propietario)throws Exception{
		
		if (!propietario.validateCreate()){
			return propietario;
		}
		
		RodDAOFactory.getPropietarioDAO().update(propietario);
		
		return propietario;
	}
	
	public Propietario updatePropietario(Propietario propietario)throws Exception{
		
		if (!propietario.validateCreate()){
			return propietario;
		}
		
		RodDAOFactory.getPropietarioDAO().update(propietario);
		
		return propietario;
	}
}
