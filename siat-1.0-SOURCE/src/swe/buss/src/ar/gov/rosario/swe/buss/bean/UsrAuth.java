//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ar.gov.rosario.swe.SweCommonError;
import ar.gov.rosario.swe.buss.dao.GenericDAO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;

@Entity
@Table(name = "swe_usrauth")
@SequenceGenerator(
    name="usrauth_seq",
    sequenceName="swe_usrauth_id_seq",
    allocationSize = 0
)
public class UsrAuth extends BaseBO{
    
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator="usrauth_seq",strategy=GenerationType.SEQUENCE)
    private Long id;
 
    public Long getId() {
        return id;
    }
    
    @Column(name = "nomUsuario")
    private String nomUsuario;    

	@Column(name = "password")
    private String password;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAplicacion")	
	private Aplicacion aplicacion;	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNomUsuario(String nomUsuario) {
		this.nomUsuario = nomUsuario;
	}

	public String getNomUsuario() {
		return nomUsuario;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}
	
	
	// Validaciones 	
	/**
	 * Valida la actualizacion del UsrAuth
	 * @return boolean
	 * @throws Exception
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();

		// validaciones comunes
		this.validate();
		
		if (hasError()) {
			return false;
		}

		return !hasError();		
	}
	
	/**
	 * Validaciones comunes de creacion y actualizacion	 
	 * Unicidad de nomUsuario.
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		UniqueMap uniqueMap = new UniqueMap();

	
		// Validaciones de Unicidad
		uniqueMap.addString("nomUsuario");
//		uniqueMap.addString("aplicacion.codigo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)){
			addRecoverableError(SweCommonError.USRAUTH_UNIQUE);
		}

		return !hasError();
	}
	

}
