//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

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

import org.hibernate.annotations.OrderBy;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.EstadoGesJudVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudDeuVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudEventoVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudVO;
import ar.gov.rosario.siat.gde.iface.model.HistGesJudVO;
import ar.gov.rosario.siat.gde.iface.model.TipoJuzgadoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a GesJud
 * Corresponde a una Gestion Judicial
 * @author tecso
 */
@Entity
@Table(name = "gde_gesJud")
public class GesJud extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desGesJud")
	private String desGesJud;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idProcurador") 
	private Procurador procurador;
	
	@Column(name = "fechaCaducidad")
	private Date fechaCaducidad;
	
	@Column(name = "fechaAlta")
	private Date fechaAlta;
	
	@Column(name = "juzgado")
	private String juzgado;
	
    @Column(name="idCaso") 
	private String idCaso;

    @Column(name="codTipJuz")    
    private Long codTipoJuzgado;
    
	@Column(name = "observacion")
	private String observacion;
	
	@Column(name = "usrcreador")
	private String usrCreador;
	
	@Column(name = "nroExpediente")
	private Long nroExpediente;
	
	@Column(name = "anioExpediente")
	private Long anioExpediente;
	
	@OneToMany()
	@JoinColumn(name="idGesJud")
	private List<GesJudDeu> listGesJudDeu;

	@OneToMany()
	@JoinColumn(name="idGesJud")
	@OrderBy(clause="fechaEvento asc")
	private List<GesJudEvento> listGesJudEvento;

	@OneToMany()
	@JoinColumn(name="idGesJud")
	private List<HistGesJud> listHistGesJud;
	
	@Transient
	private TipoJuzgado tipoJuzgado;
	
	// Constructores
	public GesJud(){
		super();
	}
	
	public GesJud(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	/**
	 * Obtiene el GesJud de la BD y setea el tipoJuzgado
	 */
	public static GesJud getById(Long id) {
		GesJud gesJud= (GesJud) GdeDAOFactory.getGesJudDAO().getById(id);
		gesJud.setTipoJuzgado(TipoJuzgado.getBycodigo(String.valueOf(gesJud.getCodTipoJuzgado())));
		return gesJud;
	}
	
	public static GesJud getByIdNull(Long id) {
		return (GesJud) GdeDAOFactory.getGesJudDAO().getByIdNull(id);
	}
	
	public static List<GesJud> getList() {
		return (ArrayList<GesJud>) GdeDAOFactory.getGesJudDAO().getList();
	}
		
	public static List<GesJud> getListActivos() {			
		return (ArrayList<GesJud>) GdeDAOFactory.getGesJudDAO().getListActiva();
	}
	
	public static GesJud getByDes(String descripcion) {
		return GdeDAOFactory.getGesJudDAO().getByDes(descripcion);
	}
	
	/**
	 * Hace el toVO() y setea el label del estado
	 */
	public GesJudVO toVOForABM(int topeProfundidad, boolean copiarList)
			throws Exception {
		GesJudVO gesJudVO = (GesJudVO) super.toVO(topeProfundidad, false);
		gesJudVO.setIdCaso(this.getIdCaso());
		gesJudVO.getEstadoGesJudVO().setIdEstadoGesJud(getEstado());
		gesJudVO.setTipoJuzgado((TipoJuzgadoVO) (getTipoJuzgado()!=null?getTipoJuzgado().toVO(0, false):new TipoJuzgadoVO()));
		
		if(getEstado().intValue()==Estado.ACTIVO.getId().intValue())
			gesJudVO.getEstadoGesJudVO().setDesEstadoGesJud(EstadoGesJudVO.VIGENTE);
		else if(getEstado().intValue()==Estado.INACTIVO.getId().intValue())
			gesJudVO.getEstadoGesJudVO().setDesEstadoGesJud(EstadoGesJudVO.CADUCO);
		
		if(copiarList){
			// Seteo de la lista de gesJudDeu
			if(listGesJudDeu!=null){
				List<GesJudDeuVO> lista = new ArrayList<GesJudDeuVO>();
				for(GesJudDeu gesJudDeu: listGesJudDeu){
					lista.add(gesJudDeu.toVOLight());
				}
				gesJudVO.setListGesJudDeu(lista);
			}
			
			// Seteo de la lista de gesJudEvento
			if(listGesJudEvento!=null){
				List<GesJudEventoVO> lista = new ArrayList<GesJudEventoVO>();
				for(GesJudEvento gesJudEvento: listGesJudEvento){
					lista.add(gesJudEvento.toVoForView());
				}
				gesJudVO.setListGesJudEvento(lista);
			}
			
			// Seteo de la lista de histGesJud
			if(listHistGesJud!=null){
				List<HistGesJudVO> lista = new ArrayList<HistGesJudVO>();
				for(HistGesJud historico: listHistGesJud){
					lista.add((HistGesJudVO) historico.toVO(0,false));
				}
				gesJudVO.setListHistGesJud(lista);
			}			
		}
		return gesJudVO;
	}
	
	//	 Getters y setters

	public String getDesGesJud() {
		return desGesJud;
	}

	public void setDesGesJud(String desGesJud) {
		this.desGesJud = desGesJud;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public String getJuzgado() {
		return juzgado;
	}

	public void setJuzgado(String juzgado) {
		this.juzgado = juzgado;
	}

	public List<GesJudDeu> getListGesJudDeu() {
		return listGesJudDeu;
	}

	public void setListGesJudDeu(List<GesJudDeu> listGesJudDeu) {
		this.listGesJudDeu = listGesJudDeu;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Procurador getProcurador() {
		return procurador;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public List<HistGesJud> getListHistGesJud() {
		return listHistGesJud;
	}

	public void setListHistGesJud(List<HistGesJud> listHistGesJud) {
		this.listHistGesJud = listHistGesJud;
	}

	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public List<GesJudEvento> getListGesJudEvento() {
		return listGesJudEvento;
	}

	public void setListGesJudEvento(List<GesJudEvento> listGesJudEvento) {
		this.listGesJudEvento = listGesJudEvento;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public TipoJuzgado getTipoJuzgado() {
		return tipoJuzgado;
	}

	public void setTipoJuzgado(TipoJuzgado tipoJuzgado) {
		this.tipoJuzgado = tipoJuzgado;
	}

	public Long getCodTipoJuzgado() {
		return codTipoJuzgado;
	}

	public void setCodTipoJuzgado(Long codTipoJuzgado) {
		this.codTipoJuzgado = codTipoJuzgado;
	}

	public String getUsrCreador() {
		return usrCreador;
	}

	public void setUsrCreador(String usrCreador) {
		this.usrCreador = usrCreador;
	}
	
	public Long getAnioExpediente() {
		return anioExpediente;
	}

	public void setAnioExpediente(Long anioExpediente) {
		this.anioExpediente = anioExpediente;
	}

	public Long getNroExpediente() {
		return nroExpediente;
	}

	public void setNroExpediente(Long nroExpediente) {
		this.nroExpediente = nroExpediente;
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

	/**
	 * Valida si la gestion judicial no contiene eventos asociados, al eliminar una deuda
	 * @return
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		if (listGesJudDeu!=null && !listGesJudDeu.isEmpty()) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,	GdeError.GESJUD_LABEL, GdeError.GESJUDDEU_LABEL );
		}
		
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones
		if(getProcurador()==null || getProcurador().getId()<0)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.PROCURADOR_LABEL);
		
		if(StringUtil.isNullOrEmpty(desGesJud))
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.GESJUD_DESCRIPCION_LABEL);
		
		if(StringUtil.isNullOrEmpty(juzgado))
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.GESJUD_JUZGADO_LABEL);		
		
		if(idCaso==null)
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.GESJUD_CASO_LABEL);
		
		if(getFechaAlta()==null)
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.GESJUD_FECHAALTA_LABEL);
				
		if(getTipoJuzgado()== null || StringUtil.isNullOrEmpty(getTipoJuzgado().getCodTipoJuzgado()))
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	GdeError.GESJUD_TIPO_JUZGADO_LABEL);
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("desGesJud");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.GESJUD_DESCRIPCION_LABEL);			
		}

		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	/**
	 * Verifica si la Gestion Judicial contiene TODOS los predecesores del evento pasado como parametro
	 * @author arobledo
	 * @param evento
	 * @throws Exception
	 * @return 
	 * False si:<br>
	 * 	- evento es null. <br>
	 * 	- la gestion judicial no tiene cargado ningun evento y el evento tiene predecesores
	 * 	- no contiene todos los predecesores.<br><br>
	 * True si:<br>
	 * 	- contiene TODOS los predecesores.<br>
	 * 	- tanto la lista de eventos asociados a la gestion judicial como la lista de predecesores estan vacias<br>
	 * 	- El evento no tiene predecesores 
	 */
	public boolean contienePredecedores(Evento evento) throws Exception{
		if(evento==null)
			return false;
		if(listGesJudEvento==null || listGesJudEvento.isEmpty()){
			if(evento.getListEventosPredecesores()!= null && !evento.getListEventosPredecesores().isEmpty())
				// El evento tiene predecedores que no fueron cargados previamente
				return false;
			else
				// ambas listas estan vacias, es como si la gestion judicial tuviera todos los predecesores
				return true;
		}else{
			if(evento.getListEventosPredecesores()== null || evento.getListEventosPredecesores().isEmpty())
				// El evento no tiene predecesores, es como si la gestion judicial tuviera todos
				return true;
			else{
				// Recorre la lista de eventos predecesores
				for(Evento eventoPredecesor:evento.getListEventosPredecesores()){				
					// Para cada eventoPredecesor, lo busca en la lista de gesJudEvento
					if(!this.contieneEvento(eventoPredecesor.getId().longValue())){
						// no lo contiene
						return false;
					}							
				}
				// Si llega aca es porque contiene todos
				return true;
				}
		}		
	}
	
	/**
	 * Verifica si el idEvento pasado como parametro aparece al menos 1 vez en la lista de gesJudEvento
	 * @author arobledo
	 * @param idEvento
	 * @return False si:<br>
	 * 		- listGesJudEvento == null.<br>
	 * 		- no lo contiene 
	 */
	public boolean contieneEvento(long idEvento){
		if(listGesJudEvento!=null){
			for(GesJudEvento gesJudEvento: listGesJudEvento){
				if(gesJudEvento.getEvento().getId().longValue()==idEvento){
					return true;
				}
			}
		}
		return false;
	}
	
	public GesJudEvento getGesJudEvento(Long idEvento){
		if(listGesJudEvento!=null){
			for(GesJudEvento gesJudEvento: listGesJudEvento){
				if(gesJudEvento.getEvento().getId().equals(idEvento))
					return gesJudEvento;				
			}
		}
		return null;
	}
	/**
	 * devuelve el ultimo evento ingresado (el que tiene la ultima fecha evento), suponiendo que estan ordenados por fechaEvento
	 * @return null si no hay ninguno ingresado
	 */
	public GesJudEvento getUltimoEventoIngresado(){
		if(listGesJudEvento!=null && !listGesJudEvento.isEmpty()){
			return listGesJudEvento.get(listGesJudEvento.size()-1);
		}
		return null;
	}
	
	/**
	 * Verifica si el ultimo evento ingresado tiene la marca de "afecta caducidad en Juicio" en TRUE.
	 * @return Si no tiene eventos devuelve False
	 */
	public boolean sePuedeCaducar(){
		if(listGesJudEvento!=null && !listGesJudEvento.isEmpty()){
			if(getUltimoEventoIngresado().getEvento().getAfectaCadJui().intValue()==1)
				return true;				
		}
		return false;
	}
	
	/**
	 * Devuelve la cantidad de dias desde el ultimo evento registrado
	 * @return null si no se ingreso ningun evento
	 */
	public String getDiasUltEvento() {
		GesJudEvento ultimoEvento = getUltimoEventoIngresado();
		if(ultimoEvento!=null && !ultimoEvento.getEvento().getId().equals(4L)){
			return String.valueOf(DateUtil.getCantDias(ultimoEvento.getFechaEvento(), new Date()));
		}
		return null;
	}
	
	/**
	 * Devuelve la cantidad de dias desde la fecha del evento pasado como parametro
	 * @return null si no existe el evento
	 * @throws Exception 
	 */
	public String getDiasDesdeEvento(Integer codEvento) throws Exception {
		GesJudEvento evento = getGesJudEvento(Evento.getByCodigo(codEvento).getId());
		if(evento!=null){
			return String.valueOf(DateUtil.getCantDias(evento.getFechaEvento(), new Date()));
		}
		return null;
	}	
	/**
	 * Devuelve la cantidad de dias entre los 2 eventos
	 * @return null si alguno de los 2 eventos no existe
	 * @param codEvento1
	 * @param codEvento2
	 * @throws Exception
	 */
	public Integer getDiasEntreEventos(Integer codEvento1, Integer codEvento2) throws Exception{
		GesJudEvento gesJudEvento1 = getGesJudEvento(Evento.getByCodigo(codEvento1).getId());
		GesJudEvento gesJudEvento2 = getGesJudEvento(Evento.getByCodigo(codEvento2).getId());		
		
		if(gesJudEvento1!=null && gesJudEvento2!=null){
			Date fechaEvento1 = gesJudEvento1.getFechaEvento();
			Date fechaEvento2 = gesJudEvento2.getFechaEvento();
			
			int cantDias = DateUtil.getCantDias(fechaEvento1, fechaEvento2);
			return cantDias;
		}
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	// metodos para ABM de gesJudDeu
	public GesJudDeu createGesJudDeu(GesJudDeu gesJudDeu) throws Exception {
		
		// Validaciones de negocio
		if (!gesJudDeu.validateCreate()) {
			return gesJudDeu;
		}

		// creacion 
		GdeDAOFactory.getGesJudDeuDAO().update(gesJudDeu);
		
		return gesJudDeu;
	}	
	
	public GesJudDeu updateGesJudDeu(GesJudDeu gesJudDeu) throws Exception {
		
		// Validaciones de negocio
		if (!gesJudDeu.validateUpdate()) {
			return gesJudDeu;
		}

		// actualiza
		GdeDAOFactory.getGesJudDeuDAO().update(gesJudDeu);
		
		return gesJudDeu;
	}	
	
	public GesJudDeu deleteGesJudDeu(GesJudDeu gesJudDeu) throws Exception {
		
		// Validaciones de negocio
		if (!gesJudDeu.validateDelete()) {
			return gesJudDeu;
		}

		// eliminacion del hist 
		GdeDAOFactory.getHistGesJudDAO().delete(gesJudDeu);
		
		return gesJudDeu;
	}	

	// metodos para ABM de gesJudEvento
	public GesJudEvento createGesJudEvento(GesJudEvento gesJudEvento) throws Exception{
		// Validaciones de negocio
		if (!gesJudEvento.validateCreate()) {
			return gesJudEvento;
		}

		// creacion del evento
		GdeDAOFactory.getGesJudEventoDAO().update(gesJudEvento);
		
		return gesJudEvento;
	}

	/**
	 * Ejecuta el insert sin realizar las validaciones de negocio.Para utilizar en la importacion de eventos por archivo
	 * @param gesJudEvento
	 * @return
	 * @throws Exception
	 */
	public GesJudEvento createGesJudEventoSinValidate(GesJudEvento gesJudEvento) throws Exception{
		// creacion del evento
		GdeDAOFactory.getGesJudEventoDAO().update(gesJudEvento);
		
		return gesJudEvento;
	}
	
	public GesJudEvento deleteGesJudEvento(GesJudEvento gesJudEvento) throws Exception{
		// Validaciones de negocio
		if (!gesJudEvento.validateDelete()) {
			return gesJudEvento;
		}

		// creacion del hist 
		GdeDAOFactory.getGesJudEventoDAO().delete(gesJudEvento);
		
		return gesJudEvento;
	}
	
	// metodos para manejar los historicos (HistGesJud)
	public HistGesJud createHistGesJud(HistGesJud histGesJud) throws Exception {
		
		// Validaciones de negocio
		if (!histGesJud.validateCreate()) {
			return histGesJud;
		}

		// creacion del hist 
		GdeDAOFactory.getHistGesJudDAO().update(histGesJud);
		
		return histGesJud;
	}	
	
	public HistGesJud deleteHistGesJud(HistGesJud histGesJud) throws Exception {
		
		// Validaciones de negocio
		if (!histGesJud.validateDelete()) {
			return histGesJud;
		}

		// eliminacion del hist 
		GdeDAOFactory.getHistGesJudDAO().delete(histGesJud);
		
		return histGesJud;
	}

	
	
	@Override
	public String infoString() {
		String ret=" Gestion Judicial";
		
		if(desGesJud!=null){
			ret+=" - Descripcion: "+desGesJud;
		}
		
		if(procurador!=null){
			ret+=" - Procurador: "+procurador.getDescripcion();
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
		
		if(usrCreador!=null){
			ret+=" - Creada por: "+usrCreador;
		}
		
		if(nroExpediente!=null){
			ret+=" - Nro. Expediente: "+nroExpediente;
		}
		
		if(anioExpediente!=null){
			ret+=" - Año: "+anioExpediente;
		}
		
		return ret;
	}



	
	public String getCasoView(){		
		CasoVO caso = CasServiceLocator.getCasCasoService().construirCasoVO(getIdCaso());		
		return caso.getCasoView();
	}
	


}
