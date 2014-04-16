//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonView;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.Vigencia;

public class CuentaVO extends SiatBussImageModel {
	private static final long serialVersionUID = 0;

	
	private RecursoVO recurso = new RecursoVO();
	
	private ObjImpVO  objImp  = new ObjImpVO();

	private BrocheVO broche = new BrocheVO();
	
	private EstCueVO estCue = new EstCueVO();
	
	// Utilizado para visualizacion y edicion de caso en mantenedor de cuenta.
	private BroCueVO broCue;
	
	private String numeroCuenta = "";
	
	private String codGesCue = "";
	
	private Date fechaAlta;

	private Date fechaBaja;
	
	private DomicilioVO domicilioEnvio = new DomicilioVO();
	
	private List<CuentaTitularVO> listCuentaTitular = new ArrayList<CuentaTitularVO>();
		
	private SiNo cambioTitularIF = SiNo.OpcionNula;

	private SiNo cambioObjImpIF = SiNo.OpcionNula;

	private String fechaAltaView = "";

	private String fechaBajaView = "";
	
	private Boolean liquidacionDeudaBussEnabled = true;
	private Boolean estadoDeudaBussEnabled      = true;
	private Boolean estadoCuentaBussEnabled      = true;
	
	private String nomTitPri="";

	private SiNo esExcluidaEmision = SiNo.OpcionSelecionar;
	
	private SiNo permiteImpresion = SiNo.OpcionSelecionar;
	
	
	private boolean marcada = false;
	
	private CuentaTitularVO cuentaTitularPrincipal = null; // null para evitar el ciclo
	
	private List<RecAtrCueDefinition> listRecAtrCueDefinition = new ArrayList<RecAtrCueDefinition>();
	
	private String nombreTitularPrincipal ="";
	private String cuitTitularPrincipal="";
	
	private String observacion = "";

	private String desDomEnv = "";
	
	private String catDomEnv = "";

	private Boolean relacionarBussEnabled   = true;
	
	// Constructores
	public CuentaVO() {
		super();
	}
	
	public CuentaVO(ObjImpVO objImp, String numeroCuenta) {
		super();
		this.objImp = objImp;
		this.numeroCuenta = numeroCuenta;
	}

	public CuentaVO(long id, String numeroCuenta) {
		super();
		this.setId(id);
		this.numeroCuenta = numeroCuenta;
	}
	
	// Getters y Setters

	public BrocheVO getBroche() {
		return broche;
	}
	public void setBroche(BrocheVO broche) {
		this.broche = broche;
	}
	public ObjImpVO getObjImp() {
		return objImp;
	}
	public void setObjImp(ObjImpVO objImp) {
		this.objImp = objImp;
	}
	public String getCodGesCue() {
		return codGesCue;
	}
	public void setCodGesCue(String codGesCue) {
		this.codGesCue = codGesCue;
	}
	public void setCatDomEnv(String catDomEnv) {
		this.catDomEnv = catDomEnv;
	}

	public String getCatDomEnv() {
		return catDomEnv;
	}

