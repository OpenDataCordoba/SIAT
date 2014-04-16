//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Bean correspondiente al Dominio Atributo Valor
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_domAtrVal") 
public class DomAtrVal extends BaseBO {
	
	
	
	// Propiedades
	private static final long serialVersionUID = 1L;	
	
	@Column(name = "strValor")
	private String strValor;    
	
	@Column(name = "desValor")
	private String desValor;
	
	@ManyToOne()  
    @JoinColumn(name="idDomAtr")
	private DomAtr domAtr; 

	@Column(name = "fechaDesde")	
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")	
	private Date fechaHasta;

	// Constructores
	public DomAtrVal(){
		super();
	}

	//	Getters y setters
	public String getDesValor() {
		return desValor;
	}
	public void setDesValor(String desValor) {
		this.desValor = desValor;
	}
	public DomAtr getDomAtr() {
		return domAtr;
	}
	public void setDomAtr(DomAtr domAtr) {
		this.domAtr = domAtr;
	}
	public String getValor() {
		return strValor;
	}
	public void setValor(String valor) {
		this.strValor = valor;
	}
	public String getStrValor() {
		return strValor;
	}
	public void setStrValor(String strValor) {
		this.strValor = strValor;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	// Metodos de Clase
	public static DomAtrVal getById(Long id) {
		return (DomAtrVal) DefDAOFactory.getDomAtrValDAO().getById(id);  
	}
	
	public static List<DomAtrVal> getList() {
		return (List<DomAtrVal>) DefDAOFactory.getDomAtrValDAO().getList();
	}
	
	public static List<DomAtrVal> getListActivos() {			
		return (List<DomAtrVal>) DefDAOFactory.getDomAtrValDAO().getListActiva();
	}
	
	// Metodos de instancia
	// Validaciones 
	public boolean validateCreate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		if (!this.validate()) {
			return false;
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}

	public boolean validateUpdate() throws Exception {
		
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
	
		if (hasError()) {
			return false;
		}
		
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
		
		// Validaciones de requerido
		if (getDomAtr() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.DOMATR_LABEL );
		}

		if (StringUtil.isNullOrEmpty(getStrValor())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.DOMATRVAL_STRVALOR);
		}

		if (StringUtil.isNullOrEmpty(getDesValor())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.DOMATRVAL_DESVALOR);
		}

		if (getFechaDesde() == null ) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.DOMATRVAL_FECHADESDE);
		}
		
		if (hasError()) {
			return false;
		}
		
		// validar el rango fecha desde - fecha hasta
		if (getFechaHasta() != null ) {
			if ( DateUtil.isDateAfter(getFechaDesde(), getFechaHasta()) ) {
				addRecoverableError( BaseError.MSG_VALORMAYORQUE, 
					DefError.DOMATRVAL_FECHADESDE, DefError.DOMATRVAL_FECHAHASTA );
			}		
		}
		
		// validar que el formato del strValor coincida con el tipo correspondiente
		if(!this.getDomAtr().getTipoAtributo().validarTipoValor(this.getStrValor())){
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_INVALIDO, DefError.DOMATRVAL_STRVALOR );
		}

		// validar la unicidad de Dominio atributo y strValor
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addEntity("domAtr").addString("strValor");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)){
			addRecoverableError(BaseError.MSG_CAMPO_UNICOS2, DefError.DOMATR_LABEL, DefError.DOMATRVAL_STRVALOR );
		}
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
}
