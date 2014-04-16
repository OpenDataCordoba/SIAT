//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ZonaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

public class RepartidorVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private Long nroRepartidor;
	private String desRepartidor="";
	private RecursoVO recurso = new RecursoVO();
  	private PersonaVO persona = new PersonaVO();
  	private String legajo;
	private ZonaVO zona = new ZonaVO();
	private TipoRepartidorVO tipoRepartidor = new TipoRepartidorVO();
	private BrocheVO broche = new BrocheVO();

	private String nroRepartidorView;
	private String personaView;
	
	// Listas de Entidades Relacionadas con Repartidor
	private List<CriRepCatVO> listCriRepCat = new ArrayList<CriRepCatVO>();
	private List<CriRepCalleVO> listCriRepCalle = new ArrayList<CriRepCalleVO>();
	
	//Constructores 
	
	public RepartidorVO(){
		super();
	}

	public RepartidorVO(int id, String desRepartidor) {
		super(id);
		setDesRepartidor(desRepartidor);
	}
	
	//Getters y Setters
	
	public String getDesRepartidor() {
		return desRepartidor;
	}
	public void setDesRepartidor(String desRepartidor) {
		this.desRepartidor = desRepartidor;
	}
	public String getLegajo() {
		return legajo;
	}
	public void setLegajo(String legajo) {
		this.legajo = legajo;
	}
	public Long getNroRepartidor() {
		return nroRepartidor;
	}
	public void setNroRepartidor(Long nroRepartidor) {
		this.nroRepartidor = nroRepartidor;
		this.nroRepartidorView = nroRepartidor.toString();
	}
	public PersonaVO getPersona() {
		return persona;
	}
	public void setPersona(PersonaVO persona) {
		this.persona = persona;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public TipoRepartidorVO getTipoRepartidor() {
		return tipoRepartidor;
	}
	public void setTipoRepartidor(TipoRepartidorVO tipoRepartidor) {
		this.tipoRepartidor = tipoRepartidor;
	}
	public ZonaVO getZona() {
		return zona;
	}
	public void setZona(ZonaVO zona) {
		this.zona = zona;
	}
	public BrocheVO getBroche() {
		return broche;
	}
	public void setBroche(BrocheVO broche) {
		this.broche = broche;
	}
	public String getNroRepartidorView() {
		return nroRepartidorView;
	}
	public void setNroRepartidorView(String nroRepartidorView) {
		this.nroRepartidorView = nroRepartidorView;
	}
	public String getPersonaView() {
		//personaView = persona.getView();
		personaView = getDesRepartidor();
		return personaView;
	} 
	public void setPersonaView(String personaView) {
		this.personaView = personaView;
	}

	public List<CriRepCalleVO> getListCriRepCalle() {
		return listCriRepCalle;
	}
	public void setListCriRepCalle(List<CriRepCalleVO> listCriRepCalle) {
		this.listCriRepCalle = listCriRepCalle;
	}
	public List<CriRepCatVO> getListCriRepCat() {
		return listCriRepCat;
	}
	public void setListCriRepCat(List<CriRepCatVO> listCriRepCat) {
		this.listCriRepCat = listCriRepCat;
	}
	/**
	 *  Devuelve la desRepartidor si el id=-1, y personaView si id<>-1.
	 *  (es para usar un combo de repartidores) 
	 * @return string
	 */
	public String getDescripcionForCombo(){
		
		if(this.getId() == null || this.getId().longValue()==-1){
			return this.getDesRepartidor();
		}

		return this.getIdView()+" - "+this.getPersonaView();
	}
	
	public String getDesRepartidorView(){
		String descripcion = this.getDesRepartidor();
		
		if (StringUtil.isNullOrEmpty(descripcion)) {
			descripcion = this.getPersonaView();
		}
		
		return descripcion;
	}

}
