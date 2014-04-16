//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Session;

import ar.gov.rosario.swe.SweCommonError;
import ar.gov.rosario.swe.SweDAOFactory;
import ar.gov.rosario.swe.buss.dao.SweHibernateUtil;
import ar.gov.rosario.swe.iface.model.UsrAplVO;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.StringUtil;


/**
 * Usuario
 * 
 * @author tecso
 * 
 */

@Entity
@Table(name = "swe_usrapl")
@SequenceGenerator(
    name="usrapl_seq",
    sequenceName="swe_usrapl_id_seq",
    allocationSize = 0
)

public class UsrApl extends BaseBO {
	
	@Id @GeneratedValue(generator="usrapl_seq",strategy=GenerationType.SEQUENCE)
    private Long id;
 
    public Long getId() {
        return id;
   }
	
	private static final long serialVersionUID = 1L;

	@Column(name = "username")
	private String     username;	
	@Transient
	private String password;	
	@Transient
	private String passRetype;		
	@Column(name = "uid")
	private Long       uid;
	@Column(name = "fechaAlta")
	private Date       fechaAlta;
	@Column(name = "fechaBaja")
	private Date       fechaBaja;
	@Column(name = "permiteWeb")
    private Integer	   permiteWeb;	
	
	@OneToOne
    @JoinColumn(name="idAplicacion")	
	private Aplicacion aplicacion;
	
    @OneToMany()
    @JoinColumn(name="idUsrApl")    
	private List<UsrRolApl> listUsrRolApl;	
    
