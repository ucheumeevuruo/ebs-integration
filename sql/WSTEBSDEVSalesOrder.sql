DECLARE
l_api_version_number NUMBER := 1.0;
l_return_status                VARCHAR2(2000);
l_msg_count                      NUMBER;
l_msg_data                        VARCHAR2(2000);
l_debug_level                   NUMBER := 1; -- OM DEBUG LEVEL (MAX 5)

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
mo_global.set_policy_context('S',102);
fnd_global.apps_initialize (1111,50778,660);

/*****************INITIALIZE HEADER RECORD******************************/
l_header_rec := oe_order_pub.G_MISS_HEADER_REC;

/***********POPULATE REQUIRED ATTRIBUTES **********************************/
l_header_rec.operation := OE_GLOBALS.G_OPR_CREATE;
l_header_rec.order_type_id := 1001;  
l_header_rec.sold_to_org_id := 7373; --7052;
l_header_rec.ship_to_org_id := 17547; 
l_header_rec.invoice_to_org_id := 6373;
l_header_rec.sold_from_org_id :=124;
l_header_rec.salesrep_id := 100000041;
--l_header_rec.price_list_id := 1000; --p_price_list_id;--1000;
--l_header_rec.pricing_date := SYSDATE;
l_header_rec.transactional_curr_code := 'NGN'; --p_curr_code;--'USD';
l_header_rec.flow_status_code := 'ENTERED'; --p_flow_status_code;--
l_header_rec.cust_po_number :='TestEnter'; -- p_po_num;--'06112009-08';
l_header_rec.order_source_id := 0; --p_order_source_id;--0 ;
-- To BOOK the Sales Order
l_action_request_tbl(1) := oe_order_pub.G_MISS_REQUEST_REC;
l_action_request_tbl(1).request_type := oe_globals.g_book_order;
l_action_request_tbl(1).entity_code := oe_globals.g_entity_header;

/*****************INITIALIZE LINE RECORD********************************/
l_line_tbl(1) := oe_order_pub.G_MISS_LINE_REC;
l_line_tbl(1).operation := OE_GLOBALS.G_OPR_CREATE;
l_line_tbl(1).inventory_item_id := 13004;
l_line_tbl(1).ordered_quantity := 2;
l_line_tbl(1).ship_to_org_id := 17547;
--l_line_tbl(1).tax_code := 'Location';

/*****************CALLTO PROCESS ORDER API*********************************/
dbms_output.put_line('Calling API');
oe_order_pub.Process_Order( p_api_version_number => l_api_version_number,
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

/*****************CHECK RETURN STATUS***********************************/ 
IF l_return_status = FND_API.G_RET_STS_SUCCESS THEN
dbms_output.put_line('API Return status is success ');
--dbms_output.put_line('debug level ' l_debug_level);
if (l_debug_level > 0) then
dbms_output.put_line('success');
end if;
COMMIT;
ELSE
dbms_output.put_line('Return status failure ');
if (l_debug_level > 0) then
  dbms_output.put_line('failure');
end if;
ROLLBACK;
END IF;

-- Display Return Status
if (l_debug_level > 0) then
DBMS_OUTPUT.PUT_LINE('process ORDER ret status IS: ' ||l_return_status);
DBMS_OUTPUT.PUT_LINE('process ORDER msg data IS: ' ||l_msg_data);
DBMS_OUTPUT.PUT_LINE('process ORDER msg COUNT IS: '||l_msg_count);
DBMS_OUTPUT.PUT_LINE('header.order_number IS: ' ||to_char(l_header_rec_out.order_number));
DBMS_OUTPUT.PUT_LINE('header.return_status IS: '||l_header_rec_out.return_status);
DBMS_OUTPUT.PUT_LINE('header.booked_flag IS: '||l_header_rec_out.booked_flag);
DBMS_OUTPUT.PUT_LINE('header.header_id IS: '||l_header_rec_out.header_id);
DBMS_OUTPUT.PUT_LINE('header.order_source_id IS: '||l_header_rec_out.order_source_id);
DBMS_OUTPUT.PUT_LINE('header.flow_status_code IS: '||l_header_rec_out.flow_status_code);
end if;
--Display ERROR Messages
IF (l_debug_level > 0) THEN
FOR i IN 1 .. l_msg_count LOOP
     oe_msg_pub.get( p_msg_index => i, p_encoded =>Fnd_Api.G_FALSE, p_data => l_data, p_msg_index_out => l_msg_index);
      dbms_output.put_line( i|| ') '|| l_data);
      dbms_output.put_line('message index is: ' ||l_msg_index);
END LOOP;
END IF;
IF (l_debug_level > 0) THEN
  DBMS_OUTPUT.PUT_LINE('Debug = ' ||OE_DEBUG_PUB.G_DEBUG);
  DBMS_OUTPUT.PUT_LINE('Debug Level = ' ||to_char(OE_DEBUG_PUB.G_DEBUG_LEVEL));
  DBMS_OUTPUT.PUT_LINE('Debug File = ' ||OE_DEBUG_PUB.G_DIR||'/'||OE_DEBUG_PUB.G_FILE);

  DBMS_OUTPUT.PUT_LINE('****************************************************');
  --OE_DEBUG_PUB.DEBUG_OFF;
END IF;
END;

select * from oe_order_headers_all
