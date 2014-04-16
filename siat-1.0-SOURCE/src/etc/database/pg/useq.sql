-- Usar asi: psql -U USER DB -tf useq.sql  | psql -U USER DB

select
	'SELECT pg_catalog.setval(pg_get_serial_sequence(''' || relname ||
	''', ''id''), (SELECT MAX(id) FROM ' || relname || ')+1);'
from 
	pg_class 
where 
	relkind ='r' and relname not like 'pg_%' and relname not like 'sql_%' 
order by 
	relname;
