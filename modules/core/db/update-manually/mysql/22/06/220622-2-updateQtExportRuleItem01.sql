alter table SANGYO_QT_EXPORT_RULE_ITEM add constraint FK_SANGYO_QT_EXPORT_RULE_ITEM_ON_QT_EXPORT_RULES foreign key (QT_EXPORT_RULES_ID) references SANGYO_QT_EXPORT_RULES(ID);
create index IDX_SANGYO_QT_EXPORT_RULE_ITEM_ON_QT_EXPORT_RULES on SANGYO_QT_EXPORT_RULE_ITEM (QT_EXPORT_RULES_ID);
