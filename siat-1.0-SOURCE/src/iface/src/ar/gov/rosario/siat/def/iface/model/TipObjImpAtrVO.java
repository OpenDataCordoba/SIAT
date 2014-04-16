//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Tipo Objeto Imponible Atributo
 * @author tecso
 *
 */
public class TipObjImpAtrVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipObjImpAtrVO";
	

	private TipObjImpVO tipObjImp = new TipObjImpVO();
	private AtributoVO 	atributo  = new AtributoVO();

	private SiNo 	esRequerido    = SiNo.OpcionSelecionar;
	private SiNo 	esMultivalor   = SiNo.OpcionSelecionar;
	private SiNo 	esAtributoSIAT = SiNo.OpcionSelecionar;
	private Integer posColInt      = 0;
	private String  posColIntView  = "";
	private Integer posColIntHas   = 0;
	private String  posColIntHasView  = "";
	private SiNo 	esClave        = SiNo.OpcionSelecionar;
	private SiNo 	esClaveFuncional = SiNo.OpcionSelecionar;
	private SiNo 	esDomicilioEnvio = SiNo.OpcionSelecionar;
	private SiNo 	esUbicacion    	= SiNo.OpcionSelecionar;
	private SiNo 	esVisConDeu    	= SiNo.OpcionSelecionar;
	private SiNo 	poseeVigencia  	= SiNo.OpcionSelecionar;
	private String  valorDefecto   	= "";
	private SiNo 	esAtributoBus   = SiNo.OpcionSelecionar;
	private SiNo 	esAtriBusMasiva = SiNo.OpcionSelecionar;
	private SiNo 	admBusPorRan    = SiNo.OpcionSelecionar;
	private SiNo	valDefEsRef		= SiNo.NO;
	private SiNo 	esVisible       = SiNo.SI;
	private SiNo	esCodGesCue		= SiNo.OpcionSelecionar; 
	
	private Date fechaDesde; 
	private String fechaDesdeView = "";
	private Date fechaHasta; 
	private String fechaHastaView = "";
	
	// Buss Flags
	private String 	modifAtributoEnabled;
	private String 	modifEsAtributoSIATEnabled;
	private String 	modifPosColIntDesEnabled;
	private String 	modifPosColIntHasEnabled;
	
	// View Constants
	
	
	// Constructores
	public TipObjImpAtrVO() {
		super();
	}

	public TipObjImpAtrVO(TipObjImpVO tipObjImp ) {
		super();
		this.tipObjImp = tipObjImp;
	}
	
	public TipObjImpAtrVO(TipObjImpVO tipObjImp, AtributoVO atributo ) {
		this(tipObjImp);
		this.atributo = atributo;
	}

	public TipObjImpAtrVO(int id, String desAtributo) {
		super();
		setId(Long.valueOf(id));
		this.setAtributo(new AtributoVO());
		this.getAtributo().setDesAtributo(desAtributo);
	}
	
	//	 Getters y Setters	
	public SiNo getAdmBusPorRan() {
		return admBusPorRan;
	}
	public void setAdmBusPorRan(SiNo admBusPorRan) {
		this.admBusPorRan = admBusPorRan;
	}
	public AtributoVO getAtributo() {
		return atributo;
	}
	public void setAtributo(AtributoVO atributo) {
		this.atributo = atributo;
	}
	public SiNo getEsAtributoBus() {
		return esAtributoBus;
	}
	public void setEsAtributoBus(SiNo esAtributoBus) {
		this.esAtributoBus = esAtributoBus;
	}
	public SiNo getEsAtributoSIAT() {
		return esAtributoSIAT;
	}
	public void setEsAtributoSIAT(SiNo esAtributoSIAT) {
		this.esAtributoSIAT = esAtributoSIAT;
	}
	public SiNo getEsClave() {
		return esClave;
	}
	public void setEsClave(SiNo esClave) {
		this.esClave = esClave;
	}
	public SiNo getEsClaveFuncional() {
		return esClaveFuncional;
	}
	public void setEsClaveFuncional(SiNo esClaveFuncional) {
		this.esClaveFuncional = esClaveFuncional;
	}
	public SiNo getEsDomicilioEnvio() {
		return esDomicilioEnvio;
	}
	public void setEsDomicilioEnvio(SiNo esDomicilioEnvio) {
		this.esDomicilioEnvio = esDomicilioEnvio;
	}
	public SiNo getEsMultivalor() {
		return esMultivalor;
	}
	public void setEsMultivalor(SiNo esMultivalor) {
		this.esMultivalor = esMultivalor;
	}
	public SiNo getEsVisConDeu() {
		return esVisConDeu;
	}
	public void setEsVisConDeu(SiNo esVisConDeu) {
		this.esVisConDeu = esVisConDeu;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
		this.fechaHasta = fechaHasta;
	}
	public Integer getPosColInt() {
		return posColInt;
	}
	public void setPosColInt(Integer posColInt) {
		this.posColIntView = StringUtil.formatInteger(posColInt);
		this.posColInt = posColInt;
	}
	public Integer getPosColIntHas() {
		return posColIntHas;
	}
	public void setPosColIntHas(Integer posColIntHas) {
		this.posColIntHasView = StringUtil.formatInteger(posColIntHas);
		this.posColIntHas = posColIntHas;
	}
	public SiNo getPoseeVigencia() {
		return poseeVigencia;
	}
	public void setPoseeVigencia(SiNo poseeVigencia) {
		this.poseeVigencia = poseeVigencia;
	}
	public TipObjImpVO getTipObjImp() {
		return tipObjImp;
	}
	public void setTipObjImp(TipObjImpVO tipObjImp) {
		this.tipObjImp = tipObjImp;
	}
	public String getValorDefecto() {
		return valorDefecto;
	}
	public void setValorDefecto(String valorDefecto) {
		this.valorDefecto = valorDefecto;
	}
	public SiNo getEsUbicacion() {
		return esUbicacion;
	}
	public void setEsUbicacion(SiNo esUbicacion) {
		this.esUbicacion = esUbicacion;
	}
	
	public SiNo getEsRequerido() {
		return esRequerido;
	}
	public void setEsRequerido(SiNo esRequerido) {
		this.esRequerido = esRequerido;
	}
	public SiNo getEsAtriBusMasiva() {
		return esAtriBusMasiva;
	}
	public void setEsAtriBusMasiva(SiNo esAtriBusMasiva) {
		this.esAtriBusMasiva = esAtriBusMasiva;
	}

	public SiNo getValDefEsRef() {
		return valDefEsRef;
	}

	public void setValDefEsRef(SiNo valDefEsRef) {
		this.valDefEsRef = valDefEsRef;
	}

	public SiNo getEsVisible() {
		return esVisible;
	}

	public void setEsVisible(SiNo esVisible) {
		this.esVisible = esVisible;
	}

	public void setEsCodGesCue(SiNo esCodGesCue) {
		this.esCodGesCue = esCodGesCue;
	}

	public SiNo getEsCodGesCue() {
		return esCodGesCue;
	}

	// Buss flags getters y setters
	public String getModifAtributoEnabled() {
		return modifAtributoEnabled;
	}
	public void setModifAtributoEnabled(String modifAtributoEnabled) {
		this.modifAtributoEnabled = modifAtributoEnabled;
	}
	public String getModifEsAtributoSIATEnabled() {
		return modifEsAtributoSIATEnabled;
	}
	public void setModifEsAtributoSIATEnabled(String modifEsAtributoSIATEnabled) {
		this.modifEsAtributoSIATEnabled = modifEsAtributoSIATEnabled;
	}
	public String getModifPosColIntDesEnabled() {
		return modifPosColIntDesEnabled;
	}
	public void setModifPosColIntDesEnabled(String modifPosColIntDesEnabled) {
		this.modifPosColIntDesEnabled = modifPosColIntDesEnabled;
	}
	public String getModifPosColIntHasEnabled() {
		return modifPosColIntHasEnabled;
	}
	public void setModifPosColIntHasEnabled(String modifPosColIntHasEnabled) {
		this.modifPosColIntHasEnabled = modifPosColIntHasEnabled;
	}
	
	// View flags getters
	
	
	// View getters
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
	public String getPosColIntHasView() {
		return posColIntHasView;
	}
	public void setPosColIntHasView(String posColHasView) {
		this.posColIntHasView = posColHasView;
	}
	public String getPosColIntView() {
		return posColIntView;
	}
	public void setPosColIntView(String posColIntView) {
		this.posColIntView = posColIntView;
	}

}
