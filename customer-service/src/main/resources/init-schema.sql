DROP SCHEMA IF EXISTS customer CASCADE;

CREATE SCHEMA CUSTOMER;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE customer.customers(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);

DROP MATERIALIZED VIEW IF EXISTS customer.order_customer_m_view;
-- A materialized view get the view data and save in local disc, not needing to query in database. It's a better performance
CREATE MATERIALIZED VIEW customer.order_customer_m_view TABLESPACE pg_default
AS
SELECT id,
    username,
    first_name,
    last_name
  FROM customer.customers
WITH DATA;

refresh materialized VIEW customer.order_customer_m_view;

-- CREATE THIS FUNCTION FOR EACH DATA MANIPULATION, CALLS IT FUNCTION AND REFRESH THE MATERIALIZED VIEW
CREATE OR replace function customer.refresh_order_customer_m_view()
returns trigger
AS '
  BEGIN
        refresh materialized VIEW customer.order_customer_m_view;
        return null;
  END;
' LANGUAGE plpgsql;

CREATE trigger refresh_order_customer_m_view
after INSERT OR UPDATE OR DELETE OR truncate
ON customer.customers FOR each statement
EXECUTE PROCEDURE customer.refresh_order_customer_m_view();