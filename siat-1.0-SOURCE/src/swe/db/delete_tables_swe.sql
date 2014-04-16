--Drop de aplicacion SWE--


--DELETE SWE_ITEMMENU--

delete from swe_itemmenu where idaplicacion=1;

--DELETE SWE_ROLACCMODAPL join SWE_ACCMODAPL--

SElECT ma.id from swe_rolaccmodapl ma INNER JOIN swe_accmodapl am on (ma.idaccmodapl == am.id) where am.idaplicacion=1  INTO TEMP Tempjoin;

delete from swe_rolaccmodapl where swe_rolaccmodapl.id in  (select Tempjoin.id from Tempjoin);


--DELETE SWE_ACCMODAPL--

delete from swe_accmodapl where idaplicacion=1;

--DELETE SWE_MODAPL--

delete from swe_modapl where idaplicacion=1;

--DELETE SWE_ROLACCMODAPL (SWE_USRROLAPL join SWE_USRAPL join SWE_ROLAPL)--

SElECT ua.id from swe_usrapl ua INNER JOIN swe_usrrolapl am on (ua.id == am.idusrapl) inner join swe_rolapl ra on (ra.id == am.idrolapl) where ua.idaplicacion=1  INTO TEMP Tempjoin2;

delete from swe_rolaccmodapl where swe_rolaccmodapl.id in  (select Tempjoin2.id from Tempjoin2);


--DELETE SWE_ROLACCMODAPL join SWE_USRROLAPL --

SElECT ur.id from swe_rolaccmodapl ur INNER JOIN swe_usrrolapl ram on (ur.idrolapl == ram.idrolapl) where ur.idrolapl=1  INTO TEMP Tempjoin3;

delete from swe_rolaccmodapl where swe_rolaccmodapl.id in  (select Tempjoin3.id from Tempjoin3);


--DELETE SWE_USRROLAPL--

delete from swe_usrrolapl where swe_usrrolapl.idusrapl=1;


--DELETE SWE_ROLAPL--

delete from swe_usrapl where swe_usrapl.idaplicacion=1;

--DELETE SWE_USRAPL--

delete from swe_rolapl where swe_rolapl.idaplicacion=1;

--DELETE SWE_USRAPLICACION--

delete from swe_aplicacion where swe_aplicacion.id=1;


