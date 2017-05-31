CREATE OR REPLACE PROCEDURE PURCHASE_ORDER(
	poNumber IN VARCHAR2(200);
	my_hmap IN product;
)
AS
PO_header_id_v           		NUMBER;
PO_line_id_v             		NUMBER;
PO_dist_id_v             		NUMBER;
v_quantity_ordered      	 	NUMBER;
v_org_id						po_headers_interface.org_id%type;
v_batch_id              	 	po_headers_interface.batch_id%type;
v_currency_code          		po_headers_interface.currency_code%type;
v_agent_id               		po_headers_interface.agent_id%type;
v_approval_status		 		po_headers_interface.approval_status%type;
v_vendor_id				 		po_headers_interface.vendor_id%type;
v_vendor_name			 		po_headers_interface.vendor_name%type;
v_vendor_site_code		 		po_headers_interface.vendor_site_code%type;
v_bill_to_location_id		 	po_headers_interface.bill_to_location_id%type;	
v_bill_to_location		 		po_headers_interface.bill_to_location%type;
v_unit_price 			 		NUMBER;
v_ship_to_location		 		VARCHAR2(2000);
v_ship_to_organization_code	   	NUMBER;
v_ship_to_organization_id		NUMBER;
v_ship_to_location_id			po_headers_interface.ship_to_location_id%type;

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
    
	SELECT 
		a.ORG_ID, 
		a.CURRENCY_CODE, 
		a.AGENT_ID,  
		b.VENDOR_NAME, 
		a.AUTHORIZATION_STATUS,
		b.VENDOR_ID, 
		a.VENDOR_SITE_ID,
		a.SHIP_TO_LOCATION_ID, 
		a.BILL_TO_LOCATION_ID
	INTO 
		v_org_id,
		v_currency_code,
		v_agent_id,
		v_vendor_name,
		v_approval_status,
		v_vendor_id,
		v_vendor_site_code,
		v_ship_to_location_id,
		v_bill_to_location_id
	FROM po_headers_all a
	INNER JOIN ap_suppliers b ON b.VENDOR_ID = a.VENDOR_ID
	WHERE PO_HEADER_ID = poNumber;

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
	vendor_id, 
	vendor_site_code,
	ship_to_location,
	bill_to_location
)
VALUES
(
	PO_header_id_v,
	4,
	'PENDING',
	'ORIGINAL',
	v_org_id,
	'STANDARD',
	v_currency_code,
	v_agent_id,
	v_vendor_name,
	'INCOMPLETE',
	v_vendor_id,
	v_vendor_site_code,
	v_ship_to_location,
	v_bill_to_location
); --,

 i := my_hmap.FIRST;

  while (i is not null)  loop  
    INSERT INTO po.po_lines_interface
	(
		interface_line_id,
		interface_header_id,
		line_num,--Line Number
		shipment_num,--Shipment Number
		line_type,
		item,--Product
		unit_of_measure,--Unit of Measure
		quantity,--Quantity
		unit_price,--Unit Price
		ship_to_organization_code,
		ship_to_organization_id,
		ship_to_location_id,         ---= '102', ship_to_location_id = '163'
		ship_to_location
	)
	VALUES
	(
		PO_line_id_v,
		PO_header_id_v,
		my_hmap(i)(0),-- Line NUMBER
		my_hmap(i)(1),--Shipment NUMBER
		'Each',-- Line Type
		my_hmap(i)(2),--Item
		my_hmap(i)(3),--Unit og Measure
		my_hmap(i)(4),--Quantity
		my_hmap(i)(5),--Unit Price
		my_hmap(i)(6),--Org Code
		my_hmap(i)(7),--Org Id
		my_hmap(i)(9),--Location Id
		my_hmap(i)(10)--Location
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
		my_hmap(i)(0),
		my_hmap(i)(11)
	);
    i := my_hmap.NEXT(i);
  end loop;



--1002); -- Code Combination ID for the Charge Account to be used on the Distribution

commit;
END;