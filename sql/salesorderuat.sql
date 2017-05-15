--SET SERVEROUTPUT ON;
CREATE TYPE array_table AS TABLE OF VARCHAR2 (50);
TYPE product IS TABLE OF array_table INDEX BY PLS_INTEGER;

CREATE OR REPLACE PROCEDURE salesOrder(
	order_id IN NUMBER,  
	sold_to_id IN NUMBER,
	ship_to_id IN NUMBER, 
	invoice_to_id IN NUMBER,
	sold_from_id IN NUMBER,
	salesrep_id IN NUMBER,
	price_id IN NUMBER,
	trx_curr_code IN VARCHAR,
	status_code IN VARCHAR,
	po_num IN NUMBER,
	source_id IN NUMBER,
	my_hmap IN product
)
AS
DECLARE
l_api_version_number NUMBER := 1.0;
l_return_status      VARCHAR2(2000);
l_msg_count          NUMBER;
l_msg_data           VARCHAR2(2000);
l_debug_level        NUMBER := 5; -- OM DEBUG LEVEL (MAX 5)
  i varchar2(30);
-- IN Variables --
l_header_rec oe_order_pub.header_rec_type;
l_line_tbl oe_order_pub.line_tbl_type;
l_action_request_tbl oe_order_pub.Request_Tbl_Type;

-- OUT Variables --
l_header_rec_out oe_order_pub.header_rec_type;
l_header_val_rec_out oe_order_pub.header_val_rec_type;
l_header_adj_tbl_out oe_order_pub.header_adj_tbl_type;
l_header_adj_val_tbl_out oe_order_pub.header_adj_val_tbl_type;
l_header_price_att_tbl_out oe_order_pub.header_price_att_tbl_type;
l_header_adj_att_tbl_out oe_order_pub.header_adj_att_tbl_type;
l_header_adj_assoc_tbl_out oe_order_pub.header_adj_assoc_tbl_type;
l_header_scredit_tbl_out oe_order_pub.header_scredit_tbl_type;
l_header_scredit_val_tbl_out oe_order_pub.header_scredit_val_tbl_type;
l_line_tbl_out oe_order_pub.line_tbl_type;
l_line_val_tbl_out oe_order_pub.line_val_tbl_type;
l_line_adj_tbl_out oe_order_pub.line_adj_tbl_type;
l_line_adj_val_tbl_out oe_order_pub.line_adj_val_tbl_type;
l_line_price_att_tbl_out oe_order_pub.line_price_att_tbl_type;
l_line_adj_att_tbl_out oe_order_pub.line_adj_att_tbl_type;
l_line_adj_assoc_tbl_out oe_order_pub.line_adj_assoc_tbl_type;
l_line_scredit_tbl_out oe_order_pub.line_scredit_tbl_type;
l_line_scredit_val_tbl_out oe_order_pub.line_scredit_val_tbl_type;
l_lot_serial_tbl_out oe_order_pub.lot_serial_tbl_type;
l_lot_serial_val_tbl_out oe_order_pub.lot_serial_val_tbl_type;
l_action_request_tbl_out oe_order_pub.request_tbl_type;
l_msg_index NUMBER;
l_data VARCHAR2(2000);
l_loop_count NUMBER;
l_debug_file VARCHAR2(200);
--book API vars 
b_return_status VARCHAR2(200);
b_msg_count NUMBER;
b_msg_data VARCHAR2(2000);
BEGIN

/*****************INITIALIZE DEBUG INFO*************************************/
IF (l_debug_level > 0) THEN
l_debug_file := OE_DEBUG_PUB.Set_Debug_Mode('FILE');
oe_debug_pub.initialize;
oe_debug_pub.setdebuglevel(l_debug_level);
Oe_Msg_Pub.initialize;
END IF;

/*****************INITIALIZE ENVIRONMENT*************************************/ 
mo_global.init('ONT');
mo_global.set_policy_context('S',101);
fnd_global.apps_initialize (1187,50988,660);

