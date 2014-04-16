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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuVO;
import ar.gov.rosario.siat.gde.iface.model.EstadoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudDeuVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a GesJudDeu
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_gesJudDeu")
public class GesJudDeu extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idGesJud") 
	private GesJud gesJud;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idConstanciaDeu") 
	private ConstanciaDeu constanciaDeu;
	
	@Column(name = "idDeuda")
    private Long idDeuda;
	
	@Column(name = "observacion")
	private String observacion;
	

	
	// Constructores
	public GesJudDeu(){
		super();
	}
	
	public GesJudDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static GesJudDeu getById(Long id) {
		GesJudDeu gesJudDeu = (GesJudDeu) GdeDAOFactory.getGesJudDeuDAO().getById(id);
		gesJudDeu.getGesJud().setTipoJuzgado(TipoJuzgado.getBycodigo(String.valueOf(gesJudDeu.getGesJud().getCodTipoJuzgado())));
		return gesJudDeu;
	}
	
	public static GesJudDeu getByIdNull(Long id) {
		return (GesJudDeu) GdeDAOFactory.getGesJudDeuDAO().getByIdNull(id);
	}
	
	public static List<GesJudDeu> getList() {
		return (ArrayList<GesJudDeu>) GdeDAOFactory.getGesJudDeuDAO().getList();
	}
	
	public static List<GesJudDeu> getListActivos() {			
		return (ArrayList<GesJudDeu>) GdeDAOFactory.getGesJudDeuDAO().getListActiva();
	}
			
	/**
	 * Busca un GesJudDeu por idDeuda
	 * @param idDeuda
	 * @return null si no encuentra nada o si el parametro es nulo
	 */
	public static GesJudDeu getByIdDeuda(Long idDeuda){
		return GdeDAOFactory.getGesJudDeuDAO().getByIdDeuda(idDeuda);
	}

	/**
	 * Busca una lista de GesJudDeu por Constancia de Deuda
	 * @param Constancia
	 * @return null si no encuentra nada o si el parametro es nulo
	 */
	public static List<GesJudDeu> getListByIdConstancia(Long idConstanciaDeu){
		return GdeDAOFactory.getGesJudDeuDAO().getListByIdConstancia(idConstanciaDeu);
	}
	
	/**
	 * Hace el toVO(0, false), carga la gesJud con toVOForABM(1, false). Carga la constanciaDeu con toVOLight() y setea la deudaJudicialVO con el forViewSinConceptos()
	 * @return
	 * @throws Exception
	 */
	public GesJudDeuVO toVOForABM() throws Exception{
		GesJudDeuVO gesJudDeuVO = (GesJudDeuVO) this.toVO(0, false);
		gesJudDeuVO.setGesJud(getGesJud().toVOForABM(1, false));
		if(getConstanciaDeu()!=null)
			gesJudDeuVO.setConstanciaDeu(getConstanciaDeu().toVOLight());
		
		Deuda deuda = Deuda.getById(idDeuda);
		gesJudDeuVO.setDeuda(deuda.toVOForViewSinConceptos());		
		return gesJudDeuVO;
	}
	
	/**
	 * Hace el toVO(0, false), carga la gesJud con toVOForABM(1, false). Carga el procurador de la gesjud, carga la constanciaDeu con toVOLight() y setea la deudaJudicialVO con el forViewSinConceptos()
	 * @return
	 * @throws Exception
	 */
	public GesJudDeuVO toVOForABMwithProcu() throws Exception{
		GesJudDeuVO gesJudDeuVO = (GesJudDeuVO) this.toVO(0, false);
		gesJudDeuVO.setGesJud(getGesJud().toVOForABM(1, false));
		gesJudDeuVO.getGesJud().setProcurador((ProcuradorVO) gesJud.getProcurador().toVO(0, false));
		if(getConstanciaDeu()!=null)
			gesJudDeuVO.setConstanciaDeu(getConstanciaDeu().toVOLight());
		
		Deuda deuda = Deuda.getById(idDeuda);
		gesJudDeuVO.setDeuda(deuda.toVOForViewSinConceptos());
		gesJudDeuVO.getDeuda().setEstadoDeuda((EstadoDeudaVO) deuda.getEstadoDeuda().toVO(0, false));
		return gesJudDeuVO;
	}
	
	/**
	 * Hace el toVO(0, false), carga la gesJud con toVOForABM(0, false). Carga la constanciaDeu con toVO(0, false) y setea la deudaJudicialVO con el forViewSinConceptos()
	 * @return
	 * @throws Exception
	 */
	public GesJudDeuVO toVOLight() throws Exception{
		GesJudDeuVO gesJudDeuVO = (GesJudDeuVO) this.toVO(0, false);
		gesJudDeuVO.setGesJud(getGesJud().toVOForABM(0, false));
		if(getConstanciaDeu()!=null)
			gesJudDeuVO.setConstanciaDeu((ConstanciaDeuVO) getConstanciaDeu().toVO(0, false));
		
		Deuda deuda = Deuda.getById(idDeuda);
		gesJudDeuVO.setDeuda(deuda.toVOForViewSinConceptos());
		gesJudDeuVO.getDeuda().setCuenta((CuentaVO) deuda.getCuenta().toVO(0, false));
		return gesJudDeuVO;
	}
	
	// Getters y setters
	public ConstanciaDeu getConstanciaDeu() {
		return constanciaDeu;
	}

	public void setConstanciaDeu(ConstanciaDeu constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}

	public GesJud getGesJud() {
		return gesJud;
	}

	public void setGesJud(GesJud gesJud) {
		this.gesJud = gesJud;
	}

	public Long getIdDeuda() {
		return idDeuda;
	}

	public void setIdDeuda(Long idDeuda) {
		this.idDeuda = idDeuda;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
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
	
		// Valida que la gestion judicial no tenga ningun evento registrado, para poder eliminar una deuda
		if(getGesJud().getListGesJudEvento()!=null && !getGesJud().getListGesJudEvento().isEmpty())
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.GESJUDDEU_LABEL, GdeError.GESJUDEVENTO_LABEL );
		
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
		
		// Validaciones de unique
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Hace Deuda.getById() con el idDeuda que posee.
	 */
	public Deuda getDeuda(){
		return Deuda.getById(idDeuda);
	}
	
	/**
	 * Activa el GesJudDeu. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getGesJudDeuDAO().update(this);
	}

	/**
	 * Desactiva el GesJudDeu. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getGesJudDeuDAO().update(this);
	}
	
	/**
	 * Valida la activacion del GesJudDeu
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del GesJudDeu
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
