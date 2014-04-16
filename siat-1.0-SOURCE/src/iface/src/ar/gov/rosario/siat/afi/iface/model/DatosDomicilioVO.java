//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PropietarioAfip;

/**
 * Value Object del DatosDomicilio
 * @author tecso
 *
 */
public class DatosDomicilioVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "datosDomicilioVO";
	
	private ForDecJurVO forDecJur = new ForDecJurVO();
	
	private Integer   	codPropietario;	
	private Integer   	codInterno;
	private Integer   	numero;
	private Integer   	provincia;
	private String  	calle="";	  
	private String   	adicional="";	  
	private String    	torre="";	 
	private String  	piso="";	
	private String    	dptoOficina="";	 
	private String   	sector="";	 
	private String    	barrio="";	 
	private String    	localidad="";	 
	private String   	codPostal="";
	private String   	codInternoView="";
	private String   	numeroView="";
	private String   	provinciaView="";
	
	private List<LocalVO> listLocal = new ArrayList<LocalVO>();
	private List<SocioVO> listSocio = new ArrayList<SocioVO>();

	
	// Constructores
	public DatosDomicilioVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public DatosDomicilioVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}

	// Getters y Setters	
	public Integer getCodPropietario() {
		return codPropietario;
	}

	public void setCodPropietario(Integer codPropietario) {
		this.codPropietario = codPropietario;
	}

	public Integer getCodInterno() {
		return codInterno;
	}

	public void setCodInterno(Integer codInterno) {
		this.codInterno = codInterno;
		this.codInternoView = StringUtil.formatInteger(codInterno);
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
		this.numeroView = StringUtil.formatInteger(numero);
	}

	public Integer getProvincia() {
		return provincia;
	}

	public void setProvincia(Integer provincia) {
		this.provincia = provincia;
		this.provinciaView = StringUtil.formatInteger(provincia);
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getAdicional() {
		return adicional;
	}

	public void setAdicional(String adicional) {
		this.adicional = adicional;
	}

	public String getTorre() {
		return torre;
	}

	public void setTorre(String torre) {
		this.torre = torre;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getDptoOficina() {
		return dptoOficina;
	}

	public void setDptoOficina(String dptoOficina) {
		this.dptoOficina = dptoOficina;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	public ForDecJurVO getForDecJur() {
		return forDecJur;
	}

	public void setForDecJur(ForDecJurVO forDecJur) {
		this.forDecJur = forDecJur;
	}

	public List<LocalVO> getListLocal() {
		return listLocal;
	}

	public void setListLocal(List<LocalVO> listLocal) {
		this.listLocal = listLocal;
	}

	public List<SocioVO> getListSocio() {
		return listSocio;
	}

	public void setListSocio(List<SocioVO> listSocio) {
		this.listSocio = listSocio;
	}

	public String getCodPostal() {
		return codPostal;
	}

	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}

	
	
	// View getters	

	public String getCodPropietarioView() {
		return PropietarioAfip.getById(this.codPropietario).getValue();
	}

	public String getCodInternoView() {
		return codInternoView;
	}

	public String getNumeroView() {
		return numeroView;
	}

	public String getProvinciaView() {
		return provinciaView;
	}


	public String getDireccionView(){
		String domicilio = "";

		String nombreCalle = this.getCalle();

		if (!StringUtil.isNullOrEmpty(nombreCalle)){
			domicilio = nombreCalle;
		}else{
			domicilio = "Sin Calle";
		}
		if (this.getNumero() != null){
			domicilio += " " + this.getNumeroView();
		}
		if (!StringUtil.isNullOrEmpty(this.getTorre()) && !"0".equals(this.getTorre().trim())){
			domicilio += " Torre:" + StringUtil.cut(this.getTorre());
		}
		if (!StringUtil.isNullOrEmpty(this.getPiso()) && !"0".equals(this.getPiso().trim())){
			domicilio += " Piso:" + this.getPiso();
		}
		if (!StringUtil.isNullOrEmpty(this.getDptoOficina()) && !"0".equals(this.getDptoOficina().trim())){
			domicilio += " Depto/Oficina:" + this.getDptoOficina();
		}
		return domicilio;
	}
	
	public String getLocalidadView(){
		return this.localidad+" "+this.codPostal;
	}

	public String getAsociacionView(){
		String asociacion = "";
		if(!ListUtil.isNullOrEmpty(this.listLocal)){
			boolean first = true;
			for(LocalVO local: this.listLocal){
				if(first){
					first = false;
				}else{
					asociacion += " - ";
				}
				asociacion += local.getNumeroCuenta();
			}
			if(asociacion.endsWith(" - "))
				asociacion = asociacion.substring(0, asociacion.lastIndexOf(" - "));
		}else if(!ListUtil.isNullOrEmpty(this.listSocio)){
			boolean first = true;
			for(SocioVO socio: this.listSocio){
				if(first){
					first = false;
				}else{
					asociacion += " - ";
				}
				asociacion += socio.getApellidoYNombreView();
				if(asociacion.endsWith(" - "))
					asociacion = asociacion.substring(0, asociacion.lastIndexOf(" - "));
			}	
		}
		return asociacion;
	}
}
