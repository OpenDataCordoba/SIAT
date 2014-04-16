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
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a Tipo de Indeterminado
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoIndet")
public class TipoIndet extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Integer COD_DUPLICE_MR = 1;

	@Column(name = "codTipoIndet")
	private String codTipoIndet;
	
	@Column(name = "desTipoIndet")
	private String desTipoIndet;

	@Column(name = "codIndetMR")
	private String codIndetMR;

	//Constructores 
	public TipoIndet(){
		super();
	}

	// Getters Y Setters
	public String getCodTipoIndet() {
		return codTipoIndet;
	}
	public void setCodTipoIndet(String codTipoIndet) {
		this.codTipoIndet = codTipoIndet;
	}
	public String getDesTipoIndet() {
		return desTipoIndet;
	}
	public void setDesTipoIndet(String desTipoIndet) {
		this.desTipoIndet = desTipoIndet;
	}
	public String getCodIndetMR() {
		return codIndetMR;
	}
	public void setCodIndetMR(String codIndetMR) {
		this.codIndetMR = codIndetMR;
	}

	// Metodos de clase	
	public static TipoIndet getById(Long id) {
		return (TipoIndet) BalDAOFactory.getTipoIndetDAO().getById(id);
	}
	
	public static TipoIndet getByIdNull(Long id) {
		return (TipoIndet) BalDAOFactory.getTipoIndetDAO().getByIdNull(id);
	}
	
	public static TipoIndet getByCodigo(String codTipoIndet) throws Exception{
		return (TipoIndet) BalDAOFactory.getTipoIndetDAO().getByCodigo(codTipoIndet);
	}
	
	public static List<TipoIndet> getList() {
		return (ArrayList<TipoIndet>) BalDAOFactory.getTipoIndetDAO().getList();
	}
	
	public static List<TipoIndet> getListActivos() {			
		return (ArrayList<TipoIndet>) BalDAOFactory.getTipoIndetDAO().getListActiva();
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
		
		//Validaciones de VO

		if (!this.validate()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
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
		
		if (StringUtil.isNullOrEmpty(getCodTipoIndet())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOINDET_LABEL);
		}

		//Validaciones de Requeridos	
		
		if (hasError()) {
			return false;
		}

		return true;
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
