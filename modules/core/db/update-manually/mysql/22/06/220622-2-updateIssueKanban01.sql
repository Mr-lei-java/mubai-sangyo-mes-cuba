alter table SANGYO_ISSUE_KANBAN add constraint FK_SANGYO_ISSUE_KANBAN_ON_ISSUE_USER foreign key (ISSUE_USER_ID) references SEC_USER(ID);
create index IDX_SANGYO_ISSUE_KANBAN_ON_ISSUE_USER on SANGYO_ISSUE_KANBAN (ISSUE_USER_ID);