    @OneToMany()
    @JoinColumn(name="idUsrApl")   
	private List <UsrAplAdmSwe> listAplicacionesPermitidas = new ArrayList<UsrAplAdmSwe>() ;
	
	
	public UsrApl() {
		super();
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}
	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}	
	public void setPassRetype(String passRetype) {
		this.passRetype = passRetype;
	}

	public String getPassRetype() {
		return passRetype;
	}

	public List<UsrRolApl> getListUsrRolApl() {
		return listUsrRolApl;
	}
	
	public void setListUsrRolApl(List<UsrRolApl> listUsrRolApl) {
		this.listUsrRolApl = listUsrRolApl;
	}

	public static UsrApl getById(Long id) {
		return (UsrApl) SweDAOFactory.getUsrAplDAO().getById(id);
	}
	
	
	public List<UsrAplAdmSwe> getListAplicacionesPermitidas() {
		return listAplicacionesPermitidas;
	}

	public void setListAplicacionesPermitidas(
			List<UsrAplAdmSwe> listAplicacionesPermitidas) {
		this.listAplicacionesPermitidas = listAplicacionesPermitidas;
	}

	public Integer getPermiteWeb() {
		return permiteWeb;
	}

	public void setPermiteWeb(Integer permiteWeb) {
		this.permiteWeb = permiteWeb;
	}
	

	/**
	 * Obtiene la lista total de acciones permitadas para este usuario.
	*/
	public List<AccModApl> getListAccModUsr() throws Exception {
		return SweDAOFactory.getAccModAplDAO().findByUsr(this);
	}

	public void loadFromVO(UsrAplVO usrAplVO) throws DemodaServiceException{
		setUsername(usrAplVO.getUsername()); 
		setUid(usrAplVO.getUid());
		setEstado(usrAplVO.getEstado().getId());

		Aplicacion aplicacion = Aplicacion.getById(usrAplVO.getAplicacion().getId());
        if (aplicacion == null) {
        	throw new DemodaServiceException("No se encontro registro Aplicacion en la Base de Datos.");
        }
        setAplicacion(aplicacion);
	}

	public void loadFromVOForUpdate(UsrAplVO usrAplVO) throws DemodaServiceException{
		setUsername(usrAplVO.getUsername());
		setEstado(usrAplVO.getEstado().getId());
		setPermiteWeb(usrAplVO.getPermiteWeb().getId());
		//setUid(usrAplVO.getUid());
	}

	
	/**
	 * Requeridos: username y fechaAlta. 
	 * @return boolean
	 * @throws SegWebException 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();

		//Validaciones de VO
		if (StringUtil.isNullOrEmpty(getUsername())) {
			addRecoverableError(SweCommonError.USR_APL_USERNAME_REQUIRED);
		}
		if (this.fechaAlta == null) {
			addRecoverableError(SweCommonError.USR_APL_FECHAALTA_REQUIRED);
		}

		if (hasError()) {
			return false;
		}

		return true;
	}

	/**
	 * Requeridos: nombreUsuario y fechaAlta
	 * @return
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();

		// Validaciones de VO
		if (StringUtil.isNullOrEmpty(getUsername())) {
			addRecoverableError(SweCommonError.USR_APL_USERNAME_REQUIRED);
		}	
		if (this.fechaAlta == null) {
			addRecoverableError(SweCommonError.USR_APL_FECHAALTA_REQUIRED);
		}

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();

		if (SweHibernateUtil.hasReference(this, UsrRolApl.class, "usrApl")) {
			addRecoverableError(SweCommonError.USR_APL_HASREF);
		}

		if (hasError())
			return false;
		return true;
	}

	/**
	 * @param idRolApl id de un rol de aplicacion
	 * @return true si este usuario pertenece al rol idRolApl del parametro.
	 */
	public boolean hasRol(Long idRolApl) {
		for (UsrRolApl item: getListUsrRolApl()) {
			if (item.getRolApl().getId().longValue() == idRolApl.longValue()) {
				return true;
			}
		}
		return false;
	}

	//m�todos de instancia
	
	/**
	 * Llena la lista de aplicaciones permitidas, con el array de ids que le llega
	 * @param listIdsAppSelected array de ids de aplicaciones
	 */
	public void agregarAplicacionesPermitidas(Long[] listIdsAppSelected) {
		for(Long idApp: listIdsAppSelected){
			this.getListAplicacionesPermitidas().add(new UsrAplAdmSwe(Aplicacion.getById(idApp), this));
		}		
	}
	
	/**
	 * Actualiza la lista de aplicaciones permitidas para el usuario.
	 * Elimina las actuales e inserta las nuevas 
	 * @param idsAppSelected ids de las nuevas aplicaciones permitidas
	 * @throws Exception
	 */
	public void actualizarAplicacionesPermitidas(Long[] idsAppSelected) throws Exception{
		SweDAOFactory.getUsrAplAdmiSweDAO().deleteForUser(getId());
		Session session = SweHibernateUtil.currentSession();
		session.flush();
		agregarAplicacionesPermitidas(idsAppSelected);
	}

	public UsrApl getUsrAplForClone(UsrApl usrAplNew){
		UsrApl usrApl = new UsrApl();
		usrApl.fechaAlta = usrAplNew.fechaAlta;
		usrApl.fechaBaja = usrAplNew.fechaBaja;
		usrApl.id = usrAplNew.id;
		usrApl.password = usrAplNew.password;
		usrApl.uid = usrAplNew.uid;
		usrApl.username = usrAplNew.username;
		usrApl.permiteWeb = this.permiteWeb;
		usrApl.aplicacion = this.aplicacion;
		usrApl.listUsrRolApl = new ArrayList<UsrRolApl>(this.listUsrRolApl);
		Collections.copy(usrApl.listUsrRolApl, this.listUsrRolApl);
		//usrApl.listUsrRolApl = this.listUsrRolApl;
		usrApl.listAplicacionesPermitidas = new ArrayList<UsrAplAdmSwe>(this.listAplicacionesPermitidas);
		Collections.copy(usrApl.listAplicacionesPermitidas , this.listAplicacionesPermitidas);
		//usrApl.listAplicacionesPermitidas = this.listAplicacionesPermitidas;
		return usrApl; 
	}
	
	public UsrApl setUsrAplForClone(UsrApl usrApl){
//		UsrApl usrApl = new UsrApl();
		usrApl.permiteWeb = this.permiteWeb;
		usrApl.aplicacion = this.aplicacion;
		usrApl.listUsrRolApl = new ArrayList<UsrRolApl>(this.listUsrRolApl);
		Iterator<UsrRolApl> iteratorListUsrRolApl = this.listUsrRolApl.iterator();
		while ( iteratorListUsrRolApl.hasNext() ) {
			UsrRolApl usrRolApl = (UsrRolApl) iteratorListUsrRolApl.next();
			usrApl.listUsrRolApl.add(usrRolApl);
		}
//		Collections.copy(usrApl.listUsrRolApl, this.listUsrRolApl);
		//usrApl.listUsrRolApl = this.listUsrRolApl;
		usrApl.listAplicacionesPermitidas = new ArrayList<UsrAplAdmSwe>(this.listAplicacionesPermitidas);
		Iterator<UsrAplAdmSwe> iteratorListAplicacionesPermitidas = this.listAplicacionesPermitidas.iterator();
		while ( iteratorListAplicacionesPermitidas.hasNext() ) {
			UsrAplAdmSwe usrAplAdmSwe = (UsrAplAdmSwe) iteratorListAplicacionesPermitidas.next();
			usrApl.listAplicacionesPermitidas.add(usrAplAdmSwe);
		}
//		Collections.copy(usrApl.listAplicacionesPermitidas , this.listAplicacionesPermitidas);
		//usrApl.listAplicacionesPermitidas = this.listAplicacionesPermitidas;
		return usrApl; 
	}
	
}
