//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.bean.CuentaBanco;
import ar.gov.rosario.siat.bal.buss.bean.Reclamo;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.bean.AuxAplPagCue;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.PagoDeuda;
import ar.gov.rosario.siat.gde.buss.bean.Recibo;
import ar.gov.rosario.siat.gde.buss.bean.ReciboConvenio;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a Banco
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_banco")
public class Banco extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String COD_BANCO_GENERICO = "999";
	
	@Column(name = "codBanco")
	private String codBanco;

	@Column(name = "desBanco")
	private String desBanco;
	
	// Constructores
	public Banco() {
		super();
	}

	// Getters Y Setters
	public String getDesBanco() {
		return desBanco;
	}
	public void setDesBanco(String desBanco) {
		this.desBanco = desBanco;
	}
	public String getCodBanco() {
		return codBanco;
	}
	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}

	// Metodos de clase
	public static Banco getById(Long id) {
		return (Banco) DefDAOFactory.getBancoDAO().getById(id);
	}

	public static Banco getByIdNull(Long id) {
		return (Banco) DefDAOFactory.getBancoDAO().getByIdNull(id);
	}

	public static Banco getByCodBanco(String codBanco) {
		return (Banco) DefDAOFactory.getBancoDAO().getByCodBanco(codBanco);
	}
	public static List<Banco> getList() {
		return (ArrayList<Banco>) DefDAOFactory.getBancoDAO().getList();
	}

	public static List<Banco> getListActivos() {
		return (ArrayList<Banco>) DefDAOFactory.getBancoDAO().getListActiva();
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
		
		if (GenericDAO.hasReference(this, CuentaBanco.class, "banco")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,DefError.BANCO_LABEL , BalError.CUENTABANCO_LABEL);
		}
		if (GenericDAO.hasReference(this, Reclamo.class, "banco")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,DefError.BANCO_LABEL , BalError.RECLAMO_LABEL);
		}
		if (GenericDAO.hasReference(this, Reclamo.class, "banco")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,DefError.BANCO_LABEL , BalError.RECLAMO_LABEL);
		}
		if (GenericDAO.hasReference(this, ConvenioCuota.class, "bancoPago") || GenericDAO.hasReference(this, PagoDeuda.class, "bancoPago")
				|| GenericDAO.hasReference(this, Recibo.class, "bancoPago") || GenericDAO.hasReference(this, ReciboConvenio.class, "bancoPago")
				|| GenericDAO.hasReference(this, AuxAplPagCue.class, "bancoPago")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,	DefError.BANCO_LABEL , DefError.BANCO_REGISTROS_ASOCIADOS);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//Validaciones        
		
		if (StringUtil.isNullOrEmpty(getCodBanco())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.BANCO_CODBANCO);
		}		
		if (StringUtil.isNullOrEmpty(getDesBanco())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.BANCO_DESBANCO);
		}
		
		if (hasError()) {
			return false;
		}
		
		
		
		return true;
	}
}
