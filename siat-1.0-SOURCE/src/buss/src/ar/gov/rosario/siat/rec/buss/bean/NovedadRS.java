//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.TipoEmision;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.emi.buss.bean.AuxDeuda;
import ar.gov.rosario.siat.emi.buss.bean.Emision;
import ar.gov.rosario.siat.emi.buss.dao.EmiDAOFactory;
import ar.gov.rosario.siat.exe.buss.bean.CueExe;
import ar.gov.rosario.siat.gde.buss.bean.Anulacion;
import ar.gov.rosario.siat.gde.buss.bean.DeudaAdmin;
import ar.gov.rosario.siat.gde.buss.bean.GdeGDeudaManager;
import ar.gov.rosario.siat.gde.buss.bean.MotAnuDeu;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.seg.buss.bean.UsuarioSiat;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.UserContext;

/**
 * Bean correspondiente a Novedades de regimen simplificado
 * 
 * estado: -1: Se ha registrado el regimen correspondiente
 *          1: Se ha procesado la deuda (anular y emitir) correctamente 
 *          2: Se ha procesado con errores
 * msgDeuda: tiene un string con el comentario de las modificaciones realizadas o del error
 * 
 * @author tecso
 */
@Entity
@Table(name = "dre_novedadRS")
public class NovedadRS extends BaseBO {
	private static Logger log = Logger.getLogger(NovedadRS.class);

	private static final long serialVersionUID = 1L;
	
	public static Integer REGISTRADO = -1;
	public static Integer PROCESADO_OK = 1;
	public static Integer PROCESADO_ERROR = 2;
	public static Integer REGISTRADO_NO_PROCESAR= 3;
		
	@Column(name = "tipoUsuario")
	private Integer tipoUsuario; 

	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoTramiteRS") 
	private TipoTramiteRS tipoTramiteRS;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idCatRSDrei") 
	private CatRSDrei catRSDRei;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idCuentaDrei") 
	private Cuenta cuentaDRei;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
    @JoinColumn(name="idCuentaEtur") 
	private Cuenta cuentaEtur;
	
	@Column(name="fechaTransaccion")
	private Date fechaTransaccion;

	@Column(name = "tipoTransaccion")
	private String tipoTransaccion; 
	
	@Column(name = "usrCliente")
	private String usrCliente; 

	@Column(name = "cuit")
	private String cuit; 

	@Column(name = "desCont")
	private String desCont; 

	@Column(name = "domLocal")
	private String domLocal; 

