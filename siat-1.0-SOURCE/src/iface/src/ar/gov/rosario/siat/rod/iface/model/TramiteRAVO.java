//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.gde.iface.model.MandatarioVO;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.model.LocalidadVO;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del TramiteRA
 * @author tecso
 *
 */
public class TramiteRAVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tramiteRAVO";
	
	private Integer nroComuna;
	
	private Integer nroTramite;
	private Integer codTipoTramite;
	private String desTipoTramite="";
	private String rubros="";
	private TipoTramiteVO tipoTramite = new TipoTramiteVO();
	
	// A
	private String aNroPatente="";
	private Integer aDigVerif;
	
	// B
	private Integer bCodMarca;
	private String bDesMarca="";
	private MarcaVO bMarca = new MarcaVO();
	private Integer bCodModelo;
	private ModeloVO bModelo=new ModeloVO();
	private String bDesModelo="";
	private Integer bAnio;
	private String bCodTipoVeh="";
	private String bDesTipoVeh="";
	private TipoVehiculoVO bTipoVeh = new TipoVehiculoVO();
	private TipoFabricacionVO bTipoFab=new TipoFabricacionVO() ;
	private Integer bCodFab;
	private String bDesFab="";
	private TipoMotorVO bTM=new TipoMotorVO();
	private Integer bCodTipoMotor;
	private String bDesTipoMotor="";
	private String bNroMotor="";
	private Integer bCodUso;
	private String bDesUso="";
	private TipoUsoVO bTipoUso=new TipoUsoVO();
	private String bHPoC="";
	private Double bPesoVacio;
	private Double bPrecioAlta;
	private Date bFechaFactura;
	private Double bPesoCarga;
	private TipoCargaVO bTipoCarga=new TipoCargaVO();
	private String bCodTipoCarga="";
	private String bDesTipoCarga="";
	private Double bCapCarga;
	private String bUnidadMedida="";
	private Integer bCantidad;
	private Integer bAsientos;
	private Integer bCantEjes;
	private Double bLargoCarr;
	private Double bAltoCarr;

	// C
	private String cApellidoORazon="";
	private Integer cCodTipoDoc;
	private String cDesTipoDoc="";
	private Integer cTD;
	private TipoDocVO cTipoDoc=new TipoDocVO();
	private Long cNroDoc;
	private String cNroCuit="";
	private Integer cCantDuenios=0;
	private Integer cNroIB;
	private Integer cNroProdAgr;
	private Integer cCodTipoProp;
	private String cDesTipoProp="";
	private TipoPropietarioVO cTipoPropietario=new TipoPropietarioVO();
	private Date cFechaNac;
	private Integer cCodEstCiv;
	private String cDesEstCiv="";
	private Integer cCodSexo;
	private String cDesSexo="";
	private EstadoCivilVO cEstCiv=new EstadoCivilVO();
	private PersonaVO cPersonaActual=new PersonaVO();
	
    private List<PropietarioVO> listPropietario = new ArrayList<PropietarioVO>();
    private Boolean marcarPrincipalBussEnabled = Boolean.TRUE;
    
	
	// D
	private String dCodLocalidad="";
	private String dDesLocalidad="";
	private LocalidadVO dLocalidad=new LocalidadVO();
	private Long dCodCalle;
	private String dDesCalle="";
	private Long dNumero;
	private String dPiso="";
	private String dDpto="";
	private Integer dBis;
	private DomicilioVO dDomicilio=new DomicilioVO();
	private Integer dEsValido;
	
	// E
	private String eDesMarca="";
	private Integer eCodTipoMotor;
	private String eDesTipoMotor="";
	private String eNroMotor="";
	private TipoMotorVO eTipoMotor=new TipoMotorVO();
	
	// F
	private String fDesMotivoBaja="";
	
	// G
	private String gNroCuit="";
	private String gApellidoORazon="";
	private String gDesTipoDoc="";
	private Long gNroDoc;
	private String gDesDomicilio="";
	private TipoDocVO gTipoDoc=new TipoDocVO();
	private PersonaVO gPropAnterior=new PersonaVO();
	
	// H
	private String hPatentePad="";
	private String hPatenteCorr="";
	
	//
	private Date fecha;
	private String observacion="";
	private String observacionAPI="";
	private String observacionRNPA="";
	
	// I
	private Integer iCodPago;
	private String iDesPago="";
	private String iDesBancoMuni="";
	private Double iImporte1;
	private Date iFecha1;
	private Double iImporte2;
	private Date iFecha2;
	private MandatarioVO mandatario=new MandatarioVO();
	private TipoPagoVO iTipoPago=new TipoPagoVO();

	// Buss Flags
	
	
	// View Constants
	private String nroComunaView="";
	private String nroTramiteView="";
	private String codTipoTramiteView="";
	
	//A
	private String aDigVerifView="";
	
	//B
	private String bAnioView="";
	private String bPesoVacioView="";
	private String bPrecioAltaView="";
	private String bFechaFacturaView="";
	private String bPesoCargaView="";
	private String bCapCargaView="";
	private String bCantidadView="";
	private String bAsientosView="";
	private String bCantEjesView="";
	private String bLargoCarrView="";
	private String bAltoCarrView="";
	private String bCodFabView="";
	private String bCodTipoMotorView="";
	private String bCodModeloView="";
	private String bCodMarcaView="";
	
	//C
	private String cCodTipoDocView="";
	private String cNroDocView="";
	private String cCantDueniosView="";
	private String cNroIBView="";
	private String cNroProdAgrView="";
	private String cCodTipoPropView="";
	private String cFechaNacView="";
	private String cCodEstCivView="";
	
	//D
	private String dCodCalleView="";
	private String dNumeroView="";
	private String dPisoView="";
	private String dBisView="";
	
	//E
	private String eCodTipoMotorView="";
	
	//G
	private String gNroDocView="";
	
	private String fechaView="";
	//I
	private String iCodPagoView="";
	private String iImporte1View="";
	private String iFecha1View="";
	private String iImporte2View="";
	private String iFecha2View="";
	
	// cambio de estado
	private EstadoTramiteRAVO estadoTramiteRA = new EstadoTramiteRAVO(); 
	private boolean cambiarEstadoBussEnabled = true;
	private List<HisEstTraVO> listHisEstTra = new ArrayList<HisEstTraVO>();
	private String logCambios = "";

	// Constructores
	public TramiteRAVO() {
		super();
	}

	// Getters y Setters

	public String getANroPatente() {
		return aNroPatente;
	}

	public void setANroPatente(String nroPatente) {
		aNroPatente = nroPatente;
	}

	public Integer getNroComuna() {
		return nroComuna;
	}

	public void setNroComuna(Integer nroComuna) {
		this.nroComuna = nroComuna;
		this.nroComunaView = StringUtil.formatInteger(nroComuna);
	}

	public Integer getNroTramite() {
		return nroTramite;
	}

	public void setNroTramite(Integer nroTramite) {
		this.nroTramite = nroTramite;
		this.nroTramiteView = StringUtil.formatInteger(nroTramite);
	}

	public Integer getCodTipoTramite() {
		return codTipoTramite;
	}

	public void setCodTipoTramite(Integer codTipoTramite) {
		this.codTipoTramite = codTipoTramite;
		this.codTipoTramiteView = StringUtil.formatInteger(codTipoTramite);
	}

	public Integer getADigVerif() {
		return aDigVerif;
	}

	public void setADigVerif(Integer digVerif) {
		aDigVerif = digVerif;
		this.aDigVerifView = StringUtil.formatInteger(digVerif);
	}

	public Integer getBCodMarca() {
		return bCodMarca;
	}

	public void setBCodMarca(Integer codMarca) {
		bCodMarca = codMarca;
		this.bCodMarcaView = StringUtil.formatInteger(codMarca);
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
		this.bCodModeloView = StringUtil.formatInteger(codModelo);
	}

	public String getBDesModelo() {
		return bDesModelo;
	}

	public void setBDesModelo(String desModelo) {
		bDesModelo = desModelo;
	}

	public Integer getBAnio() {
		return bAnio;
	}

	public void setBAnio(Integer anio) {
		bAnio = anio;
		this.bAnioView = StringUtil.formatInteger(anio);
	}

	public Integer getBCodFab() {
		return bCodFab;
	}

	public void setBCodFab(Integer codFab) {
		bCodFab = codFab;
		this.bCodFabView = StringUtil.formatInteger(codFab);
	}

	public TipoMotorVO getBTM() {
		return bTM;
	}

	public void setBTM(TipoMotorVO TM) {
		bTM = TM;
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
		this.bPesoVacioView = StringUtil.formatDouble(pesoVacio);
	}

	public Double getBPrecioAlta() {
		return bPrecioAlta;
	}

	public void setBPrecioAlta(Double precioAlta) {
		bPrecioAlta = precioAlta;
		this.bPrecioAltaView = StringUtil.formatDouble(precioAlta);
	}

	public Date getBFechaFactura() {
		return bFechaFactura;
	}

	public void setBFechaFactura(Date fechaFactura) {
		bFechaFactura = fechaFactura;
		this.bFechaFacturaView = DateUtil.formatDate(fechaFactura, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getBPesoCarga() {
		return bPesoCarga;
	}

	public void setBPesoCarga(Double pesoCarga) {
		bPesoCarga = pesoCarga;
		this.bPesoCargaView = StringUtil.formatDouble(pesoCarga);
	}

	public TipoCargaVO getBTipoCarga() {
		return bTipoCarga;
	}

	public void setBTipoCarga(TipoCargaVO tipoCarga) {
		bTipoCarga = tipoCarga;
	}

	public Double getBCapCarga() {
		return bCapCarga;
	}

	public void setBCapCarga(Double capCarga) {
		bCapCarga = capCarga;
		this.bCapCargaView = StringUtil.formatDouble(capCarga);
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
		this.bCantidadView = StringUtil.formatInteger(cantidad);
	}

	public Integer getBAsientos() {
		return bAsientos;
	}

	public void setBAsientos(Integer asientos) {
		bAsientos = asientos;
		this.bAsientosView = StringUtil.formatInteger(asientos);
	}

	public Integer getBCantEjes() {
		return bCantEjes;
	}

	public void setBCantEjes(Integer cantEjes) {
		bCantEjes = cantEjes;
		this.bCantEjesView = StringUtil.formatInteger(cantEjes);
	}

	public Double getBLargoCarr() {
		return bLargoCarr;
	}

	public void setBLargoCarr(Double largoCarr) {
		bLargoCarr = largoCarr;
		this.bLargoCarrView = StringUtil.formatDouble(largoCarr);
	}

	public Double getBAltoCarr() {
		return bAltoCarr;
	}

	public void setBAltoCarr(Double altoCarr) {
		bAltoCarr = altoCarr;
		this.bAltoCarrView = StringUtil.formatDouble(altoCarr);
	}

	public String getCApellidoORazon() {
		return cApellidoORazon;
	}

	public void setCApellidoORazon(String apellidoORazon) {
		cApellidoORazon = apellidoORazon;
	}

	public Integer getCCodTipoDoc() {
		return cCodTipoDoc;
	}

	public void setCCodTipoDoc(Integer codTipoDoc) {
		cCodTipoDoc = codTipoDoc;
	}

	public String getCDesTipoDoc() {
		return cDesTipoDoc;
	}

	public void setCDesTipoDoc(String desTipoDoc) {
		cDesTipoDoc = desTipoDoc;
	}

	public TipoDocVO getCTipoDoc() {
		return cTipoDoc;
	}

	public void setCTipoDoc(TipoDocVO tipoDoc) {
		cTipoDoc = tipoDoc;
	}

	public Long getCNroDoc() {
		return cNroDoc;
	}

	public void setCNroDoc(Long nroDoc) {
		cNroDoc = nroDoc;
		this.cNroDocView = StringUtil.formatLong(nroDoc);
	}

	public String getCNroCuit() {
		return cNroCuit;
	}

	public void setCNroCuit(String nroCuit) {
		cNroCuit = nroCuit;
	}

	public Integer getCCantDuenios() {
		return cCantDuenios;
	}

	public void setCCantDuenios(Integer cantDuenios) {
		cCantDuenios = cantDuenios;
		this.cCantDueniosView = StringUtil.formatInteger(cantDuenios);
	}

	public Integer getCNroIB() {
		return cNroIB;
	}

	public void setCNroIB(Integer nroIB) {
		cNroIB = nroIB;
		this.cNroIBView = StringUtil.formatInteger(nroIB);
	}

	public Integer getCNroProdAgr() {
		return cNroProdAgr;
	}

	public void setCNroProdAgr(Integer nroProdAgr) {
		cNroProdAgr = nroProdAgr;
		this.cNroProdAgrView = StringUtil.formatInteger(nroProdAgr);
	}

	public Integer getCCodTipoProp() {
		return cCodTipoProp;
	}

	public void setCCodTipoProp(Integer codTipoProp) {
		cCodTipoProp = codTipoProp;
	}

	public String getCDesTipoProp() {
		return cDesTipoProp;
	}

	public void setCDesTipoProp(String desTipoProp) {
		cDesTipoProp = desTipoProp;
	}

	public Date getCFechaNac() {
		return cFechaNac;
	}

	public void setCFechaNac(Date fechaNac) {
		cFechaNac = fechaNac;
		this.cFechaNacView = DateUtil.formatDate(fechaNac, DateUtil.ddSMMSYYYY_MASK);
	}

	public Integer getCCodEstCiv() {
		return cCodEstCiv;
	}

	public void setCCodEstCiv(Integer codEstCiv) {
		cCodEstCiv = codEstCiv;
	}

	public EstadoCivilVO getCEstCiv() {
		return cEstCiv;
	}

	public void setCEstCiv(EstadoCivilVO estCiv) {
		cEstCiv = estCiv;
	}

	public PersonaVO getCPersonaActual() {
		return cPersonaActual;
	}

	public void setCPersonaActual(PersonaVO personaActual) {
		cPersonaActual = personaActual;
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
		this.dCodCalleView = StringUtil.formatLong(codCalle);
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
		this.dNumeroView = StringUtil.formatLong(numero);
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
		if (bis.equals(SiNo.SI.getId())) {
			this.dBisView = "Si";
		} else {
			this.dBisView = "No";			
		}
	}

	public DomicilioVO getDDomicilio() {
		return dDomicilio;
	}

	public void setDDomicilio(DomicilioVO domicilio) {
		dDomicilio = domicilio;
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
		this.eCodTipoMotorView = StringUtil.formatInteger(codTipoMotor);
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

	public String getGNroCuit() {
		return gNroCuit;
	}

	public void setGNroCuit(String nroCuit) {
		gNroCuit = nroCuit;
	}

	public String getGApellidoORazon() {
		return gApellidoORazon;
	}

	public void setGApellidoORazon(String apellidoORazon) {
		gApellidoORazon = apellidoORazon;
	}

	public String getGDesTipoDoc() {
		return gDesTipoDoc;
	}

	public void setGDesTipoDoc(String desTipoDoc) {
		gDesTipoDoc = desTipoDoc;
	}

	public Long getGNroDoc() {
		return gNroDoc;
	}

	public void setGNroDoc(Long nroDoc) {
		gNroDoc = nroDoc;
		this.gNroDocView = StringUtil.formatLong(nroDoc);
	}

	public String getGDesDomicilio() {
		return gDesDomicilio;
	}

	public void setGDesDomicilio(String desDomicilio) {
		gDesDomicilio = desDomicilio;
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
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
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
		this.iImporte1View = StringUtil.formatDouble(importe1);
	}

	public Date getIFecha1() {
		return iFecha1;
	}

	public void setIFecha1(Date fecha1) {
		iFecha1 = fecha1;
		this.iFecha1View = DateUtil.formatDate(fecha1, DateUtil.ddSMMSYYYY_MASK);
	}

	public Double getIImporte2() {
		return iImporte2;
	}

	public void setIImporte2(Double importe2) {
		iImporte2 = importe2;
		this.iImporte2View = StringUtil.formatDouble(importe2);
	}

	public Date getIFecha2() {
		return iFecha2;
	}

	public void setIFecha2(Date fecha2) {
		iFecha2 = fecha2;
		this.iFecha2View = DateUtil.formatDate(fecha2, DateUtil.ddSMMSYYYY_MASK);
	}

	public MandatarioVO getMandatario() {
		return mandatario;
	}

	public void setMandatario(MandatarioVO mandatario) {
		this.mandatario = mandatario;
	}
	
	public TipoMotorVO getETipoMotor() {
		return eTipoMotor;
	}

	public void setETipoMotor(TipoMotorVO tipoMotor) {
		eTipoMotor = tipoMotor;
	}

	public TipoDocVO getGTipoDoc() {
		return gTipoDoc;
	}

	public void setGTipoDoc(TipoDocVO tipoDoc) {
		gTipoDoc = tipoDoc;
	}	
	
	public TipoPagoVO getITipoPago() {
		return iTipoPago;
	}

	public void setITipoPago(TipoPagoVO tipoPago) {
		iTipoPago = tipoPago;
	}

	public String getNroComunaView() {
		return nroComunaView;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	
	
	
	public void setNroComunaView(String nroComunaView) {
		this.nroComunaView = nroComunaView;
	}

	public String getNroTramiteView() {
		return nroTramiteView;
	}

	public void setNroTramiteView(String nroTramiteView) {
		this.nroTramiteView = nroTramiteView;
	}


	public String getADigVerifView() {
		return aDigVerifView;
	}

	public void setADigVerifView(String digVerifView) {
		aDigVerifView = digVerifView;
	}

	public String getBAnioView() {
		return bAnioView;
	}

	public void setBAnioView(String anioView) {
		bAnioView = anioView;
	}

	public String getBPesoVacioView() {
		return bPesoVacioView;
	}

	public void setBPesoVacioView(String pesoVacioView) {
		bPesoVacioView = pesoVacioView;
	}

	public String getBPrecioAltaView() {
		return bPrecioAltaView;
	}

	public void setBPrecioAltaView(String precioAltaView) {
		bPrecioAltaView = precioAltaView;
	}

	public String getBFechaFacturaView() {
		return bFechaFacturaView;
	}

	public void setBFechaFacturaView(String fechaFacturaView) {
		bFechaFacturaView = fechaFacturaView;
	}

	public String getBPesoCargaView() {
		return bPesoCargaView;
	}

	public void setBPesoCargaView(String pesoCargaView) {
		bPesoCargaView = pesoCargaView;
	}

	public String getBCapCargaView() {
		return bCapCargaView;
	}

	public void setBCapCargaView(String capCargaView) {
		bCapCargaView = capCargaView;
	}

	public String getBCantidadView() {
		return bCantidadView;
	}

	public void setBCantidadView(String cantidadView) {
		bCantidadView = cantidadView;
	}

	public String getBAsientosView() {
		return bAsientosView;
	}

	public void setBAsientosView(String asientosView) {
		bAsientosView = asientosView;
	}

	public String getBCantEjesView() {
		return bCantEjesView;
	}

	public void setBCantEjesView(String cantEjesView) {
		bCantEjesView = cantEjesView;
	}

	public String getBLargoCarrView() {
		return bLargoCarrView;
	}

	public void setBLargoCarrView(String largoCarrView) {
		bLargoCarrView = largoCarrView;
	}

	public String getBAltoCarrView() {
		return bAltoCarrView;
	}

	public void setBAltoCarrView(String altoCarrView) {
		bAltoCarrView = altoCarrView;
	}

	public String getCCodTipoDocView() {
		return cCodTipoDocView;
	}

	public void setCCodTipoDocView(String codTipoDocView) {
		cCodTipoDocView = codTipoDocView;
	}

	public String getCNroDocView() {
		return cNroDocView;
	}

	public void setCNroDocView(String nroDocView) {
		cNroDocView = nroDocView;
	}

	public String getCCantDueniosView() {
		return cCantDueniosView;
	}

	public void setCCantDueniosView(String cantDueniosView) {
		cCantDueniosView = cantDueniosView;
	}

	public String getCNroIBView() {
		return cNroIBView;
	}

	public void setCNroIBView(String nroIBView) {
		cNroIBView = nroIBView;
	}

	public String getCNroProdAgrView() {
		return cNroProdAgrView;
	}

	public void setCNroProdAgrView(String nroProdAgrView) {
		cNroProdAgrView = nroProdAgrView;
	}

	public String getCCodTipoPropView() {
		return cCodTipoPropView;
	}

	public void setCCodTipoPropView(String codTipoPropView) {
		cCodTipoPropView = codTipoPropView;
	}

	public String getCFechaNacView() {
		return cFechaNacView;
	}

	public void setCFechaNacView(String fechaNacView) {
		cFechaNacView = fechaNacView;
	}

	public String getCCodEstCivView() {
		return cCodEstCivView;
	}

	public void setCCodEstCivView(String codEstCivView) {
		cCodEstCivView = codEstCivView;
	}

	public String getDCodCalleView() {
		return dCodCalleView;
	}

	public void setDCodCalleView(String codCalleView) {
		dCodCalleView = codCalleView;
	}

	public String getDNumeroView() {
		return dNumeroView;
	}

	public void setDNumeroView(String numeroView) {
		dNumeroView = numeroView;
	}

	public String getDPisoView() {
		return dPisoView;
	}

	public void setDPisoView(String pisoView) {
		dPisoView = pisoView;
	}

	public String getDBisView() {
		return dBisView;
	}

	public void setDBisView(String bisView) {
		dBisView = bisView;
	}

	public String getECodTipoMotorView() {
		return eCodTipoMotorView;
	}

	public void setECodTipoMotorView(String codTipoMotorView) {
		eCodTipoMotorView = codTipoMotorView;
	}

	public String getGNroDocView() {
		return gNroDocView;
	}

	public void setGNroDocView(String nroDocView) {
		gNroDocView = nroDocView;
	}

	public String getFechaView() {
		return fechaView;
	}

	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}

	public String getICodPagoView() {
		return iCodPagoView;
	}

	public void setICodPagoView(String codPagoView) {
		iCodPagoView = codPagoView;
	}

	public String getIImporte1View() {
		return iImporte1View;
	}

	public void setIImporte1View(String importe1View) {
		iImporte1View = importe1View;
	}

	public String getIFecha1View() {
		return iFecha1View;
	}

	public void setIFecha1View(String fecha1View) {
		iFecha1View = fecha1View;
	}

	public String getIImporte2View() {
		return iImporte2View;
	}

	public void setIImporte2View(String importe2View) {
		iImporte2View = importe2View;
	}

	public String getIFecha2View() {
		return iFecha2View;
	}

	public void setIFecha2View(String fecha2View) {
		iFecha2View = fecha2View;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public TipoTramiteVO getTipoTramite() {
		return tipoTramite;
	}

	public void setTipoTramite(TipoTramiteVO tipoTramite) {
		this.tipoTramite = tipoTramite;
	}

	public ModeloVO getBModelo() {
		return bModelo;
	}

	public void setBModelo(ModeloVO modelo) {
		bModelo = modelo;
	}

	public LocalidadVO getDLocalidad() {
		return dLocalidad;
	}

	public void setDLocalidad(LocalidadVO dLocalidad) {
		this.dLocalidad = dLocalidad;
	}

	public TipoUsoVO getBTipoUso() {
		return bTipoUso;
	}

	public void setBTipoUso(TipoUsoVO tipoUso) {
		bTipoUso = tipoUso;
	}

	public TipoVehiculoVO getBTipoVeh() {
		return bTipoVeh;
	}

	public void setBTipoVeh(TipoVehiculoVO tipoVeh) {
		bTipoVeh = tipoVeh;
	}

	public Integer getCTD() {
		return cTD;
	}

	public void setCTD(Integer ctd) {
		cTD = ctd;
	}

	public MarcaVO getBMarca() {
		return bMarca;
	}

	public void setBMarca(MarcaVO marca) {
		bMarca = marca;
	}

	public Integer getCCodSexo() {
		return cCodSexo;
	}

	public void setCCodSexo(Integer codSexo) {
		cCodSexo = codSexo;
	}

	public PersonaVO getGPropAnterior() {
		return gPropAnterior;
	}

	public void setGPropAnterior(PersonaVO propAnterior) {
		gPropAnterior = propAnterior;
	}

	public TipoFabricacionVO getBTipoFab() {
		return bTipoFab;
	}

	public void setBTipoFab(TipoFabricacionVO tipoFab) {
		bTipoFab = tipoFab;
	}

	public String getBCodTipoCarga() {
		return bCodTipoCarga;
	}

	public void setBCodTipoCarga(String codTipoCarga) {
		bCodTipoCarga = codTipoCarga;
	}

	public Integer getBCodTipoMotor() {
		return bCodTipoMotor;
	}

	public void setBCodTipoMotor(Integer codTipoMotor) {
		bCodTipoMotor = codTipoMotor;
		this.bCodTipoMotorView = StringUtil.formatInteger(codTipoMotor);
		
	}

	public String getCDesEstCiv() {
		return cDesEstCiv;
	}

	public void setCDesEstCiv(String desEstCiv) {
		cDesEstCiv = desEstCiv;
	}

	public String getDesTipoTramite() {
		return desTipoTramite;
	}

	public void setDesTipoTramite(String desTipoTramite) {
		this.desTipoTramite = desTipoTramite;
	}
	public TipoPropietarioVO getCTipoPropietario() {
		return cTipoPropietario;
	}

	public void setCTipoPropietario(TipoPropietarioVO cTipoPropietario) {
		this.cTipoPropietario = cTipoPropietario;
	}

	public EstadoTramiteRAVO getEstadoTramiteRA() {
		return estadoTramiteRA;
	}

	public void setEstadoTramiteRA(EstadoTramiteRAVO estadoTramiteRA) {
		this.estadoTramiteRA = estadoTramiteRA;
	}

	public boolean isCambiarEstadoBussEnabled() {
		return cambiarEstadoBussEnabled;
	}

	public void setCambiarEstadoBussEnabled(boolean cambiarEstadoBussEnabled) {
		this.cambiarEstadoBussEnabled = cambiarEstadoBussEnabled;
	}

	public String getBCodFabView() {
		return bCodFabView;
	}

	public void setBCodFabView(String codFabView) {
		bCodFabView = codFabView;
	}

	public String getBCodTipoMotorView() {
		return bCodTipoMotorView;
	}

	public void setBCodTipoMotorView(String codTipoMotorView) {
		bCodTipoMotorView = codTipoMotorView;
	}

	public String getBCodModeloView() {
		return bCodModeloView;
	}

	public void setBCodModeloView(String codModeloView) {
		bCodModeloView = codModeloView;
	}

	public String getBCodMarcaView() {
		return bCodMarcaView;
	}

	public void setBCodMarcaView(String codMarcaView) {
		bCodMarcaView = codMarcaView;
	}
	
	public String getDesCodTipoTramite(){
		return getCodTipoTramiteView()+" - "+getDesTipoTramite();
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

	public String getCDesSexo() {
		return cDesSexo;
	}

	public void setCDesSexo(String desSexo) {
		cDesSexo = desSexo;
	}

	public String getCodTipoTramiteView() {
		return codTipoTramiteView;
	}

	public void setCodTipoTramiteView(String codTipoTramiteView) {
		this.codTipoTramiteView = codTipoTramiteView;
	}

	public String getBCodTipoVeh() {
		return bCodTipoVeh;
	}

	public void setBCodTipoVeh(String codTipoVeh) {
		bCodTipoVeh = codTipoVeh;
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

	public Integer getDEsValido() {
		return dEsValido;
	}

	public void setDEsValido(Integer esValido) {
		dEsValido = esValido;
	}

	public List<HisEstTraVO> getListHisEstTra() {
		return listHisEstTra;
	}

	public void setListHisEstTra(List<HisEstTraVO> listHisEstTra) {
		this.listHisEstTra = listHisEstTra;
	}

	public List<PropietarioVO> getListPropietario() {
		return listPropietario;
	}

	public void setListPropietario(List<PropietarioVO> listPropietario) {
		this.listPropietario = listPropietario;
	}

	public Boolean getMarcarPrincipalBussEnabled() {
		return marcarPrincipalBussEnabled;
	}

	public void setMarcarPrincipalBussEnabled(Boolean marcarPrincipalBussEnabled) {
		this.marcarPrincipalBussEnabled = marcarPrincipalBussEnabled;
	}

	public String getLogCambios() {
		return logCambios;
	}

	public void setLogCambios(String logCambios) {
		this.logCambios = logCambios;
	}
	
	
}
