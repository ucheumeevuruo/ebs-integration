DECLARE
    PO_header_id_v           		NUMBER;
    PO_line_id_v             		NUMBER;
    PO_dist_id_v             		NUMBER;
    v_quantity_ordered      	 	NUMBER;
	v_batch_id              	 	NUMBER;
	v_currency_code          		NUMBER;
	v_agent_id               		NUMBER;
                
BEGIN
   SELECT po.po_headers_interface_s.NEXTVAL
     INTO PO_header_id_v
     FROM DUAL;
   SELECT po.po_lines_interface_s.NEXTVAL
     INTO PO_line_id_v
     FROM DUAL;
   SELECT po.po_distributions_interface_s.NEXTVAL
     INTO PO_dist_id_v
     FROM DUAL;

INSERT INTO po.po_distributions_interface
(
	interface_header_id,
	interface_line_id,
	interface_distribution_id,
	distribution_num,
	quantity_ordered
)
--charge_account_id)
VALUES
(
	?,
	?,
	?,
	?,
	?
);
--1002); -- Code Combination ID for the Charge Account to be used on the Distribution

commit;
END;
