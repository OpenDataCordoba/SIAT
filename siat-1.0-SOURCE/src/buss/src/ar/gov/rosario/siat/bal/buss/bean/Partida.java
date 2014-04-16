//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Partida
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_partida")
public class Partida extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "codPartida")
	private String codPartida;
	
	@Column(name = "desPartida")
	private String desPartida;
	
	@Column(name = "preEjeAct")
	private String preEjeAct;
	
	@Column(name = "preEjeVen")
	private String preEjeVen;
	
	@Column(name = "esActual")
	private Integer esActual;
	
	@OneToMany(mappedBy="partida")
	@JoinColumn(name="idPartida")
	private List<ParCueBan> listParCueBan; // Es la lista de Cuentas de la Partida
	
	
	//Constructores 
	
	public Partida(){
		super();
	}

	// Getters Y Setters
	
	public String getCodPartida() {
		return codPartida;
	}
	public void setCodPartida(String codPartida) {
		this.codPartida = codPartida;
	}
	public String getDesPartida() {
		return desPartida;
	}
	public void setDesPartida(String desPartida) {
		this.desPartida = desPartida;
	}
	
	public String getPreEjeAct() {
		return preEjeAct;
	}

	public void setPreEjeAct(String preEjeAct) {
		this.preEjeAct = preEjeAct;
	}

	public String getPreEjeVen() {
		return preEjeVen;
	}

	public void setPreEjeVen(String preEjeVen) {
		this.preEjeVen = preEjeVen;
	}

	public Integer getEsActual() {
		return esActual;
	}
	public void setEsActual(Integer esActual) {
		this.esActual = esActual;
	}
	
	public List<ParCueBan> getListParCueBan() {
		return listParCueBan;
	}

	public void setListParCueBan(List<ParCueBan> listParCueBan) {
		this.listParCueBan = listParCueBan;
	}
	// Metodos de clase	
	public static Partida getById(Long id) {
		return (Partida) BalDAOFactory.getPartidaDAO().getById(id);
	}
	
	public static Partida getByIdNull(Long id) {
		return (Partida) BalDAOFactory.getPartidaDAO().getByIdNull(id);
	}
	
	public static Partida getByCod(String codPartida){
		return (Partida) BalDAOFactory.getPartidaDAO().getByCod(codPartida);
	}
	
	public static List<Partida> getList() {
		return (ArrayList<Partida>) BalDAOFactory.getPartidaDAO().getList();
	}
	
	public static List<Partida> getListActivos() {			
		return (ArrayList<Partida>) BalDAOFactory.getPartidaDAO().getListActiva();
	}
	
	/**
	 *  Devuelve la lista de Partidas ordenadas por codPartida desde la base de datos.
	 *  (Se ordena por codigo considerandolo como string, por lo tanto para codigo numericos sin los ceros a la izquierda queda desordenado)
	 * 
	 * @return
	 */
	public static List<Partida> getListActivaOrdenadasPorCodigo() {			
		return (ArrayList<Partida>) BalDAOFactory.getPartidaDAO().getListActivaOrdenadasPorCodigo();
	}
	
	/**
	 *  Devuelve la lista de Partidas ordenadas por codPartida preprocesada en Java.
	 *  (Se ordena por codigo completando a 6 caracteres con ceros a la izquierda)
	 * 
	 * @return
	 */
	public static List<Partida> getListActivaOrdenadasPorCodigoEsp() {			
		List<Partida> listPartida = (ArrayList<Partida>) BalDAOFactory.getPartidaDAO().getListActivaOrdenadasPorCodigo(); 
		listPartida = Partida.ordenarListaPartida(listPartida);
		return listPartida;
	}
	
	public static List<Partida> getListActivosActuales() {			
		return (ArrayList<Partida>) BalDAOFactory.getPartidaDAO().getListActivaActual();
	}

	public static List<Partida> getListActivosNoActuales() {			
		return (ArrayList<Partida>) BalDAOFactory.getPartidaDAO().getListActivaNoActual();
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
		//Validaciones de Negocio
				
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
		
		if (StringUtil.isNullOrEmpty(getCodPartida())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARTIDA_CODPARTIDA);
		}
		
		if (StringUtil.isNullOrEmpty(getDesPartida())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARTIDA_DESPARTIDA);
		}
		
		if (getEsActual() == null ||getEsActual() == 0) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARTIDA_ESACTUAL);
		}
			
		if (StringUtil.isNullOrEmpty(getPreEjeAct())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARTIDA_PREEJEACT);
		}
		
		if (StringUtil.isNullOrEmpty(getPreEjeVen())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARTIDA_PREEJEVEN);
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
		
		//Validaciones de VO
		if ((GenericDAO.hasReference(this, AuxRecaudado.class, "partida"))||
			(GenericDAO.hasReference(this, AuxSellado.class, "partida"))||
			(GenericDAO.hasReference(this, SinIndet.class, "partida"))||
			(GenericDAO.hasReference(this, SinPartida.class, "partida"))) {
			addRecoverableError(BalError.ASENTAMIENTO_AUXILIAR_LABEL);
		}
		if (GenericDAO.hasReference(this, DisParDet.class, "partida")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.PARTIDA_LABEL , BalError.DISPARDET_LABEL);
		}
		
		// Valida si tiene referencia a supervisores
		if(GenericDAO.hasReference(this, ParCueBan.class, "partida")){
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.PARTIDA_LABEL , BalError.PARCUEBAN_LABEL);
		}

		if (GenericDAO.hasReference(this, ParSel.class, "partida")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.PARTIDA_LABEL , BalError.PARSEL_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
	public String getCodDesPartida(){
		return StringUtil.concat(this.getCodPartida(), this.getDesPartida());
	}
	
	/**
	 * Activa la Partida. Previamente valida la activacion.
	 * 
	 */
	public void activar() {
		if (!this.validateActivar()) {
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getPartidaDAO().update(this);
	}

	/**
	 * Desactiva la Partida. Previamente valida la desactivacion.
	 * 
	 */
	public void desactivar() {
		if (!this.validateDesactivar()) {
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getPartidaDAO().update(this);
	}

	/**
	 * Valida la activacion de la Partida
	 * 
	 * @return boolean
	 */
	private boolean validateActivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 * Valida la desactivacion de la Partida
	 * 
	 * @return boolean
	 */
	private boolean validateDesactivar() {
		// limpiamos la lista de errores
		clearError();

		// Validaciones
		return true;
	}

	/**
	 *  Devuelve la Cuenta Bancaria vigente a la fecha pasada.
	 * @return
	 */
	public CuentaBanco getCuentaBancoVigente(Date fecha) throws Exception{
		return (CuentaBanco) BalDAOFactory.getParCueBanDAO().getVigenteByIdPartidaYFecha(this.getId(), fecha);
	}

	/**
	 *  Ordenar lista de Partida pasada
	 * 
	 * @param listPartida
	 * @return
	 */
	public static List<Partida> ordenarListaPartida(List<Partida> listPartida){
    	Comparator<Partida> comparator = new Comparator<Partida>(){
			public int compare(Partida partida1, Partida partida2) {
				String codPartidaFila1 = StringUtil.completarCerosIzq(partida1.getCodPartida().trim(), 6);
				String codPartidaFila2 = StringUtil.completarCerosIzq(partida2.getCodPartida().trim(), 6);
				return codPartidaFila1.compareTo(codPartidaFila2);
			}    		
    	};    	
    	Collections.sort(listPartida, comparator);
    	return listPartida;
    }

}
