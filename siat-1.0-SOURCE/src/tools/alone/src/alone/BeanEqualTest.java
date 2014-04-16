//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone; 

import org.hibernate.Session;

import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.gde.buss.bean.ConvenioCuota;
import ar.gov.rosario.siat.gde.buss.bean.EstadoConCuo;

public class BeanEqualTest {

	
	public static void main(String[] args) throws Exception {
		System.out.printf("DisParTest\n\n");
		BeanEqualTest main = new BeanEqualTest();
		
		Alone.init(false, false);
		
		main.process();
	}

	public void process() {  
	   Session session = null;
	   try{
			session = SiatHibernateUtil.currentSession();
			ConvenioCuota cc = ConvenioCuota.getByIdNull(4975139L); //idestadoconcuo = 1 

			//public static final Long ID_IMPAGO = 1L;
			//public static final Long ID_PAGO_BUENO = 2L;
			//public static final Long ID_PAGO_A_CUENTA = 3L;
			
			System.out.printf("EstadoConCuo.id = %s\n", cc.getEstadoConCuo().getId());
			System.out.printf("EstadoConCuo == 1? %s\n", cc.getEstadoConCuo().equals(EstadoConCuo.ID_IMPAGO));
			System.out.printf("EstadoConCuo == 2? %s\n", cc.getEstadoConCuo().equals(EstadoConCuo.ID_PAGO_BUENO));
			System.out.printf("EstadoConCuo == 3? %s\n", cc.getEstadoConCuo().equals(EstadoConCuo.ID_PAGO_A_CUENTA));
			
			
	   } catch ( Exception e ) {
		   System.out.println("Migrania Error en: "+ e);
	   } finally {
			SiatHibernateUtil.closeSession();
	   }
	}
	
	
}
