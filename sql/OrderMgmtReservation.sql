CREATE PROCEDURE ORDER_MGMT_RESERVATION(
	order_number IN NUMBER,
	customer_array OUT customers_array
)
AS 
   l_header_rec                 oe_order_pub.header_rec_type; 
   l_line_tbl                   oe_order_pub.line_tbl_type; 
   l_action_request_tbl         oe_order_pub.request_tbl_type; 
   l_header_adj_tbl             oe_order_pub.header_adj_tbl_type; 
   l_line_adj_tbl               oe_order_pub.line_adj_tbl_type; 
   l_header_scr_tbl             oe_order_pub.header_scredit_tbl_type; 
   l_line_scredit_tbl           oe_order_pub.line_scredit_tbl_type; 
   l_request_rec                oe_order_pub.request_rec_type; 
   l_return_status              VARCHAR2(1000); 
   l_msg_count                  NUMBER; 
   l_msg_data                   VARCHAR2(1000); 
   p_api_version_number         NUMBER := 1.0; 
   p_init_msg_list              VARCHAR2(10) := fnd_api.g_false; 
   p_return_values              VARCHAR2(10) := fnd_api.g_false; 
   p_action_commit              VARCHAR2(10) := fnd_api.g_false; 
   x_return_status              VARCHAR2(1); 
   x_msg_count                  NUMBER; 
   x_msg_data                   VARCHAR2(100); 
   p_header_rec                 oe_order_pub.header_rec_type := oe_order_pub.g_miss_header_rec; 
   x_header_rec                 oe_order_pub.header_rec_type := oe_order_pub.g_miss_header_rec; 
   p_old_header_rec             oe_order_pub.header_rec_type := oe_order_pub.g_miss_header_rec; 
   p_header_val_rec             oe_order_pub.header_val_rec_type := oe_order_pub.g_miss_header_val_rec; 
   p_old_header_val_rec         oe_order_pub.header_val_rec_type := oe_order_pub.g_miss_header_val_rec; 
   p_header_adj_tbl             oe_order_pub.header_adj_tbl_type := oe_order_pub.g_miss_header_adj_tbl; 
   p_old_header_adj_tbl         oe_order_pub.header_adj_tbl_type := oe_order_pub.g_miss_header_adj_tbl; 
   p_header_adj_val_tbl         oe_order_pub.header_adj_val_tbl_type := oe_order_pub.g_miss_header_adj_val_tbl; 
   p_old_header_adj_val_tbl     oe_order_pub.header_adj_val_tbl_type := oe_order_pub.g_miss_header_adj_val_tbl; 
   p_header_price_att_tbl       oe_order_pub.header_price_att_tbl_type := oe_order_pub.g_miss_header_price_att_tbl; 
   p_old_header_price_att_tbl   oe_order_pub.header_price_att_tbl_type := oe_order_pub.g_miss_header_price_att_tbl; 
   p_header_adj_att_tbl         oe_order_pub.header_adj_att_tbl_type := oe_order_pub.g_miss_header_adj_att_tbl; 
   p_old_header_adj_att_tbl     oe_order_pub.header_adj_att_tbl_type := oe_order_pub.g_miss_header_adj_att_tbl; 
   p_header_adj_assoc_tbl       oe_order_pub.header_adj_assoc_tbl_type := oe_order_pub.g_miss_header_adj_assoc_tbl; 
   p_old_header_adj_assoc_tbl   oe_order_pub.header_adj_assoc_tbl_type := oe_order_pub.g_miss_header_adj_assoc_tbl; 
   p_header_scredit_tbl         oe_order_pub.header_scredit_tbl_type := oe_order_pub.g_miss_header_scredit_tbl; 
   p_old_header_scredit_tbl     oe_order_pub.header_scredit_tbl_type := oe_order_pub.g_miss_header_scredit_tbl; 
   p_header_scredit_val_tbl     oe_order_pub.header_scredit_val_tbl_type := oe_order_pub.g_miss_header_scredit_val_tbl; 
   p_old_header_scredit_val_tbl oe_order_pub.header_scredit_val_tbl_type := oe_order_pub.g_miss_header_scredit_val_tbl; 
   x_line_tbl                   oe_order_pub.line_tbl_type := oe_order_pub.g_miss_line_tbl; 
   p_old_line_tbl               oe_order_pub.line_tbl_type := oe_order_pub.g_miss_line_tbl; 
   p_line_val_tbl               oe_order_pub.line_val_tbl_type := oe_order_pub.g_miss_line_val_tbl; 
   p_old_line_val_tbl           oe_order_pub.line_val_tbl_type := oe_order_pub.g_miss_line_val_tbl; 
   p_line_adj_tbl               oe_order_pub.line_adj_tbl_type := oe_order_pub.g_miss_line_adj_tbl; 
   p_old_line_adj_tbl           oe_order_pub.line_adj_tbl_type := oe_order_pub.g_miss_line_adj_tbl; 
   p_line_adj_val_tbl           oe_order_pub.line_adj_val_tbl_type := oe_order_pub.g_miss_line_adj_val_tbl; 
   p_old_line_adj_val_tbl       oe_order_pub.line_adj_val_tbl_type := oe_order_pub.g_miss_line_adj_val_tbl; 
   p_line_price_att_tbl         oe_order_pub.line_price_att_tbl_type := oe_order_pub.g_miss_line_price_att_tbl; 
   p_old_line_price_att_tbl     oe_order_pub.line_price_att_tbl_type := oe_order_pub.g_miss_line_price_att_tbl; 
   p_line_adj_att_tbl           oe_order_pub.line_adj_att_tbl_type := oe_order_pub.g_miss_line_adj_att_tbl; 
   p_old_line_adj_att_tbl       oe_order_pub.line_adj_att_tbl_type := oe_order_pub.g_miss_line_adj_att_tbl; 
   p_line_adj_assoc_tbl         oe_order_pub.line_adj_assoc_tbl_type := oe_order_pub.g_miss_line_adj_assoc_tbl; 
   p_old_line_adj_assoc_tbl     oe_order_pub.line_adj_assoc_tbl_type := oe_order_pub.g_miss_line_adj_assoc_tbl; 
   p_line_scredit_tbl           oe_order_pub.line_scredit_tbl_type := oe_order_pub.g_miss_line_scredit_tbl; 
   p_old_line_scredit_tbl       oe_order_pub.line_scredit_tbl_type := oe_order_pub.g_miss_line_scredit_tbl; 
   p_line_scredit_val_tbl       oe_order_pub.line_scredit_val_tbl_type := oe_order_pub.g_miss_line_scredit_val_tbl; 
   p_old_line_scredit_val_tbl   oe_order_pub.line_scredit_val_tbl_type := oe_order_pub.g_miss_line_scredit_val_tbl; 
   p_lot_serial_tbl             oe_order_pub.lot_serial_tbl_type := oe_order_pub.g_miss_lot_serial_tbl; 
   p_old_lot_serial_tbl         oe_order_pub.lot_serial_tbl_type := oe_order_pub.g_miss_lot_serial_tbl; 
   p_lot_serial_val_tbl         oe_order_pub.lot_serial_val_tbl_type := oe_order_pub.g_miss_lot_serial_val_tbl; 
   p_old_lot_serial_val_tbl     oe_order_pub.lot_serial_val_tbl_type := oe_order_pub.g_miss_lot_serial_val_tbl; 
   p_action_request_tbl         oe_order_pub.request_tbl_type := oe_order_pub.g_miss_request_tbl; 
   x_header_val_rec             oe_order_pub.header_val_rec_type; 
   x_header_adj_tbl             oe_order_pub.header_adj_tbl_type; 
   x_header_adj_val_tbl         oe_order_pub.header_adj_val_tbl_type; 
   x_header_price_att_tbl       oe_order_pub.header_price_att_tbl_type; 
   x_header_adj_att_tbl         oe_order_pub.header_adj_att_tbl_type; 
   x_header_adj_assoc_tbl       oe_order_pub.header_adj_assoc_tbl_type; 
   x_header_scredit_tbl         oe_order_pub.header_scredit_tbl_type; 
   x_header_scredit_val_tbl     oe_order_pub.header_scredit_val_tbl_type; 
   x_line_val_tbl               oe_order_pub.line_val_tbl_type; 
   x_line_adj_tbl               oe_order_pub.line_adj_tbl_type; 
   x_line_adj_val_tbl           oe_order_pub.line_adj_val_tbl_type; 
   x_line_price_att_tbl         oe_order_pub.line_price_att_tbl_type; 
   x_line_adj_att_tbl           oe_order_pub.line_adj_att_tbl_type; 
   x_line_adj_assoc_tbl         oe_order_pub.line_adj_assoc_tbl_type; 
   x_line_scredit_tbl           oe_order_pub.line_scredit_tbl_type; 
   x_line_scredit_val_tbl       oe_order_pub.line_scredit_val_tbl_type; 
   x_lot_serial_tbl             oe_order_pub.lot_serial_tbl_type; 
   x_lot_serial_val_tbl         oe_order_pub.lot_serial_val_tbl_type; 
   x_action_request_tbl         oe_order_pub.request_tbl_type; 
   x_debug_file                 VARCHAR2(100); 
   l_msg_index_out              NUMBER(10); 
   l_line_tbl_index             NUMBER; 
   l_header_id					oe_order_lines_all.header_id%type;
   l_line_id					  oe_order_lines_all.line_id%type;
   l_reserved_qty				oe_order_lines_all.ordered_quantity%type;
   CURSOR line_attributes IS
   SELECT b.header_id, b.line_id, b.ordered_quantity FROM oe_order_headers_all a
   INNER JOIN oe_order_lines_all b ON b.header_id = a.header_id
   WHERE a.order_number := order_number;
