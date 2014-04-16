//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Adapter de Persona
 * 
 * @author tecso
 */
public class PersonaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "personaAdapterVO";
	
    private PersonaVO persona = new PersonaVO();
    
    private List<Sexo> listSexo = Sexo.getList(Sexo.OpcionSeleccionar);
    
    private List<TipoDocumentoVO> listTipoDocumento = new ArrayList<TipoDocumentoVO>();
    
    private List<SiNo>   listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public PersonaAdapter(){
    	super(PadSecurityConstants.ABM_PERSONA);
    }

    //  Getters y Setters
	public PersonaVO getPersona() {
		return persona;
	}
	public void setPersona(PersonaVO persona) {
		this.persona = persona;
	}
	public List<Sexo> getListSexo() {
		return listSexo;
	}
	public void setListSexo(List<Sexo> listSexo) {
		this.listSexo = listSexo;
	}
	public List<TipoDocumentoVO> getListTipoDocumento() {
		return listTipoDocumento;
	}
	public void setListTipoDocumento(List<TipoDocumentoVO> listTipoDocumento) {
		this.listTipoDocumento = listTipoDocumento;
	}
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	// Metodo que permite al populateVO encontrar el Domicilio a traves de la reutilizacion de domicilio
	public DomicilioVO getDomicilio(){
		return this.getPersona().getDomicilio();
	}
}