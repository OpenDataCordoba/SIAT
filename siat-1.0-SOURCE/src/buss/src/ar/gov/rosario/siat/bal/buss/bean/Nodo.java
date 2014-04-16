//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.RangoFechaVO;

/**
 * Representan los distintos niveles de la clasificación. Se guardan los nodo del árbol de clasificación. 
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_nodo")
public class Nodo extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Transient
	private Logger log = Logger.getLogger(Nodo.class);	
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idClasificador") 
	private Clasificador clasificador; 

	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "nivel")
	private Integer nivel;
	
	@ManyToOne(optional=true) 
    @JoinColumn(name="idNodoPadre") 
	private Nodo nodoPadre;

	@Transient
	private Double total = 0D;

	@Transient
	private Double totalEjeAct = 0D;

	@Transient
	private Double totalEjeVen = 0D;

	@Transient
	private List<Nodo> listNodoHijoForTree;
	
	@Transient
	private Integer cantNivel; 
	
	@Transient
	private String clave;
	
	@Transient
	private String claveId;
	
	@Transient
	private Double totalCom = 0D;

	@Transient
	private Double totalEjeActCom = 0D;

	@Transient
	private Double totalEjeVenCom = 0D;
	
	@Transient
	private Double variacion = 0D;
	
	@Transient
	private List<Double> listTotalRango = new ArrayList<Double>();;
	
	@Transient
	private String forzarMostrar = "false";
	
	// Listas de Entidades Relacionadas con Nodo
	@OneToMany()
	@JoinColumn(name="idNodo")
	@OrderBy(clause="fechaDesde")
	private List<RelPartida> listRelPartida;

	@OneToMany()
	@JoinColumn(name="idNodo1")
	@OrderBy(clause="fechaDesde")
	private List<RelCla> listRelCla;

	@OneToMany()
	@JoinColumn(name="idNodoPadre")
	private List<Nodo> listNodoHijo;
	
	// Constructores
	public Nodo(){
		super();	
	}
	
	// Getters y Setters
	public Clasificador getClasificador() {
		return clasificador;
	}
	public void setClasificador(Clasificador clasificador) {
		this.clasificador = clasificador;
	}
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
	public Integer getNivel() {
		return nivel;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}
	public Nodo getNodoPadre() {
		return nodoPadre;
	}
	public void setNodoPadre(Nodo nodoPadre) {
		this.nodoPadre = nodoPadre;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public List<Nodo> getListNodoHijoForTree() {
		return listNodoHijoForTree;
	}
	public void setListNodoHijoForTree(List<Nodo> listNodoHijoForTree) {
		this.listNodoHijoForTree = listNodoHijoForTree;
	}
	public Integer getCantNivel() {
		return cantNivel;
	}
	public void setCantNivel(Integer cantNivel) {
		this.cantNivel = cantNivel;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getClaveId() {
		return claveId;
	}
	public void setClaveId(String claveId) {
		this.claveId = claveId;
	}
	public List<RelPartida> getListRelPartida() {
		return listRelPartida;
	}
	public void setListRelPartida(List<RelPartida> listRelPartida) {
		this.listRelPartida = listRelPartida;
	}
	public List<RelCla> getListRelCla() {
		return listRelCla;
	}
	public void setListRelCla(List<RelCla> listRelCla) {
		this.listRelCla = listRelCla;
	}
	public List<Nodo> getListNodoHijo() {
		return listNodoHijo;
	}
	public void setListNodoHijo(List<Nodo> listNodoHijo) {
		this.listNodoHijo = listNodoHijo;
	}
	public Double getTotalEjeAct() {
		return totalEjeAct;
	}
	public void setTotalEjeAct(Double totalEjeAct) {
		this.totalEjeAct = totalEjeAct;
	}
	public Double getTotalEjeVen() {
		return totalEjeVen;
	}
	public void setTotalEjeVen(Double totalEjeVen) {
		this.totalEjeVen = totalEjeVen;
	}
	public Double getTotalCom() {
		return totalCom;
	}
	public void setTotalCom(Double totalCom) {
		this.totalCom = totalCom;
	}
	public Double getTotalEjeActCom() {
		return totalEjeActCom;
	}
	public void setTotalEjeActCom(Double totalEjeActCom) {
		this.totalEjeActCom = totalEjeActCom;
	}
	public Double getTotalEjeVenCom() {
		return totalEjeVenCom;
	}
	public void setTotalEjeVenCom(Double totalEjeVenCom) {
		this.totalEjeVenCom = totalEjeVenCom;
	}
	public Double getVariacion() {
		return variacion;
	}
	public void setVariacion(Double variacion) {
		this.variacion = variacion;
	}
	public List<Double> getListTotalRango() {
		return listTotalRango;
	}
	public void setListTotalRango(List<Double> listTotalRango) {
		this.listTotalRango = listTotalRango;
	}
	public String getForzarMostrar() {
		return forzarMostrar;
	}
	public void setForzarMostrar(String forzarMostrar) {
		this.forzarMostrar = forzarMostrar;
	}

	public String obtenerDescripcionTab(int nivelDesde) {
		String descripcionTab = "";
		for(int c=nivelDesde; c<this.getNivel();c++)
			descripcionTab = descripcionTab.concat(" ");
		descripcionTab = descripcionTab.concat(this.getDescripcion());
		return descripcionTab;	
	}
	
	// Metodos de Clase
	public static Nodo getById(Long id) {
		return (Nodo) BalDAOFactory.getNodoDAO().getById(id);
	}
	
	public static Nodo getByIdNull(Long id) {
		return (Nodo) BalDAOFactory.getNodoDAO().getByIdNull(id);
	}
	
	/**
	 *  Obtiene un Nodo por id de Clasificador, Nivel y codigo indicados como parametros.
	 * 
	 * @param idClasificador
	 * @param nivel
	 * @param codigo
	 * @return
	 */
	public static Nodo getByIdClaNivelIdNodoPadreYCod(Long idClasificador, Integer nivel, Long idNodoPadre, String codigo) {
		return (Nodo) BalDAOFactory.getNodoDAO().getByIdClaNivelIdNodoPadreYCod(idClasificador, nivel, idNodoPadre, codigo);
	}
	
	public static List<Nodo> getList() {
		return (ArrayList<Nodo>) BalDAOFactory.getNodoDAO().getList();
	}
	
	public static List<Nodo> getListActivos() {			
		return (ArrayList<Nodo>) BalDAOFactory.getNodoDAO().getListActiva();
	}
	
	/**
	 *  Devuelve la lista de Nodos para el Nivel indicado
	 * 
	 * @param nivel
	 * @return
	 */
	public static List<Nodo> getListActivosByNivel(Integer nivel) {			
		return (ArrayList<Nodo>) BalDAOFactory.getNodoDAO().getListActivosByNivel(nivel);
	}
	
	/**
	 *  Devuelve la lista de Nodos para el Clasificador y Nivel indicados
	 * 
	 * @param clasificador
	 * @param nivel
	 * @return
	 */
	public static List<Nodo> getListActivosByClasificadorYNivel(Clasificador clasificador,Integer nivel) {			
		return (ArrayList<Nodo>) BalDAOFactory.getNodoDAO().getListActivosByClasificadorYNivel(clasificador,nivel);
	}
	
	/**
	 *  Devuelve la lista de Nodos Hojas (que no tienen hijos para el Clasificador)
	 * 
	 * @param clasificador
	 * @return
	 */
	public static List<Nodo> getListNodosHojasByClasificador(Clasificador clasificador) {			
		return (ArrayList<Nodo>) BalDAOFactory.getNodoDAO().getListNodosHojasByClasificador(clasificador);
	}
	
	/**
	 *  Devuelve la lista de Nodos para el Clasificador
	 * 
	 * @param clasificador
	 * @return
	 */
	public static List<Nodo> getListActivosByClasificador(Clasificador clasificador) {			
		return (ArrayList<Nodo>) BalDAOFactory.getNodoDAO().getListActivosByClasificador(clasificador);
	}
	
	public String getClave(){
		return this.clave;
	}
	
	/**
	 *  Calcula el Total de Importe y los totales de ejercicio actual y vencido entre las fechas indicadas.
	 * 
	 * @param fechaDesde
	 * @param fechaHasta
	 */
	public void calcularTotal(Date fechaDesde, Date fechaHasta){
		Double totalNodo = 0D;
		Double totVen = 0D;
		Double totAct = 0D;
		if(!ListUtil.isNullOrEmpty(this.getListNodoHijo())){
			for(Nodo nodoHijo: this.getListNodoHijo()){
				// calcular total
				nodoHijo.calcularTotal(fechaDesde, fechaHasta);
				totalNodo +=nodoHijo.getTotal();
				totVen += nodoHijo.getTotalEjeVen();
				totAct += nodoHijo.getTotalEjeAct();						
			}					
		}else{
			if(this.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue() && this.getNivel().intValue() == 6){
				List<Partida> listPartida = RelPartida.getListPartidaVigenteByIdNodo(this.getId(), fechaDesde); 
				for(Partida partida: listPartida){
					Object totales[] = ImpPar.getTotalActVenByIdPartidaYFechas(partida.getId(), fechaDesde, fechaHasta, null);
					if(totales != null){
						if(totales[0] != null)
							totVen += (Double) totales[0];
						if(totales[1] != null)
							totAct += (Double) totales[1];
						if(totales[2] != null)
							totalNodo += (Double) totales[2];
					}
				}
			}else if(this.getClasificador().getId().longValue() != Clasificador.ID_CLA_RUBRO.longValue()){ 
				List<Nodo> listNodoRel = RelCla.getListNodoByIdNodo(this.getId(), fechaDesde); 
				for(Nodo nodoRel: listNodoRel){
					nodoRel.calcularTotal(fechaDesde, fechaHasta);
					totalNodo +=nodoRel.getTotal();
					totVen += nodoRel.getTotalEjeVen();
					totAct += nodoRel.getTotalEjeAct();					
				}

			}
		}
		this.setTotal(NumberUtil.truncate(totalNodo,SiatParam.DEC_IMPORTE_VIEW));
		this.setTotalEjeVen(NumberUtil.truncate(totVen,SiatParam.DEC_IMPORTE_VIEW));
		this.setTotalEjeAct(NumberUtil.truncate(totAct,SiatParam.DEC_IMPORTE_VIEW));
	}
	
	/**
	 *  Calcula el Total de Importe para los distintos rango de fechas indicadas.
	 * 
	 * @param listRangoFecha
	 */
	public void calcularTotalEnRangos(List<RangoFechaVO> listRangoFecha){
		List<Double> listTotalRango = new ArrayList<Double>();
		for(RangoFechaVO rangoFecha: listRangoFecha){
			listTotalRango.add(Integer.valueOf(rangoFecha.getIndice()), 0D);
		}
		
		if(!ListUtil.isNullOrEmpty(this.getListNodoHijo())){
			for(Nodo nodoHijo: this.getListNodoHijo()){
				// calcular total
				nodoHijo.calcularTotalEnRangos(listRangoFecha);
				for(RangoFechaVO rangoFecha: listRangoFecha){
					Double totalNodoHijo = nodoHijo.getListTotalRango().get(Integer.valueOf(rangoFecha.getIndice()));
					listTotalRango.set(Integer.valueOf(rangoFecha.getIndice()),listTotalRango.get(Integer.valueOf(rangoFecha.getIndice()))+totalNodoHijo);
				}
			}					
		}else{
			if(this.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue() && this.getNivel().intValue() == 6){
				
				for(RangoFechaVO rangoFecha: listRangoFecha){
					String idRango = rangoFecha.getIndice();
					Date fechaDesde = rangoFecha.getFechaDesde();
					Date fechaHasta = rangoFecha.getFechaHasta();
					List<Partida> listPartida = RelPartida.getListPartidaVigenteByIdNodo(this.getId(), fechaDesde); 
					Double totalNodo = 0D;
					for(Partida partida: listPartida){
						Object totales[] = ImpPar.getTotalActVenByIdPartidaYFechas(partida.getId(), fechaDesde, fechaHasta, null);
						if(totales != null){
							if(totales[2] != null)
								totalNodo += (Double) totales[2];
						}
					}
					listTotalRango.set(Integer.valueOf(idRango),listTotalRango.get(Integer.valueOf(idRango))+totalNodo);
				}
			}else if(this.getClasificador().getId().longValue() != Clasificador.ID_CLA_RUBRO.longValue()){ 
				for(RangoFechaVO rangoFecha: listRangoFecha){
					String idRango = rangoFecha.getIndice();
					Date fechaDesde = rangoFecha.getFechaDesde();
					Date fechaHasta = rangoFecha.getFechaHasta();
					List<Nodo> listNodoRel = RelCla.getListNodoByIdNodo(this.getId(), fechaDesde); 
					for(Nodo nodoRel: listNodoRel){
						nodoRel.calcularTotal(fechaDesde, fechaHasta);
						listTotalRango.set(Integer.valueOf(idRango),listTotalRango.get(Integer.valueOf(idRango))+nodoRel.getTotal());
					}
				}
			}
		}
		for(Double totalRango: listTotalRango){
			totalRango = NumberUtil.truncate(totalRango,SiatParam.DEC_IMPORTE_VIEW);
		}
		this.setListTotalRango(listTotalRango);
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

		if(this.getClasificador().getId().longValue() != Clasificador.ID_CLA_RUBRO.longValue()
				&& this.getNodoPadre() != null && RelCla.existeRelClaVigenteForNodo(this.getNodoPadre().getId())){
			addRecoverableError(BalError.NODO_EXISTE_RELCLA);
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
		if(getClasificador() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.NODO_CLASIFICADOR);
		}
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.NODO_DESCRIPCION);
		}
		if(StringUtil.isNullOrEmpty(getCodigo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.NODO_CODIGO);
		}
		if(getNivel()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.NODO_NIVEL);
		}
		if(getNivel()!=null && getNivel()>1 && getNodoPadre() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.NODO_NODO_PADRE);
		}

		if (hasError()) {
			return false;
		}

		// Otras validaciones
		if(getNivel() < 1){
			addRecoverableError(BaseError.MSG_FUERA_DE_DOMINIO, BalError.NODO_NIVEL);
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
		if (GenericDAO.hasReference(this, Nodo.class, "nodoPadre")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.NODO_LABEL , BalError.NODO_LABEL);
		}

		if (GenericDAO.hasReference(this, RelPartida.class, "nodo")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.NODO_LABEL , BalError.RELPARTIDA_LABEL);
		}
		
		if (GenericDAO.hasReference(this, RelCla.class, "nodo1") ||
				GenericDAO.hasReference(this, RelCla.class, "nodo2")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.NODO_LABEL , BalError.RELCLA_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
	
	// Administrar RelCla (Relacion entre Cuentas)
	public RelCla createRelCla(RelCla relCla) throws Exception {
		
		// Validaciones de negocio
		if (!relCla.validateCreate()) {
			return relCla;
		}

		BalDAOFactory.getRelClaDAO().update(relCla);

		return relCla;
	}
	
	public RelCla updateRelCla(RelCla relCla) throws Exception {
		
		// Validaciones de negocio
		if (!relCla.validateUpdate()) {
			return relCla;
		}

		BalDAOFactory.getRelClaDAO().update(relCla);
		
		return relCla;
	}
	
	public RelCla deleteRelCla(RelCla relCla) throws Exception {
	
		// Validaciones de negocio
		if (!relCla.validateDelete()) {
			return relCla;
		}
		
		BalDAOFactory.getRelClaDAO().delete(relCla);
		
		return relCla;
	}

	// Administrar RelPartida (Relacion entre Cuentas)
	public RelPartida createRelPartida(RelPartida relPartida) throws Exception {
		
		// Validaciones de negocio
		if (!relPartida.validateCreate()) {
			return relPartida;
		}

		BalDAOFactory.getRelPartidaDAO().update(relPartida);

		return relPartida;
	}
	
	public RelPartida updateRelPartida(RelPartida relPartida) throws Exception {
		
		// Validaciones de negocio
		if (!relPartida.validateUpdate()) {
			return relPartida;
		}

		BalDAOFactory.getRelPartidaDAO().update(relPartida);
		
		return relPartida;
	}
	
	public RelPartida deleteRelPartida(RelPartida relPartida) throws Exception {
	
		// Validaciones de negocio
		if (!relPartida.validateDelete()) {
			return relPartida;
		}
		
		BalDAOFactory.getRelPartidaDAO().delete(relPartida);
		
		return relPartida;
	}
	
	/**
	 * Recorre la lista de Nodos y por cada Nodo padre lo agrega a la lista final y llama 
	 * a armar el arbol para el cual dicho nodo es raiz.
	 * 
	 * @param listNodo
	 * @return listNodoRaiz
	 */
	public static List<Nodo> obtenerListaArbol(List<Nodo> listNodo){
		List<Nodo> listNodoArbol = new ArrayList<Nodo>();
		for(Nodo nodo: listNodo){
			if(nodo.esNodoRaiz()){
				nodo.setClave(nodo.getCodigo());
				nodo.setClaveId(nodo.getId().toString());
				List<Nodo> listHijos = nodo.armarListaHijos(listNodo);
				nodo.completarClaveConCodigoCero();
				nodo.completarClaveIdConIdCero();
				listNodoArbol.add(nodo);
				listNodoArbol.addAll(listHijos);			
			}
		}
		return listNodoArbol;
	}
	
	/**
	 * Recorre la lista de Nodos. Arma el árbol tomando como raiz el nodo instanciado, 
	 * y guarda una lista con sus Nodos Hijos. Cada nodo hijo tiene una lista
	 * cargada con sus nodos Hijos hasta llegar a las hojas del arbol.
	 * 
	 * @param listNodo
	 */
	public List<Nodo> armarListaHijos(List<Nodo> listNodo){
		List<Nodo> listNodoHijo = new ArrayList<Nodo>();
		List<Nodo> listNodoRestantes = new ArrayList<Nodo>();
		listNodoRestantes.addAll(listNodo);
		for(Nodo nodo: listNodo){
			if(!nodo.esNodoRaiz() && nodo.getNodoPadre().getId().longValue() == this.getId().longValue()){
				nodo.setClave(nodo.getNodoPadre().getClave()+"."+nodo.getCodigo());
				nodo.setClaveId(nodo.getNodoPadre().getClaveId()+"."+nodo.getId());
				listNodoRestantes.remove(nodo);
				List<Nodo> listHijos = nodo.armarListaHijos(listNodoRestantes);
				listNodoRestantes.removeAll(listHijos);
				nodo.completarClaveConCodigoCero();
				nodo.completarClaveIdConIdCero();
				listNodoHijo.add(nodo);
				listNodoHijo.addAll(listHijos);
			}
		}
		return listNodoHijo;
	}
	/**
	 * Devuelve true si el Nodo es Raiz de arbol, o sea no tiene Padre. Caso contrario devuelve false.
	 * 
	 * @return true o false
	 */
	public boolean esNodoRaiz(){
		return (this.getNodoPadre() == null);
	}
	
	/**
	 *  Completar la clave con codigos "00" hasta llegar al nivel indicado en el clasificador.
	 * 
	 */
	public void completarClaveConCodigoCero(){
		Integer hastaNivel = this.getClasificador().getCantNivel();
		if(this.getNivel().intValue() < hastaNivel){
			for(int c=this.getNivel(); c<hastaNivel; c++ ){
				this.setClave(this.getClave()+".00");
			}
		}	
	}

	/**
	 *  Completar la clave de Ids con id "0" hasta llegar al nivel indicado en el clasificador.
	 * 
	 */
	public void completarClaveIdConIdCero(){
		Integer hastaNivel = this.getClasificador().getCantNivel();
		if(this.getNivel().intValue() < hastaNivel){
			for(int c=this.getNivel(); c<hastaNivel; c++ ){
				this.setClaveId(this.getClaveId()+".0");
			}
		}		
	}

	/**
	 * Arma la Clave recorriendo el arbol de forma ascendente y completando con ceros hasta el nivel del nodo
	 * @return
	 */
	public String obtenerClave(){
		this.armarClave();
		return this.clave;
	}
	
	/**
	 * Arma la Clave recorriendo el arbol de forma ascendente.
	 * @return
	 */
	public String armarClave(){
		if(this.esNodoRaiz()){
			this.setClave(this.getCodigo());
		}else{	
			this.setClave(this.getNodoPadre().armarClave()+"."+this.getCodigo());
		}
		String claveSinCeros = this.clave;
		this.completarClaveConCodigoCero();	
		return claveSinCeros;
	}
	
	
	/**
	 * Recorre la lista de Nodos y por cada Nodo padre lo agrega a la lista final y llama 
	 * a armar el arbol para el cual dicho nodo es raiz.
	 * 
	 * @param listNodo
	 * @return listNodoRaiz
	 */
	public static List<Nodo> obtenerListaArbolConTotales(List<Nodo> listNodo, Date fechaDesde, Date fechaHasta,List<Nodo> listNodoRubro, Balance balance){
		List<Nodo> listNodoArbol = new ArrayList<Nodo>();
		for(Nodo nodo: listNodo){
			if(nodo.esNodoRaiz()){
				nodo.setClave(nodo.getCodigo());
				nodo.setClaveId(nodo.getId().toString());
				List<Nodo> listHijos = nodo.armarListaHijosConTotales(listNodo,fechaDesde,fechaHasta, listNodoRubro, balance);
				nodo.completarClaveConCodigoCero();
				nodo.completarClaveIdConIdCero();
				Double totalNodo = 0D;
				Double totVen = 0D;
				Double totAct = 0D;
				if(listHijos != null){
					for(Nodo nodoHijo:listHijos){
						if(nodoHijo.getNodoPadre().getId().longValue() == nodo.getId().longValue()){
							totalNodo +=nodoHijo.getTotal();
							totVen += nodoHijo.getTotalEjeVen();
							totAct += nodoHijo.getTotalEjeAct();							
						}
					}					
				}
				nodo.setTotal(NumberUtil.truncate(totalNodo,SiatParam.DEC_IMPORTE_VIEW));
				nodo.setTotalEjeVen(NumberUtil.truncate(totVen,SiatParam.DEC_IMPORTE_VIEW));
				nodo.setTotalEjeAct(NumberUtil.truncate(totAct,SiatParam.DEC_IMPORTE_VIEW));
				listNodoArbol.add(nodo);
				listNodoArbol.addAll(listHijos);			
			}
		}
		return listNodoArbol;
	}
	
	/**
	 * Recorre la lista de Nodos. Arma el árbol tomando como raiz el nodo instanciado, 
	 * y guarda una lista con sus Nodos Hijos. Cada nodo hijo tiene una lista
	 * cargada con sus nodos Hijos hasta llegar a las hojas del arbol.
	 * 
	 * @param listNodo
	 */
	public List<Nodo> armarListaHijosConTotales(List<Nodo> listNodo,Date fechaDesde, Date fechaHasta,List<Nodo> listNodoRubro, Balance balance){
		List<Nodo> listNodoHijo = new ArrayList<Nodo>();
		List<Nodo> listNodoRestantes = new ArrayList<Nodo>();
		listNodoRestantes.addAll(listNodo);
		if(balance != null){
			fechaDesde = balance.getFechaBalance();
			fechaHasta = balance.getFechaBalance();
		}
		for(Nodo nodo: listNodo){
			if(!nodo.esNodoRaiz() && nodo.getNodoPadre().getId().longValue() == this.getId().longValue()){
				nodo.setClave(nodo.getNodoPadre().getClave()+"."+nodo.getCodigo());
				nodo.setClaveId(nodo.getNodoPadre().getClaveId()+"."+nodo.getId());
				listNodoRestantes.remove(nodo);
				List<Nodo> listHijos = nodo.armarListaHijosConTotales(listNodoRestantes,fechaDesde,fechaHasta,listNodoRubro, balance);
				listNodoRestantes.removeAll(listHijos);
				nodo.completarClaveConCodigoCero();
				nodo.completarClaveIdConIdCero();
				if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue() && nodo.getNivel().intValue() == 6){
					List<Partida> listPartida = RelPartida.getListPartidaVigenteByIdNodo(nodo.getId(), fechaDesde); 
					Double totalDP = 0D;
					Double totVenDP = 0D;
					Double totActDP = 0D;
					for(Partida partida: listPartida){
						Object totales[] = ImpPar.getTotalActVenByIdPartidaYFechas(partida.getId(), fechaDesde, fechaHasta, balance);
						if(totales != null){
							if(totales[0] != null)
								totVenDP += (Double) totales[0];
							if(totales[1] != null)
								totActDP += (Double) totales[1];
							if(totales[2] != null)
								totalDP += (Double) totales[2];
						}
					}
					nodo.setTotal(NumberUtil.truncate(totalDP,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeVen(NumberUtil.truncate(totVenDP,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeAct(NumberUtil.truncate(totActDP,SiatParam.DEC_IMPORTE_VIEW));

					if(nodo.getTotal() != 0 || "true".equals(nodo.getForzarMostrar())){
						this.setForzarMostrar("true");
					}
				}else if(nodo.getClasificador().getId().longValue() != Clasificador.ID_CLA_RUBRO.longValue() && 
						ListUtil.isNullOrEmpty(nodo.getListNodoHijo())){ 
					List<Nodo> listNodoRel = RelCla.getListNodoByIdNodo(nodo.getId(), fechaDesde); 
					Double totalDP = 0D;
					Double totVenDP = 0D;
					Double totActDP = 0D;
					for(Nodo nodoRel: listNodoRel){
						if(nodoRel != null && listNodoRubro != null){
							for(Nodo nodoRubro: listNodoRubro){
								if(nodoRubro.getId().longValue() == nodoRel.getId().longValue()){
									totalDP += nodoRubro.getTotal();
									totVenDP += nodoRubro.getTotalEjeVen();
									totActDP += nodoRubro.getTotalEjeAct();								
								}
							}
						}						
					}
					nodo.setTotal(NumberUtil.truncate(totalDP,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeVen(NumberUtil.truncate(totVenDP,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeAct(NumberUtil.truncate(totActDP,SiatParam.DEC_IMPORTE_VIEW));
					if(nodo.getTotal() != 0 || "true".equals(nodo.getForzarMostrar())){
						this.setForzarMostrar("true");
					}
				}else{
					Double totalNodo = 0D;
					Double totVen = 0D;
					Double totAct = 0D;
					if(listHijos != null){
						for(Nodo nodoHijo:listHijos){
							if(nodoHijo.getNodoPadre().getId().longValue() == nodo.getId().longValue()){
								totalNodo += nodoHijo.getTotal();
								totVen += nodoHijo.getTotalEjeVen();
								totAct += nodoHijo.getTotalEjeAct();								
							}
						}											
					}
					nodo.setTotal(NumberUtil.truncate(totalNodo,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeVen(NumberUtil.truncate(totVen,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeAct(NumberUtil.truncate(totAct,SiatParam.DEC_IMPORTE_VIEW));
					if(nodo.getTotal() != 0 || "true".equals(nodo.getForzarMostrar())){
						this.setForzarMostrar("true");
					}
				}
				listNodoHijo.add(nodo);
				listNodoHijo.addAll(listHijos);
			}
		}
		return listNodoHijo;
	}

	/**
	 *  Completar la clave con codigos "00" hasta llegar al nivel indicado en el clasificador.
	 * 
	 */
	public void completarClaveConCodigoCero(int hastaNivel){
		if(this.getNivel().intValue() < hastaNivel){
			for(int c=this.getNivel(); c<hastaNivel; c++ ){
				this.setClave(this.getClave()+".00");
			}
		}	
	}

	/**
	 *  Completar la clave de Ids con id "0" hasta llegar al nivel indicado en el clasificador.
	 * 
	 */
	public void completarClaveIdConIdCero(int hastaNivel){
		if(this.getNivel().intValue() < hastaNivel){
			for(int c=this.getNivel(); c<hastaNivel; c++ ){
				this.setClaveId(this.getClaveId()+".0");
			}
		}		
	}
	
	/**
	 * Recorre la lista de Nodos y por cada Nodo padre lo agrega a la lista final y llama 
	 * a armar el arbol para el cual dicho nodo es raiz.
	 * 
	 * @param listNodo
	 * @return listNodoRaiz
	 */
	public static List<Nodo> obtenerListaArbolConTotalesComparativo(List<Nodo> listNodo, Date priFechaDesde, Date priFechaHasta, Date segFechaDesde, Date segFechaHasta,List<Nodo> listNodoRubro){
		List<Nodo> listNodoArbol = new ArrayList<Nodo>();
		for(Nodo nodo: listNodo){
			if(nodo.esNodoRaiz()){
				nodo.setClave(nodo.getCodigo());
				nodo.setClaveId(nodo.getId().toString());
				List<Nodo> listHijos = nodo.armarListaHijosConTotalesComparativo(listNodo,priFechaDesde,priFechaHasta,segFechaDesde, segFechaHasta, listNodoRubro);
				nodo.completarClaveConCodigoCero();
				nodo.completarClaveIdConIdCero();
				Double totalNodo = 0D;
				Double totVen = 0D;
				Double totAct = 0D;
				Double totalNodoCom = 0D;
				Double totVenCom = 0D;
				Double totActCom = 0D;
				if(listHijos != null){
					for(Nodo nodoHijo:listHijos){
						if(nodoHijo.getNodoPadre().getId().longValue() == nodo.getId().longValue()){
							totalNodo +=nodoHijo.getTotal();
							totVen += nodoHijo.getTotalEjeVen();
							totAct += nodoHijo.getTotalEjeAct();
							totalNodoCom +=nodoHijo.getTotalCom();
							totVenCom += nodoHijo.getTotalEjeVenCom();
							totActCom += nodoHijo.getTotalEjeActCom();
						}
					}					
				}
				nodo.setTotal(NumberUtil.truncate(totalNodo,SiatParam.DEC_IMPORTE_VIEW));
				nodo.setTotalEjeVen(NumberUtil.truncate(totVen,SiatParam.DEC_IMPORTE_VIEW));
				nodo.setTotalEjeAct(NumberUtil.truncate(totAct,SiatParam.DEC_IMPORTE_VIEW));
				nodo.setTotalCom(NumberUtil.truncate(totalNodoCom,SiatParam.DEC_IMPORTE_VIEW));
				nodo.setTotalEjeVenCom(NumberUtil.truncate(totVenCom,SiatParam.DEC_IMPORTE_VIEW));
				nodo.setTotalEjeActCom(NumberUtil.truncate(totActCom,SiatParam.DEC_IMPORTE_VIEW));
				Double variacion = 0D;
				if(nodo.getTotal() != 0){
					variacion = ((nodo.getTotalCom()/nodo.getTotal())-1)*100;
				}
				nodo.setVariacion(NumberUtil.truncate(variacion,2));				
				listNodoArbol.add(nodo);
				listNodoArbol.addAll(listHijos);			
			}
		}
		return listNodoArbol;
	}
	
	/**
	 * Recorre la lista de Nodos. Arma el árbol tomando como raiz el nodo instanciado, 
	 * y guarda una lista con sus Nodos Hijos. Cada nodo hijo tiene una lista
	 * cargada con sus nodos Hijos hasta llegar a las hojas del arbol.
	 * 
	 * @param listNodo
	 */
	public List<Nodo> armarListaHijosConTotalesComparativo(List<Nodo> listNodo,Date priFechaDesde,Date priFechaHasta,Date segFechaDesde,Date segFechaHasta,List<Nodo> listNodoRubro){
		List<Nodo> listNodoHijo = new ArrayList<Nodo>();
		List<Nodo> listNodoRestantes = new ArrayList<Nodo>();
		listNodoRestantes.addAll(listNodo);
		for(Nodo nodo: listNodo){
			if(!nodo.esNodoRaiz() && nodo.getNodoPadre().getId().longValue() == this.getId().longValue()){
				nodo.setClave(nodo.getNodoPadre().getClave()+"."+nodo.getCodigo());
				nodo.setClaveId(nodo.getNodoPadre().getClaveId()+"."+nodo.getId());
				listNodoRestantes.remove(nodo);
				List<Nodo> listHijos = nodo.armarListaHijosConTotalesComparativo(listNodoRestantes,priFechaDesde,priFechaHasta,segFechaDesde, segFechaHasta,listNodoRubro);
				listNodoRestantes.removeAll(listHijos);
				nodo.completarClaveConCodigoCero();
				nodo.completarClaveIdConIdCero();
				if(nodo.getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO.longValue() && nodo.getNivel().intValue() == 6){
					List<Partida> listPartida = RelPartida.getListPartidaVigenteByIdNodo(nodo.getId(), priFechaDesde); 
					Double totalDP = 0D;
					Double totVenDP = 0D;
					Double totActDP = 0D;
					for(Partida partida: listPartida){
						Object totales[] = ImpPar.getTotalActVenByIdPartidaYFechas(partida.getId(), priFechaDesde, priFechaHasta, null);
						if(totales != null){
							if(totales[0] != null)
								totVenDP += (Double) totales[0];
							if(totales[1] != null)
								totActDP += (Double) totales[1];
							if(totales[2] != null)
								totalDP += (Double) totales[2];
						}
					}
					nodo.setTotal(NumberUtil.truncate(totalDP,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeVen(NumberUtil.truncate(totVenDP,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeAct(NumberUtil.truncate(totActDP,SiatParam.DEC_IMPORTE_VIEW));
					
					// Totales Comparativos
					List<Partida> listPartidaCom = RelPartida.getListPartidaVigenteByIdNodo(nodo.getId(), segFechaDesde); 
					Double totalDPCom = 0D;
					Double totVenDPCom = 0D;
					Double totActDPCom = 0D;
					for(Partida partida: listPartidaCom){
						Object totalesCom[] = ImpPar.getTotalActVenByIdPartidaYFechas(partida.getId(), segFechaDesde, segFechaHasta, null);
						if(totalesCom != null){
							if(totalesCom[0] != null)
								totVenDPCom += (Double) totalesCom[0];
							if(totalesCom[1] != null)
								totActDPCom += (Double) totalesCom[1];
							if(totalesCom[2] != null)
								totalDPCom += (Double) totalesCom[2];
						}
					}
					nodo.setTotalCom(NumberUtil.truncate(totalDPCom,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeVenCom(NumberUtil.truncate(totVenDPCom,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeActCom(NumberUtil.truncate(totActDPCom,SiatParam.DEC_IMPORTE_VIEW));
					
					Double variacion = 0D;
					if(nodo.getTotal() != 0){
						variacion = ((nodo.getTotalCom()/nodo.getTotal())-1)*100;
					}
					nodo.setVariacion(NumberUtil.truncate(variacion,2));		
					
					if(nodo.getTotal() != 0 || nodo.getTotalCom() != 0 || "true".equals(nodo.getForzarMostrar())){
						this.setForzarMostrar("true");
					}

				}else if(nodo.getClasificador().getId().longValue() != Clasificador.ID_CLA_RUBRO.longValue() && 
						ListUtil.isNullOrEmpty(nodo.getListNodoHijo())){ 
					List<Nodo> listNodoRel = RelCla.getListNodoByIdNodo(nodo.getId(), priFechaDesde); 
					Double totalDP = 0D;
					Double totVenDP = 0D;
					Double totActDP = 0D;
					for(Nodo nodoRel: listNodoRel){
						if(nodoRel != null && listNodoRubro != null){
							for(Nodo nodoRubro: listNodoRubro){
								if(nodoRubro.getId().longValue() == nodoRel.getId().longValue()){
									totalDP += nodoRubro.getTotal();
									totVenDP += nodoRubro.getTotalEjeVen();
									totActDP += nodoRubro.getTotalEjeAct();								
								}
							}
						}						
					}
					nodo.setTotal(NumberUtil.truncate(totalDP,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeVen(NumberUtil.truncate(totVenDP,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeAct(NumberUtil.truncate(totActDP,SiatParam.DEC_IMPORTE_VIEW));
					
					// Totales Comparativos
					List<Nodo> listNodoRelCom = RelCla.getListNodoByIdNodo(nodo.getId(), segFechaDesde); 
					Double totalDPCom = 0D;
					Double totVenDPCom = 0D;
					Double totActDPCom = 0D;
					for(Nodo nodoRel: listNodoRelCom){
						if(nodoRel != null && listNodoRubro != null){
							for(Nodo nodoRubro: listNodoRubro){
								if(nodoRubro.getId().longValue() == nodoRel.getId().longValue()){
									totalDPCom += nodoRubro.getTotalCom();
									totVenDPCom += nodoRubro.getTotalEjeVenCom();
									totActDPCom += nodoRubro.getTotalEjeActCom();								
								}
							}
						}						
					}
					nodo.setTotalCom(NumberUtil.truncate(totalDPCom,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeVenCom(NumberUtil.truncate(totVenDPCom,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeActCom(NumberUtil.truncate(totActDPCom,SiatParam.DEC_IMPORTE_VIEW));
					
					Double variacion = 0D;
					if(nodo.getTotal() != 0){
						variacion = ((nodo.getTotalCom()/nodo.getTotal())-1)*100;
					}
					nodo.setVariacion(NumberUtil.truncate(variacion,2));		
					
					if(nodo.getTotal() != 0 || nodo.getTotalCom() != 0 || "true".equals(nodo.getForzarMostrar())){
						this.setForzarMostrar("true");
					}
				}else{
					Double totalNodo = 0D;
					Double totVen = 0D;
					Double totAct = 0D;
					Double totalNodoCom = 0D;
					Double totVenCom = 0D;
					Double totActCom = 0D;
					if(listHijos != null){
						for(Nodo nodoHijo:listHijos){
							if(nodoHijo.getNodoPadre().getId().longValue() == nodo.getId().longValue()){
								totalNodo += nodoHijo.getTotal();
								totVen += nodoHijo.getTotalEjeVen();
								totAct += nodoHijo.getTotalEjeAct();
								totalNodoCom += nodoHijo.getTotalCom();
								totVenCom += nodoHijo.getTotalEjeVenCom();
								totActCom += nodoHijo.getTotalEjeActCom();	
							}
						}											
					}
					nodo.setTotal(NumberUtil.truncate(totalNodo,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeVen(NumberUtil.truncate(totVen,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeAct(NumberUtil.truncate(totAct,SiatParam.DEC_IMPORTE_VIEW));
					
					nodo.setTotalCom(NumberUtil.truncate(totalNodoCom,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeVenCom(NumberUtil.truncate(totVenCom,SiatParam.DEC_IMPORTE_VIEW));
					nodo.setTotalEjeActCom(NumberUtil.truncate(totActCom,SiatParam.DEC_IMPORTE_VIEW));
				
					Double variacion = 0D;
					if(nodo.getTotal() != 0){
						variacion = ((nodo.getTotalCom()/nodo.getTotal())-1)*100;
					}
					nodo.setVariacion(NumberUtil.truncate(variacion,2));	
					
					if(nodo.getTotal() != 0 || nodo.getTotalCom() != 0 || "true".equals(nodo.getForzarMostrar())){
						this.setForzarMostrar("true");
					}
				}
				listNodoHijo.add(nodo);
				listNodoHijo.addAll(listHijos);
			}
		}
		return listNodoHijo;
	}
	
}
