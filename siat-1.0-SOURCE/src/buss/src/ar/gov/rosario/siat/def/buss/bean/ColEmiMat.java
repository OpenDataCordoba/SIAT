//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.QryTableDataType;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a las columnas de  
 * una matriz de Parametros de Emision.
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_colEmiMat")
public class ColEmiMat extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idEmiMat") 
	private EmiMat emiMat;
	
	@Column(name = "codColumna")
	private String codColumna;

	@Column(name = "tipoColumna")
	private Integer tipoColumna;

	@Column(name = "tipoDato")
	private Integer tipoDato;

	@Column(name = "orden")
	private Integer orden;
	
	// Constructores
	public ColEmiMat(){
		super();
	}
	
	public ColEmiMat(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ColEmiMat getById(Long id) {
		return (ColEmiMat) DefDAOFactory.getColEmiMatDAO().getById(id);
	}
	
	public static ColEmiMat getByIdNull(Long id) {
		return (ColEmiMat) DefDAOFactory.getColEmiMatDAO().getByIdNull(id);
	}
	
	public static List<ColEmiMat> getList() {
		return (ArrayList<ColEmiMat>) DefDAOFactory.getColEmiMatDAO().getList();
	}
	
	public static List<ColEmiMat> getListActivos() {			
		return (ArrayList<ColEmiMat>) DefDAOFactory.getColEmiMatDAO().getListActiva();
	}
	
	
	// Getters y setters
	public EmiMat getEmiMat() {
		return emiMat;
	}
	
	public void setEmiMat(EmiMat emiMat) {
		this.emiMat = emiMat;
	}
	
	public String getCodColumna() {
		return codColumna;
	}
	
	public void setCodColumna(String codColumna) {
		this.codColumna = codColumna;
	}
	
	public Integer getTipoColumna() {
		return tipoColumna;
	}
	
	public void setTipoColumna(Integer tipoColumna) {
		this.tipoColumna = tipoColumna;
	}
	
	public Integer getTipoDato() {
		return tipoDato;
	}
	
	public void setTipoDato(Integer tipoDato) {
		this.tipoDato = tipoDato;
	}
	
	public Integer getOrden() {
		return orden;
	}
	
	public void setOrden(Integer orden) {
		this.orden = orden;
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
		if (getEmiMat() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.COLEMIMAT_EMIMAT );
		}
		
		if (StringUtil.isNullOrEmpty(getCodColumna())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.COLEMIMAT_CODCOLUMNA );
		}

		if (getTipoDato() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.COLEMIMAT_TIPODATO );
		}

		if (getTipoDato() != null && !QryTableDataType.getEsValido(getTipoDato())) {
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, DefError.COLEMIMAT_TIPODATO );
		}

		if (getTipoColumna() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.COLEMIMAT_TIPOCOLUMNA );
		}

		if (getTipoColumna() != null && !SiNo.getEsValido(getTipoColumna())) {
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, DefError.COLEMIMAT_TIPOCOLUMNA );
		}
		
		if (getOrden() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.COLEMIMAT_ORDEN );
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de el codigo ingresado sea unico
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addEntity("emiMat");
		uniqueMap.addString("codColumna");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.COLEMIMAT_CODCOLUMNA);			
		}

		// Validamos que el orden ingresado sea unico
		UniqueMap uniqueOrder = new UniqueMap();
		uniqueOrder.addEntity("emiMat");
		uniqueOrder.addInteger("orden");
		if(!GenericDAO.checkIsUnique(this, uniqueOrder)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.COLEMIMAT_ORDEN);			
		}

		return true;
	}
	
	// Metodos de negocio
	public String getTipoDatoForReport() {
		return QryTableDataType.getById(tipoDato).getValue();
	}

	public String getTipoColumnaForReport() {
		return SiNo.getById(tipoColumna).getValue();
	}

}
