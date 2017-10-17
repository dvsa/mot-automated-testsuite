select p.username as username, cast(scd.last_observed_drift/30 as decimal(0)) as last_drift,
  question_1.question_text as question_1, question_2.question_text as question_2
from person p, security_card sc, person_security_card_map pscm, security_card_drift scd,
  person_security_question_map answer_1, person_security_question_map answer_2,
  security_question question_1, security_question question_2
where p.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and sc.id = scd.security_card_id
and p.username is not null -- exclude dodgy test data
and answer_1.id = ( -- each user has 2 questions, selects first question
  select max(psqm.id) from person_security_question_map psqm
  where psqm.person_id = p.id
)
and answer_1.answer = '$2y$10$.1QYAai1g2XTuGSPL3YAyOT.DJijVMA6k9m/se5jcdcCiQTdXzD9y' -- hash of 'answer'
and answer_1.security_question_id = question_1.id
and answer_2.id = ( -- each user has 2 questions, selects second question
  select min(psqm.id) from person_security_question_map psqm
  where psqm.person_id = p.id
)
and answer_2.answer = '$2y$10$.1QYAai1g2XTuGSPL3YAyOT.DJijVMA6k9m/se5jcdcCiQTdXzD9y' -- hash of 'answer'
and answer_2.security_question_id = question_2.id
order by p.id
limit 50