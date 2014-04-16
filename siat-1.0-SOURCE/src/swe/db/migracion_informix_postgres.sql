COPY swe_aplicacion (id, codigo, descripcion, segtimeout, maxnivelmenu, usuario, fechaultmdf, estado) FROM '/var/lib/postgresql/swe.exp/swe_a00194_utf8.unl' WITH DELIMITER '|';

COPY swe_modapl FROM '/var/lib/postgresql/swe.exp/swe_m00195_utf8.unl' WITH DELIMITER '|';

COPY swe_accmodapl FROM '/var/lib/postgresql/swe.exp/swe_a00196_utf8.unl' WITH DELIMITER '|';

COPY swe_itemmenu FROM '/var/lib/postgresql/swe.exp/swe_i00197_utf8.unl' WITH DELIMITER '|' NULL '';

COPY swe_rolapl FROM '/var/lib/postgresql/swe.exp/swe_r00198_utf8.unl' WITH DELIMITER '|' NULL '';

COPY swe_rolaccmodapl FROM '/var/lib/postgresql/swe.exp/swe_r00199_utf8.unl' WITH DELIMITER '|' NULL '';

COPY swe_usrrolapl FROM '/var/lib/postgresql/swe.exp/swe_u00200_utf8.unl' WITH DELIMITER '|' NULL '';

COPY swe_usrapl FROM '/var/lib/postgresql/swe.exp/swe_u00201_utf8.unl' WITH DELIMITER '|' NULL '';

COPY swe_usrapladmswe FROM '/var/lib/postgresql/swe.exp/swe_u00202_utf8.unl' WITH DELIMITER '|' NULL '';