BEGIN 
   dbms_output.ENABLE(1000000); 
   oe_msg_pub.initialize; 
   oe_debug_pub.initialize; 
   /*****************INITIALIZE ENVIRONMENT*************************************/ 
mo_global.init('ONT');
mo_global.set_policy_context('S',101);
fnd_global.apps_initialize (1187,50988,660);

   --X_DEBUG_FILE := OE_DEBUG_PUB.Set_Debug_Mode('FILE');  
   --oe_debug_pub.SetDebugLevel(5);  
   --dbms_output.put_line('START OF NEW DEBUG');
   -- This is to reserve an order line 
   l_line_tbl_index :=1; 
   -- Initialize record to missing 
   l_line_tbl(l_line_tbl_index) := OE_ORDER_PUB.G_MISS_LINE_REC; 
   -- Line attributes 
   OPEN line_attributes; 
   LOOP 
	FETCH line_attributes into l_header_id, l_line_id, l_reserved_qty; 
      EXIT WHEN line_attributes%notfound; 
	   l_line_tbl(l_line_tbl_index).header_id := l_header_id; 
	   l_line_tbl(l_line_tbl_index).line_id := l_line_id; 
	   l_line_tbl(l_line_tbl_index).reserved_quantity := l_reserved_qty; 
	   l_line_tbl(l_line_tbl_index).operation := OE_GLOBALS.G_OPR_UPDATE; 
   END LOOP; 
   CLOSE line_attributes; 
 
   -- CALL TO PROCESS ORDER  
