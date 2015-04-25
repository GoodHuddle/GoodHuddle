create table block_id_mapping
(
  id bigserial not null primary key,
  page_id bigint references page
);

alter table member add column post_code text;

