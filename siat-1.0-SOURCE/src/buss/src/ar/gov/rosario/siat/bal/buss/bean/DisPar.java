//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Distribuidor de Partida (DisPar)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_disPar")
public class DisPar extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "desDisPar")
	private String desDisPar;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso") 
	private Recurso recurso;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoImporte") 
	private TipoImporte tipoImporte;
	
	// Listas de Entidades Relacionadas con DisPar
	@OneToMany()
	@JoinColumn(name="idDisPar")
	@OrderBy(clause="idTipoImporte")
	private List<DisParDet> listDisParDet;

	@OneToMany()
	@JoinColumn(name="idDisPar")
	@OrderBy(clause="fechaDesde")
	private List<DisParRec> listDisParRec;

	@OneToMany()
	@JoinColumn(name="idDisPar")
	@OrderBy(clause="fechaDesde")
	private List<DisParPla> listDisParPla;

	@Transient
	private List<DisParDet> listDisParDetVigentes;

	//Constructores 
	public DisPar(){
		super();
	}

	// Getters y Setters
	public List<DisParDet> getListDisParDetVigentes() {
		return listDisParDetVigentes;
	}
	
	public void setListDisParDetVigentes(List<DisParDet> listDisParDetVigentes) {
		this.listDisParDetVigentes = listDisParDetVigentes;
	}
	public String getDesDisPar() {
		return desDisPar;
	}
	public void setDesDisPar(String desDisPar) {
		this.desDisPar = desDisPar;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public List<DisParDet> getListDisParDet() {
		return listDisParDet;
	}
	public void setListDisParDet(List<DisParDet> listDisParDet) {
		this.listDisParDet = listDisParDet;
	}
	public List<DisParPla> getListDisParPla() {
		return listDisParPla;
	}
	public void setListDisParPla(List<DisParPla> listDisParPla) {
		this.listDisParPla = listDisParPla;
	}
	public List<DisParRec> getListDisParRec() {
		return listDisParRec;
	}
	public void setListDisParRec(List<DisParRec> listDisParRec) {
		this.listDisParRec = listDisParRec;
	}
	public TipoImporte getTipoImporte() {
		return tipoImporte;
	}
	public void setTipoImporte(TipoImporte tipoImporte) {
		this.tipoImporte = tipoImporte;
	}

	// Metodos de clase	
	public static DisPar getById(Long id) {
		return (DisPar) BalDAOFactory.getDisParDAO().getById(id);
	}
	
	public static DisPar getByIdNull(Long id) {
		return (DisPar) BalDAOFactory.getDisParDAO().getByIdNull(id);
	}
	
	public static DisPar getByCodRecursoYidTipoImporte(String codRecurso,Long  idTipoImporte) throws Exception {
		return (DisPar) BalDAOFactory.getDisParDAO().getByCodRecursoYidTipoImporte(codRecurso,idTipoImporte);
	}
	
	public static List<DisPar> getList() {
		return (ArrayList<DisPar>) BalDAOFactory.getDisParDAO().getList();
	}
	
	public static List<DisPar> getListActivos() {			
		return (ArrayList<DisPar>) BalDAOFactory.getDisParDAO().getListActiva();
	}
	
	public static List<DisPar> getListActivosGenericos() throws Exception{			
		return (ArrayList<DisPar>) BalDAOFactory.getDisParDAO().getListActivaGenericos();
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

		// TODO cambiar mensaje de error por otro mas especifico y claro
		if(this.getTipoImporte() != null && DisPar.getByCodRecursoYidTipoImporte(this.getRecurso().getCodRecurso(),this.getTipoImporte().getId())!=null){
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, BalError.DISPAR_TIPOIMPORTE);
		}
		
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
		if(StringUtil.isNullOrEmpty(getDesDisPar())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_DESDISPAR);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_RECURSO);
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
		
		if (GenericDAO.hasReference(this, DisParDet.class, "disPar")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.DISPAR_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, DisParRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, DisParPla.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARPLA_LABEL);
		}
		
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	/**
	 * Activa la DisPar. Previamente valida la activacion 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getDisParDAO().update(this);
	}

	/**
	 * Desactiva la DisPar. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getDisParDAO().update(this);
	}
	
	/**
	 * Valida la activacion de la DisPar
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion de la DisPar
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	// Administracion de DisParDet
	public DisParDet createDisParDet(DisParDet disParDet) throws Exception {
		
		// Validaciones de negocio
		if (!disParDet.validateCreate()) {
			return disParDet;
		}

		BalDAOFactory.getDisParDetDAO().update(disParDet);
		
		return disParDet;
	}	

	public DisParDet updateDisParDet(DisParDet disParDet) throws Exception {
		
		// Validaciones de negocio
		if (!disParDet.validateUpdate()) {
			return disParDet;
		}

		BalDAOFactory.getDisParDetDAO().update(disParDet);
		
		return disParDet;
	}	

	public DisParDet deleteDisParDet(DisParDet disParDet) throws Exception {
		
		// Validaciones de negocio
		if (!disParDet.validateDelete()) {
			return disParDet;
		}
				
		BalDAOFactory.getDisParDetDAO().delete(disParDet);
		
		return disParDet;
	}	
	
	// Administracion de DisParRec
	public DisParRec createDisParRec(DisParRec disParRec) throws Exception {

		// Validaciones de negocio
		if (!disParRec.validateCreate()) {
			return disParRec;
		}

		BalDAOFactory.getDisParRecDAO().update(disParRec);

		return disParRec;
	}
	
	public DisParRec updateDisParRec(DisParRec disParRec) throws Exception {
		
		// Validaciones de negocio
		if (!disParRec.validateUpdate()) {
			return disParRec;
		}
		
		BalDAOFactory.getDisParRecDAO().update(disParRec);
		
	    return disParRec;
	}
	
	public DisParRec deleteDisParRec(DisParRec disParRec) throws Exception {

		// Validaciones de negocio
		if (!disParRec.validateDelete()) {
			return disParRec;
		}
		
		BalDAOFactory.getDisParRecDAO().delete(disParRec);
		
		return disParRec;
	}

	// Administracion de DisParPla
	public DisParPla createDisParPla(DisParPla disParPla) throws Exception {

		// Validaciones de negocio
		if (!disParPla.validateCreate()) {
			return disParPla;
		}

		BalDAOFactory.getDisParPlaDAO().update(disParPla);

		return disParPla;
	}
	
	public DisParPla updateDisParPla(DisParPla disParPla) throws Exception {
		
		// Validaciones de negocio
		if (!disParPla.validateUpdate()) {
			return disParPla;
		}
		
		BalDAOFactory.getDisParPlaDAO().update(disParPla);
		
	    return disParPla;
	}
	
	public DisParPla deleteDisParPla(DisParPla disParPla) throws Exception {

		// Validaciones de negocio
		if (!disParPla.validateDelete()) {
			return disParPla;
		}
		
		BalDAOFactory.getDisParPlaDAO().delete(disParPla);
		
		return disParPla;
	}
	//	 <--- ABM DisParPla
		
	/**
	 * Obtiene la lista de DisParDet ordenada por Tipo de importe y descripcion del RecCon
	 * @return List<DisParDet>
	 */
	public List<DisParDet> getListDisParDetOrderByTipoImpDesRecCon(){
		
		List<DisParDet> listDisParDetOrdenada = new ArrayList<DisParDet>();
		
		// uso un TreeMap para ordenar las claves. Aporte de un groso: Dennis
		TreeMap mapDisParDet = new TreeMap();
		for (DisParDet dpd : this.getListDisParDet()) {
			// armado de la clave: desTipoImporte + desRecCon + id
			String tipoImporte = dpd.getTipoImporte().getDesTipoImporte(); // requerido
			String desRecCon = " ";
			if(dpd.getRecCon() != null){
				desRecCon = dpd.getRecCon().getDesRecCon();	
			}
			Long id = dpd.getId();
			
			String clave = tipoImporte + desRecCon + id;
			mapDisParDet.put(clave, dpd);
		}
		
		// recorro las claves ordenadas por el treeMap. Aporte de Dennis
		for (Object clave : mapDisParDet.keySet()) {
			listDisParDetOrdenada.add( (DisParDet) mapDisParDet.get(clave));
		}
		
		return listDisParDetOrdenada;
	}
	
	/**
	 * Carga la lista de Detalles del Distribuidor de Partida con los vigentes a la fecha de hoy.
	 *
	 */
	public void loadListDisParDetVigente(){
		// Inicializamos la lista de DisParDet Vigentes
		this.listDisParDetVigentes = new ArrayList<DisParDet>();
		// Pasamos a la lista de vigentes los vigentes a la fecha
		for (DisParDet disParDet : this.getListDisParDet()) {
			if (DateUtil.isDateBeforeOrEqual(disParDet.getFechaDesde(), new Date()) && 
					(disParDet.getFechaHasta() == null 	
							||	DateUtil.isDateBeforeOrEqual(new Date(), disParDet.getFechaHasta()))){
				this.listDisParDetVigentes.add(disParDet);
			}					
		}
	}
	
	/**
	 *  Filtra la lista de DisParDet Vigentes por TipoImporte y RecCon
	 * 
	 * @param idTipoImporte
	 * @param recCon
	 * @return
	 * @throws Exception
	 */
	public List<DisParDet> getListDisParDetByidTipoImporteYRecCon(Long idTipoImporte,Long idRecCon) throws Exception {

		List<DisParDet> listRet = new ArrayList<DisParDet>();
		// Busca entre la lista por Tipo Importe y RecCon
		for (DisParDet disParDet : this.getListDisParDetVigentes() ) {
			if(idRecCon == null) {
				// No se informa el idRecCon
				// Se busca por TipoImporte con RecCon == null
				if (  disParDet.getTipoImporte().getId().longValue()==idTipoImporte.longValue() &&
						disParDet.getRecCon() == null ) {
						// Se encuentra DisParDet para tipoImporte con recCon = null
						listRet.add(disParDet);
				}
			} else {
				// Se informa el idRecCon
				// Se verifica por TipoImporte-RecCon
				if (  disParDet.getTipoImporte().getId().longValue()==idTipoImporte.longValue() &&
						disParDet.getRecCon() != null && disParDet.getRecCon().getId().longValue()==idRecCon.longValue() ) {
						// Se encuentra DisParDet para tipoImporte y recCon
						listRet.add(disParDet);
				}
			}
		}
		return listRet;
	}
	
}
