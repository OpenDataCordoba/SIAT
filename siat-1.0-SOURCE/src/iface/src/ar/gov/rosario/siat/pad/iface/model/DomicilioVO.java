//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class DomicilioVO extends SiatBussImageModel {

	// Propiedades
	private static final long serialVersionUID = 1L;

	public static final String NAME = "domicilioVO";
    private Long   	  numero;
    private SiNo      bis = SiNo.NO;
    private String 	  letraCalle;
    private Character letraNumero;
    private String   piso;
    private String   depto;
    private String    monoblock;
    private String    refGeografica;
    private CalleVO   calle = new CalleVO();

    private String numeroView;
    private boolean bisView;    
    
    private boolean validoPorRequeridos = false;
    
    // COORDENADAS
    private Double coordenadaX;
    private Double coordenadaY;

    private LocalidadVO localidad = new LocalidadVO();
    
    private String codPostalFueraRosario;
    
    private SiNo esValidado = SiNo.SI;
    private TipoDomicilioVO tipoDomicilio = new TipoDomicilioVO();
	
	// Constructores
	public DomicilioVO(){
		super();
	}
	
	public DomicilioVO getDuplicate(){
		
		DomicilioVO d = new DomicilioVO();
		d.setId(this.getId());
	    d.setNumero(numero);
	    d.setBis(bis);
	    
	    d.setLetraCalle(letraCalle);
	    d.setLetraNumero(letraNumero);
	    d.setPiso(piso);
	    d.setDepto(depto);
	    d.setMonoblock(monoblock);
	    d.setRefGeografica(refGeografica);
	    if (calle != null){
	    	d.setCalle(calle.getDuplicate());
	    }
	    d.setCoordenadaX(coordenadaX);
	    d.setCoordenadaY(coordenadaY);
	    if(localidad != null){
	    	d.setLocalidad(localidad.getDuplicate());
	    }
		return d;
	}

	// Getters y setters
	
	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
		this.numeroView = StringUtil.formatLong(numero); 
	}

	public String getLetraCalle() {
		return letraCalle;
	}

	public void setLetraCalle(String letraCalle) {
		this.letraCalle = letraCalle;
	}

	public Character getLetraNumero() {
		return letraNumero;
	}

	public void setLetraNumero(Character letraNumero) {
		this.letraNumero = letraNumero;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getDepto() {
		return depto;
	}

	public void setDepto(String depto) {
		this.depto = depto;
	}

	public String getMonoblock() {
		return monoblock;
	}

	public void setMonoblock(String monoblock) {
		this.monoblock = monoblock;
		if (this.monoblock != null) {
			this.monoblock = monoblock.trim();
		}
	}

	public String getRefGeografica() {
		return refGeografica;
	}

	public void setRefGeografica(String refGeografica) {
		this.refGeografica = refGeografica;
	}

	public CalleVO getCalle() {
		return calle;
	}

	public void setCalle(CalleVO calle) {
		this.calle = calle;
	}

	public Double getCoordenadaX() {
		return coordenadaX;
	}

	public void setCoordenadaX(Double coordenadaX) {
		this.coordenadaX = coordenadaX;
	}

	public Double getCoordenadaY() {
		return coordenadaY;
	}

	public void setCoordenadaY(Double coordenadaY) {
		this.coordenadaY = coordenadaY;
	}

	public LocalidadVO getLocalidad() {
		return localidad;
	}

	public void setLocalidad(LocalidadVO localidad) {
		this.localidad = localidad;
	}
	
	public String getCodPostalFueraRosario() {
		return codPostalFueraRosario;
	}

	public void setCodPostalFueraRosario(String codPostalFueraRosario) {
		this.codPostalFueraRosario = codPostalFueraRosario;
	}

	public String getNumeroView() {
		return numeroView;
	}

	public void setNumeroView(String numeroView) {
		this.numeroView = numeroView;
	}

	public SiNo getBis() {
		return bis;
	}

	public void setBis(SiNo bis) {
		this.bis = bis;
		
		if (bis == SiNo.SI) {
			this.bisView = true;
		} else {
			this.bisView = false;			
		}
		
	}
	
	public boolean getValidoPorRequeridos() {
		return validoPorRequeridos;
	}

	public void setValidoPorRequeridos(boolean validoPorRequeridos) {
		this.validoPorRequeridos = validoPorRequeridos;
	}

	public SiNo getEsValidado() {
		return esValidado;
	}

	public void setEsValidado(SiNo esValidado) {
		this.esValidado = esValidado;
	}

	public TipoDomicilioVO getTipoDomicilio() {
		return tipoDomicilio;
	}

	public void setTipoDomicilio(TipoDomicilioVO tipoDomicilio) {
		this.tipoDomicilio = tipoDomicilio;
	}

	public boolean getBisView() {
		return bisView;
	}

	public void setBisView(boolean bisView) {
		this.bisView = bisView;
		
		if (bisView == true) {
			this.bis = SiNo.SI;
		} else {
			this.bis = SiNo.NO;			
		}
	}

	public String getView(){
		String domicilio = "";

		String nombreCalle = StringUtil.cut(this.getCalle().getNombreCalle());

		if (!StringUtil.isNullOrEmpty(nombreCalle)){
			domicilio = nombreCalle;
		}else{
			domicilio = "Sin Calle";
		}

		if (this.getNumero() != null){
			domicilio += " " + this.getNumeroView();
		}
		if (this.getLetraNumero() != null ){
			domicilio += " " + this.getLetraNumero();
		}

		if (this.getBis() != null && this.getBis().getEsSI()) {
			domicilio += " bis ";
		}
		
		if (!StringUtil.isNullOrEmpty(this.getLetraCalle())){
			domicilio += " " + StringUtil.cut(this.getLetraCalle());
		}

		if (!StringUtil.isNullOrEmpty(this.getMonoblock())){
			domicilio += " MB:" + StringUtil.cut(this.getMonoblock());
		}

		if (!StringUtil.isNullOrEmpty(this.getPiso())){
			domicilio += " Piso:" + this.getPiso();
		}

		if (!StringUtil.isNullOrEmpty(this.getDepto())){
			domicilio += " Depto:" + this.getDepto();
		}

		return domicilio;
	}

	
	public String getViewConOtraLocalidad(){

		if (this.getLocalidad().getEsRosario()){
			return this.getView();
		} else {
			return this.getView() + " " + this.getLocalidad().getDescripcionPostalView() + "    cod. Postal: "+this.getCodPostalFueraRosario();
		}

	}

}