/*****************INITIALIZE HEADER RECORD******************************/
l_header_rec := oe_order_pub.G_MISS_HEADER_REC;

/***********POPULATE REQUIRED ATTRIBUTES **********************************/
l_header_rec.operation := OE_GLOBALS.G_OPR_CREATE;
l_header_rec.order_type_id := order_id;  
l_header_rec.sold_to_org_id := sold_to_id; --7052;
l_header_rec.ship_to_org_id := ship_to_id;  --123; 
l_header_rec.invoice_to_org_id := invoice_to_id;  --1105;
l_header_rec.sold_from_org_id := sold_from_id;
l_header_rec.salesrep_id := salesrep_id;
l_header_rec.price_list_id := price_id; --p_price_list_id;--1000;
l_header_rec.pricing_date := SYSDATE;
l_header_rec.transactional_curr_code := trx_curr_code; --p_curr_code;--'USD';//string
l_header_rec.flow_status_code := status_code; --p_flow_status_code;--//string
l_header_rec.cust_po_number := po_num; -- p_po_num;--'06112009-08';
l_header_rec.order_source_id := source_id; --p_order_source_id;--0 ;
-- To BOOK the Sales Order
l_action_request_tbl(1) := oe_order_pub.G_MISS_REQUEST_REC;
l_action_request_tbl(1).request_type := oe_globals.g_book_order;
l_action_request_tbl(1).entity_code := oe_globals.g_entity_header;

/*****************INITIALIZE LINE RECORD********************************/
-- or to loop on every element - it's a "collection"
  i := my_hmap.FIRST;

  while (i is not null)  loop  
    dbms_output.put_line(my_hmap(i).inventory);  
    l_line_tbl(i) := oe_order_pub.G_MISS_LINE_REC;
    l_line_tbl(i).operation := OE_GLOBALS.G_OPR_CREATE;
    l_line_tbl(i).inventory_item_id := my_hmap(i)(1);
    l_line_tbl(i).ordered_quantity := my_hmap(i)(2);
    l_line_tbl(i).ship_to_org_id := my_hmap(i)(3);    
    i := my_hmap.NEXT(i);
  end loop;
--l_line_tbl(1).tax_code := 'Location';

/*****************CALLTO PROCESS ORDER API*********************************/
--dbms_output.put_line('Calling API');
oe_order_pub.Process_Order( 
p_api_version_number => l_api_version_number,
p_header_rec => l_header_rec,
p_line_tbl => l_line_tbl,
p_action_request_tbl => l_action_request_tbl, 
--OUT variables
x_header_rec => l_header_rec_out,
x_header_val_rec => l_header_val_rec_out,
x_header_adj_tbl => l_header_adj_tbl_out,
x_header_adj_val_tbl => l_header_adj_val_tbl_out,
x_header_price_att_tbl => l_header_price_att_tbl_out,
x_header_adj_att_tbl => l_header_adj_att_tbl_out,
x_header_adj_assoc_tbl => l_header_adj_assoc_tbl_out,
x_header_scredit_tbl => l_header_scredit_tbl_out,
x_header_scredit_val_tbl => l_header_scredit_val_tbl_out,
x_line_tbl => l_line_tbl_out,
x_line_val_tbl => l_line_val_tbl_out,
x_line_adj_tbl => l_line_adj_tbl_out,
x_line_adj_val_tbl => l_line_adj_val_tbl_out,
x_line_price_att_tbl => l_line_price_att_tbl_out,
x_line_adj_att_tbl => l_line_adj_att_tbl_out,
x_line_adj_assoc_tbl => l_line_adj_assoc_tbl_out,
x_line_scredit_tbl => l_line_scredit_tbl_out,
x_line_scredit_val_tbl => l_line_scredit_val_tbl_out,
x_lot_serial_tbl => l_lot_serial_tbl_out,
x_lot_serial_val_tbl => l_lot_serial_val_tbl_out,
x_action_request_tbl => l_action_request_tbl_out,
x_return_status => l_return_status,
x_msg_count => l_msg_count,
x_msg_data => l_msg_data);

z:=l_return_status;
END;