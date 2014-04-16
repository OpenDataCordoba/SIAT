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
 * Leyendas para Acumuladores de Partidas. Se utilizan en el Reporte de Rentas.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_leyParAcu")
public class LeyParAcu extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "descripcion")
	private String descripcion;
		
	//Constructores 
	public LeyParAcu(){
		super();
	}

	// Getters Y Setters
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	// Metodos de clase	
	public static LeyParAcu getById(Long id) {
		return (LeyParAcu) BalDAOFactory.getLeyParAcuDAO().getById(id);
	}
	
	public static LeyParAcu getByIdNull(Long id) {
		return (LeyParAcu) BalDAOFactory.getLeyParAcuDAO().getByIdNull(id);
	}
	
	public static LeyParAcu getByCod(String codigo) {
		return (LeyParAcu) BalDAOFactory.getLeyParAcuDAO().getByCod(codigo);
	}
	
	public static List<LeyParAcu> getList() {
		return (ArrayList<LeyParAcu>) BalDAOFactory.getLeyParAcuDAO().getList();
	}
	
	public static List<LeyParAcu> getListActivos() {			
		return (ArrayList<LeyParAcu>) BalDAOFactory.getLeyParAcuDAO().getListActiva();
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
		if(StringUtil.isNullOrEmpty(getCodigo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.LEYPARACU_CODIGO_LABEL);
		}
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.LEYPARACU_DESCRIPCION_LABEL);
		}
		
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
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	


}
