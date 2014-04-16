//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

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
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipoMulta
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipoMulta")
public class TipoMulta extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_CIERRE_COMERCIO = 4L;
	public static final long ID_MULTA_REVISION = 7L;
	
	@Column(name = "desTipoMulta")
	private String desTipoMulta;
	
	@Column(name = "esImporteManual")
	private Integer esImporteManual;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idrecClaDeu")
	private RecClaDeu recClaDeu;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idrecurso")
	private Recurso recurso;
	
	@Column(name = "asociadaAOrden")
	private Integer asociadaAOrden;
	
	@Column(name="canMinDes")
	private Double canMinDes;
	
	@Column(name="canMinHas")
	private Double canMinHas;

	// Constructores
	public TipoMulta(){
		super();
	}
	
	public TipoMulta(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoMulta getById(Long id) {
		return (TipoMulta) GdeDAOFactory.getTipoMultaDAO().getById(id);
	}
	
	public static TipoMulta getByIdNull(Long id) {
		return (TipoMulta) GdeDAOFactory.getTipoMultaDAO().getByIdNull(id);
	}
	
	public static List<TipoMulta> getList() {
		return (ArrayList<TipoMulta>) GdeDAOFactory.getTipoMultaDAO().getList();
	}
	
	public static List<TipoMulta> getListActivos() {			
		return (ArrayList<TipoMulta>) GdeDAOFactory.getTipoMultaDAO().getListActiva();
	}
	
	public static List<TipoMulta> getListActivosByIdRecurso(Long idRecurso) {			
		return (ArrayList<TipoMulta>) GdeDAOFactory.getTipoMultaDAO().getListActivosByIdRecurso(idRecurso);
	}
	
	// Getters y setters
	public String getDesTipoMulta() {
		return desTipoMulta;
	}

	public void setDesTipoMulta(String desTipoMulta) {
		this.desTipoMulta = desTipoMulta;
	}
	
	public Integer getEsImporteManual() {
		return esImporteManual;
	}

	public void setEsImporteManual(Integer esImporteManual) {
		this.esImporteManual = esImporteManual;
	}
	
	public RecClaDeu getRecClaDeu() {
		return recClaDeu;
	}

	public void setRecClaDeu(RecClaDeu recClaDeu) {
		this.recClaDeu = recClaDeu;
	}
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Integer getAsociadaAOrden() {
		return asociadaAOrden;
	}

	public void setAsociadaAOrden(Integer asociadaAOrden) {
		this.asociadaAOrden = asociadaAOrden;
	}
	
	public Double getCanMinDes() {
		return canMinDes;
	}

	public void setCanMinDes(Double canMinDes) {
		this.canMinDes = canMinDes;
	}
	

	public Double getCanMinHas() {
		return canMinHas;
	}

	public void setCanMinHas(Double canMinHas) {
		this.canMinHas = canMinHas;
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
	
		
		if (GenericDAO.hasReference(this, Multa.class, "tipoMulta")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.TIPOMULTA_LABEL, GdeError.MULTA_LABEL );
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		/*if (StringUtil.isNullOrEmpty(getCod${Bean}())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.${BEAN}_COD${BEAN} );
		}*/
		
		if (StringUtil.isNullOrEmpty(getDesTipoMulta())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPOMULTA_DESTIPOMULTA);
		}
		
		if (getEsImporteManual()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPOMULTA_ESIMPORTEMANUAL);
		}
		
		if (getRecClaDeu()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPOMULTA_RECCLADEU);
		}
		
		if (getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPOMULTA_RECURSO);
		}

		if (getAsociadaAOrden()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPOMULTA_ASOCIADAAORDEN);
		}
		
		if (hasError()) {
			return false;
		}
				
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoMulta. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getTipoMultaDAO().update(this);
	}

	/**
	 * Desactiva el TipoMulta. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getTipoMultaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoMulta
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	/**
	 * Valida la desactivacion del TipoMulta
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