oe_order_pub.Process_order (  
	p_api_version_number   => 1.0, 
	p_init_msg_list        => fnd_api.g_false, 
	p_return_values        => fnd_api.g_false, 
	p_action_commit        => fnd_api.g_false, 
	x_return_status        => l_return_status, 
	x_msg_count            => l_msg_count, 
	x_msg_data             => l_msg_data, 
	p_header_rec           => l_header_rec, 
	p_line_tbl             => l_line_tbl, 
	p_action_request_tbl   => l_action_request_tbl, 
	-- OUT PARAMETERS  
	x_header_rec             => x_header_rec,
	x_header_val_rec         => x_header_val_rec,
	x_header_adj_tbl         => x_header_adj_tbl,
	x_header_adj_val_tbl     => x_header_adj_val_tbl, 
	x_header_price_att_tbl   => x_header_price_att_tbl, 
	x_header_adj_att_tbl     => x_header_adj_att_tbl,
	x_header_adj_assoc_tbl   => x_header_adj_assoc_tbl, 
	x_header_scredit_tbl     => x_header_scredit_tbl, 
	x_header_scredit_val_tbl => x_header_scredit_val_tbl, 
	x_line_tbl               => x_line_tbl, 
	x_line_val_tbl           => x_line_val_tbl, 
	x_line_adj_tbl           => x_line_adj_tbl, 
	x_line_adj_val_tbl       => x_line_adj_val_tbl, 
	x_line_price_att_tbl     => x_line_price_att_tbl, 
	x_line_adj_att_tbl       => x_line_adj_att_tbl, 
	x_line_adj_assoc_tbl     => x_line_adj_assoc_tbl, 
	x_line_scredit_tbl       => x_line_scredit_tbl, 
	x_line_scredit_val_tbl   => x_line_scredit_val_tbl, 
	x_lot_serial_tbl         => x_lot_serial_tbl, 
	x_lot_serial_val_tbl     => x_lot_serial_val_tbl, 
	x_action_request_tbl     => x_action_request_tbl
);  
   --dbms_output.put_line('OM Debug file: ' ||oe_debug_pub.G_DIR||'/'||oe_debug_pub.G_FILE);  
   -- Retrieve messages  
   --dbms_output.Put_line('Line Id: ' ||x_line_tbl(l_line_tbl_index).line_id); 
 
   FOR i IN 1 .. l_msg_count 
   LOOP 
	oe_msg_pub.Get (  
			p_msg_index     => i, 
			p_encoded       => fnd_api.g_false, 
      p_data          => l_msg_data, 
      p_msg_index_out => l_msg_index_out
    ); 
	customer_array(i) := l_msg_data;
   END LOOP; 
 
   
   -- Check the return status  
   IF l_return_status = fnd_api.g_ret_sts_success 
   THEN 
	COMMIT;
   ELSE 
    ROLLBACK;
   END IF; 

END; 