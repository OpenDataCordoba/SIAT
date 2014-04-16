//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.buss.dao.ExeDAOFactory;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.exe.iface.model.TipSujExeVO;
import ar.gov.rosario.siat.exe.iface.model.TipoSujetoVO;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipoSujeto
 * 
 * @author tecso
 */
@Entity
@Table(name = "exe_TipoSujeto")
public class TipoSujeto extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_JUBILADO_PROPIETARIO = 14L; 
	public static final Long ID_JUBILADO_INQUILINO = 44L;
	public static final Long ID_NOVEDADES = 108L;

	public static final String COD_JUBILADO_PROPIETARIO = "14"; 
	public static final String COD_JUBILADO_INQUILINO = "44";

	
	@Column(name = "desTipoSujeto")
	private String desTipoSujeto;
	
	@Column(name = "codTipoSujeto")
	private String codTipoSujeto;
	
	@OneToMany()
	@JoinColumn(name="idTipoSujeto") 
	private List<TipSujExe> listTipSujExe; 


	//<#Propiedades#>
	
	// Constructores
	public TipoSujeto(){
		super();
		// Seteo de valores default			
	}
	
	
	public TipoSujeto(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoSujeto getById(Long id) {
		return (TipoSujeto) ExeDAOFactory.getTipoSujetoDAO().getById(id);
	}
	
	public static TipoSujeto getByIdNull(Long id) {
		return (TipoSujeto) ExeDAOFactory.getTipoSujetoDAO().getByIdNull(id);
	}
	
	public static List<TipoSujeto> getList() {
		return (List<TipoSujeto>) ExeDAOFactory.getTipoSujetoDAO().getList();
	}
	
	public static List<TipoSujeto> getListActivos() {			
		return (List<TipoSujeto>) ExeDAOFactory.getTipoSujetoDAO().getListActiva();
	}
	
	
	public static List<TipoSujeto> getListByExencion(Exencion exencion) {			
		return (List<TipoSujeto>) ExeDAOFactory.getTipoSujetoDAO().getListByExencion(exencion);
	}
	
	// Getters y setters
	
	public String getDesTipoSujeto() {
		return desTipoSujeto;
	}

	public void setDesTipoSujeto(String desTipoSujeto) {
		this.desTipoSujeto = desTipoSujeto;
	}

	public String getCodTipoSujeto() {
		return codTipoSujeto;
	}

	public void setCodTipoSujeto(String codTipoSujeto) {
		this.codTipoSujeto = codTipoSujeto;
	}

	public List<TipSujExe> getListTipSujExe() {
		return listTipSujExe;
	}

	public void setListTipSujExe(List<TipSujExe> listTipSujExe) {
		this.listTipSujExe = listTipSujExe;
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
	
		//<#ValidateDelete#>
		if(ExeDAOFactory.getTipoSujetoDAO().hasReferenceGen(this, TipSujExe.class, "tipoSujeto"))
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, ExeError.TIPOSUJETO_LABEL ,
					ExeError.EXENCION_LABEL);

		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodTipoSujeto())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.TIPOSUJETO_CODTIPOSUJETO );
		}
		
		if (StringUtil.isNullOrEmpty(getDesTipoSujeto())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.TIPOSUJETO_DESTIPOSUJETO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codTipoSujeto");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, ExeError.TIPOSUJETO_CODTIPOSUJETO);			
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoSujeto. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		ExeDAOFactory.getTipoSujetoDAO().update(this);
	}

	/**
	 * Desactiva el TipoSujeto. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		ExeDAOFactory.getTipoSujetoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoSujeto
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;	
	}
	
	/**
	 * Valida la desactivacion del TipoSujeto
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	
	// ---> ABM Tipo Sujeto
	public TipoSujeto createTipoSujeto(TipoSujeto tipoSujeto) throws Exception {

		// Validaciones de negocio
		if (!tipoSujeto.validateCreate()) {
			return tipoSujeto;
		}

		ExeDAOFactory.getTipoSujetoDAO().update(tipoSujeto);

		return tipoSujeto;
	}
	
	public TipoSujeto updateTipoSujeto(TipoSujeto tipoSujeto) throws Exception {
		
		// Validaciones de negocio
		if (!tipoSujeto.validateUpdate()) {
			return tipoSujeto;
		}

		ExeDAOFactory.getTipoSujetoDAO().update(tipoSujeto);
		
		return tipoSujeto;
	}
	
	public TipoSujeto deleteTipoSujeto(TipoSujeto tipoSujeto) throws Exception {
	
		// Validaciones de negocio
		if (!tipoSujeto.validateDelete()) {
			return tipoSujeto;
		}
		
		ExeDAOFactory.getTipoSujetoDAO().delete(tipoSujeto);
		
		return tipoSujeto;
	}
	// <--- ABM Tipo Sujeto
	
	// ---> ABM TipSujExe
	public TipSujExe createTipSujExe(TipSujExe tipSujExe) throws Exception {

		// Validaciones de negocio
		if (!tipSujExe.validateCreate()) {
			return tipSujExe;
		}

		ExeDAOFactory.getTipSujExeDAO().update(tipSujExe);

		return tipSujExe;
	}
	
	public TipSujExe updateTipSujExe(TipSujExe tipSujExe) throws Exception {
		
		// Validaciones de negocio
		if (!tipSujExe.validateUpdate()) {
			return tipSujExe;
		}

		ExeDAOFactory.getTipoSujetoDAO().update(tipSujExe);
		
		return tipSujExe;
	}
	
	public TipSujExe deleteTipSujExe(TipSujExe tipSujExe) throws Exception {
	
		// Validaciones de negocio
		if (!tipSujExe.validateDelete()) {
			return tipSujExe;
		}
		
		ExeDAOFactory.getTipSujExeDAO().delete(tipSujExe);
		
		return tipSujExe;
	}
	// <--- ABM TipSujExe


	public TipoSujetoVO toVOforView() throws Exception {
		TipoSujetoVO tipoSujetoVO = (TipoSujetoVO) this.toVO(0, false);
		List<TipSujExeVO> listTipSujExeVO = new ArrayList<TipSujExeVO>();		
		if(listTipSujExe!=null){
			for(TipSujExe tipSujExe: listTipSujExe){
				TipSujExeVO tipSujExeVO = (TipSujExeVO) tipSujExe.toVO(0, false);
				ExencionVO exencionVO = (ExencionVO) tipSujExe.getExencion().toVO(0, false);
				exencionVO.setRecurso((RecursoVO) tipSujExe.getExencion().getRecurso().toVO(0, false));
				tipSujExeVO.setExencion(exencionVO);
				listTipSujExeVO.add(tipSujExeVO);
			}
			tipoSujetoVO.setListTipSujExe(listTipSujExeVO);
		}
		return tipoSujetoVO;
	}

	
	
	//<#MetodosBeanDetalle#>
}
