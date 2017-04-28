DECLARE
    PO_header_id_v           		NUMBER;
    PO_line_id_v             		NUMBER;
    PO_dist_id_v             		NUMBER;
    v_quantity_ordered      	 	NUMBER;
	v_batch_id              	 	NUMBER;
	v_currency_code          		VARCHAR2(10);
	v_agent_id               		NUMBER;
	v_approval_status		 		VARCHAR2;
	v_vendor_id				 		NUMBER;
	v_vendor_name			 		VARCHAR2;
	vendor_site_code		 		NUMBER;
	v_ship_to_location		 		VARCHAR2
	v_bill_to_location		 		VARCHAR2
	v_unit_price 			 		NUMBER;
	v_ship_to_organization_code	   	NUMBER
	v_ship_to_location		 		VARCHAR2;
	v_ship_to_organization_id		NUMBER
	v_ship_to_location_id			NUMBER
                
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

INSERT INTO po.po_headers_interface
(
	interface_header_id,
	batch_id,
	process_code,
	action,
	org_id,
	document_type_code,
	currency_code,
	agent_id,
	vendor_name,
	approval_status,
	vendor_id,     --='INCOMPLETE', VENDOR_ID = '33'
	vendor_site_code,
	ship_to_location,
	bill_to_location
) 
VALUES
(
	PO_header_id_v, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
);

INSERT INTO po.po_lines_interface
(
	interface_line_id,
	interface_header_id,
	line_num,
	shipment_num,
	line_type,
	item,
	unit_of_measure,
	quantity,
	unit_price,
	ship_to_organization_code,
	ship_to_organization_id,
	ship_to_location_id,         ---= '102', ship_to_location_id = '163'
	ship_to_location
)
VALUES
(
	PO_line_id_v, PO_header_id_v, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? 
);

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
	PO_header_id_v,
	PO_line_id_v,
	PO_dist_id_v,
	?,
	?
);
--1002); -- Code Combination ID for the Charge Account to be used on the Distribution

commit;