	@Column(name = "telefono")
	private String telefono;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "tipoCont")
	private Integer tipoCont;
	
	@Column(name = "isib")
	private String isib;
	
	@Column(name = "nroCuenta")
	private String nroCuenta; 

	@Column(name = "listActividades")
	private String listActividades;
	
	@Column(name = "mesInicio")
	private Integer mesInicio; 
	
	@Column(name = "anioInicio")
	private Integer anioInicio;
	
	@Column(name = "precioUnitario")
	private Double precioUnitario;
	
	@Column(name = "canPer")
	private Integer canPer;
	
	@Column(name = "ingBruAnu")
	private Double ingBruAnu;
	
	@Column(name = "supAfe")
	private Double supAfe;
	
	@Column(name = "publicidad")
	private Double publicidad;
	
	@Column(name = "redHabSoc")
	private Double redHabSoc;
	
	@Column(name = "adicEtur")
	private Integer adicEtur; 
	
	@Column(name = "nroCategoria")
	private Integer nroCategoria;
	
	@Column(name = "importeDrei")
	private Double importeDrei;
	
	@Column(name = "importeEtur")
	private Double importeEtur;
	
	@Column(name = "importeAdicional")
	private Double importeAdicional;
	
	@Column(name = "desCategoria")
	private String desCategoria;
	
	@Column(name = "desPublicidad")
	private String desPublicidad;
	
	@Column(name = "desEtur")
	private String desEtur;

	@Column(name = "cuna")
	private String cuna;

	@Column(name = "codBar")
	private String codBar;

	@Column(name = "codBarComprimido")
	private String codBarComprimido;

	@Column(name = "mesBaja")
	private Integer mesBaja;
	
	@Column(name = "anioBaja")
	private Integer anioBaja;
	
	@Column(name = "motivoBaja")
	private Integer motivoBaja;

	@Column(name = "desBaja")
	private String desBaja;
	
	@Column(name = "msgDeuda")
	private String msgDeuda;
	
	@Column(name = "conObservacion")
	private Integer conObservacion;
	
	@Transient
	private Integer estadoAux = NovedadRS.REGISTRADO;
	
	@Transient
	private ReporteNovedadRSHelper reporteNovedadRSHelper = null;
	
	// Constructores
	public NovedadRS(){
		super();
	}
	
	
	//Metodos de clase
	public static NovedadRS getById(Long id) {
		return (NovedadRS) RecDAOFactory.getNovedadRSDAO().getById(id);  
	}
	
	public static NovedadRS getByIdNull(Long id) {
		return (NovedadRS) RecDAOFactory.getNovedadRSDAO().getByIdNull(id);
	}

	
	public static List<NovedadRS> getListTramitesByCuit(String cuit) throws Exception {
		return RecDAOFactory.getNovedadRSDAO().getListTramitesByCuit(cuit);
	}

	
	public static NovedadRS getLastByCtaDRei(Cuenta cuentaDReI) throws Exception {
		return RecDAOFactory.getNovedadRSDAO().getLastNovedadRS("DREI", cuentaDReI, null);
	}

	
	public static NovedadRS getLastByCtaetur(Cuenta cuentaEtur) throws Exception {
		return RecDAOFactory.getNovedadRSDAO().getLastNovedadRS("ETUR", cuentaEtur, null);
	}
	
	/**
	 * Obtiene la lista de Novedades de Regimen Simplificado en estado Registrado (sin procesar) y unicamente para tipo de tramite "ADHESION"
	 * 
	 * @return
	 */
	public static List<NovedadRS> getListRegistrado()throws Exception {
		return RecDAOFactory.getNovedadRSDAO().getListRegistrado();
	}
	
	/**
	 * Obtiene la lista de Novedades de Regimen Simplificado en estado Registrado (sin procesar) utilizando los filtros pasados como parametros.
	 * 
	 * @return
	 */
	public static List<NovedadRS> getListRegistradoByFiltros(TipoTramiteRS tipoTramiteRS, Date fechaNovedadDesde, Date fechaNovedadHasta) throws Exception {
		return RecDAOFactory.getNovedadRSDAO().getListRegistradoByFiltros(tipoTramiteRS, fechaNovedadDesde, fechaNovedadHasta);
	}
	
	/**
	 *  Devuelve true si existe al menos una Novedad de regimen simplificado en estado Registrado (sin procesar)  y tipo de tramite "ADHESION"
	 * 
	 * @return
	 */
	public static boolean existenNovedadRSSinProcesar()throws Exception {
		return RecDAOFactory.getNovedadRSDAO().existenNovedadRSSinProcesar();
	}
	
	public Integer getAdicEtur() {
		return adicEtur;
	}


	public void setAdicEtur(Integer adicEtur) {
		this.adicEtur = adicEtur;
	}


	public Integer getAnioBaja() {
		return anioBaja;
	}


	public void setAnioBaja(Integer anioBaja) {
		this.anioBaja = anioBaja;
	}


	public Integer getAnioInicio() {
		return anioInicio;
	}


	public void setAnioInicio(Integer anioInicio) {
		this.anioInicio = anioInicio;
	}


	public Integer getCanPer() {
		return canPer;
	}


	public void setCanPer(Integer canPer) {
		this.canPer = canPer;
	}


	public CatRSDrei getCatRSDRei() {
		return catRSDRei;
	}


	public void setCatRSDRei(CatRSDrei catRSDRei) {
		this.catRSDRei = catRSDRei;
	}


	public String getCodBar() {
		return codBar;
	}


	public void setCodBar(String codBar) {
		this.codBar = codBar;
	}


	public Cuenta getCuentaDRei() {
		return cuentaDRei;
	}


	public void setCuentaDRei(Cuenta cuentaDRei) {
		this.cuentaDRei = cuentaDRei;
	}


	public Cuenta getCuentaEtur() {
		return cuentaEtur;
	}


	public void setCuentaEtur(Cuenta cuentaEtur) {
		this.cuentaEtur = cuentaEtur;
	}


	public String getCuit() {
		return cuit;
	}


	public void setCuit(String cuit) {
		this.cuit = cuit;
	}


	public String getCuna() {
		return cuna;
	}


	public void setCuna(String cuna) {
		this.cuna = cuna;
	}


	public String getDesCategoria() {
		return desCategoria;
	}


	public void setDesCategoria(String desCategoria) {
		this.desCategoria = desCategoria;
	}


	public String getDesCont() {
		return desCont;
	}


	public void setDesCont(String desCont) {
		this.desCont = desCont;
	}


	public String getDesEtur() {
		return desEtur;
	}


	public void setDesEtur(String desEtur) {
		this.desEtur = desEtur;
	}


	public String getDesPublicidad() {
		return desPublicidad;
	}


	public void setDesPublicidad(String desPublicidad) {
		this.desPublicidad = desPublicidad;
	}


	public String getDomLocal() {
		return domLocal;
	}


	public void setDomLocal(String domLocal) {
		this.domLocal = domLocal;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Date getFechaTransaccion() {
		return fechaTransaccion;
	}


	public void setFechaTransaccion(Date fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}


	public Double getImporteAdicional() {
		return importeAdicional;
	}


	public void setImporteAdicional(Double importeAdicional) {
		this.importeAdicional = importeAdicional;
	}


	public Double getImporteDrei() {
		return importeDrei;
	}


	public void setImporteDrei(Double importeDrei) {
		this.importeDrei = importeDrei;
	}


	public Double getImporteEtur() {
		return importeEtur;
	}


	public void setImporteEtur(Double importeEtur) {
		this.importeEtur = importeEtur;
	}


	public Double getIngBruAnu() {
		return ingBruAnu;
	}


	public void setIngBruAnu(Double ingBruAnu) {
		this.ingBruAnu = ingBruAnu;
	}


	public String getIsib() {
		return isib;
	}


	public void setIsib(String isib) {
		this.isib = isib;
	}


	public String getListActividades() {
		return listActividades;
	}


	public void setListActividades(String listActividades) {
		this.listActividades = listActividades;
	}


	public Integer getMesBaja() {
		return mesBaja;
	}


	public void setMesBaja(Integer mesBaja) {
		this.mesBaja = mesBaja;
	}


	public Integer getMesInicio() {
		return mesInicio;
	}


	public void setMesInicio(Integer mesInicio) {
		this.mesInicio = mesInicio;
	}


	public Integer getMotivoBaja() {
		return motivoBaja;
	}


	public void setMotivoBaja(Integer motivoBaja) {
		this.motivoBaja = motivoBaja;
	}


	public Integer getNroCategoria() {
		return nroCategoria;
	}


	public void setNroCategoria(Integer nroCategoria) {
		this.nroCategoria = nroCategoria;
	}


	public String getNroCuenta() {
		return nroCuenta;
	}


	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}


	public Double getPrecioUnitario() {
		return precioUnitario;
	}


	public void setPrecioUnitario(Double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}


	public Double getPublicidad() {
		return publicidad;
	}


	public void setPublicidad(Double publicidad) {
		this.publicidad = publicidad;
	}


	public Double getRedHabSoc() {
		return redHabSoc;
	}


	public void setRedHabSoc(Double redHabSoc) {
		this.redHabSoc = redHabSoc;
	}


	public Double getSupAfe() {
		return supAfe;
	}


	public void setSupAfe(Double supAfe) {
		this.supAfe = supAfe;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public Integer getTipoCont() {
		return tipoCont;
	}


	public void setTipoCont(Integer tipoCont) {
		this.tipoCont = tipoCont;
	}


	public TipoTramiteRS getTipoTramiteRS() {
		return tipoTramiteRS;
	}


	public void setTipoTramiteRS(TipoTramiteRS tipoTramiteRS) {
		this.tipoTramiteRS = tipoTramiteRS;
	}


	public String getTipoTransaccion() {
		return tipoTransaccion;
	}


	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}


	public Integer getTipoUsuario() {
		return tipoUsuario;
	}


	public void setTipoUsuario(Integer tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}


	public String getUsrCliente() {
		return usrCliente;
	}

	public String getCodBarComprimido() {
		return codBarComprimido;
	}

	public void setCodBarComprimido(String codBarComprimido) {
		this.codBarComprimido = codBarComprimido;
	}

	public void setUsrCliente(String usrCliente) {
		this.usrCliente = usrCliente;
	}


	public String getDesBaja() {
		return desBaja;
	}


	public void setDesBaja(String desBaja) {
		this.desBaja = desBaja;
	}


	public String getMsgDeuda() {
		return msgDeuda;
	}


	public void setMsgDeuda(String msgDeuda) {
		this.msgDeuda = msgDeuda;
	}

	public void addMsgDeuda(String msgDeuda) {
		if (this.msgDeuda==null)
			this.msgDeuda = msgDeuda;
		else
			this.msgDeuda += "\n" + msgDeuda;
		
	}
	
	public Integer getConObservacion() {
		return conObservacion;
	}

	public void setConObservacion(Integer conObservacion) {
		this.conObservacion = conObservacion;
	}

	/**
	 * Variable Auxiliar utilizada en el acomodar deuda. (no es un campo de dre_novedadRS) 
	 * @return
	 */
	public Integer getEstadoAux() {
		return estadoAux;
	}

	/**
	 * Variable Auxiliar utilizada en el acomodar deuda. (no es un campo de dre_novedadRS) 
	 * 
	 */	
	public void setEstadoAux(Integer estadoAux) {
		this.estadoAux = estadoAux;
	}
	
	// Metodos de Instancia
	
	/**
	 * 
	 * Obtiene la deuda/multas existente con posterioridad al periodo/anio de inicio
	 * y genera una logica segun cada caso.
	 * 
	 * Como resutado, ademas de anular y/o generar deuda, construye un string con la descripcion
	 * y lo almacena como propiedad de la NovedadRS
	 *
	 * Se abre transaccion, se trabaja, se modifica la novedad y se cierra la transaccion
	 * 
	 * (Extension del acomodarDeuda, antes de llamarlo setea en la novedad la clase para generacion de reportes usada por el proceso de aplicacion masiva)
	 * 
	 * @param confirmado
	 * @param reporteNovedadRSHelper
	 * 
	 * @return
	 */
	public String acomodarDeuda(boolean confirmado, ReporteNovedadRSHelper reporteNovedadRSHelper) {
		
		this.reporteNovedadRSHelper = reporteNovedadRSHelper;
		
		return acomodarDeuda(confirmado);
	}
	
	/**
	 * 
	 * Obtiene la deuda/multas existente con posterioridad al periodo/anio de inicio
	 * y genera una logica segun cada caso.
	 * 
	 * Como resutado, ademas de anular y/o generar deuda, construye un string con la descripcion
	 * y lo almacena como propiedad de la NovedadRS
	 *
	 * Se abre transaccion, se trabaja, se modifica la novedad y se cierra la transaccion
	 * 
	 * @param novedadRS
	 * @return
	 */
	public String acomodarDeuda(boolean confirmado) {
		String regimen="";
		Integer anioInicio=0;
		Integer mesInicio=0;
		Long idCuentaDREI = null;
		Long idCuentaETUR = null;
		
		String ret="";
		
		try {
			SiatHibernateUtil.currentSession();
			
			log.debug("WS-AD: acomodar" + this.getId() + " - " + this.getCuna());
			Integer estado = NovedadRS.PROCESADO_OK;

			Long idTipoTramiteRS = this.getTipoTramiteRS().getId();
			log.debug(" tipo tramite: " + idTipoTramiteRS);
			
			// obtiene periodo de inicio y regimen en que queda la cuenta
			if (TipoTramiteRS.ID_TRAMITE_ALTA.equals(idTipoTramiteRS) ||
					TipoTramiteRS.ID_TRAMITE_MODIFICACION.equals(idTipoTramiteRS) ||
					TipoTramiteRS.ID_TRAMITE_RECATEGORIZACION.equals(idTipoTramiteRS)) {

				anioInicio = this.getAnioInicio();
				mesInicio = this.getMesInicio();
				regimen="SIMPLIFICADO";

				ret = "Acomodar Deuda: En regimen:" + regimen + " - Periodo Inicio=" + mesInicio.toString() + " - Mes Inicio=" + anioInicio.toString();
				log.debug("WS-AD: acomodar" + ret );

			} else if(TipoTramiteRS.ID_TRAMITE_BAJA.equals(idTipoTramiteRS) && this.getMotivoBaja().equals(2)) {

				// exclusion del regimen
				anioInicio = this.getAnioBaja();
				mesInicio = this.getMesBaja();
				regimen = "GENERAL";

				ret = "Acomodar Deuda: En regimen:" + regimen + " - Periodo Inicio=" + mesInicio.toString() + " - Mes Inicio=" + anioInicio.toString();
				log.debug("WS-AD: acomodar" + ret );

			} else if(TipoTramiteRS.ID_TRAMITE_BAJA.equals(idTipoTramiteRS) && this.getMotivoBaja().equals(1)) {
				
				// cese de actividades. no hay que hacer nada con la deuda
				regimen="BAJA";
				ret = "Cese de actividades. No se interviene la deuda por entender que son funciones propias al cierre de comercio.\n";
				log.debug("WS-AD: acomodar" + ret );
				
				// Cargar Cuenta para Reporte
				if(this.reporteNovedadRSHelper != null)
					this.reporteNovedadRSHelper.addCuentaMapaCierre(this.getNroCuenta());
				
			} else {
				// es un tramite que no tiene efecto sobre la deuda: Listar o ver. Nunca deberia pasar por aca.
				regimen ="NADA";
				ret = "No hay efecto sobre la deuda.\n";
				log.debug("WS-AD: acomodar" + ret );
			}


			// en caso GENERAL o SIMPLIFICADO, actualiza la deuda
			if(this.getCuentaDRei() != null)
				idCuentaDREI = this.getCuentaDRei().getId();
			if(this.getCuentaEtur() != null)
				idCuentaETUR = this.getCuentaEtur().getId();
			
			if (regimen.equals("GENERAL") || regimen.equals("SIMPLIFICADO")) {
				
				Date fechaInicio = null;
				try{ 
					fechaInicio = DateUtil.getDate(anioInicio, mesInicio+1, 1);
					Date fechaInicioRegimen = DateUtil.getDate(SiatParam.getInteger(SiatParam.ANIO_INICIO_RS), SiatParam.getInteger(SiatParam.MES_INICIO_RS)+1, 1);
					if(fechaInicio == null){
						ret += "\nNo se puede obtener una fecha para el mes y año de inicio/baja de la novedad.\n";
						estado = NovedadRS.PROCESADO_ERROR;
					}else if(!DateUtil.isDateAfterOrEqual(fechaInicio, fechaInicioRegimen)){
						ret += "\nEl periodo de inicio/baja es menor al inicio del regimen simplificado.\n";
						estado = NovedadRS.PROCESADO_ERROR;
					}
				}catch (Exception e) {
						fechaInicio = null;
						ret += "\nNo se puede obtener una fecha para el mes y año de inicio/baja de la novedad.\n";
						estado = NovedadRS.PROCESADO_ERROR;
				}
				
				if(estado.intValue() != PROCESADO_ERROR){
					// cuenta drei
					if (idCuentaDREI==null) {
						ret += "No se informa la cuenta de DReI\n";
						
					} else {
						ret += procesarDeudaCuenta(idCuentaDREI, regimen, anioInicio, mesInicio, confirmado);
					}
					
					// cuenta etur
					if (idCuentaETUR==null) {
						ret += "No se informa la cuenta Etur\n";
						
					} else {
						ret += procesarDeudaCuenta(idCuentaETUR, regimen, anioInicio, mesInicio, confirmado);
					}					
				}
			
			}
			
			// aqui decide en funcion de los estados de las novedades el estado en que debe quedar la novedadRS
			// y actualiza
			if (confirmado){
				Transaction tx = SiatHibernateUtil.currentSession().beginTransaction();
				this.setEstado(estado);
				this.setMsgDeuda(ret);
				
				RecDAOFactory.getNovedadRSDAO().update(this);
				if(tx != null) tx.commit();
			}
			
			
		} catch (Exception e) {
			ret = "ERROR no manejado: " + e.getMessage();
			
		}
		
		return ret;
	}

	


	/**
	 * procesa las novedades para la cuenta que corresponde
	 * 
	 * @return
	 */
	private String procesarDeudaCuenta(Long idCuenta, String regimenActual, Integer anio, Integer mes, boolean confirmado)  {
		String msgDeuda;
		
		Session session=null;
		Transaction tx =null;

		UserContext userContext = new UserContext();
		userContext.setUserName("siat");
		userContext.setIdUsuarioSiat(UsuarioSiat.ID_USUARIO_SIAT); 
		DemodaUtil.setCurrentUserContext(userContext);
		
		try {
			
			session = SiatHibernateUtil.currentSession();
			
			if (confirmado) {
				// abre la transaccion
				tx= session.beginTransaction();
			}

			log.debug("WS-AD-PC: INICIO");
			log.debug("levanta la cuenta: " + idCuenta);
			
			Cuenta cuenta = Cuenta.getById(idCuenta);
			
			// obtiene el codigo de emision para el recurso
			String codigoEmi = cuenta.getRecurso().getCodigoEmisionBy(new Date());
			log.debug("WS-AD-PC: recuperamos codigo para emision");

			// mensaje para la cuenta
			msgDeuda = "Cuenta: " + cuenta.getRecurso().getCodRecurso() + ": " + cuenta.getNumeroCuenta() + "\n";
			if (regimenActual.equals("BAJA")) {
				// es una baja de la cuenta completa. no se hace nada sobre la deuda
				msgDeuda += "No se toca la deuda por asumir acciones durante el cierre de comercio\n";
			
				if(!this.estadoAux.equals(NovedadRS.PROCESADO_ERROR))
					this.estadoAux = NovedadRS.PROCESADO_OK;
				
				if (confirmado)
					tx.rollback();
				
				return msgDeuda;
			} 

			/*-issue 6677: Error en "acomodar deuda" que genera registros duplicados de deuda RS...
			 * 
			 * La propiedad listDeudaAdminRaw mantiene un cache con deudaAdmin asociada a la cuenta,
			 * si en una novedad anterior, la deuda fue anulada, todavia va a estar en ese cache sin anular.
			 * Seteamos en null, para que el cuenta.getListDeudaAdmin cargue este cache nuevamente.
			 */
			cuenta.setListDeudaAdminRaw(null);
			
			msgDeuda += "Analisis sobre deuda existente:\n";
			log.debug(msgDeuda);
			// obtiene la lista de deuda en la via administrativa
			List<DeudaAdmin> listDeudaAdmin = cuenta.getListDeudaAdmin();

			// Obj: Itera la deuda existente con fecha mayor al inicio del periodo
			//      Anula la deuda que tenga que anular
			//      Registra en un mapa el monto pagado en cada periodo
			
			Map<String, Double> mapPerPagos = new HashMap<String, Double>();
			Map<String, Boolean> mapAjustes = new HashMap<String, Boolean>();
			Map<String, Boolean> mapNoTocar = new HashMap<String, Boolean>(); // las deuda de los periodos que esten aqui, quedan intactas. Ni se emiten ni se anulan. (issue 5455) 
			
			// Variable utilizada para la excluir de la emision a los periodos que ya se encuentren emitidos (issue 5455)
			String abrClaDeuNuevo = null;
			if (regimenActual.equals("GENERAL"))
				abrClaDeuNuevo = RecClaDeu.ABR_ORIG;
			else if(regimenActual.equals("SIMPLIFICADO")) 
				abrClaDeuNuevo = RecClaDeu.ABR_RS;
			
			Long periodoAnioInicio = Long.valueOf(anio*100+mes);
			
			Long idTipoTramiteRS = this.getTipoTramiteRS().getId();
			
			// itera la deuda
			for (DeudaAdmin deudaAdmin: listDeudaAdmin) {
				
				Long periodoDeuda = deudaAdmin.getAnio()*100 + deudaAdmin.getPeriodo();
				
				// 1- anulacion
				
				// si la deuda es de un perioro mayor o igual a la que esta, la elimina
				if (periodoDeuda.longValue() >=  periodoAnioInicio.longValue()) {
					// anula

					msgDeuda += getDescDeuda(deudaAdmin);
					log.debug(msgDeuda);

					String abrClaDeu = deudaAdmin.getRecClaDeu().getAbrClaDeu();
					// Excluir de la emision y anulacio a los periodos que ya se encuentren emitidos (issue 5455)
					// Solo para los casos que no son Modificaciones y Recategorizaciones
					if(!TipoTramiteRS.ID_TRAMITE_MODIFICACION.equals(idTipoTramiteRS) && 
							!TipoTramiteRS.ID_TRAMITE_RECATEGORIZACION.equals(idTipoTramiteRS) && 
							abrClaDeu.equals(abrClaDeuNuevo)){

						mapNoTocar.put(periodoDeuda.toString(), true);
					}

					// si es un ajuste, lo anota en el mapa para posterior tratamiento
					if (abrClaDeu.equals(RecClaDeu.ABR_AV) || abrClaDeu.equals(RecClaDeu.ABR_AF) ) {

						mapAjustes.put(periodoDeuda.toString(), true);						
					}

					if (abrClaDeu.equals(RecClaDeu.ABR_MULTA) || 
							abrClaDeu.equals(RecClaDeu.ABR_ORIG) ||
							abrClaDeu.equals(RecClaDeu.ABR_RECTIF)||
							abrClaDeu.equals(RecClaDeu.ABR_RS)) {

						// hay que analizar estos casos
						if (deudaAdmin.getIdConvenio() != null) {
							msgDeuda += " -> NO SE ANULA. ESTA EN CONVENIO\n";

							// Marca la novedad como con observaciones. Esto es para luego poder filtras las que tienen estas situaciones especiales
							if(confirmado && (this.getConObservacion() == null || this.getConObservacion().intValue() == SiNo.NO.getId().intValue())){
								this.setConObservacion(SiNo.SI.getId());
								RecDAOFactory.getNovedadRSDAO().update(this);
							}
							// Cargar Cuenta para Reporte
							if(this.reporteNovedadRSHelper != null)
								this.reporteNovedadRSHelper.addCuentaMapaConvenio(cuenta.getRecurso().getDesRecurso()+" - Nro: "+cuenta.getNumeroCuenta());


						} else if (deudaAdmin.getFechaPago() != null) {
							msgDeuda += " -> NO SE ANULA. TIENE PAGO\n";

							// si no es una multa (es orig, rectif o rs) toma el valor pagado
							if (!abrClaDeu.equals(RecClaDeu.ABR_MULTA)) {
								Double val = mapPerPagos.get(periodoDeuda.toString());
								if (val==null) val = 0D;
								val += (deudaAdmin.getImporte() - deudaAdmin.getSaldo());
								mapPerPagos.put(periodoDeuda.toString(), val);
							}

							// Marca la novedad como con observaciones. Esto es para luego poder filtras las que tienen estas situaciones especiales
							if(confirmado && (this.getConObservacion() == null || this.getConObservacion().intValue() == SiNo.NO.getId().intValue())){
								this.setConObservacion(SiNo.SI.getId());
								RecDAOFactory.getNovedadRSDAO().update(this);
							}
							// Cargar Cuenta para Reporte
							if(this.reporteNovedadRSHelper != null)
								this.reporteNovedadRSHelper.addCuentaMapaPagos(cuenta.getRecurso().getDesRecurso()+" - Nro: "+cuenta.getNumeroCuenta());

							// Si la deuda esta aca, no la tengo ni que emitir ni que anular.
						} else if (mapNoTocar.containsKey(periodoDeuda.toString())) {
							msgDeuda += " -> NO SE ANULA: Marcada como no alterar (issue 5455).\n";

						} else {
							// anulamos deuda
							if (confirmado) {
								// setea los datos en la deuda Anulada
								Anulacion anulacionDueda = new Anulacion();
								anulacionDueda.setIdDeuda(deudaAdmin.getId());
								anulacionDueda.setFechaAnulacion(new Date());
								anulacionDueda.setMotAnuDeu(MotAnuDeu.getById(MotAnuDeu.ID_ANULACION));
								anulacionDueda.setObservacion("Anulacion por adhesion/modif/recat. a RS");
								anulacionDueda.setRecurso(deudaAdmin.getRecurso());
								anulacionDueda.setViaDeuda(deudaAdmin.getViaDeuda());

								GdeDAOFactory.getDeudaAnuladaDAO().update(anulacionDueda);
								anulacionDueda = GdeGDeudaManager.getInstance().anularDeuda(anulacionDueda,deudaAdmin,null);
							}
							msgDeuda += " -> ANULADA!\n";

						}

					} else {
						// si es una clasificacion deuda de ajuste

						// en estos casos no se analiza nada
						msgDeuda += "-> NO SE ANULA. ES INDEPENDIENTE DEL REGIMEN\n";

						// Marca la novedad como con observaciones. Esto es para luego poder filtras las que tienen estas situaciones especiales
						if(confirmado && (this.getConObservacion() == null || this.getConObservacion().intValue() == SiNo.NO.getId().intValue())){
							this.setConObservacion(SiNo.SI.getId());
							RecDAOFactory.getNovedadRSDAO().update(this);
						}

						// Cargar Cuenta para Reporte
						if(this.reporteNovedadRSHelper != null)
							this.reporteNovedadRSHelper.addCuentaMapaAjustes(cuenta.getRecurso().getDesRecurso()+" - Nro: "+cuenta.getNumeroCuenta());

					}
					
					log.debug(msgDeuda);
				}
			}
			
			// 2- emsion: ultimo periodo emitido
			Recurso recurso = cuenta.getRecurso();
			
			msgDeuda += "Emision: Ultimo periodo emitido del recurso: " + recurso.getUltPerEmi() + "\n";
			log.debug(msgDeuda);
			
			log.debug("Fecha inicio: mes:" + mes.toString() + " - anio: " + anio.toString());
			log.debug("Fecha string para transformar: " + "01/"+mes.toString()+"/"+anio.toString());
			
			String strMes="";
			if (mes<10)
				strMes = "0" + mes.toString();
			else
				strMes = mes.toString();
			
			Date fechaUltPerEmi = DateUtil.getDate("01/"+recurso.getUltPerEmi().substring(4,6)+"/"+recurso.getUltPerEmi().substring(0, 4), DateUtil.ddSMMSYYYY_MASK);
			Date fechaPriPerEmi = DateUtil.getDate("01/"+strMes+"/"+anio.toString(), DateUtil.ddSMMSYYYY_MASK);

			log.debug("fechaPriPerEmi=" + fechaPriPerEmi);
			

			List<Date[]> listRangosEmision  = DateUtil.getListDateRangeForYears(fechaPriPerEmi, fechaUltPerEmi);

			log.debug("Fecha inicio: "+DateUtil.formatDate(fechaPriPerEmi, DateUtil.ddSMMSYY_MASK)+ ", Fecha ult perEmi: "+DateUtil.formatDate(fechaUltPerEmi, DateUtil.ddSMMSYYYY_MASK));

			if (null == recurso.getVencimiento()) {
				msgDeuda += "ERROR: No se pudo obtener la fecha de vencimiento para el recurso. Ni se anula ni se emite deuda\n";
				
				this.estadoAux = NovedadRS.PROCESADO_ERROR;
				
				if (confirmado)
					tx.rollback();
				
				return msgDeuda;
			}
			
			if(ListUtil.isNullOrEmpty(listRangosEmision)) {
				msgDeuda += "No se emite deuda.\n";

			} else {
				// Armar lista de Atributos con los valores cargados en la novedad para pasar a la emisión
				List<GenericAtrDefinition> listAtributos = new ArrayList<GenericAtrDefinition>();
				
				// Valor del Regimen
				GenericAtrDefinition atrDefinition = new GenericAtrDefinition();
				atrDefinition.setAtributo((AtributoVO) Atributo.getByCodigo(Atributo.COD_REGIMEN).toVO(1));
				if (regimenActual.equals("GENERAL"))
					atrDefinition.addValor(Cuenta.VALOR_REGIMEN_GENERAL);
				else if(regimenActual.equals("SIMPLIFICADO") ) 
					atrDefinition.addValor(Cuenta.VALOR_REGIMEN_SIMPLIFICADO);
				listAtributos.add(atrDefinition);
				
				// Valor del CUMUR
				atrDefinition = new GenericAtrDefinition();
				atrDefinition.setAtributo((AtributoVO) Atributo.getByCodigo(Atributo.COD_CUMUR).toVO(1));
				atrDefinition.addValor(this.getCuna());
				listAtributos.add(atrDefinition);
				
				// Valor del Periodo Inicio RS
				atrDefinition = new GenericAtrDefinition();
				atrDefinition.setAtributo((AtributoVO) Atributo.getByCodigo(Atributo.COD_PERINIRS).toVO(1));
				
				String perIniRS = "";
				if(this.mesInicio != null && this.anioInicio != null){
					perIniRS = StringUtil.completarCerosIzq(mesInicio.toString(),2);
					perIniRS += "/"+anioInicio.toString();
				}else{
					perIniRS = "-";
				}
				atrDefinition.addValor(perIniRS);
				listAtributos.add(atrDefinition);
				
				RecClaDeu recClaDeuDifRS = RecClaDeu.getByRecursoAndAbrClaDeu(recurso, RecClaDeu.ABR_DIFRS);

				log.debug(msgDeuda);					
				for (Date[] rangoAEmitir : listRangosEmision) {
					Date fechaPriPerEmiForRango = rangoAEmitir[0];
					Date fechaUltPerEmiForRango = rangoAEmitir[1];

					Integer perEmiDesde = new Integer(DateUtil.getMes(fechaPriPerEmiForRango));
					Integer perEmiHasta = new Integer(DateUtil.getMes(fechaUltPerEmiForRango));
					Integer anioEmi = new Integer(DateUtil.getAnio(fechaPriPerEmiForRango));

					log.debug("codEmi=" + codigoEmi);
					log.debug("perEmiDesde=" + perEmiDesde);
					log.debug("perEmiHasta=" + perEmiHasta);
					log.debug("anioEmi="+anioEmi);

					Emision emi = new Emision();

					emi.setRecurso(cuenta.getRecurso());
					emi.setPeriodoDesde(perEmiDesde);
					emi.setPeriodoHasta(perEmiHasta);
					emi.setAnio(anioEmi);
					emi.setFechaEmision(new Date());
					emi.setTipoEmision(TipoEmision.getById(TipoEmision.ID_INDIVIDUAL));
					emi.setCantDeuPer(1);

					if (confirmado){
						EmiDAOFactory.getEmisionDAO().update(emi);
					}

					emi.ininitializaEngine(codigoEmi);
					emi.setListAuxDeuda(new ArrayList<AuxDeuda>());

					List<Date> listFechaEmision = DateUtil.getListFirstDayEachMonth(fechaPriPerEmiForRango, fechaUltPerEmiForRango);
					if(ListUtil.isNullOrEmpty(listFechaEmision)){
						continue;
					}

					emi.initializeMapCodRecCon();

					for(Date fechaAEmitir: listFechaEmision){
						Date fechaAnalisis = fechaAEmitir;

						// Obtenemos las Exenciones de la cuenta
						List<CueExe> listCueExe = cuenta.getListCueExeVigente(fechaAnalisis);

						// Creamos la deuda temporal
						Integer perEmi = DateUtil.getMes(fechaAEmitir);
						// BUG
						//AuxDeuda auxDeuda = emi.eval(cuenta, listCueExe, anio, perEmi, listAtributos);
						AuxDeuda auxDeuda = emi.eval(cuenta, listCueExe, anioEmi, perEmi, listAtributos);

						if (auxDeuda == null) {
							log.info("Emision de deuda cancelada.");
							continue;
						}

						Long periodoDeuda = auxDeuda.getAnio()*100 + auxDeuda.getPeriodo();
						Double pagosPeriodo = mapPerPagos.get(periodoDeuda.toString());
						Boolean tieneAjuste = mapAjustes.get(periodoDeuda.toString());
						Boolean excluirDeEmision = mapNoTocar.get(periodoDeuda.toString());
						String obs="";

						if(excluirDeEmision !=null && excluirDeEmision== true){
							// si el existe periodo de igual clasificacion, no se emite la deuda
							obs = "Existen periodo de igual clasificacion. NO SE EMITE DEUDA";

						}else if (tieneAjuste !=null && tieneAjuste== true) {
							// si el periodo tiene ajuste, no se emite la deuda
							obs = "Existen ajustes para el periodo. NO SE EMITE DEUDA";

						} else {
							//Determino si tiene pagos
							if (pagosPeriodo!=null)  {
								if (pagosPeriodo < auxDeuda.getImporte()) {
									// pago menos de la deuda que esta intentando generar
									obs ="Existen pagos para el periodo por: $" + StringUtil.formatDouble(pagosPeriodo) + ". ";
									obs+=" Debido a que es menor a la deuda a emitir, se crea con la reducción en el saldo.";

									auxDeuda.setSaldo(auxDeuda.getSaldo() - pagosPeriodo);

									/* issue 7804: cuando existe deuda RS y tiene que emitir otra sin anular
									 * la anterior, emitirla con idreccladeu = "Dif RS".
									 */
									String abrClaDeu = auxDeuda.getRecClaDeu().getAbrClaDeu();
									if(abrClaDeu.equals(RecClaDeu.ABR_RS)){
										//si la deuda que estaba creando por la diferencia es de RS, cambio la clasificacion a DifRS
										auxDeuda.setRecClaDeu(recClaDeuDifRS);
									}
								} else {
									// pago lo mismo o mas que la deuda que esta intentando generar
									// issue 6677: no se genera deudas RS si existe un pago para el periodo >= al importe RS a emitir 
									continue;
								}
							}

							emi.getListAuxDeuda().add(auxDeuda);

							if (confirmado)
								emi.createDeudaAdminFromAuxDeuda(auxDeuda);

							msgDeuda += "Emitido: " + getDescDeuda(auxDeuda, obs) + "\n";
							log.debug(msgDeuda);
						}
					}
				}
			}
			
			if(!this.estadoAux.equals(NovedadRS.PROCESADO_ERROR))
				this.estadoAux = NovedadRS.PROCESADO_OK;
			
			if (confirmado) 
				tx.commit();
			
			return msgDeuda;

			
		} catch (Exception e) {
			e.printStackTrace();
			if (tx!=null)tx.rollback();
			
			this.estadoAux = NovedadRS.PROCESADO_ERROR;
			return e.getMessage();
		} 
		
	}
	
	private String getDescDeuda(DeudaAdmin da) {
		String ret="";
		ret += "[";
		ret += da.getRecClaDeu().getAbrClaDeu() ;
		ret += "-"+ (da.getAnio()*100+da.getPeriodo());
		ret += "-"+DateUtil.formatDate(da.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK);
		ret += "-$"+StringUtil.formatDouble(da.getImporte());
		ret += "-$"+StringUtil.formatDouble(da.getSaldo());
		ret += "-FecVen:" + DateUtil.formatDate(da.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK);
		ret += "]";
		return ret;
	}
	

	
	private String getDescDeuda(AuxDeuda ad, String obs) {
		String ret="";
		ret += "[";
		ret += ad.getRecClaDeu().getAbrClaDeu() ;
		ret += "-"+ (ad.getAnio()*100+ad.getPeriodo());
		ret += "-"+DateUtil.formatDate(ad.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK);
		ret += "-$"+StringUtil.formatDouble(ad.getImporte());
		ret += "-$"+StringUtil.formatDouble(ad.getSaldo());
		ret += "-FecVen:" + DateUtil.formatDate(ad.getFechaVencimiento(), DateUtil.ddSMMSYYYY_MASK);
		if (obs!=null) 
			ret += " " + obs + " ";
		ret += "]";
		return ret;
	}
	
	
	
	
}
