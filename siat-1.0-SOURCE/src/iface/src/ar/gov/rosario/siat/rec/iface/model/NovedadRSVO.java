//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del Novedad de regimen simplificado
 * @author tecso
 *
 */
public class NovedadRSVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "novedadRSVO";
	
	public static Integer REGISTRADO = -1;
	public static Integer PROCESADO_OK = 1;
	public static Integer PROCESADO_ERROR = 2;
	public static Integer REGISTRADO_NO_PROCESAR= 3;
	
	private Integer tipoUsuario; 
	private TipoTramiteRSVO tipoTramiteRS = new TipoTramiteRSVO();
	private CatRSDreiVO catRSDRei = new CatRSDreiVO();
	private CuentaVO cuentaDRei;
	private CuentaVO cuentaEtur;
	private Date fechaTransaccion;
	private String tipoTransaccion; 
	private String usrCliente; 
	private String cuit; 
	private String desCont; 
	private String domLocal; 
	private String telefono;
	private String email;
	private Integer tipoCont;
	private String isib;
	private String nroCuenta; 
	private String listActividades;
	private Integer mesInicio; 
	private Integer anioInicio;
	private Double precioUnitario;
	private Integer canPer;
	private Double ingBruAnu;
	private Double supAfe;
	private Double publicidad;
	private Double redHabSoc;
	private Integer adicEtur; 
	private Integer nroCategoria;
	private Double importeDrei;
	private Double importeEtur;
	private Double importeAdicional;
	private String desCategoria;
	private String desPublicidad;
	private String desEtur;
	private String cuna;
	private String codBar;
	private String codBarComprimido;
	private Integer mesBaja;
	private Integer anioBaja;
	private Integer motivoBaja;
	private String desBaja;
	private String msgDeuda;
	
	// View
	private String tipoUsuarioView; 
	private String fechaTransaccionView;
	private String tipoContView;
	private String mesInicioView; 
	private String anioInicioView;
	private String precioUnitarioView;
	private String canPerView;
	private String ingBruAnuView;
	private String supAfeView;
	private String publicidadView;
	private String redHabSocView;
	private String adicEturView; 
	private String nroCategoriaView;
	private String importeDreiView;
	private String importeEturView;
	private String importeAdicionalView;
	private String mesBajaView;
	private String anioBajaView;
	private String motivoBajaView;
	
	// Buss Flags
	private Boolean aplicarBussEnabled      = true;
	
	//Constructores
	public NovedadRSVO() {
		super();
	}
	public Integer getAdicEtur() {
		return adicEtur;
	}
	public void setAdicEtur(Integer adicEtur) {
		this.adicEtur = adicEtur;
		this.adicEturView = StringUtil.formatInteger(adicEtur);
	}
	public Integer getAnioBaja() {
		return anioBaja;
	}
	public void setAnioBaja(Integer anioBaja) {
		this.anioBaja = anioBaja;
		this.anioBajaView = StringUtil.formatInteger(anioBaja);
	}
	public Integer getAnioInicio() {
		return anioInicio;
	}
	public void setAnioInicio(Integer anioInicio) {
		this.anioInicio = anioInicio;
		this.anioInicioView = StringUtil.formatInteger(anioInicio);
	}
	public Integer getCanPer() {
		return canPer;
	}
	public void setCanPer(Integer canPer) {
		this.canPer = canPer;
		this.canPerView = StringUtil.formatInteger(canPer);
	}
	public CatRSDreiVO getCatRSDRei() {
		return catRSDRei;
	}
	public void setCatRSDRei(CatRSDreiVO catRSDRei) {
		this.catRSDRei = catRSDRei;
	}
	public String getCodBar() {
		return codBar;
	}
	public void setCodBar(String codBar) {
		this.codBar = codBar;
	}
	public CuentaVO getCuentaDRei() {
		return cuentaDRei;
	}
	public void setCuentaDRei(CuentaVO cuentaDRei) {
		this.cuentaDRei = cuentaDRei;
	}
	public CuentaVO getCuentaEtur() {
		return cuentaEtur;
	}
	public void setCuentaEtur(CuentaVO cuentaEtur) {
		this.cuentaEtur = cuentaEtur;
	}
	public String getCuit() {
		return cuit;
	}
	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
	public String getCuna() {
		return cuna;
	}
	public void setCuna(String cuna) {
		this.cuna = cuna;
	}
	public String getDesBaja() {
		return desBaja;
	}
	public void setDesBaja(String desBaja) {
		this.desBaja = desBaja;
	}
	public String getDesCategoria() {
		return desCategoria;
	}
	public void setDesCategoria(String desCategoria) {
		this.desCategoria = desCategoria;
	}
	public String getDesCont() {
		return desCont;
	}
	public void setDesCont(String desCont) {
		this.desCont = desCont;
	}
	public String getDesEtur() {
		return desEtur;
	}
	public void setDesEtur(String desEtur) {
		this.desEtur = desEtur;
	}
	public String getDesPublicidad() {
		return desPublicidad;
	}
	public void setDesPublicidad(String desPublicidad) {
		this.desPublicidad = desPublicidad;
	}
	public String getDomLocal() {
		return domLocal;
	}
	public void setDomLocal(String domLocal) {
		this.domLocal = domLocal;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getFechaTransaccion() {
		return fechaTransaccion;
	}
	public void setFechaTransaccion(Date fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
		this.fechaTransaccionView = DateUtil.formatDate(fechaTransaccion, DateUtil.ddSMMSYYYY_MASK);
	}
	public Double getImporteAdicional() {
		return importeAdicional;
	}
	public void setImporteAdicional(Double importeAdicional) {
		this.importeAdicional = importeAdicional;
		this.importeAdicionalView = StringUtil.formatDouble(importeAdicional);
	}
	public Double getImporteDrei() {
		return importeDrei;
	}
	public void setImporteDrei(Double importeDrei) {
		this.importeDrei = importeDrei;
		this.importeDreiView = StringUtil.formatDouble(importeDrei);
	}
	public Double getImporteEtur() {
		return importeEtur;
	}
	public void setImporteEtur(Double importeEtur) {
		this.importeEtur = importeEtur;
		this.importeEturView = StringUtil.formatDouble(importeEtur);
	}
	public Double getIngBruAnu() {
		return ingBruAnu;
	}
	public void setIngBruAnu(Double ingBruAnu) {
		this.ingBruAnu = ingBruAnu;
		this.ingBruAnuView = StringUtil.formatDouble(ingBruAnu);
	}
	public String getIsib() {
		return isib;
	}
	public void setIsib(String isib) {
		this.isib = isib;
	}
	public String getListActividades() {
		return listActividades;
	}
	public void setListActividades(String listActividades) {
		this.listActividades = listActividades;
	}
	public Integer getMesBaja() {
		return mesBaja;
	}
	public void setMesBaja(Integer mesBaja) {
		this.mesBaja = mesBaja;
		this.mesBajaView = StringUtil.formatInteger(mesBaja);
	}
	public Integer getMesInicio() {
		return mesInicio;
	}
	public void setMesInicio(Integer mesInicio) {
		this.mesInicio = mesInicio;
		this.mesInicioView = StringUtil.formatInteger(mesInicio);
	}
	public Integer getMotivoBaja() {
		return motivoBaja;
	}
	public void setMotivoBaja(Integer motivoBaja) {
		this.motivoBaja = motivoBaja;
		this.motivoBajaView = StringUtil.formatInteger(motivoBaja);
	}
	public Integer getNroCategoria() {
		return nroCategoria;
	}
	public void setNroCategoria(Integer nroCategoria) {
		this.nroCategoria = nroCategoria;
		this.nroCategoriaView = StringUtil.formatInteger(nroCategoria);
	}
	public String getNroCuenta() {
		return nroCuenta;
	}
	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}
	public Double getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
		this.precioUnitarioView = StringUtil.formatDouble(precioUnitario);
	}
	public Double getPublicidad() {
		return publicidad;
	}
	public void setPublicidad(Double publicidad) {
		this.publicidad = publicidad;
		this.publicidadView = StringUtil.formatDouble(publicidad);
	}
	public Double getRedHabSoc() {
		return redHabSoc;
	}
	public void setRedHabSoc(Double redHabSoc) {
		this.redHabSoc = redHabSoc;
		this.redHabSocView = StringUtil.formatDouble(redHabSoc);
	}
	public Double getSupAfe() {
		return supAfe;
	}
	public void setSupAfe(Double supAfe) {
		this.supAfe = supAfe;
		this.supAfeView = StringUtil.formatDouble(supAfe);
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public Integer getTipoCont() {
		return tipoCont;
	}
	public void setTipoCont(Integer tipoCont) {
		this.tipoCont = tipoCont;
		this.tipoContView = StringUtil.formatInteger(tipoCont);
	}
	public TipoTramiteRSVO getTipoTramiteRS() {
		return tipoTramiteRS;
	}
	public void setTipoTramiteRS(TipoTramiteRSVO tipoTramiteRS) {
		this.tipoTramiteRS = tipoTramiteRS;
	}
	public String getTipoTransaccion() {
		return tipoTransaccion;
	}
	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}
	public Integer getTipoUsuario() {
		return tipoUsuario;
	}
	public void setTipoUsuario(Integer tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
		this.tipoUsuarioView = StringUtil.formatInteger(tipoUsuario);
	}
	public String getUsrCliente() {
		return usrCliente;
	}
	public void setUsrCliente(String usrCliente) {
		this.usrCliente = usrCliente;
	}
	public String getMsgDeuda() {
		return msgDeuda;
	}
	public void setMsgDeuda(String msgDeuda) {
		this.msgDeuda = msgDeuda;
	}
	public String getFechaVigenciaView(){
		if (this.mesBaja!=null && this.anioBaja!=null)
			return "01/"+StringUtil.completarCerosIzq(this.mesBaja.toString(), 2)+"/"+this.anioBaja;
		if (this.mesInicio!=null && this.anioInicio!=null)
			return "01/"+StringUtil.completarCerosIzq(this.mesInicio.toString(), 2)+"/"+this.anioInicio;
		
		return "";
	}
	public String getAdicEturView() {
		return adicEturView;
	}
	public void setAdicEturView(String adicEturView) {
		this.adicEturView = adicEturView;
	}
	public String getAnioBajaView() {
		return anioBajaView;
	}
	public void setAnioBajaView(String anioBajaView) {
		this.anioBajaView = anioBajaView;
	}
	public String getAnioInicioView() {
		return anioInicioView;
	}
	public void setAnioInicioView(String anioInicioView) {
		this.anioInicioView = anioInicioView;
	}
	public String getCanPerView() {
		return canPerView;
	}
	public void setCanPerView(String canPerView) {
		this.canPerView = canPerView;
	}
	public String getFechaTransaccionView() {
		return fechaTransaccionView;
	}
	public void setFechaTransaccionView(String fechaTransaccionView) {
		this.fechaTransaccionView = fechaTransaccionView;
	}
	public String getImporteAdicionalView() {
		return importeAdicionalView;
	}
	public void setImporteAdicionalView(String importeAdicionalView) {
		this.importeAdicionalView = importeAdicionalView;
	}
	public String getImporteDreiView() {
		return importeDreiView;
	}
	public void setImporteDreiView(String importeDreiView) {
		this.importeDreiView = importeDreiView;
	}
	public String getImporteEturView() {
		return importeEturView;
	}
	public void setImporteEturView(String importeEturView) {
		this.importeEturView = importeEturView;
	}
	public String getIngBruAnuView() {
		return ingBruAnuView;
	}
	public void setIngBruAnuView(String ingBruAnuView) {
		this.ingBruAnuView = ingBruAnuView;
	}
	public String getMesBajaView() {
		return mesBajaView;
	}
	public void setMesBajaView(String mesBajaView) {
		this.mesBajaView = mesBajaView;
	}
	public String getMesInicioView() {
		return mesInicioView;
	}
	public void setMesInicioView(String mesInicioView) {
		this.mesInicioView = mesInicioView;
	}
	public String getMotivoBajaView() {
		return motivoBajaView;
	}
	public void setMotivoBajaView(String motivoBajaView) {
		this.motivoBajaView = motivoBajaView;
	}
	public String getPrecioUnitarioView() {
		return precioUnitarioView;
	}
	public void setPrecioUnitarioView(String precioUnitarioView) {
		this.precioUnitarioView = precioUnitarioView;
	}
	public String getPublicidadView() {
		return publicidadView;
	}
	public void setPublicidadView(String publicidadView) {
		this.publicidadView = publicidadView;
	}
	public String getRedHabSocView() {
		return redHabSocView;
	}
	public void setRedHabSocView(String redHabSocView) {
		this.redHabSocView = redHabSocView;
	}
	public String getSupAfeView() {
		return supAfeView;
	}
	public void setSupAfeView(String supAfeView) {
		this.supAfeView = supAfeView;
	}
	public String getTipoContView() {
		return tipoContView;
	}
	public void setTipoContView(String tipoContView) {
		this.tipoContView = tipoContView;
	}
	public String getTipoUsuarioView() {
		return tipoUsuarioView;
	}
	public void setTipoUsuarioView(String tipoUsuarioView) {
		this.tipoUsuarioView = tipoUsuarioView;
	}
	public void setNroCategoriaView(String nroCategoriaView) {
		this.nroCategoriaView = nroCategoriaView;
	}
	public String getNroCategoriaView() {
		return this.nroCategoriaView;
	}
	public String getImporteTotalView() {
		Double importeDrei = 0D;
		Double importeAdicional = 0D;
		Double importeEtur = 0D;
		if(importeDrei != 0)
			importeDrei = this.importeDrei;
		if(importeAdicional != 0)
			importeAdicional = this.importeAdicional;
		if(importeEtur != 0)
			importeEtur = this.importeEtur;
		return StringUtil.formatDouble(importeDrei + importeAdicional + importeEtur);
	}
	public String getCodBarComprimido() {
		return codBarComprimido;
	}
	public void setCodBarComprimido(String codBarComprimido) {
		this.codBarComprimido = codBarComprimido;
	}
	public String getEstadoView() {
		if(this.getEstado().getId().equals(NovedadRSVO.REGISTRADO))
			return "Registrado";
		else if(this.getEstado().getId().equals(NovedadRSVO.PROCESADO_OK))
			return "Procesado OK";
		else if(this.getEstado().getId().equals(NovedadRSVO.PROCESADO_ERROR))
			return "Procesado Error";
		else if(this.getEstado().getId().equals(NovedadRSVO.REGISTRADO_NO_PROCESAR))
			return "Registro - No procesar";
		return "";
	}

	public String getMesAnioInicioView(){
		String mesAnioInicio = "";
		if(mesInicio != null)
			mesAnioInicio += mesInicio.toString()+"/";
		if(anioInicio != null)
			mesAnioInicio += anioInicio.toString();
		
		return mesAnioInicio;
	}
	
	//	 Buss flags getters y setters
	public Boolean getAplicarBussEnabled() {
		return aplicarBussEnabled;
	}
	public void setAplicarBussEnabled(Boolean aplicarBussEnabled) {
		this.aplicarBussEnabled = aplicarBussEnabled;
	}
	public String getAplicarEnabled() {
		return this.getAplicarBussEnabled() ? ENABLED : DISABLED;
	}

	/**
	 * Devuelve el codigo de barra comprimido y enviado si existe. Si no lo vuelve a comprimir usando el codigo de barra sin comprimir guardado en la novedadRs.
	 * 
	 * @return
	 */
	public String getCodBarComprimidoView() {
		if(StringUtil.isNullOrEmpty(codBarComprimido)){
			return StringUtil.genCodBarComprimidoForAfip(this.getCodBar());
		}
		return codBarComprimido;
	}
}

