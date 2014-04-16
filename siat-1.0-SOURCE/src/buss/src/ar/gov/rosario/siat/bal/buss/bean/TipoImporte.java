//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Tipo de Importe
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoImporte")
public class TipoImporte extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Long ID_CAPITAL = 1L;
	public static final Long ID_INTERES_FINANCIERO = 2L;
	public static final Long ID_ACTUALIZACION = 3L;
	public static final Long ID_SALDO_A_REINGRESAR = 4L;
	public static final Long ID_DUPLICES = 5L;
	public static final Long ID_INDETERMINADO = 6L;
	public static final Long ID_REDONDEO = 7L;
	public static final Long ID_PAGO_A_CUENTA = 8L;

	
	@Column(name = "codTipoImporte")
	private String codTipoImporte;
	
	@Column(name = "desTipoImporte")
	private String desTipoImporte;
	
	@Column(name = "abreConceptos")
	private Integer abreConceptos;
	
	//Constructores 
	
	public TipoImporte(){
		super();
	}

	// Getters Y Setters

	public Integer getAbreConceptos() {
		return abreConceptos;
	}
	public void setAbreConceptos(Integer abreConceptos) {
		this.abreConceptos = abreConceptos;
	}
	public String getCodTipoImporte() {
		return codTipoImporte;
	}
	public void setCodTipoImporte(String codTipoImporte) {
		this.codTipoImporte = codTipoImporte;
	}
	public String getDesTipoImporte() {
		return desTipoImporte;
	}
	public void setDesTipoImporte(String desTipoImporte) {
		this.desTipoImporte = desTipoImporte;
	}
	
	// Metodos de clase	
	public static TipoImporte getById(Long id) {
		return (TipoImporte) BalDAOFactory.getTipoImporteDAO().getById(id);
	}
	
	public static TipoImporte getByIdNull(Long id) {
		return (TipoImporte) BalDAOFactory.getTipoImporteDAO().getByIdNull(id);
	}
	
	public static List<TipoImporte> getList() {
		return (ArrayList<TipoImporte>) BalDAOFactory.getTipoImporteDAO().getList();
	}
	
	public static List<TipoImporte> getListActivos() {			
		return (ArrayList<TipoImporte>) BalDAOFactory.getTipoImporteDAO().getListActiva();
	}
	
	public static List<TipoImporte> getListActivosNoAbreConcepto() throws Exception {			
		return (ArrayList<TipoImporte>) BalDAOFactory.getTipoImporteDAO().getListActivosNoAbreConcepto();
	}
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
