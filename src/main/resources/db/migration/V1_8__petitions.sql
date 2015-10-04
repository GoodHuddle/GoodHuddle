--------------------------------------------------------------------------------
-- petition
--------------------------------------------------------------------------------

create table petition
(
  id                        bigserial not null primary key,
  huddle_id                 bigint not null references huddle,
  name                      text,
  description               text,
  subject                   text,
  content                   text,
  thankyou_email_template   text,
  admin_email_addresses     text,
  admin_email_template      text,
  message                   text,
  created_on                timestamp with time zone,
  created_by                bigint references member
);

--------------------------------------------------------------------------------
-- petition_signature
--------------------------------------------------------------------------------

create table petition_signature
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  petition_id           bigint references petition,
  member_id             bigint references member not null,
  subject               text,
  message               text,
  created_on            timestamp with time zone,
  created_by            bigint references member
);
