create table page_revision
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  page_id               bigint references page,
  title                 text,
  slug                  text,
  layout  	            text,
  content  	            text,
  created_on            timestamp with time zone,
  created_by            bigint references member
);

