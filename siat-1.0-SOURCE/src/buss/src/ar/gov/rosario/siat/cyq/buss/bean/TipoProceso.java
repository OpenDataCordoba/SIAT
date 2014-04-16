//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Tipos de Procesos
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_tipoProceso")
public class TipoProceso extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String COD_TIPO_PROCESO_POR_DEFECTO = "999";
	public static final String COD_TIPO_PROCESO_TFC = "101"; // Transferencia de Fondos de Comercio
	
	@Column(name = "codTipoProceso")
	private String codTipoProceso;

	@Column(name = "desTipoProceso")
	private String desTipoProceso;
	
	@Column(name = "tipo")
	private String tipo;

	// Constructores
	public TipoProceso(){
		super();
	}
	// Getters Y Setters 
	public String getCodTipoProceso() {
		return codTipoProceso;
	}
	public void setCodTipoProceso(String codTipoProceso) {
		this.codTipoProceso = codTipoProceso;
	}
	public String getDesTipoProceso() {
		return desTipoProceso;
	}
	public void setDesTipoProceso(String desTipoProceso) {
		this.desTipoProceso = desTipoProceso;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	// Metodos de Clase
	public static TipoProceso getById(Long id) {
		return (TipoProceso) CyqDAOFactory.getTipoProcesoDAO().getById(id);
	}
	
	public static TipoProceso getByIdNull(Long id) {
		return (TipoProceso) CyqDAOFactory.getTipoProcesoDAO().getByIdNull(id);
	}
	
	public static List<TipoProceso> getList() {
		return (ArrayList<TipoProceso>) CyqDAOFactory.getTipoProcesoDAO().getList();
	}
	
	public static List<TipoProceso> getListActivos() {			
		return (ArrayList<TipoProceso>) CyqDAOFactory.getTipoProcesoDAO().getListActiva();
	}
	
	
	public static List<TipoProceso> getList4Conversion(TipoProceso tipoProceso) throws Exception {			
		return (ArrayList<TipoProceso>) CyqDAOFactory.getTipoProcesoDAO().getList4Conversion(tipoProceso);
	}
	
	
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones		
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}

}
