
--------------------------------------------------------------------------------
-- huddle
--------------------------------------------------------------------------------

create table huddle
(
  id                          bigserial not null primary key,
  slug                        text not null,
  name                        text,
  base_url                    text,
  description                 text,
  created_on                  timestamp without time zone,
  setup_wizard_complete       boolean default false
);


--------------------------------------------------------------------------------
-- security_group
--------------------------------------------------------------------------------

create table security_group
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  name                  text,
  display_name          text,
  description           text,
  admin_access          boolean
);

--------------------------------------------------------------------------------
-- security_group_permission
--------------------------------------------------------------------------------

create table security_group_permission
(
  member_id             bigint not null references security_group on delete cascade,
  permission            text not null
);

--------------------------------------------------------------------------------
-- member
--------------------------------------------------------------------------------

create table member
(
  id                      bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  username                text,
  first_name              text,
  last_name	              text,
  email                   text,
  security_group_id       bigint references security_group on delete restrict,
  password_enc            text,
  huddle_owner            boolean,
  password_reset_code     text,
  password_reset_expiry   timestamp with time zone,
  last_login              timestamp with time zone,
  login_attempts          numeric default 0
);

--------------------------------------------------------------------------------
-- theme
--------------------------------------------------------------------------------

create table theme
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  name                  text,
  description           text,
  active                boolean default false
);

--------------------------------------------------------------------------------
-- theme_setting
--------------------------------------------------------------------------------

create table theme_setting
(
  id                        bigserial not null primary key,
  huddle_id                 bigint not null references huddle,
  field_code                text,
  value                     text
);


--------------------------------------------------------------------------------
-- layout
--------------------------------------------------------------------------------

create table layout
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  theme_id              bigint not null references theme on delete cascade,
  name                  text,
  type                  text
);

--------------------------------------------------------------------------------
-- menu
--------------------------------------------------------------------------------

create table menu
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  name                  text
);

--------------------------------------------------------------------------------
-- menu_item
--------------------------------------------------------------------------------

create table menu_item
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  menu_id               bigint not null references menu on delete restrict,
  parent_item_id        bigint references menu_item on delete restrict,
  position              int,
  label                 text,
  target_type           text,
  target_id             int,
  url                   text
);

--------------------------------------------------------------------------------
-- page
--------------------------------------------------------------------------------

create table page
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  menu_item_id          bigint references menu_item,
  title                 text,
  slug                  text,
  layout  	            text,
  content  	            text
);

--------------------------------------------------------------------------------
-- blog
--------------------------------------------------------------------------------

create table blog
(
  id                          bigserial not null primary key,
  huddle_id                   bigint not null references huddle,
  menu_item_id                bigint references menu_item,
  title                       text,
  slug                        text,
  layout  	                  text,
  default_post_layout  	      text,
  allow_comments              text,
  require_comment_approval    text
);

--------------------------------------------------------------------------------
-- blog_post
--------------------------------------------------------------------------------

create table blog_post
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  title                 text,
  slug                  text,
  layout  	            text,
  blog_id               bigint not null references blog on delete cascade,
  created_by            bigint not null references member on delete restrict,
  created_on            timestamp with time zone,
  published_on          timestamp with time zone,
  author_id             bigint references member,
  blurb  	              text,
  feat_img_available    boolean default false,
  content  	            text,
  comments_open         boolean
);

--------------------------------------------------------------------------------
-- blog_post_comment
--------------------------------------------------------------------------------

create table blog_post_comment
(
  id                 bigserial not null primary key,
  huddle_id          bigint not null references huddle,
  blog_post_id       bigint not null references blog_post on delete cascade,
  member_id          bigint references member on delete restrict,
  created_on         timestamp with time zone,
  display_name       text,
  comment            text,
  approved           boolean not null default false
);

--------------------------------------------------------------------------------
-- email_settings
--------------------------------------------------------------------------------

create table email_settings
(
  id                          bigserial not null primary key,
  huddle_id                   bigint not null references huddle,
  send_from_address           text,
  send_from_name              text,
  mandrill_api_key            text
);

--------------------------------------------------------------------------------
-- mailout
--------------------------------------------------------------------------------

create table mailout
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  name                  text,
  description           text,
  status                text,
  subject               text,
  content               text,
  created_on            timestamp with time zone,
  created_by            bigint references member,
  generated_on          timestamp with time zone,
  generated_by          bigint references member,
  sent_on               timestamp with time zone,
  sent_by               bigint references member
);

--------------------------------------------------------------------------------
-- email
--------------------------------------------------------------------------------

create table email
(
  id                    bigserial not null primary key,
  huddle_id             bigint not null references huddle,
  mailout_id            bigint references mailout,
  recipient_id          bigint references member not null,
  status                text,
  subject               text,
  content               text,
  error                 text,
  created_on            timestamp with time zone,
  created_by            bigint references member,
  sent_on               timestamp with time zone
);

--------------------------------------------------------------------------------
-- payment_settings
--------------------------------------------------------------------------------

create table payment_settings
(
  id                          bigserial not null primary key,
  huddle_id                   bigint not null references huddle,
  publishable_key             text,
  secret_key                  text
);

--------------------------------------------------------------------------------
-- Subscriptions
--------------------------------------------------------------------------------

create table subscription
(
  id                        bigserial not null primary key,
  huddle_id                 bigint not null references huddle,
  frequency                 text,
  status                    text,
  payment_type              text,
  stripe_customer_id        text,
  stripe_subscription_id    text,
  currency                  text,
  amount_in_cents           numeric,
  description               text,
  created_on                timestamp with time zone,
  member_id                 bigint references member,
  cancelled_on              timestamp without time zone,
  cancelled_by              bigint references member,
  next_payment_due          timestamp with time zone
);

--------------------------------------------------------------------------------
-- payment
--------------------------------------------------------------------------------

create table payment
(
  id                        bigserial not null primary key,
  huddle_id                 bigint not null references huddle,
  stripe_payment_id         text,
  currency                  text,
  amount_in_cents           numeric,
  fees_in_cents             numeric,
  description               text,
  paid_by_email             text,
  paid_on                   timestamp with time zone,
  paid_by                   bigint references member,
  stripe_balance_trans_id   text,
  subscription_id           bigint references subscription,
  type                      text,
  status                    text,
  refunded_on               timestamp without time zone,
  refunded_by               bigint references member
);

--------------------------------------------------------------------------------
-- tag
--------------------------------------------------------------------------------

create table tag
(
  id                        bigserial not null primary key,
  huddle_id                 bigint not null references huddle,
  name                      text,
  description               text
);

--------------------------------------------------------------------------------
-- member_tag
--------------------------------------------------------------------------------

create table member_tag
(
  tag_id             bigserial not null references tag,
  member_id          bigserial not null references member
);

--------------------------------------------------------------------------------
-- blog_post_tag
--------------------------------------------------------------------------------

create table blog_post_tag
(
  tag_id             bigserial not null references tag on delete restrict,
  blog_post_id       bigserial not null references blog_post on delete cascade
);



