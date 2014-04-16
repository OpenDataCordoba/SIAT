//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone; 

import coop.tecso.demoda.iface.helper.StringUtil;

public class MantisTest {

	
	public static void main(String[] args) throws Exception {
		System.out.printf("MantisTest\n\n");
		MantisTest main = new MantisTest();
		
		Alone.init(false, false);
		
		main.test();
	}

	public boolean test(){
	   
	  // Session session = null;
	  // Transaction tx = null; 
	   try{
	//		session = SiatHibernateUtil.currentSession();
	//		tx = session.beginTransaction();

		
			System.out.println("CodBar:      605727038936374011221932340560780");
			System.out.println("Comprimido: "+StringUtil.genCodBarComprimidoForAfip("60572703893637401122193234056078"));
			/*DeudaAdmin deudaAdmin = DeudaAdmin.getByIdNull(39608960L);
			if(deudaAdmin != null){
				SiatBussCache.getInstance().getListTipObjImpAtrManual(deudaAdmin.getCuenta().getObjImp().getTipObjImp().getId());
			}else{
				System.out.println("Maldición! No esta en nuestra base.");
			}

			
			SiatHibernateUtil.closeSession();
			session = SiatHibernateUtil.currentSession();
			tx = session.beginTransaction();
			
			//DeudaAdmin deudaAdmin = DeudaAdmin.getByIdNull(39608960L);
			deudaAdmin = DeudaAdmin.getByIdNull(39608960L);
			if(deudaAdmin != null){
				deudaAdmin.getCuenta().getObjImp().getDefinitionValue(new Date());
			}else{
				System.out.println("Maldición! No esta en nuestra base.");
			}*/
			
	   }catch ( Exception e ) {
		    //System.out.println("Migrania Error en: "+ e);
			e.printStackTrace();
		   return false;
	   } finally {
		//	SiatHibernateUtil.closeSession();
	   }
		return true;
	}
	
	
}
