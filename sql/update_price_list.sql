update qp_list_lines  set OPERAND = ?
where LIST_LINE_ID = (
SELECT spll.LIST_LINE_ID
FROM qp_list_headers_b spl,
qp_list_lines spll,
qp_pricing_attributes qpa,
mtl_system_items_b msi
WHERE msi.organization_id = ?
AND msi.inventory_item_id = ?
AND spl.list_header_id = ?
AND spll.list_header_id = spl.list_header_id
AND qpa.list_header_id = spl.list_header_id
AND spll.list_line_id = qpa.list_line_id
AND qpa.product_attribute_context = 'ITEM'
AND qpa.product_attribute = 'PRICING_ATTRIBUTE1'
AND qpa.product_attr_value = TO_CHAR (msi.inventory_item_id)
AND qpa.product_uom_code = msi.primary_uom_code
AND qpa.pricing_attribute_context IS NULL
AND qpa.excluder_flag = 'N'
AND qpa.pricing_phase_id = 1
)
