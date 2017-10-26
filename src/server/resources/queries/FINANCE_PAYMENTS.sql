select p.receipt_reference, date_format(p.created_on, '%d-%b-%Y') payment_date, pt.type_name, p.amount, tst.sales_reference, ae.ae_ref
from payment p, payment_type pt, test_slot_transaction tst, auth_for_ae ae
where p.receipt_reference is not null
and p.status_id = 1  -- successful payments only
and p.type = pt.id
and tst.payment_id = p.id
and ae.organisation_id = tst.organisation_id
limit 20