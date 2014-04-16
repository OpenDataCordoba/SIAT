//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;

public class BussImageModel extends CommonView {

	private String    usuario=""; // Usuario que ingresa o modifica los datos de la base
    private Date      fechaUltMdf= new Date(); // Fecha de ultima modificacion en la base
    private Estado    estado = Estado.ACTIVO; // Estado en la base
    private Vigencia  vigencia = Vigencia.CREADO;
    
	/**
	 * Constructor
	 */
	public BussImageModel() {    
		super();
	}

    /**
     * Constructor parametrizado
     * @param commonKey
     */
    public BussImageModel(CommonKey commonKey) {
        super();
        super.setId(commonKey.getId());
    }
    
	/**
	 * Constructor parametrizado
	 * @param id
	 */
	public BussImageModel(Long id) {
		super();
		super.setId(id);
	}

	/**
	 * Constructor parametrizado
	 * @param id
	 */
	public BussImageModel(long id) {
		super();
		super.setId(Long.valueOf(id));
	}
	
	/**
	 * Constructor parametrizado
	 * @param id
	 */
	public BussImageModel(String id) {
		this.setId(Long.valueOf(id));
	}

	public void setId(Long id){
		super.setId(id);
	}

	public Date getFechaUltMdf() {
		return fechaUltMdf;
	}

	public void setFechaUltMdf(Date fechaUltMdf) {
		this.fechaUltMdf = fechaUltMdf;
	}
    
    public String getFechaUltMdfView() {
        return getStringByDate(fechaUltMdf);
    }

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Vigencia getVigencia() {
		return vigencia;
	}

	public void setVigencia(Vigencia vigencia) {
		this.vigencia = vigencia;
	}
	
	/**
	 * Valida que la fecha del bean al momento de modificar sea la misma que la del objeto al que se muestra en la vista. 
	 * 
	 * @param fechaBO
	 * @return boolean
	 */
	public boolean validateVersion(Date fechaBO){
		if(DateUtil.isDateEqual(this.getFechaUltMdf(), fechaBO)){
			return true;
		}else{
			this.addRecoverableValueError("El registro ha sido actualizado por otro usuario. Debe recargar los datos.");
			return false;
		}
	}
}
