alter table petition add column petition_email_template text;


--------------------------------------------------------------------------------
-- petition_target
--------------------------------------------------------------------------------

create table petition_target
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  petition_id           bigint references petition,
  name                  text,
  email                 text
);

