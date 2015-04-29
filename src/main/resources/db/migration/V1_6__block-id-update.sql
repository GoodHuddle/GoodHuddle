alter table block_id_mapping drop constraint block_id_mapping_page_id_fkey;

alter table block_id_mapping add constraint block_id_mapping_page_id_fkey
  foreign key (page_id) references page(id) on delete cascade;