	public String getNumeroCuenta() {
		return numeroCuenta;
	}
	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}
	public String getCatastralyNumeroCuenta(){		
		return this.objImp.getClave() + " - " + getNumeroCuenta();
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	
	public BroCueVO getBroCue() {
		return broCue;
	}
	public void setBroCue(BroCueVO broCue) {
		this.broCue = broCue;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
		this.fechaBajaView = DateUtil.formatDate(fechaBaja, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaAltaView() {
		return fechaAltaView;
	}
	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public String getFechaBajaView() {
		return fechaBajaView;
	}
	public void setFechaBajaView(String fechaBajaView) {
		this.fechaBajaView = fechaBajaView;
	}
	public DomicilioVO getDomicilioEnvio() {
		return domicilioEnvio;
	}
	public void setDomicilioEnvio(DomicilioVO domicilioEnvio) {
		this.domicilioEnvio = domicilioEnvio;
	}
	public SiNo getPermiteImpresion() {
		return permiteImpresion;
	}
	public void setPermiteImpresion(SiNo permiteImpresion) {
		this.permiteImpresion = permiteImpresion;
	}
	public List<CuentaTitularVO> getListCuentaTitular() {
		return listCuentaTitular;
	}
	public void setListCuentaTitular(List<CuentaTitularVO> listCuentaTitular) {
		this.listCuentaTitular = listCuentaTitular;
	}
	public SiNo getCambioObjImpIF() {
		return cambioObjImpIF;
	}
	public void setCambioObjImpIF(SiNo cambioObjImpIF) {
		this.cambioObjImpIF = cambioObjImpIF;
	}
	public SiNo getCambioTitularIF() {
		return cambioTitularIF;
	}
	public void setCambioTitularIF(SiNo cambioTitularIF) {
		this.cambioTitularIF = cambioTitularIF;
	}
	public CuentaTitularVO getCuentaTitularPrincipal() {
		return cuentaTitularPrincipal;
	}
	public void setCuentaTitularPrincipal(CuentaTitularVO cuentaTitularPrincipal) {
		this.cuentaTitularPrincipal = cuentaTitularPrincipal;
	}
	public String getNomTitPri() {
		return nomTitPri;
	}
	public void setNomTitPri(String nomTitPri) {
		this.nomTitPri = nomTitPri;
	}
	
	public SiNo getEsExcluidaEmision() {
		return esExcluidaEmision;
	}

	public void setEsExcluidaEmision(SiNo esExcluidaEmision) {
		this.esExcluidaEmision = esExcluidaEmision;
	}

	public String getActDomicilioEnvio(){
		if (ModelUtil.isNullOrEmpty(this.getDomicilioEnvio())){
			return CommonView.METODO_AGREGAR;
		}else{
			return CommonView.METODO_MODIFICAR;
		}
	}

	public Boolean getEstadoDeudaBussEnabled() {
		return estadoDeudaBussEnabled;
	}
	public void setEstadoDeudaBussEnabled(Boolean estadoDeudaBussEnabled) {
		this.estadoDeudaBussEnabled = estadoDeudaBussEnabled;
	}
	public Boolean getLiquidacionDeudaBussEnabled() {
		return liquidacionDeudaBussEnabled;
	}
	public void setLiquidacionDeudaBussEnabled(Boolean liquidacionDeudaBussEnabled) {
		this.liquidacionDeudaBussEnabled = liquidacionDeudaBussEnabled;
	}
	public boolean getMarcada() {
		return marcada;
	}
	public void setMarcada(boolean marcada) {
		this.marcada = marcada;
	}
	
	public Boolean getEstadoCuentaBussEnabled() {
		return estadoCuentaBussEnabled;
	}

	public void setEstadoCuentaBussEnabled(Boolean estadoCuentaBussEnabled) {
		this.estadoCuentaBussEnabled = estadoCuentaBussEnabled;
	}

	public String getVigenciaForCamDomView(){

		String vigenciaView;
		if (this.getVigencia().equals(Vigencia.VIGENTE)) {
			vigenciaView = Estado.ACTIVO.getValue();
		} else {
			vigenciaView = Estado.INACTIVO.getValue();
		}
		
		return vigenciaView;
	}

	public List<RecAtrCueDefinition> getListRecAtrCueDefinition() {
		return listRecAtrCueDefinition;
	}
	public void setListRecAtrCueDefinition(List<RecAtrCueDefinition> listRecAtrCueDefinition) {
		this.listRecAtrCueDefinition = listRecAtrCueDefinition;
	}

	public String getNombreTitularPrincipal() {
		return nombreTitularPrincipal;
	}

	public void setNombreTitularPrincipal(String nombreTitularPrincipal) {
		this.nombreTitularPrincipal = nombreTitularPrincipal;
	}

	public String getCuitTitularPrincipal() {
		return cuitTitularPrincipal;
	}

	public void setCuitTitularPrincipal(String cuitTitularPrincipal) {
		this.cuitTitularPrincipal = cuitTitularPrincipal;
	}
	
	public String getDesEstadoView(){
		if(this.getEstado().getId().intValue() != Estado.ACTIVO.getId().intValue()){
			return this.getEstado().getValue();
		}else{
			return this.getVigencia().getValue();
		}
	}

	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public EstCueVO getEstCue() {
		return estCue;
	}

	public void setEstCue(EstCueVO estCue) {
		this.estCue = estCue;
	}

	public void setDesDomEnv(String desDomEnv) {
		this.desDomEnv = desDomEnv;
	}

	public String getDesDomEnv() {
		return desDomEnv;
	}
	
	// Buss flags getters y setters
	public Boolean getRelacionarBussEnabled() {
		return relacionarBussEnabled;
	}
	public void setRelacionarBussEnabled(Boolean relacionarBussEnabled) {
		this.relacionarBussEnabled = relacionarBussEnabled;
	}
	public String getRelacionarEnabled() {
		return this.getRelacionarBussEnabled() ? ENABLED : DISABLED;
	}
	
	public String getRecursoCuentaView(){
		String codRecurso="";
		if (!StringUtil.isNullOrEmpty(this.recurso.getCodRecurso()))
			codRecurso=this.recurso.getCodRecurso()+" ";
		return codRecurso + this.getNumeroCuenta();
	}
	
	/**
	 * Formatea el numero de cuenta agregando un guion antes del ultimo caracter. Para el caso de Cuentas de DREI se agrega antes de los dos ultimos caracteres.
	 * 
	 * @return
	 */
	public String getNumeroCuentaConGuion(){
		if(!StringUtil.isNullOrEmpty(this.numeroCuenta)){
			try{
				int cantChar = 1;
				if(RecursoVO.COD_RECURSO_DReI.equals(this.getRecurso().getCodRecurso())){
					cantChar = 2;
				}
				String nroCtaConGuion = this.numeroCuenta.substring(0, this.numeroCuenta.length()-cantChar);
				nroCtaConGuion += "-";
				nroCtaConGuion += this.numeroCuenta.substring(this.numeroCuenta.length()-cantChar);
				return nroCtaConGuion;
			}catch (Exception e) {
				return "";
			}
		}
		return "";
	}
	
}
